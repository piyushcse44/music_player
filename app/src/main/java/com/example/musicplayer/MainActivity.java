package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);
        Dexter.withContext(MainActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        ArrayList<File> arrayList = fetch_songs(Environment.getExternalStorageDirectory());


                        String [] song_name = new String[arrayList.size()];
                        for(int i=0;i<arrayList.size();i++)
                        {
                            song_name[i]=arrayList.get(i).getName();
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        CustomAdapter adapter = new CustomAdapter(song_name,arrayList);
                        recyclerView.setAdapter(adapter);









                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "Permission retarted", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();



    }
    public ArrayList<File> fetch_songs(File file)
    {
        ArrayList<File> arrayList= new ArrayList<>();
        File[] songs = file.listFiles();

        if(songs==null) return(arrayList);
        for(File my_file : songs)
        {

            if(!my_file.isHidden()&&my_file.isDirectory())
            {
                arrayList.addAll(fetch_songs(my_file));
            }
            else
            {
                if(my_file.getName().endsWith(".mp3")&&!my_file.getName().startsWith("."))
                {
                    arrayList.add(my_file);
                }
            }
        }

        return arrayList;

    }
}