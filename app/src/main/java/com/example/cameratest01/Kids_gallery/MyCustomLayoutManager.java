package com.example.cameratest01.Kids_gallery;

import android.content.Context;
import android.os.Parcelable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyCustomLayoutManager extends LinearLayoutManager {

    private int mPendingTargetPos = -1;
    private int mPendingPosOffset = -1;

    public MyCustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (mPendingTargetPos !=-1 && state.getItemCount()>0){
            scrollToPositionWithOffset(mPendingTargetPos, mPendingPosOffset);
            mPendingTargetPos=-1;
            mPendingPosOffset=-1;
        }
        super.onLayoutChildren(recycler, state);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        mPendingTargetPos=-1;
        mPendingPosOffset=-1;
    }

    public void setTargetStartPos(int position, int offset){
        mPendingTargetPos=position;
        mPendingPosOffset=offset;
    }
}
