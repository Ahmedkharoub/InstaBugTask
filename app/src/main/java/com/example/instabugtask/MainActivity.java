package com.example.instabugtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText enterUrl, enterBody;
    RadioButton postButton, getButton;
    ImageButton addRow,removeRow;
    private ArrayList<headerKey> header;
    RecyclerView recyclerView;
    private HeaderAdapter headerAdapter;
    FloatingActionButton send;
    Boolean wifiConnection = false;
    Boolean mobileConnection = false;
    TextView offlineMessage;
    ScrollView parentView;
    int responseCode;
    String values = "";
    String values2 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        header = new ArrayList<headerKey>();
        initRecyclerView();

        enterUrl = findViewById(R.id.edit_enter_url);


        enterBody = findViewById(R.id.edit_enter_body);
        enterBody.setVisibility(View.INVISIBLE);


        NetworkConnection networkConnection = new NetworkConnection(
                this.getApplicationContext());

        offlineMessage = findViewById(R.id.offline_message);
        offlineMessage.setVisibility(View.INVISIBLE);

        parentView = findViewById(R.id.parent_view);
        parentView.setVisibility(View.VISIBLE);

        postButton = findViewById(R.id.radio_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                if (checked){
                    enterBody.setVisibility(View.VISIBLE);
                }
            }
        });
        getButton = findViewById(R.id.radio_get);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterBody.setVisibility(View.INVISIBLE);
            }
        });

        addRow = findViewById(R.id.add_row);
        addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              headerKey headerKey = new headerKey("","");
              header.add(headerKey);
              headerAdapter.notifyDataSetChanged();
            }
        });

        send = findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
               if (!networkConnection.isInternetAvailble()){
                    Toast.makeText(MainActivity.this, "Your Device NoT Connected", Toast.LENGTH_SHORT).show();
                }else {
                   Boolean isGet = getButton.isChecked();
                   String url = enterUrl.getText().toString();
                   if (postButton.isChecked()) {
                       String body = enterBody.getText().toString();
                   }


                   new AsyncTask<String, Integer, String>() {

                       @SuppressLint("StaticFieldLeak")
                       @Override
                       protected String doInBackground(String... strings) {
                           URL url = null;
                           String content = "", line = "";


                           try {
                               url = new URL(strings[0]);
                               HttpURLConnection connection = null;
                               connection = (HttpURLConnection) url.openConnection();

                               if (isGet){
                                   connection.setRequestMethod("GET");
                               }else {
                                   connection.setRequestMethod("POST");
                                   connection.setRequestProperty("Content-Type", "application/json");
                                   connection.setRequestProperty("Accept", "application/json");
                                   connection.setDoOutput(true);

                                   try(OutputStream os = connection.getOutputStream()) {
                                       byte[] input =  enterBody.getText().toString().getBytes("utf-8");
                                       os.write(input, 0, input.length);
                                   } catch (IOException ioException) {
                                       ioException.printStackTrace();
                                   }
                               }

                               for (Map.Entry<String, List<String>> entries : connection.getHeaderFields().entrySet()) {

                                   for (String value : entries.getValue()) {
                                       values += entries.getKey()  + "," + "\n"+ value ;


                                   }
                               }


                               Map<String, List<String>> map = connection.getHeaderFields();

                               for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                                   values2 += entry.getKey()  + "," + "\n"+ entry.getValue();
                               }


                               connection.setConnectTimeout(5000);
                               connection.setReadTimeout(5000);
                               for (headerKey headerKey : header) {
                                   connection.setRequestProperty(headerKey.getKey(),headerKey.getValue());

                               }
                               connection.connect();
                               BufferedReader rd = null;
                               rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                               while (true) {
                                   try {
                                       if (!((line = rd.readLine()) != null))
                                           break;
                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   }
                                   content += line + "\n";
                               }
                              responseCode = connection.getResponseCode();
                           } catch (ProtocolException protocolException) {
                               protocolException.printStackTrace();
                           } catch (MalformedURLException malformedURLException) {
                               malformedURLException.printStackTrace();
                           } catch (IOException ioException) {
                               ioException.printStackTrace();
                           }

                           return content;
                       }

                           protected void onProgressUpdate(Integer... progress) {
                       }

                       protected void onPostExecute(String result) {
                           Intent intent = new Intent(getApplicationContext(), Displaying.class);
                           Boolean isGet = getButton.isChecked();
                           String display = "url: " + enterUrl.getText().toString() + "\n" + "Type: ";
                           if (isGet) display += "get"; else display += "post";
                           display += "\n";
                           if (!isGet) display += "post= " + enterBody.getText().toString() ;
                           display +="\n";
                           for (headerKey headerKey : header) {
                               display += "key: " + headerKey.getKey() + " Value: "+ headerKey.getValue();
                           }
                           display +="\n";
                           display +="RespondBody: " + result;
                           display +="\n";

                           display += "ResponseCode: "+ responseCode;
                           display +="\n";

                           display +="RequestHeader: "+ values;
                           display +="\n";

                           display +="ResponseHeader: "+ values2;
                           display +="\n";

                           System.out.println("value" + values2);


                           intent.putExtra("display", display);

                           startActivity(intent);
                       }
                   }.execute(url);

               }

            }
        });
    }



    public void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        headerAdapter = new HeaderAdapter(header);
        recyclerView.setAdapter(headerAdapter);
    }

}

