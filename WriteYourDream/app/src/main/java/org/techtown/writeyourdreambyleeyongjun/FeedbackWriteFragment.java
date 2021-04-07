package org.techtown.writeyourdreambyleeyongjun;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class FeedbackWriteFragment extends Fragment {

    EditText editText;
    Button button;
    Button button2;

    ContentActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_feedback_write,container,false);


        editText = (EditText) rootView.findViewById(R.id.editText);
        button = (Button) rootView.findViewById(R.id.button4);
        button2 = (Button) rootView.findViewById(R.id.button5);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Log.d("AppHelper","hi");
                    ((ContentActivity) getActivity()).scrollRolling();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                String year = calendar.get(Calendar.YEAR)+"";
                String month = calendar.get(Calendar.MONTH)+1+"";
                String day = calendar.get(Calendar.DATE)+"";

                ContentActivity activity = (ContentActivity) getActivity();

                String feedback = year+"."+month+"."+day+". "+editText.getText()+"";
                activity.createFeedback(feedback);
                activity.insertFeedbackData(feedback);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        return rootView;
    }

    public void cancel(){
        getActivity().onBackPressed();
    }
}
