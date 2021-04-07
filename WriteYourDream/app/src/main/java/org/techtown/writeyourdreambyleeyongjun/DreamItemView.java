package org.techtown.writeyourdreambyleeyongjun;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class DreamItemView extends LinearLayout {

    LinearLayout linearLayout;

    TextView textView;
    TextView textView3;
    TextView textView5;
    TextView textView7;
    TextView textView10;
    TextView textView12;
    TextView textView14;
    ImageView imageView;

    CheckBox checkBox;

    ScrollView scrollView;

    public DreamItemView(Context context) {
        super(context);

        init(context);
    }

    public DreamItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.dream_item,this,true);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        textView = (TextView) findViewById(R.id.textView);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView10 = (TextView) findViewById(R.id.textView10);
        textView12 = (TextView) findViewById(R.id.textView12);
        textView14 = (TextView) findViewById(R.id.textView14);

        imageView = (ImageView) findViewById(R.id.imageView);

        checkBox = (CheckBox) findViewById(R.id.checkBox4);

        //scrollView = (ScrollView) findViewById(R.id.scrollview);

    }

    public void setDream(String string){
        textView.setText(string);
    }
    public void setStartYY(String string){
        textView3.setText(string);
    }
    public void setStartMM(String string){
        textView5.setText(string);
    }
    public void setStartDD(String string){
        textView7.setText(string);
    }
    public void setFinishYY(String string){
        textView10.setText(string);
    }
    public void setFinishMM(String string){
        textView12.setText(string);
    }
    public void setFinishDD(String string){
        textView14.setText(string);
    }
    public void setImageView(String string){
        if(string!=null){
            //imageView.setImageURI(Uri.parse(string));

            Uri mUri = Uri.parse(string);
            Glide.with(getContext()).load(mUri).into(imageView);
        }

    }

    public void autoScroll(){
        scrollView.fullScroll(ScrollView.FOCUS_RIGHT);
    }
    public void checkVisible(){
        checkBox.setVisibility(VISIBLE);
    }

    public void checkGone(){
        checkBox.setVisibility(GONE);
    }
}
