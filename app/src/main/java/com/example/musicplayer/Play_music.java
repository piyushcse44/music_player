package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class Play_music extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        exit=true;
        while(update_seekbar_pos.isAlive());
        super.onDestroy();
        mediaplayer.stop();
        mediaplayer.release();
    }
    // Global variables
    MediaPlayer mediaplayer;
    Intent intent;
    SeekBar seekBar;
    Bundle bundle;
    int []pos = {0};
    ArrayList<File> arrayList;
    TextView welcome;
    ImageView next;
    ImageView prev;
    ImageView play;
    TextView start;
    TextView end;
    seek_bar update_seekbar_pos ;
    boolean exit = false;
    int song_duration=0;
    int currentPosition=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
       // Assign ids to view Variable
        seekBar = findViewById(R.id.seekBar);
        welcome = findViewById(R.id.Welcome);
        next = findViewById(R.id.next);
        prev =  findViewById(R.id.previous);
        play  = findViewById(R.id.play_pauseimageView2);
        start = findViewById(R.id._start_counter);
        end = findViewById(R.id.end_counter);


      // use intent to receive files from previous activity
        intent = getIntent();
        bundle = intent.getExtras();
        int cur_pos = bundle.getInt("position");
        pos[0]=cur_pos;
        arrayList=(ArrayList<File>) bundle.get("song ka file");
        update_seekbar_pos= new seek_bar();



       // change or set Music
           change_to();




    }

    class seek_bar extends Thread {



        @Override
        public void run() {

            while(!exit) {

                try {
                    currentPosition = mediaplayer.getCurrentPosition();
                    int progress = mediaplayer.getCurrentPosition();


                    seekBar.setProgress(progress);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalStateException e) {
                    break;
                }
            }





            }


    }

    @SuppressLint("SetTextI18n")
    public void change_to()
    {
        Log.d("yes", "exit se phle "+update_seekbar_pos .getState());
        exit=true;
        while(update_seekbar_pos.isAlive());
        Log.d("yes", "exit ke baad "+update_seekbar_pos .getState());
        if(pos[0]<0)
          pos[0]=arrayList.size()-1;
        if(pos[0]>=arrayList.size())
          pos[0]=0;
      //  set all values 0 as the new music going to start
        seekBar.setProgress(0);
        welcome.setText(""+arrayList.get(pos[0]).getName());
        welcome.setSelected(true);

     //  new music starts here
        Uri uri =Uri.parse(arrayList.get(pos[0]).toString());
        mediaplayer = MediaPlayer.create(this,uri);
     // now music starts
        mediaplayer.start();
        play.setImageResource(R.drawable.pause);
      // set seekbar and progress bar max limit
        start.setText("0:00");
        song_duration = mediaplayer.getDuration();
        seekBar.setMax(song_duration);
        song_duration/=1000;
        int minute = song_duration/60;
        int second = song_duration%60;
        end.setText(""+minute+":");
        if(second/10==0) end.setText(""+end.getText()+"0");
        end.setText(""+end.getText()+second);



        //  show all listener to system so that they recognized their path

        Music_start();



    }
    public  void Music_start()
    {



        next.setOnClickListener(v -> {
            mediaplayer.stop();
            mediaplayer.release();
            pos[0]++;
            change_to();


        });

        prev.setOnClickListener(v -> {
            mediaplayer.stop();
            mediaplayer.release();
            pos[0]--;
            change_to();


        });


        // manipulate seek bar

//         new Timer().scheduleAtFixedRate(new TimerTask() {
//             @Override
//             public void run() {
//                 Toast.makeText(Play_music.this, "agaya mai", Toast.LENGTH_SHORT).show();
//                 seekBar.setProgress(mediaplayer.getCurrentPosition());
//             }
//         },2,5000);
        Log.d("yes", "new banne ke phle "+update_seekbar_pos .getState());
        exit = false;
        update_seekbar_pos.interrupt();
        update_seekbar_pos =new seek_bar();
        update_seekbar_pos.start();
        Log.d("yes", "new banne ke baad"+update_seekbar_pos .getState());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                    mediaplayer.seekTo(progress);

                int cur_duration = (int) ((double) song_duration / seekBar.getMax() * currentPosition);
                int minute = cur_duration / 60;
                int second = cur_duration % 60;
                start.setText(""+minute+":");
                if(second/10==0) start.setText(""+start.getText()+"0");
                start.setText(""+start.getText()+second);



            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });
        // when a song is complete
        mediaplayer.setOnCompletionListener(mp -> {
       // curr son ends so start next song
           pos[0]++;
           change_to();

        });

        // listener for pause and resume explicitly
        play.setOnClickListener(v -> {
            if(mediaplayer.isPlaying()) {
                mediaplayer.pause();
                play.setImageResource(R.drawable.play);

            }
            else
            {
                mediaplayer.start();
                play.setImageResource(R.drawable.pause);



            }
        });


    }



}