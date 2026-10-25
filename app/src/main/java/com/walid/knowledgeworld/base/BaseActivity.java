package com.walid.knowledgeworld.base;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Dialog loadingDialog;

    public Handler getHandler() {
        return handler;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showLoading() {
        if (loadingDialog != null) {
            hideLoading();
        }
        loadingDialog = new Dialog(this);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    @Override
    public void showMessage(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void hideKeyboard() {
        // Option to implement soft keyboard hiding if needed
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
