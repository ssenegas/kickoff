package com.senegas.kickoff.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.senegas.kickoff.pitches.FootballDimensionConstants;
import com.senegas.kickoff.pitches.Pitch;
import com.senegas.kickoff.screens.MatchScreen;
import com.senegas.kickoff.utils.Direction;

import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_TOP_EDGE_Y;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.PITCH_HEIGHT_IN_PX;

public enum MatchState implements State<MatchScreen> {

    INTRODUCTION() {
        private float elapsedTimeInSeconds;

        @Override
        public void enter(MatchScreen match) {
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
        public void update(MatchScreen match) {
            this.elapsedTimeInSeconds += Gdx.graphics.getDeltaTime();
            //System.out.println("startTime: " + this.elapsedTimeInSeconds);
            boolean timeOut = (this.elapsedTimeInSeconds > 7);

            if (match.getHomeTeam().isReady() && match.getAwayTeam().isReady() && timeOut) {
                match.stateMachine.changeState(PREPAREFORKICKOFF);
            }
        }

        @Override
        public void exit(MatchScreen match) {
        }

        @Override
        public boolean onMessage(MatchScreen match, Telegram telegram) {
            return false;
        }
    },

    PREPAREFORKICKOFF() {
        private float elapsedTimeInSeconds;

        @Override
        public void enter(MatchScreen match) {
            match.getCameraHelper().setTarget(match.getPitch().getCenterSpot());
            match.getHomeTeam().setupKickoff(true);
            match.getAwayTeam().setupKickoff(false);
        }

        @Override
        public void update(MatchScreen match) {
            if (match.getHomeTeam().isReady() && match.getAwayTeam().isReady()) {
                this.elapsedTimeInSeconds += Gdx.graphics.getDeltaTime();
                boolean timeOut = (this.elapsedTimeInSeconds > 2);
                if (timeOut) {
                    match.stateMachine.changeState(INPLAY);
                }
            }
        }

        @Override
        public void exit(MatchScreen match) {
            match.whistle.play(0.2f);
        }

        @Override
        public boolean onMessage(MatchScreen match, Telegram telegram) {
            return false;
        }
    },

    INPLAY() {
        @Override
        public void enter(MatchScreen match) {
            match.getCameraHelper().setTarget(null);
        }

        @Override
        public void update(MatchScreen match) {
            match.getCameraHelper().setPosition(MathUtils.clamp(match.getBall().getPosition().x, match.getCamera().viewportWidth / 2 * match.getCamera().zoom, Pitch.WIDTH - match.getCamera().viewportWidth / 2 * match.getCamera().zoom),
                    MathUtils.clamp(match.getBall().getPosition().y, match.getCamera().viewportHeight / 2 * match.getCamera().zoom, Pitch.HEIGHT - match.getCamera().viewportHeight / 2 * match.getCamera().zoom));

            match.getHomeTeam().getTactic().update(match.getBall().getPosition());
            match.getAwayTeam().getTactic().update(match.getBall().getPosition());
        }

        @Override
        public void exit(MatchScreen match) {

        }

        @Override
        public boolean onMessage(MatchScreen match, Telegram telegram) {
            return false;
        }
    },

    THROW_IN() {
        @Override
        public void enter(MatchScreen match) {
            long id = match.whistle.play(0.2f);
            match.getHomeTeam().setupThrowIn(new Vector3(match.getPitch().getLastIntersection(), 0), match.getLastTeamTouch() == match.getHomeTeam());
            match.getAwayTeam().setupThrowIn(new Vector3(match.getPitch().getLastIntersection(), 0), match.getLastTeamTouch() == match.getAwayTeam());
            match.getCameraHelper().setTarget(match.getPitch().getLastIntersection());
        }

        @Override
        public void update(MatchScreen match) {
            //match.getPitch().showLastIntersection(match.getApp().shapeBatch);
            if (match.getHomeTeam().isReady() && match.getAwayTeam().isReady()) {
                animateThrowIn(match);
                match.stateMachine.changeState(INPLAY);
            }
        }

        private void animateThrowIn(MatchScreen match) {
            Vector3 position = new Vector3(match.getPitch().getLastIntersection(), 20);
            int direction = 0;
            if (match.getPitch().getLastIntersection().x < FootballDimensionConstants.HALF_PITCH_WIDTH_IN_PX) { // right side
                direction = 90;
                match.getBall().setPosition(position.add(5, 0, 0));
            } else { // left side
                direction = 270;
                match.getBall().setPosition(position.add(-5, 0, 0));
            }
            Vector3 dv = Direction.EAST.getDirectionVector();
            Vector3 velocity = dv.scl(200);
            velocity.z = 80;
            match.getBall().kick(velocity, null);
        }

        @Override
        public void exit(MatchScreen match) {

        }

        @Override
        public boolean onMessage(MatchScreen match, Telegram telegram) {
            return false;
        }
    };

    @Override
    public void enter(MatchScreen match) {
    }

    @Override
    public void exit(MatchScreen match) {
    }

    @Override
    public boolean onMessage(MatchScreen match, Telegram telegram) {
        // We don't use messaging in this example
        return false;
    }
}
