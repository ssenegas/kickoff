package com.senegas.kickoff.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.senegas.kickoff.entities.Player.Direction;
import com.senegas.kickoff.pitches.Pitch;
import com.senegas.kickoff.screens.Match;
import com.senegas.kickoff.tactics.Tactic;

public class Team  implements Disposable {
	private String name;
	private Array<Player> players;
	private Tactic tactic;
	private Direction direction = Direction.NORTH;
	private Match match;
	
	public Team(Match m) {
		players = new Array<Player>();
		match = m;
		createPlayers();
		tactic = new Tactic(this, "tactics/4-3-3.xml");
	}

	private void createPlayers() {
		for (int i = 0; i < 10; i++) {
			players.add(new Player(0, Pitch.HEIGHT/2));			
		}
	}
	
	public void update(float deltaTime) {
		tactic.update(match.ball());
		
		for (Player player : players)
			player.update(deltaTime);
	}
	
	public void draw(Batch batch) {
		for (Player player : players)
			player.draw(batch);
	}
	
	public Array<Player> members() {
		return players;
	}
	
	public Tactic tactic() {
		return tactic;
	}

	@Override
	public void dispose() {
		for (Player player : players)
			player.getTexture().dispose();
		tactic.dispose();
	}
}
