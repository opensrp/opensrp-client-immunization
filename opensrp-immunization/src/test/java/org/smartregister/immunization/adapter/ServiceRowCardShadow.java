package org.smartregister.immunization.adapter;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowViewGroup;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.view.ServiceRowCard;

/**
 * Created by kaderchowdhury on 13/12/17.
 */

@Implements (ServiceRowCard.class)
public class ServiceRowCardShadow extends ShadowViewGroup {

    @Implementation
    public void setServiceWrapper(ServiceWrapper serviceWrapper) {

    }
}
