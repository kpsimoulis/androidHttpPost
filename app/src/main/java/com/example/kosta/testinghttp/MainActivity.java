package com.example.kosta.testinghttp;
        import android.app.Activity;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import org.apache.http.HttpResponse;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;

        import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import java.io.OutputStreamWriter;
        import java.io.UnsupportedEncodingException;
        import java.net.URL;

        import javax.net.ssl.HttpsURLConnection;
        import java.net.HttpURLConnection;
        import java.util.ArrayList;
        import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button login;

    private EditText userName, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    private boolean CheckFields(){
        String testUser , testPass;
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        testUser = userName.getEditableText().toString();
        testPass = password.getEditableText().toString();
        if(testUser.isEmpty() || testPass.isEmpty()||testUser.contentEquals("")||testPass.contentEquals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        String givenUsername = userName.getEditableText().toString();
        String givenPassword = password.getEditableText().toString();
        if(v.getId() == R.id.login){
            if(CheckFields()){
                sendPostRequest(givenUsername, givenPassword);
            }

        }
    }

//    private void sendPostRequest(String givenUsername, String givenPassword) {
//
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//
//            private Exception exception;
//
//            protected String doInBackground(String... params) {
//                String paramUsername = params[0];
//                String paramPassword = params[1];
//                try {
//                    URL url = new URL("http://10.0.2.2:1337/user/create");
////                    URL url = new URL("https://www.google.ca");
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//                    conn.setReadTimeout(10000);
//                    conn.setConnectTimeout(15000);
//                    conn.setRequestMethod("POST");
//                    conn.setDoInput(true);
//                    conn.setDoOutput(true);
//
//                    Uri.Builder builder = new Uri.Builder()
//                            .appendQueryParameter("name", paramUsername)
//                            .appendQueryParameter("secondParam", paramPassword)
//                            .appendQueryParameter("thirdParam", "testParam");
//                    String query = builder.build().getEncodedQuery();
//
//                    OutputStream os = conn.getOutputStream();
//                    BufferedWriter writer = new BufferedWriter(
//                            new OutputStreamWriter(os, "UTF-8"));
//                    writer.write(query);
//                    writer.flush();
//                    writer.close();
//                    os.close();
//
//                    conn.connect();
//
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//                if(result.equals("OK")){
//                    Toast.makeText(getApplicationContext(), "HTTP POST is working...", Toast.LENGTH_LONG).show();
//                }else{
//                    Toast.makeText(getApplicationContext(), "Invalid POST req...", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//
//        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
//        sendPostReqAsyncTask.execute(givenUsername, givenPassword);
//
//        }




    private void sendPostRequest(String givenUsername, String givenPassword) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];
                String paramPassword = params[1];
                System.out.println("*** doInBackground ** paramUsername " + paramUsername + " paramPassword :" + paramPassword);
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://10.0.2.2:1337/user/create");
                BasicNameValuePair usernameBasicNameValuePair = new BasicNameValuePair("name", paramUsername);
                BasicNameValuePair passwordBasicNameValuePAir = new BasicNameValuePair("password", paramPassword);
                List nameValuePairList = new ArrayList();
                nameValuePairList.add(usernameBasicNameValuePair);
                nameValuePairList.add(passwordBasicNameValuePAir);
                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while((bufferedStrChunk = bufferedReader.readLine()) != null){
                            stringBuilder.append(bufferedStrChunk);
                        }
                        return stringBuilder.toString();
                    } catch (ClientProtocolException cpe) {
                        System.out.println("First Exception cause of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
                        System.out.println("Second Exception cause of HttpResponse :" + ioe);
                        ioe.printStackTrace();
                    }
                } catch (UnsupportedEncodingException uee) {
                    System.out.println("An Exception given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(result.equals("OK")){
                    Toast.makeText(getApplicationContext(), "HTTP POST is working...", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid POST req...", Toast.LENGTH_LONG).show();
                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(givenUsername, givenPassword);

    }


}