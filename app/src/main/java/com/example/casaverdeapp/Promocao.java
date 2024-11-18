package com.example.casaverdeapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.io.File;

public class Promocao extends AppCompatActivity {

    private TextView textViewPromotion;
    private ImageView imageViewPromotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocao);

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
        toolbarTitle.setText("Promoções Especiais"); // Define o título aqui

        textViewPromotion = findViewById(R.id.textViewPromotion);
        imageViewPromotion = findViewById(R.id.imageViewPromotion);

        loadPromotionData();
    }

    private void loadPromotionData() {
        SharedPreferences sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);

        // Carregar texto da promoção
        String promotionText = sharedPreferences.getString("promotion_text", "Texto padrão da promoção");
        textViewPromotion.setText(promotionText);

        // Carregar imagem da promoção
        String imagePath = sharedPreferences.getString("promotion_image_path", "");
        if (!imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageViewPromotion.setImageBitmap(myBitmap);
            } else {
                imageViewPromotion.setImageBitmap(null); // Se a imagem não for encontrada
            }
        } else {
            imageViewPromotion.setImageBitmap(null); // Se não houver imagem
        }
    }
}
