package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Apple {
    private float firstX;
    private float firstY;
    private float x;
    private float y;
    private int r;
    private int color;
    private int survivedTime;
    private int limit=200000;

    private Paint paint;
    private int currentFrame;
    public Apple(float _x,float _y,int _r,int _color){
        x = _x;
        y = _y;
        r = _r;
        firstX = x;
        firstY = y;
        currentFrame = 0;
        color = _color;
        paint = new Paint();
        paint.setAntiAlias(false);
        paint.setColor(color);
    }



    public int onUpdate(){
        currentFrame = GameSurfaceView.getFrame();
        double rad = Math.toRadians(6*currentFrame);
        x = (float)Math.cos(rad)*firstX-(float)Math.sin(rad)*firstY;
        y = (float)Math.sin(rad)*firstX+(float)Math.cos(rad)*firstY;
        if(currentFrame == 1){
            survivedTime++;
        }

        if(survivedTime>=limit){
            return 1;
        }

        return 0;
    }


    public void onDraw(Canvas canvas){
        canvas.drawCircle(x,y,r,paint);
    }


    public void setLimit(int sec){
        limit = sec;
    }
}
