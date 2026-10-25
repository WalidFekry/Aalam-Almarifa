package com.walid.knowledgeworld.favorites;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {

    private static final String PREF_NAME = "knowledgeworld_favorites";
    private static final String KEY_FAVORITES_LIST = "favorites_json_array";

    private static FavoritesManager instance;
    private final SharedPreferences prefs;

    private FavoritesManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized FavoritesManager getInstance(Context context) {
        if (instance == null) {
            instance = new FavoritesManager(context);
        }
        return instance;
    }

    public synchronized List<FavoriteItem> getAllFavorites() {
        List<FavoriteItem> list = new ArrayList<>();
        String jsonString = prefs.getString(KEY_FAVORITES_LIST, "[]");
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                FavoriteItem item = new FavoriteItem(
                        obj.optString("id"),
                        obj.optString("type"),
                        obj.optString("title"),
                        obj.optString("content"),
                        obj.optString("url"),
                        obj.optLong("timestamp")
                );
                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public synchronized boolean isFavorite(String id) {
        if (id == null || id.isEmpty()) return false;
        List<FavoriteItem> all = getAllFavorites();
        for (FavoriteItem item : all) {
            if (id.equals(item.getId())) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean toggleFavorite(FavoriteItem item) {
        if (item == null || item.getId() == null) return false;
        if (isFavorite(item.getId())) {
            removeFavorite(item.getId());
            return false;
        } else {
            addFavorite(item);
            return true;
        }
    }

    public synchronized void addFavorite(FavoriteItem item) {
        if (item == null || item.getId() == null) return;
        List<FavoriteItem> all = getAllFavorites();
        for (int i = 0; i < all.size(); i++) {
            if (item.getId().equals(all.get(i).getId())) {
                all.set(i, item);
                saveList(all);
                return;
            }
        }
        all.add(0, item);
        saveList(all);
    }

    public synchronized void removeFavorite(String id) {
        if (id == null) return;
        List<FavoriteItem> all = getAllFavorites();
        List<FavoriteItem> updated = new ArrayList<>();
        for (FavoriteItem item : all) {
            if (!id.equals(item.getId())) {
                updated.add(item);
            }
        }
        saveList(updated);
    }

    private synchronized void saveList(List<FavoriteItem> list) {
        JSONArray array = new JSONArray();
        for (FavoriteItem item : list) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("id", item.getId());
                obj.put("type", item.getType());
                obj.put("title", item.getTitle());
                obj.put("content", item.getContent());
                obj.put("url", item.getUrl());
                obj.put("timestamp", item.getTimestamp());
                array.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        prefs.edit().putString(KEY_FAVORITES_LIST, array.toString()).apply();
    }
}
