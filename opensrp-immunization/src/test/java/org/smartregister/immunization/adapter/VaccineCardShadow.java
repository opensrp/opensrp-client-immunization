package org.smartregister.immunization.adapter;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowViewGroup;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.view.VaccineCard;

/**
 * Created by kaderchowdhury on 13/12/17.
 */
@Implements (VaccineCard.class)
public class VaccineCardShadow extends ShadowViewGroup {

    @Implementation
    public void setVaccineWrapper(VaccineWrapper vaccineWrapper) {

    }

    @Implementation
    public void updateChildsActiveStatus() {
        // Nothing to update this is not possible on the local JVM
    }

}
