package com.oddoson.android.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.oddoson.android.common.R;
import com.oddoson.android.media.VoicePlayerManager.VoicePlayCallback;

/**
 * 播放器view
 * 
 * @author oddoson
 * 
 *         <pre>
 * mVoiceView = (VoiceView) findViewById(R.id.voiceView1);
 * mVoiceView.setVoicePath(path);// 设置播放路径
 * </pre>
 */
public class VoiceView extends FrameLayout implements OnClickListener,
        VoicePlayCallback
{
    Context mContext;
    View parentView;
    ImageButton play_btn;
    SeekBar play_seekBar;
    TextView timeTextView;
    VoicePlayerManager voicePlayerManager;
    String path;
    Handler mHandler;
    
    private boolean isPause = false;
    
    public VoiceView(Context context)
    {
        super(context);
        mContext = context;
        init();
    }
    
    public VoiceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        init();
    }
    
    void init()
    {
        initView();
        mHandler = new Handler();
        initManager();
    }
    
    void initView()
    {
        parentView = LayoutInflater.from(mContext).inflate(R.layout.voice_view,
                null);
        addView(parentView);
        play_btn = (ImageButton) parentView.findViewById(R.id.play_btn);
        play_seekBar = (SeekBar) parentView.findViewById(R.id.play_seekBar);
        timeTextView = (TextView) findViewById(R.id.time_tv);
        play_btn.setOnClickListener(this);
        play_seekBar.setEnabled(false);
        play_seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if (voicePlayerManager.isPlaying())
                {
                    voicePlayerManager.seekToPercent(seekBar.getProgress());
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser)
            {
            }
        });
    }
    
    void initManager()
    {
        voicePlayerManager = new VoicePlayerManager("");
        voicePlayerManager.setPlayListener(this);
    }
    
    /**
     * 设置音频时长
     * 
     * @param time
     */
    public void setTimeText(String time)
    {
        timeTextView.setText(time);
    }
    
    void setTimeView()
    {
        timeTextView.setText(parseTime(voicePlayerManager.getLength()));
    }
    
    /**
     * 00:00的时间格式
     * 
     * @param time
     * @return
     */
    String parseTime(int time)
    {
        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }
    
    /**
     * 设置播放路径，可以是 网络url 或sd卡上的音频
     */
    public void setVoicePath(String path)
    {
        this.path = path;
    }
    
    public void play()
    {
        if (TextUtils.isEmpty(path))
        {
            throw new RuntimeException("请设置播放路径");
        }
        play_seekBar.setProgress(0);
        voicePlayerManager.setFilePath(path);
        if (voicePlayerManager.isPlaying())
        {
            voicePlayerManager.stop();
        }
        voicePlayerManager.play();
        play_btn.setImageResource(R.drawable.play_pause_normal);
        mHandler.postDelayed(seekRunnable, 1000);
    }
    
    public void stop()
    {
        play_seekBar.setProgress(0);
        updateUiPause();
        voicePlayerManager.stop();
        mHandler.removeCallbacks(seekRunnable);
    }
    
    public void pause()
    {
        updateUiPause();
        voicePlayerManager.pause();
        mHandler.removeCallbacks(seekRunnable);
    }
    
    public void resume()
    {
        if (voicePlayerManager.isOk())
        {
            updateUiPlay();
            voicePlayerManager.resume();
            mHandler.postDelayed(seekRunnable, 500);
        }
    }
    
    public boolean isPlaying()
    {
        return voicePlayerManager.isPlaying();
    }
    
    private Runnable seekRunnable = new Runnable()
    {
        
        @Override
        public void run()
        {
            if (voicePlayerManager.isPlaying())
            {
                int percent = voicePlayerManager.getPosition() * 100
                        / voicePlayerManager.getLength();
                Log.i("voicePlay....", "play percent=" + percent);
                play_seekBar.setProgress(percent);
            }
            mHandler.postDelayed(seekRunnable, 200);
        }
    };
    
    void updateUiPlay()
    {
        play_btn.setImageResource(R.drawable.play_pause_normal);
        play_seekBar.setEnabled(true);
    }
    
    void updateUiPause()
    {
        play_btn.setImageResource(R.drawable.play_pause);
        play_seekBar.setEnabled(false);
    }
    
    /**
     * 进度条开关
     * 
     * @param show
     */
    public void setShowSeekBar(Boolean show)
    {
        if (show)
        {
            play_seekBar.setVisibility(View.VISIBLE);
        }
        else
        {
            play_seekBar.setVisibility(View.GONE);
        }
    }
    
    /**
     * 时间开关
     * 
     * @param show
     */
    public void setShowTimeView(Boolean show)
    {
        if (show)
        {
            timeTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            timeTextView.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onClick(View v)
    {
        if (v.getId()==R.id.play_btn)
        {
            if (voicePlayerManager.isStart() && !voicePlayerManager.isPrepare())
            {
                // 资源未准备好。
                stop();
            }
            else if (voicePlayerManager.isOk()
                    && voicePlayerManager.isPlaying())
            {
                pause();
            }
            else
            {
                if (voicePlayerManager.isOk()
                        && voicePlayerManager.getPosition() > 0)
                    resume();
                else
                    play();
            }
        }
    }
    
    public void onDestory()
    {
        updateUiPause();
        play_seekBar.setProgress(0);
        mHandler.removeCallbacks(seekRunnable);
        voicePlayerManager.destory();
    }
    
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        onDestory();
    }
    
    @Override
    public void onPrepared(MediaPlayer mp)
    {
        setTimeView();
        if (voicePlayerManager.isStart())
        {
            updateUiPlay();
        }
    }
    
    @Override
    public void onCompleted(MediaPlayer mp)
    {
        updateUiPause();
        play_seekBar.setProgress(0);
        mHandler.removeCallbacks(seekRunnable);
    }
    
    @Override
    public void onError(MediaPlayer mp, int what, String msg)
    {
        updateUiPause();
        play_seekBar.setProgress(0);
        mHandler.removeCallbacks(seekRunnable);
    }
    
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent)
    {
        // TODO Auto-generated method stub
        
    }
    
}
