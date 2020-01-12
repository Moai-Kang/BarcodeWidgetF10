package com.f10company.barcodewidgetf10;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.List;

import static com.google.zxing.integration.android.IntentIntegrator.CODE_128;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences prefs;
    private ImageButton scanQRBtn;
    TextView tv;

    static ArrayList<String> codeString;
    static ArrayList<String> codeFormat;
    static ArrayList<String> codeFormatWithoutQRCode;
    static ArrayList<String> codeStringWithoutQRCode;
    static ArrayList<String> codeeNickname;

    private ImageViewAdapter pagerAdapter;
    static ArrayListSaveByJson temp = new ArrayListSaveByJson();
    static Display display;

    ListView layout_below;
    ArrayList<String> menu;
    ArrayList<Integer> saveDialog;
    boolean nowFirstWindow = true;

    TextView license;
    static Context ctx;
    //EditText barcode;//바코드 직접 입력받기
    ImageView emptyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanQRBtn = (ImageButton) findViewById(R.id.QRBarcodeBtn);
        tv = (TextView) findViewById(R.id.nickname);
        emptyImage = (ImageView) findViewById(R.id.emptyImage);
        license = (TextView) findViewById(R.id.openSource);

        ctx = getBaseContext();
        display = getWindowManager().getDefaultDisplay();

        codeString = temp.getStringArrayPref(MainActivity.this, "codeString");
        codeFormat = temp.getStringArrayPref(MainActivity.this, "codeFormat");
        codeeNickname = temp.getStringArrayPref(MainActivity.this, "codeNickname");
        //barcode=(EditText)findViewById(R.id.barcode_edit);//바코드 직접입력
        final EditText barcode = new EditText(MainActivity.this);
        addAlarmBarListSetting();
        setViewPager();

        scanQRBtn.setOnClickListener(new View.OnClickListener() { // 버튼을 클릭했을 시에 수행할 내용, +버튼 눌렀을때의 동작
            @Override
            public void onClick(View v) {
                final String[] items = {"카메라 인식", "직접입력","사진 가져오기"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("설정");//여기서부터 다시
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(MainActivity.this, ScanQR.class);
                            startActivityForResult(intent, 1); //인텐트 다녀 온 후 onActivityResult 호출
                        }
                        else if (which == 1) {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                            builder2.setTitle("바코드를 입력해주세요.");
                            builder2.setView(barcode);
                            builder2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Point size = new Point();
                                    MainActivity.display.getSize(size);
                                    int width = size.x;
                                    int height = size.y;
                                    Log.d("확인","입력한 바코드는:"+barcode.getText().toString());
                                    codeString.add(barcode.getText().toString());
                                    codeFormat.add(CODE_128);
                                    codeeNickname.add("바코드별명");
                                    save();
                                    setViewPager();
                                    CreateCodeImage edit_bar=new CreateCodeImage();
                                    edit_bar.createBitMatrix(barcode.getText().toString(),CODE_128,MainActivity.display);
                                }
                            });
                            AlertDialog alertDialog = builder2.create();
                            alertDialog.show();
                        }
                        else if(which==2){
                            //사진가져오기 넣을 예정
                            Intent intent = new Intent(MainActivity.this, ReadGalleryCodeActivity.class);
                            startActivityForResult(intent, 1); //인텐트 다녀 온 후 onActivityResult 호출
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        license.setOnClickListener(this);
    }

    //This is the test versinontlqkf

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (nowFirstWindow) {
            //nowFirstWindow = false;
            save();
            Log.d("asdf", "onWindowForcused");

            View skyContent = (View) findViewById(R.id.skyContent);
            LinearLayout.LayoutParams lp_but = (LinearLayout.LayoutParams) skyContent.getLayoutParams();

            lp_but.setMargins(0, (int) (skyContent.getHeight() / 100 * 20), 0, (int) (skyContent.getHeight() / 100 * 5.5));

            skyContent.setLayoutParams(lp_but);

            skyContent = null;
            lp_but = null;

        }
    }

    public void setViewPager() {
        if (!codeString.isEmpty()) {
            emptyImage.setVisibility(View.GONE);
            ForViewPagerSize vp;
            vp = (ForViewPagerSize) findViewById(R.id.vp);

            pagerAdapter = new ImageViewAdapter(this);
            pagerAdapter.getData(codeString, codeFormat, codeeNickname, display);////////54352
            pagerAdapter.setContext(MainActivity.this);

            int dpValue = 54;
            float d = getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d);

            vp.setClipToPadding(false);
            vp.setPadding(margin, 0, margin, 0);
            vp.setPageMargin(margin / 2);

            emptyImage.setPadding(margin, 0, margin, 0);

            vp.setAdapter(pagerAdapter);
        }
        Log.d("adad", "adad");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //requestCode : 어느 인텐트를 다녀왔는지 확인하는 것, resultCOde: 다녀온인텐트가 결과를 알려주는 것,
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                codeString.add(data.getExtras().getString("codeString"));
                codeFormat.add(data.getExtras().getString("codeFormat"));
                codeeNickname.add(data.getExtras().getString("codeNickname"));

                save();

                setViewPager();
            }
        }
    }

    public void save() {
        temp.setStringArrayPref(MainActivity.this, "codeString", codeString);
        temp.setStringArrayPref(MainActivity.this, "codeFormat", codeFormat);
        temp.setStringArrayPref(MainActivity.this, "codeNickname", codeeNickname);
    }

    public void addAlarmBarListSetting() {
        layout_below = (ListView) findViewById(R.id.checkNoticeList);
        saveDialog = new ArrayList<>();

        menu = new ArrayList<>();
        menu.add("알림창 설정");

        ArrayAdapter<String> menuAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_activated_1, menu);
        layout_below.setAdapter(menuAdapter);

        layout_below.setOnItemClickListener(new AdapterView.OnItemClickListener() { //리스트 뷰의 각 아이템을 클릭 했을 때
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showBarcodeList();
            }
        });
    }

    void notification(int count) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (count == -1) {
            notificationManager.cancel(1);//취소하는 경우
            return;
        }
        CreateCodeImage cci = new CreateCodeImage();
        RemoteViews customView = new RemoteViews(getPackageName(), R.layout.custom_view);//커스텀
        Bitmap sub_codeImage = cci.createBitMatrix(MainActivity.codeStringWithoutQRCode.get(count),
                MainActivity.codeFormatWithoutQRCode.get(count), MainActivity.display); // 이 줄 수정
        customView.setImageViewBitmap(R.id.content_view, sub_codeImage);
        Intent Pintent = new Intent(this, MainActivity.class);//Pending Intent에 적용될 클래스
        PendingIntent notiIntent = PendingIntent.getActivity(this, 0, Pintent, PendingIntent.FLAG_UPDATE_CURRENT);//노티피케이션 클릭시 홈화면으로 이동
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default").setOngoing(true);
        //노티피케이션의 객체 선언부분이라고 보면됨
        // setOngoing을 하면 고정
        builder.setSmallIcon(R.drawable.noti_icon);
        builder.setSubText("테스트입니다");
        builder.setCustomContentView(customView);
        builder.setContentIntent(notiIntent);//Pending 인텐트를 실행
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        // 사용자가 탭을 클릭하면 자동 제거
        // builder.setAutoCancel(true);//이게 위에 setOngoing때문에 작동을 안함.
        // 알림 표시

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        notificationManager.notify(1, builder.build());
    }

    void showBarcodeList() {

        final List<String> choosebarcodeList = new ArrayList<>();
        choosebarcodeList.add("바코드를 고정하지 않음");

        codeStringWithoutQRCode = new ArrayList<>();
        codeFormatWithoutQRCode = new ArrayList<>();

        Log.d("asdf", "1");
        for (int i = 0; i < codeString.size(); i++) {
            if (!codeFormat.get(i).equals("QR_CODE")) { //qr 코드가 아닐때만
                codeStringWithoutQRCode.add(codeString.get(i));
                codeFormatWithoutQRCode.add(codeFormat.get(i));
            }
        }

        for (int i = 0; i < codeStringWithoutQRCode.size(); i++) {
            //choosebarcodeList.add(codeStringWithoutQRCode.get(i));
            choosebarcodeList.add(codeeNickname.get(i));
        }

        final CharSequence[] items = choosebarcodeList.toArray(new String[choosebarcodeList.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        int color = getResources().getColor(R.color.colorMainSkyBlue);
        TextView title = new TextView(this);
        // You Can Customise your Title here
        title.setText("바코드 선택");
        title.setBackgroundColor(color);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        builder.setCustomTitle(title);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    notification(which - 1);
                } else {
                    notification(which - 1);
                }
            }
        });
        builder.show();
    }

    public void onClick(View v) {
        startActivity(new Intent(this, OpenSource.class));
    }
}