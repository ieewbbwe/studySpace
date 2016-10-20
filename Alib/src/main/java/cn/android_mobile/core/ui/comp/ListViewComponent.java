package cn.android_mobile.core.ui.comp;

import java.text.DateFormat;
import java.util.Date;

import android.view.View;
import android.widget.ListView;
import cn.android_mobile.core.BasicActivity;
import cn.android_mobile.core.BasicAdapter;
import cn.android_mobile.core.R;
import cn.android_mobile.core.base.BaseComponent;
import cn.android_mobile.core.ui.PullRefreshLayout;
import cn.android_mobile.core.ui.PullRefreshLayout.OnRefreshListener;
import cn.android_mobile.core.ui.listener.AutoLoadListener;
import cn.android_mobile.core.ui.listener.AutoLoadListener.AutoLoadCallBack;

public class ListViewComponent extends BaseComponent {
	public interface IListViewComponent {
		public void onRefersh();
		public void nextPage();
	}

	
	private IListViewComponent listener = null;
	private PullRefreshLayout pullrefersh;
	public ListView listview;
	private View footerView;
	private boolean isHasFooterView=true;
//	private BasicAdapter adapter;

	public ListViewComponent(BasicActivity activity, int resId) {
		super(activity, resId);
	}
	public ListViewComponent(BasicActivity activity,View v) {
		super(activity, v);
	}
	@Override
	public int onCreate() {
		return R.layout.component_listview;
	}

	@Override
	public void initComp() {
		pullrefersh = (PullRefreshLayout) findViewById(R.id.listview_pullrefersh);
		listview = (ListView) findViewById(R.id.listview_listview);
		footerView=activity.inflater.inflate(R.layout.loadding, null);
		listview.addFooterView(footerView);
	}

	@Override
	public void initListener() {
		pullrefersh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if(listener!=null){
					listener.onRefersh();
				}
			}
		});
		AutoLoadListener autoLoadListener = new AutoLoadListener(
				new AutoLoadCallBack() {
					@Override
					public void nextPage() {
						if(listener!=null){
							listener.nextPage();
						}
					}
				}, false);
		listview.setOnScrollListener(autoLoadListener);
	}

	public void doRefersh() {
		pullrefersh.doRefresh();
	}

	public void onComplete() {
		pullrefersh.onComplete( DateFormat.getDateTimeInstance().format(new Date()));
	}

	public void setAdapter(BasicAdapter adapter) {
		listview.setAdapter(adapter);
	}

	public void removeFooterView(){
		if(isHasFooterView){
			listview.removeFooterView(footerView);
		}
		isHasFooterView=false;
	}
	public void addFooterView(){
		if(isHasFooterView==false){
			listview.addFooterView(footerView);
		}
		isHasFooterView=true;
	}
	
	public IListViewComponent getListener() {
		return listener;
	}

	public void setListener(IListViewComponent listener) {
		this.listener = listener;
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}
}
