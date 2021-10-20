package org.unibl.etf.memory;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.unibl.etf.memory.adapters.GridViewAdapter;
import org.unibl.etf.memory.database.MemoryCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameFragment extends Fragment implements AdapterView.OnItemClickListener{

    private int columnsNum;
    private int rowsNum;

    private List<MemoryCard> memoryCards = new ArrayList<MemoryCard>();

    //info za svaki imageView da li je vec clicked
    private boolean[] clickedImageViews;
    private int[] images;
    private int counter=0;
    private GridView gridView;
    private Handler handler;

    ImageView firstImageViewSelected = null, secondImageViewSeelected = null;
    MemoryCard firstMemoryCard = null, secondMemoryCard = null;
    int firstMemoryCardClickedIndex = -1, secondMemoryCardClickedIndex = -1;


    private View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root= inflater.inflate(R.layout.fragment_game, container, false);
        handler=new Handler(getActivity().getApplicationContext().getMainLooper());


        gridView = (GridView) root.findViewById(R.id.gridViewImages);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            columnsNum = getArguments().getInt("numColumnsPortrait",2);
            rowsNum = getArguments().getInt("numRowsPortrait", 2);
        } else {
            columnsNum = getArguments().getInt("numColumnsLandscape", 2);
            rowsNum = getArguments().getInt("numRowsLandscape", 2);
        }

        loadImageViews();
        setGridviewSize();
        counter=images.length/2;
        return root;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setGridviewSize();
        firstImageViewSelected = null;
        secondImageViewSeelected = null;
        firstMemoryCard = null;
        secondMemoryCard = null;
    }

    private void setGridviewSize(){
        //get screen size
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        double padding = 9;

        int orientation = this.getResources().getConfiguration().orientation;

        int cardWidth = 0, cardHeight;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            columnsNum = getArguments().getInt("numColumnsPortrait",2);
            rowsNum = getArguments().getInt("numRowsPortrait", 2);

            cardWidth = (int)(width/columnsNum - 2*columnsNum*padding);
            cardHeight = cardWidth;

        } else {
            columnsNum = getArguments().getInt("numColumnsLandscape", 2);
            rowsNum = getArguments().getInt("numRowsLandscape", 2);

            cardWidth = (int)(width/columnsNum - 2*columnsNum*padding);
            cardHeight = (int)(( height)/rowsNum - 2*rowsNum*padding);
        }

        gridView.setNumColumns(columnsNum);

        //GridViewAdapter gridViewAdapter = new GridViewAdapter(getContext(), backroundImages, sizeOfCard, sizeOfCard);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(gridView.getContext(), images, clickedImageViews, cardWidth, cardHeight);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(this);
    }

    private void loadImageViews(){

        memoryCards=getRandomMemoryCardsFromDatabase(columnsNum*rowsNum);

        images = new int[memoryCards.size()];

        int br=0;
        for(MemoryCard m : memoryCards){
            int id = GameFragment.this.getResources().getIdentifier(m.getName(), "drawable", getActivity().getPackageName());
            images[br++] = id;
        }

        clickedImageViews = new boolean[columnsNum*rowsNum];
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(view.isEnabled()){
            //ako su vec dvije izabrane
           /* if(firstImageViewSelected!=null && secondImageViewSeelected!=null){
                Log.d("aa", "KLIKNUT INDEX "+ i+ " ALI SE NE OKRECE");
                return;
            }*/

            //  clickedImageViews[i]=true;
            //  ((ImageView) view).setClickable(false);
            if(firstImageViewSelected == null){
                firstMemoryCardClickedIndex = i;
                firstImageViewSelected = (ImageView) view;
                firstMemoryCard = memoryCards.get(i);
                //clickedImageViews[i]=true;

                firstImageViewSelected.setEnabled(false);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        flipImageAnimation((ImageView) view,  String.valueOf(images[i]));
                    }
                });
            }
            else if (firstImageViewSelected!= null && secondImageViewSeelected==null){
                secondMemoryCardClickedIndex = i;
                //clickedImageViews[i]=true;
                secondImageViewSeelected = (ImageView) view;
                secondMemoryCard = memoryCards.get(i);

                ((ImageView) view).setEnabled(false);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        flipImageAnimation((ImageView) view,  String.valueOf(images[i]));
                    }
                });

                Log.d("tag", "odabrane " + firstMemoryCardClickedIndex+ " " +secondMemoryCardClickedIndex);

                //provjeri poklapanje
                if(firstMemoryCard.getMemoryCard_id() == secondMemoryCard.getMemoryCard_id()){
                    clickedImageViews[firstMemoryCardClickedIndex]=true;
                    clickedImageViews[secondMemoryCardClickedIndex]=true;
                    firstImageViewSelected = null;
                    secondImageViewSeelected = null;
                    firstMemoryCard = null;
                    secondMemoryCard = null;
                    Toast.makeText(getContext(), "Bravo!", Toast.LENGTH_SHORT).show();
                    counter--;
                }
                else{
                    //rotiranje sa delayom zbog iscrtavanja
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flipImageAnimation(firstImageViewSelected, "ic_empty_card");
                            flipImageAnimation(secondImageViewSeelected, "ic_empty_card");

                            //clickedImageViews[firstMemoryCardClickedIndex]= false;
                            //clickedImageViews[secondMemoryCardClickedIndex]= false;
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
            if(counter==0){
                Toast.makeText(getContext(), "Pronasli ste sve parove!", Toast.LENGTH_SHORT).show();
            }
        }
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
    public static List<MemoryCard> getRandomMemoryCardsFromDatabase(int numberOfMemoryCards)
    {
        //check if number is odd
        if(numberOfMemoryCards % 2 != 0)
            return null;
        List<MemoryCard> memoryCards=MainActivity.getMemoryCardDatabase().getMemoryCardDao().getMemoryCard();
        Collections.shuffle(memoryCards);
        List<MemoryCard> newList=new ArrayList<>();
        int counter=0;
            for(MemoryCard memoryCard:memoryCards) {
                if(counter==numberOfMemoryCards/2)
                    break;
                newList.add(memoryCard);
                newList.add(memoryCard);
                counter++;
            }

        Collections.shuffle(newList);
        return newList;
    }
}