package com.example.kosandra.ui.general_logic;

import android.view.MotionEvent;

import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

/**

        Interface responsible for handling bar chart gestures for chart interactions.

        Extends OnChartGestureListener for supporting other chart gestures. */
public interface OnBarChartsListener extends OnChartGestureListener {
    /**

     Callback method invoked when a chart gesture starts.
     @param me The motion event associated with the gesture
     @param lastPerformedGesture The last performed gesture on the chart */
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
