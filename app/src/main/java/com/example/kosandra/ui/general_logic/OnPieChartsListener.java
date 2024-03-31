package com.example.kosandra.ui.general_logic;

import android.view.MotionEvent;

import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

public interface OnPieChartsListener extends OnChartGestureListener {

    @Override
    void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture);

    @Override
    default void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture){}

    @Override
    default void onChartLongPressed(MotionEvent me){}

    @Override
    default void onChartDoubleTapped(MotionEvent me){}

    @Override
    default void onChartSingleTapped(MotionEvent me){}

    @Override
    default void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY){}

    @Override
    default void onChartScale(MotionEvent me, float scaleX, float scaleY){}

    @Override
   default void onChartTranslate(MotionEvent me, float dX, float dY){}
}
