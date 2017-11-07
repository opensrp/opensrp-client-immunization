package org.smartregister.immunization.customshadows;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowAbsListView;
import org.robolectric.shadows.ShadowAdapterView;
import org.robolectric.shadows.ShadowView;
import org.smartregister.immunization.view.ExpandableHeightGridView;

/**
 * Created by real on 07/11/17.
 */
@Implements(ExpandableHeightGridView.class)
public class ExpandableHeightGridViewShadow extends ShadowGridView{

    public void __constructor__(Context context) {    }

    public void __constructor__(Context context, AttributeSet attrs) {

    }

    public void __constructor__(Context context, AttributeSet attrs,
                                    int defStyle) {

    }

    public boolean isExpanded() {

        return false;
    }


    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    }

    public void setExpanded(boolean expanded) {

    }

}
