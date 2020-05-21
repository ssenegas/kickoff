package com.senegas.kickoff.pitches;

public final class FootballDimensionConstants {
	/* 1 inch = 2,54 cm */
	/* 1 foot -> 12 inches -> 30.48 cm */
	/** Meter to pixel ratio 1px = 3 inches = 7,62 cm = 0.0762 m (Magic number that
	 * fits with original Kick Off graphics) */
	public final static double METER_PER_PIXEL = 0.0762;
	public final static double CM_PER_PIXEL = 7.62;

	public final static int OUTER_TOP_EDGE_X = 176;
	public final static int OUTER_TOP_EDGE_Y = 64;
	public final static int OUTER_BOTTOM_EDGE_X = 176;
	public final static int OUTER_BOTTOM_EDGE_Y = 80;
	public final static int LEFT_POST_OUTEREDDGE_X = 0x194;
	public final static int RIGHT_POST_OUTEREDGE_X = 0x1FC;
	public final static int CROSSBAR_TOPEDGE = 0x24;
	public final static int LEFT_POST_INNEREDGE_X = 0x19C;                   
	public final static int RIGHT_POST_INNEREDGE_X = 0x1F4;                   
	public final static int CROSSBAR_BOTEDGE = 0x1C;                   

	/** The height of the pitch in pixel (given by the original Kick Off graphics) */
	public final static double	PITCH_HEIGHT_IN_PX = 1392;

	/** The width of the pitch in pixel (given by the original Kick Off graphics) */
	public final static double	PITCH_WIDTH_IN_PX = 914;

	/** The Football pitch half height in pixel (696 px, given by the original Kick Off graphics) */
	static public final double	HALF_PITCH_HEIGHT_IN_PX = PITCH_HEIGHT_IN_PX / 2;

	/** The Football pitch half width in pixels (456 px, given by the original Kick Off graphics) */
	static public final double	HALF_PITCH_WIDTH_IN_PX = PITCH_WIDTH_IN_PX / 2;

	/** The Football pitch in meters (106.0704 m) */
	static public final double PITCH_LENGTH_IN_M = PITCH_HEIGHT_IN_PX * METER_PER_PIXEL;

	/** The width of the pitch in meters (69.4944 m) */
	static public final double PITCH_WIDTH_IN_M = PITCH_WIDTH_IN_PX * METER_PER_PIXEL;

	/** The field of play is divided into two halves by a halfway line. */
	static public final double HALF_GROUND_HEIGHT = 53.03535;

	/** The half width of the pitch in meters */
	static public final double HALF_PITCH_WIDTH_IN_M = PITCH_WIDTH_IN_M / 2;

	/** The centre mark is indicated at the midpoint of the halfway line.
	 * A circle with a radius of 9.15 m (10 yds) */
	static public final double CIRCLE_RADIUS = 9.15;

	/** Within each penalty area, a penalty mark is made 11 m (12 yds) from
	 * the midpoint between the goalposts and equidistant to them. */
	static public final double PENALTY_MARK = 11.0;

	/** An arc of a circle with a radius of 9.15 m (10 yds) from each penalty
	 * mark is drawn outside the penalty area. */
	static public final double	PENALTY_ARC_RADIUS = 9.15;

	/** A penalty area is defined at each end of the field as follows:
	 * Two lines are drawn at right angles to the goal line, 16.5 m (18 yds) from the inside of each goalpost.
	 * These lines extend into the field of play for a distance of 16.5 m (18 yds) and are joined by a line drawn
	 * parallel with the goal line. The area bounded by these lines and the goal line is the penalty area. */
	static public final double	PENALTY_AREA_HEIGHT = 16.5;

	/** The half penalty area width */
	static public final double	HALF_PENALTY_AREA_WIDTH = 15.5;

	/** The penalty area width (m) */
	static public final double	PENALTY_AREA_WIDTH = HALF_PENALTY_AREA_WIDTH * 2;

	/** The half penalty area width in pixels */
	static public final double	HALF_PENALTY_AREA_WIDTH_IN_PX = 265;

	/** The penalty area height in pixels */
	static public final double	PENALTY_AREA_HEIGHT_IN_PX = 217;

	/** The penalty area width in pixels */
	static public final double	PENALTY_AREA_WIDTH_IN_PX = HALF_PENALTY_AREA_WIDTH_IN_PX * 2;

	/** They consist of two upright posts equidistant from the corner flagposts and joined at the
	 * top by a horizontal crossbar.
	 * The distance between the posts is 7.32 m (8 yds) */
	static public final double GOAL_WIDTH_IN_M = 7.32;

	/** The distance from the lower edge of the crossbar to the ground is 2.44 m (8 ft). */
	static public final double GOAL_HEIGHT_IN_M = 2.44;

	/** Two lines are drawn at right angles to the goal line, 5.5 m (6 yds) from the inside
	 * of each goalpost. These lines extend into the field of play for a distance of 5.5 m (6 yds)
	 * and are joined by a line drawn parallel with the goal line. */
	static public final double	GOAL_AREA_HEIGHT = 5.5;

	/** The half goal area width */
	static public final double	HALF_GOAL_AREA_WIDTH = 7.0;

	/** The goal area width (m) */
	static public final double	GOAL_AREA_WIDTH = 2 * HALF_GOAL_AREA_WIDTH;

	/** The half goal area width in pixels */
	static public final double	HALF_GOAL_AREA_WIDTH_IN_PX = 121;

	/**  The goal area width in pixels */
	static public final double	GOAL_AREA_WIDTH_IN_PX = 2*HALF_GOAL_AREA_WIDTH_IN_PX;

	/** The goal area height in pixels */
	static public final double	GOAL_AREA_HEIGHT_IN_PX = 73;

	static public final float OUTER_LEFT_TOUCHLINE_X = 176;

	static public final float OUTER_RIGHT_TOUCHLINE_X = 1089;

	static public final float OUTER_TOP_GOAL_LINE_Y = 64;

    static public final float OUTER_BOTTOM_GOAL_LINE_Y = 1455;

    private FootballDimensionConstants() {
	}
}
