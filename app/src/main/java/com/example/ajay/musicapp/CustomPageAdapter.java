package com.example.ajay.musicapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomPageAdapter extends PagerAdapter {

    private Context mContext;
    private  ArrayList<Song> data;
    private HashMap<String,Bitmap> imageBitmapList;

    public CustomPageAdapter(Context context, ArrayList<Song> data) {
        mContext = context;
        this.data = data;
        this.imageBitmapList=new HashMap<>();
    }





    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.swipe_image,collection, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.songImage);
        TextView tvSongName = (TextView) view.findViewById(R.id.songName);
        TextView tvArtistName = (TextView) view.findViewById(R.id.songArtist);

      //  if(data.get(position).getImage()!=null)
        //    imageView.setImageBitmap(data.get(position).getImage());

        System.out.println("$$$$"+ data.get(position).getTitle());
         tvSongName.setText(data.get(position).getTitle());
        tvArtistName.setText(data.get(position).getArtistName());

        imageView.setImageResource(R.drawable.music);


        if(imageBitmapList.containsKey(data.get(position).getDatas()))
        {
            if(imageBitmapList.get(data.get(position).getDatas())!=null)
                imageView.setImageBitmap(imageBitmapList.get(data.get(position).getDatas()));
        }
        else {
            Bitmap btmap = getAlbumCover(data.get(position).getDatas());
            if (btmap != null)
            { imageView.setImageBitmap(btmap);
                imageBitmapList.put(data.get(position).getDatas(),btmap);
            }
            else
            {
                imageBitmapList.put(data.get(position).getDatas(),null);
            }
        }
        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout)object);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return (view==(LinearLayout)object);
    }

    public Bitmap getAlbumCover(String pth) {

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art = null;
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        mmr.setDataSource(mContext, Uri.fromFile(new File(pth)));
        rawArt = mmr.getEmbeddedPicture();
        if (rawArt != null) {
            System.out.println("INSIDE RAW ART NOT NULL");
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
        }
        return art;
    }

}
