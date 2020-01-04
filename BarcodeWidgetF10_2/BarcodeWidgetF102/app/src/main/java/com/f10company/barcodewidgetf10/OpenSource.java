package com.f10company.barcodewidgetf10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OpenSource extends AppCompatActivity {

    private WebView web;
    private WebSettings websetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source);

        web=(WebView)findViewById(R.id.webView);
        web.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        websetting = web.getSettings(); //세부 세팅 등록
        websetting.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        websetting.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        websetting.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        websetting.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        websetting.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        websetting.setSupportZoom(true); // 화면 줌 허용 여부
        websetting.setBuiltInZoomControls(true); // 화면 확대 축소 허용 여부
        websetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        websetting.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        websetting.setDomStorageEnabled(true); // 로컬저장소 허용 여부

        web.loadUrl("file:///android_asset/opensource.html");
    }
}