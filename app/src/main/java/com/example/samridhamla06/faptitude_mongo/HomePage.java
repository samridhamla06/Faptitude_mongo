package com.example.samridhamla06.faptitude_mongo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class HomePage extends AppCompatActivity {

    private Question question;
    private ListView listView;
    public static Socket socket;
    private String caption;
    private JSONArray myJsonArray;
    private List<Question> questionList;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        listView = (ListView)findViewById(R.id.listView1);
        final String URL = "http://192.168.2.2:3000/hello";
        questionList = new ArrayList<>();
        try {
            initSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }



// pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Toast.makeText(HomePage.this,"Connection successfull",Toast.LENGTH_LONG).show();
                            myJsonArray = response.getJSONArray("questions");
                            addJSONToListView(myJsonArray);
                          //  initSocket();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", "volley error madrchod");
            }
        });

        Volley.newRequestQueue(this).add(req);
        //checkIntentStatus();
       Intent receivedNewQsIntent = getIntent();
        System.out.println("----------IST STEP-----------------IST STEP--------");
        if(receivedNewQsIntent!=null && receivedNewQsIntent.hasExtra("caption")){
            System.out.println("----------2ND STEP-----------------2ND STEP--------");
            Log.d("receivedNewQsIntent",receivedNewQsIntent.getStringExtra("caption"));
            caption = receivedNewQsIntent.getStringExtra("caption");
            String host = "http://192.168.2.4:3000";
            try {
                socket = IO.socket(host);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            socket.emit("Question-Added",caption);
        }

        myAdapter = new MyAdapter(HomePage.this,questionList);
        listView.setAdapter(myAdapter);
    }

    private void checkIntentStatus() throws URISyntaxException {
        Intent receivedNewQsIntent = getIntent();

        if(receivedNewQsIntent!=null && receivedNewQsIntent.hasExtra("caption")){
            System.out.println(receivedNewQsIntent.getExtras().getString("caption"));
            caption = receivedNewQsIntent.getExtras().getString("caption");
            String host = "http://192.168.2.4:3000";
            socket = IO.socket(host);
            socket.emit("Question-Added", caption);
        }else
            return;
    }

    private void addJSONToListView(JSONArray arr) throws JSONException {
        Log.d("json_arr", arr.toString());
        for(int i=0;i<arr.length();i++){

            JSONObject obj = arr.getJSONObject(i);

            if(obj!=null) {
                questionList.add(convertJSONToObject(obj));
                System.out.println("my retrieved object is :" + convertJSONToObject(obj).toString());
                myAdapter.notifyDataSetChanged();
            }
        }
        Log.d("ques_list : ", questionList.toString());
    }

    public void onClickingAdd(View view){
        Intent myIntent = new Intent(this,AddQuestion.class);
        startActivity(myIntent);

    }

    private Question convertJSONToObject(JSONObject jsonObject) throws JSONException {
        int qid = jsonObject.getInt("qid");
        String desc = jsonObject.getString("desc");
        int image_id = jsonObject.getInt("image_id");
        String caption = jsonObject.getString("caption");
        String insert_user = jsonObject.getString("insert_user");

        return new Question(qid,desc,image_id,caption,insert_user);
    }
    private void initSocket() throws URISyntaxException {
        String host = "http://192.168.2.2:3000";
        socket = IO.socket(host);
        socket.on("chat-message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                HomePage.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            Log.d("received", data.getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        socket.on("Question-Added", new Emitter.Listener() {

            @Override
            public void call(final Object... args) {
                HomePage.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Welcome to Question Added");
                        String receivedCaption = (String) args[0];
                        Log.d("New Question is", receivedCaption);
                        questionList.add(new Question(10, "kuch bhi", 10, receivedCaption, "shamala"));
                        myAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        socket.emit("message", "Sam is in");
        socket.connect();

    }
}
