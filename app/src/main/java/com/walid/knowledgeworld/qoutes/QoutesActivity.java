package com.walid.knowledgeworld.qoutes;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.walid.knowledgeworld.R;
import com.walid.knowledgeworld.base.BaseActivity;
import com.walid.knowledgeworld.databinding.ActivityQoutesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QoutesActivity extends BaseActivity implements Adaptersada.OnMessengerClick {

    private static final String TAG = "QoutesActivity";
    private final String apiUrl = "https://post.walid-fekry.com/auto_notification/auto_notification_apps/api_post_apps.php";

    private ActivityQoutesBinding binding;
    private List<new_item_post> list2;
    private InterstitialAd mInterstitialAd;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityQoutesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        if (isNetworkConnected()) {
            showAdsInterstitial();
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.rec2.setLayoutManager(mLayoutManager);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_anmation);
        binding.rec2.setLayoutAnimation(animation);

        fetchQuotesData();

        MobileAds.initialize(this, initializationStatus -> {});
        AdRequest adRequest = new AdRequest.Builder().build();

        binding.adView2.loadAd(adRequest);

        InterstitialAd.load(this, getString(R.string.app_interstitial_admob), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        Network network = cm.getActiveNetwork();
        if (network == null) return false;
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        return capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    private void fetchQuotesData() {
        executorService.execute(() -> {
            boolean success = false;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(apiUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    success = true;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching quotes data", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            final boolean fetchSuccess = success;
            getHandler().post(() -> {
                if (isFinishing() || isDestroyed()) return;

                if (fetchSuccess && list2 != null && !list2.isEmpty()) {
                    Adaptersada adaptersada = new Adaptersada(list2, QoutesActivity.this);
                    binding.rec2.setAdapter(adaptersada);
                } else {
                    loadFallbackData();
                }
            });
        });
    }

    private void loadFallbackData() {
        list2 = new ArrayList<>();
        list2.add(new new_item_post(getResources().getString(R.string.my42)));
        list2.add(new new_item_post(getResources().getString(R.string.my1)));
        list2.add(new new_item_post(getResources().getString(R.string.my2)));
        list2.add(new new_item_post(getResources().getString(R.string.my3)));
        list2.add(new new_item_post(getResources().getString(R.string.my4)));
        list2.add(new new_item_post(getResources().getString(R.string.my5)));
        list2.add(new new_item_post(getResources().getString(R.string.my6)));
        list2.add(new new_item_post(getResources().getString(R.string.my7)));
        list2.add(new new_item_post(getResources().getString(R.string.my8)));
        list2.add(new new_item_post(getResources().getString(R.string.my9)));
        list2.add(new new_item_post(getResources().getString(R.string.my10)));
        list2.add(new new_item_post(getResources().getString(R.string.my11)));
        list2.add(new new_item_post(getResources().getString(R.string.my12)));
        list2.add(new new_item_post(getResources().getString(R.string.my13)));
        list2.add(new new_item_post(getResources().getString(R.string.my14)));
        list2.add(new new_item_post(getResources().getString(R.string.my15)));
        list2.add(new new_item_post(getResources().getString(R.string.my16)));
        list2.add(new new_item_post(getResources().getString(R.string.my17)));
        list2.add(new new_item_post(getResources().getString(R.string.my18)));
        list2.add(new new_item_post(getResources().getString(R.string.my19)));
        list2.add(new new_item_post(getResources().getString(R.string.my21)));
        list2.add(new new_item_post(getResources().getString(R.string.my22)));
        list2.add(new new_item_post(getResources().getString(R.string.my23)));
        list2.add(new new_item_post(getResources().getString(R.string.my26)));
        list2.add(new new_item_post(getResources().getString(R.string.my27)));
        list2.add(new new_item_post(getResources().getString(R.string.my28)));
        list2.add(new new_item_post(getResources().getString(R.string.my29)));
        list2.add(new new_item_post(getResources().getString(R.string.my30)));
        list2.add(new new_item_post(getResources().getString(R.string.my31)));
        list2.add(new new_item_post(getResources().getString(R.string.my32)));
        list2.add(new new_item_post(getResources().getString(R.string.my33)));
        list2.add(new new_item_post(getResources().getString(R.string.my34)));
        list2.add(new new_item_post(getResources().getString(R.string.my35)));
        list2.add(new new_item_post(getResources().getString(R.string.my36)));
        list2.add(new new_item_post(getResources().getString(R.string.my37)));
        list2.add(new new_item_post(getResources().getString(R.string.my38)));
        list2.add(new new_item_post(getResources().getString(R.string.my39)));
        list2.add(new new_item_post(getResources().getString(R.string.my40)));
        list2.add(new new_item_post(getResources().getString(R.string.my41)));
        list2.add(new new_item_post(getResources().getString(R.string.staticvalue)));

        Adaptersada adapter2 = new Adaptersada(list2, QoutesActivity.this);
        binding.rec2.setAdapter(adapter2);
        Toast.makeText(QoutesActivity.this, "توجد مشكله با الاتصال تاكد ان الانترنت متاح لديك ", Toast.LENGTH_SHORT).show();
    }

    private void showAdsInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(QoutesActivity.this);
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.");
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray post = response.optJSONArray("post");
            if (post != null) {
                list2 = new ArrayList<>();
                for (int i = 0; i < post.length(); i++) {
                    JSONObject posts = post.optJSONObject(i);
                    if (posts != null) {
                        String title = posts.optString("title");
                        String imageurl = posts.optString("imageurl");
                        int id = posts.optInt("id");
                        String createdAt = posts.optString("created_at");
                        list2.add(new new_item_post(title, imageurl, id, createdAt));
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error", e);
        }
    }

    @Override
    public void onMessengerItemClick(new_item_post item) {
    }

    @Override
    protected void onDestroy() {
        executorService.shutdown();
        super.onDestroy();
    }
}
