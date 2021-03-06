package com.oddoson.android.common.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.oddoson.android.common.R;
import com.oddoson.android.common.entity.IndexSideEntity;
import com.oddoson.android.common.interfaces.IndexSideCallback;
import com.oddoson.android.common.pinyin.PinYinUtil;
import com.oddoson.android.common.util.ActivityUtil;
import com.oddoson.android.common.view.ClearEditText;
import com.oddoson.android.common.view.IndexSideBar;
import com.oddoson.android.common.view.IndexSideBar.PinyinComparetor;
import com.oddoson.android.common.view.adapter.CommonAdapter;
import com.oddoson.android.common.view.adapter.CommonViewHolder;

/**
 * 导航，联系人导航 demo
 * 
 * @author oddoson
 * 
 */
public class IndexSideBarActivity extends Activity implements OnClickListener {
	private ArrayList<IndexSideEntity<String>> datas = new ArrayList<IndexSideEntity<String>>();
	private ListView mListView2;
	private CommonAdapter adapter;
	private ClearEditText ClearEditText1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_indexsidebaractivity_main);
		ActivityUtil.noAutoSoftInput(this);// 禁止自动弹出输入法
		ActivityUtil.setSoftInputAdjustPan(this);
		initview();
	}

	void initview() {
		mListView2 = (ListView) findViewById(R.id.listView1);
		IndexSideBar mBar = (IndexSideBar) findViewById(R.id.indexSideBar1);

		for (int i = 0; i < s.length; i++) {
			IndexSideEntity<String> item = new IndexSideEntity<String>();
			item.setFirstChar(PinYinUtil.getFirstCharPinYin(s[i]));
			if ((item.getFirstChar() >= 'a' && item.getFirstChar() <= 'z')
					|| (item.getFirstChar() >= 'A' && item.getFirstChar() <= 'Z')) {
			} else {
				item.setFirstChar('#');
			}
			item.setData(s[i]);
			datas.add(item);
		}
		Collections.sort(datas, new PinyinComparetor());// 排序
		setIsFirstChar(datas);
		adapter = new CommonAdapter<IndexSideEntity<String>>(this,
				R.layout.demo_indexsidebaractivity_item, datas) {
			@Override
			protected void fillItemData(CommonViewHolder viewHolder,
					IndexSideEntity<String> itemData, int position) {
				TextView tView = viewHolder.getView(R.id.first_char);

				if (itemData.getIsFirst()) {
					tView.setText(itemData.getFirstChar() + "");
					tView.setVisibility(View.VISIBLE);
				} else {
					tView.setVisibility(View.GONE);
				}
				viewHolder.setTextForTextView(R.id.textView1,
						itemData.getData());
			}
		};

		mListView2.setAdapter(adapter);

		mBar.setCallback(new IndexSideCallback() {
			@Override
			public void setSelection(int position) {
				mListView2.setSelection(position);
			}

			@Override
			public int getPositionForSection(char sectionChar, int position) {
				int count = mListView2.getCount();
				for (int i = 0; i < count; i++) {
					@SuppressWarnings("unchecked")
					IndexSideEntity<String> item = (IndexSideEntity<String>) mListView2
							.getAdapter().getItem(i);
					if (sectionChar == item.getFirstChar()) {
						return i;
					}
				}
				return -1;
			}
		});

		ClearEditText1 = (ClearEditText) findViewById(R.id.clearEditText1);
		ClearEditText1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				filterData(arg0.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<IndexSideEntity<String>> filterDateList = new ArrayList<IndexSideEntity<String>>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = datas;
		} else {
			filterDateList.clear();
			for (IndexSideEntity<String> item : datas) {
				String name = item.getData();
				if (name.indexOf(filterStr.toString()) != -1) {
					filterDateList.add(item);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, new PinyinComparetor());
		setIsFirstChar(filterDateList);
		adapter.setDatas(filterDateList);
	}
	
	void setIsFirstChar(List<IndexSideEntity<String>> filterDateList){
		char lastchar=' ';
		for (int i = 0; i < filterDateList.size(); i++) {
			IndexSideEntity<String> item = filterDateList.get(i);
			if (lastchar != item.getFirstChar()) {
				item.setIsFirst(true);
				lastchar = item.getFirstChar();
			} else {
				item.setIsFirst(false);
			}
		}
	}

	String s[] = { "t", "我", "你", "好", "什么", " ", "不好", "不不不", "边框", "aa",
			"好可怕", "噢好玩", "额", "不不不", "一点都 不好玩", "他", "吗", "正在", "出错", "三",
			"啊啊", "搜索", "得到", "方法", "嘎嘎嘎", "哈哈哈", "吉家家", "卡卡卡", "啦啦啦", "请求",
			"五万五", "呃呃呃", "日日日", "他他他", "已拥有", "uu", "iii", "oo6", "哦哦", "潘潘潘",
			"23", "方法", "很多个", "个梵蒂冈", "发个", "hhh", "金骏眉", "453", "发个顺丰的",
			"法规的法规", "分W公司的", "发个都是", "重启", "重庆" };

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		default:
			break;
		}
	}

}
