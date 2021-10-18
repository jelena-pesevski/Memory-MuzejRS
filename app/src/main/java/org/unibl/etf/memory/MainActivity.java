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

    private static MemoryCardDatabase memoryCardDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayList();
    }

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

    public static MemoryCardDatabase getMemoryCardDatabase() {
        return memoryCardDatabase;
    }
}