package com.example.kosandra.ui.client.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kosandra.R;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.ui.client.interface_recycle_view.ClientCardClickListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterRVListVisitClient extends RecyclerView.Adapter<AdapterRVListVisitClient.VisitHolder> {
    private final List<HairstyleVisit> hairstyleVisits = new ArrayList<>();
    private final ClientCardClickListener listenerInterface;

    public AdapterRVListVisitClient(ClientCardClickListener listenerInterface) {
        this.listenerInterface = listenerInterface;
    }

    @NonNull
    @Override
    public AdapterRVListVisitClient.VisitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hairstyle_recycler_view, parent, false);
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

    public void setVisitList(List<HairstyleVisit> visitList) {
        this.hairstyleVisits.clear();
        this.hairstyleVisits.addAll(visitList);
        notifyDataSetChanged();
    }

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
