package com.senegas.kickoff.tactics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.senegas.kickoff.entities.Team;
import com.senegas.kickoff.utils.Direction;
import com.senegas.kickoff.utils.PitchUtils;

import java.io.IOException;

import static com.senegas.kickoff.pitches.FootballDimensionConstants.PITCH_HEIGHT_IN_PX;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.PITCH_WIDTH_IN_PX;

/**
 * Any tactic must be a subclass of Tactic
 * Areas and corners numbers are defined as follow.
 * <pre>
 *             Opponent team side
 *  Corner 2 +------+------+------+ Corner 3
 *           |     .|      |.     |
 *           |  3  .+---7--+. 11  |
 *           |     .        .     |
 *           |. . . . . . . . . . |
 *           |     .        .     |
 *           |  2  . /--6-\ . 10  |
 *           |     ./      \.     |
 *           +------+------+------+
 *           |     .\      /.     |
 *           |  1  . \--5-/ .  9  |
 *           |     .        .     |
 *           |. . . . . . . . . . |
 *           |     .        .     |
 *           |  0  .+---4--+.  8  |
 *           |     .|      |.     |
 *  Corner 0 +------+------+------+ Corner 1
 *                My team side
 * </pre>
 * @author Sébastien Sénégas
 *
 */
public class Tactic {
	enum Location {
		area1("area1"), area2("area2"),   area3("area3"),   area4("area4"),
		area5("area5"), area6("area6"),   area7("area7"),   area8("area8"),
		area9("area9"), area10("area10"), area11("area11"), area12("area12"),
		kickoff_own("kickoff_own"), kickoff_def("kickoff_def"),
		goalkick_own("goalkick_own"), goalkick_def("goalkick_own"),
		corner1("corner 1"), corner2("corner 2"), corner3("corner 3"), corner4("corner 4");
		
		private String name;
		
		/**
		 * Constructor
		 * @param name
		 */
		Location(String name) {
			this.name = name;
		}
		
		public String toString() {
			return this.name;
		}
	}
	
	private final static int REGION_ROWS = 4;
	private final static int REGION_COLUMNS = 3;
	public final static int REGIONS_COUNT = REGION_ROWS * REGION_COLUMNS;
	private final static float REGION_WIDTH_IN_PX = (float) (PITCH_WIDTH_IN_PX / REGION_COLUMNS);
	private final static float REGION_HEIGHT_IN_PX = (float) (PITCH_HEIGHT_IN_PX / REGION_ROWS);
	
	private String name;
	private Team team;
	private Vector2[][] locations;
	private Array<Rectangle> regions;
	
