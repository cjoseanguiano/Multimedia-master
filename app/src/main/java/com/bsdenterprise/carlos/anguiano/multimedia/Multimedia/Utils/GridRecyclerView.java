package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleableRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.bsdenterprise.carlos.anguiano.multimedia.R;

/**
 * Created by Carlos Anguiano on 05/09/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public class GridRecyclerView extends RecyclerView {


    private GridLayoutManager manager;
    private int columnWidth = -1;
    private int columnNumber = 3;

    public GridRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public GridRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GridRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        @StyleableRes int index = 1;
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.columnWidth,
                    R.attr.columnNumber
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            columnWidth = array.getDimensionPixelSize(0, -1);
            columnNumber = array.getInteger(index,0);


            array.recycle();
        }

        manager = new GridLayoutManager(getContext(), 3);
        setLayoutManager(manager);
    }
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if(columnNumber==0) {
            if (columnWidth > 0) {
                int spanCount = Math.max(1, getMeasuredWidth() / columnWidth);
                manager.setSpanCount(spanCount);
            }
        }else{
            manager.setSpanCount(columnNumber);
        }
    }
}
