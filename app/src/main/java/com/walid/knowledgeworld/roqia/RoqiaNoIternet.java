package com.walid.knowledgeworld.roqia;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.walid.knowledgeworld.databinding.ActivityRoqiaNoIternetBinding;

public class RoqiaNoIternet extends AppCompatActivity {

    private ActivityRoqiaNoIternetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoqiaNoIternetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (binding.backButton != null) {
            binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }
    }

    public void onBackClicked(View view) {
        getOnBackPressedDispatcher().onBackPressed();
    }
}
