package org.techtown.writeyourdreambyleeyongjun;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FeedbackItemView extends LinearLayout {

    //yyyy,mm,dd
    //TextView textView3;
    //TextView textView5;
    //TextView textView7;
    //feedback
    TextView textView;

    CheckBox checkBox;

    public FeedbackItemView(Context context) {
        super(context);
        init(context);
    }

    public FeedbackItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.feedback_item, this, true);

        textView = (TextView) findViewById(R.id.textView);
        //textView3 = (TextView) findViewById(R.id.textView3);
        //textView5 = (TextView) findViewById(R.id.textView5);
        //textView7 = (TextView) findViewById(R.id.textView7);

        checkBox = (CheckBox) findViewById(R.id.checkBox4);

    }

    public void setFeedback(String string){
        textView.setText(string);
    }
    public void setYYYY(String string){
        //textView3.setText(string);
    }
    public void setMM(String string){
        //textView5.setText(string);
    }
    public void setDD(String string){
        //textView7.setText(string);
    }

    public void checkVisible(){
        checkBox.setVisibility(VISIBLE);
    }

    public void checkGone(){
        checkBox.setVisibility(GONE);
    }
}
