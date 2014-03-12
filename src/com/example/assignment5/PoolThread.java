package com.example.assignment5;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class PoolThread extends Thread {

	ArrayList<Ball> balls = new ArrayList<Ball>();
	SurfaceHolder holder;
	Random rand = new Random();
	Canvas canvas;
	boolean running = true;
	int w, 
		h;
	
	public PoolThread(SurfaceHolder holder, int w, int h) {
		this.holder = holder;
		this.w = w;
		this.h = h;
	}
	
	public void setRunning(boolean running){
		this.running = running;
	}
	
	public void run(){
		canvas = null;
		Paint backPaint 	= new Paint(),
			  borderPaint 	= new Paint();
		backPaint.setColor(Color.WHITE);
		borderPaint.setStyle(Paint.Style.STROKE);
		long previousTime = System.currentTimeMillis();
		while(running){
			try{
				canvas = holder.lockCanvas(null);
				if(canvas != null){
					synchronized (holder) {
						long currentTime = System.currentTimeMillis();
						double elapsedTime = currentTime - previousTime;
						if(elapsedTime > 20){
							previousTime = System.currentTimeMillis();
							synchronized(balls){
								for(Ball b: balls){
									b.next_pos();
								}
							}
						}
						canvas.drawRect(0, 0, w, h, backPaint);
						canvas.drawRect(0, 0, w - 1, h - 1, borderPaint);
						synchronized(balls){
							for(Ball b: balls){
								canvas.drawCircle(b.x,  b.y,  b.radius,  b.getPaint());
							}
						}
					}
				}
			} finally{
				if(canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	public void add(float x, float y){
		synchronized(balls){
			balls.add(new PoolThread.Ball(x, y, rand.nextInt(70)));
		}
	}
	
	public class Ball {
		
		Random rand = new Random();
		Paint ballPaint = new Paint();
		float x_dir = rand.nextInt(40) - 20,
			  y_dir = rand.nextInt(40) - 20,
			  x,
			  y;
		int radius;
		
		public Ball(float x, float y, int radius) {
			this.x = x;
			this.y = y;
			this.radius = radius;
			ballPaint.setColor(Color.argb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
		}
		
		public void next_pos(){
			if(x >= w - radius){
				x_dir = -x_dir;
				x = w - radius;
			}
			if(x <= 0 + radius){
				x_dir = -x_dir;
				x = radius;
			}
			if(y >= h - radius){
				y_dir = -y_dir;
				y = h - radius;
			}
			if(y <= 0 + radius){
				y_dir = -y_dir;
				y = radius;
			}
			x += x_dir;
			y += y_dir;
		}
		
		public Paint getPaint(){
			return ballPaint;
		}
	}


}
