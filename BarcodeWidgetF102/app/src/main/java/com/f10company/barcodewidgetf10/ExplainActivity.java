package com.f10company.barcodewidgetf10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class ExplainActivity extends AppCompatActivity {
    private ViewPager viewPager ;
    private ExplainViewPagerAdapter pagerAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);

        viewPager = (ViewPager) findViewById(R.id.viewPagerForExplain) ;
        pagerAdapter = new ExplainViewPagerAdapter(this) ;
        viewPager.setAdapter(pagerAdapter) ;
    }
}
