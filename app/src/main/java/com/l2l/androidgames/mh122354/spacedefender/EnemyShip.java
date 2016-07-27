package com.l2l.androidgames.mh122354.spacedefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by mh122354 on 6/22/2016.
 */
public class EnemyShip {

    private Bitmap bitmap;
    private int x,y;
    private int speed=1;

    //for detecting off screen enemies
    private int maxX,minX;

    //To spawn enemies in bounds
    private int minY,maxY;

    private Rect hitBox;


    public EnemyShip(Context context, int screenX, int screenY){
        bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy);
        maxX=screenX;
        maxY=screenY;

        minX=0;
        minY=0;

        Random generator = new Random();

        speed= generator.nextInt(6)+5;
        x=screenX;
        y=generator.nextInt(maxY)-bitmap.getHeight();

        hitBox= new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());
    }


    public void update(int playerSpeed){
        x-=playerSpeed;
        x-=speed;

        if(x<minX-bitmap.getWidth()){
            Random generator= new Random();
            speed=generator.nextInt(10)+10;
            x=maxX;
            y=generator.nextInt(maxY)-bitmap.getHeight();

        }
        //Keep Hitbox on Character
        hitBox.left=x;
        hitBox.top=y;
        hitBox.right=x+bitmap.getWidth();
        hitBox.bottom=y+bitmap.getHeight();
    }


    public Bitmap getBitmap(){return bitmap;}

    public int getX(){return x;}

    public int getY(){return y;}

    public Rect getHitBox(){return hitBox;}

    public void setX(int x){
        this.x=x;
    }
}
