package com.example.casaverdeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private EditText editTextFullName, editTextEmail, editTextPhoneNumber, editTextCPF;
    private RadioGroup radioGroupSex;
    private RadioButton radioMale, radioFemale;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        editTextFullName = findViewById(R.id.editTextFullName);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextCPF = findViewById(R.id.editTextCPF);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(v -> {
            String fullName = editTextFullName.getText().toString();
            String email = editTextEmail.getText().toString();
            String phoneNumber = editTextPhoneNumber.getText().toString();
            String cpf = editTextCPF.getText().toString();
            String sex = radioMale.isChecked() ? "Masculino" : "Feminino";

            if (fullName.isEmpty() || email.isEmpty() || cpf.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, Object> user = new HashMap<>();
                user.put("nome_completo", fullName);
                user.put("sexo", sex);
                user.put("email", email);
                user.put("numero_celular", phoneNumber);
                user.put("cpf", cpf);

                db.collection("usuarios")
                        .add(user)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(RegisterActivity.this, "Parabéns! Você está participando do Programa Fidelidade", Toast.LENGTH_SHORT).show();
                            // Gerar o cartão de fidelidade e QR code aqui
                            gerarCartaoFidelidade(fullName, documentReference.getId());
                        })
                        .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Erro ao cadastrar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void gerarCartaoFidelidade(String fullName, String userId) {
        // Dados que serão codificados no QR code
        String qrData = "https://seuappfirebase.com/user/" + userId;

        // Cria um objeto MultiFormatWriter, que é responsável por codificar os dados no formato QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            // Gera a matriz de bits do QR code com os dados fornecidos
            BitMatrix bitMatrix = multiFormatWriter.encode(qrData, BarcodeFormat.QR_CODE, 200, 200);

            // Cria um objeto BarcodeEncoder, que é responsável por converter a matriz de bits em um bitmap
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            // Exibe o QR code em uma nova atividade ou fragmento
            Intent intent = new Intent(RegisterActivity.this, FidelityCardActivity.class);
            intent.putExtra("USER_NAME", fullName);
            intent.putExtra("QR_CODE", bitmap);
            startActivity(intent);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
