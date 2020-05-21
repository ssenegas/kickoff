package com.senegas.kickoff.pitches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.senegas.kickoff.KickOff;
import com.senegas.kickoff.entities.Player;
import com.senegas.kickoff.screens.Match;

import static com.senegas.kickoff.pitches.FootballDimensionConstants.GOAL_AREA_HEIGHT_IN_PX;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.HALF_GOAL_AREA_WIDTH_IN_PX;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.HALF_PENALTY_AREA_WIDTH_IN_PX;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.HALF_PITCH_HEIGHT_IN_PX;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.HALF_PITCH_WIDTH_IN_PX;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_TOP_EDGE_X;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.OUTER_TOP_EDGE_Y;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.PENALTY_AREA_HEIGHT_IN_PX;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.PITCH_HEIGHT_IN_PX;
import static com.senegas.kickoff.pitches.FootballDimensionConstants.PITCH_WIDTH_IN_PX;

/**
 * Scanner
 * @author Sébastien Sénégas
 *
 */
public class Scanner {
    private static final String TAG = Scanner.class.getSimpleName();

    private static final int BALL_RADIUS = 2;
    private static final int PLAYER_RADIUS = 4;

    private int zoomFactor;
    private float ratio;
    private boolean isVisible;
    private static final int[] ZOOM = {112, 92, 78, 230, 144};
    private boolean defaultMode;
    private boolean withBackground;
    private Vector2 origin;
    private Match match;
    /**
     * Constructor
     * @param match the match
     */
    public Scanner(Match match) {
        this.match = match;
        this.isVisible = true;
        this.zoomFactor = 3;
        this.ratio = (float) (ZOOM[this.zoomFactor] / PITCH_WIDTH_IN_PX);
        this.defaultMode = true;
        this.withBackground = true;
        this.origin = new Vector2(18, KickOff.V_HEIGHT - 26);

        Gdx.app.log(TAG, "constructor");
    }

    /**
     * Draw the scanner
     */
    public void draw(ShapeRenderer shapeBatch) {
        this.origin.y = KickOff.V_HEIGHT - 26 - (float) PITCH_HEIGHT_IN_PX * this.ratio;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (this.withBackground) {
            drawTranslucentPitchBackground(shapeBatch);
        }
        drawPitch(shapeBatch);
        drawBall(shapeBatch);
        drawPlayers(shapeBatch);

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawPlayers(ShapeRenderer shapeBatch) {
        shapeBatch.begin(ShapeType.Filled);

        // Players positions
        Color homeColor = this.match.getHomeTeam().getMainColor();
        shapeBatch.setColor(new Color(homeColor.r, homeColor.g, homeColor.b, 0.6f));
        for (Player player : this.match.getHomeTeam().getPlayers()) {
            drawPlayer(shapeBatch, player);
        }
        Color awayColor = this.match.getAwayTeam().getMainColor();
        shapeBatch.setColor(new Color(awayColor.r, awayColor.g, awayColor.b, 0.6f));
        for (Player player : this.match.getAwayTeam().getPlayers()) {
            drawPlayer(shapeBatch, player);
        }

        shapeBatch.end();
    }

    private void drawPlayer(ShapeRenderer shapeBatch, Player player) {
        shapeBatch.circle((float) (this.origin.x + (player.getPosition().x - OUTER_TOP_EDGE_X) * this.ratio),
                (float) (this.origin.y + (player.getPosition().y - OUTER_TOP_EDGE_Y) * this.ratio), PLAYER_RADIUS);

        // old fashion with line instead circle
/*        app.shapeBatch.line( (float)(this.origin.x + (player.getPosition().x - OUTER_TOP_EDGE_X ) * ratio),
				(float)(this.origin.y + (player.getPosition().y - OUTER_TOP_EDGE_Y ) * ratio),
				(float)(this.origin.x + 1 + (player.getPosition().x - OUTER_TOP_EDGE_X ) * ratio),  // + 1 for width
				(float)(this.origin.y + (player.getPosition().y - OUTER_TOP_EDGE_Y ) * ratio));*/
    }

    private void drawBall(ShapeRenderer shapeBatch) {
        shapeBatch.begin(ShapeType.Filled);
        // Ball position
        shapeBatch.setColor(new Color(1.0f, 1.0f, 1.0f, 0.6f));
        shapeBatch.circle((float) (this.origin.x + (this.match.getBall().getPosition().x - OUTER_TOP_EDGE_X) * this.ratio),
                (float) (this.origin.y + (this.match.getBall().getPosition().y - OUTER_TOP_EDGE_Y) * this.ratio), BALL_RADIUS);
        // old fashion with line instead circle
        /*		app.shapeBatch.line( (float)(this.origin.x + (match.getBall().getPosition().x - OUTER_TOP_EDGE_X ) * ratio),
				(float)(this.origin.y + (match.getBall().getPosition().y - OUTER_TOP_EDGE_Y ) * ratio),
				(float)(this.origin.x + 1 + (match.getBall().getPosition().x - OUTER_TOP_EDGE_X ) * ratio),  // + 1 for width
				(float)(this.origin.y + (match.getBall().getPosition().y - OUTER_TOP_EDGE_Y ) * ratio));*/
        shapeBatch.end();
    }

    private void drawTranslucentPitchBackground(ShapeRenderer shapeBatch) {
        shapeBatch.begin(ShapeType.Filled);
        shapeBatch.setColor(new Color(0x3f3f3f1f));
        // Translucent background pitch
        shapeBatch.rect(this.origin.x, this.origin.y, (float) PITCH_WIDTH_IN_PX * this.ratio, (float) PITCH_HEIGHT_IN_PX * this.ratio);
        shapeBatch.end();
    }

    private void drawPitch(ShapeRenderer shapeBatch) {
        shapeBatch.begin(ShapeType.Line);
        shapeBatch.setColor(new Color(0x1f1f1f3f));
        Gdx.gl.glLineWidth(2);

        drawPitchBoundary(shapeBatch);
        drawGoalAreas(shapeBatch);
        drawPenaltyAreas(shapeBatch);

        shapeBatch.end();
    }

    private void drawPitchBoundary(ShapeRenderer shapeBatch) {
        // Pitch border
        shapeBatch.rect(this.origin.x, this.origin.y,
                (float) PITCH_WIDTH_IN_PX * this.ratio, (float) PITCH_HEIGHT_IN_PX * this.ratio);
        // Half way horizontal line
        shapeBatch.line((float) (this.origin.x), (float) (this.origin.y + HALF_PITCH_HEIGHT_IN_PX * this.ratio),
                (float) (this.origin.x + PITCH_WIDTH_IN_PX * this.ratio), (float) (this.origin.y + HALF_PITCH_HEIGHT_IN_PX * this.ratio));
    }

    private void drawGoalAreas(ShapeRenderer shapeBatch) {
        // Goal area horizontal lines
        shapeBatch.line((float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + GOAL_AREA_HEIGHT_IN_PX * this.ratio),
                (float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + GOAL_AREA_HEIGHT_IN_PX * this.ratio));

        shapeBatch.line((float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + (PITCH_HEIGHT_IN_PX - GOAL_AREA_HEIGHT_IN_PX) * this.ratio),
                (float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + (PITCH_HEIGHT_IN_PX - GOAL_AREA_HEIGHT_IN_PX) * this.ratio));


        // Goal area vertical lines
        shapeBatch.line((float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX) * this.ratio),
                (float)(this.origin.y),
                (float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + GOAL_AREA_HEIGHT_IN_PX * this.ratio));
        shapeBatch.line((float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX) * this.ratio),
                (float)(this.origin.y),
                (float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + GOAL_AREA_HEIGHT_IN_PX * this.ratio));

