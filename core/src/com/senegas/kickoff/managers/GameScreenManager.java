package com.senegas.kickoff.managers;

import com.senegas.kickoff.KickOff;
import com.senegas.kickoff.screens.AbstractScreen;
import com.senegas.kickoff.screens.LoadingScreen;
import com.senegas.kickoff.screens.MainMenuScreen;
import com.senegas.kickoff.screens.MatchScreen;
import com.senegas.kickoff.screens.SplashScreen;

import java.util.HashMap;

public class GameScreenManager {

    private final KickOff app;

    private HashMap<STATE, AbstractScreen> gameScreens;

    public enum STATE {
        MAIN_MENU,
        PLAY,
        LOADING,
        SPLASH,
        SETTINGS
    }

    public GameScreenManager(KickOff app) {
        this.app = app;

        initGameScreens();
        setScreen(STATE.LOADING);
    }

    private void initGameScreens() {
        this.gameScreens = new HashMap<>();
        this.gameScreens.put(STATE.LOADING, new LoadingScreen(this.app));
        this.gameScreens.put(STATE.SPLASH, new SplashScreen(this.app));
        this.gameScreens.put(STATE.MAIN_MENU, new MainMenuScreen(this.app));
        this.gameScreens.put(STATE.PLAY, new MatchScreen(this.app));
    }

    public void setScreen(STATE nextScreen) {
        this.app.setScreen(this.gameScreens.get(nextScreen));
    }

    public void dispose() {
        for (AbstractScreen screen : this.gameScreens.values()) {
            if (screen != null) {
                screen.dispose();
            }
        }
    }
}
