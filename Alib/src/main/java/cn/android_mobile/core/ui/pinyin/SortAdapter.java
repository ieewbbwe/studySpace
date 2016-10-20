package cn.android_mobile.core.ui.pinyin;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import cn.android_mobile.core.R;
import cn.android_mobile.toolkit.Lg;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;
	private boolean isDisplayIcon=true;
	public void isDisplayIcon(boolean b){
		isDisplayIcon=b;
	}
	public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public void updateListView(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.activity_basic_type_sort_item, null);
			viewHolder.tvTitle = (TextView) view
					.findViewById(R.id.basic_type_sort_item_title);
			viewHolder.tvLetter = (TextView) view
					.findViewById(R.id.basic_type_sort_item_catalog);
			viewHolder.ivImg = (ImageView) view
					.findViewById(R.id.basic_type_sort_item_img);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
			viewHolder.ivImg.setImageResource(R.drawable.home_nav_carbg);
		}
		if(!isDisplayIcon){
			viewHolder.ivImg.setVisibility(View.GONE);
		}
		int section = getSectionForPosition(position);

		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.sortLetters);
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tvTitle.setText(this.list.get(position).name);
		if (list.get(position).img!=null&&(list.get(position).img.toLowerCase().endsWith(".png")
				|| list.get(position).img.toLowerCase().endsWith(".jpg")
				|| list.get(position).img.toLowerCase().endsWith(".jpeg")))
			ImageLoader.getInstance().displayImage(list.get(position).img,
					viewHolder.ivImg);
//		Lg.print(list.get(position).img);
		return view;
	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		ImageView ivImg;
	}

	public int getSectionForPosition(int position) {
		return list.get(position).sortLetters.charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).sortLetters;
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}