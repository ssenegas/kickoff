package com.senegas.kickoff.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Joystick
{	
	private float MaxDistance = 2000.0f; //in pixels non sqrt
	private float MaxDistanceSqrt = (float)Math.sqrt((double)MaxDistance);
	
	private float CentreX = -1;
	private float CentreY = -1;
	
	private float CurrentX;
	private float CurrentY;
	
	public int FingerId = -1;
	
	public float DeadZone = 0.1f;
	
	Vector2 tmp = new Vector2();
	Vector2 CentreVector = new Vector2();
	
	public Rectangle Area;
	
	public Joystick()
	{
		
	}
	
	public void SetMaxDistance(float maxDistanceInPixels)
	{
		MaxDistance = (float)Math.pow((double)maxDistanceInPixels, 2);
		MaxDistanceSqrt = maxDistanceInPixels;
	}
	
	public void Update(float delta)
	{
		if (FingerId == -1)
			return;
		
		if (Gdx.input.isTouched(FingerId) == false)
		{
			FingerId = -1;
			
			CentreX = -1;
			CentreY = -1;
			
			return;
		}
		
		
		CurrentX = Gdx.input.getX(FingerId);
		CurrentY = Gdx.input.getY(FingerId);
		
		//Reverse height, so we get graphics height instead of touch height (touch 0,0 = top left; graphics 0,0 = bottom left)
		CurrentY = Gdx.graphics.getHeight() - CurrentY;		
		
		//On first touch, set the centre
		if (CentreX == -1 && CentreY == -1)
		{
			CentreX = CurrentX;
			CentreY = CurrentY;
		}
		
		float x = CentreX - CurrentX;
		float y = CentreY - CurrentY;
		
		float dis = (x * x) + (y * y);
				
		if (dis > MaxDistance)
		{			
			dis = (float)Math.sqrt((double)dis);
			
			float dif = dis - MaxDistanceSqrt;			
			
			CentreVector.x = CentreX;
			CentreVector.y = CentreY;
			
			CentreVector.sub(CurrentX, CurrentY);
			CentreVector.nor(); // Direction
			CentreVector.scl(dif);
			
			CentreX -= CentreVector.x;
			CentreY -= CentreVector.y;
			
			if (!Area.contains(CentreX, CentreY))
			{
				CentreX += CentreVector.x;
				CentreY += CentreVector.y;
			}
		}
		
	}
	
	public Vector2 Value()
	{
		if (FingerId == -1)
		{
			tmp.x = 0;
			tmp.y = 0;			
		}
		else
		{
			tmp.x = CentreX - CurrentX;
			tmp.y = CentreY - CurrentY;
			
			tmp.y *= -1;
			tmp.x *= -1;
		
			float length = tmp.len();
			float percentage = length / MaxDistanceSqrt;
			
			if (percentage < DeadZone)
			{
				tmp.x = 0; 
				tmp.y = 0;
			}
			else
			{			
				tmp.nor();
				tmp.scl(percentage);			
			}
			
		}
		
		return tmp;
	}
	
	public void Draw(ShapeRenderer shapeRenderer)
	{
		if (FingerId == -1)
			return;
		
		shapeRenderer.begin(ShapeType.Filled);
		
		shapeRenderer.circle(CentreX, CentreY, (float)Math.sqrt((double)MaxDistance) );
		shapeRenderer.circle(CurrentX, CurrentY, 10);
		
		shapeRenderer.end();
	}

}
