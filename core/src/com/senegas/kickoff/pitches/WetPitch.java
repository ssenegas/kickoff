package com.senegas.kickoff.pitches;

import com.badlogic.gdx.assets.AssetManager;

public class WetPitch extends Pitch {

	public WetPitch(AssetManager assetManager) {
		super(assetManager, "pitches/wet.tmx", 0.775f);
	}

	@Override
	public String getDescription() {
		return "Wet pitch";
	}
}
