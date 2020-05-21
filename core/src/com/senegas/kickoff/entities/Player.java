package com.senegas.kickoff.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_TOP_EDGE_X;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_TOP_EDGE_Y;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.PITCH_HEIGHT_IN_PX;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.PITCH_WIDTH_IN_PX;

/**
 * Player entity class
 * @author Sébastien Sénégas
 *
 */
public class Player extends Entity implements Disposable, InputProcessor {


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

    public static final int SPRITE_WIDTH = 16;
    public static final int SPRITE_HEIGHT = 16;

	public enum Direction {NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NONE} // create an enum outside


    private Circle bounds;
    private Direction direction = Direction.NONE;
    private final static int FRAME_COUNT = 14;
    private Vector3 desiredPosition = new Vector3((int) (PITCH_WIDTH_IN_PX / 2 + OUTER_TOP_EDGE_X),
                                                (int) (PITCH_HEIGHT_IN_PX / 2 + OUTER_TOP_EDGE_Y + 16),
                                                0);
	private float speed = 200f;
	private int height = 177; // 1m 77
	private int currentFrameAnimationRow = 0;
	private int currentFrameAnimationColumn = 0;
	private float currentFrameTime = 0.0f;
	private float maxFrameTime = 5 / this.speed; // max time between each frame
	private int runningFrameAnimation[] = { 0, 3, 2, 1, 1, 2, 3, 4, 7, 6, 5, 5, 6, 7 };
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
	private Team team;
	
	/**
	 * Constructor
	 * @param texture texture of the player
	 * @param position position of the player
	 */
	public Player(Texture texture, Team team, Vector3 position) {
		super(texture, position);
		this.frames = TextureRegion.split(texture, SPRITE_WIDTH, SPRITE_HEIGHT);
		this.bounds = new Circle(position.x, position.y, SPRITE_WIDTH / 2);
		this.team = team;
	}
	
	/**
	 * Update the position and animation frame
	 * @param deltaTime The time in seconds since the last render.
	 */
	@Override
    public void update(float deltaTime) {
		moveToDesiredPosition();

		if (this.direction != Direction.NONE) {
			// update animation
			this.currentFrameTime += deltaTime;
			this.currentFrame = (int) (this.currentFrameTime / this.maxFrameTime) % FRAME_COUNT;
			this.currentFrameAnimationRow = ((this.runningFrameAnimation[this.currentFrame] + 8 * this.direction.ordinal()) / 20);
			this.currentFrameAnimationColumn = ((this.runningFrameAnimation[this.currentFrame] + 8 * this.direction.ordinal()) % 20);
		}
		
		// update position			
		this.position.x += this.velocity.x * this.directionCoefficients[this.direction.ordinal()].x * deltaTime;
		this.position.y += this.velocity.y * this.directionCoefficients[this.direction.ordinal()].y * deltaTime;
		//velocity.scl(deltaTime);
		//position.add(velocity.x * directionCoefficients[direction].x, velocity.y * directionCoefficients[direction].y, 0);
		//velocity.scl(1/deltaTime);			
		
		// update bounds
		this.bounds.setPosition(this.position.x, this.position.y);

		//adjustVelocity();
		
		// stop at border of map
//		if (x < 0) {
//			x = 0;
//			velocity.x = 0.0f;
//		} else if (x > WIDTH - WIDTH) {
//			x = WIDTH - WIDTH;
//			velocity.x = 0.0f;
//		}
//		
//		if (y < 0) {
//			y = 0;
//			velocity.y = 0.0f;
//		} else if (y > HEIGHT - HEIGHT) {
//			y = HEIGHT - HEIGHT;
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
		batch.draw(this.frames[this.currentFrameAnimationRow][this.currentFrameAnimationColumn], this.position.x - SPRITE_WIDTH / 2, this.position.y - SPRITE_HEIGHT / 2);
	}

