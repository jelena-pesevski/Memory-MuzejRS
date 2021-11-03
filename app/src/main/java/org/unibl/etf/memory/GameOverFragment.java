package org.unibl.etf.memory;

import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.unibl.etf.memory.utils.Preferences;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class GameOverFragment extends Fragment {

    View root;
    SoundPool soundPool;
    int sound;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_game_over, container, false);
        if(Preferences.getIsMusicPlaying(getContext()))
            setSound();
        root.findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","HOME PRESSED");
                Navigation.findNavController(root).popBackStack(R.id.levelsScreenFragment,false);
            }
        });

        return root;
    }

    private void setSound()
    {
        Log.d("TAG", "SET CONFETTI SOUND");
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();
        soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                playSound();
            }
        });
        sound= soundPool.load(getContext(),R.raw.confetti,1);
    }

    private void playSound()
    {
        Log.d("TAG", "PLAY CONFETTI SOUND");
        soundPool.play(sound,1,1,1,0,1);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final KonfettiView konfettiView =getView().findViewById(R.id.viewKonfetti);

       // playSound();
        konfettiView.build()
                .addColors(Color.rgb(236,172,56), Color.rgb(159,57,23),
                            Color.rgb(214,237,243), Color.rgb(168,198,99))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .streamFor(200, 3000L);

    }


}