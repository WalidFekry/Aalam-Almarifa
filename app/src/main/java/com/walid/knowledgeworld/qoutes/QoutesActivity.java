package com.walid.knowledgeworld.qoutes;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class QoutesActivity extends BaseActivity {

    private static final String TAG = "QoutesActivity";
    private static final String API_URL = "https://post.walid-fekry.com/auto_notification/auto_notification_apps/api_post_apps.php";

    private ActivityQoutesBinding binding;
    private List<new_item_post> list2;
    private InterstitialAd mInterstitialAd;

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowInsetsControllerCompat insetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (insetsController != null) {
            insetsController.hide(WindowInsetsCompat.Type.statusBars());
            insetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }

        binding = ActivityQoutesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.rec2.setLayoutManager(mLayoutManager);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_anmation);
        binding.rec2.setLayoutAnimation(animation);

        fetchQuotesData();

        AdRequest adRequest = new AdRequest.Builder().build();

        binding.adView2.loadAd(adRequest);

        InterstitialAd.load(this, getString(R.string.app_interstitial_admob), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                showAdsInterstitial();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });
    }

    private void fetchQuotesData() {
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Error fetching quotes data via OkHttp", e);
                getHandler().post(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    loadFallbackData();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                boolean success = false;
                try (ResponseBody responseBody = response.body()) {
                    if (response.isSuccessful() && responseBody != null) {
                        String jsonString = responseBody.string();
                        success = parseResult(jsonString);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing OkHttp response", e);
                }

                final boolean isSuccess = success;
                getHandler().post(() -> {
                    if (isFinishing() || isDestroyed()) return;

                    if (isSuccess && list2 != null && !list2.isEmpty()) {
                        Adaptersada adaptersada = new Adaptersada(list2, QoutesActivity.this);
                        binding.rec2.setAdapter(adaptersada);
                    } else {
                        loadFallbackData();
                    }
                });
            }
        });
    }

    private boolean parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray postArray = response.optJSONArray("post");
            if (postArray != null && postArray.length() > 0) {
                List<new_item_post> parsedList = new ArrayList<>();
                for (int i = 0; i < postArray.length(); i++) {
                    JSONObject posts = postArray.optJSONObject(i);
                    if (posts != null) {
                        String title = posts.optString("title");
                        String imageurl = posts.optString("imageurl");
                        int id = posts.optInt("id");
                        String createdAt = posts.optString("created_at");
                        parsedList.add(new new_item_post(title, imageurl, id, createdAt));
                    }
                }
                if (!parsedList.isEmpty()) {
                    list2 = parsedList;
                    return true;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error", e);
        }
        return false;
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
        list2.add(new new_item_post(getResources().getString(R.string.my25)));
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
}
