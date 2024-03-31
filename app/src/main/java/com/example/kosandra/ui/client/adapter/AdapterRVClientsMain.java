package com.example.kosandra.ui.client.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kosandra.R;
import com.example.kosandra.entity.Client;
import com.example.kosandra.ui.general_logic.RvItemClickListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRVClientsMain extends RecyclerView.Adapter<AdapterRVClientsMain.ClientHolder> {
    private List<Client> clients = new ArrayList<>();
    private final RvItemClickListener<Client> rvItemClickListener;

    public AdapterRVClientsMain(RvItemClickListener<Client> rvItemClickListener) {
        this.rvItemClickListener = rvItemClickListener;
    }

    @NonNull
    @Override
    public ClientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client_main, parent, false);
        return new ClientHolder(view, rvItemClickListener, clients);
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

    public List<Client> getClients() {
        return clients;
    }

    public class ClientHolder extends RecyclerView.ViewHolder {
        private CircleImageView client_image;
        private TextView tv_client_name;
        private ImageButton but_delete;
        private LinearLayout item_layout;

        public ClientHolder(@NonNull View itemView, RvItemClickListener<Client> rvItemClickListener, List<Client> clients) {
            super(itemView);
            findViews(itemView);
            setupListeners(rvItemClickListener, clients);
        }

        private void findViews(View itemView) {
            client_image = itemView.findViewById(R.id.item_client_image);
            tv_client_name = itemView.findViewById(R.id.item_client_name);
            but_delete = itemView.findViewById(R.id.but_delete);
            item_layout = itemView.findViewById(R.id.linear_layout_client_main);
        }

        private void setupListeners( RvItemClickListener<Client> rvItemClickListener, List<Client> clients) {
            itemView.setOnLongClickListener(v -> handleClick(rvItemClickListener, clients, true));
            itemView.setOnClickListener(v -> handleClick(rvItemClickListener, clients, false));
            but_delete.setOnClickListener(v -> handleDeleteClick(rvItemClickListener, clients));
            client_image.setOnClickListener(v -> handleImageClick(rvItemClickListener, clients));
        }

        private boolean handleClick(RvItemClickListener<Client> rvItemClickListener, List<Client> clients, boolean isLongClick) {
            int position = getAdapterPosition();
            if (rvItemClickListener != null && position != RecyclerView.NO_POSITION) {
                Client client = clients.get(position);
                if (isLongClick) {
                    rvItemClickListener.onLongClick(item_layout, but_delete, client);
                } else {
                    rvItemClickListener.onClick(item_layout, but_delete, client);
                }
                return true;
            }
            return false;
        }

        private void handleDeleteClick(RvItemClickListener<Client> rvItemClickListener, List<Client> clients) {
            int position = getAdapterPosition();
            if (rvItemClickListener != null && position != RecyclerView.NO_POSITION) {
                Client client = clients.get(position);
                rvItemClickListener.onDeleteClick(item_layout, but_delete, client);
                clients.remove(position);
                notifyItemRemoved(position);
            }
        }

        private void handleImageClick(RvItemClickListener<Client> rvItemClickListener, List<Client> clients) {
            int position = getAdapterPosition();
            if (rvItemClickListener != null && position != RecyclerView.NO_POSITION) {
                Client client = clients.get(position);
                rvItemClickListener.onPhotoClick(client, client_image);
            }
        }
    }
}