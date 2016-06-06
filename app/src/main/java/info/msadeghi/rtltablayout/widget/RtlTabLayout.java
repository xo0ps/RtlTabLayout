package info.msadeghi.rtltablayout.widget;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

/**
 * Created by mahdi on 6/5/16 AD.
 */
public class RtlTabLayout extends TabLayout {

    private int mMode, mContentInsetStart, mTabPaddingStart, mRequestedTabMinWidth, INVALID_WIDTH, mScrollableTabMinWidth, mTabGravity;
    private LinearLayout mTabStrip;

    public RtlTabLayout(Context context) {
        super(context);
        init();
    }

    public RtlTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RtlTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        try {
            Field field = TabLayout.class.getDeclaredField("mMode");
            field.setAccessible(true);
            mMode = field.getInt(this);
            field = TabLayout.class.getDeclaredField("mContentInsetStart");
            field.setAccessible(true);
            mContentInsetStart = field.getInt(this);
            field = TabLayout.class.getDeclaredField("mTabPaddingStart");
            field.setAccessible(true);
            mTabPaddingStart = field.getInt(this);
            field = TabLayout.class.getDeclaredField("mRequestedTabMinWidth");
            field.setAccessible(true);
            mRequestedTabMinWidth = field.getInt(this);
            field = TabLayout.class.getDeclaredField("INVALID_WIDTH");
            field.setAccessible(true);
            INVALID_WIDTH = field.getInt(INVALID_WIDTH);
            field = TabLayout.class.getDeclaredField("mScrollableTabMinWidth");
            field.setAccessible(true);
            mScrollableTabMinWidth = field.getInt(this);
            field = TabLayout.class.getDeclaredField("mTabGravity");
            field.setAccessible(true);
            mTabGravity = field.getInt(this);
            field = TabLayout.class.getDeclaredField("mTabStrip");
            field.setAccessible(true);
            mTabStrip = (LinearLayout) field.get(this);

            applyModeAndGravity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyModeAndGravity() {
        int paddingStart = 0;
        if (mMode == MODE_SCROLLABLE) {
            // If we're scrollable, or fixed at start, inset using padding
            paddingStart = Math.max(0, mContentInsetStart - mTabPaddingStart);
        }
        ViewCompat.setPaddingRelative(mTabStrip, paddingStart, 0, 0, 0);

        switch (mMode) {
            case MODE_FIXED:
                mTabStrip.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            case MODE_SCROLLABLE:
                mTabStrip.setGravity(GravityCompat.END);
                break;
        }

        updateTabViews(true);
    }

    private void updateTabViews(final boolean requestLayout) {
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View child = mTabStrip.getChildAt(i);
            child.setMinimumWidth(getTabMinWidth());
            updateTabViewLayoutParams((LinearLayout.LayoutParams) child.getLayoutParams());
            if (requestLayout) {
                child.requestLayout();
            }
        }
    }

    private int getTabMinWidth() {
        if (mRequestedTabMinWidth != INVALID_WIDTH) {
            // If we have been given a min width, use it
            return mRequestedTabMinWidth;
        }
        // Else, we'll use the default value
        return mMode == MODE_SCROLLABLE ? mScrollableTabMinWidth : 0;
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams lp) {
        if (mMode == MODE_FIXED && mTabGravity == GRAVITY_FILL) {
            lp.width = 0;
            lp.weight = 1;
        } else {
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            lp.weight = 0;
        }
    }
}
