package com.walid.knowledgeworld;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.walid.knowledgeworld.File_UIX.VideoEnabledWebChromeClient;
import com.walid.knowledgeworld.File_UIX.VideoEnabledWebView;
import com.walid.knowledgeworld.qoutes.QoutesActivity;
import com.walid.knowledgeworld.roqia.RoqiaNoIternet;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String[] permission = new String[]{
            Manifest.permission.POST_NOTIFICATIONS
    };
    boolean isPermission = false;
    private final ActivityResultLauncher<String> requestPermissionLauncherNotification =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    isPermission = true;
                } else {
                    isPermission = false;
                    showPermissionDialog();
                }
            });
    private VideoEnabledWebView webView;
    private VideoEnabledWebChromeClient webChromeClient;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private Button btn_1, btn_2, btn_3, btn_4, btn_5;
    private Animation tran_in, tran_out, tran;
    private Boolean open = true;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //add to your Activity before setContentView(R.layout.main_activity);:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_1 = findViewById(R.id.button1);
        btn_2 = findViewById(R.id.button2);
        btn_3 = findViewById(R.id.button3);
        btn_4 = findViewById(R.id.button4);
        btn_5 = findViewById(R.id.button5);

        tran_in = AnimationUtils.loadAnimation(this, R.anim.tran_in);
        tran_out = AnimationUtils.loadAnimation(this, R.anim.tran_out);
        tran = AnimationUtils.loadAnimation(this, R.anim.tran);
        btn_1.bringToFront();
        onClickButtons();


        //Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        // Save the web view
        webView = findViewById(R.id.webView);

        // Initialize the VideoEnabledWebChromeClient and set event handlers
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = findViewById(R.id.videoLayout); // Your own view, read class comments
        //noinspection all
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Your code...
                setTitle("انتظر .. جاري التحميل ⚡");
                if (progress == 100) {
                    setTitle(view.getTitle());
                    showAdsInterstitial();
                }
                super.onProgressChanged(view, progress);
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    MainActivity.this.getSupportActionBar().hide(); //Hide Toolbar
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 15) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

                    }
                } else {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    MainActivity.this.getSupportActionBar().show(); //Show Toolbar
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 15) {
                        //noinspection all
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        webView.setWebChromeClient(webChromeClient);
        // Call private class InsideWebViewClient
        webView.setWebViewClient(new InsideWebViewClient());

        //Offline webView
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // load online by default

        if (!isNetworkAvailable()) { // loading offline
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        // Navigate anywhere you want, but consider that this classes have only been tested on YouTube's mobile site
        String link = getIntent().getStringExtra("link");
        if (link == null) {
            webView.loadUrl("https://app-knowledge-world.blogspot.com");
        } else {
            webView.loadUrl(link);
        }

        //Firebase Message Sent
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //Start Ads
        setUpAds();

        //Notification Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            cheakNotificationPermission();
        }
    }

    private void setUpAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        // Ad Banner
        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);

        InterstitialAd.load(this, getString(R.string.app_interstitial_admob), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
//                Log.i(TAG, "onAdLoaded");

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                // Handle the error
//                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }

    private void cheakNotificationPermission() {
        if (!isPermission) {
            requestPermissionsNotfication();
        } else {
            Toast.makeText(this, "Granted done", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermissionsNotfication() {
        if (ContextCompat.checkSelfPermission(this, permission[0]) == PackageManager.PERMISSION_GRANTED) {
            isPermission = true;
        } else {
            requestPermissionLauncherNotification.launch(permission[0]);
        }
    }

    private void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setMessage("من فضلك قم بالموافقة على الاشعارات لكي تصلك رسائل التفاؤل والإقتباسات من التطبيق ..")
                .setPositiveButton("الاعدادات", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void showAdsInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // Standard back button implementation (for example this could close the app)
            new PrettyDialog(this)
                    .setTitle("هل تريد الخروج من تطبيق عالم المعرفة \uD83E\uDD14")
                    .setMessage("إذا اعجبك تطبيق \" عالم المعرفة \" لا تترد في أن تدعمنا بتقييمك للتطبيق لنستمر دائماً في اسعادك و نشر المزيد  \uD83C\uDF3A")
                    .setIcon(R.drawable.icon_log_out)
                    .addButton(
                            "خروج \uD83D\uDE22",                    // button text
                            R.color.pdlg_color_white,        // button text color
                            R.color.colorAccent,        // button background color
                            new PrettyDialogCallback() {        // button OnClick listener
                                @Override
                                public void onClick() {
                                    finish();
                                }
                            }
                    )
                    .addButton(
                            "تقييم التطبيق ❤",
                            R.color.pdlg_color_white,
                            R.color.colorAccent,
                            new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    // Dismiss
                                    Intent morapp = new Intent(Intent.ACTION_VIEW);
                                    morapp.setData(Uri.parse("https://t.co/nEevBOppAH?" + getPackageName()));
                                    startActivity(morapp);
                                }
                            }
                    )
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            webView.reload();
            showAdsInterstitial();
            return true;
        }
        if (id == R.id.action_copyUrl) {
            String url = webView.getUrl();

            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", url);
            Toast.makeText(getApplicationContext(), "تم نسخ رابط المقال بنجاح يمكنك الآن مشاركتة ✅",
                    Toast.LENGTH_LONG).show();
            clipboard.setPrimaryClip(clip);

            return true;
        }
        if (id == R.id.action_home) {
            webView.loadUrl("https://app-knowledge-world.blogspot.com");
            return true;
        }
        if (id == R.id.action_About) {
            webView.loadUrl(getString(R.string.aboutApp));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            webView.loadUrl("https://app-knowledge-world.blogspot.com");
            //Interstitial Show

        } else if (id == R.id.nav_2) {
            Intent morapp = new Intent(Intent.ACTION_VIEW);
            morapp.setData(Uri.parse("https://play.google.com/store/apps/dev?id=6257553101128037563"));
            startActivity(morapp);

        } else if (id == R.id.nav_3) {
            webView.loadUrl("https://sites.google.com/view/appknowledgeworld/home");
        } else if (id == R.id.nav_99) {
            webView.loadUrl("https://app-knowledge-world.blogspot.com/2019/07/blog-post_26.html");
            //Interstitial Show


        } else if (id == R.id.nav_990) {
            webView.loadUrl("https://app-knowledge-world.blogspot.com/2019/07/blog-post_35.html");
            //Interstitial Show

        } else if (id == R.id.nav_2626) {
            startActivity(new Intent(MainActivity.this, RoqiaNoIternet.class));
            showAdsInterstitial();
        } else if (id == R.id.nav_2627) {
            startActivity(new Intent(MainActivity.this, QoutesActivity.class));
        } else if (id == R.id.nav_4) {
            String chr = getString(R.string.descrip);
            String cherelien = "https://t.co/nEevBOppAH?" + getPackageName();
            Intent chare = new Intent(Intent.ACTION_SEND);
            chare.setType("text/plain");
            chare.putExtra(Intent.EXTRA_TEXT, chr + "\n" + cherelien);
            startActivity(chare);

        } else if (id == R.id.nav_5) {
            Intent morapp = new Intent(Intent.ACTION_VIEW);
            morapp.setData(Uri.parse("https://t.co/nEevBOppAH?" + getPackageName()));
            startActivity(morapp);

        } else if (id == R.id.nav_6) {
            String txt = "";
            Intent sendemail = new Intent(Intent.ACTION_SEND);
            sendemail.setData(Uri.parse("mailto:"));
            sendemail.setType("plain/text");
            sendemail.putExtra(Intent.EXTRA_EMAIL, new String[]{"prowalidfekry@gmail.com"});
            sendemail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name2));
            sendemail.putExtra(Intent.EXTRA_TEXT, txt);
            startActivity(sendemail);

        } else if (id == R.id.nav_7) {
            Intent morapp = new Intent(Intent.ACTION_VIEW);
            morapp.setData(Uri.parse("https://www.facebook.com/App.Thawap"));
            startActivity(morapp);

        } else if (id == R.id.nav_8) {
            Intent morapp = new Intent(Intent.ACTION_VIEW);
            morapp.setData(Uri.parse("https://www.facebook.com/groups/440403217380641"));
            startActivity(morapp);

        } else if (id == R.id.nav_qism_1) {
            webView.loadUrl(getString(R.string.qism_1));
            //Interstitial Show

        } else if (id == R.id.nav_qism_2) {
            webView.loadUrl(getString(R.string.qism_2));
        } else if (id == R.id.nav_qism_3) {
            webView.loadUrl(getString(R.string.qism_3));
            //Interstitial Show

        } else if (id == R.id.nav_qism_4) {
            webView.loadUrl(getString(R.string.qism_4));
        } else if (id == R.id.nav_qism_5) {
            webView.loadUrl(getString(R.string.qism_5));
            //Interstitial Show


        } else if (id == R.id.nav_qism_6) {
            webView.loadUrl(getString(R.string.qism_6));
        } else if (id == R.id.nav_qism_7) {
            webView.loadUrl(getString(R.string.qism_7));
            //Interstitial Show

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // isNetworkAvaible
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        //If Internet Null show Message
        if (activeNetworkInfo == null) {
            final PrettyDialog pDialog = new PrettyDialog(this);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setTitle("تنبيه !")
                    .setMessage("المرجو التحقق من تشغيل الإنترنت كي يتم جلب البيانات الجديدة.").
                    setIcon(R.mipmap.ic_launcher)
                    .setAnimationEnabled(true)
                    .setMessageColor(R.color.colorPrimary)
                    .setTitleColor(R.color.colorAccent)
                    .setIconTint(R.color.pdlg_color_white)
                    .setGravity(Gravity.DISPLAY_CLIP_HORIZONTAL)
                    .setTypeface(Typeface.createFromAsset(getAssets(), "jazeera.ttf"))
                    .addButton(
                            "حسناً",
                            R.color.pdlg_color_white,
                            R.color.colorPrimary,
                            new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    pDialog.dismiss();
                                }
                            }
                    )
                    .show();
        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void button1(View view) {

        if (open) {

            open = false;
            btn_1.startAnimation(tran);
            btn_2.startAnimation(tran_in);
            btn_3.startAnimation(tran_in);
            btn_4.startAnimation(tran_in);
            btn_5.startAnimation(tran_in);

            btn_2.setVisibility(View.VISIBLE);
            btn_3.setVisibility(View.VISIBLE);
            btn_4.setVisibility(View.VISIBLE);
            btn_5.setVisibility(View.VISIBLE);
        } else {
            open = true;
            btn_1.startAnimation(tran);
            btn_2.startAnimation(tran_out);
            btn_3.startAnimation(tran_out);
            btn_4.startAnimation(tran_out);
            btn_5.startAnimation(tran_out);

            btn_2.setVisibility(View.INVISIBLE);
            btn_3.setVisibility(View.INVISIBLE);
            btn_4.setVisibility(View.INVISIBLE);
            btn_5.setVisibility(View.INVISIBLE);
        }
    }

    private void onClickButtons() {
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareText = webView.getUrl();
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                try {
                    MainActivity.this.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "هذا التطبيق غير منصب في الهاتف!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String shareText = webView.getUrl();
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.twitter.android");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                try {
                    MainActivity.this.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "هذا التطبيق غير منصب في الهاتف!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String shareText = webView.getUrl();
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.facebook.katana");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                try {
                    MainActivity.this.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "هذا التطبيق غير منصب في الهاتف!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String shareText = webView.getUrl();
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.facebook.orca");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                try {
                    MainActivity.this.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "هذا التطبيق غير منصب في الهاتف!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class InsideWebViewClient extends WebViewClient {
        @Override
        // Force links to be opened inside WebView and not in Default Browser
        // Thanks http://stackoverflow.com/a/33681975/1815624
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
