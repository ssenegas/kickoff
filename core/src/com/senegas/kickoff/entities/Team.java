package com.senegas.kickoff.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.senegas.kickoff.screens.MatchScreen;
import com.senegas.kickoff.tactics.Tactic;
import com.senegas.kickoff.tactics.Tactic424;
import com.senegas.kickoff.utils.Direction;

import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_TOP_EDGE_Y;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.PITCH_HEIGHT_IN_PX;

/**
 * Team
 * @author Sébastien Sénégas
 *
 */
public class Team implements Disposable {
    private Tactic tactic;
	private MatchScreen match;
	private String name;
	private Direction direction;
	private Color mainColor;
	private Texture texture;
    private Array<Player> players = new Array<>();
	private Player closest;

    /**
	 * Constructor
	 * @param match the match
     * @param name the team name
     * @param direction the team's direction
	 */
	public Team(MatchScreen match, String name, Direction direction) {
		this.tactic = new Tactic424(this);
		this.match = match;
		this.name = name;
		this.direction = direction;
        this.mainColor = (direction == Direction.NORTH ? new Color(Color.RED) : new Color(Color.BLUE));
		this.texture = (direction == Direction.NORTH ? this.match.getApp().assets.get("entities/style1a.png") : this.match.getApp().assets.get("entities/style1b.png"));

		createPlayers();
		setupIntroduction();
	}

	/**
	 * Create the players
	 */
	private void createPlayers() {
		float x = 0;
        float y = (int) (PITCH_HEIGHT_IN_PX / 2 + OUTER_TOP_EDGE_Y + 16 + (this.direction == Direction.NORTH ? -16 : 16));

        for (int i = 0; i < 10; i++) {
            this.players.add(new Player(this.texture, this, new Vector3(x, y, 0)));
			x -= Player.SPRITE_SIZE;
		}
	}

	public void setupIntroduction() {
		Vector3 playerPosition = new Vector3(0,
				(int) (PITCH_HEIGHT_IN_PX / 2 + OUTER_TOP_EDGE_Y + 16),
				0);
        playerPosition.add(352, Player.SPRITE_SIZE * (this.direction == Direction.NORTH ? -1 : 1), 0);

		for (Player player : this.players) {
			player.setDestination(new Vector3(playerPosition));
			playerPosition.sub(16, 0, 0);
		}
		//setControlState(Player::None);
	}
	
	/**
	 * Update the team's players
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		//this.tactic.update(this.match.getBall());
		for (Player player : this.players) {
			player.update(deltaTime);
		}
	}

	public boolean isReady() {
		for (Player player : this.players) {
            if (!player.inPosition()) {
                return false;
            }
		}
		return true;
	}

	/**
	 * Draw the team's players
	 * @param batch
	 */
	public void draw(SpriteBatch batch) {
        for (Player player : this.players) {
            player.render(batch);
        }
	}

    public void showDebug(ShapeRenderer shapeBatch, Vector3 position) {
        if (this.direction == Direction.NORTH) {
            for (Player player : this.players) {
                player.showBounds(shapeBatch);
            }
            this.tactic.showRegionAndExpectedPlayerLocation(shapeBatch, position);
        }
    }
	
	/**
	 * Get the players
	 * @return
	 */
	public Array<Player> getPlayers() {
		return this.players;
	}

	public Player calculateClosestPlayer(Vector3 position) {
		float shortestDistance = 0f;
        this.closest = null;
		for (Player player : this.players) {
			float distance = player.getPosition().dst2(position);
            if (this.closest == null || distance < shortestDistance) {
                this.closest = player;
				shortestDistance = distance;
			}
		}
        return this.closest;
	}

	public void setupThrowIn(Vector3 position, boolean attack) {
		calculateClosestPlayer(position);
		if (attack) {
            this.closest.setDestination(position);
		}
	}

	public void setupKickoff(boolean attack) {
        this.tactic.setupKickoff(attack);
	}
	
	/**
	 * Get the tactic
	 * @return
	 */
	public Tactic getTactic() {
		return this.tactic;
	}
	
	/**
	 * Get the name
	 * @return
	 */
	public String getName() {
		return this.name;
	}

    /**
     * Get the direction's team
     * @return
     */
	public Direction getDirection() {
		return this.direction;
	}

    /**
     * Get the team's main color
     * @return
     */
    public Color getMainColor() {
        return this.mainColor;
    }
	
	@Override
	public void dispose() {
		this.tactic.dispose();
	}
}
