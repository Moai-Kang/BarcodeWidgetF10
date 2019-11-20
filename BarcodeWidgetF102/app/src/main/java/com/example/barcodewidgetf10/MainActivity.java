package com.example.barcodewidgetf10;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //토글 버튼 관련 변수

    SharedPreferences.Editor prefEditor;
    SharedPreferences prefs;
    Bitmap codeImage;
    private Button scanQRBtn;
    private TextView tv;

    static ArrayList<String> codeString;
    static ArrayList<String> codeFormat;

    //private ViewPager viewPager ;
    private ImageViewAdapter pagerAdapter ;
    ArrayListSaveByJson temp = new ArrayListSaveByJson();
    static Display display;

    ListView layout_below;
    ArrayList<String> menu;

    ForViewPagerSize vp;

    ArrayList<Integer> saveDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanQRBtn  = (Button)findViewById(R.id.QRBarcodeBtn);
        tv = (TextView)findViewById(R.id.textview);
        display = getWindowManager().getDefaultDisplay();
        //
        // iv = (ImageView)findViewById(R.id.codeImage);
        //////////////////////////////////////////////////////////////

        codeString = temp.getStringArrayPref(MainActivity.this,"codeString");
        codeFormat = temp.getStringArrayPref(MainActivity.this,"codeFormat");

        if(codeString.isEmpty()) {
            tv.setText("바코드/QR코드를 스캔하세요.");
        }
        else {
            String tempString="";
            for(int i=0; i<codeString.size(); i++) {
                tempString = tempString + " \n " + codeString.get(i);
            }
            tv.setText(tempString);
        }

        addAlarmBarListSetting();
        setViewPager();

        scanQRBtn.setOnClickListener(new View.OnClickListener() { // 버튼을 클릭했을 시에 수행할 내용
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanQR.class);
                startActivityForResult(intent, 1); //인텐트 다녀 온 후 onActivityResult 호출
            }
        });
    }

    public  void setViewPager() {
        vp = (ForViewPagerSize)findViewById(R.id.vp);
        //viewPager = (ViewPager) findViewById(R.id.viewPager) ;
        pagerAdapter = new ImageViewAdapter(this) ;
        pagerAdapter.getData(codeString,codeFormat,display);////////54352

        //viewPager.setAdapter(pagerAdapter) ;
        vp.setAdapter(pagerAdapter);
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
        if(requestCode==1) {
            if (resultCode == Activity.RESULT_OK) {

                codeString.add(data.getExtras().getString("codeString"));
                codeFormat.add(data.getExtras().getString("codeFormat"));
                String text = codeString.get(codeString.size()-1); // 사용자가 입력한 저장할 데이터
                String format = codeFormat.get(codeFormat.size()-1); // 스캔한 코드의 형식

                temp.setStringArrayPref(MainActivity.this,"codeString",codeString);
                temp.setStringArrayPref(MainActivity.this,"codeFormat",codeFormat);

                tv.setText(text);
                setViewPager();
            }
        }
    }

     void notification(int count) {
         NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if(count == -1) {
            notificationManager.cancel(1);//취소하는 경우
            return;
        }
        CreateCodeImage cci = new CreateCodeImage();
        RemoteViews customView = new RemoteViews(getPackageName(), R.layout.custom_view);//커스텀
        Bitmap sub_codeImage = cci.createBitMatrix(MainActivity.codeString.get(count),MainActivity.codeFormat.get(count),MainActivity.display);
        customView.setImageViewBitmap(R.id.content_view,sub_codeImage);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default").setOngoing(true);
        //노티피케이션의 객체 선언부분이라고 보면됨
        // setOngoing을 하면 고정
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setCustomContentView(customView);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);//이게 위에 setOngoing때문에 작동을 안함.
        // 알림 표시

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        notificationManager.notify(1, builder.build());
    }

    public void addAlarmBarListSetting()
    {
        layout_below = (ListView)findViewById(R.id.checkNoticeList);
        saveDialog = new ArrayList<>();

        menu = new ArrayList<>();
        menu.add("알림창 추가");

        ArrayAdapter<String> menuAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, menu);
        layout_below.setAdapter(menuAdapter);

        layout_below.setOnItemClickListener(new AdapterView.OnItemClickListener() { //리스트 뷰의 각 아이템을 클릭 했을 때
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showBarcodeList();
            }
        });
    }

    void showBarcodeList() {

        final List<String> choosebarcodeList = new ArrayList<>();
        choosebarcodeList.add("바코드를 고정하지 않음");

        for(int i = 0 ; i< codeString.size() ; i++) {
            choosebarcodeList.add(codeString.get(i));
        }

        final CharSequence[] items = choosebarcodeList.toArray(new String[choosebarcodeList.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setItems(items, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               if(which == 0) {
                  notification(which - 1);
               }
               else{
                   notification(which - 1);
               }
           }
       });
        builder.show();
    }
}
