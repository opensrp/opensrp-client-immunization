package org.smartregister.immunization.view;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.domain.db.Event;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.view.mock.ViewAttributes;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;

import java.util.Date;

/**
 * Created by onaio on 30/08/2017.
 */

@PrepareForTest ({ImmunizationLibrary.class})
@Config (shadows = {FontTextViewShadow.class})
@PowerMockIgnore ({"javax.xml.*", "org.xml.sax.*", "org.w3c.dom.*", "org.springframework.context.*", "org.apache.log4j.*"})
public class ImmunizationRowCardTest extends BaseUnitTest {

    private final String magicDue = "due";
    private final String magicMR = "mr";
    private final String magicMeasles = "measles";
    private final String magicExpired = "expired";
    @Rule
    public PowerMockRule rule = new PowerMockRule();
    private ImmunizationRowCard view;

    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);
        view = new ImmunizationRowCard(RuntimeEnvironment.application);
        EventClientRepository eventClientRepository = Mockito.mock(EventClientRepository.class);

        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.PROGRAMCLIENTID, VaccineTest.NAME, 0,
                new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0, new Date());
        Event event = new Event();
        event.setEventId("1");
        event.setDateCreated(new DateTime());

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        ImmunizationLibrary immunizationLibrary = Mockito.mock(ImmunizationLibrary.class);
        VaccineRepository vaccineRepository = Mockito.mock(VaccineRepository.class);
        ImmunizationLibrary.init(Mockito.mock(org.smartregister.Context.class), Mockito.mock(Repository.class),
                Mockito.mock(CommonFtsObject.class), 0, 0);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.vaccineRepository()).thenReturn(vaccineRepository);
        PowerMockito.when(vaccineRepository.find(org.mockito.ArgumentMatchers.anyLong())).thenReturn(vaccine);
        PowerMockito.when(immunizationLibrary.eventClientRepository()).thenReturn(eventClientRepository);
        PowerMockito.when(eventClientRepository
                .convert(org.mockito.ArgumentMatchers.any(JSONObject.class), org.mockito.ArgumentMatchers.any(Class.class)))
                .thenReturn(event);
    }

    @Test
    public void testConstructors() {
        Assert.assertNotNull(new ImmunizationRowCard(RuntimeEnvironment.application));
        Assert.assertNotNull(new ImmunizationRowCard(RuntimeEnvironment.application, true));
        Assert.assertNotNull(new ImmunizationRowCard(RuntimeEnvironment.application, ViewAttributes.attrs));
        Assert.assertNotNull(new ImmunizationRowCard(RuntimeEnvironment.application, ViewAttributes.attrs, 0));
        Assert.assertNotNull(new ImmunizationRowCard(RuntimeEnvironment.application, ViewAttributes.attrs, 0, 0));
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

}
