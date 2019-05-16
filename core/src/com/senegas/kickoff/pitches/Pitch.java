package com.senegas.kickoff.pitches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

/**
 * base class for Pitch
 * @author Sébastien Sénégas
 *
 */
public abstract class Pitch implements Disposable, FootballDimensions {
	
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
	
	/**
	 * Constructor
	 * @param fileName the tile map file name
	 * @param friction the friction coefficient
	 */
	public Pitch(String fileName, float friction) {
		Gdx.app.log("Pitch", "Load tile map " + fileName);
		tiledMap = new TmxMapLoader().load(fileName);
		this.friction = friction;
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

	static public Vector2 getCenterSpot()
	{
		return new Vector2((int) (Pitch.PITCH_WIDTH_IN_PX / 2 + Pitch.OUTER_TOP_EDGE_X),
                           (int) (Pitch.PITCH_HEIGHT_IN_PX / 2 + Pitch.OUTER_TOP_EDGE_Y + 16));
	}
}
