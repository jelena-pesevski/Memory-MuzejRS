package org.unibl.etf.memory;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.unibl.etf.memory.Adapters.GridViewAdapter;
import org.unibl.etf.memory.Database.MemoryCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private int columnsNum;
    private int rowsNum;

    private List<MemoryCard> memoryCards = new ArrayList<MemoryCard>();

    //info za svaki imageView da li je vec clicked
    private boolean[] clickedImageViews;
    private int[] images;
    private int[] backroundImages;
    private GridView gridView;
    private Handler handler;

    ImageView firstImageViewSelected = null, secondImageViewSeelected = null;
    MemoryCard firstMemoryCard = null, secondMemoryCard = null;
    int firstMemoryCardClickedIndex = -1, secondMemoryCardClickedIndex = -1;

    private void loadImageViews(){

        //images = new {R.drawable.ic_bird1, R.drawable.ic_bird2};
        memoryCards.add(new MemoryCard(0, "aaa", "ic_pepper", "opis 1"));
        memoryCards.add(new MemoryCard(1, "bbb", "ic_pear", "opis 2"));
        memoryCards.add(new MemoryCard(0, "aaa", "ic_pepper", "opis 1"));
        memoryCards.add(new MemoryCard(1, "bbb", "ic_pear", "opis 2"));

        images = new int[memoryCards.size()];

        int br=0;
        for(MemoryCard m : memoryCards){
            int id = GameActivity.this.getResources().getIdentifier(m.getPath(), "drawable", GameActivity.this.getPackageName());
            images[br++] = id;
        }

        clickedImageViews = new boolean[columnsNum*rowsNum];
        backroundImages= new int[columnsNum*rowsNum];
        for(int i = 0;i<columnsNum*rowsNum;i++){
            backroundImages[i] = R.drawable.ic_empty_card;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        handler=new Handler(getApplicationContext().getMainLooper());

        Intent intent = getIntent();
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            columnsNum = intent.getIntExtra("numColumnsPortrait", 2);
            rowsNum = intent.getIntExtra("numRowsPortrait", 2);
        } else {
            columnsNum = intent.getIntExtra("numColumnsLandscape", 2);
            rowsNum = intent.getIntExtra("numRowsLandscape", 2);
        }

        //get screen size
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        double padding = 9;

        int sizeOfCard = (int)(width/columnsNum - 2*columnsNum*padding);

        gridView = (GridView) findViewById(R.id.gridViewImages);
        gridView.setNumColumns(columnsNum);

        loadImageViews();

        GridViewAdapter gridViewAdapter = new GridViewAdapter(this, backroundImages, sizeOfCard, sizeOfCard);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(this);
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


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("aa", "Test*************************************************** "+ Thread.currentThread().getId());
        //prvi put kliknuta
        if(view.isEnabled()){
            Log.d("aa", "ANIMACIJA"+clickedImageViews[i]);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    flipImageAnimation((ImageView) view,  String.valueOf(images[i]));
                }
            });

          //  clickedImageViews[i]=true;
          //  ((ImageView) view).setClickable(false);
            ((ImageView) view).setEnabled(false);

            if(firstImageViewSelected == null){
                firstMemoryCardClickedIndex = i;
                firstImageViewSelected = (ImageView) view;
                firstMemoryCard = memoryCards.get(i);
                //firstImageViewSelected.setClickable(false);
                //firstImageViewSelected.setOnClickListener(null);
            }
            else {
                secondMemoryCardClickedIndex = i;
                secondImageViewSeelected = (ImageView) view;
                secondMemoryCard = memoryCards.get(i);

                Log.d("aa", "kliknute" + firstMemoryCardClickedIndex+ " " +secondMemoryCardClickedIndex);

                //provjeri poklapanje
                if(firstMemoryCard.getMemoryCard_id() == secondMemoryCard.getMemoryCard_id()){
                   /* firstImageViewSelected.setEnabled(false);
                    secondImageViewSeelected.setClickable(false);*/

                 //   Toast.makeText(getApplicationContext(), "Cestitam", Toast.LENGTH_SHORT).show();

                    firstImageViewSelected = null;
                    secondImageViewSeelected = null;
                    firstMemoryCard = null;
                    secondMemoryCard = null;
                }
                else{
                    //rotiranje sa delayom zbog iscrtavanja
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flipImageAnimation(firstImageViewSelected, "ic_empty_card");
                            flipImageAnimation(secondImageViewSeelected, "ic_empty_card");

                         //   clickedImageViews[firstMemoryCardClickedIndex]= false;
                         //   clickedImageViews[secondMemoryCardClickedIndex]= false;
                            firstImageViewSelected.setEnabled(true);
                            secondImageViewSeelected.setEnabled(true);

                            //ne moze se izvuci izvan else zbog toga sto mora da se izvrsi tacno nakon animacija iznad
                            firstImageViewSelected = null;
                            secondImageViewSeelected = null;
                            firstMemoryCard = null;
                            secondMemoryCard = null;

                        }
                    }, 1000);
                }
            }
        }
    }
    public static List<MemoryCard> getRandomMemoryCardsFromDatabase(int numberOfMemoryCards)
    {
        //check if number is odd
        if(numberOfMemoryCards % 2 != 0)
            return null;
        Random random=new Random();
        List<MemoryCard> memoryCards=MainActivity.getMemoryCardDatabase().getMemoryCardDao().getMemoryCard();
        List<MemoryCard> newList=null;
        for(int i=0;i<numberOfMemoryCards/2;i++)
        {
            int randomIndex = random.nextInt(numberOfMemoryCards/2);
            MemoryCard memoryCard=memoryCards.get(randomIndex);
            newList.add(memoryCard);
            newList.add(memoryCard);
            Collections.shuffle(newList);



        }
        return newList;








    }

}