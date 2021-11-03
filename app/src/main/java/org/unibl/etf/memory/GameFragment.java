package org.unibl.etf.memory;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioAttributes;
import android.media.SoundPool;
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

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.unibl.etf.memory.adapters.GridViewAdapter;
import org.unibl.etf.memory.database.MemoryCard;
import org.unibl.etf.memory.utils.Constants;
import org.unibl.etf.memory.utils.Preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameFragment extends Fragment implements AdapterView.OnItemClickListener{

    private int columnsNum;
    private int rowsNum;
    private SoundPool soundPool;
    private int sound;

    private List<MemoryCard> memoryCards = new ArrayList<MemoryCard>();

    //info za svaki imageView da li je vec clicked
    private boolean[] clickedImageViews;
    private int[] images;
    private int counter=0;
    private GridView gridView;
    private Handler handler;

    private ImageView firstImageViewSelected = null, secondImageViewSeelected = null;
    private MemoryCard firstMemoryCard = null, secondMemoryCard = null;
    private int firstMemoryCardClickedIndex = -1, secondMemoryCardClickedIndex = -1;

    private View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_game, container, false);
        handler=new Handler(getActivity().getApplicationContext().getMainLooper());

        setSound();
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

    @SuppressWarnings("deprecation")
    private void setGridviewSize(){
        //get screen size
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        double padding = Constants.cardsPadding;
        double landscapePadding = Constants.cardsPaddingLandscape;

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

            cardWidth = (int)(width/columnsNum - 2*columnsNum*landscapePadding);
            cardHeight = (int)((height)/rowsNum - 2*rowsNum*landscapePadding);
        }

        gridView.setNumColumns(columnsNum);

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
            if(firstImageViewSelected == null){
                firstMemoryCardClickedIndex = i;
                firstImageViewSelected = (ImageView) view;
                firstMemoryCard = memoryCards.get(i);

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

                //check if cards match
                if(firstMemoryCard.getMemoryCard_id() == secondMemoryCard.getMemoryCard_id()){
                    clickedImageViews[firstMemoryCardClickedIndex]=true;
                    clickedImageViews[secondMemoryCardClickedIndex]=true;
                    firstImageViewSelected = null;
                    secondImageViewSeelected = null;
                    firstMemoryCard = null;
                    secondMemoryCard = null;

                    if(Preferences.getIsMusicPlaying(requireActivity())){
                        playSound();
                    }

                    counter--;
                }
                else{
                    //flipping with delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flipImageAnimation(firstImageViewSelected, "ic_empty_card");
                            flipImageAnimation(secondImageViewSeelected, "ic_empty_card");

                            firstImageViewSelected.setEnabled(true);
                            secondImageViewSeelected.setEnabled(true);

                            //has to be done after the animation above
                            firstImageViewSelected = null;
                            secondImageViewSeelected = null;
                            firstMemoryCard = null;
                            secondMemoryCard = null;

                        }
                    }, 1000);
                }
            }
            if(counter==0){
                //go to game over fragment
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(getActivity()!=null){
                         /*   Fragment gameOverFragment = new GameOverFragment();

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.navHostFragment, gameOverFragment, "GAME_OVER_FRAGMENT")
                                    .commit();*/
                            Navigation.findNavController(root).navigate(R.id.action_gameOver);

                        }
                    }
                }, 2000);
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

    private void setSound()
    {
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();
        soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();
        sound= soundPool.load(getContext(),R.raw.match,1);

    }

    private void playSound()
    {
        soundPool.play(sound,1,1,1,0,1);

    }
}