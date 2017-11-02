package org.smartregister.immunization.fragment.mock;




import android.app.Activity;
import android.view.View;

import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.listener.ServiceActionListener;

/**
 * Created by real on 02/11/17.
 */

public class FragmentUtilActivityUsingServiceActionListener extends Activity implements ServiceActionListener {

    @Override
    public void onGiveToday(ServiceWrapper tag, View view) {

    }

    @Override
    public void onGiveEarlier(ServiceWrapper tag, View view) {

    }

    @Override
    public void onUndoService(ServiceWrapper tag, View view) {

    }
}
