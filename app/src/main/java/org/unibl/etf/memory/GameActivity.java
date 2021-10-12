package org.unibl.etf.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private List<ImageView> imageViews = new ArrayList<ImageView>();
    //info za svaki imageView da li je vec clicked
    private List<Boolean> clickedImageViews = new ArrayList<>();


    private void loadImageViews(){
        imageViews.clear();
        clickedImageViews.clear();

        imageViews.add(imageView1);
        clickedImageViews.add(false);
    }

    private ImageView imageView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imageView1 = (ImageView) findViewById(R.id.imageView1);


        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAnimation(imageView1, 0, "ic_bird2", "ic_bird1");
            }
        });


        loadImageViews();
    }



    private void makeAnimation(ImageView imageViewForChanging,int imageViewId, String frontImagePath, String backImagePath){
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageViewForChanging, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imageViewForChanging, "scaleX", 0f, 1f);

        oa1.setDuration(300);
        oa2.setDuration(300);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                String pathImageToShow = "";

                super.onAnimationEnd(animation);
                if(clickedImageViews.get(imageViewId) == false){
                    pathImageToShow = frontImagePath;
                }
                else{
                    pathImageToShow = backImagePath;
                }

                Context context = imageViewForChanging.getContext();
                int id = context.getResources().getIdentifier(pathImageToShow, "drawable", context.getPackageName());
                imageViewForChanging.setImageResource(id);

                oa2.start();

                Boolean newValue = !clickedImageViews.get(imageViewId);
                clickedImageViews.set(imageViewId, newValue);
            }
        });

        oa1.start();
    }
}