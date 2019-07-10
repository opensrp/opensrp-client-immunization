package org.smartregister.immunization.customshadows;

import android.content.Context;
import android.util.AttributeSet;

import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowTextView;
import org.smartregister.view.customcontrols.CustomFontTextView;

/**
 * Created by onadev on 15/06/2017.
 */
@Implements (CustomFontTextView.class)
public class FontTextViewShadow extends ShadowTextView {

    public void __constructor__(Context context, AttributeSet attrs, int defStyle) {

    }
}
