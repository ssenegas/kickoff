package com.senegas.kickoff.pitches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.senegas.kickoff.entities.Ball;

import static com.badlogic.gdx.math.Intersector.intersectSegments;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_BOTTOM_GOAL_LINE_Y;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_LEFT_TOUCHLINE_X;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_RIGHT_TOUCHLINE_X;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_TOP_EDGE_X;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_TOP_EDGE_Y;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_TOP_GOAL_LINE_Y;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.PITCH_HEIGHT_IN_PX;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.PITCH_WIDTH_IN_PX;

/**
 * base class for Pitch
 * @author Sébastien Sénégas
 *
 */
public abstract class Pitch implements Disposable {

	private static final int MAP_WIDTH_IN_TILE = 80;
	private static final int MAP_HEIGHT_IN_TILE = 96;
	private static final int TILE_IN_PIXEL = 16;

	public static final int HEIGHT = MAP_HEIGHT_IN_TILE * TILE_IN_PIXEL;
	public static final int WIDTH = MAP_WIDTH_IN_TILE * TILE_IN_PIXEL;
	
	private TiledMap tiledMap;
	private float friction;
	private Vector2 lastIntersection;
	
	/**
	 * Constructor
	 * @param tmxMap the tile map file name
	 * @param friction the friction coefficient
	 */
	public Pitch(AssetManager assetManager, String tmxMap, float friction) {
		Gdx.app.log("Pitch", "Load tile map " + tmxMap);
		this.tiledMap = assetManager.get(tmxMap);
		this.friction = friction;
		this.lastIntersection = new Vector2();
	}
	
	/**
	 * Get the tiled map
	 * @return TiledMap
	 */
	public TiledMap getTiledMap() {
		return this.tiledMap;
	}
	
	/**
	 * Get the friction coefficient
	 * @return float
	 */
	public float getFriction() {
		return this.friction;
	}
	
	@Override
	public void dispose() {
		this.tiledMap.dispose();
	}

	public Vector2 getCenterSpot() {
		return new Vector2((int) (PITCH_WIDTH_IN_PX / 2 + OUTER_TOP_EDGE_X),
                           (int) (PITCH_HEIGHT_IN_PX / 2 + OUTER_TOP_EDGE_Y + Ball.SPRITE_SIZE));
	}

	public boolean crossesSideLine(Vector3 lastPosition, Vector3 position) {
		Vector2 start = new Vector2(lastPosition.x, lastPosition.y);
		Vector2 end = new Vector2(position.x, position.y);

		Vector2 sideLineStart = new Vector2(OUTER_LEFT_TOUCHLINE_X, OUTER_TOP_GOAL_LINE_Y);
		Vector2 sideLineEnd = new Vector2(OUTER_LEFT_TOUCHLINE_X, OUTER_BOTTOM_GOAL_LINE_Y);

		if (intersectSegments(start, end, sideLineStart, sideLineEnd, this.lastIntersection)) {
			return true;
		}

		sideLineStart = new Vector2(OUTER_RIGHT_TOUCHLINE_X, OUTER_TOP_GOAL_LINE_Y);
		sideLineEnd = new Vector2(OUTER_RIGHT_TOUCHLINE_X, OUTER_BOTTOM_GOAL_LINE_Y);

		if (intersectSegments(start, end, sideLineStart, sideLineEnd, this.lastIntersection)) {
			return true;
		}

		return false;
	}

	public Vector2 getLastIntersection() {
		return this.lastIntersection;
	}

	public abstract String getDescription();
}
