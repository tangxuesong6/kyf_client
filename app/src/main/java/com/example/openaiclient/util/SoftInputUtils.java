package com.example.openaiclient.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftInputUtils {
    private int softInputHeight = 0; // The height of the soft input
    private boolean softInputHeightChanged = false; // Whether the soft input height has changed

    private boolean isNavigationBarShown = false; // Whether the navigation bar is shown
    private int navigationBarHeight = 0; // The height of the navigation bar

    private View targetView; // The target view to attach the soft input to
    private ISoftInputChangedListener listener; // The listener to notify when the soft input changes
    private boolean isSoftInputShown = false; // Whether the soft input is shown

    public interface ISoftInputChangedListener {
        void onChanged(boolean isSoftInputShown, int softInputHeight, int viewOffset);
    }

    public void attachSoftInput(final View targetView, final ISoftInputChangedListener listener) {
        if (targetView == null || listener == null) {
            return;
        }

        this.targetView = targetView;
        this.listener = listener;

        final View rootView = targetView.getRootView();
        if (rootView == null) {
            return;
        }

        navigationBarHeight = getNavigationBarHeight(targetView.getContext()); // Get the height of the navigation bar

        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int rootHeight = rootView.getHeight(); // Get the height of the root view
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect); // Get the visible display frame of the root view

                isNavigationBarShown = (rootHeight - rect.bottom == navigationBarHeight); // Check if the navigation bar is shown

                int mutableHeight = isNavigationBarShown ? navigationBarHeight : 0; // Calculate the mutable height
                int visibleHeight = rootHeight - mutableHeight - rect.bottom; // Calculate the visible height

                boolean isSoftInputShown = (visibleHeight > 0); // Check if the soft input is shown
                int softInputHeight = isSoftInputShown ? visibleHeight : 0; // Calculate the soft input height

                if (softInputHeight != SoftInputUtils.this.softInputHeight) { // Check if the soft input height has changed
                    softInputHeightChanged = true;
                    SoftInputUtils.this.softInputHeight = softInputHeight;
                } else {
                    softInputHeightChanged = false;
                }

                int[] location = new int[2];
                targetView.getLocationOnScreen(location); // Get the location of the target view on the screen

                int viewOffset = location[1] + targetView.getHeight() - rect.bottom; // Calculate the view offset

                if (isSoftInputShown != SoftInputUtils.this.isSoftInputShown || (isSoftInputShown && softInputHeightChanged)) { // Check if the soft input has changed
                    if (listener != null) {
                        listener.onChanged(isSoftInputShown, softInputHeight, viewOffset); // Notify the listener of the soft input change
                    }

                    SoftInputUtils.this.isSoftInputShown = isSoftInputShown;
                }
            }
        });
    }

    public static int getNavigationBarHeight(Context context) {
        if (context == null) {
            return 0;
        }

        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android"); // Get the resource ID of the navigation bar height
        return resources.getDimensionPixelSize(resourceId); // Get the dimension pixel size of the navigation bar height
    }

}