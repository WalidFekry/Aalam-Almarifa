package com.walid.knowledgeworld.favorites;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.walid.knowledgeworld.databinding.ItemFavoriteBinding;
import com.walid.knowledgeworld.dialogs.QuoteImageDialog;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    public interface OnFavoriteChangeListener {
        void onFavoriteRemoved();
    }

    private final List<FavoriteItem> items;
    private final Context context;
    private final OnFavoriteChangeListener listener;

    public FavoritesAdapter(List<FavoriteItem> items, Context context, OnFavoriteChangeListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteBinding binding = ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteItem item = items.get(position);

        if (FavoriteItem.TYPE_QUOTE.equals(item.getType())) {
            holder.binding.favType.setText("اقتباس 💬");
            holder.binding.btnImageFav.setVisibility(View.VISIBLE);
        } else {
            holder.binding.favType.setText("مقال 📰");
            holder.binding.btnImageFav.setVisibility(View.GONE);
        }

        holder.binding.favTitle.setText(item.getTitle() != null ? item.getTitle().replace("_", "\n") : "");
        if (item.getContent() != null && !item.getContent().isEmpty()) {
            holder.binding.favContent.setText(item.getContent());
            holder.binding.favContent.setVisibility(View.VISIBLE);
        } else {
            holder.binding.favContent.setVisibility(View.GONE);
        }

        holder.binding.btnDeleteFav.setOnClickListener(v -> {
            FavoritesManager.getInstance(context).removeFavorite(item.getId());
            int adapterPos = holder.getBindingAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION && adapterPos < items.size()) {
                items.remove(adapterPos);
                notifyItemRemoved(adapterPos);
                if (listener != null) listener.onFavoriteRemoved();
            }
        });

        holder.binding.btnShareFav.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            String shareContent = item.getTitle();
            if (item.getUrl() != null && !item.getUrl().isEmpty()) {
                shareContent += "\n" + item.getUrl();
            }
            shareContent += "\n\nتم المشاركة عبر تطبيق عالم المعرفة 💙\nhttps://t.co/nEevBOppAH";
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
            context.startActivity(Intent.createChooser(sendIntent, "مشاركة المفضلة:"));
        });

        holder.binding.btnImageFav.setOnClickListener(v -> {
            QuoteImageDialog dialog = new QuoteImageDialog(context, item.getTitle());
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemFavoriteBinding binding;

        public ViewHolder(@NonNull ItemFavoriteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
