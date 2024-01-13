package com.example.kosandra.ui.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kosandra.R;
import com.example.kosandra.view_model.ClientViewModel;

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
            translationXAnimator.setDuration(300);

            translationXAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(@NonNull Animator animation, boolean isReverse) {
                    item_layout.setBackgroundColor(ContextCompat.getColor(item_layout.getContext(), R.color.blue_100));
                }

                @Override
                public void onAnimationEnd(@NonNull Animator animation, boolean isReverse) {
                    but_delete.setVisibility(View.VISIBLE);
                }
            });
            translationXAnimator.start();
        }

        private void animateItemDeselected() {
            int offset = but_delete.getWidth();

            ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(item_layout, "translationX", -offset, 0);
            translationXAnimator.setDuration(300);
            translationXAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(@NonNull Animator animation, boolean isReverse) {
                    but_delete.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (item_layout.getTranslationX() == 0) {
                        item_layout.setBackgroundColor(ContextCompat.getColor(item_layout.getContext(), R.color.white));
                    }
                }
            });
            translationXAnimator.start();
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
    }
}