	public void showBounds(ShapeRenderer shapeBatch) {
		// enable transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shapeBatch.begin(ShapeType.Line);

		shapeBatch.setColor(new Color(0, 0, 0, 0.5f));
		shapeBatch.circle(this.bounds.x, this.bounds.y, this.bounds.radius);

		shapeBatch.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	/**
	 * The player boundaries used to check collision
	 * @return Circle
	 */
	public Circle getBounds() {
		return this.bounds;
	}

    public void setDestination(Vector3 destination) {
	    this.desiredPosition = new Vector3(destination.x, destination.y, 0);
    }

	public Vector3 getDestination() {
		return this.desiredPosition;
	}

    public boolean inPosition()
    {
        Vector3 currentDistance = new Vector3(this.position.x, this.position.y, 0);
        currentDistance.sub(this.desiredPosition);

		if (currentDistance.len() < 2.25) {
			return true;
		}
        return false;
    }
	
	/**
	 * Move the player to destination position
	 */
	public void moveToDesiredPosition()
	{
		Vector3 start = new Vector3(this.position.x, this.position.y, 0);
		float distance = start.dst(this.desiredPosition);
		
		boolean moving = true;
	    if (distance <= 2)
	    {
	    	moving = false;
			this.velocity.x = 0;
			this.velocity.y = 0;
	    }
		
		if (moving == true)
		{
			this.velocity.set(this.desiredPosition.x - this.position.x, this.desiredPosition.y - this.position.y, 0);
			this.velocity.nor(); // Normalizes the value to be used
			
			float fThreshold = (float) Math.cos(Math.PI / 8);

			if (this.velocity.x > fThreshold) {
				this.velocity.x = this.speed;
				this.velocity.y = 0;
			} else if (this.velocity.x < -fThreshold) {
				this.velocity.x = -this.speed;
				this.velocity.y = 0;
			} else if (this.velocity.y > fThreshold) {
				this.velocity.x = 0;
				this.velocity.y = this.speed;
			} else if (this.velocity.y < -fThreshold) {
				this.velocity.x = 0;
				this.velocity.y = -this.speed;
			} else if (this.velocity.x > 0 && this.velocity.y > 0) {
				this.velocity.x = this.speed;
				this.velocity.y = this.speed;
			} else if (this.velocity.x > 0 && this.velocity.y < 0) {
				this.velocity.x = this.speed;
				this.velocity.y = -this.speed;
			} else if (this.velocity.x < 0 && this.velocity.y > 0) {
				this.velocity.x = -this.speed;
				this.velocity.y = this.speed;
			} else if (this.velocity.x < 0 && this.velocity.y < 0)
			{
				this.velocity.x = -this.speed;
				this.velocity.y = -this.speed;
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
	
	@Override
    public boolean keyDown(int keycode) {
            switch(keycode) {
            case Keys.LEFT:
				this.velocity.x = -this.speed;
                break;
            case Keys.RIGHT:
				this.velocity.x = this.speed;
                break;
            case Keys.UP:
				this.velocity.y = this.speed;
                break;
            case Keys.DOWN:
				this.velocity.y = -this.speed;
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
				this.velocity.x = 0.0f;
            	break;
            case Keys.UP:
            case Keys.DOWN:
				this.velocity.y = 0.0f;
                break;    
            }
            
    		updateDirection();
            
            return true;
    }
    
    /**
     * Update the player direction according to its velocity
     */
	private void updateDirection() {
		if (this.velocity.y > 0) {
			if (this.velocity.x < 0) {
				this.direction = Direction.NORTH_WEST;
			} else if (this.velocity.x > 0) {
				this.direction = Direction.NORTH_EAST;
			} else {
				this.direction = Direction.NORTH;
			}
		} else if (this.velocity.y < 0) {
			if (this.velocity.x < 0) {
				this.direction = Direction.SOUTH_WEST;
			} else if (this.velocity.x > 0) {
				this.direction = Direction.SOUTH_EAST;
			} else {
				this.direction = Direction.SOUTH;
			}
		} else if (this.velocity.x < 0) {
			this.direction = Direction.WEST;
		} else if (this.velocity.x > 0) {
			this.direction = Direction.EAST;
		} else {
			this.direction = Direction.NONE;
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
		return this.direction.ordinal();
	}

	public int height() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public float speed() {
		return this.speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	@Override
	public void dispose() {
	}

	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
}
