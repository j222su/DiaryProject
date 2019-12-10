package com.example.info1.diaryproject;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener, View.OnClickListener {

    DatePicker datePicker;
    EditText editText;
    Button button;
    String fileName, diaryData;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datePicker = findViewById(R.id.datePicker);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);


        //1. 현재 날짜 가져오기
        picCurrentDate();
        //2. 날짜를 선택하면 이벤트처리한다.
        datePicker.setOnDateChangedListener(this);
        //3. 일기를 저장하는 기능 이벤트처리
        button.setOnClickListener(this);

    }

    private void picCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.init(year, month, day, null);//datePicker속에 년도, 월, 일을 집어넣는다.

    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        //year, month, day를 string으로 만들어서 파일명을 만든다.
        //공유하기 위해서 String을 전역변수로 만든다.
        FileInputStream fis = null;
        try {
            fileName = String.valueOf(year) + "_" + String.valueOf(month) + "_" + String.valueOf(day) + ".txt";
            fis = openFileInput(fileName);
            byte[] readDiary = new byte[fis.available()];
            fis.read(readDiary);
            editText.setText(new String(readDiary));//string으로 바뀌어서 저장된다.
            button.setText("modify");
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            toastDisplay(fileName + "doesn't have ur diary");
            button.setText("save");
            editText.setText("");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                saveDiary();
                break;
        }
    }

    private void saveDiary() {
        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            diaryData = editText.getText().toString();//데이터를 가져올 때 한 번만 가져오기 위해서 선언하였다.
            //이렇게 하지 않으면 if문에서 editText.getText().toString()를 다시 읽어야 되기 때문이다.
            if (diaryData.trim().length() >= 1) { //일기장에 내용을 적지 않으면 저장되지 않아야 하므로
                fos.write(diaryData.getBytes());
                toastDisplay(fileName + "Completed save");
            } else {
                toastDisplay("write first and save");
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toastDisplay(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
    }
}
