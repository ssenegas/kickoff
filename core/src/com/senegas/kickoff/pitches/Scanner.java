package com.senegas.kickoff.pitches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.senegas.kickoff.entities.Player;
import com.senegas.kickoff.screens.Match;

/**
 * Scanner
 * @author Sébastien Sénégas
 *
 */
public class Scanner implements FootballDimensions {
	private int zoomFactor;
	private boolean isVisible;
	private static final int[] ZOOM = {112, 92, 78, 230, 144};
	private boolean defaultMode;
	private Vector2 origin;
	private Match match;

	/**
	 * Constructor
	 * @param m the match
	 */
	public Scanner(Match m) {
		this.match = m;
		this.isVisible = true;
		this.zoomFactor = 3;
		this.defaultMode = true;
		this.origin = new Vector2(18, Gdx.graphics.getHeight() - 26);
	}
	
	/**
	 * Draw the scanner
	 */
	public void draw() {
		//batch.end();
		
		float ratio = (float) (ZOOM[zoomFactor] / PITCH_WIDTH_IN_PX);
		//Gdx.app.log("Scanner", "graphics.getHeight " + Gdx.graphics.getHeight());
		this.origin.y = Gdx.graphics.getHeight() - 26 - (float)PITCH_HEIGHT_IN_PX * ratio;
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		//shapeRenderer.setProjectionMatrix(match.camera.projection);
		match.shapeRenderer.begin(ShapeType.Filled);
		match.shapeRenderer.setColor(new Color(0x3f3f3f1f));
		// Translucent background pitch
		match.shapeRenderer.rect(this.origin.x, this.origin.y, (float)PITCH_WIDTH_IN_PX * ratio, (float)PITCH_HEIGHT_IN_PX * ratio);
		match.shapeRenderer.end();
		//Gdx.gl.glDisable(GL20.GL_BLEND);

		match.shapeRenderer.begin(ShapeType.Line);
		match.shapeRenderer.setColor(new Color(0x1f1f1f3f));
		Gdx.gl.glLineWidth(2);
		
		// Pitch border
		match.shapeRenderer.rect(this.origin.x, this.origin.y, (float)PITCH_WIDTH_IN_PX * ratio, (float)PITCH_HEIGHT_IN_PX * ratio);
		
		// Penalty area horizontal lines
		match.shapeRenderer.line( (float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + PENALTY_AREA_HEIGHT_IN_PX * ratio),
				(float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + PENALTY_AREA_HEIGHT_IN_PX * ratio));

		match.shapeRenderer.line( (float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + (PITCH_HEIGHT_IN_PX - PENALTY_AREA_HEIGHT_IN_PX) * ratio),
				(float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + (PITCH_HEIGHT_IN_PX - PENALTY_AREA_HEIGHT_IN_PX) * ratio));		

		// Half way horizontal line
		match.shapeRenderer.line( (float)(this.origin.x),
				(float)(this.origin.y + HALF_PITCH_HEIGHT_IN_PX * ratio),
				(float)(this.origin.x + PITCH_WIDTH_IN_PX * ratio),
				(float)(this.origin.y + HALF_PITCH_HEIGHT_IN_PX * ratio));									                  

		// Goal area horizontal lines
		match.shapeRenderer.line( (float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + GOAL_AREA_HEIGHT_IN_PX * ratio),
				(float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + GOAL_AREA_HEIGHT_IN_PX * ratio));

		match.shapeRenderer.line( (float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + (PITCH_HEIGHT_IN_PX - GOAL_AREA_HEIGHT_IN_PX) * ratio),
				(float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + (PITCH_HEIGHT_IN_PX - GOAL_AREA_HEIGHT_IN_PX) * ratio));					                  

		// Penalty area vertical lines
		match.shapeRenderer.line( (float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y),
				(float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + PENALTY_AREA_HEIGHT_IN_PX * ratio));
		match.shapeRenderer.line( (float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y),
				(float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + PENALTY_AREA_HEIGHT_IN_PX * ratio));

		match.shapeRenderer.line( (float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + (PITCH_HEIGHT_IN_PX - PENALTY_AREA_HEIGHT_IN_PX) * ratio),
				(float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + PITCH_HEIGHT_IN_PX * ratio));
		match.shapeRenderer.line( (float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + (PITCH_HEIGHT_IN_PX - PENALTY_AREA_HEIGHT_IN_PX) * ratio),
				(float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + PITCH_HEIGHT_IN_PX * ratio));						                  	

		// Goal area vertical lines
		match.shapeRenderer.line( (float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y),
				(float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + GOAL_AREA_HEIGHT_IN_PX * ratio));
		match.shapeRenderer.line( (float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y),
				(float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + GOAL_AREA_HEIGHT_IN_PX * ratio));

		match.shapeRenderer.line( (float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + (PITCH_HEIGHT_IN_PX - GOAL_AREA_HEIGHT_IN_PX) * ratio),
				(float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + PITCH_HEIGHT_IN_PX * ratio));
		match.shapeRenderer.line( (float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + (PITCH_HEIGHT_IN_PX - GOAL_AREA_HEIGHT_IN_PX) * ratio),
				(float)(this.origin.x + (HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX) * ratio),
				(float)(this.origin.y + PITCH_HEIGHT_IN_PX * ratio));
		match.shapeRenderer.end();

		match.shapeRenderer.begin(ShapeType.Filled);
		// Ball position
		match.shapeRenderer.setColor(new Color(1.0f, 1.0f, 1.0f, 0.6f));
		match.shapeRenderer.circle((float)(this.origin.x + (match.getBall().getPosition().x - OUTER_TOP_EDGE_X ) * ratio),
				             (float)(this.origin.y + (match.getBall().getPosition().y - OUTER_TOP_EDGE_Y ) * ratio), 3);
/*		shapeRenderer.line( (float)(this.origin.x + (match.getBall().getPosition().x - OUTER_TOP_EDGE_X ) * ratio),
				(float)(this.origin.y + (match.getBall().getPosition().y - OUTER_TOP_EDGE_Y ) * ratio),
				(float)(this.origin.x + 1 + (match.getBall().getPosition().x - OUTER_TOP_EDGE_X ) * ratio),  // + 1 for width
				(float)(this.origin.y + (match.getBall().getPosition().y - OUTER_TOP_EDGE_Y ) * ratio));*/
		
		// Player position
		match.shapeRenderer.setColor(new Color(1.0f, 0, 0, 0.6f));
		for (Player player : match.getHomeTeam().getPlayers())
			match.shapeRenderer.circle((float)(this.origin.x + (player.getPosition().x - OUTER_TOP_EDGE_X ) * ratio),
				             (float)(this.origin.y + (player.getPosition().y - OUTER_TOP_EDGE_Y ) * ratio), 5);
		match.shapeRenderer.setColor(new Color(0, 0, 1.0f, 0.6f));
		for (Player player : match.getAwayTeam().getPlayers())
			match.shapeRenderer.circle((float)(this.origin.x + (player.getPosition().x - OUTER_TOP_EDGE_X ) * ratio),
				             (float)(this.origin.y + (player.getPosition().y - OUTER_TOP_EDGE_Y ) * ratio), 5);
/*		shapeRenderer.line( (float)(this.origin.x + (match.player.getPosition().x - OUTER_TOP_EDGE_X ) * ratio),
				(float)(this.origin.y + (match.player.getPosition().y - OUTER_TOP_EDGE_Y ) * ratio),
				(float)(this.origin.x + 1 + (match.player.getPosition().x - OUTER_TOP_EDGE_X ) * ratio),  // + 1 for width
				(float)(this.origin.y + (match.player.getPosition().y - OUTER_TOP_EDGE_Y ) * ratio));*/

		match.shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		//batch.begin();
	}
	
	/**
	 * Toggle the scanner visibility
	 */
	public void switchVisible()
	{
		isVisible =! isVisible;
	}

	public void switchMode()
	{
		defaultMode =! defaultMode;
	}

	/**
	 * Toggle the zoom factor
	 */
	public void toggleZoom()
	{
		zoomFactor++;
		zoomFactor = zoomFactor % (ZOOM.length);
	}
}
