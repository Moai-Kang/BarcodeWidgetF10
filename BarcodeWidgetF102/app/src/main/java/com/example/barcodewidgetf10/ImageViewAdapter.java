package com.example.barcodewidgetf10;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class ImageViewAdapter extends PagerAdapter {

    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    private Context mContext = null ;
    public ImageView img;

    ArrayList<String> codeString;
    ArrayList<String> codeFormat;
    Display display;

    public void getData(ArrayList<String> arr1, ArrayList<String> arr2, Display _display)
    {
        codeString = arr1;
        codeFormat=arr2;
        display=_display;
    }

    public ImageViewAdapter() {

    }

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public  ImageViewAdapter(Context context) {
        mContext = context ;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;

        if (mContext != null) {
            // LayoutInflater를 통해 "/res/layout/pages.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.pages, container, false);


            //////////////////////////////////////////////
            CreateCodeImage cci = new CreateCodeImage();
            img = view.findViewById(R.id.codeImage);
            if(codeString.isEmpty()) {img.setImageResource(R.drawable.plzsetqrcode);}
            else{ img.setImageBitmap(cci.createBitMatrix(codeString.get(position),codeFormat.get(position),display));}
        }


        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() { // 이 부분 수정 필요, 지금은 10개로 고정했지만, 나중에는 저장된 바코드 개수만큼 return 해줘야 함.
        // 전체 페이지 수는 10개로 고정.
        if(codeString.isEmpty())
            return 1;
        return codeString.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
}
