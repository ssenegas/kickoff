package com.senegas.kickoff.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;
import com.senegas.kickoff.entities.Ball;
import com.senegas.kickoff.entities.Player;
import com.senegas.kickoff.entities.Player.Direction;
import com.senegas.kickoff.entities.Team;
import com.senegas.kickoff.pitches.Pitch;
import com.senegas.kickoff.pitches.PitchFactory;
import com.senegas.kickoff.pitches.Scanner;
import com.senegas.kickoff.states.MatchState;
import com.senegas.kickoff.utils.CameraHelper;
import com.senegas.kickoff.utils.PitchUtils;

import static com.badlogic.gdx.math.Intersector.intersectLines;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.*;
//import static com.senegas.kickoff.states.MatchState.INPLAY;
import static com.senegas.kickoff.states.MatchState.INPLAY;
import static com.senegas.kickoff.states.MatchState.THROW_IN;

/**
 * Match
 *
 * @author Sébastien Sénégas
 */
public class Match implements Screen {
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
    private Player lastTouch;

    public ShapeRenderer shapeRenderer;

    public StateMachine<Match, MatchState> stateMachine;
    public Sound crowd;
    public Sound whistle;

    private static final boolean DEBUG = true;

//	private static float angx = 0;
//	private static float angy = 0;
//	private static float incx = 0.001f;
//	private static float incy = 0.0013f;

    public Match() {
        pitch = PitchFactory.getInstance().make(Pitch.Type.PLAYERMANAGER);
        renderer = new OrthogonalTiledMapRenderer(pitch.getTiledMap());

        camera = new OrthographicCamera();
        //camera.setToOrtho(true);
        cameraHelper = new CameraHelper();
        cameraHelper.setZoom(.45f);

        Vector2 centerSpot = pitch.getCenterSpot();
        ball = new Ball(new Vector3(centerSpot.x, centerSpot.y, 160));
        home = new Team(this, "TeamA", Direction.NORTH);
        away = new Team(this, "TeamB", Direction.SOUTH);

        scanner = new Scanner(this);

        crowd =  Gdx.audio.newSound(Gdx.files.internal("sounds/crowd.ogg"));
        whistle = Gdx.audio.newSound(Gdx.files.internal("sounds/whistle.ogg"));
        //cameraController = new OrthoCamController(camera);
        //Gdx.input.setInputProcessor(player);

        font = new BitmapFont();
        batch = new SpriteBatch();
        stateMachine = new DefaultStateMachine<Match, MatchState>(this);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        stateMachine.changeState(MatchState.INTRODUCTION);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateMachine.update();
        home.update(deltaTime);
        away.update(deltaTime);
        ball.update(deltaTime);

        handleInput();

        cameraHelper.update(deltaTime);

        checkCollisions();
//        boolean gameIsRunning = true;
//
//    	if(!gameIsRunning) {
//    		double cx = (Pitch.PITCH_WIDTH_IN_PX/2 + Pitch.OUTER_TOP_EDGE_X - 8);
//    		double cy = (Pitch.PITCH_HEIGHT_IN_PX/2 + Pitch.OUTER_TOP_EDGE_Y + 8) - (camera.viewportHeight/2);
//
//    		camera.position.x = (float) MathUtils.clamp( cx + ((Pitch.PITCH_WIDTH_IN_PX/2) * Math.sin(angx)), camera.viewportWidth/2 * camera.zoom, Pitch.WIDTH - camera.viewportWidth/2 * camera.zoom);
//    		camera.position.y = (float) MathUtils.clamp( cy + ((Pitch.PITCH_HEIGHT_IN_PX/2) * Math.cos(angy)), camera.viewportHeight/2 * camera.zoom, Pitch.HEIGHT - camera.viewportHeight/2 * camera.zoom);
//
//    		angx += incx;
//    		angy += incy;
//    	}
//    	else
//    	{

//    	}
        cameraHelper.applyTo(camera);

        renderer.setView(camera);
        renderer.render();
        renderer.getBatch().begin();
        home.draw(renderer.getBatch());
        away.draw(renderer.getBatch());
        ball.draw(renderer.getBatch());
        renderer.getBatch().end();
        scanner.draw();

        if (DEBUG) {
            displayDebugInfo();
        }
    }

    private void displayDebugInfo() {
        this.home.showDebug();
        this.ball.showPosition(this.camera);

        Player player = this.home.getPlayers().get(0);
        Vector2 ballLocation = PitchUtils.globalToPitch(ball.getPosition().x, ball.getPosition().y);
        ballLocation = new Vector2(ball.getPosition().x, ball.getPosition().y);

        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        font.draw(batch, "Player: " + (int) player.getPosition().x + ", " +
                                          (int) player.getPosition().y, 10, 40);

        font.draw(batch, "Ball: " + (int) ballLocation.x + ", " +
                                        (int) ballLocation.y + ", " +
                                        (int) ball.getPosition().z, 10, 60);

        font.draw(batch, "Ball Last Pos.: " + (int) ball.getLastPosition().x + ", " +
                (int) ball.getLastPosition().y + ", " +
                (int) ball.getLastPosition().z, 10, 80);

        font.draw(batch, this.home.getTactic().getName(), 10, 100);
        font.draw(batch, this.stateMachine.getCurrentState().toString(), 10, 120);
        batch.end();
    }

    public void setLastPlayerTouch(Player player) {
        lastTouch = player;
    }

    public Player getLastPlayerTouch() {
        return lastTouch;
    }

    public Team getLastTeamTouch() {
        if (lastTouch == null) return null;
        return lastTouch.getTeam();
    }

    /**
     * Check collisions
     */
    private void checkCollisions() {
        if (stateMachine.getCurrentState() == INPLAY) {
            for (Player player : home.getPlayers()) {
                if (player.getBounds().contains(ball.getPosition().x, ball.getPosition().y)) {
                    this.setLastPlayerTouch(player);
                    if (ball.getPosition().z < player.height() / CM_PER_PIXEL) { //!Reimp move constant elsewhere
                        ball.applyForce(player.speed() * 1.125f + 30.0f, player.getDirection());
                    }
                }
            }
            if (pitch.crossesSideLine(ball.getLastPosition(), ball.getPosition())) {
                stateMachine.changeState(THROW_IN);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
    }

    public Team getHomeTeam() {
        return home;
    }

    public Team getAwayTeam(){
        return away;
    }

    public Ball getBall() {
        return ball;
    }

    public Pitch getPitch() {
        return pitch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public CameraHelper getCameraHelper() {
        return cameraHelper;
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        renderer.dispose();
        pitch.dispose();
        home.dispose();
        away.dispose();
        ball.dispose();
        shapeRenderer.dispose();
        crowd.dispose();
        whistle.dispose();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            cameraHelper.setZoom(cameraHelper.getZoom() + 0.02f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.I)) {
            cameraHelper.setZoom(cameraHelper.getZoom() - 0.02f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            ball.applyForce(400, 6);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            ball.applyForce(400, 2);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            ball.applyForce(400, 0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            ball.applyForce(400, 4);
        }
        // handle scanner zoom
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            scanner.toggleZoom();
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
