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
import java.io.FileDescriptor;

import static krullmensch.de.mediaplayer.R.drawable.media_pause;


public class MainActivity extends AppCompatActivity {

    SeekBar sb;
    ImageView imgview;
    CheckBox checked1, checked;
    MediaPlayer mPlayer;
    TextView timePos, timeDur;
    Handler handler;
    Runnable runnable;
    private boolean isCurrentBackgroundBlue = true;
    private boolean paused = true;
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
            File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            if (musicDir.exists()) {
                String[] songs = musicDir.list();
                for (int i = 0; i < songs.length; i++) {
                    System.out.println(songs[i]);
                }
                if (songs.length != 0) {
                    File myMusicFile = new File(musicDir, songs[0]);
                    if (myMusicFile.exists()) {
                        Uri mySong = Uri.parse(myMusicFile.getAbsolutePath());
                        mPlayer = MediaPlayer.create(this, mySong);
                        if (mPlayer != null) {
                            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            sb.setMax(mPlayer.getDuration());
                            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                                    if (input) {
                                        mPlayer.seekTo(progress);
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
                    }
                }
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
        if (paused) {
            paused = false;
            icon = R.drawable.media_pause;

        } else {
            paused = true;
            icon = R.drawable.media_play;
        }
        play.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
    }

    public void onPlayButtonPress(View view) {
        changeIcon(view);
        startStopMedia();
        sbPositioningAndTime();
    }

    public void changeColor(View view) {
        if (imgview != null) {
            if (isCurrentBackgroundBlue) {
                imgview.setBackgroundResource(android.R.color.holo_orange_dark);
                isCurrentBackgroundBlue = false;
            } else {
                imgview.setBackgroundResource(android.R.color.holo_blue_dark);
                isCurrentBackgroundBlue = true;
            }
        }
    }

    public void changeColor2(View view) {
        if (imgview != null) {
            if (isCurrentBackgroundBlue) {
                imgview.setBackgroundResource(android.R.color.holo_orange_dark);
                isCurrentBackgroundBlue = false;
            } else {
                imgview.setBackgroundResource(android.R.color.holo_blue_dark);
                isCurrentBackgroundBlue = true;
            }
        }
    }

    public void changeA(View view) {
        Intent intent = new Intent(getApplicationContext(), Activity2.class);
        startActivity(intent);
    }
}





