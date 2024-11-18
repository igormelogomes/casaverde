package com.example.casaverdeapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.io.File;

public class Aprenda extends AppCompatActivity {

    private ImageView imageViewAprenda;
    private EditText editTextAprenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprenda);

        // Inicializa o Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Esconde o título padrão
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Configura o título personalizado
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Você Sabia?"); // Define o título aqui

        imageViewAprenda = findViewById(R.id.imageViewAprenda);
        editTextAprenda = findViewById(R.id.editTextAprenda);

        loadAprendaData();
    }

    private void loadAprendaData() {
        SharedPreferences sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);

        // Carregar texto de Aprenda
        String aprendaText = sharedPreferences.getString("aprenda_text", "Texto padrão de Aprenda");
        editTextAprenda.setText(aprendaText);

        // Carregar a imagem de Aprenda
        String imagePath = sharedPreferences.getString("aprenda_image_path", "");
        if (!imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageViewAprenda.setImageBitmap(myBitmap);
            } else {
                imageViewAprenda.setImageBitmap(null); // Se a imagem não for encontrada
            }
        } else {
            imageViewAprenda.setImageBitmap(null); // Se não houver imagem
        }
    }
}
