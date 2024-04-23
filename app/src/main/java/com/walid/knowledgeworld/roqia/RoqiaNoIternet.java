package com.walid.knowledgeworld.roqia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.walid.knowledgeworld.R;

public class RoqiaNoIternet extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, RoqiaNoIternet.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roqia_no_iternet);
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }
}