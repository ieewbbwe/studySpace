package cn.android_mobile.core.ui.tween;

import android.app.Activity;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class AEngine {
	public static TweenManager tweenManager = new TweenManager();
	private static boolean isAnimationRunning = true;
	private static Activity activity;

	public static void quite() {
		isAnimationRunning = false;
		activity = null;
	}

	public static void initEngine(Activity act) {
		activity = act;
		isAnimationRunning = true;
		Tween.registerAccessor(AView.class, new AAccessor());
		setAnimationThread();
	}

	
	private static void setAnimationThread() {
		new Thread(new Runnable() {
			private long lastMillis = -1;
			@Override
			public void run() {
				while (isAnimationRunning) {
					if (lastMillis > 0) {
						long currentMillis = System.currentTimeMillis();
						final float delta = (currentMillis - lastMillis) / 1000f;
						/*
						 * view.post(new Runnable(){
						 * 
						 * @Override public void run() {
						 * 
						 * } });
						 */
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								tweenManager.update(delta);
							}
						});

						lastMillis = currentMillis;
					} else {
						lastMillis = System.currentTimeMillis();
					}

					try {
						Thread.sleep(1000 / 60);
					} catch (InterruptedException ex) {
					}
				}
			}
		}).start();

	}
}
