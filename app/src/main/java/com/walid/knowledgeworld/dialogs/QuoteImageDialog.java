package com.walid.knowledgeworld.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.walid.knowledgeworld.databinding.DialogQuoteImageBinding;
import com.walid.knowledgeworld.utils.QuoteImageGenerator;

public class QuoteImageDialog extends Dialog {

    private final String quoteText;
    private int selectedStyle = QuoteImageGenerator.STYLE_DEEP_ORANGE;
    private DialogQuoteImageBinding binding;

    public QuoteImageDialog(@NonNull Context context, String quoteText) {
        super(context);
        this.quoteText = quoteText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DialogQuoteImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        updatePreview();

        binding.btnStyleOrange.setOnClickListener(v -> {
            selectedStyle = QuoteImageGenerator.STYLE_DEEP_ORANGE;
            updatePreview();
        });

        binding.btnStyleBlue.setOnClickListener(v -> {
            selectedStyle = QuoteImageGenerator.STYLE_ROYAL_BLUE;
            updatePreview();
        });

        binding.btnStyleGreen.setOnClickListener(v -> {
            selectedStyle = QuoteImageGenerator.STYLE_EMERALD_GREEN;
            updatePreview();
        });

        binding.btnStylePurple.setOnClickListener(v -> {
            selectedStyle = QuoteImageGenerator.STYLE_PURPLE_NIGHT;
            updatePreview();
        });

        binding.btnStyleDark.setOnClickListener(v -> {
            selectedStyle = QuoteImageGenerator.STYLE_DARK_ELEGANT;
            updatePreview();
        });

        binding.btnShareImage.setOnClickListener(v -> shareImage());
        binding.btnCancel.setOnClickListener(v -> dismiss());
    }

    private void updatePreview() {
        Bitmap previewBitmap = QuoteImageGenerator.renderQuoteBitmap(getContext(), quoteText, selectedStyle);
        binding.imagePreview.setImageBitmap(previewBitmap);
    }

    private void shareImage() {
        Uri imageUri = QuoteImageGenerator.generateAndShareImage(getContext(), quoteText, selectedStyle);
        if (imageUri != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getContext().startActivity(Intent.createChooser(shareIntent, "مشاركة الاقتباس كصورة:"));
            dismiss();
        } else {
            Toast.makeText(getContext(), "تعذر إنشاء الصورة، المرجو المحاولة مجدداً", Toast.LENGTH_SHORT).show();
        }
    }
}
