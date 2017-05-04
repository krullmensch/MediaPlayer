package krullmensch.de.mediaplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

import java.nio.channels.SelectionKey;


public class MainActivity extends AppCompatActivity {

    SeekBar sb;
    ImageView imgview;
    CheckBox checked1;
    CheckBox checked;
    MediaPlayer mPlayer;
    private boolean isCurrentBackgroundBlue = true;
    private boolean paused = true;
    private ImageButton toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgview = (ImageView) findViewById(R.id.centerIcon);
        checked1 = (CheckBox) findViewById(R.id.checked1);
        checked1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked1.isChecked()) {
                    imgview.setImageResource(R.drawable.media_shuffle);
                } else {
                    imgview.setImageDrawable(null);
                }
            }
        });
        sb = (SeekBar) findViewById(R.id.seekbar);
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

        checked = (CheckBox) findViewById(R.id.checked);
        checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checked.isChecked()) {
                    imgview.setImageResource(R.drawable.media_repeat);
                } else {
                    imgview.setImageDrawable(null);
                }
            }
        });
    }
    private void startStopMedia() {
        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(this, R.raw.music);
        }

        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
    }

    private void changeIcon(View view) {
        ImageButton play = (ImageButton) view;
        int icon;
        if (paused) {
            paused = false;
            icon = android.R.drawable.ic_media_pause;

            if (sb != null) {
                sb.setProgress(50);
            }
        } else {
            paused = true;
            icon = android.R.drawable.ic_media_play;
            sb.setProgress(100);
        }
        play.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
    }

    public void onPlayButtonPress(View view) {
        startStopMedia();
        changeIcon(view);
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





