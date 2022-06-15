package org.smartregister.immunization.view;

import android.widget.Button;

import androidx.test.core.app.ApplicationProvider;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.domain.Event;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;

import java.util.Date;

/**
 * Created by onaio on 30/08/2017.
 */

@Config(shadows = {FontTextViewShadow.class})
public class ImmunizationRowCardTest extends BaseUnitTest {

    private final String magicDue = "due";
    private final String magicMR = "mr";
    private final String magicMeasles = "measles";
    private final String magicExpired = "expired";

    private ImmunizationRowCard view;

    private ImmunizationLibrary immunizationLibrary;

    @Before
    public void setUp() throws Exception {
        view = new ImmunizationRowCard(ApplicationProvider.getApplicationContext());
        EventClientRepository eventClientRepository = Mockito.mock(EventClientRepository.class);

        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.PROGRAMCLIENTID, VaccineTest.NAME, 0,
                new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0, new Date(), 1, 1);
        Event event = new Event();
        event.setEventId("1");
        event.setDateCreated(new DateTime());

        Mockito.mockStatic(ImmunizationLibrary.class);
        immunizationLibrary = Mockito.mock(ImmunizationLibrary.class);
        VaccineRepository vaccineRepository = Mockito.mock(VaccineRepository.class);
        ImmunizationLibrary.init(Mockito.mock(org.smartregister.Context.class), Mockito.mock(Repository.class),
                Mockito.mock(CommonFtsObject.class), 0, 0);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(immunizationLibrary.vaccineRepository()).thenReturn(vaccineRepository);
        Mockito.when(vaccineRepository.find(org.mockito.ArgumentMatchers.anyLong())).thenReturn(vaccine);
        Mockito.when(immunizationLibrary.eventClientRepository()).thenReturn(eventClientRepository);
        Mockito.when(eventClientRepository
                        .convert(org.mockito.ArgumentMatchers.any(JSONObject.class), org.mockito.ArgumentMatchers.any(Class.class)))
                .thenReturn(event);
    }

    @Test
    public void testConstructors() {

        Assert.assertNotNull(new ImmunizationRowCard(ApplicationProvider.getApplicationContext()));
        Assert.assertNotNull(new ImmunizationRowCard(ApplicationProvider.getApplicationContext(), true));
        Assert.assertNotNull(new ImmunizationRowCard(ApplicationProvider.getApplicationContext(), Robolectric.buildAttributeSet().build()));
        Assert.assertNotNull(new ImmunizationRowCard(ApplicationProvider.getApplicationContext(), Robolectric.buildAttributeSet().build(), 0));
        Assert.assertNotNull(new ImmunizationRowCard(ApplicationProvider.getApplicationContext(), Robolectric.buildAttributeSet().build(), 0, 0));

    }

    @Test
    public void assertgetStateCallsUpdateStateReturnsWrapperState() throws Exception {
        Alert alert = new Alert("", "", "", AlertStatus.normal, "", "");
        VaccineWrapper wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());
        wrapper.setDbKey(0l);
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.DUE);

        alert = new Alert("", "", "", AlertStatus.upcoming, "", "");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertNotNull(view.getState());

        alert = new Alert("", "", "", AlertStatus.urgent, "", "");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.OVERDUE);

        alert = new Alert("", "", "", AlertStatus.expired, "", "");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMeasles);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.EXPIRED);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicExpired);
        wrapper.setAlert(alert);
        wrapper.setName(magicMeasles);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.EXPIRED);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicExpired);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setUpdatedVaccineDate(new DateTime(), true);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.DONE_CAN_NOT_BE_UNDONE);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(false);
        wrapper.setStatus(magicExpired);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setUpdatedVaccineDate(new DateTime(), true);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.DONE_CAN_BE_UNDONE);
    }

    @Test
    public void testHideVaccineOverdueRowCardColor() {
        Mockito.when(immunizationLibrary.hideOverdueVaccineStatus()).thenReturn(true);

        Alert alert = new Alert("", "", "", AlertStatus.urgent, "", "");
        VaccineWrapper wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());

        ImmunizationRowCard rowCard = Mockito.spy(view);
        Button statusIV = Mockito.mock(Button.class);
        Whitebox.setInternalState(rowCard, "statusIV", statusIV);
        rowCard.setVaccineWrapper(wrapper);

        Mockito.verify(statusIV).setBackgroundResource(R.drawable.vaccine_card_background_white);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());
        wrapper.setDbKey(0L);
        rowCard.setVaccineWrapper(wrapper);

        Mockito.verify(statusIV, Mockito.times(2)).setBackgroundResource(R.drawable.vaccine_card_background_white);

    }

    @Test
    public void testShowVaccineOverdueRowCardColor() {
        Mockito.when(immunizationLibrary.hideOverdueVaccineStatus()).thenReturn(false);

        Alert alert = new Alert("", "", "", AlertStatus.urgent, "", "");
        VaccineWrapper wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());

        ImmunizationRowCard rowCard = Mockito.spy(view);
        Button statusIV = Mockito.mock(Button.class);
        Whitebox.setInternalState(rowCard, "statusIV", statusIV);
        rowCard.setVaccineWrapper(wrapper);

        Mockito.verify(statusIV).setBackgroundResource(R.drawable.vaccine_card_background_red);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());
        wrapper.setDbKey(0L);
        rowCard.setVaccineWrapper(wrapper);

        Mockito.verify(statusIV).setBackgroundResource(R.drawable.vaccine_card_background_blue);
    }

}
