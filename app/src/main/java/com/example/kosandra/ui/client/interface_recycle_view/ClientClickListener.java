package com.example.kosandra.ui.client.interface_recycle_view;

import android.view.View;

import com.example.kosandra.entity.Client;

public interface ClientClickListener {
    void onClientClicked(View item_layout, View but_delete,  Client client);
    void onClientLongClicked(View item_layout, View but_delete, Client client);

    void onDeleteClicked(View item_layout, View but_delete, Client client);
    void onViewPhotoClicked(Client client, View client_image);
}
