package com.mercur.treasuresofghost;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.mercur.treasuresofghost.GameView.screenRatioX;
import static com.mercur.treasuresofghost.GameView.screenRatioY;

public class Cartouche {
    int x, y, width, height;
    Bitmap cartousche;

    Cartouche(Resources res){
        cartousche = BitmapFactory.decodeResource(res, R.drawable.cartouche1);

        width = cartousche.getWidth();
        height = cartousche.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        cartousche = Bitmap.createScaledBitmap(cartousche, width, height, false);
    }
    Rect getCollisionShape (){
        return  new Rect (x, y, x + width, y + height);
    }
}

