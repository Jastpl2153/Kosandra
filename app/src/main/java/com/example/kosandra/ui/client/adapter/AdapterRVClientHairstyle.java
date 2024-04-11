package com.example.kosandra.ui.client.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kosandra.R;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.ui.client.client_listener_rv.ClientCardClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * AdapterRVClientHairstyle is a RecyclerView adapter for displaying a list of HairstyleVisit objects in a client's card view.
 * <p>
 * It binds HairstyleVisit data to the client's hairstyle visit card layout.
 * <p>
 * This adapter also provides a way to interact with individual items via click listeners.
 */
public class AdapterRVClientHairstyle extends RecyclerView.Adapter<AdapterRVClientHairstyle.VisitHolder> {
    private final List<HairstyleVisit> hairstyleVisits = new ArrayList<>();
    private final ClientCardClickListener listenerInterface;

    /**
     * Constructs an AdapterRVClientHairstyle with a listener interface to handle item clicks.
     *
     * @param listenerInterface An instance of ClientCardClickListener for handling item click events.
     */
    public AdapterRVClientHairstyle(ClientCardClickListener listenerInterface) {
        this.listenerInterface = listenerInterface;
    }

    @NonNull
    @Override
    public AdapterRVClientHairstyle.VisitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client_card_hairstyle, parent, false);
        return new VisitHolder(view, listenerInterface, hairstyleVisits);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitHolder holder, int position) {
        holder.bind(hairstyleVisits.get(position), position);
    }

    @Override
    public int getItemCount() {
        return hairstyleVisits.size();
    }

    /**
     * Sets the list of HairstyleVisit items to be displayed in the adapter.
     * Clears the existing list and adds all items from the provided list.
     *
     * @param visitList The new list of HairstyleVisit items to display.
     */
    public void setVisitList(List<HairstyleVisit> visitList) {
        this.hairstyleVisits.clear();
        this.hairstyleVisits.addAll(visitList);
        notifyDataSetChanged();
    }

    /**
     * VisitHolder represents an item view inside the RecyclerView for displaying HairstyleVisit data.
     * <p>
     * It binds the data to the corresponding layout elements and handles item click events.
     */
    public static class VisitHolder extends RecyclerView.ViewHolder {
        private TextView tvNameHaircut;
        private TextView tvCountVisit;

        public VisitHolder(@NonNull View itemView, ClientCardClickListener listenerInterface, List<HairstyleVisit> hairstyleVisits) {
            super(itemView);
            tvNameHaircut = itemView.findViewById(R.id.tv_name_haircut);
            tvCountVisit = itemView.findViewById(R.id.tv_count_visit);

            itemView.setOnClickListener(v -> {
                if (listenerInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        HairstyleVisit hairstyleVisit = hairstyleVisits.get(position);
                        listenerInterface.onItemClick(v, hairstyleVisit);
                    }
                }
            });
        }

        public void bind(HairstyleVisit hairstyleVisit, int position) {
            tvNameHaircut.setText(hairstyleVisit.getHaircutName());
            tvCountVisit.setText((position + 1) + " посещение: ");
        }
    }
}
