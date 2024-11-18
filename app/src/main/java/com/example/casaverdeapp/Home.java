package com.example.casaverdeapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.casaverdeapp.databinding.ActivityHomeBinding;

public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @SuppressLint({"NonConstantResourceId", "QueryPermissionsNeeded"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.more.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(Home.this, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_main, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.sobre) {
                    Intent intent = new Intent(Home.this, Sobre.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            });
            popup.show();
        });

        binding.local.setOnClickListener(view -> {
            Uri locationUri = Uri.parse("https://maps.app.goo.gl/EFczQGkdxDbMJXyWA");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, locationUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        });

        binding.admin.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, LoginActivity.class);
            startActivity(intent);
        });

        binding.share.setOnClickListener(view -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Aqui está o conteúdo que quero compartilhar.");
            startActivity(Intent.createChooser(shareIntent, "Compartilhar via"));
        });

        binding.image1.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.image2.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Promocao.class);
            startActivity(intent);
        });

        binding.image3.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Aprenda.class);
            startActivity(intent);
        });
    }
}
