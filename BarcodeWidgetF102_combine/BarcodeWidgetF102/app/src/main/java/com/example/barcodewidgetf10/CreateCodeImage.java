package com.example.barcodewidgetf10;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class CreateCodeImage {
    public Bitmap createBitMatrix(String text, String format, Display display) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        //////////////////////////////////


        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        //////////////////////////디스플레이의 길이를 알기 위한 방법.

        try{
            BarcodeFormat f = BarcodeFormat.valueOf(format);//format과 같은 상수형을 대입
            BitMatrix bitMatrix = multiFormatWriter.encode(text,f, width,400);
            //BitMatrix sub_bitMatrix=multiFormatWriter.encode(text, f,width,300); //@@@@@@@@@
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            //Bitmap sub_bitmap=barcodeEncoder.createBitmap(sub_bitMatrix);//notification 커스텀 전용 바코드//@@@@@@@@@@@@@@@@@@@@@
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            //iv.setImageBitmap(bitmap);
            //codeImage = bitmap;
            //sub_codeImage=sub_bitmap;

            return bitmap;
        }catch (Exception e){}

        return null;
    }
}
