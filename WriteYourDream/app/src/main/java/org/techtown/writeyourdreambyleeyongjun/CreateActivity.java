package org.techtown.writeyourdreambyleeyongjun;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Calendar;

public class CreateActivity extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE=200;

    Button button;
    Button backButton;

    EditText editText;
    CheckBox checkBox;
    CheckBox checkBox2;
    CheckBox checkBox3;

    LinearLayout linearLayout;
    LinearLayout linearLayout2;

    Button button5;
    ImageView imageView;

    Uri uri; //이미지 주소를 저장할 공간

    TimePicker timePicker;

    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;
    Spinner spinner5;
    Spinner spinner6;

    String[] items1 = {"2010","2011","2012","2013","2014","2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025",
            "2026","2027","2028","2029","2030","2031","2032"};
    String[] items2 = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    String[] items3 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23",
            "24","25","26","27","28","29","30","31"};
    //String[] items4 = {"오전","오후"};
    //String[] items5 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23",
     //       "24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48",
         //   "49","50","51","52","53","54","55","56","57","58","59"};

    //변경시에 사용되는 것들
    Cursor cursor;
    Intent intent;
    int keyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        editText = (EditText) findViewById(R.id.editText);

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hideCalendar(isChecked);
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hideImage(isChecked);
            }
        });


        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hideTimePicker(isChecked);
            }
        });

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        backButton = (Button) findViewById(R.id.button2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);

        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionChecked();
            }
        });

        timePicker = (TimePicker) findViewById(R.id.timePicker);

        spinner1 = (Spinner) findViewById(R.id.spinner1); /////startYY, finishYY 설정
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner4.setAdapter(adapter1);


        spinner2 = (Spinner) findViewById(R.id.spinner2); /////startMM, finishMM 설정
        spinner5 = (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner5.setAdapter(adapter2);

        spinner3 = (Spinner) findViewById(R.id.spinner3); /////startDD, finishDD 설정
        spinner6 = (Spinner) findViewById(R.id.spinner6);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner6.setAdapter(adapter3);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,-2010);
        calendar.add(Calendar.DATE,-1);
        int i = Calendar.YEAR;
        spinner1.setSelection(calendar.get(Calendar.YEAR));
        spinner4.setSelection(calendar.get(Calendar.YEAR));

        spinner2.setSelection(calendar.get(Calendar.MONTH));
        spinner5.setSelection(calendar.get(Calendar.MONTH));

        spinner3.setSelection(calendar.get(Calendar.DATE));
        spinner6.setSelection(calendar.get(Calendar.DATE));

        intent = getIntent();
        keyId = intent.getIntExtra("keyId",1000);
        if(intent.getStringExtra("modify")!=null){
            TextView textView3 = (TextView) findViewById(R.id.textView3);
            textView3.setText("변경하기");
            loadingData(keyId);
        }
    }

    public void hideCalendar(boolean isChecked){
        if(isChecked){ //체크가 되었을 때
            linearLayout.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
        }else{ //체크가 풀렸을 때
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
        }
    }

    public void hideImage(boolean isChecked){
        if(isChecked){ //체크가 되었을 때
            imageView.setVisibility(View.GONE);
        }else{ //체크가 풀렸을 때
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void hideTimePicker(boolean isChecked){
        if(isChecked){ //체크가 되었을 때
            timePicker.setVisibility(View.GONE);
        }else{ //체크가 풀렸을 때
            timePicker.setVisibility(View.VISIBLE);
        }
    }

    public void permissionChecked(){
        //////미디어읽기에 관한 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ////한번 권한을 거절하면 ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)값이 True가 됨.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
        else{
           galleryOpen();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case 1:  //권한의 requestCode를 1로 설정함
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    //권한 승인
                    galleryOpen();
                }
                return;
        }
    }

    public void galleryOpen(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent,GET_GALLERY_IMAGE);
        Log.d("AppHelper","생성됨");
    }

    ///galleryOpen에 대한 onactivityresult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("AppHelper","생성됨2");
        // Check which request we're responding to
        if (requestCode == GET_GALLERY_IMAGE && resultCode==RESULT_OK && null!=data) {
            // Make sure the request was successful
                try {
                    uri = data.getData();
                    //imageView.setImageURI(uri);
                    Glide.with(getApplicationContext()).load(uri).into(imageView);
                    checkBox2.setChecked(false);
                    Log.d("AppHelper","생성됨3");

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }else{
            Toast.makeText(getApplicationContext(),"취소 되었습니다.",Toast.LENGTH_LONG).show();
        }

    }

    //변경시 원래 데이터 보여주기
    public void loadingData(int keyId){
        String tableName = "dreamInfo";
        String sql ="select * from " + tableName+" where _id = "+keyId+"";
        cursor = AppHelper.database.rawQuery(sql,null);
        cursor.moveToFirst();
        editText.setText(cursor.getString(1));

        ///calendar info
        if(!cursor.getString(3).equals("")) {
            spinner1.setSelection(Integer.valueOf(cursor.getString(2)) - 2010);
            spinner2.setSelection(Integer.valueOf(cursor.getString(3)) - 1);
            spinner3.setSelection(Integer.valueOf(cursor.getString(4)) - 1);
            spinner4.setSelection(Integer.valueOf(cursor.getString(5)) - 2010);
            spinner5.setSelection(Integer.valueOf(cursor.getString(6)) - 1);
            spinner6.setSelection(Integer.valueOf(cursor.getString(7)) - 1);
        }else{
            checkBox.setChecked(true);
        }

        if(cursor.getString(8)!=null){
            //imageView.setImageURI(Uri.parse(cursor.getString(8)));
            Glide.with(getApplicationContext()).load(Uri.parse(cursor.getString(8))).into(imageView);
            uri = Uri.parse(cursor.getString(8));
        }else{
            checkBox2.setChecked(true);
        }

        if(cursor.getInt(9)!=1000){
            timePicker.setHour(cursor.getInt(9));
            timePicker.setMinute(cursor.getInt(10));
        }else{
            checkBox3.setChecked(true);
        }
    }

    public void saveData() {
        Intent intent = new Intent();
        intent.putExtra("keyId", keyId);
        if (editText.getText().toString().length()==0) {
            Toast.makeText(getApplicationContext(), "목표가 설정되지 않았습니다.", Toast.LENGTH_LONG).show();
        } else {
            intent.putExtra("dream", editText.getText().toString());
        }

        if (!checkBox.isChecked()) {
            intent.putExtra("startYY", spinner1.getSelectedItem().toString());
            intent.putExtra("startMM", spinner2.getSelectedItem().toString());
            intent.putExtra("startDD", spinner3.getSelectedItem().toString());
            intent.putExtra("finishYY", spinner4.getSelectedItem().toString());
            intent.putExtra("finishMM", spinner5.getSelectedItem().toString());
            intent.putExtra("finishDD", spinner6.getSelectedItem().toString());
        }

        if (!checkBox2.isChecked()) { //image
            if(uri!=null){
                intent.putExtra("Image", uri+"");
            }
        }

        if (!checkBox3.isChecked()) { //alarm
            intent.putExtra("Hour", timePicker.getHour());
            intent.putExtra("Minute", timePicker.getMinute());
        }

        if(editText.getText().toString().length()!=0){
            setResult(RESULT_OK,intent);
            finish();
        }

    }

}
