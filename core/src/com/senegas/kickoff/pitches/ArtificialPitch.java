package com.senegas.kickoff.pitches;

import com.badlogic.gdx.assets.AssetManager;

public class ArtificialPitch extends Pitch {

	public ArtificialPitch(AssetManager assetManager) {
		super(assetManager, "pitches/synthetic.tmx", 0.975f);
	}

	@Override
	public String getDescription() {
		return "Artificial pitch";
	}
}
