package com.senegas.kickoff.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.senegas.kickoff.KickOff;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = KickOff.TITLE + " v" + KickOff.VERSION;
		config.vSyncEnabled = true;
		config.useGL30 = true;
		config.width = KickOff.APP_DESKTOP_WIDTH;
		config.height = KickOff.APP_DESKTOP_HEIGHT;
		config.forceExit = false;
		new LwjglApplication(new KickOff(), config);
	}
}
