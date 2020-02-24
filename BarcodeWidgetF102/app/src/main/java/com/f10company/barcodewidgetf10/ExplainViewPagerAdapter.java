package com.f10company.barcodewidgetf10;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ExplainViewPagerAdapter  extends PagerAdapter {

    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    static private Context mContext = null ;


    //설명창 이미지//
    static int arr[] ={
            R.drawable.mainexplain1,
            R.drawable.mainexplain2,
    R.drawable.explain1,
    R.drawable.explain2,
    R.drawable.explain3};

    public ExplainViewPagerAdapter() {

    }

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public ExplainViewPagerAdapter(Context context) {
        mContext = context ;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;

        if (mContext != null) {
            // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.explain_page, container, false);

            ImageView imageView = (ImageView)view.findViewById(R.id.explainImage);
            imageView.setImageResource(arr[position]);
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
    public int getCount() {
        // 전체 페이지 수는 10개로 고정.
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
}