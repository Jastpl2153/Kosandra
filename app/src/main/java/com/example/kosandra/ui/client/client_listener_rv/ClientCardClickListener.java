package com.example.kosandra.ui.client.client_listener_rv;

import android.view.View;

import com.example.kosandra.entity.HairstyleVisit;
/**

 Interface definition for a callback to be invoked when a client card is clicked.

 Implementations of this interface must override the onItemClick method to handle the click event. */
public interface ClientCardClickListener {
    /**

     Called when a client card is clicked.
     @param view The view that was clicked.
     @param hairstyleVisit The hairstyle visit associated with the client card that was clicked. */
    void onItemClick(View view, HairstyleVisit hairstyleVisit);
}
