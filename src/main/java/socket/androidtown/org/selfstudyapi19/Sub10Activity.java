package socket.androidtown.org.selfstudyapi19;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Sub10Activity extends AppCompatActivity {

    public static final int REQUSET_CODE_MENU=201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub10);

        final EditText eT = (EditText)findViewById(R.id.editText);
        final EditText eT2 = (EditText)findViewById(R.id.editText2);

        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Sub11Activity.class);
                startActivityForResult(intent, REQUSET_CODE_MENU);
            }
        });

        Button data = (Button)findViewById(R.id.sendData);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String id = eT.getText().toString(); //editText의 값을 받아온다.
                    String pw = eT2.getText().toString(); //editText2의 값을 받아온다.
                    String result;

                    CustomTask task = new CustomTask();
                    result = task.execute(id,pw).get();
                    Log.i("리턴 값",result);

                    if(result.equals("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>StudyHelperSite</title></head></html>It is null value.</br>Login Success.")){
                        Toast.makeText(Sub10Activity.this, "로그인 성공.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(Sub10Activity.this, "로그인 실패. ID와 PW를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.i("DBtest","에러났넹..Error");
                }
            }
        });
    }
    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.130.102:8080/Server_Android/server.jsp"); //URL 주소
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // URL을 연결한 객체 생성
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//Post방식 통신 <-> Get방식
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());//output스트림 개방

                /*
                conn.setDoOutput(true); // 쓰기모드 지정
                conn.setDoInput(true); // 읽기모드 지정
                conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
                conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정
                strCookie = conn.getHeaderField("Set-Cookie"); //쿠키데이터 보관
                InputStream is = conn.getInputStream(); //input스트림 개방
                */

                sendMsg = "cid="+strings[0]+"&cpw="+strings[1];

                Log.i("통신결과",sendMsg+"입니다.\n"+strings[0]+"얘가 스트링즈0  "+strings[1]+"얘가1");
                osw.write(sendMsg);
                osw.flush();

                //jsp와 통신 성공 시 수행
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "EUC-KR");
                    BufferedReader reader = new BufferedReader(tmp); //문자열set 세팅
                    StringBuffer buffer = new StringBuffer(); //문자열을 닫기 위한 객체

                    //jsp에서 보낸 값을 받는 부분
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }

                    receiveMsg = buffer.toString();
                    Log.i("통신결과",receiveMsg+"이게 리시브 메세지 입니다.\n");
                } else {//통신실패
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }
}