package com.walid.knowledgeworld.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

public class QuoteImageGenerator {

    private static final String TAG = "QuoteImageGenerator";

    public static final int STYLE_DEEP_ORANGE = 0;
    public static final int STYLE_ROYAL_BLUE = 1;
    public static final int STYLE_EMERALD_GREEN = 2;
    public static final int STYLE_PURPLE_NIGHT = 3;
    public static final int STYLE_DARK_ELEGANT = 4;

    public static Uri generateAndShareImage(Context context, String quoteText, int styleIndex) {
        Bitmap bitmap = renderQuoteBitmap(context, quoteText, styleIndex);

        try {
            File imagesFolder = new File(context.getCacheDir(), "images");
            if (!imagesFolder.exists()) {
                boolean created = imagesFolder.mkdirs();
                if (!created && !imagesFolder.exists()) {
                    Log.e(TAG, "Failed to create images cache folder");
                }
            }

            File imageFile = new File(imagesFolder, "quote_" + System.currentTimeMillis() + ".png");
            FileOutputStream stream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();

            String authority = context.getPackageName() + ".fileprovider";
            return FileProvider.getUriForFile(context, authority, imageFile);
        } catch (Exception e) {
            Log.e(TAG, "Error generating quote image", e);
            return null;
        }
    }

    public static Bitmap renderQuoteBitmap(Context context, String quoteText, int styleIndex) {
        int width = 1080;
        int height = 1080;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        int[] colors;
        switch (styleIndex) {
            case STYLE_ROYAL_BLUE:
                colors = new int[]{Color.parseColor("#0F2027"), Color.parseColor("#203A43"), Color.parseColor("#2C5364")};
                break;
            case STYLE_EMERALD_GREEN:
                colors = new int[]{Color.parseColor("#11998e"), Color.parseColor("#38ef7d")};
                break;
            case STYLE_PURPLE_NIGHT:
                colors = new int[]{Color.parseColor("#23074d"), Color.parseColor("#cc5333")};
                break;
            case STYLE_DARK_ELEGANT:
                colors = new int[]{Color.parseColor("#141E30"), Color.parseColor("#243B55")};
                break;
            case STYLE_DEEP_ORANGE:
            default:
                colors = new int[]{Color.parseColor("#FF8000"), Color.parseColor("#D66A00"), Color.parseColor("#803B00")};
                break;
        }

        Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LinearGradient gradient = new LinearGradient(0, 0, width, height, colors, null, Shader.TileMode.CLAMP);
        backgroundPaint.setShader(gradient);
        canvas.drawRect(0, 0, width, height, backgroundPaint);

        Paint cardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cardPaint.setColor(Color.argb(35, 255, 255, 255));
        cardPaint.setStyle(Paint.Style.FILL);
        RectF cardRect = new RectF(60, 60, width - 60, height - 60);
        canvas.drawRoundRect(cardRect, 40, 40, cardPaint);

        Paint cardBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        cardBorder.setColor(Color.argb(80, 255, 255, 255));
        cardBorder.setStyle(Paint.Style.STROKE);
        cardBorder.setStrokeWidth(3f);
        canvas.drawRoundRect(cardRect, 40, 40, cardBorder);

        Typeface typeface = null;
        try {
            typeface = Typeface.createFromAsset(context.getAssets(), "jazeera.ttf");
        } catch (Exception ignored) {
        }

        TextPaint markPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        markPaint.setColor(Color.argb(120, 255, 255, 255));
        markPaint.setTextSize(140f);
        markPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("“", width / 2f, 180, markPaint);

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(52f);
        if (typeface != null) textPaint.setTypeface(typeface);

        int textWidth = width - 200;
        String formattedText = quoteText != null ? quoteText.replace("_", "\n") : "";

        StaticLayout staticLayout = StaticLayout.Builder.obtain(formattedText, 0, formattedText.length(), textPaint, textWidth)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setLineSpacing(0f, 1.3f)
                .setIncludePad(true)
                .build();

        canvas.save();
        float textY = (height - staticLayout.getHeight()) / 2f;
        if (textY < 220) textY = 220;
        canvas.translate(width / 2f - textWidth / 2f, textY);
        staticLayout.draw(canvas);
        canvas.restore();

        Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.argb(100, 255, 255, 255));
        linePaint.setStrokeWidth(2f);
        canvas.drawLine(200, height - 150, width - 200, height - 150, linePaint);

        TextPaint brandPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        brandPaint.setColor(Color.WHITE);
        brandPaint.setTextSize(36f);
        brandPaint.setTextAlign(Paint.Align.CENTER);
        if (typeface != null) brandPaint.setTypeface(typeface);
        canvas.drawText("🌸 تطبيق عالم المعرفة 🌸", width / 2f, height - 90, brandPaint);

        return bitmap;
    }
}
