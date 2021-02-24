package com.mercur.treasuresofghost;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.mercur.treasuresofghost.GameView.screenRatioX;
import static com.mercur.treasuresofghost.GameView.screenRatioY;

public class Ghost {
    public int speed = 20;
    public boolean wasShot = true;
    int x=0, y, width, height, ghostCounter = 1;
    Bitmap ghost1, ghost2, ghost3, ghost4;

    Ghost(Resources res){
        ghost1 = BitmapFactory.decodeResource(res, R.drawable.ghost1);
        ghost2 = BitmapFactory.decodeResource(res, R.drawable.ghost2);
        ghost3 = BitmapFactory.decodeResource(res, R.drawable.ghost3);
        ghost4 = BitmapFactory.decodeResource(res, R.drawable.ghost4);

        width = ghost1.getWidth();
        height = ghost1.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        ghost1 = Bitmap.createScaledBitmap(ghost1, width, height, false);
        ghost2 = Bitmap.createScaledBitmap(ghost2, width, height, false);
        ghost3 = Bitmap.createScaledBitmap(ghost3, width, height, false);
        ghost4 = Bitmap.createScaledBitmap(ghost4, width, height, false);

        y = -height;
    }

    Bitmap getBird () {

        if (ghostCounter == 1){
            ghostCounter++;
            return ghost1;
        }
        if (ghostCounter == 2){
            ghostCounter++;
            return ghost2;
        }
        if (ghostCounter == 3){
            ghostCounter++;
            return ghost3;
        }
        ghostCounter = 1;

        return ghost4;
    }

    Rect getCollisionShape (){
        return  new Rect (x, y, x + width, y + height);
    }
}
