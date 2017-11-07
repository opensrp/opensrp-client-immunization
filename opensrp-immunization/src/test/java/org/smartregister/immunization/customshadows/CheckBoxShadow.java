package org.smartregister.immunization.customshadows;

import android.content.Context;
import android.util.AttributeSet;
import com.vijay.jsonwizard.customviews.CheckBox;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowCompoundButton;

/**
 * Created by onadev on 15/06/2017.
 */
@Implements(CheckBox.class)
public class CheckBoxShadow extends ShadowCompoundButton {


    public void __constructor__(Context context) {

    }

    public void __constructor__(Context context, AttributeSet attrs) {

    }

    public void __constructor__(Context context, AttributeSet attrs, int defStyleAttr) {

    }

    public void __constructor__(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    }

    @Implementation
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

    }

    @Implementation
    public void applyStyle(int resId) {

    }

    @Implementation
    private void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

    }

    @Implementation
    public void setCheckedImmediately(boolean checked) {

    }


}
