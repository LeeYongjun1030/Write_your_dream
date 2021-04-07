package org.techtown.writeyourdreambyleeyongjun;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static int ONE_MINUTE = 5626;

    private static int CREATEACTIVITY = 2283;
    private static int CONTENTACTIVITY = 2284;

    private AdView mAdView;
    private AdView mAdView2;
    private InterstitialAd mInterstitialAd;

    //추가하기버튼 및 리스트뷰
    ListView listView;
    Button button;
    ListAdapter adapter;

    //편집버튼
    Button button2;

    //삭제버튼
    Button button3;

    Cursor cursor;


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LottieAnimationView lottie = (LottieAnimationView) findViewById(R.id.lottie);
        lottie.playAnimation();
        lottie.loop(true);

        LottieAnimationView lottie2 = (LottieAnimationView) findViewById(R.id.lottie2);
        //lottie2.playAnimation();
        //lottie2.loop(true);


        ////ad
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mAdView2 = findViewById(R.id.adView2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3308391231186761/3718712993");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //그다음 전면 광고를 위한 로드
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                //Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        //developer's comment
        Button qmarkbutton = (Button) findViewById(R.id.qmarkButton);
        qmarkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyComment();
            }
        });

        String databaseName = "dream.db";
        AppHelper.DatabaseHelper helper = new AppHelper.DatabaseHelper(this,databaseName, null,1);
        AppHelper.database = helper.getWritableDatabase();
        //AppHelper.createTable("dreamInfo");

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// + 버튼 클릭
                Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
                startActivityForResult(intent,CREATEACTIVITY);
            }
        });

        listView = (ListView) findViewById(R.id.listView);

        adapter = new ListAdapter();

        listView.setAdapter(adapter);

        makeListFromDatabase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }else{
                    Log.d("AppHelper","not yet");
                }

                listViewClicked(position);
            }
        });

        //편집버튼 기능
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2Clicked();
            }
        });

        //삭제버튼 기능
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               button3Clicked();
            }
        });


        if(cursor.getColumnCount()<1000){
            for(int i=cursor.getColumnCount();i<cursor.getColumnCount()+50;i++){
                AppHelper.createColumn(i-12);
            }
        }

    } /////onCreate()///

    public void showMyComment(){
        String comment = "개발자입니다. \n" +
                "무엇이든 꾸준히 실천하는 것은 매우 어렵습니다. 저 또한 매일 저 자신과 타협하며 " +
                "계획했던 일들을 금방 포기하기 일쑤였고, 한번 제대로 살아보자는 다짐이 계기가 되어 이 앱을 제작하게 되었습니다. 앱을 사용하시는 경우는 다음과 같이 크게 두가지가 있습니다.\n" +
                "1. 매일 상기하면 좋을 나만의 목표, 꿈이 있을 때\n" +
                "2. 꾸준히 해야 할 무엇인가가 있을 때 \n" +
                "이러한 경우 앱을 사용하시면 도움이 될 수 있습니다. \n" +
                "앱이 여러분들의 삶에 조금이라도 도움이 되길 간절히 바랍니다.\n" +
                "앱에 불편한 점이 있다면 리뷰로 의견을 남겨주세요. 최대한 해결할 수 있도록 하겠습니다. 앱이 마음에 드셨다면 가족, 친구, 직장 동료들에게 추천 부탁드리겠습니다. 계획하시는 모든 일, 목표가 이뤄지기를 마음 속으로 응원하겠습니다!";

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("소개");
        builder.setMessage(comment);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    public void makeListFromDatabase(){
        String tableName = "dreamInfo";
        String sql ="select * from " + tableName;
        cursor = AppHelper.database.rawQuery(sql,null);

        for(int i=0; i <cursor.getCount() ;i++){
            cursor.moveToNext();
            DreamItem item;
            adapter.addItem(item =new DreamItem(cursor.getString(1),
                    cursor.getString(2),cursor.getString(3),
                    cursor.getString(4),cursor.getString(5),
                    cursor.getString(6),cursor.getString(7)));
            item.setImage_uri(cursor.getString(8));
        }

        adapter.notifyDataSetChanged();
        cursor.moveToFirst();
    }

    public void button2Clicked(){
        if(!adapter.VISIBLE){
            //편집하기 버튼을 누르면 체크박스가 보여야 하므로 visible값을 true로 변경. 그에 따라 getview에서 메소드 실행
            adapter.VISIBLE = true;

            //button2.setText("취소");
            //button2.setBackground(getDrawable(R.drawable.cancel));
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.VISIBLE);
            button.setClickable(false);
            button.setVisibility(View.INVISIBLE);

            //listView 다시 생성
            listView.setAdapter(adapter);

        }else{//편집하기 중 취소버튼을 눌렀을 경우
            //편집 기능을 끝내는 순간 check 기록이 남아있으면 안되므로 clear를 해줌
            adapter.VISIBLE = false;
            adapter.checkedList.clear();

            //button2.setText("편집하기");
            button2.setVisibility(View.VISIBLE);
            //button2.setBackground(getDrawable(R.drawable.minus));
            button.setClickable(true);
            button.setVisibility(View.VISIBLE);
            button3.setVisibility(View.INVISIBLE);

            listView.setAdapter(adapter);
        }
    }

    public void button3Clicked(){
        ArrayList<Integer> deleteList = new ArrayList<Integer>();

        ///checkedList를 오름차순 정렬(sort), 순서반대로(reverse)를 해주면 오름차순 정렬이 됨. 뒤에서 부터 삭제하므로 index 문제가 해결됨
        Collections.sort(adapter.checkedList);
        Collections.reverse(adapter.checkedList);
        deleteList = adapter.checkedList;
        //Toast.makeText(getApplicationContext(),"삭제 예정은 :"+ deleteList,Toast.LENGTH_LONG).show();

        for(int i=0; i<deleteList.size(); i++){

            //////alarm 삭제
            String sql4 = "select _id, title from dreaminfo";
            Cursor cursor = AppHelper.database.rawQuery(sql4, null);

            for(int k=0; k<deleteList.get(i)+1;k++){
                cursor.moveToNext();
            }
            AlarmHATT myAlarm = new AlarmHATT(getApplicationContext());
            Log.d("AppHelper",cursor.getInt(0)+cursor.getString(1)+"");
            myAlarm.deleteAlarm(cursor.getInt(0),cursor.getString(1));


            //database 아이템들의 _id를 reIndex 해줘야됨 먼저..
            AppHelper.deleteTableData(deleteList.get(i));
            adapter.dropItem(adapter.items.get(deleteList.get(i)));
            //Toast.makeText(getApplicationContext(),"삭제 예정은 :"+adapter.items.get(deleteList.get(i)).getDream(),Toast.LENGTH_LONG).show();
        }
        adapter.notifyDataSetChanged();
        adapter.VISIBLE = false;
        adapter.checkedList.clear();

        //button2.setText("편집하기");
        //button2.setBackground(getDrawable(R.drawable.minus));
        button2.setVisibility(View.VISIBLE);
        button.setClickable(true);
        button.setVisibility(View.VISIBLE);
        button3.setVisibility(View.INVISIBLE);

        //adapter.VISIBLE = false인 상태에서 setAdapter를 한번 더 해줘야 체크박스가 없는 리스트가 나열됨.
        listView.setAdapter(adapter);
    }

    public void listViewClicked(int position){
        String tableName = "dreamInfo";
        String sql ="select * from " + tableName;
        Cursor cursor2 = AppHelper.database.rawQuery(sql,null);

        cursor2.moveToFirst();
        Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
        for(int i=0; i <position ;i++) {
            cursor2.moveToNext();

        }
        //없는 항목에 대해서는 null을 리턴
        intent.putExtra("position", position);
        intent.putExtra("key", cursor2.getInt(0));
        intent.putExtra("dream", cursor2.getString(1));
        intent.putExtra("startYY", cursor2.getString(2));
        intent.putExtra("startMM", cursor2.getString(3));
        intent.putExtra("startDD", cursor2.getString(4));
        intent.putExtra("finishYY", cursor2.getString(5));
        intent.putExtra("finishMM",cursor2.getString(6));
        intent.putExtra("finishDD", cursor2.getString(7));
        intent.putExtra("Image", cursor2.getString(8));
        intent.putExtra("Hour", cursor2.getInt(9));
        intent.putExtra("Minute", cursor2.getInt(10));

        startActivityForResult(intent,CONTENTACTIVITY);
        cursor2.moveToFirst();
    }

    public class AlarmHATT{
        private  Context context;
        public AlarmHATT(Context context){
            this.context = context;
        }

        public void Alarm(int hour, int min, int keyId, String title){
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(MainActivity.this,BroadcastD.class);

            intent.putExtra("keyId",keyId);
            intent.putExtra("title",title);

            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this,keyId,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,min);

            long differ = Calendar.getInstance().getTimeInMillis()-calendar.getTimeInMillis();
            if(differ>0){
                calendar.add(Calendar.DATE,1);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,sender);
            }else{
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,sender);
            }
            Toast.makeText(getApplicationContext(), "알림이 매일 "+hour+"시"+min+"분에 울리게됩니다.", Toast.LENGTH_LONG).show();
        }

        public void deleteAlarm(int keyId,String title){
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(MainActivity.this,BroadcastD.class);

            intent.putExtra("keyId",keyId);
            intent.putExtra("title",title);

            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this,keyId,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(sender);
            //Toast.makeText(getApplicationContext(), title+"알림이 삭제되었습니다.", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { ////CreateActivity 종료시 매소드 실행, 각종 정보를 받아온다.
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CREATEACTIVITY){

            if(resultCode == RESULT_OK){

                AppHelper.insertTitleData(data.getStringExtra("dream")); //database에 다짐 저장

                //insertTitleData를 하면 추가된 데이터는 가장 마지막 열에 놓여지게 되니까 cursor를 제일 마지막으로 옮겨서 그 key id값을 찾아서 저장.
                //이 찾은 key id를 이용하여 나머지 항목(기간, 사진, 알림)을 저장.
                String tableName = "dreamInfo";
                String sql ="select * from " + tableName;
                Cursor idFinder = AppHelper.database.rawQuery(sql,null);
                idFinder.moveToLast();
                int keyId = idFinder.getInt(0);
                String title = idFinder.getString(1);
                Log.d("AppHelper",keyId+"");

                DreamItem item;

                if(data.getStringExtra("startYY")==null){
                    adapter.addItem(item = new DreamItem(data.getStringExtra("dream"),"                                                                         ",
                            "","","","",""));
                    //adapter.notifyDataSetChanged();

                    AppHelper.insertCalendarData("                                                                                   ","",
                            "","",
                            "","",keyId);

                }else{
                    adapter.addItem(item = new DreamItem(data.getStringExtra("dream"),
                            data.getStringExtra("startYY"),data.getStringExtra("startMM"),
                            data.getStringExtra("startDD"),data.getStringExtra("finishYY"),
                            data.getStringExtra("finishMM"),data.getStringExtra("finishDD")));
                    //adapter.notifyDataSetChanged();

                    //database에 저장되어있는 해당 다짐과 같은 줄에 날짜정보를 업데이트
                    AppHelper.insertCalendarData(data.getStringExtra("startYY"),data.getStringExtra("startMM"),
                            data.getStringExtra("startDD"),data.getStringExtra("finishYY"),
                            data.getStringExtra("finishMM"),data.getStringExtra("finishDD"),keyId);
                }

                if(data.getStringExtra("Image")!=null){
                    //Toast.makeText(getApplicationContext(), "받아온 이미지 주소는 "+data.getStringExtra("Image"), Toast.LENGTH_LONG).show();
                    item.setImage_uri(data.getStringExtra("Image"));
                    //database에 저장되어있는 해당 다짐과 같은 줄에 이미지정보를 업데이트
                    AppHelper.insertImageData(data.getStringExtra("Image"),keyId);
                }

                adapter.notifyDataSetChanged();

                if(data.getIntExtra("Hour",1000)!=1000 && data.getIntExtra("Minute",1000)!=1000) {

                    AlarmHATT myAlarm = new AlarmHATT(getApplicationContext());
                    myAlarm.Alarm(data.getIntExtra("Hour", 1), data.getIntExtra("Minute", 1)
                    ,keyId,title);

                    AppHelper.insertAlarmData(data.getIntExtra("Hour", 1000), data.getIntExtra("Minute", 1000),keyId);
                }else{
                    AppHelper.insertAlarmData(1000, 1000, keyId);
                }

                cursor = AppHelper.database.rawQuery(sql,null);
                cursor.moveToFirst();

            }else{
                Toast.makeText(getApplicationContext(), "작성이 취소되었습니다.", Toast.LENGTH_LONG).show();
            }

        }
        if(requestCode==CONTENTACTIVITY){
            adapter.items.clear();

            String tableName = "dreamInfo";
            String sql ="select * from " + tableName;
            cursor = AppHelper.database.rawQuery(sql,null);

            for(int i=0; i <cursor.getCount() ;i++){
                cursor.moveToNext();
                DreamItem item;
                adapter.addItem(item =new DreamItem(cursor.getString(1),
                        cursor.getString(2),cursor.getString(3),
                        cursor.getString(4),cursor.getString(5),
                        cursor.getString(6),cursor.getString(7)));
                item.setImage_uri(cursor.getString(8));
            }

            adapter.notifyDataSetChanged();
            cursor.moveToFirst();
        }
    }

    class ListAdapter extends BaseAdapter
    {
        ArrayList<DreamItem> items = new ArrayList<DreamItem>();

        DreamItemView[] view = new DreamItemView[500];

        ArrayList<Integer> checkedList = new ArrayList<Integer>();

        public boolean VISIBLE = false;

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(DreamItem item){
            items.add(item);
        }

        public void dropItem(DreamItem item){
            items.remove(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            view[position] = new DreamItemView(getApplicationContext());

            DreamItem item = items.get(position);

            view[position].setDream(item.getDream());
            view[position].setStartYY(item.getStart_year());
            view[position].setStartMM(item.getStart_month());
            view[position].setStartDD(item.getStart_day());
            view[position].setFinishYY(item.getFinish_year());
            view[position].setFinishMM(item.getFinish_month());
            view[position].setFinishDD(item.getFinish_day());

            view[position].setImageView(item.getImage_uri());

            //boolean visible값을 전달받아 그에 맞는 메소드 실행
            if(VISIBLE){
                //checkBox가 켜진채 리스트뷰 생성
                view[position].checkVisible();
            }else{
                //checkBox가 꺼진채 리스트뷰 생성
                view[position].checkGone();
            }

            view[position].checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){//체크가 된 순간, position값을 arraylist에 저장
                        if(!checkedList.contains(position)){//중복을 허용하게 되면 스크롤을 내렸다 다시 올릴때 check되었던 position값이 arraylist에 다시 들어가게됨
                        checkedList.add(position);}
                        //Toast.makeText(getApplicationContext(),checkedList.size()+":"+ checkedList,Toast.LENGTH_LONG).show();

                    }else{
                        //체크가 풀린 순간, position값을 arraylist에서 삭제
                        //position값의 index를 찾은 뒤 그 값을 삭제
                        int index = checkedList.indexOf(position);
                        checkedList.remove(index);
                        //Toast.makeText(getApplicationContext(),checkedList.size()+":"+ checkedList,Toast.LENGTH_LONG).show();
                    }
                }
            });


            //scroll에 의해 check가 사라짐을 방지하기 위한 if문. 미리 저장해놓았던 checkedList를 이용하여, checkedList에 포함되어있는 position의 리스트를 만들때는
            //getview에서 checkbox에 check된 상태로 뷰를 만들도록 한다.
            if(checkedList.contains(position)){
                view[position].checkBox.setChecked(true);
            }

            return  view[position];
        }


    }

    @Override
    public void onBackPressed() {
        if(adapter.VISIBLE){
            //checkBox 켜진 상태에서는 이걸 우선적으로 종료함
            adapter.VISIBLE = false;
            adapter.checkedList.clear();

            //button2.setText("편집하기");
            //button2.setBackground(getDrawable(R.drawable.minus));
            button2.setVisibility(View.VISIBLE);
            button.setClickable(true);
            button.setVisibility(View.VISIBLE);
            button3.setVisibility(View.INVISIBLE);
            listView.setAdapter(adapter);
        }else{
            super.onBackPressed();
        }
    }
}
