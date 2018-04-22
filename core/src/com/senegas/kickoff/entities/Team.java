package com.senegas.kickoff.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.senegas.kickoff.entities.Player.Direction;
import com.senegas.kickoff.pitches.Pitch;
import com.senegas.kickoff.screens.Match;
import com.senegas.kickoff.tactics.Tactic;

public class Team  implements Disposable {
	private Match match;
	private String name;
	private Array<Player> players;
	private Tactic tactic;
	private Direction direction = Direction.NORTH;
	
	/**
	 * Constructor
	 * @param match the match
	 * @param name the team name
	 */
	public Team(Match match, String name) {
		this.match = match;
		this.name = name;
		this.players = new Array<Player>();
		createPlayers();
		this.tactic = new Tactic(this, "tactics/4-3-3.xml");
	}

	/**
	 * Create the players
	 */
	private void createPlayers() {
		for (int i = 0; i < 10; i++) {
			this.players.add(new Player(0, Pitch.HEIGHT/2));			
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

	@Override
	public void dispose() {
		for (Player player : this.players)
			player.getTexture().dispose();
		this.tactic.dispose();
	}
}
