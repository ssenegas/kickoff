package com.senegas.kickoff.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.senegas.kickoff.pitches.FootballDimensions;
import com.senegas.kickoff.screens.Match;

public class Scanner implements FootballDimensions {
	private ShapeRenderer shapeRenderer;
	private int zoomFactor;
	private boolean isVisible;
	private boolean isViewportVisible;
	private static final int[] ZOOM = {112, 92, 78, 230, 144};
	private boolean defaultMode;
	private Vector2 origin;
	Match match;

	public Scanner(Match m) {
		match = m;
		isVisible = true;
		isViewportVisible = false;
		zoomFactor = 3;
		defaultMode = true;
		origin = new Vector2(18, Gdx.graphics.getHeight() - 26);
		shapeRenderer = new ShapeRenderer();
	}
	
	public void draw() {
		//batch.end();
		
		float ratio = (float) (ZOOM[zoomFactor] / PITCH_WIDTH_IN_PX);
		origin.y = Gdx.graphics.getHeight() - 26 - (float)PITCH_HEIGHT_IN_PX * ratio;
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0, 0, 0, 0.2f));
		// Translucent background pitch
		shapeRenderer.rect(origin.x, origin.y, (float)PITCH_WIDTH_IN_PX * ratio, (float)PITCH_HEIGHT_IN_PX * ratio);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		Gdx.gl.glLineWidth(2);
		
		// Pitch border
		shapeRenderer.rect(origin.x, origin.y, (float)PITCH_WIDTH_IN_PX * ratio, (float)PITCH_HEIGHT_IN_PX * ratio);
		
		// Penalty area horizontal lines
		shapeRenderer.line( (float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX ) * ratio ),
				(float)(origin.y + PENALTY_AREA_HEIGHT_IN_PX * ratio ),
				(float)(origin.x + ( HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX ) * ratio ),
				(float)(origin.y + PENALTY_AREA_HEIGHT_IN_PX * ratio ) );

		shapeRenderer.line( (float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + (PITCH_HEIGHT_IN_PX - PENALTY_AREA_HEIGHT_IN_PX ) * ratio),
				(float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + (PITCH_HEIGHT_IN_PX - PENALTY_AREA_HEIGHT_IN_PX ) * ratio ) );		

		// Half way horizontal line
		shapeRenderer.line( (float)( origin.x),
				(float)( origin.y + HALF_PITCH_HEIGHT_IN_PX * ratio),
				(float)( origin.x + PITCH_WIDTH_IN_PX * ratio),
				(float)( origin.y + HALF_PITCH_HEIGHT_IN_PX * ratio) );									                  

		// Goal area horizontal lines
		shapeRenderer.line( (float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + GOAL_AREA_HEIGHT_IN_PX * ratio),
				(float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + GOAL_AREA_HEIGHT_IN_PX * ratio ) );

		shapeRenderer.line( (float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + (PITCH_HEIGHT_IN_PX - GOAL_AREA_HEIGHT_IN_PX ) * ratio),
				(float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + (PITCH_HEIGHT_IN_PX - GOAL_AREA_HEIGHT_IN_PX ) * ratio ) );					                  

		// Penalty area vertical lines
		shapeRenderer.line( (float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y),
				(float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + PENALTY_AREA_HEIGHT_IN_PX * ratio ) );
		shapeRenderer.line( (float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y),
				(float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + PENALTY_AREA_HEIGHT_IN_PX * ratio ) );

		shapeRenderer.line( (float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + (PITCH_HEIGHT_IN_PX - PENALTY_AREA_HEIGHT_IN_PX ) * ratio),
				(float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX - HALF_PENALTY_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + PITCH_HEIGHT_IN_PX * ratio ) );	
		shapeRenderer.line( (float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + (PITCH_HEIGHT_IN_PX - PENALTY_AREA_HEIGHT_IN_PX ) * ratio),
				(float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX + HALF_PENALTY_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + PITCH_HEIGHT_IN_PX * ratio ) );						                  	

		// Goal area vertical lines
		shapeRenderer.line( (float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y),
				(float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + GOAL_AREA_HEIGHT_IN_PX * ratio ) );
		shapeRenderer.line( (float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y),
				(float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + GOAL_AREA_HEIGHT_IN_PX * ratio ) );           

		shapeRenderer.line( (float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + (PITCH_HEIGHT_IN_PX - GOAL_AREA_HEIGHT_IN_PX ) * ratio),
				(float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX - HALF_GOAL_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + PITCH_HEIGHT_IN_PX * ratio ) );	
		shapeRenderer.line( (float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + (PITCH_HEIGHT_IN_PX - GOAL_AREA_HEIGHT_IN_PX ) * ratio),
				(float)( origin.x + ( HALF_PITCH_WIDTH_IN_PX + HALF_GOAL_AREA_WIDTH_IN_PX ) * ratio),
				(float)( origin.y + PITCH_HEIGHT_IN_PX * ratio ) );
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Filled);
		// Player position
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle((float)(origin.x + (match.player.getPosition().x - OUTER_TOP_EDGE_X ) * ratio), (float)(origin.y + (match.player.getPosition().y - OUTER_TOP_EDGE_Y ) * ratio), 2);
/*		shapeRenderer.line( (float)(origin.x + (match.player.getPosition().x - OUTER_TOP_EDGE_X ) * ratio),
				(float)(origin.y + (match.player.getPosition().y - OUTER_TOP_EDGE_Y ) * ratio),
				(float)(origin.x + 1 + (match.player.getPosition().x - OUTER_TOP_EDGE_X ) * ratio),  // + 1 for width
				(float)(origin.y + (match.player.getPosition().y - OUTER_TOP_EDGE_Y ) * ratio ) );*/
		
		// Ball position
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.circle((float)(origin.x + (match.ball.getPosition().x - OUTER_TOP_EDGE_X ) * ratio), (float)(origin.y + (match.ball.getPosition().y - OUTER_TOP_EDGE_Y ) * ratio), 2);
/*		shapeRenderer.line( (float)(origin.x + (match.ball.getPosition().x - OUTER_TOP_EDGE_X ) * ratio),
				(float)(origin.y + (match.ball.getPosition().y - OUTER_TOP_EDGE_Y ) * ratio),
				(float)(origin.x + 1 + (match.ball.getPosition().x - OUTER_TOP_EDGE_X ) * ratio),  // + 1 for width
				(float)(origin.y + (match.ball.getPosition().y - OUTER_TOP_EDGE_Y ) * ratio ) );*/
		shapeRenderer.end();
		
		//batch.begin();
	}
	
	public void switchVisible()
	{
		isVisible=!isVisible;
	}

	public void switchViewport()
	{
		isViewportVisible=!isViewportVisible;
	}

	public void switchMode()
	{
		defaultMode=!defaultMode;
	}

	public void toggleZoom()
	{
		zoomFactor++;
		zoomFactor=zoomFactor%(ZOOM.length);
	}
}
