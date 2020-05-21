package com.senegas.kickoff.managers;

import com.senegas.kickoff.KickOff;
import com.senegas.kickoff.screens.AbstractScreen;
import com.senegas.kickoff.screens.MainMenu;
import com.senegas.kickoff.screens.Match;

import java.util.HashMap;

public class GameScreenManager {

    private final KickOff app;

    private HashMap<STATE, AbstractScreen> gameScreens;

    public enum STATE {
        MAIN_MENU,
        PLAY,
        SETTINGS
    }

    public GameScreenManager(KickOff app) {
        this.app = app;

        initGameScreens();
        setScreen(STATE.MAIN_MENU);
    }

    private void initGameScreens() {
        this.gameScreens = new HashMap<>();
        this.gameScreens.put(STATE.MAIN_MENU, new MainMenu(this.app));
        this.gameScreens.put(STATE.PLAY, new Match(this.app));
    }

    public void setScreen(STATE nextScreen) {
        this.app.setScreen(this.gameScreens.get(nextScreen));
    }

    public void dispose() {
        for (AbstractScreen screen : this.gameScreens.values()) {
            if (screen != null){
                screen.dispose();
            }
        }
    }
}
