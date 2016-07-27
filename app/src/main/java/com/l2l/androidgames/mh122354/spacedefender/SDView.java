package com.l2l.androidgames.mh122354.spacedefender;

import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mh122354 on 6/21/2016.
 */
public class SDView extends SurfaceView implements Runnable {

    private Context context;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    volatile boolean playing;
    private boolean gameEnded;

    Thread gameThread=null;

    private int screenX;
    private int screenY;

    private PlayerShip player;
    private EnemyShip enemy1;
    private EnemyShip enemy2;
    private EnemyShip enemy3;
    public ArrayList<SpaceDust> dustList=
            new ArrayList<SpaceDust>();
    //For Drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder holder;

    //HUD Variables
    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;



    public SDView(Context context,int x, int y){
        super(context);
        this.context=context;

        prefs=context.getSharedPreferences("HighScores",context.MODE_PRIVATE);
        editor=prefs.edit();

        fastestTime=prefs.getLong("fastestTime",10000000);

        //Prepare for drawing
        holder=getHolder();
        paint=new Paint();




        screenX=x;
        screenY=y;

        startGame();




    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }
        private void update(){

            boolean hitDetected=false;
            ///If player collides with enemy , move enemy off screen
            if(Rect.intersects(player.getHitBox(),enemy1.getHitBox())){
                hitDetected=true;
                enemy1.setX(-100);
            }
            if(Rect.intersects(player.getHitBox(),enemy2.getHitBox())){
                hitDetected=true;
                enemy2.setX(-100);
            }

            if(Rect.intersects(player.getHitBox(),enemy3.getHitBox())){
                hitDetected=true;
                enemy3.setX(-100);
            }
            if(hitDetected){
                Log.i("Hit detected:","Shields Remaining - "+player.getShieldStrength());

                player.reduceShieldStrength();
                if(player.getShieldStrength()<0){
                    gameEnded=true;
                    dustList.clear();
                }
            }

            //Update Locations & Speed
            player.update();
            enemy1.update(player.getSpeed());
            enemy2.update((player.getSpeed()));
            enemy3.update((player.getSpeed()));

            //Update dust speed according to player
            for(SpaceDust sd : dustList){
                sd.update(player.getSpeed());
            }

            if(!gameEnded){

                distanceRemaining-=player.getSpeed();

                //Time Flying
                timeTaken=System.currentTimeMillis()- timeStarted;
            }
                //Game won if true
            if(distanceRemaining<0){
                if(timeTaken<fastestTime){
                    //Update High Score
                    editor.putLong("fastestTime",timeTaken);
                    editor.commit();
                    fastestTime=timeTaken;
                }
                distanceRemaining=0;
                gameEnded=true;
            }
        }
        private void draw(){
            if(holder.getSurface().isValid()){
                //Lock area for painting
                canvas=holder.lockCanvas();

                //clear screen
                canvas.drawColor(Color.argb(255,0,0,0));


                //Make specs white for space dust
                paint.setColor(Color.argb(255,255,255,255));

                //Draw Dust
                for(SpaceDust sd: dustList){
                    canvas.drawPoint(sd.getX(),sd.getY(),paint);
                }


                //Draw player
                canvas.drawBitmap(
                        player.getBitmap(),
                        player.getX(),
                        player.getY(),
                        paint


                );

                //Draw enemies
                canvas.drawBitmap(
                        enemy1.getBitmap(),
                        enemy1.getX(),
                        enemy1.getY(),
                        paint
                );

                canvas.drawBitmap(
                        enemy2.getBitmap(),
                        enemy2.getX(),
                        enemy2.getY(),
                        paint
                );

                canvas.drawBitmap(
                        enemy3.getBitmap(),
                        enemy3.getX(),
                        enemy3.getY(),
                        paint
                );
                if(!gameEnded) {
                    //Draw Hud
                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setColor(Color.argb(255, 255, 255, 255));
                    paint.setTextSize(25);
                    canvas.drawText("Fastest: " + fastestTime + "s", 10, 20, paint);
                    canvas.drawText("Time: " + timeTaken + "s", screenX / 2, 20, paint);
                    canvas.drawText("Distance: " +
                            distanceRemaining / 1000 + "KM", screenX / 3, screenY - 30, paint);
                    canvas.drawText("Shield: " + player.getShieldStrength(), 10, screenY-30, paint);
                    canvas.drawText("Speed: " + player.getSpeed() * 60 + "MPS", (screenX / 3) * 2, screenY -30, paint);
                }else{
                    //Pause Screen
                    paint.setTextSize(80);
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText("Game Over",screenX/2,100,paint);
                    paint.setTextSize(25);
                    canvas.drawText("Fastest: "+fastestTime+"s",screenX-150,160,paint);
                    canvas.drawText("Time: "+timeTaken+"s",screenX/2,200, paint);
                    canvas.drawText("Distance remaining: "+distanceRemaining/100+"KM",screenX/2,240,paint);
                    paint.setTextSize(80);
                    canvas.drawText("Tap to replay!",screenX/2,350,paint);
                }

                holder.unlockCanvasAndPost(canvas);
            }
        }

        private void startGame(){


            player= new PlayerShip(context,screenX,screenY);
            enemy1=new EnemyShip(context,screenX,screenY);
            enemy2=new EnemyShip(context,screenX,screenY);
            enemy3=new EnemyShip(context,screenX,screenY);

            int numSpecs=100;

            for(int i=0;i<numSpecs;i++){
                SpaceDust spec = new SpaceDust(screenX,screenY);
                dustList.add(spec);
            }

            //Reset Time & Distance
            distanceRemaining = 10000;
            timeTaken=0;

            //Start Time
            timeStarted= System.currentTimeMillis();

            gameEnded=false;


        }
        private void control(){
            //Control FPS

            try{
                gameThread.sleep(17);
            }catch (InterruptedException ex){

            }
        }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){

            //Lifted finger up
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;

            //Touching Screen
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                if(gameEnded){
                    dustList.clear();
                    startGame();
                }
                break;
        }
        return true;
    }

    public void pause(){
        playing=false;
        try{
            gameThread.join();
        } catch (InterruptedException ex){

        }
    }

    public void resume(){
        playing=true;
        gameThread=new Thread(this);
        gameThread.start();
    }
}
