package com.senegas.kickoff.pitches;

import com.badlogic.gdx.assets.AssetManager;

public enum PitchFactory {

    INSTANCE;

	private AssetManager assetManager;

	public void initialize(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	/**
	 * Make a pitch
	 * @param type
	 * @return a pitch
	 */
	public Pitch createPitch(PitchType type) {
		switch (type) {
			case CLASSIC:
				return new ClassicPitch(assetManager);
			case WET:
				return new WetPitch(assetManager);
			case SOGGY:
				return new SoggyPitch(assetManager);
			case ARTIFICIAL:
				return new ArtificialPitch(assetManager);
			case PLAYER_MANAGER:
				return new PlayerManagerPitch(assetManager);
			default:
				throw new IllegalArgumentException("Unknown pitch type: " + type);
	    }
	}

	public enum PitchType {
		CLASSIC, WET, SOGGY, ARTIFICIAL, PLAYER_MANAGER
	}
}
