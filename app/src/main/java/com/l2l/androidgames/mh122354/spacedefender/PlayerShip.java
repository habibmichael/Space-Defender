package com.l2l.androidgames.mh122354.spacedefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by mh122354 on 6/21/2016.
 */
public class PlayerShip {

    private Bitmap bitmap;
    private int x,y;
    private int speed=0;
    private boolean boosting;
    private final int gravity=-12;

    private int maxY;
    private int minY;

    private final int MIN_SPEED=1;
    private final int MAX_SPEED=20;

    private Rect hitBox;
    private int shieldStrength;


    public PlayerShip(Context context, int screenX, int screenY){


        minY=0;
        boosting=false;
        x=50;
        y=50;
        speed=1;
        bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.ship);
        maxY=screenY-bitmap.getHeight();

        hitBox= new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());

        shieldStrength=2;

    }
    public void update(){
        if(boosting)
            speed+=2;
            else
            speed-=5;

        if(speed>MAX_SPEED)
            speed=MAX_SPEED;
        if(speed<MIN_SPEED)
            speed=MIN_SPEED;

        y-=speed+gravity;

        if(y<minY)
            y=minY;
        if(y>maxY)
            y=maxY;

        //Keep Hitbox on Character
        hitBox.left=x;
        hitBox.top=y;
        hitBox.right=x+bitmap.getWidth();
        hitBox.bottom=y+bitmap.getHeight();
    }

    public void reduceShieldStrength(){
        shieldStrength=shieldStrength-1;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }

    public int getSpeed(){
        return speed;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public Rect getHitBox(){return hitBox;}

    public int getShieldStrength(){return shieldStrength;}

    public void setBoosting(){
        boosting=true;
    }
    public void stopBoosting(){
        boosting=false;
    }

}
