package socket.androidtown.org.selfstudyapi19;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/*private class WordbaseHelper extends SQLiteOpenHelper{
    public WordbaseHelper(Context context){
        super(context, FinalDataBase, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        println("creating table ["+TABLE_NAME + "].");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}*/


public class MainActivity extends AppCompatActivity {
    public static final int REQUSET_CODE_MENU = 101;

    /*public void onButtonClicked(View v){
        Intent intent = new Intent(getApplicationContext(), Sub10Activity.class);
        startActivityForResult(intent, REQUSET_CODE_MENU);
    }*/

    SQLiteDatabase db;
    String dbName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Sub10Activity.class);
                startActivityForResult(intent, REQUSET_CODE_MENU);
            }

        });
        //View imageView3 = (View) findViewById(R.id.imageView3);
        //View.OnClickListener(imageView3);

        //textView : 영어학습
        View textView = (View) findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*dbName = "newdatabase";
                db=openOrCreateDatabase(dbName,MODE_WORLD_WRITEABLE,null);
*/
                /*ArrayList<EnglishData> list1 = new ArrayList<>();
                for (int i = 0 ;i < 5; i++){
                    EnglishData data = new EnglishData();
                    data.setWord();
                    data.setMean();
                    list1.add();
                }*/
                Intent intent = new Intent(getApplicationContext(), Sub20Activity.class);
                startActivityForResult(intent, REQUSET_CODE_MENU);
            }
        });

        //textView2 : 타이머
        View textView2 = (View) findViewById(R.id.textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                startActivityForResult(intent, REQUSET_CODE_MENU);
            }

        });



        /*CustomTask task = new CustomTask();
        //execute의 매개값은
        //sendMsg = "id="+strings[0]+"&pwd="+strings[1];
        //doInBackround에서 사용된 문자열 배열에 필요한 값을 넣습니다.
        task.execute("rain483", "1234");
        // 그리고 jsp로 부터 리턴값을 받아야할 때는
        //String returns = task.execute("rain483","1234").get();
        //처럼 사용하시면 되는데요. get()에서 에러가 발생할 수 있어서 try catch문으로
        //감싸야에러가 나지 않습니다.
        */
    }
}

/*    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://localhost:8080/AndroidSocket/server.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&pwd="+strings[1];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
                //회원가입처럼 보낼 데이터가 여러 개일 경우 &로 구분하여 작성합니다.
                osw.write(sendMsg);//OutputStreamWriter에 담아 전송합니다.
                osw.flush();
                //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    //jsp에서 보낸 값을 받겠죠?
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                    // 통신이 실패했을 때 실패한 이유를 알기 위해 로그를 찍습니다.
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //jsp로부터 받은 리턴 값입니다.
            return receiveMsg;
        }
    }
}*/









/*


  //안드로이드에서 소켓 클라이언트로 연결하는 방법에 대해 알 수 있습니다.

 // @author Mike


public class MainActivity extends AppCompatActivity {

    EditText input01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input01 = (EditText) findViewById(R.id.editText);

        // 버튼 이벤트 처리
        Button button01 = (Button) findViewById(R.id.button);
        button01.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String addr = input01.getText().toString().trim();
                Log.d("주소","messege"+addr);
                ConnectThread thread = new ConnectThread(addr);
                thread.start();
            }
        });

    }


     // 소켓 연결할 스레드 정의

    class ConnectThread extends Thread {
        String hostname;

        public ConnectThread(String addr) {
            hostname = addr;
        }

        public void run() {

            try {
                int port = 11001;
                //hostname = "http://192.168.120.87:8080/AndroidSocket/server.jsp";
                Socket sock = new Socket(hostname, port);
                ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
                outstream.writeObject("Hello AndroidTown on Android");
                outstream.flush();

                ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
                String obj = (String) instream.readObject();

                Log.d("MainActivity", "서버에서 받은 메시지 : " + obj);

                sock.close();

            } catch(Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}




*/