package com.example.kosandra.ui.client.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.kosandra.R;
import com.example.kosandra.entity.Client;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.ui.client.interface_recycle_view.ClientClickListener;
import com.example.kosandra.view_model.ClientViewModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRVListClients extends RecyclerView.Adapter<AdapterRVListClients.ClientHolder> {
    private List<Client> clients = new ArrayList<>();
    private final ClientClickListener clientClickListener;

    public AdapterRVListClients(ClientClickListener clientClickListener) {
        this.clientClickListener = clientClickListener;
    }

    @NonNull
    @Override
    public ClientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        return new ClientHolder(view, clientClickListener, clients);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientHolder holder, int position) {
        Client client = clients.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(client.getPhoto(), 0, client.getPhoto().length);
        holder.client_image.setImageBitmap(bitmap);
        holder.tv_client_name.setText(client.getName());
    }

    @Override
    public int getItemCount() {
        return clients != null ? clients.size() : 0;
    }

    public void setClients(List<Client> clients) {
        this.clients.clear();
        this.clients.addAll(clients);
        notifyDataSetChanged();
    }

    public class ClientHolder extends RecyclerView.ViewHolder {
        private CircleImageView client_image;
        private TextView tv_client_name;
        private ImageButton but_delete;
        private LinearLayout item_layout;

        public ClientHolder(@NonNull View itemView, ClientClickListener clientClickListener, List<Client> clients) {
            super(itemView);
            findViews(itemView);
            setupListeners(clientClickListener, clients);
        }

        private void findViews(View itemView) {
            client_image = itemView.findViewById(R.id.client_image);
            tv_client_name = itemView.findViewById(R.id.tv_client_name);
            but_delete = itemView.findViewById(R.id.but_delete);
            item_layout = itemView.findViewById(R.id.item_layout);
        }

        private void setupListeners(ClientClickListener clientClickListener, List<Client> clients) {
            itemView.setOnLongClickListener(v -> handleClick(clientClickListener, clients, true));
            itemView.setOnClickListener(v -> handleClick(clientClickListener, clients, false));
            but_delete.setOnClickListener(v -> handleDeleteClick(clientClickListener, clients));
            client_image.setOnClickListener(v -> handleImageClick(clientClickListener, clients));
        }

        private boolean handleClick(ClientClickListener clientClickListener, List<Client> clients, boolean isLongClick) {
            int position = getAdapterPosition();
            if (clientClickListener != null && position != RecyclerView.NO_POSITION) {
                Client client = clients.get(position);
                if (isLongClick) {
                    clientClickListener.onClientLongClicked(item_layout, but_delete, client);
                } else {
                    clientClickListener.onClientClicked(item_layout, but_delete, client);
                }
                return true;
            }
            return false;
        }

        private void handleDeleteClick(ClientClickListener clientClickListener, List<Client> clients) {
            int position = getAdapterPosition();
            if (clientClickListener != null && position != RecyclerView.NO_POSITION) {
                Client client = clients.get(position);
                clientClickListener.onDeleteClicked(item_layout, but_delete, client);
                clients.remove(position);
                notifyItemRemoved(position);
            }
        }

        private void handleImageClick(ClientClickListener clientClickListener, List<Client> clients) {
            int position = getAdapterPosition();
            if (clientClickListener != null && position != RecyclerView.NO_POSITION) {
                Client client = clients.get(position);
                clientClickListener.onViewPhotoClicked(client, client_image);
            }
        }
    }
}