package org.unibl.etf.memory;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.unibl.etf.memory.utils.Preferences;

import java.util.prefs.PreferenceChangeEvent;

public class StartScreenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_start_screen, container, false);
        root.findViewById(R.id.play_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.start_game);
            }
        });

        root.findViewById(R.id.sound_btn).setOnClickListener(this::soundBtnCLicked);

        return root;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Boolean isMusicPlaying= Preferences.getIsMusicPlaying(requireActivity());
        this.updateSoundIcon(view, isMusicPlaying);
    }

    private void updateSoundIcon(View view, Boolean isMusicPlaying) {
        if(isMusicPlaying){
            view.findViewById(R.id.sound_btn).setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_sound_on ));
        }else{
            view.findViewById(R.id.sound_btn).setBackground(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_sound_off));
        }
    }

    private void soundBtnCLicked(View view) {
        Boolean newMusicPreference=!Preferences.getIsMusicPlaying(requireActivity());
        Preferences.changeIsMusicPlaying(requireActivity(), newMusicPreference);

        if(newMusicPreference){
            requireActivity().startService(new Intent(requireActivity(), BackgroundMusic.class));
        }else{
            requireActivity().stopService(new Intent(requireActivity(), BackgroundMusic.class));
        }

        this.updateSoundIcon(view, newMusicPreference);
    }
}