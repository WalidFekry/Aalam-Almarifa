package com.walid.knowledgeworld.roqia;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.walid.knowledgeworld.databinding.ActivityRoqiaNoIternetBinding;

public class RoqiaNoIternet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRoqiaNoIternetBinding binding = ActivityRoqiaNoIternetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());
    }

}
