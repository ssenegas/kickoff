package com.senegas.kickoff.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.senegas.kickoff.entities.Ball;
import com.senegas.kickoff.entities.Player;
import com.senegas.kickoff.pitches.ClassicPitch;
import com.senegas.kickoff.pitches.Pitch;
import com.senegas.kickoff.pitches.PlayerManagerPitch;
import com.senegas.kickoff.pitches.SoggyPitch;
import com.senegas.kickoff.pitches.SyntheticPitch;
import com.senegas.kickoff.pitches.WetPitch;
import com.senegas.kickoff.utils.Joystick;
import com.senegas.kickoff.utils.OrthoCamController;

public class Match implements Screen {
	private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private BitmapFont font;
	private SpriteBatch batch;
	private Pitch pitch;
	private Ball ball;
	private Player player;
	
	@Override
	public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   
        
        player.update(deltaTime);
        ball.update(deltaTime);
        
        checkCollisions();
        
        camera.position.x = MathUtils.clamp(ball.getPosition().x, camera.viewportWidth/2 * camera.zoom, Pitch.WIDTH - camera.viewportWidth/2 * camera.zoom);
        camera.position.y = MathUtils.clamp(ball.getPosition().y, camera.viewportHeight/2 * camera.zoom, Pitch.HEIGHT - camera.viewportHeight/2 * camera.zoom);
        camera.update();
        
        renderer.setView(camera);
		renderer.render();
		
        renderer.getBatch().begin();
        player.draw(renderer.getBatch());
        ball.draw(renderer.getBatch());
        renderer.getBatch().end();
		
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		font.draw(batch, "Player: " + player.getPosition().x + ", " + player.getPosition().y, 10, camera.viewportHeight - 10);
		batch.end(); 

		handleInput();
	}
	
	private void checkCollisions() {
		if (player.getBounds().contains(ball.getPosition().x, ball.getPosition().y)) {
			//System.out.format("collision%n");
			ball.move(200 * 1.125 + 30, player.getDirection());
		}
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
	}

	@Override
	public void show() {
		pitch = new ClassicPitch();
		renderer = new OrthogonalTiledMapRenderer(pitch.getTiledMap());
        camera = new OrthographicCamera();
        //camera.setToOrtho(true);
        camera.zoom = .45f;
        
        player = new Player(Pitch.WIDTH/3, Pitch.HEIGHT/2);
        ball = new Ball(Pitch.WIDTH/2, Pitch.HEIGHT/2, 400);
        
        //cameraController = new OrthoCamController(camera);
		Gdx.input.setInputProcessor(player);
		
		font = new BitmapFont();
		batch = new SpriteBatch();
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
		pitch.dispose();
		renderer.dispose();
		player.getTexture().dispose();
		ball.getTexture().dispose();
	}
	
	private void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.O)) {
                camera.zoom += 0.02;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.I)) {
        	camera.zoom -= 0.02;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
        	ball.move(400, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
        	ball.move(400, 2);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.E)) {
        	ball.move(400, 6);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.X)) {
        	ball.move(400, 4);
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
