package com.example.samridhamla06.faptitude_mongo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by samridhamla06 on 03/12/15.
 */
public class MyAdapter extends ArrayAdapter<Question> {

    private TextView caption;
    private TextView insert_user;
    private Context context;


    private List<Question> questionList;

    public MyAdapter(Context context, List<Question> questionList) {
        super(context,R.layout.mytextview, questionList);

        this.questionList = questionList;
        this.context = context;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.mytextview,null,false);//to add some layout to parent

        caption = (TextView)rowView.findViewById(R.id.textView2);
        insert_user = (TextView)rowView.findViewById(R.id.textView3);

        caption.setText(questionList.get(position).getCaption());
        insert_user.setText(questionList.get(position).getInsert_user());
        return rowView;
    }
}

