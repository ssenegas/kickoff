package com.senegas.kickoff.utils;

import com.badlogic.gdx.math.Vector3;

public enum Direction {
    NORTH(new Vector3(0, 1, 0)),
    NORTH_EAST(new Vector3(1, 1, 0).nor()),
    EAST(new Vector3(1, 0, 0)),
    SOUTH_EAST(new Vector3(1, -1, 0).nor()),
    SOUTH(new Vector3(0, -1, 0)),
    SOUTH_WEST(new Vector3(-1, -1, 0).nor()),
    WEST(new Vector3(-1, 0, 0)),
    NORTH_WEST(new Vector3(-1, 1, 0).nor());

    private final Vector3 directionVector;

    Direction(Vector3 directionVector) {
        this.directionVector = directionVector;
    }

    public Vector3 getDirectionVector() {
        // Ensure the vector is immutable by creating a new instance
        return new Vector3(directionVector);
    }

    public static Direction getDirection(float dx, float dy) {
        double angle = Math.atan2(dy, dx);
        double degree = Math.toDegrees(angle);

        if (degree < 0) {
            degree += 360;
        }

        if (degree >= 337.5 || degree < 22.5) {
            return Direction.EAST;
        } else if (degree >= 22.5 && degree < 67.5) {
            return Direction.NORTH_EAST;
        } else if (degree >= 67.5 && degree < 112.5) {
            return Direction.NORTH;
        } else if (degree >= 112.5 && degree < 157.5) {
            return Direction.NORTH_WEST;
        } else if (degree >= 157.5 && degree < 202.5) {
            return Direction.WEST;
        } else if (degree >= 202.5 && degree < 247.5) {
            return Direction.SOUTH_WEST;
        } else if (degree >= 247.5 && degree < 292.5) {
            return Direction.SOUTH;
        } else if (degree >= 292.5 && degree < 337.5) {
            return Direction.SOUTH_EAST;
        }

        return Direction.SOUTH;  // Default, should never hit this.
    }
}
