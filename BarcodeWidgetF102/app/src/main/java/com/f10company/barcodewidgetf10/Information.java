package com.f10company.barcodewidgetf10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class Information extends AppCompatActivity {
    private ArrayList<HashMap<String,String>> array = new ArrayList<HashMap<String,String>>();
    HashMap<String,String> data;
    ListView listView;
    Button but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        but = ((Button)findViewById(R.id.exit_information));
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        data = new HashMap<String,String>();
        data.put("title","상호");
        data.put("context","F10");
        array.add(data);

        data = new HashMap<String,String>();
        data.put("title","대표");
        data.put("context","박승한");
        array.add(data);

        data = new HashMap<String,String>();
        data.put("title","주소");
        data.put("context","서울시 광진구 능동로 209 세종대학교 (군자관 102호)");
        array.add(data);

        data = new HashMap<String,String>();
        data.put("title","문의사항");
        data.put("context","f10companyofficial@gmail.com");
        array.add(data);

        data = new HashMap<String,String>();
        data.put("title","사업자등록번호");
        data.put("context","104-26-11788");
        array.add(data);

        listView = (ListView)findViewById(R.id.listViewInformation);


        SimpleAdapter adapter = new SimpleAdapter(
                Information.this,
                array,
                android.R.layout.simple_list_item_2,
                new String[]{"title","context"},
                new int[]{android.R.id.text1,android.R.id.text2}
                );
        listView.setAdapter(adapter);

    }
}
