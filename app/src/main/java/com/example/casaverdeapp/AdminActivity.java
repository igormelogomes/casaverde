package com.example.casaverdeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AdminActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST_PROMOTION = 1;
    private static final int PICK_IMAGE_REQUEST_APRENDA = 2;

    private Button buttonBack;
    private Button buttonChangePhotoPromotion;
    private Button buttonChangeTextPromotion;
    private Button buttonRemovePhotoPromotion;
    private Button buttonRemoveTextPromotion;
    private EditText editTextNewText;
    private ImageView imageViewCurrentPhoto;

    private Button buttonChangePhotoAprenda;
    private Button buttonChangeTextAprenda;
    private Button buttonRemovePhotoAprenda;
    private Button buttonRemoveTextAprenda;
    private EditText editTextAprenda;
    private ImageView imageViewAprenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Inicializar elementos de Promoção
        buttonBack = findViewById(R.id.buttonBack);
        buttonChangePhotoPromotion = findViewById(R.id.buttonChangePhotoPromotion);
        buttonChangeTextPromotion = findViewById(R.id.buttonChangeTextPromotion);
        buttonRemovePhotoPromotion = findViewById(R.id.buttonRemovePhotoPromotion);
        buttonRemoveTextPromotion = findViewById(R.id.buttonRemoveTextPromotion);
        editTextNewText = findViewById(R.id.editTextNewText);
        imageViewCurrentPhoto = findViewById(R.id.imageViewCurrentPhoto);

        // Inicializar elementos de Aprenda
        buttonChangePhotoAprenda = findViewById(R.id.buttonChangePhotoAprenda);
        buttonChangeTextAprenda = findViewById(R.id.buttonChangeTextAprenda);
        buttonRemovePhotoAprenda = findViewById(R.id.buttonRemovePhotoAprenda);
        buttonRemoveTextAprenda = findViewById(R.id.buttonRemoveTextAprenda);
        editTextAprenda = findViewById(R.id.editTextAprenda);
        imageViewAprenda = findViewById(R.id.imageViewAprenda);

        buttonBack.setOnClickListener(v -> finish());

        // Ações de Promoção
        buttonChangePhotoPromotion.setOnClickListener(v -> openImageChooser(PICK_IMAGE_REQUEST_PROMOTION));
        buttonChangeTextPromotion.setOnClickListener(v -> saveText("promotion_text", editTextNewText.getText().toString()));
        buttonRemovePhotoPromotion.setOnClickListener(v -> removePhoto("promotion_image_path"));
        buttonRemoveTextPromotion.setOnClickListener(v -> removeText("promotion_text"));

        // Ações de Aprenda
        buttonChangePhotoAprenda.setOnClickListener(v -> openImageChooser(PICK_IMAGE_REQUEST_APRENDA));
        buttonChangeTextAprenda.setOnClickListener(v -> saveText("aprenda_text", editTextAprenda.getText().toString()));
        buttonRemovePhotoAprenda.setOnClickListener(v -> removePhoto("aprenda_image_path"));
        buttonRemoveTextAprenda.setOnClickListener(v -> removeText("aprenda_text"));

        // Carregar os dados existentes
        loadExistingData();
    }

    private void openImageChooser(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                String imagePath = saveImage(bitmap);
                SharedPreferences sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (requestCode == PICK_IMAGE_REQUEST_PROMOTION) {
                    editor.putString("promotion_image_path", imagePath);
                } else if (requestCode == PICK_IMAGE_REQUEST_APRENDA) {
                    editor.putString("aprenda_image_path", imagePath);
                }
                editor.apply();
                Toast.makeText(this, "Imagem selecionada", Toast.LENGTH_SHORT).show();
                loadImage(imagePath, requestCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String saveImage(Bitmap bitmap) throws IOException {
        File outputDir = getCacheDir();
        File imageFile = File.createTempFile("image_", ".jpg", outputDir);
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        }
        return imageFile.getAbsolutePath();
    }

    private void saveText(String key, String text) {
        SharedPreferences sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, text);
        editor.apply();
        Toast.makeText(this, "Texto salvo", Toast.LENGTH_SHORT).show();
    }

    private void removePhoto(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
        if (key.equals("promotion_image_path")) {
            imageViewCurrentPhoto.setImageBitmap(null);
        } else if (key.equals("aprenda_image_path")) {
            imageViewAprenda.setImageBitmap(null);
        }
    }

    private void removeText(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
        if (key.equals("promotion_text")) {
            editTextNewText.setText("");
        } else if (key.equals("aprenda_text")) {
            editTextAprenda.setText("");
        }
    }

    private void loadExistingData() {
        SharedPreferences sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE);

        // Carregar dados de promoção
        String promotionText = sharedPreferences.getString("promotion_text", "");
        editTextNewText.setText(promotionText);
        String promotionImagePath = sharedPreferences.getString("promotion_image_path", "");
        loadImage(promotionImagePath, PICK_IMAGE_REQUEST_PROMOTION);

        // Carregar dados de Aprenda
        String aprendaText = sharedPreferences.getString("aprenda_text", "");
        editTextAprenda.setText(aprendaText);
        String aprendaImagePath = sharedPreferences.getString("aprenda_image_path", "");
        loadImage(aprendaImagePath, PICK_IMAGE_REQUEST_APRENDA);
    }

    private void loadImage(String imagePath, int requestCode) {
        if (!imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                if (requestCode == PICK_IMAGE_REQUEST_PROMOTION) {
                    imageViewCurrentPhoto.setImageBitmap(myBitmap);
                } else if (requestCode == PICK_IMAGE_REQUEST_APRENDA) {
                    imageViewAprenda.setImageBitmap(myBitmap);
                }
            }
        }
    }
}
