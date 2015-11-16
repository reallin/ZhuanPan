package com.example.linxj.surfacezhuanpan;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.linxj.zhuanView.*;

public class MainActivity extends AppCompatActivity {
    ImageView btnStart;
    com.example.linxj.zhuanView.SufaceViewTemplate mLuckyPanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (ImageView)super.findViewById(R.id.id_start_btn);
        mLuckyPanView = (com.example.linxj.zhuanView.SufaceViewTemplate) findViewById(R.id.zhuanpan);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mLuckyPanView.Start();
                if (!mLuckyPanView.isStart())
                {
                    btnStart.setImageResource(R.drawable.stop);
                    mLuckyPanView.Start();
                    //mLuckyPanView.luckyStart(1);
                } else
                {
                    if (!mLuckyPanView.isShouldEnd())

                    {
                        btnStart.setImageResource(R.drawable.start);
                        mLuckyPanView.luckyEnd();
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
