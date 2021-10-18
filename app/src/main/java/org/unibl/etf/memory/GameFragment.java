package org.unibl.etf.memory;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import org.unibl.etf.memory.Adapters.GridViewAdapter;
import org.unibl.etf.memory.Database.MemoryCard;

import java.util.ArrayList;
import java.util.List;

public class GameFragment extends Fragment implements AdapterView.OnItemClickListener{

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_game, container, false);
        handler=new Handler(getActivity().getApplicationContext().getMainLooper());

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            columnsNum = getArguments().getInt("numColumnsPortrait",2);
            rowsNum = getArguments().getInt("numRowsPortrait", 2);
        } else {
            columnsNum = getArguments().getInt("numColumnsLandscape", 2);
            rowsNum = getArguments().getInt("numRowsLandscape", 2);
        }

        //get screen size
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        double padding = 9;

        int sizeOfCard = (int)(width/columnsNum - 2*columnsNum*padding);

        gridView = (GridView) root.findViewById(R.id.gridViewImages);
        gridView.setNumColumns(columnsNum);

        loadImageViews();

        GridViewAdapter gridViewAdapter = new GridViewAdapter(getContext(), backroundImages, sizeOfCard, sizeOfCard);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(this);

        return root;
    }

    private void loadImageViews(){

        //images = new {R.drawable.ic_bird1, R.drawable.ic_bird2};
        memoryCards.add(new MemoryCard(0, "aaa", "ic_pepper", "opis 1"));
        memoryCards.add(new MemoryCard(1, "bbb", "ic_pear", "opis 2"));
        memoryCards.add(new MemoryCard(0, "aaa", "ic_pepper", "opis 1"));
        memoryCards.add(new MemoryCard(1, "bbb", "ic_pear", "opis 2"));

        images = new int[memoryCards.size()];

        int br=0;
        for(MemoryCard m : memoryCards){
            int id = GameFragment.this.getResources().getIdentifier(m.getPath(), "drawable", getActivity().getPackageName());
            images[br++] = id;
        }

        clickedImageViews = new boolean[columnsNum*rowsNum];
        backroundImages= new int[columnsNum*rowsNum];
        for(int i = 0;i<columnsNum*rowsNum;i++){
            backroundImages[i] = R.drawable.ic_empty_card;
        }

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