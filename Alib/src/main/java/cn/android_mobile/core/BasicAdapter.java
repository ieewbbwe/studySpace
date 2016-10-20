package cn.android_mobile.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public abstract class BasicAdapter extends BaseAdapter {
	protected DisplayImageOptions options;
	protected LayoutInflater mInflater;
	protected Context cxt;
	protected ImageLoadingListener animate = new AnimateFirstDisplayListener();
	protected List<?> list;
	public BasicAdapter(Context context,List<?> list){
		this.cxt = context;
//		options = new DisplayImageOptions.Builder()
//				.showStubImage(R.drawable.default_image_160x160)
//				.showImageForEmptyUri(R.drawable.default_image_160x160)
//				.showImageOnFail(R.drawable.default_image_160x160)
//				.displayer(new RoundedBitmapDisplayer(0)).build();
		mInflater = LayoutInflater.from(cxt);
		this.list=list;
	}
	public void setList(List<?> list){
		this.list=list;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

//	@Override
//	public View getView(int position, View v, ViewGroup parent)
//	 HolderView holder = null;
//		if (null == v) {
//			v = mInflater.inflate(R.layout.fragment_home_category_item, null);
//			holder = new HolderView();
//			holder.name = (TextView) v.findViewById(R.id.fragment_home_category_title);
//			v.setTag(holder);
//		} else {
//			holder = (HolderView) v.getTag();
//		}
//		if (position < HomeModel.getInstance().categoryArray.size()) {
//			Category c = HomeModel.getInstance().categoryArray.get(position);
//			holder.name.setText(c.snippet.title);
//			Lg.print(c.snippet.title);
//		}
//		return v;
//	}
//
//	public class HolderView {
//		private TextView name;
//	}
	 
}
