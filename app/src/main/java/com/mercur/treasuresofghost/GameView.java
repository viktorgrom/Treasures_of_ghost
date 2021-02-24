package com.mercur.treasuresofghost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private  Thread thread;
    private  boolean isPlaying, isGameOver = false;
    private Background background1, background2;
    private int screenX, screenY, score = 0, lifeCounterOfFish;
    private Paint paint;
    private Ghost[] ghosts;

    //private Life [] lifes;
    private Bitmap life[] = new Bitmap[2];

    private HeroFly heroFly;
    private List<Cartouche> cartouches;
    private Random random;
    private SharedPreferences prefs;
    private GameActivity activity;
    private SoundPool soundPool;
    private int sound, width, height;;
    public static float screenRatioX, screenRatioY;

    public Button btn_fire;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else
            soundPool = new SoundPool(1 , AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);


        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        background2.x = screenX;

        heroFly = new HeroFly(this, screenY, getResources());

        cartouches = new ArrayList<>();

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.BLUE);

        ghosts = new Ghost[5];

        for (int i = 0; i<5; i++){

            Ghost ghost = new Ghost(getResources());
            ghosts[i] = ghost;
        }
        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.life);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.lif_off);

        width = life[0].getWidth();
        height = life[1].getHeight();

        width /= 6;
        height /= 6;

        life[0] = Bitmap.createScaledBitmap(life[0], width, height, false);
        life[1] = Bitmap.createScaledBitmap(life[1], width, height, false);

        lifeCounterOfFish = 3;

        random = new Random();
    }

    @Override
    public void run() {
        while (isPlaying){
            update();
            draw();
            sleep();
        }
    }

    private void update(){

        background1.x -= 0 * screenRatioX;
        background2.x -= 0 * screenRatioX;

        if (background1.x + background1.background.getWidth() < 0){
            background1.x = screenX;
        }
        if (background2.x + background2.background.getWidth() < 0){
            background2.x = screenX;
        }

        if (heroFly.isGoingUp)
            heroFly.y -= 15 * screenRatioY;
        else
            heroFly.y += 15 * screenRatioY;

        if (heroFly.y < 0)
            heroFly.y = 0;

        if (heroFly.y >= screenY - heroFly.height)
            heroFly.y = screenY - heroFly.height;

        List<Cartouche> trash = new ArrayList<>();

        for (Cartouche cartouche : cartouches){
            if (cartouche.x > screenX)
                trash.add(cartouche);

            cartouche.x += 50 * screenRatioX;

            for (Ghost ghost : ghosts){
                if (Rect.intersects(ghost.getCollisionShape(), cartouche.getCollisionShape() )){

                    score++;
                    ghost.x = -500;
                    cartouche.x = screenX + 500;
                    ghost.wasShot = true;
                }

            }
        }

        for (Cartouche cartouche : trash)
            cartouches.remove(cartouche);

        for (Ghost ghost : ghosts){

            ghost.x -= ghost.speed;
            if (ghost.x + ghost.width < 0){

                if (!ghost.wasShot){
                    lifeCounterOfFish--;
                }


                if (lifeCounterOfFish == 0){

                    isGameOver = true;
                    return;
                }

                if (score <=30) {

                    int bound = (int) (10 * screenRatioX);
                    ghost.speed = random.nextInt(bound);

                    if (ghost.speed < 5 * screenRatioX)
                        ghost.speed = (int) (5 * screenRatioX);
                }
                if (score >=60){
                    int bound = (int) (15 * screenRatioX);
                    ghost.speed = random.nextInt(bound);

                    if (ghost.speed < 6 * screenRatioX)
                        ghost.speed = (int) (6 * screenRatioX);
                }
                if (score >=100){
                    int bound = (int) (19 * screenRatioX);
                    ghost.speed = random.nextInt(bound);

                    if (ghost.speed < 8 * screenRatioX)
                        ghost.speed = (int) (8 * screenRatioX);
                }
                if (score >=200){
                    int bound = (int) (22 * screenRatioX);
                    ghost.speed = random.nextInt(bound);

                    if (ghost.speed < 10 * screenRatioX)
                        ghost.speed = (int) (10 * screenRatioX);
                }

                ghost.x = screenX;
                ghost.y = random.nextInt(screenY - ghost.height);

                ghost.wasShot = false;

            }
            if (Rect.intersects(ghost.getCollisionShape(), heroFly.getCollisionShape())){
                isGameOver = true;
                return;
            }
        }

    }
    private void draw(){

        if (getHolder().getSurface().isValid()){

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Ghost ghost : ghosts)
                canvas.drawBitmap(ghost.getBird(), ghost.x, ghost.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, paint);

            if (isGameOver){
                isPlaying = false;
                canvas.drawBitmap(heroFly.getHero_dead(), heroFly.x, heroFly.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveHighScore();
                waitBeforeExiting ();
                return;
            }



            canvas.drawBitmap(heroFly.getFlight(), heroFly.x, heroFly.y, paint);

            for (Cartouche cartouche : cartouches)
                canvas.drawBitmap(cartouche.cartousche, cartouche.x, cartouche.y, paint);

            for (int i=0; i<3; i++){
                int x = (int) (380 + life[0].getWidth()  *i);
                int y = 30;

                if (i < lifeCounterOfFish){
                    canvas.drawBitmap(life[0], x, y, null);
                }
                else {
                    canvas.drawBitmap(life[1], x, y, null);
                }
            }

            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    private void waitBeforeExiting() {
        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveHighScore() {

        if (prefs.getInt("highscore", 0) < score){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }


    }

    private  void sleep(){
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) {
                    heroFly.isGoingUp = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                heroFly.isGoingUp = false;
                if (event.getX() > screenX / 2)
                    heroFly.toShoot++;

                break;
        }

        return true;
    }

    public void newBullet() {

        if (!prefs.getBoolean("isMute", false))
            soundPool.play(sound, 1, 1, 0,0,1);

        Cartouche cartouche = new Cartouche(getResources());
        cartouche.x = heroFly.x + heroFly.width;
        cartouche.y = heroFly.y + (heroFly.height/8);
        cartouches.add(cartouche);
    }
}
