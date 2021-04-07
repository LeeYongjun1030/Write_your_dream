package org.techtown.writeyourdreambyleeyongjun;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static android.view.View.VISIBLE;

public class ContentActivity extends AppCompatActivity {

    private AdView mAdView;
    private AdView mAdView2;
    private AdView mAdView3;
    private AdView mAdView4;

    private static int FEEDBACKACTIVITY = 2299;
    private static int MODIFYING = 7554;

    Intent intent;

    ScrollView scrollView;

    //기간 정보
    TextView textView3;
    TextView textView5;
    TextView textView7;
    TextView textView10;
    TextView textView12;
    TextView textView14;

    //다짐
    TextView textView16;

    //이미지
    ImageView imageView;

    //알림
    TextView textView18;
    TextView textView21;

    //수정버튼
    Button button6;

    //feedback갯수
    TextView textView;

    //리스트뷰 플러스, 쓰레기통(button3), 마이너스(button2)
    Button button;
    Button button3;
    Button button2;

    ListView listView;
    ListAdapter adapter;

    FrameLayout container;

    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        ////ad
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mAdView3 = findViewById(R.id.adView3);
        AdRequest adRequest3 = new AdRequest.Builder().build();
        mAdView3.loadAd(adRequest3);

        LottieAnimationView lottie = (LottieAnimationView) findViewById(R.id.lottie);
        lottie.playAnimation();
        lottie.loop(true);


        textView = (TextView) findViewById(R.id.textView);

        textView3 = (TextView) findViewById(R.id.textView3);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView10 = (TextView) findViewById(R.id.textView10);
        textView12 = (TextView) findViewById(R.id.textView12);
        textView14 = (TextView) findViewById(R.id.textView14);
        textView16 = (TextView) findViewById(R.id.textView16);

        imageView = (ImageView) findViewById(R.id.imageView);
        textView18 = (TextView) findViewById(R.id.textView18);
        textView21 = (TextView) findViewById(R.id.textView21);

        intent = getIntent();

        textView3.setText(intent.getStringExtra("startYY"));

        if(intent.getStringExtra("startYY")!=null){
            textView5.setText(intent.getStringExtra("startMM"));
            textView7.setText(intent.getStringExtra("startDD"));
            textView10.setText(intent.getStringExtra("finishYY"));
            textView12.setText(intent.getStringExtra("finishMM"));
            textView14.setText(intent.getStringExtra("finishDD"));
            textView16.setText(intent.getStringExtra("dream"));
        } else{
            textView5.setText("기간 없음.                        ");
        }
        if(intent.getStringExtra("Image")!=null){
            //imageView.setImageURI(Uri.parse(intent.getStringExtra("Image")));
            Uri mUri = Uri.parse(intent.getStringExtra("Image"));
            Glide.with(getApplicationContext()).load(mUri).into(imageView);
        } else{
            //imageView.setVisibility(View.GONE);
        }

        if(intent.getIntExtra("Hour",1000)!=1000 && intent.getIntExtra("Minute",1000)!=1000){
            textView18.setText(intent.getIntExtra("Hour",1000)+"");
            textView21.setText(intent.getIntExtra("Minute",1000)+"");
        }else{
            textView18.setText("미설정                                ");
        }

        button6 = (Button) findViewById(R.id.button6);

