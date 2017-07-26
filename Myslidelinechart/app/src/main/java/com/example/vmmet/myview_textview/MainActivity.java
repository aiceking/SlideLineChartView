package com.example.vmmet.myview_textview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private SlideLineChartView slideLineChartView;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slideLineChartView =(SlideLineChartView)findViewById(R.id.myview);
        btn=(Button)findViewById(R.id.mybtn);
        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mybtn:
                HashMap<String,String > map=new HashMap<>();
                for (int i=0;i<100;i++){
                    //map.put((float)Math.random()*5+"",(float) Math.random()*70+"");
                    map.put((float)i+1+"",(float) Math.random()*70+"");
                }
                slideLineChartView.setPoints(map);
                break;
        }
    }
}
