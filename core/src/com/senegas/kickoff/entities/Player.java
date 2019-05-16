package com.senegas.kickoff.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.senegas.kickoff.pitches.Pitch;

/**
 * Player entity class
 * @author Sébastien Sénégas
 *
 */
public class Player implements InputProcessor {


    /** Player constant direction */
//	public static final int NORTH = 0;
//	public static final int NORTH_EAST = 1;
//	public static final int EAST = 2;
//	public static final int SOUTH_EAST = 3;
//	public static final int SOUTH = 4;
//	public static final int SOUTH_WEST = 5;
//	public static final int WEST = 6;
//	public static final int NORTH_WEST = 7;
//	public static final int NONE = 8;

	public enum Direction { NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NONE }; // create an enum outside

	private final static int SPRITE_WIDTH = 16;
	private final static int SPRITE_HEIGHT = 16;

	private Vector3 position;
	private Vector3 velocity;
	private Circle bounds;
	private Direction direction = Direction.NONE;
    private Vector3 desiredPosition = new Vector3((int) (Pitch.PITCH_WIDTH_IN_PX / 2 + Pitch.OUTER_TOP_EDGE_X),
                                                (int) (Pitch.PITCH_HEIGHT_IN_PX / 2 + Pitch.OUTER_TOP_EDGE_Y + 16),
                                                0);
	private float speed = 200f;
	private int height = 177; // 1m 77
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
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer(); // mainly used for debug purpose
	
	/**
	 * Constructor
	 * @param x x-axis position
	 * @param y y-axis position
	 */
	public Player(int x, int y, Texture texture) {
		this.position = new Vector3(x, y, 0);
		this.velocity = new Vector3(0, 0, 0);
		this.texture = texture;
		this.frames = TextureRegion.split(texture, SPRITE_WIDTH, SPRITE_HEIGHT);
		this.bounds = new Circle(position.x, position.y, SPRITE_WIDTH/2);
	}
	
	/**
	 * Update the position and animation frame
	 * @param deltaTime The time in seconds since the last render.
	 */
	public void update(float deltaTime) {
		moveToDesiredPosition();

		if (direction != Direction.NONE) {			
			// update animation
			currentFrameTime += deltaTime;
			currentFrame = (int) (currentFrameTime / maxFrameTime) % frameCount;
			currentFrameAnimationRow = ((runningFrameAnimation[currentFrame] + 8 * direction.ordinal()) / 20);
			currentFrameAnimationColumn = ((runningFrameAnimation[currentFrame] + 8 * direction.ordinal()) % 20);
		}
		
		// update position			
		position.x += velocity.x * directionCoefficients[direction.ordinal()].x * deltaTime;
        position.y += velocity.y * directionCoefficients[direction.ordinal()].y * deltaTime;
		//velocity.scl(deltaTime);
		//position.add(velocity.x * directionCoefficients[direction].x, velocity.y * directionCoefficients[direction].y, 0);
		//velocity.scl(1/deltaTime);			
		
		// update bounds
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
	
	/**
	 * Draw the player frame into the batch at his current position
	 * @param batch the batch
	 */
	public void draw(Batch batch) {
//		System.out.format("runningFrame: %d%n", currentFrame);
//		System.out.format("currentFrameAnimationRow: %d currentFrameAnimationColumn: %d%n", currentFrameAnimationRow, currentFrameAnimationColumn);
//		System.out.format("directionCoefficients[direction].x %f%n", directionCoefficients[direction].x);
//		System.out.format("directionCoefficients[direction].y %f%n", directionCoefficients[direction].y);
	    
		// draw the frame
		batch.draw(frames[currentFrameAnimationRow][currentFrameAnimationColumn], position.x - SPRITE_WIDTH/2, position.y - SPRITE_HEIGHT/2);
	}
	
	public void showBounds(OrthographicCamera camera) {
		// enable transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(new Color(0, 0, 0, 0.5f));
		shapeRenderer.circle(bounds.x, bounds.y, bounds.radius);
		shapeRenderer.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	/**
	 * The player boundaries used to check collision
	 * @return Circle
	 */
	public Circle getBounds() {
		return bounds;
	}

    public void setDestination(Vector3 destination) {
	    this.desiredPosition = new Vector3(destination.x, destination.y, 0);
    }

    public boolean inPosition()
    {
        Vector3 currentDistance = new Vector3(this.position.x, this.position.y, 0);
        currentDistance.sub(this.desiredPosition);

        if (currentDistance.len() < 2.25) return true;
        return false;
    }
	
	/**
	 * Move the player to destination position
	 */
	public void moveToDesiredPosition()
	{
		Vector3 start = new Vector3(position.x, position.y, 0);
		float distance = start.dst(desiredPosition);
		
		boolean moving = true;
	    if (distance <= 2)
	    {
	    	moving = false;
			velocity.x = 0;
			velocity.y = 0;
	    }
		
		if (moving == true)
		{
			velocity.set(desiredPosition.x - position.x, desiredPosition.y - position.y, 0);
			velocity.nor(); // Normalizes the value to be used
			
			float fThreshold = (float) Math.cos(Math.PI / 8);
			 
			if (velocity.x > fThreshold) {
				velocity.x = speed;
				velocity.y = 0;
			}
			else if (velocity.x < -fThreshold) {
				velocity.x = -speed;
				velocity.y = 0;
			}
			else if (velocity.y > fThreshold) {
				velocity.x = 0;
				velocity.y = speed;
			}
			else if (velocity.y < -fThreshold) {
				velocity.x = 0;
				velocity.y = -speed;
			}
			else if (velocity.x > 0 && velocity.y > 0) {
				velocity.x = speed;
				velocity.y = speed;
			}
			else if (velocity.x > 0 && velocity.y < 0) {
				velocity.x = speed;
				velocity.y = -speed;
			}
			else if (velocity.x < 0 && velocity.y > 0) {
				velocity.x = -speed;
				velocity.y = speed;
			}
			else if (velocity.x < 0 && velocity.y < 0)
			{
				velocity.x = -speed;
				velocity.y = -speed;
			}
		}
		
		updateDirection();
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
	
	/**
	 * Get the player position
	 * @return the Vector3 player position
	 */
	public Vector3 getPosition() {
		return position;
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
            	velocity.x = 0.0f;
            	break;
            case Keys.UP:
            case Keys.DOWN:
            	velocity.y = 0.0f;
                break;    
            }
            
    		updateDirection();
            
            return true;
    }
    
    /**
     * Update the player direction according to its velocity
     */
	private void updateDirection() {
		if (velocity.y > 0) {
			if (velocity.x < 0) {
				direction = Direction.NORTH_WEST;
			} else if (velocity.x > 0) {
				direction = Direction.NORTH_EAST;
			} else {
				direction = Direction.NORTH;
			}
		} else if (velocity.y < 0) {
			if (velocity.x < 0) {
				direction = Direction.SOUTH_WEST;
			} else if (velocity.x > 0) {
				direction = Direction.SOUTH_EAST;
			} else {
				direction = Direction.SOUTH;
			}
		} else if (velocity.x < 0) {
			direction = Direction.WEST;
		} else if (velocity.x > 0) {
			direction = Direction.EAST;
		} else {
			direction = Direction.NONE;
		}
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
		return direction.ordinal();
	}

	public Texture getTexture() {
    	return texture;
    }

	public int height() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public float speed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public void dispose() {
		shapeRenderer.dispose();
	}
}
