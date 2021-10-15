package org.unibl.etf.memory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

        new AnimationThread(imageViewBunny).start();
        new AnimationThread(imageViewBear).start();
        new AnimationThread(imageViewBambi).start();

        return root;
    }
}

class AnimationThread extends Thread {
    private ImageView imageView;

    public AnimationThread() {

    }

    public AnimationThread(ImageView imageViewArg) {
        this.imageView = imageViewArg;
    }

    public void run() {
        Animation animationZoomIn = AnimationUtils.loadAnimation(imageView.getContext(), R.anim.zoom_in);
        Animation animationZoomOut = AnimationUtils.loadAnimation(imageView.getContext(), R.anim.zoom_out);

        while (true) {
            imageView.startAnimation(animationZoomIn);

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            imageView.startAnimation(animationZoomOut);

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
