package com.l2l.androidgames.mh122354.spacedefender;

import java.util.Random;

/**
 * Created by mh122354 on 6/23/2016.
 */
public class SpaceDust {

    private int speed;
    private int minY;
    private int maxY;
    private int maxX;
    private int minX;
    private int x,y;

    public SpaceDust(int screenX,int screenY){

        minY=0;
        minX=0;

        Random ran = new Random();
        speed= ran.nextInt(10);

        maxX=screenX;
        maxY=screenY;

        x=ran.nextInt(screenX);
        y=ran.nextInt(screenY);
    }

    public void update(int playerSpeed){
        x-=playerSpeed;
        x-=speed;

        if(x<0){
            x=maxX;
            Random ran = new Random();
            y=ran.nextInt(maxY);
            speed=ran.nextInt(15);
        }
    }

    public int getX(){return x;}

    public int getY(){return y;}




}