        shapeBatch.line((float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + (PITCH_HEIGHT_IN_PX - GOAL_AREA_HEIGHT_IN_PX) * this.ratio),
                (float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + PITCH_HEIGHT_IN_PX * this.ratio));
        shapeBatch.line((float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + (PITCH_HEIGHT_IN_PX - GOAL_AREA_HEIGHT_IN_PX) * this.ratio),
                (float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + PITCH_HEIGHT_IN_PX * this.ratio));
    }

    private void drawPenaltyAreas(ShapeRenderer shapeBatch) {
        // Penalty area horizontal lines
        shapeBatch.line((float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + PENALTY_AREA_HEIGHT_IN_PX * this.ratio),
                (float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + PENALTY_AREA_HEIGHT_IN_PX * this.ratio));

        shapeBatch.line((float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + (PITCH_HEIGHT_IN_PX - PENALTY_AREA_HEIGHT_IN_PX) * this.ratio),
                (float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + (PITCH_HEIGHT_IN_PX - PENALTY_AREA_HEIGHT_IN_PX) * this.ratio));

        // Penalty area vertical lines
        shapeBatch.line((float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX) * this.ratio),
                (float)(this.origin.y),
                (float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + PENALTY_AREA_HEIGHT_IN_PX * this.ratio));
        shapeBatch.line((float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX) * this.ratio),
                (float)(this.origin.y),
                (float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + PENALTY_AREA_HEIGHT_IN_PX * this.ratio));

        shapeBatch.line((float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + (PITCH_HEIGHT_IN_PX - PENALTY_AREA_HEIGHT_IN_PX) * this.ratio),
                (float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + PITCH_HEIGHT_IN_PX * this.ratio));
        shapeBatch.line((float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + (PITCH_HEIGHT_IN_PX - PENALTY_AREA_HEIGHT_IN_PX) * this.ratio),
                (float) (this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX) * this.ratio),
                (float) (this.origin.y + PITCH_HEIGHT_IN_PX * this.ratio));
    }

    /**
     * Toggle the scanner visibility
     */
    public void switchVisible()	{
        this.isVisible = !this.isVisible;
    }

    public void switchMode() {
        this.defaultMode = !this.defaultMode;
    }

    /**
     * Toggle the zoom factor
     */
    public void toggleZoom() {
        this.zoomFactor++;
        this.zoomFactor = this.zoomFactor % (ZOOM.length);
        this.ratio = (float) (ZOOM[this.zoomFactor] / PITCH_WIDTH_IN_PX);
    }
}
