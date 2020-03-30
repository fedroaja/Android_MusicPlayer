package umn.ac.id.uts2020_mobile_cl_00000021661_fedro_musique;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MenuUtama extends AppCompatActivity {

    private static final int PERMISSION_REQ = 1;
    SearchView etSearch;
    ArrayList<String> titles; // temp arraylist
    ArrayList<String> artists; // temp arraylist
    ArrayList<String> dirs; // temp arraylist
    ListView listmusic;
    Adapter adapters; // adapter class
    ArrayList<Data> data; // music Data arraylist

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);


        Intent intentMenuUtama = getIntent();
        String psnMasuk = intentMenuUtama.getStringExtra("AccName");
        //showing alert from login activity only;
        if (psnMasuk==null){
            psnMasuk ="empty";
        }
        if (!psnMasuk.equals("empty")){
            AlertDialog.Builder alertLogin = new AlertDialog.Builder(this);
            alertLogin.setTitle(Html.fromHtml("<b align='center'>Selamat Datang </b>"));
            alertLogin.setMessage(psnMasuk);
            alertLogin.setPositiveButton("OK",null);
            AlertDialog dialog = alertLogin.show();
            TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
            messageView.setGravity(Gravity.CENTER);
        }

        //cek external storage permission
        if(ContextCompat.checkSelfPermission(MenuUtama.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MenuUtama.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MenuUtama.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQ);
            }else{
                ActivityCompat.requestPermissions(MenuUtama.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQ);
            }
        }else{
            doStuff();
        }

    }

    // input data to Arraylist<Data>
    private ArrayList<Data> getData(){
        data = new ArrayList<Data>();
        Data d;
        for (int i=0;i<titles.size();i++){
            d= new Data(titles.get(i),artists.get(i),dirs.get(i));
            data.add(d);
        }
        return data;
    }

    public void doStuff(){
        listmusic = (ListView) findViewById(R.id.daftarmusic);
        etSearch  = (SearchView) findViewById(R.id.Search);
        titles = new ArrayList<>();
        artists = new ArrayList<>();
        dirs = new ArrayList<>();

        getMusic();

        adapters = new Adapter(this,getData());

        listmusic.setAdapter(adapters); // showing adapter to listview

        //search function
        etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapters.getFilter().filter(newText);
                return false;
            }
        });

        //function for Item listview clicked
        listmusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Data datas = (Data) listmusic.getItemAtPosition(position);
                String dir=datas.getCurrDir();
                int songIndex = position;

                Intent intent = new Intent(getApplicationContext(),MenuPlay.class);
                intent.putExtra("dir",dir).putExtra("idxsong",songIndex).putExtra("musicDetails",data);
                startActivity(intent);
            }
        });

    }

    //function for get music from external storage
    public void getMusic(){
        ContentResolver contentResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = contentResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()){
            int musicTitle = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int musicArtist = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int musicDir = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                String currTitle = musicCursor.getString(musicTitle);
                String currArtist = musicCursor.getString(musicArtist);
                String currDir = musicCursor.getString(musicDir);
                titles.add(currTitle);
                artists.add(currArtist);
                dirs.add(currDir);

            }while (musicCursor.moveToNext());
        }
    }

    //permission req handler
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case PERMISSION_REQ: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MenuUtama.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                        doStuff();
                    }
                } else{
                    Toast.makeText(this,"No Permission Granted",Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    //menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // function if item menu selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile: {
                Intent profileIntent = new Intent(MenuUtama.this,
                                                 MenuProfile.class);
                startActivity(profileIntent);
                break;
            }
            case R.id.asc: {
                Collections.sort(data, new Comparator<Data>() {
                    @Override
                    public int compare(Data o1, Data o2) {
                        return o1.getTitle().compareToIgnoreCase(o2.getTitle());
                    }
                });
                adapters.notifyDataSetChanged();
                break;
            }
            case R.id.dsc: {
                Collections.sort(data, new Comparator<Data>() {
                    @Override
                    public int compare(Data o1, Data o2) {
                        return o2.getTitle().compareToIgnoreCase(o1.getTitle());
                    }
                });
                adapters.notifyDataSetChanged();
                break;
            }
            case R.id.logout: {
                Intent logoutIntent = new Intent(MenuUtama.this,
                                                 MainActivity.class);
                startActivity(logoutIntent);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
