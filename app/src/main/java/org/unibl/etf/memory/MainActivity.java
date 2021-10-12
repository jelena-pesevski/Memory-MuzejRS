package org.unibl.etf.memory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.unibl.etf.memory.Database.MemoryCard;
import org.unibl.etf.memory.Database.MemoryCardDatabase;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final String DB_NAME="DataBase";
    public static final String Table_Name="MemoryCard";
    private MemoryCardDatabase memoryCardDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayList();
        //testiranje i primjer unosa u bazu
        //MemoryCard memoryCard = new MemoryCard(1,"s","s","s");
        //memoryCardDatabase.getMemoryCardDao().insertMemoryCard(memoryCard);
    }

    public void startGame(View view){
        Intent intent = new Intent(MainActivity.this,LevelsActivity.class);
        startActivity(intent);
    }

    private void displayList() {
        memoryCardDatabase = MemoryCardDatabase.getInstance(MainActivity.this);
        new MainActivity.RetrieveTask(this).execute();
    }
    private static class RetrieveTask extends AsyncTask<Void, Void, List<MemoryCard>> {

        private WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
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
            if (memoryCard != null && memoryCard.size() > 0) {

            }
        }
    }
}