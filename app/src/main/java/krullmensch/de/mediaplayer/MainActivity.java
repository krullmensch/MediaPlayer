package krullmensch.de.mediaplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
    TextView timePos, timeDur;
    CheckBox checked1, checked;
    MediaPlayer mPlayer;
    Handler handler;
    ImageButton play;
    Button charts;
    TextView songName, aArtistName, albumName;
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgview = (ImageView) findViewById(R.id.centerIcon);
        checked = (CheckBox) findViewById(R.id.checked);
        checked1 = (CheckBox) findViewById(R.id.checked1);
        timePos = (TextView) findViewById(R.id.timePos);
        timeDur = (TextView) findViewById(R.id.timeDur);
        play = (ImageButton) findViewById(R.id.play);
        charts = (Button) findViewById(R.id.charts);
        songName = (TextView) findViewById(R.id.songName);
        aArtistName = (TextView) findViewById(R.id.aArtistName);
        albumName = (TextView) findViewById(R.id.albumName);
        sb = (SeekBar) findViewById(R.id.seekbar);
        handler = new Handler();

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
                    playSong(new File(musicDir, songList[currentFileIndex]), false);
                }
            }
        }

        if (mPlayer != null) {
            // setting the onComplete listener for the current playing song
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mPlayer) {
                    playNext();
                    sbPositioningAndTime();
                }
            });
        }

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if (input) {
                    if (mPlayer != null) {
                        mPlayer.seekTo(progress);
                        int mPos = mPlayer.getCurrentPosition();
                        timePos.setText(getTime(mPos));
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

    public void playNext() {
        int newIndex = currentFileIndex + 1;
        if (songList.length > newIndex) {
            currentFileIndex = newIndex;
        } else {
            currentFileIndex = 0;
        }
        sbPositioningAndTime();
        File f = new File(musicDir, songList[currentFileIndex]);
        playSong(f, mPlayer.isPlaying());
    }

    public void playPrev() {
        if (currentFileIndex == 0) {
            currentFileIndex = songList.length - 1;
        } else {
            currentFileIndex--;
        }
        sbPositioningAndTime();
        File f = new File(musicDir, songList[currentFileIndex]);
        playSong(f, mPlayer.isPlaying());
    }

    private void playSong(File song, boolean doPlay) {
        if (song != null && song.exists()) {
            Uri mySong = Uri.parse(song.getAbsolutePath());
            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer = MediaPlayer.create(this, mySong);
            if (mPlayer != null) {
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                sb.setMax(mPlayer.getDuration());
                showSongMeta(song);
            }
            if (doPlay) {
                mPlayer.start();
                changeIcon(play);
            }
            sbPositioningAndTime();
        }
    }

    private void startStopMedia() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            sbPositioningAndTime();
        } else {
            mPlayer.start();
            sbPositioningAndTime();
        }
    }

    public String titleName;
    public String artistName;
    public String album;

    private void showSongMeta(File mp3File) {
        if (mPlayer != null) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mp3File.getAbsolutePath());
            titleName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            songName.setText(titleName);
            artistName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
            aArtistName.setText(artistName);
            album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            albumName.setText(album);
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
            handler.postDelayed(runnable, 500);
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
            icon = R.drawable.media_pause;
        } else {
            mPlayer.isPlaying();
            icon = R.drawable.media_play;
        }
        play.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
    }

    public void onPlayButtonPress(View view) {
        startStopMedia();
        changeIcon(view);
        sbPositioningAndTime();
    }

    public void nextSong(View view) {
        playNext();
    }

    public void prevSong(View view) {
        playPrev();
    }

    public void charts(View view) {
        Intent intent = new Intent(getApplicationContext(), chartsActivity.class);
        startActivity(intent);
    }

    public void showInfo(View view) {
        Toast toast = Toast.makeText(getBaseContext(), "Made by Marvin Krullmann at Arvato" ,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}





