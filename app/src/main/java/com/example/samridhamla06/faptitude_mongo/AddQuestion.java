package com.example.samridhamla06.faptitude_mongo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class AddQuestion extends AppCompatActivity {
    private String caption;
    private EditText editText;
    //private Socket socket;
    final String URL = "http://192.168.2.5:3000/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        editText = (EditText)findViewById(R.id.caption);



    }

    public void enterCaption(View view){
        Intent intent = new Intent(this,HomePage.class);
        caption = editText.getText().toString();
        try {
            emitEvent(caption);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //intent.putExtra("caption",caption);
        //startActivity(intent);
    }

    private void emitEvent(String caption) throws URISyntaxException, JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("qid",10);
        jsonObject.put("desc","kuchbhi---kuchbhi");
        jsonObject.put("image_id",10);
        jsonObject.put("caption",caption);
        jsonObject.put("insert_user",10);
        HomePage.socket.emit("Question-Added", jsonObject);

    }
}
