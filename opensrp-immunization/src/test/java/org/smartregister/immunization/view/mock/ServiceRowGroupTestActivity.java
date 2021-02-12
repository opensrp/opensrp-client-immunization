package org.smartregister.immunization.view.mock;

import android.app.Activity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import org.robolectric.Robolectric;
import org.smartregister.immunization.R;
import org.smartregister.immunization.view.ServiceRowGroup;

/**
 * Created by real on 05/11/17.
 */

public class ServiceRowGroupTestActivity extends Activity {

    private ServiceRowGroup view;
    private AttributeSet attributeSet;

    @Override
    public void onCreate(Bundle bundle) {
        setTheme(R.style.AppTheme); //we need this here
        super.onCreate(bundle);
        LinearLayout linearLayout;
        linearLayout = new LinearLayout(this);
        setContentView(linearLayout);
        attributeSet = Robolectric.buildAttributeSet().build();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public ServiceRowGroup getInstance() {
        return (view == null) ? new ServiceRowGroup(this) : view;
    }

    public ServiceRowGroup getInstance2() {
        return new ServiceRowGroup(this, attributeSet);
    }

    public ServiceRowGroup getInstance3() {
        return new ServiceRowGroup(this, attributeSet, 0);
    }

    public ServiceRowGroup getInstance1() {
        return new ServiceRowGroup(this, attributeSet, 0, 0);
    }

}
