package org.smartregister.immunization.adapter;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowViewGroup;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.view.ImmunizationRowCard;

/**
 * Created by kaderchowdhury on 13/12/17.
 */
@Implements (ImmunizationRowCard.class)
public class ImmunizationRowCardShadow extends ShadowViewGroup {

    @Implementation
    public void setVaccineWrapper(VaccineWrapper vaccineWrapper) {

    }
}
