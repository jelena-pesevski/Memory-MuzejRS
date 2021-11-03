package org.unibl.etf.memory;

import android.graphics.Color;
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

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class GameOverFragment extends Fragment {

    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_game_over, container, false);
        root.findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","HOME PRESSED");
                Navigation.findNavController(root).popBackStack(R.id.levelsScreenFragment,false);
            }
        });

        return root;
    }

    public void goHome(View view){
        Log.d("TAG","HOME PRESSED");
        Toast.makeText(getContext(),"pressed",Toast.LENGTH_SHORT).show();
        Navigation.findNavController(root).popBackStack(R.id.levelsScreenFragment,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final KonfettiView konfettiView =getView().findViewById(R.id.viewKonfetti);

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