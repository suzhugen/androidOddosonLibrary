package com.oddoson.android.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.oddoson.android.common.R;
import com.oddoson.android.common.interfaces.IndexSideCallback;
import com.oddoson.android.common.util.DensityUtil;

/**
 * 字母排序 导航，通讯录导航
 * @author oddoson
 * <pre>
    ArrayList<IndexSideEntity<String>> datas=new ArrayList<IndexSideEntity<String>>();
    ListView mListView2;
    void initview()
    {
        mListView2 = (ListView) findViewById(R.id.listView1);
        IndexSideBar mBar = (IndexSideBar) findViewById(R.id.indexSideBar1);
        
        char lastchar=' ';
        for (int i = 0; i < s.length; i++)
        {
            IndexSideEntity<String> item=new IndexSideEntity<String>();
            item.setFirstChar(PinYinUtil.getFirstCharPinYin(s[i]));
            if ((item.getFirstChar()>='a'&&item.getFirstChar()<='z')||(item.getFirstChar()>='A'&&item.getFirstChar()<='Z'))
            {
                
            }else {
                item.setFirstChar('#');
            }
            item.setData(s[i]);
            datas.add(item);
        }
        Collections.sort(datas,new Mycompare());//排序
        for (int i = 0; i < datas.size(); i++)
        {
            IndexSideEntity<String> item=datas.get(i);
            if (lastchar!=item.getFirstChar())
            {
                item.setIsFirst(true);
                lastchar=item.getFirstChar();
            }else {
                item.setIsFirst(false);
            }
        }
        mListView2.setAdapter(new CommonAdapter<IndexSideEntity<String>>(this, R.layout.item,
                datas)
        {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder,
                    IndexSideEntity<String> itemData, int position)
            {
                TextView tView = viewHolder.getView(R.id.first_char);
                if (itemData.getIsFirst())
                {
                    tView.setText(itemData.getFirstChar()+"");
                    tView.setVisibility(View.VISIBLE);
                }else
                {
                    tView.setVisibility(View.GONE);
                }
                viewHolder.setTextForTextView(R.id.textView1, itemData.getData());
            }
        });
        
        mBar.setCallback(new IndexSideCallback()
        {
            @Override
            public void setSelection(int position)
            {
                mListView2.setSelection(position);
            }
            
            @Override
            public int getPositionForSection(char sectionChar, int position)
            {
                int count = mListView2.getCount();
                for (int i = 0; i < count; i++)
                {
                    IndexSideEntity<String> item=(IndexSideEntity<String>) mListView2.getAdapter().getItem(i);
                    if (sectionChar==item.getFirstChar())
                    {
                        return i;
                    }
                }
                return -1;
            }
        });       
    }
    String s[] =
    { "t","我", "你", "好", "什么", "不好", "不不不","边框", "aa", "好可怕", "噢好玩", "额", "不不不", "一点都 不好玩",
            "他", "吗", "正在", "出错", "三", "啊啊", "搜索", "得到", "方法", "嘎嘎嘎", "哈哈哈",
            "吉家家", "卡卡卡", "啦啦啦", "请求", "五万五", "呃呃呃", "日日日", "他他他", "已拥有", "uu",
            "iii", "oo6", "哦哦", "潘潘潘", "23", "方法", "很多个", "个梵蒂冈", "发个", "hhh", "金骏眉",
            "453", "发个顺丰的", "法规的法规", "分W公司的", "发个都是", };
    
    class Mycompare implements Comparator<IndexSideEntity<String>>{

        @Override
        public int compare(IndexSideEntity<String> lhs,
                IndexSideEntity<String> rhs)
        {
            if (lhs.getFirstChar()>rhs.getFirstChar())
            {
                return 1;
            }else if (lhs.getFirstChar()<rhs.getFirstChar()){
                return -1;
            }
            return 0;
        }
    }
 * </pre>
 *
 */
public class IndexSideBar extends View
{
    public static final char[] chars = new char[]
    { '#', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    private TextView mDialogText;
    private int m_ItemHeight;
    private WindowManager mWindowManager;
    private IndexSideCallback callback;
    private Paint paint = new Paint();
    private RectF roundRectF;//背景矩形
    
    private Boolean isPress=false;
    private int touchIndex=-1;
    
    public IndexSideBar(Context context)
    {
        super(context);
        init(context);
    }
    
    public IndexSideBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }
    
    public IndexSideBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }
    
    private void init(Context context)
    {
        mDialogText = (TextView) LayoutInflater.from(context).inflate(
                R.layout.list_position_item, null);
        mDialogText.setVisibility(View.INVISIBLE);
        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int size = DensityUtil.dipToPx(context, 80);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(size,
                size, WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
        
        paint.setColor(0xff888888);
        paint.setTextSize(DensityUtil.spToPx(context, 16));
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
    }
    
    public void setCallback(IndexSideCallback callback)
    {
        this.callback=callback;
    }
    
    public boolean onTouchEvent(MotionEvent event)
    {
        super.onTouchEvent(event);
        switch (event.getAction())
        {
        case MotionEvent.ACTION_DOWN:
            isPress=true;
            break;
        case MotionEvent.ACTION_MOVE:
            break;
        case MotionEvent.ACTION_UP:
            isPress=false;
            break;
        default:
            break;
        }
        touch(event);
        invalidate();
        return true;
    }
    
    void touch(MotionEvent event){
        int i = (int) event.getY();
        touchIndex = i / m_ItemHeight;
        if (touchIndex >= chars.length)
        {
            touchIndex = chars.length - 1;
        }
        else if (touchIndex < 0)
        {
            touchIndex = 0;
        }
        if (callback == null)
        {
            return ;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_MOVE)
        {
            mDialogText.setVisibility(View.VISIBLE);
            mDialogText.setText(String.valueOf(chars[touchIndex]));
            int position = callback.getPositionForSection(chars[touchIndex],touchIndex);
            if (position == -1)
            {
                return ;
            }
            callback.setSelection(position);
        }
        else
        {
            mDialogText.setVisibility(View.INVISIBLE);
        }
    }
    
    
    protected void onDraw(Canvas canvas)
    {
        if (isPress)
        {
            //画背景
            if (roundRectF==null)
            {
                roundRectF=new RectF(0, 0, getWidth(), getHeight());
            }
            paint.setColor(0x44000000);
            canvas.drawRoundRect(roundRectF,10, 10, paint);
        }else
        {
            paint.setColor(0xff888888);
        }
        
        m_ItemHeight = getMeasuredHeight() /chars.length;
        float widthCenter = getMeasuredWidth() / 2;
        for (int i = 0; i < chars.length; i++)
        {
            if (i==touchIndex&&isPress)
            {
                paint.setColor(0xff02AEEE);
            }else if(isPress){
                paint.setColor(0xffffffff);
            }
            canvas.drawText(String.valueOf(chars[i]), widthCenter, m_ItemHeight
                    + (i * m_ItemHeight), paint);
        }
        super.onDraw(canvas);
    }
    
    
    @Override
    protected void onDetachedFromWindow()
    {
        if (mWindowManager!=null)
        {
            mWindowManager.removeViewImmediate(mDialogText);//立即移除 ,防止内容泄露
            mWindowManager=null;
        }
        super.onDetachedFromWindow();
    }
    
}
