package com.tec.alvaroramirez.tetristarea2;

import android.view.View;
import android.content.Context;


public class BoardGame extends View{
    private int color;

    public BoardGame(Context context) {
        super(context);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        this.color = color;
    }

    public int getColor() {
        return color;
    }


}
