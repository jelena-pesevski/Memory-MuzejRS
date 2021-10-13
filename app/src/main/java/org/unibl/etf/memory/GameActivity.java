package org.unibl.etf.memory;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.unibl.etf.memory.Adapters.GridViewAdapter;
import org.unibl.etf.memory.Database.MemoryCard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;



public class GameActivity extends AppCompatActivity {

    private int numColumnsVerical, numRowsVertical, numColumnsLandscape, numRowsLandscape;

    private List<MemoryCard> memoryCards = new ArrayList<MemoryCard>();

    //info za svaki imageView da li je vec clicked
    private List<Boolean> clickedImageViews;// = new ArrayList<>();

    GridView gridView;
    int[] images;// = {R.drawable.ic_bird1, R.drawable.ic_bird2, R.drawable.ic_bird1, R.drawable.ic_bird2, R.drawable.ic_bird1};;


    ImageView firstImageViewSelected = null, secondImageViewSeelected = null;
    MemoryCard firstMemoryCard = null, secondMemoryCard = null;
    int firstMemoryCardClickedIndex = -1, secondMemoryCardClickedIndex = -1;

    private void loadImageViews(){

        //images = new {R.drawable.ic_bird1, R.drawable.ic_bird2};
        memoryCards.add(new MemoryCard(0, "aaa", "ic_apple", "opis 1"));
        memoryCards.add(new MemoryCard(1, "bbb", "ic_bird2", "opis 2"));
        memoryCards.add(new MemoryCard(0, "aaa", "ic_apple", "opis 1"));
        memoryCards.add(new MemoryCard(1, "bbb", "ic_bird2", "opis 2"));

        images = new int[memoryCards.size()];

        int br=0;
        for(MemoryCard m : memoryCards){
            int id = GameActivity.this.getResources().getIdentifier(m.getPath(), "drawable", GameActivity.this.getPackageName());
            images[br++] = id;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        numColumnsVerical = intent.getIntExtra("numColumnsVerical", 2);
        numRowsVertical = intent.getIntExtra("numRowsVertical", 2);
        numColumnsLandscape = intent.getIntExtra("numColumnsLandscape", 2);
        numRowsLandscape = intent.getIntExtra("numRowsLandscape", 2);

        int numColumns = numColumnsVerical, numRows = numRowsVertical;

        //get screen size
        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        double padding = 9;

        int sizeOfCard = (int)(screenWidth/numColumns - 2*numColumns*padding);

        gridView = (GridView) findViewById(R.id.gridViewImages);
        gridView.setNumColumns(numColumns);

        loadImageViews();

        clickedImageViews = new ArrayList<>();
        int[] backroundImages = new int[numColumns*numRows];
        for(int i = 0;i<numColumns*numRows;i++){
            clickedImageViews.add(false);
            backroundImages[i] = R.drawable.ic_banana;
        }


        GridViewAdapter gridViewAdapter = new GridViewAdapter(this, backroundImages, sizeOfCard, sizeOfCard);
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    //if niz[i] == true (vec kliknut)

                Log.d("aa", "Test*************************************************** "+clickedImageViews.get(i));
                //prvi put kliknuta
                if(clickedImageViews.get(i) == false){
                    Log.d("aa", "ANIMACIJA"+clickedImageViews.get(i));
                    flipImageAnimation((ImageView) view,  String.valueOf(images[i]));



                    clickedImageViews.set(i, true);
                    ((ImageView) view).setClickable(false);

                    if(firstImageViewSelected == null){
                        firstMemoryCardClickedIndex = i;
                        firstImageViewSelected = (ImageView) view;
                        firstMemoryCard = memoryCards.get(i);

                        firstImageViewSelected.setClickable(false);
                    }
                    else {
                        secondMemoryCardClickedIndex = i;
                        secondImageViewSeelected = (ImageView) view;
                        secondMemoryCard = memoryCards.get(i);



                        //provjeri poklapanje
                        if(firstMemoryCard.getMemoryCard_id() == secondMemoryCard.getMemoryCard_id()){
                            firstImageViewSelected.setClickable(false);
                            secondImageViewSeelected.setClickable(false);

                            Toast.makeText(getApplicationContext(), "Cestitam", Toast.LENGTH_SHORT).show();

                        }
                        else{

                            Log.d("aa", "Prije");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Log.d("aa", "Poslije");

                            //rotiranje
                            flipImageAnimation(firstImageViewSelected, "ic_banana");
                            flipImageAnimation(secondImageViewSeelected, "ic_banana");

                            clickedImageViews.set(firstMemoryCardClickedIndex, false);
                            clickedImageViews.set(secondMemoryCardClickedIndex, false);

                            firstImageViewSelected.setClickable(true);
                            secondImageViewSeelected.setClickable(true);
                        }

                        //false
                        firstImageViewSelected = null;
                        secondImageViewSeelected = null;
                        firstMemoryCard = null;
                        secondMemoryCard = null;

                    }
                }
                //drugi
                else{


                }
                    //flipImageAnimation((ImageView) view,  "ic_bird2");
                    //else prikazi drugu sliku

            }
        });



    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        gridView.setNumColumns(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
        super.onConfigurationChanged(newConfig);
    }







    private static void flipImageAnimation(ImageView imageViewForChanging, String imageToShowPath){
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageViewForChanging, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imageViewForChanging, "scaleX", 0f, 1f);

        oa1.setDuration(300);
        oa2.setDuration(300);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());

        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                Context context = imageViewForChanging.getContext();
                int id = context.getResources().getIdentifier(imageToShowPath, "drawable", context.getPackageName());
                imageViewForChanging.setImageResource(id);

                oa2.start();
            }
        });

        oa1.start();
    }



}