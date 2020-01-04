package com.f10company.barcodewidgetf10;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class CreateCodeImage {
    public Bitmap createBitMatrix(String text, String format, Display display) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        // 디스플레이의 길이를 알기 위한 방법.

        try {
            BarcodeFormat f = BarcodeFormat.valueOf(format);//format과 같은 상수형을 대입
            BitMatrix bitMatrix;
            bitMatrix = multiFormatWriter.encode(text, f, width, 450);
            if (format.equals("QR_CODE")) {
                bitMatrix = multiFormatWriter.encode(text, f, 500 , 500);
            }
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            return bitmap;
        } catch (Exception e) {
        }

        return null;
    }
}
