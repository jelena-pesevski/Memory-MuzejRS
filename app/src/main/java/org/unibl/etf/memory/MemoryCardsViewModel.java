package org.unibl.etf.memory;

import androidx.lifecycle.ViewModel;

import org.unibl.etf.memory.database.MemoryCard;

import java.util.List;

public class MemoryCardsViewModel extends ViewModel {

    private List<MemoryCard> memoryCards;
    private boolean[] clickedImageViews;
    private int[] images;
    private int counter;

    public List<MemoryCard> getMemoryCards() {
        return memoryCards;
    }

    public void setMemoryCards(List<MemoryCard> memoryCards) {
        this.memoryCards = memoryCards;
    }

    public boolean[] getClickedImageViews() {
        return clickedImageViews;
    }

    public void setClickedImageViews(boolean[] clickedImageViews) {
        this.clickedImageViews = clickedImageViews;
    }

    public int[] getImages() {
        return images;
    }

    public void setImages(int[] images) {
        this.images = images;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
