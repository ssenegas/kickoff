package com.senegas.kickoff.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Joystick
{	
	private float MaxDistance = 2000.0f; //in pixels non sqrt
	private float MaxDistanceSqrt = (float)Math.sqrt((double) this.MaxDistance);
	
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
		this.MaxDistance = (float)Math.pow((double)maxDistanceInPixels, 2);
		this.MaxDistanceSqrt = maxDistanceInPixels;
	}
	
	public void Update(float delta)
	{
		if (this.FingerId == -1)
			return;
		
		if (Gdx.input.isTouched(this.FingerId) == false)
		{
			this.FingerId = -1;

			this.CentreX = -1;
			this.CentreY = -1;
			
			return;
		}


		this.CurrentX = Gdx.input.getX(this.FingerId);
		this.CurrentY = Gdx.input.getY(this.FingerId);
		
		//Reverse height, so we get graphics height instead of touch height (touch 0,0 = top left; graphics 0,0 = bottom left)
		this.CurrentY = Gdx.graphics.getHeight() - this.CurrentY;
		
		//On first touch, set the centre
		if (this.CentreX == -1 && this.CentreY == -1)
		{
			this.CentreX = this.CurrentX;
			this.CentreY = this.CurrentY;
		}
		
		float x = this.CentreX - this.CurrentX;
		float y = this.CentreY - this.CurrentY;
		
		float dis = (x * x) + (y * y);
				
		if (dis > this.MaxDistance)
		{			
			dis = (float)Math.sqrt((double)dis);
			
			float dif = dis - this.MaxDistanceSqrt;

			this.CentreVector.x = this.CentreX;
			this.CentreVector.y = this.CentreY;

			this.CentreVector.sub(this.CurrentX, this.CurrentY);
			this.CentreVector.nor(); // Direction
			this.CentreVector.scl(dif);

			this.CentreX -= this.CentreVector.x;
			this.CentreY -= this.CentreVector.y;
			
			if (!this.Area.contains(this.CentreX, this.CentreY))
			{
				this.CentreX += this.CentreVector.x;
				this.CentreY += this.CentreVector.y;
			}
		}
		
	}
	
	public Vector2 Value()
	{
		if (this.FingerId == -1)
		{
			this.tmp.x = 0;
			this.tmp.y = 0;
		}
		else
		{
			this.tmp.x = this.CentreX - this.CurrentX;
			this.tmp.y = this.CentreY - this.CurrentY;

			this.tmp.y *= -1;
			this.tmp.x *= -1;
		
			float length = this.tmp.len();
			float percentage = length / this.MaxDistanceSqrt;
			
			if (percentage < this.DeadZone)
			{
				this.tmp.x = 0;
				this.tmp.y = 0;
			}
			else
			{
				this.tmp.nor();
				this.tmp.scl(percentage);
			}
			
		}
		
		return this.tmp;
	}
	
	public void Draw(ShapeRenderer shapeRenderer)
	{
		if (this.FingerId == -1)
			return;
		
		shapeRenderer.begin(ShapeType.Filled);
		
		shapeRenderer.circle(this.CentreX, this.CentreY, (float)Math.sqrt((double) this.MaxDistance) );
		shapeRenderer.circle(this.CurrentX, this.CurrentY, 10);
		
		shapeRenderer.end();
	}

}
