package org.unibl.etf.memory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LevelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
    }

    public void startFirstLevel(View view){
        Intent intent=new Intent(LevelsActivity.this, GameActivity.class);
        startActivity(intent);
    }

    public void startScndLevel(View view){
        Intent intent=new Intent(LevelsActivity.this, GameActivity.class);
        startActivity(intent);
    }

    public void startThirdLevel(View view){
        Intent intent=new Intent(LevelsActivity.this, GameActivity.class);
        startActivity(intent);
    }
}
