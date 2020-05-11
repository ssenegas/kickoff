package com.senegas.kickoff.pitches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;
import com.badlogic.gdx.utils.Disposable;
import com.senegas.kickoff.entities.Ball;

import static com.badlogic.gdx.math.Intersector.intersectLines;
import static com.badlogic.gdx.math.Intersector.intersectSegments;
import static com.badlogic.gdx.math.Intersector.pointLineSide;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.*;

/**
 * base class for Pitch
 * @author Sébastien Sénégas
 *
 */
public abstract class Pitch implements Disposable {
	
	public enum Type {
		CLASSIC, WET, SOGGY, ARTIFICIAL, PLAYERMANAGER
	}
	
	public final static int MAP_WIDTH_IN_TILE = 80;
	public final static int MAP_HEIGHT_IN_TILE = 96;	
	public static final int TILE_WIDTH_IN_PIXEL = 16;
    public static final int  TILE_HEIGHT_IN_PIXEL = 16;
	public static final int HEIGHT = MAP_HEIGHT_IN_TILE * TILE_HEIGHT_IN_PIXEL;
	public static final int WIDTH = MAP_WIDTH_IN_TILE * TILE_WIDTH_IN_PIXEL;
	
	private TiledMap tiledMap;
	private float friction;
	private Vector2 lastIntersection;
	
	/**
	 * Constructor
	 * @param fileName the tile map file name
	 * @param friction the friction coefficient
	 */
	public Pitch(String fileName, float friction) {
		Gdx.app.log("Pitch", "Load tile map " + fileName);
		tiledMap = new TmxMapLoader().load(fileName);
		this.friction = friction;
		lastIntersection = new Vector2();
	}
	
	/**
	 * Get the tiled map
	 * @return TiledMap
	 */
	public TiledMap getTiledMap() {
		return tiledMap;
	}
	
	/**
	 * Get the friction coefficient
	 * @return float
	 */
	public float getFriction()
	{
		return friction;
	}
	
	@Override
	public void dispose() {
		tiledMap.dispose();		
	}

	public Vector2 getCenterSpot() {
		return new Vector2((int) (PITCH_WIDTH_IN_PX / 2 + OUTER_TOP_EDGE_X),
                           (int) (PITCH_HEIGHT_IN_PX / 2 + OUTER_TOP_EDGE_Y + Ball.SPRITE_HEIGHT));
	}

	public boolean crossesSideLine(Vector3 lastPosition, Vector3 position) {
		Vector2 start = new Vector2(lastPosition.x, lastPosition.y);
		Vector2 end = new Vector2(position.x, position.y);

		Vector2 sideLineStart = new Vector2(OUTER_LEFT_TOUCHLINE_X, OUTER_TOP_GOAL_LINE_Y);
		Vector2 sideLineEnd = new Vector2(OUTER_LEFT_TOUCHLINE_X, OUTER_BOTTOM_GOAL_LINE_Y);

		if (intersectSegments(start, end, sideLineStart, sideLineEnd, lastIntersection)) {
			return true;
		}

		sideLineStart = new Vector2(OUTER_RIGHT_TOUCHLINE_X, OUTER_TOP_GOAL_LINE_Y);
		sideLineEnd = new Vector2(OUTER_RIGHT_TOUCHLINE_X, OUTER_BOTTOM_GOAL_LINE_Y);

		if (intersectSegments(start, end, sideLineStart, sideLineEnd, lastIntersection)) {
			return true;
		}

		return false;
	}

	public Vector2 getLastIntersection() {
		return lastIntersection;
	}

}
