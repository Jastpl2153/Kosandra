package com.example.kosandra.ui.financial_statistics.colors_charts;

import android.graphics.Color;

public enum ColorEnum {
    COLORS(new int[]{
            Color.rgb(0, 191, 255),
            Color.rgb(250, 128, 114),
            Color.rgb(152, 251, 152),
            Color.rgb(255, 105, 180),
            Color.rgb(255, 127, 80),
            Color.rgb(123, 104, 238),
            Color.rgb(233, 150, 122),
            Color.rgb(173, 255, 47),
            Color.rgb(255, 182, 193),
            Color.rgb(255, 165, 0),
            Color.rgb(255, 250, 205),
            Color.rgb(221, 160, 221),
            Color.rgb(135, 206, 235),
            Color.rgb(188, 143, 143),
            Color.rgb(0, 206, 209),
            Color.rgb(220, 20, 60),
            Color.rgb(60, 179, 113),
            Color.rgb(255, 140, 0),
            Color.rgb(255, 255, 0),
            Color.rgb(106, 90, 205)
    });

    private final int[] colors;

    ColorEnum(int[] colors) {
        this.colors = colors;
    }

    public int[] getColors() {
        return colors;
    }
}
