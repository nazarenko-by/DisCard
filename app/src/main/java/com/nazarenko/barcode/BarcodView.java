package com.nazarenko.barcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class BarcodView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcod_view);
        Intent intent = getIntent();
        int type = intent.getIntExtra("TYPE",13);
        String img = intent.getStringExtra("IMAGE");
        ImageView barcodeView = findViewById(R.id.barcodeFragmentImage);
        Bitmap bitmap;
        AsyncBarcodeGenerator asyncQRGenerator = new AsyncBarcodeGenerator();
        asyncQRGenerator.setType(type);
        bitmap = asyncQRGenerator.createBarcode(img);
        barcodeView.setImageBitmap(bitmap);
    }
}
