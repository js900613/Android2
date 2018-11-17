package socket.androidtown.org.selfstudyapi19;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.widget.TextView;

import static java.lang.Thread.*;

public class TimerActivity extends AppCompatActivity {
    //Using the Accelometer & Gyroscoper
    private SensorManager mSensorManager = null;
    //Using the Gyroscope
    private SensorEventListener mGyroLis;
    private Sensor mGgyroSensor = null;

    TextView tv1;
    public static final int REQUSET_CODE_MENU=301;

    int count; //핸들러가 호출한 횟수를 누적할 count
    TimeThread thread; //스레드
    int sec, min, hour; //초,분을,시간 표시하기 위해
    //스탑워치의 상태값 정의하기
    final int READY = 0;
    final int RUNNING = 1;
    final int PAUSE = 2;
    final int STOP = 3;
    int Toggle = 0;
    //상태값을 저장할 변수
    int state = READY; //초기 상태
    ListView listView; //렙타임을 출력할 ListView 객체
    //버튼의 참조값을 얻어오기 위해
    Button pauseBtn, stopBtn;
    //각각의 자리수를 나타내는 변수 선언
    int h2, h1, mi2, mi1, s2, s1, m2, m1;
    //숫자 이미지 리소스 id 값을 담을 정수형 배열객체 생성하기
    int[] img = new int[10];
    //숫자의 ImageView 객체
    ImageView imgH2, imgH1, imgMi2, imgMi1, imgS2, imgS1, imgM2, imgM1;

