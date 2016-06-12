package utilsApp;

import android.app.Activity;
import android.os.Build.VERSION;
import android.view.View;

public abstract class SystemUiHider {
    public static final int FLAG_FULLSCREEN = 2;
    public static final int FLAG_HIDE_NAVIGATION = 6;
    public static final int FLAG_LAYOUT_IN_SCREEN_OLDER_DEVICES = 1;
    private static OnVisibilityChangeListener sDummyListener;
    protected Activity mActivity;
    protected View mAnchorView;
    protected int mFlags;
    protected OnVisibilityChangeListener mOnVisibilityChangeListener;

    public interface OnVisibilityChangeListener {
        void onVisibilityChange(boolean z);
    }

    /* renamed from: com.pibits.raspberrypiremotecam.util.SystemUiHider.1 */
    static class C01041 implements OnVisibilityChangeListener {
        C01041() {
        }

        public void onVisibilityChange(boolean visible) {
        }
    }

    public abstract void hide();

    public abstract boolean isVisible();

    public abstract void setup();

    public abstract void show();

    public static SystemUiHider getInstance(Activity activity, View anchorView, int flags) {
        if (VERSION.SDK_INT >= 11) {
            return new SystemUiHiderHoneycomb(activity, anchorView, flags);
        }
        return new SystemUiHiderBase(activity, anchorView, flags);
    }

    protected SystemUiHider(Activity activity, View anchorView, int flags) {
        this.mOnVisibilityChangeListener = sDummyListener;
        this.mActivity = activity;
        this.mAnchorView = anchorView;
        this.mFlags = flags;
    }

    public void toggle() {
        if (isVisible()) {
            hide();
        } else {
            show();
        }
    }

    public void setOnVisibilityChangeListener(OnVisibilityChangeListener listener) {
        if (listener == null) {
            listener = sDummyListener;
        }
        this.mOnVisibilityChangeListener = listener;
    }

    static {
        sDummyListener = new C01041();
    }
}
