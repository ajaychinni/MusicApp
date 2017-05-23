package com.example.ajay.musicapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import static android.R.attr.data;

public class MyAdapter extends BaseAdapter implements View.OnClickListener {


    private final Context ctx;
    private final ArrayList<Song> data;

    public MyAdapter(Context ctx, ArrayList<Song> data) {
        this.ctx=ctx;
        this.data=data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_adapter,null);
            holder=new ViewHolder();
            holder.head = (TextView) convertView.findViewById(R.id.header);
             holder.artist = (TextView) convertView.findViewById(R.id.artist);
             holder.duration = (TextView) convertView.findViewById(R.id.duration);
             holder.image = (ImageView) convertView.findViewById(R.id.image);

             holder.checkBox= (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.checkBox.setOnClickListener(this);

            convertView.setTag(holder);

        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        int dur= data.get(position).getDuration();
        int seconds =  (dur / 1000) % 60 ;
        int minutes =  (dur / (1000*60) % 60);

        holder.head.setText(data.get(position).getTitle());
        holder.artist.setText(data.get(position).getArtistName());
        holder.duration.setText(minutes+":"+seconds);


        return convertView;


    }


    @Override
    public void onClick(View v) {
        CheckBox c = (CheckBox) v;

        if(c.isChecked())
        {
            //call DB
        }
    }
   /* @Override
    public void OnLongClickListener(View v)
    {

    }*/

    class ViewHolder {
        TextView artist,head,duration;
        ImageView image;
        CheckBox checkBox;
    }

}
