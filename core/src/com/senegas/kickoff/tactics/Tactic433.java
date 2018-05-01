package com.senegas.kickoff.tactics;

import com.senegas.kickoff.entities.Team;
import com.senegas.kickoff.pitches.FootballDimensions;

public class Tactic433 extends Tactic implements FootballDimensions {

	/**
	 * Constructor 4-3-3 tactic
	 * @param team
	 */
	public Tactic433(Team team) {
		super(team, "tactics/4-3-3.xml");
	}
}
