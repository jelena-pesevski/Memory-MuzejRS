package org.unibl.etf.memory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LevelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        ImageView imageViewBunny = (ImageView) findViewById(R.id.imageViewBunny);
        ImageView imageViewBear = (ImageView) findViewById(R.id.imageViewBear);
        ImageView imageViewBambi = (ImageView) findViewById(R.id.imageViewBambi);

        new AnimationThread(imageViewBunny).start();
        new AnimationThread(imageViewBear).start();
        new AnimationThread(imageViewBambi).start();

        //Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        //imageViewBear.startAnimation(aniSlide);
    }

    public void startFirstLevel(View view){
        Intent intent=new Intent(LevelsActivity.this, GameActivity.class);

        intent.putExtra("numColumnsPortrait", 2);
        intent.putExtra("numRowsPortrait", 2);
        intent.putExtra("numColumnsLandscape", 2);
        intent.putExtra("numRowsLandscape", 2);

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


class AnimationThread extends Thread
{
    private ImageView imageView;

    public AnimationThread(){

    }

    public AnimationThread(ImageView imageViewArg){
        this.imageView = imageViewArg;
    }

    public void run(){
        Animation animationZoomIn = AnimationUtils.loadAnimation(imageView.getContext(), R.anim.zoom_in);
        Animation animationZoomOut = AnimationUtils.loadAnimation(imageView.getContext(), R.anim.zoom_out);

        while(true){
            imageView.startAnimation(animationZoomIn);

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            imageView.startAnimation(animationZoomOut);

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
