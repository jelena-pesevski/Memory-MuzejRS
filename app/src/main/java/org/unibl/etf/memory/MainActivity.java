package org.unibl.etf.memory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.unibl.etf.memory.database.MemoryCard;
import org.unibl.etf.memory.database.MemoryCardDatabase;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static MemoryCardDatabase memoryCardDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    public void onDestroy() {
        super.onDestroy();
        //stopService(new Intent(this, BackgroundMusic.class));
        Log.d("TAG", "onDestroy");

    }

    @Override
    public void onStop(){
        super.onStop();
        stopService(new Intent(this, BackgroundMusic.class));
        Log.d("TAG", "onStop");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("TAG", "onResume");
        startService(new Intent( this, BackgroundMusic.class ) );
    }
}