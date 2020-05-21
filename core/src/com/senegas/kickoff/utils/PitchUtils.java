package com.senegas.kickoff.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_BOTTOM_EDGE_X;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_BOTTOM_EDGE_Y;

public final class PitchUtils {
	
	public static Vector2 globalToPitch(Vector2 vector) {
		return new Vector2(vector.x - OUTER_BOTTOM_EDGE_X, vector.y - OUTER_BOTTOM_EDGE_Y);
	}
	
	public static Vector3 pitchToGlobal(Vector3 vector) {
		return new Vector3(vector.x + OUTER_BOTTOM_EDGE_X, vector.y + OUTER_BOTTOM_EDGE_Y, 0);
	}

	public static Vector2 globalToPitch(float x, float y) {
		return new Vector2(x - OUTER_BOTTOM_EDGE_X, y - OUTER_BOTTOM_EDGE_Y);
	}
	
	public static Vector3 pitchToGlobal(float x, float y) {
		return new Vector3(x + OUTER_BOTTOM_EDGE_X, y + OUTER_BOTTOM_EDGE_Y, 0);
	}

	public static float vectorToAngle(Vector2 vector) {
		return (float) Math.atan2(-vector.x, vector.y);
	}

	public static Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = -(float) Math.sin(angle);
		outVector.y = (float) Math.cos(angle);
		return outVector;
	}
}
