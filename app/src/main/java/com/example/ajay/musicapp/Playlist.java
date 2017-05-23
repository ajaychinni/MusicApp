package com.example.ajay.musicapp;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Playlist  extends RecyclerView.Adapter<Playlist.MyViewHolder1> {

    private final Context ctx;
    private final ArrayList<Song> data;
    private ClickListner mClickListner;
    private HashMap<String, Bitmap> imageBitmapList;

    private MyViewHolder1 holder;
    PlayerDbHelper mDbHelper;
    ArrayList<String> playlistData;



    public Playlist(Context ctx, ArrayList<Song> data) {
        this.ctx = ctx;
        this.data = data;
        this.imageBitmapList = new HashMap<>();
    }


    @Override
    public MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_adapter, null);

        holder = new MyViewHolder1(view);

        return holder;
    }

    //So that the listview images don't change dynamically
    @Override
    public void onViewRecycled(MyViewHolder1 holder) {
        super.onViewRecycled(holder);
        holder.image.setImageDrawable(null);
        holder.image.setImageResource(R.drawable.music);
    }

    @Override
    public void onBindViewHolder(MyViewHolder1 holder, final int position) {


        mDbHelper = new PlayerDbHelper(ctx);
        playlistData = mDbHelper.getPlaylist();

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        public TextView artist, head, duration;
        public ImageView image;
        public CheckBox mCheckBox;

        public MyViewHolder1(View convertView) {
            super(convertView);

            convertView.setOnClickListener(this);
            head = (TextView) convertView.findViewById(R.id.header);
            artist = (TextView) convertView.findViewById(R.id.artist);
            duration = (TextView) convertView.findViewById(R.id.duration);
            image = (ImageView) convertView.findViewById(R.id.image);
            mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            mCheckBox.setOnCheckedChangeListener(this);

        }

        @Override
        public void onClick(View v) {
            System.out.println("CLICK");
            if (mClickListner != null) {
                mClickListner.itemClicked(v, getPosition());
            }
        }



        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    }

    public void setClickListner(ClickListner clickListner) {
        mClickListner = clickListner;
    }


    public interface ClickListner {
        public void itemClicked(View view, int postion);
    }





}

