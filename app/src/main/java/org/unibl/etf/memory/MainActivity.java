package org.unibl.etf.memory;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.unibl.etf.memory.Database.MemoryCard;
import org.unibl.etf.memory.Database.MemoryCardDatabase;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MemoryCardDatabase memoryCardDatabase;
    private ConstraintLayout container;
    private Fragment startFragment;
    private Fragment levelsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  container=(ConstraintLayout)findViewById(R.id.container);
        startFragment = (StartScreenFragment) getSupportFragmentManager().findFragmentByTag("sf");
        levelsFragment = (LevelsScreenFragment) getSupportFragmentManager().findFragmentByTag("lf");

        Log.d("TAG", "************** ON CREATE");
        if(savedInstanceState==null){
            if (startFragment==null){
                startFragment = new StartScreenFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.container,startFragment, "sf").commit();
                getSupportFragmentManager().beginTransaction().hide(startFragment).commit();
            }

            if (levelsFragment==null){
                levelsFragment = new LevelsScreenFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.container,levelsFragment,"lf").commit();
                getSupportFragmentManager().beginTransaction().hide(levelsFragment).commit();

            }
        }
      //  displayList();*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        Log.d("TAG", "************** CINFIG CHANGED");

        //android:configChanges="orientation|screenSize"
    }

   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the current fragment tag or id
        savedInstanceState.putString("current_fragment", );
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        current_fragment = savedInstanceState.getString("current_fragment");
        // use this to show the current screen
    }*/

    public void startGame(View view){
        Intent intent = new Intent(MainActivity.this,LevelsActivity.class);
        startActivity(intent);

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
}