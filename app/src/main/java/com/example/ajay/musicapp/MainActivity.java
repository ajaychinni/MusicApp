package com.example.ajay.musicapp;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MyRecyclerView.ClickListner, MyRecyclerView.LongClickListner , Playlist.ClickListner{

    Toolbar mToolbar = null;
    Toolbar longClickToolbar = null;
    ArrayList<Song> data;
    Context mContext;
    SQLiteDatabase database= null;
    MyRecyclerView arrayAdapter;
    RecyclerView recyclerView;
    RecyclerView recyclerViewPlaylist;
    Playlist arrayAdapterPlaylist;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManager2;
    TextView itemSelected;
    private ArrayList<String> playlistData;
    Song songObj;
    PlayerDbHelper mDbHelper;
    private ArrayList<String> sdSongs=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        getMusicFile();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

      /*  itemSelected = (TextView) findViewById(R.id.itemSelected);
        itemSelected.setVisibility(View.GONE);
*/
        /*longClickToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(longClickToolbar);
*/
        mDbHelper = new PlayerDbHelper(this);
        playlistData=mDbHelper.getPlaylist();

    }
 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.addPlaylistl:
                Toast.makeText(MainActivity.this, "playlist", Toast.LENGTH_SHORT).show();

                mDbHelper.removeAllSongs();
                for(int i= 0;i<data.size();i++)
                {
                    if(data.get(i).isAdded()==true)
                    {
                        mDbHelper.putInDb(data.get(i).getDatas());
                    }
                }
                
                
                break;
            case R.id.playlist:
                            Intent intent = new Intent(this,Playlist.class);
                            startActivity(intent);
                break;

            case R.id.delete:

                for(int i= 0;i<data.size();i++)
                {
                    if(data.get(i).isAdded()==true)
                    {
                        mDbHelper.removeSong(data.get(i).getDatas());
                    }
                }

                break;

            case R.id.settings:

                break;

            case R.id.about:

                break;

            case R.id.history:

                break;



        }


        return true;
    }
   

    public void getMusicFile() {

        data = new ArrayList();

        ContentResolver res = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] proj = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,

        };
        //MediaStore.Audio.Albums.ALBUM_ART
        //  MediaStore.Audio.Media.ALBUM_ID

        Cursor cursor = res.query(uri, proj, MediaStore.Audio.Media.IS_MUSIC + "=1", null, null);

        Log.i("cursor", "cur" + cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String title = cursor.getString(1);
                String pth = cursor.getString(2);
                String art = cursor.getString(3);
                int dur = cursor.getInt(4);
                //long thumb = cursor.getLong(5);
                Log.i("TAG", "music files=" + title + " path=" + pth);
                songObj = new Song();
                songObj.setDatas(pth);
                songObj.setTitle(title);
                songObj.setArtistName(art);
                songObj.setDuration(dur);
               /* if(playlistData.size()>0 && playlistData.contains(pth))
                songObj.setAdded(true);*/
                // songObj.setThumbnail(thumb);
              //  songObj.setImage(getAlbumCover(pth));
                data.add(songObj);
                if(playlistData!=null && playlistData.contains(pth))
                    songObj.setAdded(true);
                sdSongs.add(pth);


            } while (cursor.moveToNext());
            cursor.close();
                 if(playlistData!=null)  removeFromPlaylist();
        }
        //recycler view for first page
        recyclerView = (RecyclerView) findViewById(R.id.listView);
        arrayAdapter = new MyRecyclerView(MainActivity.this, data);
        arrayAdapter.setClickListner(this);
        arrayAdapter.setLongClickListner(this);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(arrayAdapter);



        //recycler view for Playlist
     /*   recyclerViewPlaylist = (RecyclerView) findViewById(R.id.listView);
        arrayAdapterPlaylist = new Playlist(MainActivity.this, playlistData);
        arrayAdapterPlaylist.setClickListner(this);
        linearLayoutManager2 = new LinearLayoutManager(mContext);
        recyclerViewPlaylist.setLayoutManager(linearLayoutManager2);
        recyclerViewPlaylist.setAdapter(arrayAdapterPlaylist);*/

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);






    // ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,Player.class);
                intent.putExtra("song",data.get(i).getDatas());
                intent.putExtra("pos",i);
                startActivity(intent);
                MediaPlayer player=null;
                try {
                    if(player!=null && player.isPlaying()){
                        player.stop();
                        player.release();
                        player = null;
                    }
                    player = new MediaPlayer();
                    player.setDataSource(data.get(i).getDatas());
                    player.prepare();
                    player.start();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });*/
    }

    private void removeFromPlaylist() {
        for(int i=0;i<playlistData.size();i++)
        {
            if(!sdSongs.contains(playlistData.get(i)))
            {
                mDbHelper.removeSong(playlistData.get(i));
            }
        }
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }
    

    private void moveItem(int oldPos, int newPos) {

        data.get(oldPos);
        data.remove(oldPos);
        data.add(newPos,data.get(newPos));
        arrayAdapter.notifyItemMoved(oldPos, newPos);
    }

    private void deleteItem(final int position) {
        data.remove(position);
        arrayAdapter.notifyItemRemoved(position);
    }



    @Override
    public void itemClicked(View view, int postion) {

        Log.i("TAG","item clicked");

        Intent intent = new Intent(MainActivity.this, Player.class);
        intent.putExtra("songlist",data);
        intent.putExtra("song", data.get(postion).getDatas());
        intent.putExtra("pos", postion);
        startActivity(intent);
        MediaPlayer player = null;
        try {
            if (player != null && player.isPlaying()) {
                player.stop();
                player.release();
                player = null;
            }
           /* player = new MediaPlayer();
            player.setDataSource(data.get(postion).getDatas());
            player.prepare();
            player.start();*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void itemLongClicked(View view, int postion) {
           if(!data.get(postion).getSelectedCB())
           {
               ArrayList<Song> updatedData=new ArrayList<>();
               updatedData.addAll(data);
               for (int i=0;i<data.size();i++)
               {
                   updatedData.get(i).setVisibleCB(true);
               }
               arrayAdapter.swap(updatedData);
               mToolbar.getMenu().clear();
               mToolbar.inflateMenu(R.menu.menu_action_mode);
               getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
              // itemSelected.setVisibility(View.VISIBLE);
              /* isInActionMode=true;
               arrayAdapter.notifyDataSetChanged();
               getSupportActionBar().setDiplayHomeAsUpEnable(true);*/
           }
           

    }

 /*   @Override
    public boolean onLongClick(View v) {
    *//*   mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.menu_action_mode);
       itemSelected.setVisibility(View.VISIBLE);
       isInActionMode=true;
        arrayAdapter.notifyDataSetChanged();
        getSupportActionBar().setDiplayHomeAsUpEnable(true);*//*
       //Toast.makeText(MainActivity.this, "Long clicked", Toast.LENGTH_SHORT).show();
        Log.i("TAG","long click ");

        return false;
    }
*/





 /*   public Bitmap getArt(long thumb){
        Bitmap artwork = null;
        try {
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, thumb);
            ContentResolver res = mContext.getContentResolver();
            InputStream in = res.openInputStream(uri);
            artwork = BitmapFactory.decodeStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return artwork;
    }*/


}