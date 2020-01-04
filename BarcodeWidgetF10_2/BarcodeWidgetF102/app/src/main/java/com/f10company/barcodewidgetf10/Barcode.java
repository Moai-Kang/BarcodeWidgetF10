package com.f10company.barcodewidgetf10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class Barcode extends AppCompatActivity {
//다시확인해야함
    String Barcode;
    int width=0;
    int height=0;
    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    Bitmap bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        Intent intent = getIntent();
        Barcode =intent.getStringExtra("Code");
        width = intent.getIntExtra("width", 0);
        height = intent.getIntExtra("height", 0);

    }
    public Bitmap editBitMatrix(String text, String format) {
        Bitmap EditBarcode = createBarcode(Barcode, width, height);
        return null;
    }
    public Bitmap createBarcode(String code, int x, int y) {
        try {
            //BarcodeFormat f = BarcodeFormat.valueOf(BarcodeFormat.CODE_128);//format과 같은 상수형을 대입
            BitMatrix bitMatrix;
            bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.CODE_128, width, 400);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            Intent intent = new Intent(Barcode.this, MainActivity.class);
            intent.putExtra("codeString", code);
            intent.putExtra("codeFormat", BarcodeFormat.CODE_128);
            intent.putExtra("codeNickname", "바코드 별명");
            setResult(Activity.RESULT_OK, intent);
            finish();
            return bitmap;
        } catch (Exception e) {
        }
        return bitmap;
    }
}
