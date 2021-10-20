package org.unibl.etf.memory.adapters;

import static android.widget.ImageView.ScaleType.FIT_CENTER;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.unibl.etf.memory.R;

public class GridViewAdapter extends BaseAdapter {

    Context c;
    int[] images;
    boolean[] isFlipped;

    int width, height;

    public GridViewAdapter(Context c, int[] images, boolean[] isFlipped, int width, int height){
        this.c = c;
        this.images = images;
        this.isFlipped=isFlipped;
        this.width = width;
        this.height = height;

    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int pos) {
        return images[pos];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(c);
        if(isFlipped[i]){
            imageView.setImageResource(images[i]);
        }else{
            imageView.setImageResource(R.drawable.ic_empty_card);
        }
      //  imageView.setImageResource(images[i]);

        imageView.setScaleType(FIT_CENTER);
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new GridView.LayoutParams(width, height));

        return imageView;
    }
}
