package cn.android_mobile.core.ui.tween;

import aurelienribon.tweenengine.TweenAccessor;

public class AAccessor implements TweenAccessor<AView> {

	public static final int POSITION_X = 1;
	public static final int POSITION_Y = 2;
	public static final int POSITION_XY = 3;
	public static final int ALPHA = 4;
	public static final int TEXTSIZE = 5;
	public static final int SHADOW = 6;
	public static final int COLOR = 7; //
	public static final int BACKGROUNDCOLOR = 8;//

	public static final int SCROLLX = 9;
	public static final int SCROLLY = 10;
	public static final int SCROLL_XY = 11;

	@Override
	public int getValues(AView target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case POSITION_X:
			returnValues[0] = target.getX();
			return 1;
		case POSITION_Y:
			returnValues[0] = target.getY();
			return 1;
		case POSITION_XY:
			returnValues[0] = target.getX();
			returnValues[1] = target.getY();
			return 2;
		case ALPHA:
			returnValues[0] = target.getAlpha();
			return 1;
		case TEXTSIZE:
			returnValues[0] = target.getTextSize();
			return 1;
		case SHADOW:
			returnValues[0] = target.getRadius();
			returnValues[1] = target.getDx();
			returnValues[2] = target.getDy();
			return 3;
		case COLOR:
			returnValues[0] = target.getColor();
			return 1;
		case BACKGROUNDCOLOR:
			returnValues[0] = target.getBackgroundcolor();
			return 1;
		case SCROLLX:
			returnValues[0] = target.getScrollX();
			return 1;
		case SCROLLY:
			returnValues[0] = target.getScrollY();
			return 1;
		case SCROLL_XY:
			returnValues[0] = target.getScrollX();
			returnValues[1] = target.getScrollY();
			return 2;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(AView target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case POSITION_X:
			target.setX(newValues[0]);
			break;
		case POSITION_Y:
			target.setY(newValues[0]);
			break;
		case POSITION_XY:
			target.setX(newValues[0]);
			target.setY(newValues[1]);
			break;
		case ALPHA:
			target.setAlpha((int) (newValues[0]));
			break;
		case TEXTSIZE:
			target.setTextSize((int) (newValues[0]));
			break;
		case SHADOW:
			target.setRadius(newValues[0]);
			target.setDx(newValues[1]);
			target.setDy(newValues[2]);
			// target.setColor(newValues[3]);
			target.setShadow();
			break;
		case COLOR:
			target.setColor((int) (newValues[0]));
			break;
		case BACKGROUNDCOLOR:
			target.setBackgroundcolor((int) (newValues[0]));
			break;
			
		case SCROLLX:
			target.setScrollX((int)(newValues[0]));
			break;
		case SCROLLY:
			target.setScrollY((int)(newValues[0]));
			break;
		case SCROLL_XY:
			target.setScrollX((int)(newValues[0]));
			target.setScrollY((int)(newValues[1]));
			break;
		default:
			assert false;
			break;
		}
	}
}