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
import com.android.volley.toolbox.JsonArrayRequest;
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
    final String host = "http://192.168.2.5:3000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        listView = (ListView)findViewById(R.id.listView1);
        final String URL = "http://192.168.2.5:3000/";
        questionList = new ArrayList<>();
        try {
            initSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }



// pass second argument as "null" for GET requests

        JsonArrayRequest getRequest = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("length of get response is " + response.length());
                        myJsonArray = (JSONArray)response;
                        try {
                            addJSONToListView(myJsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        Volley.newRequestQueue(this).add(getRequest);

        myAdapter = new MyAdapter(HomePage.this,questionList);
        listView.setAdapter(myAdapter);
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
                        JSONObject data = (JSONObject) args[0];
                        Log.d("New Question is", data.toString());
                        try {
                            System.out.println("New Question conversion is " + convertJSONToObject(data).toString());
                            questionList.add(convertJSONToObject(data));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        myAdapter.notifyDataSetChanged();
                        System.out.println(" Question is now Added");
                    }
                });
            }
        });
        socket.emit("message", "Sam is in");
        socket.connect();

    }
}
