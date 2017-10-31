package org.smartregister.immunization.utils;

import android.app.Activity;
import android.widget.TableLayout;

import org.robolectric.RuntimeEnvironment;
import org.smartregister.immunization.util.VaccinatorUtils;

/**
 * Created by real on 31/10/17.
 */

public class ActivityUtils extends Activity {


    public void vaccinatorUtilsTest() {
        TableLayout tableLayout = new TableLayout(RuntimeEnvironment.application);
        VaccinatorUtils.addStatusTag(this,tableLayout,"",true);
    }

}