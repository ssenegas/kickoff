package com.senegas.kickoff.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.senegas.kickoff.entities.Player.Direction;
import com.senegas.kickoff.pitches.Pitch;
import com.senegas.kickoff.screens.Match;
import com.senegas.kickoff.tactics.Tactic;
import com.senegas.kickoff.tactics.Tactic424;

/**
 * Team
 * @author Sébastien Sénégas
 *
 */
public class Team implements Disposable {
    private Tactic tactic;
	private Match match;
	private String name;
	private Direction direction;
	private Color mainColor;
	private Texture texture;
	private Array<Player> players = new Array<Player>();

    /**
	 * Constructor
	 * @param match the match
     * @param name the team name
     * @param direction the team's direction
	 */
	public Team(Match match, String name, Direction direction) {
		this.tactic = new Tactic424(this);
		this.match = match;
		this.name = name;
		this.direction = direction;
        mainColor = (direction == Direction.NORTH ? new Color(Color.RED) : new Color(Color.BLUE));
        this.texture = (direction == Direction.NORTH ? new Texture("entities/style1a.png") : new Texture("entities/style1b.png"));

		createPlayers();
		setupIntroduction();
	}

	/**
	 * Create the players
	 */
	private void createPlayers() {
		float x = 0;
		float y = (int) (Pitch.PITCH_HEIGHT_IN_PX / 2 + Pitch.OUTER_TOP_EDGE_Y + 16 + (direction == Direction.NORTH ? -16: 16));

        for (int i = 0; i < 10; i++) {
			this.players.add(new Player(texture, new Vector3(x, y, 0)));
			x -= 16;
		}
	}

	public void setupIntroduction() {
		Vector3 playerPosition = new Vector3(0,
				(int) (Pitch.PITCH_HEIGHT_IN_PX / 2 + Pitch.OUTER_TOP_EDGE_Y + 16),
				0);
		playerPosition.add(352, Player.SPRITE_HEIGHT * (direction == Direction.NORTH ? -1: 1), 0);

		for (Player player : this.players) {
			player.setDestination(playerPosition);
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
			if (player.inPosition()) return false;
		}

		return true;
	}

	/**
	 * Draw the team's players
	 * @param batch
	 */
	public void draw(Batch batch) {
        for (Player player : this.players) {
            player.draw(batch);
        }
	}

	public void showDebug() {
        if (direction == Direction.NORTH) {
            for (Player player : this.players) {
                player.showBounds(match.getCamera());
            }
            this.tactic.showRegionAndExpectedPlayerLocation(match.getCamera(), match.getBall());
        }
    }
	
	/**
	 * Get the players
	 * @return
	 */
	public Array<Player> getPlayers() {
		return this.players;
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
        return mainColor;
    }
	
	@Override
	public void dispose() {
		for (Player player : this.players) {
			player.dispose();
			player.getTexture().dispose();
		}
		this.tactic.dispose();
	}
}
