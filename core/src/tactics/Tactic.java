package tactics;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.senegas.kickoff.pitches.Pitch;
import com.senegas.kickoff.screens.Match;

public class Tactic {
	enum LocationId {
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
	private String name;
	private static ShapeRenderer shapeRenderer;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Array<ArrayMap<LocationId, Vector2>> playersLocations;
	private Array<Rectangle> regions;
	
	public Tactic(String fileName) {
		shapeRenderer = new ShapeRenderer();
		// load players locations
		playersLocations = new Array<ArrayMap<LocationId, Vector2>>();
		 
		try {
			Element root = new XmlReader().parse(Gdx.files.internal(fileName));
			
			name = root.get("name");
			Gdx.app.log("Tactic", "Reading " + name + "...");
			
			Array<Element> players = root.getChildrenByName("player");
			for (Element player : players) {
				int shirt = player.getInt("shirt");
				
				Gdx.app.log("Tactic", "Location for player number " + shirt);
				
				Array<Element> regions = player.getChildrenByName("region");
				ArrayMap<LocationId, Vector2> locationsMap = new ArrayMap<LocationId, Vector2>();
				for (Element region : regions) {
					String locationStringId = region.get("name");
					
					locationsMap.put(LocationId.valueOf(locationStringId),
						    new Vector2(region.getFloat("x"), region.getFloat("y")));

					Gdx.app.log("Tactic", locationStringId + " read");
				}
				
				playersLocations.add(locationsMap);
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// create tactic regions
		regions = new Array<Rectangle>();
		
		for (int column = 0; column < REGION_COLUMNS; column++) {
			for (int row = 0; row < REGION_ROWS; row++) {
				regions.add( new Rectangle(column*REGION_WIDTH_IN_PX, row*REGION_HEIGHT_IN_PX, REGION_WIDTH_IN_PX, REGION_HEIGHT_IN_PX));

				Gdx.app.log("Tactic", "<Region " + (((row + column) + 1) + (column*REGION_COLUMNS)) + ">" +
	                   	              (column*REGION_WIDTH_IN_PX) +", " + (row*REGION_HEIGHT_IN_PX) + ", " +
			                          (column+1)*REGION_WIDTH_IN_PX + ", " + (row+1)*REGION_HEIGHT_IN_PX + "</Region>");
			}
		}
	}
	
	public void showRegionAndExpectedPlayerLocation(Match m) {
		// enable transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRenderer.setProjectionMatrix(m.camera.combined);
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0.8f, 0, 0, 0.2f));

		for (LocationId index : LocationId.values()) {
			if (index.ordinal() > LocationId.area12.ordinal())
				break;
			
			Rectangle region = regions.get(index.ordinal());
			
			if (region.contains(m.ball.getPosition().x - Pitch.OUTER_BOTTOM_EDGE_X, m.ball.getPosition().y - Pitch.OUTER_BOTTOM_EDGE_Y)) {
				shapeRenderer.rect(region.x + Pitch.OUTER_BOTTOM_EDGE_X, region.y + Pitch.OUTER_BOTTOM_EDGE_Y, region.width, region.height);

				// draw player location for the location id
				for (ArrayMap<LocationId, Vector2> playerLocations : playersLocations) {
					shapeRenderer.setColor(new Color(1.0f, 0.8f, 0, 0.4f));
					shapeRenderer.circle(playerLocations.get(index).x + Pitch.OUTER_BOTTOM_EDGE_X, playerLocations.get(index).y + Pitch.OUTER_BOTTOM_EDGE_Y, 8);						
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
