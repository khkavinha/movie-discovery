package com.example.kh870h.moviediscovery.MovieTrailersDisplay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by kavin on 1/27/2018.
 */

public class NonScrollListView extends ListView {

    public NonScrollListView(Context context) {
        super(context);
    }

    public NonScrollListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NonScrollListView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = View.MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
