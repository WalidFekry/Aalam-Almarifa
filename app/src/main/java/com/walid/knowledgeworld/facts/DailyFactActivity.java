package com.walid.knowledgeworld.facts;

import android.content.Intent;
import android.os.Bundle;

import com.walid.knowledgeworld.base.BaseActivity;
import com.walid.knowledgeworld.databinding.ActivityDailyFactBinding;
import com.walid.knowledgeworld.dialogs.QuoteImageDialog;

import java.util.List;
import java.util.Random;

public class DailyFactActivity extends BaseActivity {

    private ActivityDailyFactBinding binding;
    private String currentFact;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDailyFactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        currentFact = DailyFactManager.getTodayFact();
        displayFact(currentFact);

        binding.btnShareFactImage.setOnClickListener(v -> {
            QuoteImageDialog dialog = new QuoteImageDialog(this, currentFact);
            dialog.show();
        });

        binding.btnShareFactText.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, currentFact + "\n\nتم مشاركتها عبر تطبيق عالم المعرفة 💙\nhttps://t.co/nEevBOppAH");
            startActivity(Intent.createChooser(sendIntent, "مشاركة معلومة:"));
        });

        binding.btnNextFact.setOnClickListener(v -> {
            List<String> facts = DailyFactManager.getAllFacts();
            if (!facts.isEmpty()) {
                currentFact = facts.get(random.nextInt(facts.size()));
                displayFact(currentFact);
            }
        });
    }

    private void displayFact(String fact) {
        if (binding != null && fact != null) {
            binding.factText.setText(fact);
        }
    }
}
