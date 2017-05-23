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

public class MyRecyclerView  extends RecyclerView.Adapter<MyRecyclerView.MyViewHolder> {

    private final Context ctx;
    private final ArrayList<Song> data;
    private ClickListner mClickListner;
    private HashMap<String, Bitmap> imageBitmapList;

    MainActivity m = new MainActivity();
    private MyViewHolder holder;
    private LongClickListner mLongClickListner;

    public MyRecyclerView(Context ctx, ArrayList<Song> data) {
        this.ctx = ctx;
        this.data = data;
        this.imageBitmapList = new HashMap<>();
    }

    public void swap(ArrayList<Song> datas) {
        data.clear();
        data.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_adapter, null);
        // View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_adapter,null);
        holder = new MyViewHolder(view);

        return holder;
    }

    //So that the listview images don't change dynamically
    @Override
    public void onViewRecycled(MyViewHolder holder) {
        super.onViewRecycled(holder);
        holder.image.setImageDrawable(null);
        holder.image.setImageResource(R.drawable.music);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        int dur = data.get(position).getDuration();
        int seconds = (dur / 1000) % 60;
        int minutes = (dur / (1000 * 60) % 60);

        holder.head.setText(data.get(position).getTitle());
        holder.artist.setText(data.get(position).getArtistName());
        holder.duration.setText(minutes + ":" + seconds);
        if (data.get(position).getSelectedCB()) {
            holder.mCheckBox.setVisibility(View.VISIBLE);

        } else {
            holder.mCheckBox.setVisibility(View.GONE);
        }


        if (data.get(position).isAdded) {
            holder.mCheckBox.setChecked(true);

        } else {
            holder.mCheckBox.setChecked(false);
        }

        if (imageBitmapList.containsKey(data.get(position).getDatas())) {
            if (imageBitmapList.get(data.get(position).getDatas()) != null)
                holder.image.setImageBitmap(imageBitmapList.get(data.get(position).getDatas()));
        } else {
            Bitmap btmap = getAlbumCover(data.get(position).getDatas());
            if (btmap != null) {
                holder.image.setImageBitmap(btmap);
                imageBitmapList.put(data.get(position).getDatas(), btmap);
            } else {
                imageBitmapList.put(data.get(position).getDatas(), null);
            }

        }


        //  if(data.get(position).getImage()!=null)
        //holder.image.setImageBitmap(data.get(position).getImage());
//        if(data.get(position).getThumbnail()>0)
        //   holder.image.setImageBitmap(getArt(data.get(position).getThumbnail()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
        public TextView artist, head, duration;
        public ImageView image;
        public CheckBox mCheckBox;

        public MyViewHolder(View convertView) {
            super(convertView);

            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
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
        public boolean onLongClick(View v) {
            if (mLongClickListner != null) {
                mLongClickListner.itemLongClicked(v, getPosition());
            }
            return false;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (data.get(getPosition()).getSelectedCB()) {

                data.get(getPosition()).setVisibleCB(true);
                // add to db
                System.out.println("Added"+data.get(getPosition()).getDatas());
                data.get(getPosition()).setAdded(true);


            }
            else
                {
                    data.get(getPosition()).setVisibleCB(false);
                    data.get(getPosition()).setAdded(false);
                    System.out.println("Removed");
                    //remove from db
                }

            }
        }

        public void setClickListner(ClickListner clickListner) {
            mClickListner = clickListner;
        }

        public void setLongClickListner(LongClickListner longClickListener) {
            mLongClickListner = longClickListener;
        }

        public interface ClickListner {
            public void itemClicked(View view, int postion);
        }

        public interface LongClickListner {
            public void itemLongClicked(View view, int postion);
        }

        public Bitmap getAlbumCover(String pth) {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            byte[] rawArt;
            Bitmap art = null;
            BitmapFactory.Options bfo = new BitmapFactory.Options();
            mmr.setDataSource(ctx, Uri.fromFile(new File(pth)));
            rawArt = mmr.getEmbeddedPicture();
            if (rawArt != null) {
                System.out.println("INSIDE RAW ART NOT NULL");
                art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
            }
            return art;
        }


    }

