package org.techtown.writeyourdreambyleeyongjun;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity {

    ///////////////////버리는 액티비티///////////////

    ListView listView;
    ListAdapter adapter;

    //title
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ///////////////////버리는 액티비티///////////////

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent intent = getIntent();
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(intent.getStringExtra("다짐"));


        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListAdapter();

        listView.setAdapter(adapter);

        //adapter.addItem(new FeedbackItem("2019","08","16","아자아자 화이팅"));
        //adapter.addItem(new FeedbackItem("2019","08","17","아자아자 화이팅2"));
        adapter.notifyDataSetChanged();
    }


    class ListAdapter extends BaseAdapter {
        ArrayList<FeedbackItem> items = new ArrayList<FeedbackItem>();
        FeedbackItemView[] view = new FeedbackItemView[500];
        ArrayList<Integer> checkedList = new ArrayList<Integer>();
        public boolean VISIBLE = false;


        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(FeedbackItem item){
            items.add(item);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            view[position] = new FeedbackItemView(getApplicationContext());

            FeedbackItem item = items.get(position);


            view[position].setFeedback(item.getFeedback());
            view[position].setYYYY(item.getYear());
            view[position].setMM(item.getMonth());
            view[position].setDD(item.getDay());


            return view[position];
        }
    }
}
