package com.senegas.kickoff.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.senegas.kickoff.pitches.Pitch;
import com.senegas.kickoff.screens.Match;

import static com.senegas.kickoff.pitches.FootballDimensionConstants.*;

public enum MatchState implements State<Match> {

    INTRODUCTION() {
        @Override
        public void enter(final Match match) {
            match.crowd.play(0.2f);
            match.getHomeTeam().setupIntroduction();
            match.getAwayTeam().setupIntroduction();

            match.getCameraHelper().setPosition(MathUtils.clamp(0,
                    match.getCamera().viewportWidth / 2 * match.getCamera().zoom,
                    Pitch.WIDTH - match.getCamera().viewportWidth / 2 * match.getCamera().zoom),
                    MathUtils.clamp(Pitch.HEIGHT,
                            match.getCamera().viewportHeight / 2 * match.getCamera().zoom,
                            Pitch.HEIGHT - match.getCamera().viewportHeight / 2 * match.getCamera().zoom));

            match.getCameraHelper().setTarget(new Vector2(352, (int)(PITCH_HEIGHT_IN_PX / 2 + OUTER_TOP_EDGE_Y + 16)));
        }

        @Override
        public void update(final Match match) {
            if (match.getHomeTeam().isReady() && match.getAwayTeam().isReady()) {
//                float delay = 8; // seconds
//                Timer.schedule(new Timer.Task(){
//                    @Override
//                    public void run() {
//                        match.stateMachine.changeState(PREPAREFORKICKOFF);
//                    }
//                }, delay);
                match.stateMachine.changeState(PREPAREFORKICKOFF);
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
            match.getHomeTeam().setupKickoff(true);
            match.getAwayTeam().setupKickoff(false);
        }

        @Override
        public void update(Match match) {
            if (match.getHomeTeam().isReady() && match.getAwayTeam().isReady()) {
                match.stateMachine.changeState(INPLAY);
            }
        }

        @Override
        public void exit(Match match) {
            match.whistle.play(0.2f);
        }

        @Override
        public boolean onMessage(Match match, Telegram telegram) {
            return false;
        }
    },

    INPLAY() {
        @Override
        public void enter(Match match) {
            match.getCameraHelper().setTarget(null);
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
    },

    THROW_IN() {
        @Override
        public void enter(Match match) {
            match.getHomeTeam().setupThrowIn(new Vector3(match.getPitch().getLastIntersection(), 0), match.getLastTeamTouch() == match.getHomeTeam());
            match.getAwayTeam().setupThrowIn(new Vector3(match.getPitch().getLastIntersection(), 0), match.getLastTeamTouch() == match.getAwayTeam());
            match.getCameraHelper().setTarget(match.getPitch().getLastIntersection());
            long id = match.whistle.play(0.2f);
        }

        @Override
        public void update(Match match) {
        }

        @Override
        public void exit(Match match) {

        }

        @Override
        public boolean onMessage(Match match, Telegram telegram) {
            return false;
        }
    };

    @Override
    public void enter(Match match) {
    }

    @Override
    public void exit(Match match) {
    }

    @Override
    public boolean onMessage(Match match, Telegram telegram) {
        // We don't use messaging in this example
        return false;
    }
}
