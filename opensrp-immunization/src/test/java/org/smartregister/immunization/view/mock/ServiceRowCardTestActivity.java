package org.smartregister.immunization.view.mock;

import android.app.Activity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import org.robolectric.Robolectric;
import org.smartregister.immunization.R;
import org.smartregister.immunization.view.ServiceRowCard;

/**
 * Created by real on 05/11/17.
 */

public class ServiceRowCardTestActivity extends Activity {

    private ServiceRowCard view;
    private AttributeSet attrs;

    @Override
    public void onCreate(Bundle bundle) {
        setTheme(R.style.AppTheme); //we need this here
        super.onCreate(bundle);
        LinearLayout linearLayout;
        linearLayout = new LinearLayout(this);
        setContentView(linearLayout);
        attrs = Robolectric.buildAttributeSet().build();
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

    public ServiceRowCard getInstance() {
        return (view == null) ? new ServiceRowCard(this) : view;
    }

    public ServiceRowCard getInstance2() {
        return new ServiceRowCard(this, attrs);
    }

    public ServiceRowCard getInstance3() {
        return new ServiceRowCard(this, attrs, 0);
    }

    public ServiceRowCard getInstance1() {
        return new ServiceRowCard(this, attrs, 0, 0);
    }

}
