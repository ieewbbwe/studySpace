package cn.android_mobile.core.ui.pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import cn.android_mobile.core.BasicActivity;
import cn.android_mobile.core.R;
import cn.android_mobile.core.ui.BasicComponent;
import cn.android_mobile.core.ui.pinyin.SideBar.OnTouchingLetterChangedListener;
import cn.android_mobile.toolkit.Hanyu;

public class BasicTypeSortComponent extends BasicComponent {

	public BasicTypeSortComponent(BasicActivity activity, ViewGroup v) {
		super(activity, v);
	}
	public BasicTypeSortComponent(BasicActivity activity, int v) {
		super(activity, v);
	}
	public interface IBasicTypeSortComponent{
		public void onClickResult(SortModel sm);
		public void onScroll();
	}
	private IBasicTypeSortComponent listener;
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	public SortAdapter adapter;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	public void initComp() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) findViewById(R.id.basic_type_sort_sidrbar);
		dialog = (TextView) findViewById(R.id.basic_type_sort_dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}
			}
		});

		sortListView = (ListView) findViewById(R.id.basic_type_sort_listview);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
//				toast(((SortModel) adapter.getItem(position)).name);
				if(listener!=null){
					listener.onClickResult((SortModel) adapter.getItem(position));
				}
			}
		});
		sortListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				if(listener!=null){
					listener.onScroll();
				}
			}
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				
			}
		});

		SourceDateList = filledData(view.getResources().getStringArray(
				R.array.test_date));
		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(activity, SourceDateList);
		sortListView.setAdapter(adapter);
	}
	public void uploadData(List<SortModel> list){
		Collections.sort(list, pinyinComparator);
		adapter.updateListView(list);
	}
	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	public List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.name=(date[i]);
			// 汉字转换成拼音
//			String pinyin = characterParser.getSelling(date[i]);
//			String sortString = pinyin.substring(0, 1).toUpperCase();
			String sortString =Hanyu.getFirstChar(date[i]).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.sortLetters=sortString.toUpperCase();
			} else {
				sortModel.sortLetters="#";
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	public void isDisplayIcon(boolean b){
		if(adapter!=null){
			adapter.isDisplayIcon(b);
		}
	}
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.name;
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	public IBasicTypeSortComponent getListener() {
		return listener;
	}

	public void setListener(IBasicTypeSortComponent listener) {
		this.listener = listener;
	}

	@Override
	public int onCreate() {
		// TODO Auto-generated method stub
		return R.layout.activity_basic_type_sort;
	}
	@Override
	public void initListener() {
		
	}
}
