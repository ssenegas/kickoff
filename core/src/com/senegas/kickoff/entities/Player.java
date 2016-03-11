package com.senegas.kickoff.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.senegas.kickoff.pitches.Pitch;

public class Player implements InputProcessor {

	/** Player constant direction */
	public static final int NORTH = 0;
	public static final int NORTH_EAST = 1;
	public static final int EAST = 2;
	public static final int SOUTH_EAST = 3;
	public static final int SOUTH = 4;
	public static final int SOUTH_WEST = 5;
	public static final int WEST = 6;
	public static final int NORTH_WEST = 7;
	public static final int NONE = 8;
	
	private final int WIDTH = 16;
	private final int HEIGHT = 16;
	
	private Vector3 position;
	private Vector3 velocity;
	private Circle bounds;
	private int direction = NONE;
	private float speed = 200f;
	private Texture texture;
	private TextureRegion frames[][];
	private int currentFrameAnimationRow = 0;
	private int currentFrameAnimationColumn = 0;
	private float currentFrameTime = 0.0f;
	private float maxFrameTime = 5 / speed; // max time between each frame
	private int runningFrameAnimation[] = { 0, 3, 2, 1, 1, 2, 3, 4, 7, 6, 5, 5, 6, 7 };
	private int frameCount = 14;	
	private int currentFrame = 0;
	private Vector2 directionCoefficients[] = { new Vector2(0, 1f),
			                                    new Vector2(0.707f, 0.707f),
			                                    new Vector2(1f, 0),
			                                    new Vector2(0.707f, 0.707f),
			                                    new Vector2(0, 1f),
			                                    new Vector2(0.707f, 0.707f),
			                                    new Vector2(1f, 0),
			                                    new Vector2(0.707f, 0.707f),
			                                    new Vector2(0, 0) };
	
	public Player(int x, int y) {
		position = new Vector3(x, y, 0);
		velocity = new Vector3(0, 0, 0);
		texture = new Texture("entities/style1a.png");
		frames = TextureRegion.split(texture, WIDTH, HEIGHT);
		bounds = new Circle(position.x, position.y, WIDTH/2);
	}
	
	public void update(float deltaTime) {	
		// update animation
		if (direction != NONE) {
			currentFrameTime += deltaTime;
			currentFrame = (int) (currentFrameTime / maxFrameTime) % frameCount;

			currentFrameAnimationRow = ((runningFrameAnimation[currentFrame] + 8 * direction) / 20);
			currentFrameAnimationColumn = ((runningFrameAnimation[currentFrame] + 8 * direction) % 20);
		}
			
		// update position
		velocity.scl(deltaTime);
		position.add(velocity.x * directionCoefficients[direction].x, velocity.y * directionCoefficients[direction].y, 0);
		velocity.scl(1/deltaTime);
		bounds.setPosition(position.x,  position.y);

		//adjustVelocity();
		
		// stop at border of map
//		if (x < 0) {
//			x = 0;
//			velocity.x = 0.0f;
//		} else if (x > Pitch.WIDTH - WIDTH) {
//			x = Pitch.WIDTH - WIDTH;
//			velocity.x = 0.0f;
//		}
//		
//		if (y < 0) {
//			y = 0;
//			velocity.y = 0.0f;
//		} else if (y > Pitch.HEIGHT - HEIGHT) {
//			y = Pitch.HEIGHT - HEIGHT;
//			velocity.y = 0.0f;
//		}
	}
	
	public Circle getBounds() {
		return bounds;
	}
	
//	private void adjustVelocity() {
//		// adjust diagonal velocity
//		if (velocity.x != 0 && velocity.y != 0) {
//			if (velocity.x < 0)
//				velocity.x = (float) (-0.5f * Math.sqrt(2*speed*speed));
//			else
//				velocity.x = (float) (0.5f * Math.sqrt(2*speed*speed));
//			
//			if (velocity.y < 0)
//				velocity.y = (float) (-0.5f * Math.sqrt(2*speed*speed));
//			else
//				velocity.y = (float) (0.5f * Math.sqrt(2*speed*speed));
//		}
//	}
	
	private void updateDirection() {
		if (velocity.y > 0) {
			if (velocity.x < 0) {
				direction = NORTH_WEST;
			} else if (velocity.x > 0) {
				direction = NORTH_EAST;
			} else {
				direction = NORTH;
			}
		} else if (velocity.y < 0) {
			if (velocity.x < 0) {
				direction = SOUTH_WEST;
			} else if (velocity.x > 0) {
				direction = SOUTH_EAST;
			} else {
				direction = SOUTH;
			}
		} else if (velocity.x < 0) {
			direction = WEST;
		} else if (velocity.x > 0) {
			direction = EAST;
		} else {
			direction = NONE;
		}
	}
	
	public Vector3 getPosition() {
		return position;
	}

	public void draw(Batch batch) {
//		System.out.format("runningFrame: %d%n", currentFrame);
//		System.out.format("currentFrameAnimationRow: %d currentFrameAnimationColumn: %d%n", currentFrameAnimationRow, currentFrameAnimationColumn);
//		System.out.format("directionCoefficients[direction].x %f%n", directionCoefficients[direction].x);
//		System.out.format("directionCoefficients[direction].y %f%n", directionCoefficients[direction].y);
	    
		// draw the frame
		batch.draw(frames[currentFrameAnimationRow][currentFrameAnimationColumn], position.x, position.y);
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	@Override
    public boolean keyDown(int keycode) {
            switch(keycode) {
            case Keys.LEFT:
                velocity.x = -speed;
                break;
            case Keys.RIGHT:
                velocity.x = speed;
                break;
            case Keys.UP:
                velocity.y = speed;
                break;
            case Keys.DOWN:
                velocity.y = -speed;
                break;        
            }
            
    		updateDirection();
            
            return true;
    }

    @Override
    public boolean keyUp(int keycode) {
            switch(keycode) {
            case Keys.LEFT:
            case Keys.RIGHT:
            	velocity.x = 0;
            	break;
            case Keys.UP:
            case Keys.DOWN:
            	velocity.y = 0;
                break;    
            }
            
    		updateDirection();
            
            return true;
    }

    @Override
    public boolean keyTyped(char character) {
            return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
            return false;
    }

    @Override
    public boolean scrolled(int amount) {
            return false;
    }
    
    public int getDirection() {
		return direction;
	}

	public Texture getTexture() {
    	return texture;
    }
}
