package com.odddoson.android.media;

import java.io.File;
import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.util.Log;
import android.webkit.URLUtil;

/**
 * 音频播放
 * 
 * @author oddoson
 * 
 *         调用属性时请在VoicePlayCallback.onPrepared()完成之后调用，如 恢复，获取时间 等,
 *         需调用destory销毁资源
 * 
 */
public class VoicePlayerManager implements OnCompletionListener,
        MediaPlayer.OnPreparedListener, OnBufferingUpdateListener,
        OnErrorListener, OnSeekCompleteListener
{
    private MediaPlayer player;
    private int position = 0;// 播放保存位置
    private String filename;
    private boolean isPrepare = false;// 资源是否准备完成
    private boolean isStart = false;// 是否调用了play()=true,或stop()=false
    
    private VoicePlayCallback callback;
    
    public VoicePlayerManager(String filename)
    {
        this.filename = filename;
    }
    
    /**
     * 设置播放文件
     * 
     * @param filename
     */
    public void setFilePath(String filename)
    {
        this.filename = filename;
    }
    
    /**
     * 初始化播放器,重置为初始状态
     * 
     * @throws IOException
     * @throws IllegalStateException
     * @throws SecurityException
     * @throws IllegalArgumentException
     */
    private void init()
    {
        isPrepare = false;
        if (player != null)
        {
            player.release();
        }
        try
        {
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnBufferingUpdateListener(this);
            player.setOnPreparedListener(this);
            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);
            player.setOnSeekCompleteListener(this);
            player.reset();// 重置为初始状态
            if (URLUtil.isNetworkUrl(filename))
            {
                player.setDataSource(filename);
            }
            else
            {
                // 获取文件路径
                File audioFile = new File(filename);
                player.setDataSource(audioFile.getAbsolutePath());
            }
            player.prepareAsync();
        }
        catch (Exception e)
        {
            player.reset();// 重置为初始状态
            if (callback != null)
            {
                callback.onError(player, 0, "资源错误");
            }
        }
    }
    
    public boolean playerIsNull()
    {
        return player == null;
    }
    
    /**
     * 是否已调用play().
     * 
     * @return
     */
    public boolean isStart()
    {
        return isStart;
    }
    
    /**
     * 资源是否已准备好
     * @return
     */
    public boolean isPrepare()
    {
        return isPrepare;
    }
    
    public boolean isPlaying()
    {
        if (player == null)
        {
            return false;
        }
        return player.isPlaying();
    }
    
    /**
     * 播放，初始化资源
     * 
     * @throws IOException
     */
    public void play()
    {
        isStart = true;
        init();
    }
    
    /**
     * 播放器准备完成，可以进行任何操作
     * 
     * @return
     */
    public boolean isOk()
    {
        return isStart && isPrepare;
    }
    
    /**
     * 转跳，毫秒
     * 
     * @param msec
     */
    public void seekTo(int msec)
    {
        if (isOk())
            player.seekTo(msec);
    }
    
    public void seekToPercent(int percent)
    {
        if (isOk())
            player.seekTo(getLength() * percent / 100);
    }
    
    public void stop()
    {
        if (isOk())
        {
            player.stop();
        }
        isStart = false;
        isPrepare = false;
        position = 0;
    }
    
    public void pause()
    {
        if (isOk())
        {
            // 保存当前播放点
            position = player.getCurrentPosition();
            player.pause();
        }
    }
    
    protected void resume()
    {
        if (isOk() && position > 0 && filename != null)
        {
            try
            {
                player.start();
                position = 0;
            }
            catch (Exception e)
            {
            }
        }
    }
    
    /**
     * 当前播放位置
     * 
     * @return
     */
    public int getPosition()
    {
        if (!isOk())
            return 0;
        return player.getCurrentPosition();
    }
    
    /**
     * 音频长度
     * 
     * @return 毫秒
     */
    public int getLength()
    {
        if (!isOk())
            return 0;
        return player.getDuration();
    }
    
    public void destory()
    {
        isStart=false;
        isPrepare = false;
        position = 0;
        if (player != null)
        {
            stop();
            player.release();
            player = null;
        }
    }

    
    /**
     * 网络音频下载百分比
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent)
    {
        if (percent < 100)
        {
            Log.i("VoicePlayerManager", "已缓冲 percent=" + percent);
        }
        callback.onBufferingUpdate(mp, percent);
    }
    
    // 通过onPrepared播放
    @Override
    public void onPrepared(MediaPlayer mp)
    {
        Log.i("VoicePlayerManager", "onPrepared....");
        isPrepare = true;
        if (isStart)
        {
            mp.start();
        }
        if (callback != null)
        {
            callback.onPrepared(mp);
        }
    }
    
    @Override
    public void onCompletion(MediaPlayer mp)
    {
        position = 0;
        stop();
        if (callback != null)
        {
            callback.onCompleted(mp);
        }
    }
    
    public void setPlayListener(VoicePlayCallback callback)
    {
        this.callback = callback;
    }
    
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra)
    {
        Log.e("VoicePlayerManager", "播放器播放错误");
        isPrepare = false;
        stop();
        if (callback != null)
        {
            callback.onError(mp, what, "播放资源无效");
        }
        return false;
    }
    
    @Override
    public void onSeekComplete(MediaPlayer mp)
    {
        mp.start();
    }
    
    /**
     * 播放状态监听器
     * 
     * @author Administrator
     * 
     */
    public interface VoicePlayCallback
    {
        /**
         * 资源准备完成
         * 
         * @param mp
         */
        void onPrepared(MediaPlayer mp);
        
        void onCompleted(MediaPlayer mp);
        
        void onError(MediaPlayer mp, int what, String msg);
        
        /**
         * 网络下载进度
         * 
         * @param mp
         * @param percent
         */
        void onBufferingUpdate(MediaPlayer mp, int percent);
    }
    
}
