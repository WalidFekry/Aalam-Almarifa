package com.walid.knowledgeworld.favorites;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.walid.knowledgeworld.base.BaseActivity;
import com.walid.knowledgeworld.databinding.ActivityFavoritesBinding;

import java.util.List;

public class FavoritesActivity extends BaseActivity {

    private ActivityFavoritesBinding binding;
    private List<FavoriteItem> favoriteItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        binding.recyclerFavorites.setLayoutManager(new LinearLayoutManager(this));
        loadFavorites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        favoriteItems = FavoritesManager.getInstance(this).getAllFavorites();
        if (favoriteItems != null && !favoriteItems.isEmpty()) {
            binding.emptyView.setVisibility(View.GONE);
            binding.recyclerFavorites.setVisibility(View.VISIBLE);
            FavoritesAdapter adapter = new FavoritesAdapter(favoriteItems, this, this::checkEmptyState);
            binding.recyclerFavorites.setAdapter(adapter);
        } else {
            checkEmptyState();
        }
    }

    private void checkEmptyState() {
        if (favoriteItems == null || favoriteItems.isEmpty()) {
            binding.emptyView.setVisibility(View.VISIBLE);
            binding.recyclerFavorites.setVisibility(View.GONE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
            binding.recyclerFavorites.setVisibility(View.VISIBLE);
        }
    }
}
