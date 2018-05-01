package com.senegas.kickoff.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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
public class Team  implements Disposable {
	private Match match;
	private String name;
	private Direction direction;
	private Texture texture;
	private Array<Player> players;
	private Tactic tactic;
	/**
	 * Constructor
	 * @param match the match
	 * @param name the team name
	 */
	public Team(Match match, String name, Direction direction) {
		this.match = match;
		this.name = name;
		this.direction = direction;
		this.texture = direction == Direction.NORTH ? new Texture("entities/style1a.png") : new Texture("entities/style1b.png");
		this.players = new Array<Player>();
		createPlayers();
		this.tactic = new Tactic424(this);
	}

	/**
	 * Create the players
	 */
	private void createPlayers() {
		for (int i = 0; i < 10; i++) {
			this.players.add(new Player(0, Pitch.HEIGHT/2, texture));			
		}
	}
	
	/**
	 * Update the team's players
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		this.tactic.update(this.match.ball());
		
		for (Player player : this.players)
			player.update(deltaTime);
	}
	
	/**
	 * Draw the team's players
	 * @param batch
	 */
	public void draw(Batch batch) {
		for (Player player : this.players)
			player.draw(batch);
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
	
	public Direction getDirection() {
		return this.direction;
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
