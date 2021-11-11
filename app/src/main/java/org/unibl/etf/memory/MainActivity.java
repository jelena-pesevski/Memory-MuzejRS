package org.unibl.etf.memory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import org.unibl.etf.memory.database.MemoryCard;
import org.unibl.etf.memory.database.MemoryCardDatabase;
import org.unibl.etf.memory.utils.Preferences;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static MemoryCardDatabase memoryCardDatabase;

    private MemoryCardsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MemoryCardsViewModel.class);
        displayList();
    }

    private void displayList() {
        memoryCardDatabase = MemoryCardDatabase.getInstance(getApplicationContext());
        new MainActivity.RetrieveTask(this).execute();
    }

    private static class RetrieveTask extends AsyncTask<Void, Void, List<MemoryCard>> {

        private WeakReference<MainActivity> activityReference;

        RetrieveTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<MemoryCard> doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().memoryCardDatabase.getMemoryCardDao().getMemoryCard();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<MemoryCard> memoryCard) {


        }
    }

    public static MemoryCardDatabase getMemoryCardDatabase() {
        return memoryCardDatabase;
    }


    @Override
    public void onPause(){
        super.onPause();
        if(Preferences.getIsMusicPlaying(this)) {
            stopService(new Intent(this, BackgroundMusic.class));
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(Preferences.getIsMusicPlaying(this)){
            startService(new Intent( this, BackgroundMusic.class ));
        }
    }


    //reset view model da bi se razlikovalo kada se pritisne onBackPressed, a kad se desi configurationChanged
    @Override
    public void onBackPressed() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

        if(fragment instanceof GameFragment) {
            viewModel.setCounter(0);
            viewModel.setImages(null);
            viewModel.setMemoryCards(null);
            viewModel.setClickedImageViews(null);
        }

        super.onBackPressed();
    }
}