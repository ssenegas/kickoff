package com.senegas.kickoff.pitches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

public abstract class Pitch implements Disposable, FootballDimensions {
	
	public enum PitchType {
		CLASSIC, WET, SOGGY, ARTIFICIAL, PLAYERMANAGER
	}
	
	public final static int mapWidthInTiles = 80;
	public final static int mapHeightInTiles = 96;	
	public static final int tileWidthInPixel = 16;
    public static final int  tileHeightInPixel = 16;
	public static final int HEIGHT = mapHeightInTiles * tileHeightInPixel;
	public static final int WIDTH = mapWidthInTiles * tileWidthInPixel;
	
	private TiledMap tiledMap;
	private float friction;
	
	public Pitch(String fileName, float friction) {
		Gdx.app.log("Pitch", "Load tile map " + fileName);
		tiledMap = new TmxMapLoader().load(fileName);
		this.friction = friction;
	}
	
	public TiledMap getTiledMap() {
		return tiledMap;
	}
	
	public float friction()
	{
		return friction;
	}
	
	@Override
	public void dispose() {
		tiledMap.dispose();		
	}
}
