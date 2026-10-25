package com.walid.knowledgeworld.qoutes;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.walid.knowledgeworld.R;
import com.walid.knowledgeworld.databinding.ItemReslatfoalBinding;
import com.walid.knowledgeworld.dialogs.QuoteImageDialog;
import com.walid.knowledgeworld.favorites.FavoriteItem;
import com.walid.knowledgeworld.favorites.FavoritesManager;
import com.walid.knowledgeworld.dialogs.QuoteImageDialog;
import com.walid.knowledgeworld.favorites.FavoritesManager;

import java.util.List;
import java.util.Random;

public class Adaptersada extends RecyclerView.Adapter<Adaptersada.Holder> {

    private final List<new_item_post> list2;
    private final Context context;
    private final Random random = new Random();

    public Adaptersada(List<new_item_post> list2, Context context) {
        this.list2 = list2;
        this.context = context;
    }

    public List<new_item_post> getItems() {
        return list2;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReslatfoalBinding binding = ItemReslatfoalBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        new_item_post item = list2.get(position);
        ItemReslatfoalBinding binding = holder.binding;

        if (item.getTitle() != null) {
            binding.title.setText(item.getTitle().replace("_", "\n"));
        } else {
            binding.title.setText("");
        }

        binding.messageNumber.setText(String.valueOf(item.getId()));

        if (item.getCreatedAt() != null && !item.getCreatedAt().isEmpty()) {
            binding.messageTime.setText(item.getCreatedAt().split(" ")[0]);
        } else {
            binding.messageTime.setText("2021-01-05");
        }

        binding.sharequran.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, " تطبيق (عالم المعرفة) ");
            sendIntent.putExtra(Intent.EXTRA_TEXT, item.getTitle() + "\n" +
                    "تم النسخ من تطبيق عالم المعرفة 💙 👇 https://t.co/nEevBOppAH \n");
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent, "مشاركه نصوص من تطبيق عالم المعرفة :"));
        });

        binding.starquran.setOnClickListener(v -> {
            String quoteId = "quote_" + item.getId() + "_" + (item.getTitle() != null ? item.getTitle().hashCode() : 0);
            FavoriteItem favItem = new FavoriteItem(
                    quoteId,
                    FavoriteItem.TYPE_QUOTE,
                    item.getTitle(),
                    item.getCreatedAt(),
                    null,
                    System.currentTimeMillis()
            );
            boolean isFav = FavoritesManager.getInstance(context).toggleFavorite(favItem);
            if (isFav) {
                Toast.makeText(context, "تمت إضافة الاقتباس إلى المفضلة ⭐️", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "تمت إزالة الاقتباس من المفضلة", Toast.LENGTH_SHORT).show();
            }
        });

        if (binding.btnImageQuote != null) {
            binding.btnImageQuote.setOnClickListener(v -> {
                QuoteImageDialog dialog =
                        new QuoteImageDialog(context, item.getTitle());
                dialog.show();
            });
        }

        binding.qumsgwhats.setOnClickListener(v -> {
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, binding.title.getText().toString());
            try {
                context.startActivity(whatsappIntent);
            } catch (ActivityNotFoundException ex) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
            }
        });

        binding.copyquran.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("quote", item.getTitle() + "\n\nتم نسخ هذا النص من تطبيق عالم المعرفة ^_^");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "تم نسخ النص - قم بمشاركتة الآن مع أصدقائك", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(v -> {
            try {
                MediaPlayer mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.clikkk);
                if (mediaPlayer != null) {
                    mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                    mediaPlayer.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Resources res = context.getResources();
            TypedArray myImages = res.obtainTypedArray(R.array.image);
            try {
                if (myImages.length() > 0) {
                    int randomInt = random.nextInt(myImages.length());
                    int drawableID = myImages.getResourceId(randomInt, -1);
                    if (drawableID != -1) {
                        binding.baccc.setBackgroundResource(drawableID);
                        binding.title.setTextColor(Color.WHITE);
                    }
                }
            } finally {
                myImages.recycle();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list2 != null ? list2.size() : 0;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        final ItemReslatfoalBinding binding;

        public Holder(@NonNull ItemReslatfoalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
