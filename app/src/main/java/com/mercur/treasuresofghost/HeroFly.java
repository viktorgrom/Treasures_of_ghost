package com.mercur.treasuresofghost;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.mercur.treasuresofghost.GameView.screenRatioX;
import static com.mercur.treasuresofghost.GameView.screenRatioY;

public class HeroFly {
    boolean isGoingUp = false;
    int toShoot = 0;

    int x, y, width, height, wingCounter = 0, shootCounter = 1;
    Bitmap hero_fly1, hero_fly2, hero_shoot01, hero_shoot02, hero_shoot03, hero_shoot04, shoot5, hero_dead;
    private GameView gameView;

    HeroFly(GameView gameView, int screenY, Resources res){

        this.gameView = gameView;

        hero_fly1 = BitmapFactory.decodeResource(res, R.drawable.hero_fly1);
        hero_fly2 = BitmapFactory.decodeResource(res, R.drawable.hero_fly2);

        width = hero_fly1.getWidth();
        height = hero_fly1.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        hero_fly1 = Bitmap.createScaledBitmap(hero_fly1, width, height, false);
        hero_fly2 = Bitmap.createScaledBitmap(hero_fly2, width, height, false);

        hero_shoot01 = BitmapFactory.decodeResource(res, R.drawable.hero_fight01);
        hero_shoot02 = BitmapFactory.decodeResource(res, R.drawable.hero_fight_02);
        hero_shoot03 = BitmapFactory.decodeResource(res, R.drawable.hero_fight_03);
        hero_shoot04 = BitmapFactory.decodeResource(res, R.drawable.hero_fight_04);


        hero_shoot01 = Bitmap.createScaledBitmap(hero_shoot01, width, height, false);
        hero_shoot02 = Bitmap.createScaledBitmap(hero_shoot02, width, height, false);
        hero_shoot03 = Bitmap.createScaledBitmap(hero_shoot03, width, height, false);
        hero_shoot04 = Bitmap.createScaledBitmap(hero_shoot04, width, height, false);


        hero_dead = BitmapFactory.decodeResource(res, R.drawable.hero_dead);
        hero_dead = Bitmap.createScaledBitmap(hero_dead, width, height, false);

        y = screenY / 2;
        x = (int) (64 * screenRatioX);

    }

    Bitmap getFlight (){

        if (toShoot != 0){
            if (shootCounter == 1){
                shootCounter++;
                return hero_shoot01;
            }
            if (shootCounter == 2){
                shootCounter++;
                return hero_shoot02;
            }
            if (shootCounter == 3){
                shootCounter++;
                return hero_shoot03;
            }

            shootCounter = 1;
            toShoot --;
            gameView.newBullet();

            return hero_shoot04;
        }

        if (wingCounter == 0) {
            wingCounter++;
            return hero_fly1;
        }
        wingCounter--;
        return hero_fly2;
    }

    Rect getCollisionShape (){
        return  new Rect (x, y, x + width, y + height);
    }

    Bitmap getHero_dead(){
        return hero_dead;
    }
}

