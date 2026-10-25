package com.walid.knowledgeworld;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.walid.knowledgeworld.web_view.VideoEnabledWebChromeClient;
import com.walid.knowledgeworld.web_view.VideoEnabledWebView;
import com.walid.knowledgeworld.databinding.ActivityMainBinding;
import com.walid.knowledgeworld.qoutes.QoutesActivity;
import com.walid.knowledgeworld.roqia.RoqiaNoIternet;

import libs.mjn.prettydialog.PrettyDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final String DEFAULT_URL = "https://app-knowledge-world.blogspot.com";

    private ActivityMainBinding binding;
    private VideoEnabledWebView webView;
    private InterstitialAd mInterstitialAd;

    private Animation tranIn, tranOut, tran;
    private boolean isMenuOpen = false;

    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    showPermissionDialog();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        tranIn = AnimationUtils.loadAnimation(this, R.anim.tran_in);
        tranOut = AnimationUtils.loadAnimation(this, R.anim.tran_out);
        tran = AnimationUtils.loadAnimation(this, R.anim.tran);

        binding.appBarMain.button1.bringToFront();
        binding.appBarMain.button1.setOnClickListener(this::toggleFloatingButtons);
        setupFloatingButtons();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarMain.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);

        setupWebView();
        setupBackPressHandler();

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        setUpAds();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        webView = binding.appBarMain.contentMain.webView;
        View nonVideoLayout = binding.appBarMain.contentMain.nonVideoLayout;
        ViewGroup videoLayout = binding.appBarMain.contentMain.videoLayout;
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, videoLayout, false);

        VideoEnabledWebChromeClient webChromeClient = new VideoEnabledWebChromeClient(
                nonVideoLayout, videoLayout, loadingView, webView) {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                setTitle("انتظر .. جاري التحميل ⚡");
                if (progress == 100) {
                    setTitle(view.getTitle());
                    showAdsInterstitial();
                }
                super.onProgressChanged(view, progress);
            }
        };

        webChromeClient.setOnToggledFullscreen(fullscreen -> {
            WindowInsetsControllerCompat insetsController =
                    WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
            if (fullscreen) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().hide();
                }
                if (insetsController != null) {
                    insetsController.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());
                    insetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                }
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }
                if (insetsController != null) {
                    insetsController.show(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());
                }
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });

        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new InsideWebViewClient());

        WebSettings settings = webView.getSettings();
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        @SuppressLint("SetJavaScriptEnabled")
        boolean jsEnabled = true;
        settings.setJavaScriptEnabled(jsEnabled);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        if (!isNetworkAvailable()) {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        String link = getIntent().getStringExtra("link");
        if (link == null || link.trim().isEmpty()) {
            webView.loadUrl(DEFAULT_URL);
        } else {
            webView.loadUrl(link);
        }
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    return;
                }
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();
                    return;
                }
                showExitDialog();
            }
        });
    }

    private void showExitDialog() {
        new PrettyDialog(this)
                .setTitle("هل تريد الخروج من تطبيق عالم المعرفة")
                .setMessage("إذا اعجبك تطبيق \" عالم المعرفة \" لا تترد في أن تدعمنا بتقييمك للتطبيق لنستمر دائماً في اسعادك و نشر المزيد 🌸")
                .setIcon(R.drawable.icon_log_out)
                .addButton(
                        "خروج",
                        R.color.pdlg_color_white,
                        R.color.colorAccent,
                        this::finish
                )
                .addButton(
                        "تقييم التطبيق",
                        R.color.pdlg_color_white,
                        R.color.colorAccent,
                        () -> {
                            Intent moreApp = new Intent(Intent.ACTION_VIEW);
                            moreApp.setData(Uri.parse("https://t.co/nEevBOppAH?" + getPackageName()));
                            startActivity(moreApp);
                        }
                )
                .show();
    }

    private void setUpAds() {
        MobileAds.initialize(this, initializationStatus -> {});
        AdRequest adRequest = new AdRequest.Builder().build();

        binding.appBarMain.contentMain.adView.loadAd(adRequest);

        InterstitialAd.load(this, getString(R.string.app_interstitial_admob), adRequest,
                new InterstitialAdLoadCallback() {
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

    private void showAdsInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.");
        }
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setMessage("من فضلك قم بالموافقة على الاشعارات لكي تصلك رسائل التفاؤل والإقتباسات من التطبيق ..")
                .setPositiveButton("الاعدادات", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    dialog.dismiss();
                })
                .setNegativeButton("إلغاء", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        Network network = cm.getActiveNetwork();
        if (network == null) {
            showNoInternetDialog();
            return false;
        }

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        boolean isConnected = capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));

        if (!isConnected) {
            showNoInternetDialog();
        }

        return isConnected;
    }

    private void showNoInternetDialog() {
        final PrettyDialog pDialog = new PrettyDialog(this);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setTitle("تنبيه !")
                .setMessage("المرجو التحقق من تشغيل الإنترنت كي يتم جلب البيانات الجديدة.")
                .setIcon(R.mipmap.ic_launcher)
                .setAnimationEnabled(true)
                .setMessageColor(R.color.colorPrimary)
                .setTitleColor(R.color.colorAccent)
                .setIconTint(R.color.pdlg_color_white)
                .setGravity(Gravity.CENTER)
                .setTypeface(Typeface.createFromAsset(getAssets(), "jazeera.ttf"))
                .addButton("حسناً", R.color.pdlg_color_white, R.color.colorPrimary, pDialog::dismiss)
                .show();
    }

    public void toggleFloatingButtons(View view) {
        if (!isMenuOpen) {
            isMenuOpen = true;
            binding.appBarMain.button1.startAnimation(tran);
            binding.appBarMain.button2.startAnimation(tranIn);
            binding.appBarMain.button3.startAnimation(tranIn);
            binding.appBarMain.button4.startAnimation(tranIn);
            binding.appBarMain.button5.startAnimation(tranIn);

            binding.appBarMain.button2.setVisibility(View.VISIBLE);
            binding.appBarMain.button3.setVisibility(View.VISIBLE);
            binding.appBarMain.button4.setVisibility(View.VISIBLE);
            binding.appBarMain.button5.setVisibility(View.VISIBLE);
        } else {
            isMenuOpen = false;
            binding.appBarMain.button1.startAnimation(tran);
            binding.appBarMain.button2.startAnimation(tranOut);
            binding.appBarMain.button3.startAnimation(tranOut);
            binding.appBarMain.button4.startAnimation(tranOut);
            binding.appBarMain.button5.startAnimation(tranOut);

            binding.appBarMain.button2.setVisibility(View.INVISIBLE);
            binding.appBarMain.button3.setVisibility(View.INVISIBLE);
            binding.appBarMain.button4.setVisibility(View.INVISIBLE);
            binding.appBarMain.button5.setVisibility(View.INVISIBLE);
        }
    }

    private void setupFloatingButtons() {
        binding.appBarMain.button2.setOnClickListener(v -> shareToApp("com.whatsapp"));
        binding.appBarMain.button3.setOnClickListener(v -> shareToApp("com.twitter.android"));
        binding.appBarMain.button4.setOnClickListener(v -> shareToApp("com.facebook.katana"));
        binding.appBarMain.button5.setOnClickListener(v -> shareToApp("com.facebook.orca"));
    }

    private void shareToApp(String packageName) {
        String shareText = webView != null ? webView.getUrl() : DEFAULT_URL;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage(packageName);
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "هذا التطبيق غير منصب في الهاتف!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if (webView != null) webView.reload();
            return true;
        } else if (id == R.id.action_copyUrl) {
            if (webView != null) {
                String url = webView.getUrl();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    ClipData clip = ClipData.newPlainText("label", url);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "تم نسخ رابط المقال بنجاح يمكنك الآن مشاركتة ✅", Toast.LENGTH_LONG).show();
                }
            }
            return true;
        } else if (id == R.id.action_home) {
            if (webView != null) webView.loadUrl(DEFAULT_URL);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (webView != null) webView.loadUrl(DEFAULT_URL);
        } else if (id == R.id.nav_2) {
            Intent moreApp = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=6257553101128037563"));
            startActivity(moreApp);
        } else if (id == R.id.nav_3) {
            if (webView != null) webView.loadUrl("https://sites.google.com/view/appknowledgeworld/home");
        } else if (id == R.id.nav_99) {
            if (webView != null) webView.loadUrl("https://app-knowledge-world.blogspot.com/2019/07/blog-post_26.html");
        } else if (id == R.id.nav_990) {
            if (webView != null) webView.loadUrl("https://app-knowledge-world.blogspot.com/2019/07/blog-post_35.html");
        } else if (id == R.id.nav_2626) {
            showAdsInterstitial();
            startActivity(new Intent(MainActivity.this, RoqiaNoIternet.class));
        } else if (id == R.id.nav_2627) {
            startActivity(new Intent(MainActivity.this, QoutesActivity.class));
        } else if (id == R.id.nav_4) {
            String desc = getString(R.string.descrip);
            String shareUrl = "https://t.co/nEevBOppAH?" + getPackageName();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, desc + "\n" + shareUrl);
            startActivity(shareIntent);
        } else if (id == R.id.nav_5) {
            Intent rateApp = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/nEevBOppAH?" + getPackageName()));
            startActivity(rateApp);
        } else if (id == R.id.nav_6) {
            Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
            sendEmail.setData(Uri.parse("mailto:prowalidfekry@gmail.com"));
            sendEmail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name2));
            try {
                startActivity(sendEmail);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "لا يوجد تطبيق بريد إلكتروني مثبت", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_7) {
            Intent fbPage = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/App.Thawap"));
            startActivity(fbPage);
        } else if (id == R.id.nav_8) {
            Intent fbGroup = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/440403217380641"));
            startActivity(fbGroup);
        } else if (id == R.id.nav_qism_1) {
            if (webView != null) webView.loadUrl(getString(R.string.qism_1));
        } else if (id == R.id.nav_qism_2) {
            if (webView != null) webView.loadUrl(getString(R.string.qism_2));
        } else if (id == R.id.nav_qism_3) {
            if (webView != null) webView.loadUrl(getString(R.string.qism_3));
        } else if (id == R.id.nav_qism_4) {
            if (webView != null) webView.loadUrl(getString(R.string.qism_4));
        } else if (id == R.id.nav_qism_5) {
            if (webView != null) webView.loadUrl(getString(R.string.qism_5));
        } else if (id == R.id.nav_qism_6) {
            if (webView != null) webView.loadUrl(getString(R.string.qism_6));
        } else if (id == R.id.nav_qism_7) {
            if (webView != null) webView.loadUrl(getString(R.string.qism_7));
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private static class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
