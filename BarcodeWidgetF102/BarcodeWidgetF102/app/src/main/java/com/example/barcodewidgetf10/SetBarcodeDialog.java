package com.example.barcodewidgetf10;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class SetBarcodeDialog {

    private Context context;

    public SetBarcodeDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final ArrayList<String> codeString) {

        //  다이얼로그를 정의하기위해 Dialog 클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_set_barcode_dialog);

        // 다이얼로그를 노출한다.
        dlg.show();

        //  다이얼로그의 각 위젯들을 정의한다.
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);
        final ListView barcodeList = (ListView) dlg.findViewById(R.id.barcodeList);

        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, codeString); //코드 스트링 리스트의 어댑터 정의
        barcodeList.setAdapter(adapter); // 코드 스트링 리스트의 어댑터 연결

        final ArrayList<String> touchedItem = new ArrayList<>();

        barcodeList.setOnItemClickListener(new AdapterView.OnItemClickListener() { //리스트 뷰의 각 아이템을 클릭 했을 때
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                touchedItem.clear(); // touchedItem 을 전부 삭제
                touchedItem.add(codeString.get(position)); //touchedItem 에 있는 값을 저장
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(touchedItem.size() == 0) // 만약 클릭이 되지 않았으면 토스트 메시지 방출
                    Toast.makeText(context, "고정할 바코드를 클릭해 주세요", Toast.LENGTH_SHORT).show();
                else // 클릭이 되었으면 선택한 항목의 value 를 MainActivity 로 보내고, 다이얼로그를 종료한다.
                    dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 취소가 되었으면 취소가 되었다고 MainActivity 로 보내고 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}
