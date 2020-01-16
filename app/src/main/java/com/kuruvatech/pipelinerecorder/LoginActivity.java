package com.kuruvatech.pipelinerecorder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.client.Firebase;
import com.splunk.mint.Mint;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.util.ArrayList;
import com.kuruvatech.pipelinerecorder.utils.Constants;
import com.kuruvatech.pipelinerecorder.utils.SessionManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class LoginActivity extends AppCompatActivity {

    EditText un,pw;
    TextView error;
    Button ok;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Mint.initAndStartSession(this.getApplication(), "4c38f221");
        // Session Manager
        session = new SessionManager(getApplicationContext());
        session.isKill = false;
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        un=(EditText)findViewById(R.id.et_un);
        pw=(EditText)findViewById(R.id.et_pw);
        ok=(Button)findViewById(R.id.btn_login);
       // error=(TextView)findViewById(R.id.tv_error);
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (un.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pw.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                String response = null;
                try {
                       new JSONAsyncTask().execute(Constants.LOGIN_URL, un.getText().toString().trim().toLowerCase(),pw.getText().toString());
                } catch (Exception e) {
                    un.setText(e.toString());
                }

            }
        });

    }
    public  class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("email", urls[1]));
                postParameters.add(new BasicNameValuePair("password", urls[2]));
                postParameters.add(new BasicNameValuePair("role", "vendor"));
              //  postParameters.add(new BasicNameValuePair("uniqueid", uniqueId));

                HttpPost request = new HttpPost(urls[0]);
                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpClient httpclient = new DefaultHttpClient();
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                request.setEntity(formEntity);
                HttpResponse response = httpclient.execute(request);


                // StatusLine stat = response.getStatusLin;
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String data = EntityUtils.toString(entity);
                    if (data.equals("1")) {
                        session.createLoginSession("Pipeline_kuruva", urls[1],urls[1] );
                    }
                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            //adapter.notifyDataSetChanged();
            if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to login", Toast.LENGTH_LONG).show();
            else
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        session.isKill = true;
//        getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        MainActivity.this.finish();
    }
//    public void getHotelInfo()
//    {
//        String url =  Constants.GET_VENDOR_INFO + session.getEmail();
//        new GETJSONAsyncTask().execute(url);
//    }
//    public  class GETJSONAsyncTask extends AsyncTask<String, Void, Boolean> {
////
////        ProgressDialog dialog;
////
////        public  GETJSONAsyncTask()
////        {
////
////        }
////
////        @Override
////        protected void onPreExecute() {
////            super.onPreExecute();
////            super.onPreExecute();
////            dialog = new ProgressDialog(LoginActivity.this);
////            dialog.setMessage("Loading, please wait");
////            dialog.setTitle("Connecting server");
////            dialog.show();
////            dialog.setCancelable(false);
////        }
////
////        @Override
////        protected Boolean doInBackground(String... urls) {
////            try {
////
////                //------------------>>
////                HttpGet request = new HttpGet(urls[0]);
////                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
////                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
////                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
////                HttpClient httpclient = new DefaultHttpClient();
////                HttpResponse response = httpclient.execute(request);
////
////                // StatusLine stat = response.getStatusLine();
////                int status = response.getStatusLine().getStatusCode();
////
////                if (status == 200) {
////                    HttpEntity entity = response.getEntity();
////                    String data = EntityUtils.toString(entity);
////                    session.setHotelInfo(data);
////
////                    JSONArray jarray = new JSONArray(data);
////
////                    for (int i = 0; i < jarray.length(); i++) {
////                        JSONObject object = jarray.getJSONObject(i);
////
////                        if(object.has("isOpen")) {
////                            String isopen =object.get("isOpen").toString();
////                            session.setHotelopen(isopen);
////                        }
////                    }
////                    return true;
////                }
////            }  catch (IOException e) {
////                e.printStackTrace();
////            }catch (JSONException e) {
////                e.printStackTrace();
////            }
////            return false;
////        }
////
////        protected void onPostExecute(Boolean result) {
////            dialog.cancel();
////            if (result == false)
////                Toast.makeText(getApplicationContext(), "Unable to fetch add from server", Toast.LENGTH_LONG).show();
////            else
////            {
////               // startService(new Intent(getBaseContext(), NotificationListener.class));
////                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                startActivity(intent);
////                finish();
////            }
////
////        }
    //}
}
