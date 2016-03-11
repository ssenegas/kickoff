package com.senegas.kickoff.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.senegas.kickoff.KickOff;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = KickOff.TITLE + " v" + KickOff.VERSION;
		config.vSyncEnabled = true;
		//config.useGL30 = true;
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new KickOff(), config);
	}
}