	/**
	 * Constructor
	 * @param team
	 * @param fileName
	 */
	public Tactic(Team team, String fileName) {
		this.team = team;
		new Array<ObjectMap<Location, Vector2>>();
		this.locations = new Vector2[10][Location.values().length];
		this.regions = new Array<Rectangle>();
		
		createRegions();
		try {
			loadFromXml(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	 * Load player locations from tactic xml file
	 * @param fileName
	 * @throws IOException 
	 */
	private void loadFromXml(String fileName) throws IOException {
		Element root = new XmlReader().parse(Gdx.files.internal(fileName));

		this.name = root.get("name");
		Gdx.app.log("Tactic", "Loading " + this.name + "...");

		Array<Element> players = root.getChildrenByName("player");
		int playerIndex = 0;
		for (Element player : players) {
			//int shirt = player.getInt("shirt"); // shirt number
			//Gdx.app.log("Tactic", "Location for player number " + shirt);

			// regions
			Array<Element> regions = player.getChildrenByName("region");
			for (Element region : regions) {
				String regionName = region.get("name"); // region name

				this.locations[playerIndex][Location.valueOf(regionName).ordinal()] = new Vector2(region.getFloat("x"), region.getFloat("y"));

				//Gdx.app.log("Tactic", locationId + " read");
			}
			playerIndex++;
		}	
	}
	
	/**
	 * Get the tactic name
	 * @return the tactic name
	 */
	public String getName() {
		return this.name;
	}
	
	public void update(Vector3 position) {
		int regionIndex = getRegionIndex(position, this.team);
		//Gdx.app.log("Tactic", "region index: " + regionIndex);
		for (int playerIndex = 0; playerIndex < 10; playerIndex++) {
			Vector3 playerLocation = PitchUtils.pitchToGlobal(this.team.getDirection() == Direction.NORTH ? this.locations[playerIndex][regionIndex].x : (float)PITCH_WIDTH_IN_PX - this.locations[playerIndex][regionIndex].x,
					this.team.getDirection() == Direction.NORTH ? this.locations[playerIndex][regionIndex].y : (float)PITCH_HEIGHT_IN_PX - this.locations[playerIndex][regionIndex].y);
			this.team.getPlayers().get(playerIndex).setDestination(playerLocation);
		}
	}

	public void setupKickoff(boolean attack) {
	    int regionIndex = attack ? Location.kickoff_own.ordinal() : Location.kickoff_def.ordinal();
        for (int playerIndex = 0; playerIndex < 10; playerIndex++) {
            Vector3 playerLocation = PitchUtils.pitchToGlobal(this.team.getDirection() == Direction.NORTH ? this.locations[playerIndex][regionIndex].x : (float)PITCH_WIDTH_IN_PX - this.locations[playerIndex][regionIndex].x,
					this.team.getDirection() == Direction.NORTH ? this.locations[playerIndex][regionIndex].y : (float)PITCH_HEIGHT_IN_PX - this.locations[playerIndex][regionIndex].y);
            this.team.getPlayers().get(playerIndex).setDestination(playerLocation);
        }
    }
	
	/**
	* Returns a region id depending on the team orientation
	* <pre>
	* Opponent team side
	* 
	*   +------+------+------+
	*   |     .|      |.     |
	*   |  3  .+---7--+. 11  |
	*   |     .        .     |
	*   |. . . . . . . . . . |
	*   |     .        .     |
	*   |  2  . /--6-\ . 10  |
	*   |     ./      \.     |
	*   +------+------+------+
	*   |     .\      /.     |
	*   |  1  . \--5-/ .  9  |
	*   |     .        .     |
	*   |. . . . . . . . . . |
	*   |     .        .     |
	*   |  0  .+---4--+.  8  | AREA_HEIGHT
	*   |     .|      |.     |
	*   +------+------+------+
	*                   AREA_
	* My team side      WIDTH
	* </pre>
	 * Get the region index according to the ball position and team's direction
	 * @param position
	 * @param team
	 * @return region index
	 */
	public static int getRegionIndex(Vector3 position, Team team)
	{
		Vector2 pitchPosition = PitchUtils.globalToPitch(position.x, position.y);
		
		int xCoord = Double.valueOf(pitchPosition.x / REGION_WIDTH_IN_PX).intValue();
		int yCoord = Double.valueOf(pitchPosition.y / REGION_HEIGHT_IN_PX).intValue();

		xCoord = xCoord < 0 ? 0 : (xCoord >= REGION_COLUMNS ? REGION_COLUMNS - 1 : xCoord);
		yCoord = yCoord < 0 ? 0 : (yCoord >= REGION_ROWS ? REGION_ROWS - 1 : yCoord);

		int regionIndex = xCoord * REGION_ROWS + yCoord;

		return team.getDirection() == Direction.NORTH ? regionIndex : REGIONS_COUNT - 1 - regionIndex;
	}
	
	/**
	 * Debug only method that displays active region and tactic's player home location
	 * @param shapeBatch
	 * @param position
	 */
	public void showRegionAndExpectedPlayerLocation(ShapeRenderer shapeBatch, Vector3 position) {
		// enable transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		// draw active region
		shapeBatch.begin(ShapeType.Filled);

		shapeBatch.setColor(new Color(0.8f, 0, 0, 0.2f));
		int regionIndex = getRegionIndex(position, this.team);
		Rectangle region = this.regions.get(regionIndex);
		Vector3 regionLocation = PitchUtils.pitchToGlobal(region.x, region.y);
		shapeBatch.rect(regionLocation.x, regionLocation.y, region.width, region.height);

		// draw player location
		for (int playerIndex = 0; playerIndex < 10; playerIndex++) {
			shapeBatch.setColor(new Color(1.0f, 0.5f, 0, 0.4f));
			Vector3 playerLocation = this.team.getPlayers().get(playerIndex).getDestination();
			shapeBatch.circle(playerLocation.x, playerLocation.y, 8);
		}

		shapeBatch.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public void dispose() {
	}
}
