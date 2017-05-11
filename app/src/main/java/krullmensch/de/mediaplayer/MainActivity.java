package krullmensch.de.mediaplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    SeekBar sb;
    ImageView imgview;
    CheckBox checked1, checked;
    MediaPlayer mPlayer;
    TextView timePos, timeDur;
    Handler handler;
    Runnable runnable;

    private ImageButton toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgview = (ImageView) findViewById(R.id.centerIcon);
        checked = (CheckBox) findViewById(R.id.checked);
        checked1 = (CheckBox) findViewById(R.id.checked1);
        timePos = (TextView) findViewById(R.id.timePos);
        timeDur = (TextView) findViewById(R.id.timeDur);
        sb = (SeekBar) findViewById(R.id.seekbar);
        handler = new Handler();
        toast = (ImageButton) findViewById(R.id.showToast);
        imgview = (ImageView) findViewById(R.id.imgview);
        if (imgview != null) {
            imgview.setBackgroundResource(android.R.color.holo_blue_dark);
            toast.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "INFORMATION", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        }

        checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checked.isChecked()) {
                    mPlayer.setLooping(true);
                } else {
                    mPlayer.setLooping(false);
                }
            }
        });
        if (mPlayer == null) {
            if (musicDir.exists()) {
                songList = musicDir.list();
                for (int i = 0; i < songList.length; i++) {
                    System.out.println(songList[i]);
                }
                if (songList.length != 0) {
                    playSong(new File(musicDir, songList[currentFileIndex]));
                }
            }
        }

        if (mPlayer != null) {
            // setting the onComplete listener for the current playing song
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playNext();
                }
            });
        }

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if (input) {
                    if (mPlayer != null) {
                        mPlayer.seekTo(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);


    private String[] songList;
    private int currentFileIndex = 0;

    private void playNext() {
        int newIndex = currentFileIndex + 1;
        if (songList.length < newIndex) {
            currentFileIndex = newIndex;
        } else {
            currentFileIndex = 0;
        }
        File f = new File(musicDir, songList[newIndex]);
        playSong(f);
        mPlayer.start();
    }

    private void playPrev() {
        int newIndex = currentFileIndex - 1;
        if (songList.length < newIndex) {
            currentFileIndex = newIndex;
        } else {
            currentFileIndex = 0;
        }
        File f = new File(musicDir, songList[currentFileIndex]);
        playSong(f);
        mPlayer.start();
    }

    private void playSong(File song) {
        if (song != null && song.exists()) {
            Uri mySong = Uri.parse(song.getAbsolutePath());
            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer = MediaPlayer.create(this, mySong);
            if (mPlayer != null) {
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                sb.setMax(mPlayer.getDuration());
            }
        }
    }

    private void startStopMedia() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
    }

    private void sbPositioningAndTime() {
        sb.setProgress(mPlayer.getCurrentPosition());
        int mPos = mPlayer.getCurrentPosition();
        timePos.setText(getTime(mPos));
        int mDur = mPlayer.getDuration();
        timeDur.setText(getTime(mDur));


        if (mPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    sbPositioningAndTime();
                }
            };
            handler.postDelayed(runnable, 1);
        }
    }

    public String getTime(double time) {
        String finalTimerString = "";
        String secondsString = "";

        int hours = (int) (time / (1000 * 60 * 60));
        int minutes = (int) (time % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((time % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        return finalTimerString;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sbPositioningAndTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        handler.removeCallbacks(runnable);
    }

    private void changeIcon(View view) {
        ImageButton play = (ImageButton) view;
        int icon;
        if (mPlayer.isPlaying()) {
            icon = R.drawable.media_play;

        } else {
            mPlayer.isPlaying();
            icon = R.drawable.media_pause;
        }
        play.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
    }

    public void onPlayButtonPress(View view) {
        changeIcon(view);
        startStopMedia();
        sbPositioningAndTime();
    }

    public void nextSong(View view) {
        playNext();
    }

    public void prevSong(View view) {
        playPrev();
    }

    public void changeA(View view) {
        Intent intent = new Intent(getApplicationContext(), Activity2.class);
        startActivity(intent);
    }
}





