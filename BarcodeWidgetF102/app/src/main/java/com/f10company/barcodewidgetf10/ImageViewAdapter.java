package com.f10company.barcodewidgetf10;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import static com.google.zxing.integration.android.IntentIntegrator.CODE_128;

public class ImageViewAdapter extends PagerAdapter {

    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    private Context mContext = null;
    public ImageView img;
    ArrayListSaveByJson temp;
    TextView nick;
    TextView code;

    ArrayList<String> codeString;
    ArrayList<String> codeFormat;
    ArrayList<String> codeNickname;
    Display display;

    Context context;

    public void getData(ArrayList<String> arr1, ArrayList<String> arr2, ArrayList<String> arr3, Display _display) {
        codeString = arr1;
        codeFormat = arr2;
        codeNickname = arr3;
        display = _display;
    }

    public void setContext(Context c) {
        context = c;
    }


    // Context 를 전달받아 mContext 에 저장하는 생성자 추가.
    public ImageViewAdapter(Context context) {
        mContext = context;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = null;

        if (mContext != null) {
            // LayoutInflater를 통해 "/res/layout/pages.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //view = inflater.inflate(R.layout.pages, container, false);
            view = inflater.inflate(R.layout.pages,null);

            CreateCodeImage cci = new CreateCodeImage();
            nick = view.findViewById(R.id.nickname);
            code = view.findViewById(R.id.code);
            img = view.findViewById(R.id.codeImage);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    final String[] items = {"닉네임 변경", "삭제"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("설정");//여기서부터 다시
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0)
                            {
                                changeNick(v,position);
                            }
                            else if (which == 1)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                                builder.setTitle("바코드/QR코드를 삭제하시겠습니까?");

                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        MainActivity.codeeNickname.remove(position);
                                        MainActivity.codeFormat.remove(position);
                                        MainActivity.codeString.remove(position);

                                        notifyDataSetChanged();
                                    }
                                });
                                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                builder.show();
                            }
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    return false;
                }
            });

            if (codeString.isEmpty()) {
            }
            else {
                img.setImageBitmap(cci.createBitMatrix(codeString.get(position), codeFormat.get(position), display));
                nick.setText(codeNickname.get(position)); //코드 별명 만들면 별명으로

                if (!codeFormat.get(position).equals("QR_CODE")) {
                    code.setText(codeString.get(position));
                }
                else {
                    code.setVisibility(View.GONE);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lp.setMargins(0, 0, 0, 0);
                    img.setLayoutParams(lp);
                    img.setScaleType(ImageView.ScaleType.FIT_CENTER);

                }
            }
        }
        // 뷰페이저에 추가.
        container.addView(view);
        return view;
    }

    public void changeNick(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("닉네임 변경");
        final EditText et = new EditText(view.getContext());
        builder.setView(et);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.codeeNickname.set(position, et.getText().toString());
                codeNickname.set(position, et.getText().toString());
                MainActivity.temp.setStringArrayPref(context, "codeNickName", MainActivity.codeeNickname);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return codeString.size();
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }
}
