package com.f10company.barcodewidgetf10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.relex.circleindicator.CircleIndicator;

public class ExplainActivity extends AppCompatActivity {
    private ViewPager viewPager ;
    private ExplainViewPagerAdapter pagerAdapter ;
    private Button exit;
    CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);

        exit = (Button)findViewById(R.id.exit);
        viewPager = (ViewPager) findViewById(R.id.viewPagerForExplain) ;
        indicator = (CircleIndicator)findViewById(R.id.indicatorInExplain);



        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        pagerAdapter = new ExplainViewPagerAdapter(this) ;

        viewPager.setAdapter(pagerAdapter) ;

        pagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
        indicator.setViewPager(viewPager);

    }
}
