package com.example.casaverdeapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FidelityCardActivity extends AppCompatActivity {

    private TextView textViewUserName;
    private ImageView imageViewQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fidelity_card);

        textViewUserName = findViewById(R.id.textViewUserName);
        imageViewQRCode = findViewById(R.id.imageViewQRCode);

        // Recebe os dados da Intent
        String userName = getIntent().getStringExtra("USER_NAME");
        Parcelable qrCodeParcelable = getIntent().getParcelableExtra("QR_CODE");
        Bitmap qrCode = (Bitmap) qrCodeParcelable;

        // Exibe os dados recebidos
        textViewUserName.setText(userName);
        imageViewQRCode.setImageBitmap(qrCode);
    }
}