    /*
    public class TimerActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_timer); }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timer);
        tv1 = (TextView) findViewById(R.id.textView11);
        pauseBtn = (Button) findViewById(R.id.pause);
        stopBtn = (Button) findViewById(R.id.stop);

        //이미지 리소스를 배열에 저장하기
        for (int i = 0; i < 10; i++) {
            img[i] = R.drawable.f00 + i;
        }
        //각 자리수를 구성하는 ImageView 객체의 참조값 얻어오기.
        imgH2 = (ImageView) findViewById(R.id.h2);
        imgH1 = (ImageView) findViewById(R.id.h1);
        imgMi2 = (ImageView) findViewById(R.id.mi2);
        imgMi1 = (ImageView) findViewById(R.id.mi1);
        imgS2 = (ImageView) findViewById(R.id.s2);
        imgS1 = (ImageView) findViewById(R.id.s1);
        imgM2 = (ImageView) findViewById(R.id.m2);
        imgM1 = (ImageView) findViewById(R.id.m1);

        //스레드 객체 생성
        thread = new TimeThread(handler);

        //Using the Gyroscope & Accelometer
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Using the Accelometer
        mGgyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGyroLis = new GyroscopeListener();
        mSensorManager.registerListener(mGyroLis, mGgyroSensor, SensorManager.SENSOR_DELAY_UI);
    }

    //버튼을 눌렀을때 실행되는 콜백 메소드
    public void push(View v) {
        switch (v.getId()) {
            case R.id.start: //시작 버튼을 눌렀을때
                //시간초기화
                resetTime();
                try {
                    //스레드 시작 시키기
                    stopBtn.setText("정지");
                    pauseBtn.setText("일시정지");
                    thread.start();
                } catch (Exception e) { //익셉션 발생하면
                    //스레드 정지
                    thread.stopForever();
                    //스레드 객체 다시 생성
                    thread = new TimeThread(handler);
                    //스레드를 시작한다.
                    thread.start();
                }
                //상태값 바꾸기
                state = RUNNING;
                break;
            case R.id.pause:
                if (state == RUNNING) {
                    //일시 정지 시켜야 한다.
                    //스레드를 일시 정지 시킨다.
                    thread.pauseNResume(true);
                    //버튼의 내용을 바꿔준다.
                    pauseBtn.setText("재시작");
                    //상태값을 바꿔준다.
                    state = PAUSE;
                } else if (state == PAUSE) {
                    //재 시작 시켜야한다.
                    thread.pauseNResume(false);
                    //버튼의 내용을 바꿔준다.
                    pauseBtn.setText("일시정지");
                    state = RUNNING;
                }
                break;
            case R.id.stop: //정지
                if (state == RUNNING) { //시간이 가고 있는 상태일때 정지할수 있도록한다.
                    //스레드를 정지 시킨다.
                    thread.stopForever();
                    //버튼의 내용을 바꾼다.
                    stopBtn.setText("리셋");
                    //상태값을 바꿔준다.
                    state = STOP;
                } else if (state == STOP) {
                    //STOP 상태에서 리셋버튼을 누르면 시간관련된 값을 초기화 시켜준다.
                    resetTime();
                    //버튼의 내용을 바꾼다.
                    stopBtn.setText("정지");
                    //시작할수 있도록 상태값을 바꿔준다.
                    state = READY;
                }
                break;
        }
    }

    //시간 초기화 하는 메소드
    public void resetTime() {
        count = 0;
        sec = 0;
        min = 0;
        h2 = 0;
        h1 = 0;
        mi2 = 0;
        mi1 = 0;
        s2 = 0;
        s1 = 0;
        m2 = 0;
        m1 = 0;

        //각각의 자리수를 나타내는 이미지 뷰의 리소스를 바꿔준다.
        imgM1.setImageResource(img[m1]);
        imgM2.setImageResource(img[m2]);
        imgS1.setImageResource(img[s1]);
        imgS2.setImageResource(img[s2]);
        imgMi1.setImageResource(img[mi1]);
        imgMi2.setImageResource(img[mi2]);
        imgH1.setImageResource(img[h1]);
        imgH2.setImageResource(img[h2]);
    }

    //스레드로 부터 메세지를 받을 핸들러 객체
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //카운트를 센다.
            count++;

            if (count == 100) {
                sec++; //초를 올리고
                count = 0;//카운트를 초기화 한다.
            }

            if (sec == 60) {
                min++; //분을 올리고
                sec = 0; //초를 초기화 한다.
            }

            if (min == 60) {
                hour++; //시간올리기
                min = 0;
            }

            //8자리로 출력하기 위해 관련처리하기

            m1 = count % 10; //10으로 나눈 나머지
            m2 = count / 10; //10으로 나눈 몫
            s1 = sec % 10;
            s2 = sec / 10;
            mi1 = min % 10;
            mi2 = min / 10;
            h1 = hour % 10;
            h2 = hour / 10;

            //각각의 자리수를 나타내는 이미지 뷰의 리소스를 바꿔준다.
            imgM1.setImageResource(img[m1]);
            imgM2.setImageResource(img[m2]);
            imgS1.setImageResource(img[s1]);
            imgS2.setImageResource(img[s2]);
            imgMi1.setImageResource(img[mi1]);
            imgMi2.setImageResource(img[mi2]);
            imgH1.setImageResource(img[h1]);
            imgH2.setImageResource(img[h2]);
        }
    };

    private class GyroscopeListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            /*Log.i("LOG", "GYROSCOPE           [X]:" + String.format("%.4f", event.values[0])
                    + "           [Y]:" + String.format("%.4f", event.values[1])
                    + "           [Z]:" + String.format("%.4f", event.values[2]));*/

            if(event.values[0]<-0.35||event.values[0]>0.3||event.values[1]<-0.4||event.values[1]>0.5||event.values[2]<-0.35||event.values[2]>0.21){
                Toggle=0;
                tv1.setText("폰을 내려두고 공부합시다... \n잠시 후 다시 시작");
                thread.pauseNResume(true);
                Toggle++;
            }

            else if(Toggle==1) {
                try {
                    sleep(1000);
                    Toggle++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            else if(Toggle==2){
                tv1.setText("열공중...");
                thread.pauseNResume(false);
                Toggle=0;
            }



            /*if (event.values[0]<-0.35||event.values[0]>0.3) {
                tv1.setText("x축 값의 범위를 벗어남 = 움직였지!!?");
            }
            else if (event.values[1]<-0.4||event.values[1]>0.5) {
                tv1.setText("y축 값의 범위를 벗어남 = 움직였지??!!");
            }
            else if (event.values[2]<-0.35||event.values[2]>0.21) {
                tv1.setText("z축 값의 범위를 벗어남 = 움직였지??!!");
            }
            else {
                tv1.setText("열공중...");
            }*/
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }
}