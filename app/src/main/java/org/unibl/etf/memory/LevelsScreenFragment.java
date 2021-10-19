package org.unibl.etf.memory;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LevelsScreenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_levels_screen, container, false);

        ImageView imageViewBunny = (ImageView) root.findViewById(R.id.imageViewBunny);
        ImageView imageViewBear = (ImageView) root.findViewById(R.id.imageViewBear);
        ImageView imageViewBambi = (ImageView) root.findViewById(R.id.imageViewBambi);

        imageViewBunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("numColumnsPortrait", 2);
                bundle.putInt("numRowsPortrait", 2);
                bundle.putInt("numRowsLandscape", 2);
                bundle.putInt("numColumnsLandscape", 2);

                Navigation.findNavController(root).navigate(R.id.level_chosen, bundle);
            }
        });

        imageViewBear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("numColumnsPortrait", 2);
                bundle.putInt("numRowsPortrait", 3);
                bundle.putInt("numRowsLandscape", 2);
                bundle.putInt("numColumnsLandscape", 3);

                Navigation.findNavController(root).navigate(R.id.level_chosen, bundle);
            }
        });

        //start amimations
        Animation animationZoomInOut = AnimationUtils.loadAnimation(imageViewBunny.getContext(), R.anim.zoom_in_zoom_out);
        imageViewBunny.startAnimation(animationZoomInOut);
        imageViewBear.startAnimation(animationZoomInOut);
        imageViewBambi.startAnimation(animationZoomInOut);

        return root;
    }
}

