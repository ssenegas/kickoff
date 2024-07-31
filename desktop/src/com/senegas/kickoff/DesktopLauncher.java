package com.senegas.kickoff;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.senegas.kickoff.KickOff;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle(KickOff.TITLE + " v" + KickOff.VERSION);
		config.setWindowedMode(KickOff.APP_DESKTOP_WIDTH, KickOff.APP_DESKTOP_HEIGHT);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new KickOff(), config);
	}
}
