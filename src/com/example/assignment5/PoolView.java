package com.example.assignment5;

import java.util.Random;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PoolView extends SurfaceView implements SurfaceHolder.Callback{

	PoolThread thread;
	Random rand = new Random();
	long previousTime;
	
	public PoolView(Context context) {
		super(context);
	}

	public PoolView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
	}

	public PoolView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev){
		float x = ev.getX(),
			  y = ev.getY();
		long currentTime = System.currentTimeMillis();
		double elapsedTime = currentTime - previousTime;
		if(elapsedTime > 50){
			thread.add(x, y);
			previousTime = System.currentTimeMillis();
		}
		return true;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread = new PoolThread(holder, this.getWidth(), this.getHeight());
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread.setRunning(false);	
		boolean retry = true;
		while(retry){
			try{
				thread.join();
				retry = false;
			} catch(InterruptedException e){}
		}
	}

}
