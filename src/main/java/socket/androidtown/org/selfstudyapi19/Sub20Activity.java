package socket.androidtown.org.selfstudyapi19;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class Sub20Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub20);


        Resources res = getResources();
        String[] arrString = res.getStringArray(R.array.word);
        String[] arrString2 = res.getStringArray(R.array.mean);
        List<String> mArrayLst= new ArrayList<String>();
        List<String> mArrayLst2= new ArrayList<String>();
        for(String s:arrString){
            mArrayLst.add(s);
        }

        for(String m:arrString2){
            mArrayLst2.add(m);
        }

        EditText wordText = findViewById(R.id.editText3);
        EditText meanText = findViewById(R.id.editText4);
        EditText meanText2 = findViewById(R.id.editText7);

        wordText.setText(mArrayLst.get(0));
        meanText.setText(mArrayLst2.get(0)); // 나중에 랜덤으로
        meanText2.setText(mArrayLst2.get(1));





    }
}
