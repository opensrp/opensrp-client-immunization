package org.smartregister.immunization.view.mock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import org.smartregister.immunization.R;
import org.smartregister.immunization.view.ImmunizationRowGroup;

/**
 * Created by real on 05/11/17.
 */

public class ImmunizationRowGroupTestActivity extends Activity {

    private ImmunizationRowGroup view;

    @Override
    public void onCreate(Bundle bundle) {
        setTheme(R.style.AppTheme); //we need this here
        super.onCreate(bundle);
        LinearLayout linearLayout;
        linearLayout = new LinearLayout(this);
        setContentView(linearLayout);
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

    public ImmunizationRowGroup getInstance() {
        return (view == null) ? new ImmunizationRowGroup(this, true) : view;
    }

    public ImmunizationRowGroup getInstance2() {
        return new ImmunizationRowGroup(this, ViewAttributes.attrs);
    }

    public ImmunizationRowGroup getInstance3() {
        return new ImmunizationRowGroup(this, ViewAttributes.attrs, 0);
    }

    public ImmunizationRowGroup getInstance1() {
        return new ImmunizationRowGroup(this, ViewAttributes.attrs, 0, 0);
    }

}
