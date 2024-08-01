package com.senegas.kickoff.pitches;

import com.badlogic.gdx.assets.AssetManager;
import com.senegas.kickoff.KickOff;

public class ClassicPitch extends Pitch {

	public ClassicPitch(AssetManager assetManager) {
		super(assetManager, "pitches/classic.tmx", 0.975f);
	}

	@Override
	public String getDescription() {
		return "Classic pitch";
	}
}
