package com.senegas.kickoff.pitches;

import com.badlogic.gdx.assets.AssetManager;

public class PlayerManagerPitch extends Pitch {

	public PlayerManagerPitch(AssetManager assetManager) {
		super(assetManager, "pitches/playermanager.tmx", 0.975f);
	}

	@Override
	public String getDescription() {
		return "PlayerManager pitch";
	}
}
