package org.unibl.etf.memory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.unibl.etf.memory.utils.Constants;

public class LevelsScreenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_levels_screen, container, false);

        ImageView imageViewBunny = (ImageView) root.findViewById(R.id.imageViewBunny);
        ImageView imageViewBear = (ImageView) root.findViewById(R.id.imageViewBear);
        ImageView imageViewBambi = (ImageView) root.findViewById(R.id.imageViewBambi);

        imageViewBunny.setOnClickListener(this::onBunnyClicked);
        imageViewBambi.setOnClickListener(this::onBambiClicked);
        imageViewBear.setOnClickListener(this::onBearClicked);

        //start amimations
        Animation animationZoomInOut = AnimationUtils.loadAnimation(imageViewBunny.getContext(), R.anim.zoom_in_zoom_out);
        imageViewBunny.startAnimation(animationZoomInOut);
        imageViewBear.startAnimation(animationZoomInOut);
        imageViewBambi.startAnimation(animationZoomInOut);
        return root;
    }

    private void onBunnyClicked(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("numColumnsPortrait", Constants.columns_lvl1_portrait);
        bundle.putInt("numRowsPortrait", Constants.rows_lvl1_portrait);
        bundle.putInt("numRowsLandscape", Constants.rows_lvl1_land);
        bundle.putInt("numColumnsLandscape", Constants.columns_lvl1_land);

        Navigation.findNavController(view).navigate(R.id.level_chosen, bundle);
    }

    private void onBearClicked(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("numColumnsPortrait", Constants.columns_lvl3_portrait);
        bundle.putInt("numRowsPortrait", Constants.rows_lvl3_portrait);
        bundle.putInt("numRowsLandscape", Constants.rows_lvl3_land);
        bundle.putInt("numColumnsLandscape", Constants.columns_lvl3_land);

        Navigation.findNavController(view).navigate(R.id.level_chosen, bundle);
    }

    private void onBambiClicked(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("numColumnsPortrait", Constants.columns_lvl2_portrait);
        bundle.putInt("numRowsPortrait", Constants.rows_lvl2_portrait);
        bundle.putInt("numRowsLandscape", Constants.rows_lvl2_land);
        bundle.putInt("numColumnsLandscape", Constants.columns_lvl2_land);

        Navigation.findNavController(view).navigate(R.id.level_chosen, bundle);
    }
}

