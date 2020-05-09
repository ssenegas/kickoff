package com.senegas.kickoff.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.senegas.kickoff.pitches.Pitch;

import static com.senegas.kickoff.pitches.FootballDimensionConstants.*;

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
}
