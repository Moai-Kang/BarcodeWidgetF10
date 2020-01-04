package com.f10company.barcodewidgetf10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        IntentIntegrator intentIntegrator =
                new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);//QR코드/바코드를 스캔한 결과 값을 가져옴
        if (result != null) {
            //스캔을 하지 못하면
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();
            } else if (result.getFormatName().equals("QR_CODE")) {
                Toast.makeText(this, "QR코드 서비스 준비중입니다.", Toast.LENGTH_LONG).show();
                finish();
            }
            //스캔을 완료하면
            else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ScanQR.this, MainActivity.class);
                intent.putExtra("codeString", result.getContents());
                intent.putExtra("codeFormat", result.getFormatName());
                intent.putExtra("codeNickname", "바코드 별명");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);//데이터값만 넘겨주고 다시 현재 액티비티로 돌아옴.
        }
    }
}
