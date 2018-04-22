package com.senegas.kickoff.tactics;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.senegas.kickoff.entities.Ball;
import com.senegas.kickoff.entities.Team;
import com.senegas.kickoff.pitches.Pitch;
import com.senegas.kickoff.utils.PitchUtils;

public class Tactic {
	enum Location {
		area1,
		area2,
		area3,
		area4,
		area5,
		area6,
		area7,
		area8,
		area9,
		area10,
		area11,
		area12,
		kickoff_own,
		kickoff_def,
		goalkick_own,
		goalkick_def,
		corner1,
		corner2,
		corner3,
		corner4
	}
	
	private final static int REGION_ROWS = 4;
	private final static int REGION_COLUMNS = 3;
	private final static float REGION_WIDTH_IN_PX = (float) (Pitch.PITCH_WIDTH_IN_PX / REGION_COLUMNS);
	private final static float REGION_HEIGHT_IN_PX = (float) (Pitch.PITCH_HEIGHT_IN_PX / REGION_ROWS);
	
	private static ShapeRenderer shapeRenderer = new ShapeRenderer(); // mainly used for debug purpose
	
	private String name;
	private Team team;
	private Array<ObjectMap<Location, Vector2>> playersLocations;
	private Array<Rectangle> regions;
	
	/**
	 * Constructor
	 * @param team
	 * @param fileName
	 */
	public Tactic(Team team, String fileName) {
		this.team = team;
		this.playersLocations = new Array<ObjectMap<Location, Vector2>>();
		this.regions = new Array<Rectangle>();
		
		createRegions();
		loadLocations(fileName);
	}

	/**
	 * Create the pitch regions
	 */
	private void createRegions() {
		// create pitch regions
		for (int column = 0; column < REGION_COLUMNS; column++) {
			for (int row = 0; row < REGION_ROWS; row++) {
				this.regions.add(new Rectangle(column * REGION_WIDTH_IN_PX, row * REGION_HEIGHT_IN_PX,
						                       REGION_WIDTH_IN_PX, REGION_HEIGHT_IN_PX));
			}
		}
	}
	
	/**
	 * Load player locations from tatic xml file
	 * @param fileName
	 */
	private void loadLocations(String fileName) {
		try {
			Element root = new XmlReader().parse(Gdx.files.internal(fileName));
			
			this.name = root.get("name");
			Gdx.app.log("Tactic", "Loading " + name + "...");
			
			Array<Element> players = root.getChildrenByName("player");
			for (Element player : players) {
				//int shirt = player.getInt("shirt"); // shirt number
				//Gdx.app.log("Tactic", "Location for player number " + shirt);
				
				// regions
				Array<Element> regions = player.getChildrenByName("region");
				ObjectMap<Location, Vector2> locationsMap = new ObjectMap<Location, Vector2>();
				for (Element region : regions) {
					String locationId = region.get("name"); // region name
					
					// add region to map
					locationsMap.put(Location.valueOf(locationId), // returns the enum constant of the specified enum type with the specified name.
						    new Vector2(region.getFloat("x"), region.getFloat("y")));

					//Gdx.app.log("Tactic", locationId + " read");
				}
				// add locations
				this.playersLocations.add(locationsMap);
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the tactic name
	 * @return the tactic name
	 */
	public String getName() {
		return this.name;
	}
	
	public void update(Ball ball)
	{
		Vector2 ballLocation = PitchUtils.globalToPitch(ball.getPosition().x, ball.getPosition().y);
		
		for (Location location : Location.values()) { // loop on each location
			if (location.ordinal() > Location.area12.ordinal())
				break;
			
			Rectangle region = this.regions.get(location.ordinal()); // get the region for location index
			
			if (region.contains(ballLocation.x, ballLocation.y)) {
				for (int playerIndex = 0; playerIndex < 10; playerIndex++) {
					Vector2 playerLocation = PitchUtils.pitchToGlobal(this.playersLocations.get(playerIndex).get(location).x,
                                                                      this.playersLocations.get(playerIndex).get(location).y);
					this.team.getPlayers().get(playerIndex).moveTo(playerLocation);
				}
				break; // not needed to check other regions
			}	
		}
	}
	
	/**
	 * Debug only method that displays selected region and tatic's player home location
	 * @param camera
	 * @param ball
	 */
	public void showRegionAndExpectedPlayerLocation(OrthographicCamera camera, Ball ball) {
		// enable transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0.8f, 0, 0, 0.2f));

		for (Location location : Location.values()) {
			if (location.ordinal() > Location.area12.ordinal())
				break;
			
			Rectangle region = this.regions.get(location.ordinal());
			
			Vector2 ballLocation = PitchUtils.globalToPitch(ball.getPosition().x, ball.getPosition().y);
			if (region.contains(ballLocation.x, ballLocation.y)) {
				Vector2 regionLocation = PitchUtils.pitchToGlobal(region.x, region.y);
				shapeRenderer.rect(regionLocation.x, regionLocation.y, region.width, region.height);
				
				// draw player location
				for (ObjectMap<Location, Vector2> playerLocations : this.playersLocations) {
					shapeRenderer.setColor(new Color(1.0f, 0.5f, 0, 0.4f));
					Vector2 playerLocation = PitchUtils.pitchToGlobal(playerLocations.get(location).x, playerLocations.get(location).y);
					shapeRenderer.circle(playerLocation.x, playerLocation.y, 8);
				}
			}	
		}
		
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public void dispose() {
		shapeRenderer.dispose();
	}
}
