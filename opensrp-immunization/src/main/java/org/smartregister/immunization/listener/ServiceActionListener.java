package org.smartregister.immunization.listener;

import android.view.View;

import org.smartregister.immunization.domain.ServiceWrapper;

/**
 * Created by keyman on 19/05/2017.
 */
public interface ServiceActionListener {

    void onGiveToday(ServiceWrapper tag, View view);

    void onGiveEarlier(ServiceWrapper tag, View view);

    void onUndoService(ServiceWrapper tag, View view);
}
