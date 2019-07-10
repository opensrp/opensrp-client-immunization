package org.smartregister.immunization.view.mock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import org.smartregister.immunization.R;
import org.smartregister.immunization.view.ImmunizationRowCard;

/**
 * Created by real on 05/11/17.
 */

public class ImmunizationRowCardTestActivity extends Activity {

    private ImmunizationRowCard view;

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

    public ImmunizationRowCard getInstance() {
        return (view == null) ? new ImmunizationRowCard(this) : view;
    }

    public ImmunizationRowCard getInstance1() {
        return new ImmunizationRowCard(this, true);
    }

    public ImmunizationRowCard getInstance2() {
        return new ImmunizationRowCard(this, ViewAttributes.attrs);
    }

    public ImmunizationRowCard getInstance3() {
        return new ImmunizationRowCard(this, ViewAttributes.attrs, 0);
    }

    public ImmunizationRowCard getInstance4() {
        return new ImmunizationRowCard(this, ViewAttributes.attrs, 0, 0);
    }

}
