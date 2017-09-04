package com.senegas.kickoff.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.senegas.kickoff.entities.Ball;
import com.senegas.kickoff.entities.Player;
import com.senegas.kickoff.entities.Team;
import com.senegas.kickoff.pitches.ClassicPitch;
import com.senegas.kickoff.pitches.FootballDimensions;
import com.senegas.kickoff.pitches.Pitch;
import com.senegas.kickoff.pitches.Scanner;

public class Match implements Screen {
	private OrthogonalTiledMapRenderer renderer;
    public OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
	private SpriteBatch batch;
	private Pitch pitch;
	private Ball ball;
	private Scanner scanner;
	private Team teamA;
	
	private static final boolean DEBUG = true;
	
//	private static float angx = 0;
//	private static float angy = 0;
//	private static float incx = 0.001f;
//	private static float incy = 0.0013f;
	
	@Override
	public void show() {
		pitch = new ClassicPitch();
		renderer = new OrthogonalTiledMapRenderer(pitch.getTiledMap());
        camera = new OrthographicCamera();
        shapeRenderer = new ShapeRenderer();
        //camera.setToOrtho(true);
        camera.zoom = .45f;
        
        teamA = new Team(this);
        ball = new Ball((int) (Pitch.PITCH_WIDTH_IN_PX/2 + Pitch.OUTER_TOP_EDGE_X - 8),
        		        (int) (Pitch.PITCH_HEIGHT_IN_PX/2 + Pitch.OUTER_TOP_EDGE_Y + 8), 160);        
        scanner = new Scanner(this);
           
        //cameraController = new OrthoCamController(camera);
		//Gdx.input.setInputProcessor(player);
		
		font = new BitmapFont();
		batch = new SpriteBatch();
	}
	
	@Override
	public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   
        
        teamA.update(deltaTime);
        ball.update(deltaTime);
        
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
    		camera.position.x = MathUtils.clamp(ball.getPosition().x, camera.viewportWidth/2 * camera.zoom, Pitch.WIDTH - camera.viewportWidth/2 * camera.zoom);
    		camera.position.y = MathUtils.clamp(ball.getPosition().y, camera.viewportHeight/2 * camera.zoom, Pitch.HEIGHT - camera.viewportHeight/2 * camera.zoom);
//    	}
        camera.update();
        renderer.setView(camera);
		renderer.render();
		
        renderer.getBatch().begin();
        teamA.draw(renderer.getBatch());
        ball.draw(renderer.getBatch());
        renderer.getBatch().end();
		
        scanner.draw(shapeRenderer);
        
        if (DEBUG)
        {
			teamA.tactic().showRegionAndExpectedPlayerLocation(camera, ball);

			// debug information
			Player player = teamA.members().get(0);
			batch.begin();
			font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
			font.draw(batch, "Player: " +
					          (int) player.getPosition().x + ", " +
					          (int) player.getPosition().y, 10, 40);
			font.draw(batch, "Ball: " +
					         (int) ball.getPosition().x + ", " +
					         (int) ball.getPosition().y + ", " +
					         (int) ball.getPosition().z, 10, 60);
			//font.draw(batch, tactic.getName(), 10, 80);
			batch.end();
		}

		handleInput();
	}
	
	private void checkCollisions() {
		Player player = teamA.members().get(0);
		
		if (player.getBounds().contains(ball.getPosition().x, ball.getPosition().y)) {
			if (ball.getPosition().z < player.height()/FootballDimensions.CM_PER_PIXEL) { //!Reimp move constant elsewhere
				//System.out.format("collide%n");
				ball.dribble(player.speed() * 1.125f + 30.0f, player.getDirection());
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
	}
	
	public Team teamA() {
		return teamA;
	}
	
	public Ball ball()
	{
		return ball;
	}
	
	public Pitch pitch() {
		return pitch;
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
		teamA.dispose();
		ball.getTexture().dispose();
		shapeRenderer.dispose();
	}
	
	private void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.O)) {
                camera.zoom += 0.02;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.I)) {
        	camera.zoom -= 0.02;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
        	ball.dribble(400, 6);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
        	ball.dribble(400, 2);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
        	ball.dribble(400, 0);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
        	ball.dribble(400, 4);
        }    
        // handle scanner zoom
        if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
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
