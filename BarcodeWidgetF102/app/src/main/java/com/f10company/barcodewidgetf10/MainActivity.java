package com.f10company.barcodewidgetf10;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static com.google.zxing.integration.android.IntentIntegrator.CODE_128;
import static com.google.zxing.integration.android.IntentIntegrator.QR_CODE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton questionButton, exclamationButton;
    private Button addCameraButton, addAlbumButton, addSelfButton, settingButton;

    static ArrayList<String> codeString;
    static ArrayList<String> codeFormat;
    static ArrayList<String> codeFormatWithoutQRCode;
    static ArrayList<String> codeStringWithoutQRCode;
    static ArrayList<String> codeeNickname;

    final static String SHARED_PREF_NOW_POSITION_KEY = "key";
    final static String SHARED_PREF_CODE_STRING = "codeString";
    final static String SHARED_PREF_CODE_FORMAT = "codeFormat";
    final static String SHARED_PREF_CODE_NICKNAME = "codeNickname";

    final static String DEFAULT_CODE_QR_NICK = "새 QR코드";
    final static String DEFAULT_CODE_BAR_NICK = "새 바코드";

    final static String NOTI_STRING = "NULL";

    final static int CODE_IMG_PIX = 15;

    static NotificationManager notificationManager;
    static NotificationCompat.Builder builder;

    private ImageViewAdapter pagerAdapter;
    static ArrayListSaveByJson temp = new ArrayListSaveByJson();
    static Display display;

    EditText barcode;//selfInsert용

    static Context ctx;
    ImageView emptyImage;
    ForViewPagerSize vp;
    CircleIndicator indicator;

    static int nowPosition = 0; // 현재 디스플레이에 띄어저 있는 장소
    static String nowNotificationCodePosition = NOTI_STRING; // 노티피케이션에 띄어져 있는 코드 위치//코드지울때 노티가 해당 코드이면 같이 없어질때를 알기위해

    CreateCodeImage createCodeImage = new CreateCodeImage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionButton = (ImageButton) findViewById(R.id.questionButton);
        exclamationButton = (ImageButton) findViewById(R.id.exclamationButton);
        addCameraButton = (Button) findViewById(R.id.addByCamera);
        addAlbumButton = (Button) findViewById(R.id.addByAlbum);
        addSelfButton = (Button) findViewById(R.id.addBySelf);
        settingButton = (Button) findViewById(R.id.setting);

        emptyImage = (ImageView) findViewById(R.id.emptyImage);

        ctx = getBaseContext();
        display = getWindowManager().getDefaultDisplay();

        codeString = temp.getStringArrayPref(MainActivity.this, SHARED_PREF_CODE_STRING);
        codeFormat = temp.getStringArrayPref(MainActivity.this, SHARED_PREF_CODE_FORMAT);
        codeeNickname = temp.getStringArrayPref(MainActivity.this, SHARED_PREF_CODE_NICKNAME);
        nowPosition = temp.getNowPosition(MainActivity.this, SHARED_PREF_NOW_POSITION_KEY);

        vp = (ForViewPagerSize) findViewById(R.id.vp);
        indicator = (CircleIndicator) findViewById(R.id.indicator);

        barcode =  new EditText(MainActivity.this);//selfInsert용

        ///////////////////
        /*순서 바꾸지 말 것.*/
        setViewPager();
        setIndicator();
        setButtonLayout();
        ///////////////////

        if (nowPosition > 0)
            vp.setCurrentItem(nowPosition);

        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent explainIntent = new Intent(getApplicationContext(), ExplainActivity.class);
                startActivity(explainIntent);
            }
        });

        exclamationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override

                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.info:
                                startActivity(new Intent(MainActivity.this, Information.class));
                                break;
                            case R.id.openSource2:
                                startActivity(new Intent(MainActivity.this, OpenSource.class));
                                break;
                            default:
                                break;
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });


        addCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanQR.class);
                startActivityForResult(intent, 1); //인텐트 다녀 온 후 onActivityResult 호출
            }
        });

        addAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReadGalleryCodeActivity.class);
                startActivityForResult(intent, 1); //인텐트 다녀 온 후 onActivityResult 호출
            }
        });

        addSelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] item = {"바코드입력(CODE_128)", "QR입력"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("선택");
                builder.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selfInsertCode(which);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBarcodeList();
            }
        });
    }

    void selfInsertCode(final int position)
    {
        if (barcode.getParent() != null) {
            ((ViewGroup) barcode.getParent()).removeView(barcode);
            barcode.setText("");
        }
        AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity.this);
        switch (position)
        {
            case 0:builder3.setTitle("바코드를 입력해주세요."); break;
            case 1:builder3.setTitle("QR코드를 입력해주세요.");
        }

        builder3.setView(barcode);
        builder3.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (barcode.length() != 0) {

                    String codeStringSelfInsert = barcode.getText().toString();

                    Intent data = new Intent();
                    data.putExtra(SHARED_PREF_CODE_STRING,codeStringSelfInsert);

                    switch (position)
                    {
                        case 0:
                            data.putExtra(SHARED_PREF_CODE_FORMAT,CODE_128);
                            data.putExtra(SHARED_PREF_CODE_NICKNAME,DEFAULT_CODE_BAR_NICK);
                            break;
                        case 1:
                            data.putExtra(SHARED_PREF_CODE_FORMAT,QR_CODE);
                            data.putExtra(SHARED_PREF_CODE_NICKNAME,DEFAULT_CODE_QR_NICK);
                            default:
                    }

                    confirm(data);

                } else if (barcode.length() == 0) {
                    switch (position)
                    {
                        case 0:Toast.makeText(getApplicationContext(), "바코드를 입력해주세요", Toast.LENGTH_SHORT).show();break;
                        case 1:Toast.makeText(getApplicationContext(), "QR코드를 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        AlertDialog alertDialog3 = builder3.create();
        alertDialog3.show();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        temp.setNowPosition(MainActivity.this, SHARED_PREF_NOW_POSITION_KEY, nowPosition);

        if (vp.getCurrentItem() == 0)
            setViewPager();

        super.onWindowFocusChanged(hasFocus);

        save();
        setIndicator();

    }

    public void setIndicator() {
        RelativeLayout.LayoutParams lp_indicator = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                30);
        int topMargin_indicator = (int) ((float) 278 / (float) 1920 * getResources().getDisplayMetrics().heightPixels); // 휴대폰 세로길이의 1920분의 278셀 top margin
        lp_indicator.setMargins(0, topMargin_indicator, 0, 0);

        indicator.setViewPager(vp);
        indicator.setLayoutParams(lp_indicator);

    }

    public void setButtonLayout() {
        ///////////////////
        /*상단 버튼 크기와 마진 */
        int buttonMargin = (int) ((float) 36 / (float) 1080 * getResources().getDisplayMetrics().widthPixels); // 휴대폰 가로길이의 1080분의 36 크기
        int buttonSize = (int) ((float) 88 / (float) 1080 * getResources().getDisplayMetrics().widthPixels); // 휴대폰 가로길이의 1080분의 88 크기

        RelativeLayout.LayoutParams lp_exclamation = new RelativeLayout.LayoutParams(buttonSize, buttonSize);
        RelativeLayout.LayoutParams lp_question = new RelativeLayout.LayoutParams(buttonSize, buttonSize);
        lp_question.setMargins(buttonMargin, buttonMargin, buttonMargin, buttonMargin);
        lp_exclamation.setMargins(buttonMargin, buttonMargin, buttonMargin, buttonMargin);

        lp_exclamation.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp_exclamation.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        lp_question.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp_question.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        questionButton.setLayoutParams(lp_question);
        exclamationButton.setLayoutParams(lp_exclamation);

        ///////////////////

        ///////////////////
        /*하단 버튼 크기와 마진 */
        LinearLayout buttonLinearLayout = (LinearLayout) findViewById(R.id.buttonLayout);

        float deviceWidth = getResources().getDisplayMetrics().widthPixels; //휴대폰 디스플레이 가로픽셀
        float deviceHeight = getResources().getDisplayMetrics().heightPixels; //휴대폰 디스플레이 세로픽셀

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        int sideMargin = (int) ((float) 147 / (float) 1080 * deviceWidth); // 휴대폰 가로길이의 147/1080 만큼의 옆마진길이
        int topMargin = (int) ((float) 139 / (float) 1920 * deviceHeight); // 휴대폰 가로길이의 162/1920 만큼의 밑마진길이
        lp.setMargins(sideMargin, topMargin, sideMargin, 0);
        if (vp.getVisibility() == View.VISIBLE) {
            lp.addRule(RelativeLayout.BELOW, vp.getId());
        } else if (vp.getVisibility() == View.INVISIBLE) {
            topMargin = (int) ((float) 964 / (float) 1920 * deviceHeight);
            lp.setMargins(sideMargin, topMargin, sideMargin, 0);
            lp.addRule(RelativeLayout.BELOW, RelativeLayout.ALIGN_PARENT_TOP);
        }

        lp.height = (int) deviceWidth - (sideMargin * 2);
        buttonLinearLayout.setLayoutParams(lp);
        ///////////////////
    }

    public void setViewPager() {

        if (!codeString.isEmpty()) {
            emptyImage.setVisibility(View.INVISIBLE);
            vp.setVisibility(View.VISIBLE);

            pagerAdapter = new ImageViewAdapter(this);
            pagerAdapter.getData(codeString, codeFormat, codeeNickname, display);////////54352
            pagerAdapter.setContext(MainActivity.this);
            pagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());

            int dpValue = 54;
            float d = getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d);

            vp.setClipToPadding(false);
            vp.setPadding(margin, 0, margin - CODE_IMG_PIX, 0);

            vp.setPageMargin(margin / 2);

            ///////////////////////////////////////////////////////////////////////////// 바코드/QR코드가 나오는 카드의 마진값 설정
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            int topMargin = (int) ((float) 337 / (float) 1920 * getResources().getDisplayMetrics().heightPixels); // 휴대폰 세로길이의 1920분의 337픽셀 top margin
            lp.setMargins(0, topMargin, 0, 0);
            vp.setLayoutParams(lp);
            ///////////////////////////////////////////////////////////////////////////////


            vp.setAdapter(pagerAdapter);


        } else if (codeString.isEmpty()) {
            vp.setVisibility(View.GONE);
            emptyImage.setVisibility(View.VISIBLE);
            vp.setVisibility(View.INVISIBLE);

            int dpValue = 54;
            float d = getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d);

            emptyImage.setCropToPadding(false);
            emptyImage.setPadding(margin, 0, margin, 0);


            ///////////////////////////////////////////////////////////////////////////// 바코드/QR코드가 나오는 카드의 마진값 설정
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            emptyImage.setLayoutParams(lp);
            ///////////////////////////////////////////////////////////////////////////////

            vp.setAdapter(pagerAdapter);

        }
    }

    @Override
    protected void onStop() {
        nowPosition = vp.getCurrentItem();
        temp.setNowPosition(MainActivity.this, SHARED_PREF_NOW_POSITION_KEY, nowPosition);
        Log.d("tttt", "nowpostion in onstop = " + nowPosition);
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
                confirm(data);
            }
        }
    }

    void confirm(Intent data)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        //builder.setTitle("추가하시겠습니까?");

        final String codeStringConfirm = data.getExtras().getString(SHARED_PREF_CODE_STRING);
        final String codeFormatConfirm = data.getExtras().getString(SHARED_PREF_CODE_FORMAT);
        final String codeNickConfirm = data.getExtras().getString(SHARED_PREF_CODE_NICKNAME);

        LayoutInflater inflater=getLayoutInflater();
        View dialogView;
        if(codeFormatConfirm.equals(QR_CODE)) {
            dialogView = inflater.inflate(R.layout.dialog_confirm_qr, null);
        }
        else {
            dialogView = inflater.inflate(R.layout.dialog_confirm_barcode, null);
        }

        builder.setView(dialogView);
        builder.setCancelable(false);

        final ImageView img = (ImageView)dialogView.findViewById(R.id.confirmCodeImg);
        final EditText editText = (EditText)dialogView.findViewById(R.id.confirmNick);
        TextView textView = (TextView)dialogView.findViewById(R.id.confirmText);

        CreateCodeImage cci = new CreateCodeImage();
        img.setImageBitmap(cci.createBitMatrix(codeStringConfirm, codeFormatConfirm, display));

        textView.setText(codeStringConfirm);

        builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                codeString.add(codeStringConfirm);
                codeFormat.add(codeFormatConfirm);
                if(editText.getText().toString() == "" || editText.getText().toString().isEmpty())
                    codeeNickname.add(codeNickConfirm);
                else
                    codeeNickname.add(editText.getText().toString());

                save();

                if (vp.getVisibility() == View.INVISIBLE)
                    setViewPager();
                pagerAdapter.notifyDataSetChanged();

                Toast.makeText(MainActivity.this,"추가 완료",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"취소",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void save() {
        temp.setStringArrayPref(MainActivity.this, SHARED_PREF_CODE_STRING, codeString);
        temp.setStringArrayPref(MainActivity.this, SHARED_PREF_CODE_FORMAT, codeFormat);
        temp.setStringArrayPref(MainActivity.this, SHARED_PREF_CODE_NICKNAME, codeeNickname);
    }

    void notification(int count) {

        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
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
        PendingIntent notiIntent = PendingIntent.getActivity(this, 0, Pintent, PendingIntent.FLAG_CANCEL_CURRENT);//노티피케이션 클릭시 홈화면으로 이동

        nowNotificationCodePosition = codeStringWithoutQRCode.get(count);//코드지울때 노티가 해당 코드이면 같이 없어질때를 알기위해


        builder = new NotificationCompat.Builder(this, "default").setOngoing(true);
        //노티피케이션의 객체 선언부분이라고 보면됨
        // setOngoing을 하면 고정
        builder.setSmallIcon(R.drawable.noti_icon);

        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        builder.setContent(customView);
        //builder.setCustomContentView(customView);

        builder.setContentIntent(notiIntent);//Pending 인텐트를 실행
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        // 사용자가 탭을 클릭하면 자동 제거
        // builder.setAutoCancel(true);//이게 위에 setOngoing때문에 작동을 안함.
        // 알림 표시

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_HIGH));
        }
        notificationManager.notify(1, builder.build());
    }

    void showBarcodeList() {

        final List<String> choosebarcodeList = new ArrayList<>();
        choosebarcodeList.add("바코드를 고정하지 않음");

        codeStringWithoutQRCode = new ArrayList<>();
        codeFormatWithoutQRCode = new ArrayList<>();

        for (int i = 0; i < codeString.size(); i++) {
            if (!codeFormat.get(i).equals(QR_CODE)) { //qr 코드가 아닐때만
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

    @Override
    public void onClick(View v) {
    }
}