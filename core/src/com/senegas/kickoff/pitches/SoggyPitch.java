package com.senegas.kickoff.pitches;

import com.badlogic.gdx.assets.AssetManager;

public class SoggyPitch extends Pitch {

	public SoggyPitch(AssetManager assetManager) {
		super(assetManager, "pitches/soggy.tmx", 1.125f);
	}

	@Override
	public String getDescription() {
		return "Soggy pitch";
	}
}
