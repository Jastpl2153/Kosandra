package com.example.kosandra.ui.material.adapter;

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
import com.example.kosandra.entity.Materials;
import com.example.kosandra.ui.general_logic.RvItemClickListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * AdapterRvMaterialsMain class is responsible for adapting a list of Materials objects to be displayed in a RecyclerView.
 * It also handles user interaction events such as item clicks and delete actions.
 */
public class AdapterRvMaterialsMain extends RecyclerView.Adapter<AdapterRvMaterialsMain.MaterialHolder> {
    private List<Materials> materials = new ArrayList<>();
    private final RvItemClickListener<Materials> rvItemClickListener;

    /**
     * Constructor for AdapterRvMaterialsMain.
     *
     * @param rvItemClickListener Interface to handle item click events.
     */
    public AdapterRvMaterialsMain(RvItemClickListener<Materials> rvItemClickListener) {
        this.rvItemClickListener = rvItemClickListener;
    }

    @NonNull
    @Override
    public AdapterRvMaterialsMain.MaterialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_material_tab, parent, false);
        return new MaterialHolder(view, rvItemClickListener, materials);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRvMaterialsMain.MaterialHolder holder, int position) {
        Materials material = materials.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(material.getPhoto(), 0, material.getPhoto().length);
        holder.material_image.setImageBitmap(bitmap);
        holder.tv_material_name.setText(material.getColorMaterial());
        holder.tv_material_code.setText(material.getCodeMaterial());
        holder.tv_count.setText(String.valueOf(material.getCount()));
    }

    @Override
    public int getItemCount() {
        return materials.size();
    }

    /**
     * Set the list of Materials data to be displayed in the RecyclerView.
     *
     * @param materials The list of Materials objects.
     */
    public void setMaterials(List<Materials> materials) {
        this.materials.clear();
        this.materials.addAll(materials);
        notifyDataSetChanged();
    }

    public void removeMaterials(Materials material) {
        materials.remove(material);
        notifyDataSetChanged();
    }

    public class MaterialHolder extends RecyclerView.ViewHolder {
        CircleImageView material_image;
        TextView tv_material_name;
        TextView tv_material_code;
        TextView tv_count;
        private ImageButton but_delete;
        private LinearLayout item_layout;

        public MaterialHolder(@NonNull View itemView, RvItemClickListener<Materials> rvItemClickListener, List<Materials> materials) {
            super(itemView);
            findViews(itemView);
            setupListeners(rvItemClickListener, materials);
        }

        /**
         * Find and initialize views within the item view.
         *
         * @param itemView The item View.
         */
        private void findViews(View itemView) {
            material_image = itemView.findViewById(R.id.rv_material_image);
            tv_material_name = itemView.findViewById(R.id.rv_tv_material_name);
            tv_material_code = itemView.findViewById(R.id.rv_tv_material_code);
            tv_count = itemView.findViewById(R.id.rv_tv_material_count);
            but_delete = itemView.findViewById(R.id.but_delete_material);
            item_layout = itemView.findViewById(R.id.linear_layout_material_main);
        }

        /**
         * Setup click listeners for item views.
         *
         * @param rvItemClickListener Interface to handle item click events.
         * @param materials           The list of Materials objects.
         */
        private void setupListeners(RvItemClickListener<Materials> rvItemClickListener, List<Materials> materials) {
            itemView.setOnLongClickListener(v -> handleClick(rvItemClickListener, materials, true));
            itemView.setOnClickListener(v -> handleClick(rvItemClickListener, materials, false));
            but_delete.setOnClickListener(v -> handleDeleteClick(rvItemClickListener, materials));
            material_image.setOnClickListener(v -> handleImageClick(rvItemClickListener, materials));
        }

        /**
         * Handle onClick events for item views.
         *
         * @param rvItemClickListener Interface to handle item click events.
         * @param materials           The list of Materials objects.
         * @param isLongClick         Indicates if the event is a long click.
         * @return True if the event was handled, false otherwise.
         */
        private boolean handleClick(RvItemClickListener<Materials> rvItemClickListener, List<Materials> materials, boolean isLongClick) {
            int position = getAdapterPosition();
            if (rvItemClickListener != null && position != RecyclerView.NO_POSITION) {
                Materials material = materials.get(position);
                if (isLongClick) {
                    rvItemClickListener.onLongClick(item_layout, but_delete, material);
                } else {
                    rvItemClickListener.onClick(item_layout, but_delete, material);
                }
                return true;
            }
            return false;
        }

        /**
         * Handle delete button click event.
         *
         * @param rvItemClickListener Interface to handle item click events.
         * @param materials           The list of Materials objects.
         */
        private void handleDeleteClick(RvItemClickListener<Materials> rvItemClickListener, List<Materials> materials) {
            int position = getAdapterPosition();
            if (rvItemClickListener != null && position != RecyclerView.NO_POSITION) {
                Materials material = materials.get(position);
                rvItemClickListener.onDeleteClick(item_layout, but_delete, material);
            }
        }

        /**
         * Handle image click event.
         *
         * @param rvItemClickListener Interface to handle item click events.
         * @param materials           The list of Materials objects.
         */
        private void handleImageClick(RvItemClickListener<Materials> rvItemClickListener, List<Materials> materials) {
            int position = getAdapterPosition();
            if (rvItemClickListener != null && position != RecyclerView.NO_POSITION) {
                Materials material = materials.get(position);
                rvItemClickListener.onPhotoClick(material, material_image);
            }
        }
    }
}
