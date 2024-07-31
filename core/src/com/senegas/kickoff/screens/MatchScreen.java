package com.senegas.kickoff.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.senegas.kickoff.KickOff;
import com.senegas.kickoff.entities.Ball;
import com.senegas.kickoff.entities.Player;
import com.senegas.kickoff.entities.Team;
import com.senegas.kickoff.pitches.Pitch;
import com.senegas.kickoff.pitches.PitchFactory;
import com.senegas.kickoff.pitches.Scanner;
import com.senegas.kickoff.scenes.Hud;
import com.senegas.kickoff.states.MatchState;
import com.senegas.kickoff.utils.CameraHelper;
import com.senegas.kickoff.utils.Direction;
import com.senegas.kickoff.utils.PitchUtils;
import com.senegas.kickoff.utils.Stopwatch;

import static com.senegas.kickoff.pitches.FootballDimensionConstants.CM_PER_PIXEL;
import static com.senegas.kickoff.states.MatchState.INPLAY;
import static com.senegas.kickoff.states.MatchState.THROW_IN;

/**
 * Match
 *
 * @author Sébastien Sénégas
 */
public class MatchScreen extends AbstractScreen {
    private static final String TAG = MatchScreen.class.getSimpleName();

    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private CameraHelper cameraHelper;
    private BitmapFont font;
    private SpriteBatch batch;

    private Pitch pitch;
    private Scanner scanner;
    private Ball ball;
    private Team home;
    private Team away;
    private Integer scoreHome;
    private Integer scoreAway;
    private Player lastTouch;
    private Hud hud;
    private Stopwatch stopwatch;

    public StateMachine<MatchScreen, MatchState> stateMachine;
    public Sound crowd;
    public Sound whistle;
    public Sound dribble;

    private static final boolean DEBUG = true;

//	private static float angx = 0;
//	private static float angy = 0;
//	private static float incx = 0.001f;
//	private static float incy = 0.0013f;

    public MatchScreen(KickOff app) {
        super(app);

        this.scoreHome = 0;
        this.scoreAway = 0;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, KickOff.V_WIDTH, KickOff.V_HEIGHT);
        app.batch.setProjectionMatrix(this.camera.combined);
        //app.shapeBatch.setProjectionMatrix(camera.combined);

        this.cameraHelper = new CameraHelper();
        this.cameraHelper.setZoom(.35f);


        //cameraController = new OrthoCamController(camera);
        //Gdx.input.setInputProcessor(player);

        this.font = new BitmapFont();
        this.stateMachine = new DefaultStateMachine<>(this);

