package com.odddoson.android.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;

/**
 * 录音，可暂停
 * 
 * @author oddoson
 * 
 */
public class RecordManager
{
    
    private static int SAMPLE_RATE_IN_HZ = 8000;
    private RecordLister callback;
    private Handler mHandler;
    private MediaRecorder recorder;
    public String path;// 最终保存地址
    private Chronometer chronometer;// 录音计时器
    private boolean isRecording = false;
    private ArrayList<String> snippetList;// 临时保存片段 ，用于暂停回复
    private String snipperPath = "";
    private String filePath = null;
    private String fileSuffixes = ".amr";
    
    private int totalTime = 120;// 总计可录用多长时间,单位分钟
    private long recordTime = 0;// 单位：秒 当前录制时间
    
    /**
     * 
     * 
     * @param name
     *            文件名，不需要路径
     * @param chronometer
     */
    public RecordManager(String name, Chronometer chronometer)
    {
        this.path = sanitizePath(name);
        this.chronometer = chronometer;
        snippetList = new ArrayList<String>();
        initHandle();
    }
    
    void initHandle()
    {
        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch (msg.what)
                {
                case 0:
                    if (callback != null)
                    {
                        callback.onFailed();
                    }
                    break;
                case 1:
                    if (callback != null)
                    {
                        callback.onComplete(path);
                    }
                    break;
                
                default:
                    break;
                }
            }
        };
    }
    
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }
    
    private String sanitizePath(String name)
    {
        if (!name.startsWith("/"))
        {
            name = "/" + name;
        }
        if (!name.contains("."))
        {
            name += fileSuffixes;
        }
        if (TextUtils.isEmpty(filePath))
        {
            return Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/cha4.net/Voice" + name;
        }
        else
        {
            return filePath + name;
        }
    }
    
    public void setRecordLister(RecordLister callback)
    {
        this.callback = callback;
    }
    
    public void record(String path) throws IOException
    {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
        recorder.setOutputFile(path);
        recorder.prepare();
        recorder.start();
        initTimer();
    }
    
    @SuppressWarnings("deprecation")
    public void start() throws IOException
    {
        isRecording = true;
        recordTime = 0;
        snippetList.clear();
        snipperPath = newSnippetName();
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED))
        {
            throw new IOException("SD Card is not mounted,It is  " + state);
        }
        File directory = new File(path).getParentFile();
        if (!directory.exists() && !directory.mkdirs())
        {
            throw new IOException("Path to file could not be created");
        }
        snippetList.add(snipperPath);
        record(snipperPath);
    }
    
    public void resume() throws IOException
    {
        isRecording = true;
        snipperPath = newSnippetName();
        snippetList.add(snipperPath);
        record(snipperPath);
    }
    
    private String newSnippetName()
    {
        return path + "." + snippetList.size();
    }
    
    public void pause()
    {
        isRecording = false;
        timeFinished();
        recorder.stop();
        recorder.release();
    }
    
    /**
     * 停止录音并保存
     * 
     * @return 录音时长
     * @throws IOException
     */
    public long stop()
    {
        timeFinished();
        isRecording = false;
        if (isRecording)
        {
            recorder.stop();
            recorder.release();
        }
        save();
        return recordTime;
    }
    
    public void cancel()
    {
        recordTime=0;
        timeFinished();
        isRecording = false;
        if (isRecording)
        {
            recorder.stop();
            recorder.release();
        }
        clearSnippet();
    }
    
    private void save()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                boolean b = mergeFiles();
                if (b)
                    mHandler.sendEmptyMessage(1);
                else
                    mHandler.sendEmptyMessage(0);
            }
        }).start();
    }
    
    /**
     * 合并音频
     */
    public boolean mergeFiles()
    {
        try
        {
            File realFile = new File(path);
            FileOutputStream fos = new FileOutputStream(realFile);
            RandomAccessFile ra = null;
            for (int i = 0; i < snippetList.size(); i++)
            {
                File tmpFile = new File(snippetList.get(i));
                ra = new RandomAccessFile(tmpFile, "r");
                if (i != 0)
                {
                    ra.seek(6);// 跳过amr文件的文件头
                }
                byte[] buffer = new byte[1024 * 8];
                int len = 0;
                while ((len = ra.read(buffer)) != -1)
                {
                    fos.write(buffer, 0, len);
                }
            }
            ra.close();
            fos.close();
            clearSnippet();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    private void clearSnippet()
    {
        for (int i = 0; i < snippetList.size(); i++)
        {
            File tmpFile = new File(snippetList.get(i));
            tmpFile.delete();
        }
        snippetList.clear();
    }
    
    /**
     * 是否正在录音
     * 
     * @return
     */
    public boolean isRecording()
    {
        return isRecording;
    }
    
    /**
     * 最大录音时长
     * 
     * @param max
     *            单位（分）
     */
    public void setMaxRecordTime(int max)
    {
        totalTime = max;
    }
    
    private void initTimer()
    {
        chronometer.setBase(SystemClock.elapsedRealtime() - recordTime * 1000);
        chronometer
                .setOnChronometerTickListener(new OnChronometerTickListener()
                {
                    @Override
                    public void onChronometerTick(Chronometer chronometer)
                    {
                        if (recordTime >= totalTime * 60)
                        {
                            timeFinished();
                            stop();
                            return;
                        }
                        recordTime = (SystemClock.elapsedRealtime() - chronometer
                                .getBase()) / 1000;
                    }
                });
        chronometer.start();
    }
    
    /**
     * 当前录音时长
     * 
     * @return
     */
    public long getRecordTime()
    {
        return recordTime;
    }
    
    private void timeFinished()
    {
        if (null != chronometer)
        {
            chronometer.stop();
        }
    }
    
    private int BASE = 600;
    
    /**
     * 振幅
     * 
     * @return
     */
    public double getAmplitude()
    {
        if (recorder != null)
            return (recorder.getMaxAmplitude());
        else
            return 0;
    }
    
    /**
     * 当前声音大小,0-5 档次
     */
    public int getMicVoice()
    {
        int ratio = recorder.getMaxAmplitude() / BASE;
        int db = 0;// 分贝
        if (ratio > 1)
            db = (int) (20 * Math.log10(ratio));
        switch (db / 4)
        {
        case 0:
            return 0;
        case 1:
            return 1;
        case 2:
            return 2;
        case 3:
            return 3;
        case 4:
            return 4;
        case 5:
            return 5;
        default:
            return 0;
        }
    }
    
   public interface RecordLister
    {
        /**
         * 完成录音,保存文件异步通知（也可能到最大时长，自动停止）
         * 
         * @param path
         */
        void onComplete(String path);
        
        void onFailed();
    }
    
}
