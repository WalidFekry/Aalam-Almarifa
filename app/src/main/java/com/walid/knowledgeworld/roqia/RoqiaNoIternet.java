package com.walid.knowledgeworld.roqia;

import android.os.Bundle;
import android.view.View;

import com.walid.knowledgeworld.base.BaseActivity;
import com.walid.knowledgeworld.databinding.ActivityRoqiaNoIternetBinding;

public class RoqiaNoIternet extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRoqiaNoIternetBinding binding = ActivityRoqiaNoIternetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());
    }

}