        Gdx.app.log(TAG, "constructor");
    }

    @Override
    public void show() {
        this.pitch = PitchFactory.getInstance().make(this.app, Pitch.Type.PLAYERMANAGER);
        this.renderer = new OrthogonalTiledMapRenderer(this.pitch.getTiledMap(), this.app.batch);

        Vector2 centerSpot = this.pitch.getCenterSpot();
        this.ball = new Ball(this.app.assets.get("entities/ball.png"),
                new Vector3(centerSpot.x, centerSpot.y, 320));
        this.home = new Team(this, "Team A", Direction.NORTH);
        this.away = new Team(this, "Team B", Direction.SOUTH);

        this.hud = new Hud(this);
        this.stopwatch = new Stopwatch(3);

        this.scanner = new Scanner(this);

        this.crowd = this.app.assets.get("sounds/crowd.ogg");
        this.whistle = this.app.assets.get("sounds/whistle.ogg");
        this.dribble = this.app.assets.get("sounds/dribble.ogg");

        this.stateMachine.changeState(MatchState.INTRODUCTION);
    }

    @Override
    public void update(float deltaTime) {
        handleInput();

        this.stateMachine.update();
        this.home.update(deltaTime);
        this.away.update(deltaTime);
        this.ball.update(deltaTime);

        checkCollisions();

        this.cameraHelper.update(deltaTime);
        this.cameraHelper.applyTo(this.camera);

        this.hud.update(deltaTime);
        this.stopwatch.update(deltaTime);
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        this.renderer.setView(this.camera);
        this.renderer.render();

        // renders overlays for tactic and ball position
        this.app.shapeBatch.setProjectionMatrix(this.camera.combined);
        this.home.showDebug(this.app.shapeBatch, this.ball.getPosition());
        //this.ball.showPosition(this.app.shapeBatch);

        // renders entities
        this.app.batch.begin();

        this.home.draw(this.app.batch);
        this.away.draw(this.app.batch);
        this.ball.render(this.app.batch);

        // renders stopwatch
        this.app.batch.setProjectionMatrix(this.hud.stage.getCamera().combined);
        this.stopwatch.draw(this.app.batch);

        if (DEBUG) {
            displayDebugInfo();
        }

        this.app.batch.end();

        this.app.shapeBatch.setProjectionMatrix(this.app.batch.getProjectionMatrix());
        this.scanner.draw(this.app.shapeBatch);

        //app.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //this.hud.stage.draw();
    }

    private void displayDebugInfo() {
        this.app.batch.setProjectionMatrix(this.hud.stage.getCamera().combined);

        Player player = this.home.getPlayers().get(0);
        Vector2 ballLocation = PitchUtils.globalToPitch(this.ball.getPosition().x, this.ball.getPosition().y);
        ballLocation = new Vector2(this.ball.getPosition().x, this.ball.getPosition().y);

        this.font.draw(this.app.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        this.font.draw(this.app.batch, "Player: " + (int) player.getPosition().x + ", " +
                                          (int) player.getPosition().y, 10, 40);

        this.font.draw(this.app.batch, "Ball: " + (int) ballLocation.x + ", " +
                                        (int) ballLocation.y + ", " +
                                        (int) this.ball.getPosition().z, 10, 60);

        this.font.draw(this.app.batch, "Ball Last Pos.: " + (int) this.ball.getLastPosition().x + ", " +
                (int) this.ball.getLastPosition().y + ", " +
                (int) this.ball.getLastPosition().z, 10, 80);

        this.font.draw(this.app.batch, this.home.getTactic().getName(), 10, 100);
        this.font.draw(this.app.batch, this.stateMachine.getCurrentState().toString(), 10, 120);
    }

    public void setLastPlayerTouch(Player player) {
        this.lastTouch = player;
    }

    public Player getLastPlayerTouch() {
        return this.lastTouch;
    }

    public Team getLastTeamTouch() {
        if (this.lastTouch == null) {
            return null;
        }
        return this.lastTouch.getTeam();
    }

    /**
     * Check collisions
     */
    private void checkCollisions() {
        if (this.stateMachine.getCurrentState() == INPLAY) {
            for (Player player : this.home.getPlayers()) {
                if (player.getBounds().contains(this.ball.getPosition().x, this.ball.getPosition().y)) {
                    setLastPlayerTouch(player);
                    if (this.ball.getPosition().z < player.height() / CM_PER_PIXEL) { //!Reimp move constant elsewhere
                        Vector3 dv = player.getDirection().getDirectionVector();
                        Vector3 velocity = dv.scl(player.speed() * 1.125f + 30.0f);
                        velocity.z = 80;
                        ball.kick(velocity, player);
                        this.dribble.play(0.2f);
                    }
                }
            }
            if (this.pitch.crossesSideLine(this.ball.getLastPosition(), this.ball.getPosition())) {
                this.stateMachine.changeState(THROW_IN);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        this.camera.viewportHeight = height;
        this.camera.viewportWidth = width;
    }

    public Integer getScoreHome() {
        return this.scoreHome;
    }

    public Integer getScoreAway() {
        return this.scoreAway;
    }

    public Team getHomeTeam() {
        return this.home;
    }

    public Team getAwayTeam(){
        return this.away;
    }

    public Ball getBall() {
        return this.ball;
    }

    public Pitch getPitch() {
        return this.pitch;
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public CameraHelper getCameraHelper() {
        return this.cameraHelper;
    }

    @Override
    public void hide() {
        Gdx.app.log(TAG, "hide");
        //dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        super.dispose();
        Gdx.app.log(TAG, "dispose");

        this.renderer.dispose();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            this.cameraHelper.setZoom(this.cameraHelper.getZoom() + 0.02f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.I)) {
            this.cameraHelper.setZoom(this.cameraHelper.getZoom() - 0.02f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            Vector3 dv = Direction.WEST.getDirectionVector();
            Vector3 velocity = dv.scl(400 * 1.125f + 30.0f);
            velocity.z = 200;
            this.ball.kick(velocity, null);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            Vector3 dv = Direction.EAST.getDirectionVector();
            Vector3 velocity = dv.scl(400 * 1.125f + 30.0f);
            velocity.z = 200;
            this.ball.kick(velocity, null);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            Vector3 dv = Direction.NORTH.getDirectionVector();
            Vector3 velocity = dv.scl(400 * 1.125f + 30.0f);
            velocity.z = 200;
            this.ball.kick(velocity, null);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            Vector3 dv = Direction.SOUTH.getDirectionVector();
            Vector3 velocity = dv.scl(400 * 1.125f + 30.0f);
            velocity.z = 200;
            this.ball.kick(velocity, null);
        }
        // handle scanner zoom
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            this.scanner.toggleZoom();
        }

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            this.home.getPlayers().get(0).setDestination(touchPos);
        }
//        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//                if (camera.position.x > 0 + camera.viewportWidth)
//                	camera.translate(-3, 0, 0);
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//                if (camera.position.x < Pitch.WIDTH - camera.viewportWidth)
//                	camera.translate(3, 0, 0);
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//                if (camera.position.y > 0 + camera.viewportHeight)
//                	camera.translate(0, -3, 0);
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
//                if (camera.position.y < Pitch.HEIGHT - camera.viewportHeight)
//                	camera.translate(0, 3, 0);
//        }
    }
}
