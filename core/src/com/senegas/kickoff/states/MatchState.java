package com.senegas.kickoff.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.senegas.kickoff.pitches.Pitch;
import com.senegas.kickoff.screens.Match;

public enum MatchState implements State<Match> {

    INTRODUCTION() {
        @Override
        public void enter(final Match match) {

            long id = match.crowd.play(0.2f);

            match.getHomeTeam().setupIntroduction();
            match.getAwayTeam().setupIntroduction();

            match.getCameraHelper().setPosition(MathUtils.clamp(match.getBall().getPosition().x,
                    match.getCamera().viewportWidth / 2 * match.getCamera().zoom,
                    Pitch.WIDTH - match.getCamera().viewportWidth / 2 * match.getCamera().zoom),
                    MathUtils.clamp(match.getBall().getPosition().y,
                            match.getCamera().viewportHeight / 2 * match.getCamera().zoom,
                            Pitch.HEIGHT - match.getCamera().viewportHeight / 2 * match.getCamera().zoom));

            match.getCameraHelper().setTarget(new Vector2(352, (int)(Pitch.PITCH_HEIGHT_IN_PX / 2 + Pitch.OUTER_TOP_EDGE_Y + 16)));
        }

        @Override
        public void update(final Match match) {
            if (match.getHomeTeam().isReady() && match.getAwayTeam().isReady()) {
                float delay = 7; // seconds
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        match.stateMachine.changeState(PREPAREFORKICKOFF);
                    }
                }, delay);
            }
        }

        @Override
        public void exit(Match match) {

        }

        @Override
        public boolean onMessage(Match match, Telegram telegram) {
            return false;
        }
    },

    PREPAREFORKICKOFF() {
        @Override
        public void enter(final Match match) {
            match.getCameraHelper().setTarget(match.getPitch().getCenterSpot());

            match.getHomeTeam().getTactic().setupKickoff(true);
            match.getAwayTeam().getTactic().setupKickoff(false);

            float delay = 5; // seconds
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    //long id = match.whistle.play(0.2f);
                    match.getCameraHelper().setTarget(null);
                    match.stateMachine.changeState(INPLAY);
                }
            }, delay);
        }

        @Override
        public void update(Match match) {
            if (!match.getCameraHelper().hasTarget()) {
                match.getCameraHelper().setPosition(MathUtils.clamp(match.getBall().getPosition().x, match.getCamera().viewportWidth / 2 * match.getCamera().zoom, Pitch.WIDTH - match.getCamera().viewportWidth / 2 * match.getCamera().zoom),
                        MathUtils.clamp(match.getBall().getPosition().y, match.getCamera().viewportHeight / 2 * match.getCamera().zoom, Pitch.HEIGHT - match.getCamera().viewportHeight / 2 * match.getCamera().zoom));
            }
        }

        @Override
        public void exit(Match match) {

        }

        @Override
        public boolean onMessage(Match match, Telegram telegram) {
            return false;
        }
    },

    INPLAY() {
        @Override
        public void enter(Match match) {
        }

        @Override
        public void update(Match match) {
            match.getCameraHelper().setPosition(MathUtils.clamp(match.getBall().getPosition().x, match.getCamera().viewportWidth / 2 * match.getCamera().zoom, Pitch.WIDTH - match.getCamera().viewportWidth / 2 * match.getCamera().zoom),
                    MathUtils.clamp(match.getBall().getPosition().y, match.getCamera().viewportHeight / 2 * match.getCamera().zoom, Pitch.HEIGHT - match.getCamera().viewportHeight / 2 * match.getCamera().zoom));
            match.getHomeTeam().getTactic().update(match.getBall());
            match.getAwayTeam().getTactic().update(match.getBall());
        }

        @Override
        public void exit(Match match) {

        }

        @Override
        public boolean onMessage(Match match, Telegram telegram) {
            return false;
        }
    }
}