        button = (Button) findViewById(R.id.button);
        button3 = (Button) findViewById(R.id.button3);
        button2 = (Button) findViewById(R.id.button2);

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify();
            }
        });

        listView = (ListView) findViewById(R.id.listView);

        scrollView = (ScrollView) findViewById(R.id.wheel);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        adapter = new ListAdapter();

        listView.setAdapter(adapter);

        showFeedbackList();

        container = (FrameLayout) findViewById(R.id.container);
        //피드백 추가버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFeedbackFragment();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2Clicked();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button3Clicked();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                feedbackItemClicked(position);
            }
        });

    }////////////on Create///////

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
            //database 아이템들의 _id를 reIndex 해줘야됨 먼저..
            //AppHelper.deleteFeedbackData(keyId, deleteList.get(i));
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

        //position : database내에서 해당 열의 번호
        int keyId = intent.getIntExtra("key",10000);

        //
        adapter.updateFeedback(keyId);

        //갯수 업데이트
        textView.setText(adapter.getCount()+"");
    }

    @Override
    public void onBackPressed() {
        if(container.getVisibility()==VISIBLE){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            container.setVisibility(View.GONE);
        }else{
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
                Intent intent2 = new Intent();
                setResult(RESULT_OK,intent2);
                finish();
            }
        }
    }

    public void createFeedbackFragment(){
        container.setVisibility(VISIBLE);
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment =new FeedbackWriteFragment()).commit();

    }

    public  void modify(){
        Intent intent2 = new Intent(getApplicationContext(), CreateActivity.class);
        int keyId = intent.getIntExtra("key",10000);

        intent2.putExtra("modify", "modify");
        intent2.putExtra("keyId",keyId);
        startActivityForResult(intent2,MODIFYING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MODIFYING) {

            if (resultCode == RESULT_OK) {
                //제대로 변경이 완료 되었을 시
                int keyId = data.getIntExtra("keyId",1000);

                textView16.setText(data.getStringExtra("dream"));
                AppHelper.updateTitleData(data.getStringExtra("dream"),keyId); //database에 다짐 저장

                if (data.getStringExtra("startYY") == null) {

                    textView3.setText("                                                                                   ");
                    textView5.setText("");
                    textView7.setText("");
                    textView10.setText("");
                    textView12.setText("");
                    textView14.setText("");

                    AppHelper.insertCalendarData("                                                                                   ", "",
                            "", "",
                            "", "", keyId);

                } else {

                    textView3.setText(data.getStringExtra("startYY"));
                    textView5.setText(data.getStringExtra("startMM"));
                    textView7.setText(data.getStringExtra("startDD"));
                    textView10.setText(data.getStringExtra("finishYY"));
                    textView12.setText(data.getStringExtra("finishMM"));
                    textView14.setText( data.getStringExtra("finishDD"));

                    //database에 저장되어있는 해당 다짐과 같은 열에 날짜정보를 업데이트
                    AppHelper.insertCalendarData(data.getStringExtra("startYY"), data.getStringExtra("startMM"),
                            data.getStringExtra("startDD"), data.getStringExtra("finishYY"),
                            data.getStringExtra("finishMM"), data.getStringExtra("finishDD"), keyId);
                }


                if (data.getStringExtra("Image") != null) {

                    //imageView.setImageURI(Uri.parse(data.getStringExtra("Image")));
                    Uri mUri = Uri.parse(data.getStringExtra("Image"));
                    //imageView.setVisibility(VISIBLE);
                    Glide.with(getApplicationContext()).load(mUri).into(imageView);

                    //database에 저장되어있는 해당 다짐과 같은 줄에 이미지정보를 업데이트
                    AppHelper.insertImageData(data.getStringExtra("Image"), keyId);
                }else{
                    AppHelper.insertImageData(null, keyId);
                    imageView.setImageResource(R.drawable.noimage);
                    //imageView.setVisibility(View.GONE);
                }

                if (data.getIntExtra("Hour", 1000) != 1000 && data.getIntExtra("Minute", 1000) != 1000) {

                    textView18.setText(data.getIntExtra("Hour", 1000)+"");
                    textView21.setText( data.getIntExtra("Minute", 1000)+"");

                    AlarmHATT myAlarm = new AlarmHATT(getApplicationContext());
                    myAlarm.Alarm(data.getIntExtra("Hour", 1), data.getIntExtra("Minute", 1),keyId,data.getStringExtra("dream"));
                    AppHelper.insertAlarmData(data.getIntExtra("Hour", 1000), data.getIntExtra("Minute", 1000), keyId);
                } else {
                    textView18.setText("미설정                                         ");

                    //알림을 끄면 알림데이터 삭제
                    AlarmHATT myAlarm = new AlarmHATT(getApplicationContext());
                    myAlarm.deleteAlarm(keyId,data.getStringExtra("dream"));

                    AppHelper.insertAlarmData(1000, 1000, keyId);
                }

            } else {
                Toast.makeText(getApplicationContext(), "변경이 취소되었습니다.", Toast.LENGTH_LONG).show();
            }
        }

    }

    public class AlarmHATT{
        private Context context;
        public AlarmHATT(Context context){
            this.context = context;
        }

        public void Alarm(int hour, int min, int keyId, String title){
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(getApplicationContext(),BroadcastD.class);

            intent.putExtra("keyId",keyId);
            intent.putExtra("title",title);

            PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(),keyId,intent,PendingIntent.FLAG_UPDATE_CURRENT);

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
            Toast.makeText(getApplicationContext(), "알림이 매일 "+hour+"시"+min+"분에 오게됩니다.", Toast.LENGTH_LONG).show();

        }

        public void deleteAlarm(int keyId,String title){
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(getApplicationContext(),BroadcastD.class);

            intent.putExtra("keyId",keyId);
            intent.putExtra("title",title);

            PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(),keyId,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(sender);
            //Toast.makeText(getApplicationContext(), title+"알림이 삭제되었습니다.", Toast.LENGTH_LONG).show();
        }

    }

    class ListAdapter extends BaseAdapter {
        ArrayList<FeedbackItem> items = new ArrayList<FeedbackItem>();
        FeedbackItemView[] view = new FeedbackItemView[1000];
        ArrayList<Integer> checkedList = new ArrayList<Integer>();
        public boolean VISIBLE = false;

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(FeedbackItem item){
            items.add(item);
        }

        public void dropItem(FeedbackItem item){
            items.remove(item);
        }

        public void updateFeedback(int keyId){
            int feedbackCount = items.size();

            for(int i=0;i<feedbackCount;i++){
                Log.d("AppHelper",items.get(i).getFeedback());
                AppHelper.insertFeedbackData(items.get(i).getFeedback(),keyId,i);
            }
            AppHelper.updateFeedbackCount(keyId,feedbackCount);
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
            view[position] = new FeedbackItemView(getApplicationContext());

            FeedbackItem item = items.get(position);

            view[position].setFeedback(item.getFeedback());

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

            return view[position];
        }
    }

    public void showFeedbackList(){
        int position = intent.getIntExtra("position",10000);

        String tableName = "dreamInfo";
        String sql ="select * from " + tableName;
        Cursor cursor = AppHelper.database.rawQuery(sql,null);

        for(int i=0; i <position+1;i++) {
            ///리스트뷰에서 해당 position에 cursor를 옮김
            cursor.moveToNext();
        }
        Log.d("AppHelper",cursor.getInt(11)+"");


        for(int i=0; i <cursor.getInt(11);i++){
            //feedback을 카운트해서 그 갯수만큼 additem해줌
            adapter.addItem(new FeedbackItem(cursor.getString(i+12)));
        }



        adapter.notifyDataSetChanged();
        cursor.moveToFirst();

        textView.setText(adapter.getCount()+"");
    }

    public void createFeedback(String text){
        onBackPressed();

        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR)+"";
        String month = calendar.get(Calendar.MONTH)+"";
        String day = calendar.get(Calendar.DATE)+"";

        adapter.addItem(new FeedbackItem(text));
        adapter.notifyDataSetChanged();

        textView.setText(adapter.getCount()+"");
    }

    public void insertFeedbackData(String feedback){
        int position = intent.getIntExtra("position",10000);
        int primaryKey = intent.getIntExtra("key",10000);

        String tableName = "dreamInfo";
        String sql ="select * from " + tableName;
        Cursor cursor2 = AppHelper.database.rawQuery(sql,null);
        cursor2.moveToFirst();
        for(int i=0; i <position ;i++) {
            cursor2.moveToNext();
        }
        Log.d("AppHelper",cursor2.getString(1));
        Log.d("AppHelper",cursor2.getInt(11)+"");
        int feedbackCount = cursor2.getInt(11);
        AppHelper.insertFeedbackData(feedback, primaryKey, feedbackCount);
    }

    public void feedbackItemClicked(int position){
        String feedback = adapter.items.get(position).getFeedback();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Feedback");
        builder.setMessage(feedback);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();

    }

    public void scrollRolling(){
        Log.d("AppHelper",scrollView.getScrollY()+"");
        scrollView.setScrollY(1189);
    }
}
