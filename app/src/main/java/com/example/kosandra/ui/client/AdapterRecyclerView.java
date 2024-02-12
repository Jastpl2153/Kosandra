package com.example.kosandra.ui.client;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.kosandra.R;
import com.example.kosandra.view_model.ClientViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ClientHolder> {
    private List<Client> clients = new ArrayList<>();
    private ClientViewModel clientViewModel;

    public AdapterRecyclerView(ClientViewModel clientViewModel) {
        this.clientViewModel = clientViewModel;
    }

    @NonNull
    @Override
    public ClientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        return new ClientHolder(view);
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

    class ClientHolder extends RecyclerView.ViewHolder {
        private CircleImageView client_image;
        private TextView tv_client_name;
        private ImageButton but_delete;
        private LinearLayout item_layout;
        private boolean isSelected = false;

        public ClientHolder(@NonNull View itemView) {
            super(itemView);
            client_image = itemView.findViewById(R.id.client_image);
            tv_client_name = itemView.findViewById(R.id.tv_client_name);
            but_delete = itemView.findViewById(R.id.but_delete);
            item_layout = itemView.findViewById(R.id.item_layout);

            itemView.setOnLongClickListener(this::handleItemLongClick);
            itemView.setOnClickListener(v -> handleItemClick());
            but_delete.setOnClickListener(v -> handleDeleteClient());
            client_image.setOnClickListener(v -> viewClientPhoto());
        }

        private boolean handleItemLongClick(View v) {
            if (!isSelected) {
                isSelected = true;
                animateItemSelected();
            }
            return true;
        }

        private void handleItemClick() {
            if (isSelected) {
                isSelected = false;
                animateItemDeselected();
            }
        }

        private void animateItemSelected() {
            int offset = item_layout.getWidth() / 5;

            ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(item_layout, "translationX", 0, -offset);
            setupAnimator(translationXAnimator, true);
            translationXAnimator.start();
        }

        private void animateItemDeselected() {
            int offset = but_delete.getWidth();

            ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(item_layout, "translationX", -offset, 0);
            setupAnimator(translationXAnimator, false);
            translationXAnimator.start();
        }

        private void setupAnimator(ObjectAnimator animator, boolean isSelected){
            animator.setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (isSelected){
                        item_layout.setBackgroundColor(ContextCompat.getColor(item_layout.getContext(), R.color.blue_200));
                    } else {
                        but_delete.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isSelected){
                        but_delete.setVisibility(View.VISIBLE);
                    } else {
                        item_layout.setBackgroundColor(ContextCompat.getColor(item_layout.getContext(), R.color.white));
                    }
                }
            });
        }

        private void handleDeleteClient() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && position < clients.size()) {
                Client client = clients.get(position);
                clientViewModel.delete(client);
                cancelAnimation();
            }
            notifyItemRemoved(position);
        }

        private void cancelAnimation() {
            item_layout.animate().translationX(0).setDuration(0).start();
            but_delete.setVisibility(View.INVISIBLE);
            item_layout.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.white));
        }

        private void viewClientPhoto () {
            if (!isSelected) {
                int position = getAdapterPosition();

                Dialog dialog = new Dialog(client_image.getContext(), android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth);
                dialog.setContentView(R.layout.dialog_photo_client);

                SubsamplingScaleImageView imageView = dialog.findViewById(R.id.photo);

                Bitmap bitmap = BitmapFactory.decodeByteArray(clients.get(position).getPhoto(), 0, clients.get(position).getPhoto().length);
                imageView.setImage(ImageSource.bitmap(bitmap));

                dialog.show();
            }
        }
    }
}