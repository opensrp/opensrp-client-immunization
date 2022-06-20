package org.smartregister.immunization.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.util.ReflectionHelpers;
import org.robolectric.annotation.Config;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.immunization.view.VaccineCard;
import org.smartregister.immunization.view.VaccineGroup;
import org.smartregister.util.JsonFormUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

import androidx.test.core.app.ApplicationProvider;

/**
 * Created by onaio on 30/08/2017.
 */
@Config (shadows = {FontTextViewShadow.class, ImageUtilsShadow.class, VaccineCardShadow.class})
public class VaccineCardAdapterTest extends BaseUnitTest {

    private final int magicNumber = 231231;
    private final String magicDate = "1985-07-24T00:00:00.000Z";
    @Mock
    protected View convertView;
    @Mock
    protected ViewGroup parentView;
    @Mock
    private Context context;
    private VaccineCardAdapter vaccineCardAdapter;
    private VaccineGroup view;
    @Mock
    private CommonPersonObjectClient commonPersonObjectClient;
    private CommonPersonObjectClient childdetails;
    private ArrayList<Vaccine> vaccinelist;
    private ArrayList<Alert> alertlist;
    private VaccineWrapper wrapper;
    private ArrayList<VaccineWrapper> wrappers;
    private List<Vaccine> vaccineList = new ArrayList<>();
    private List<Alert> alertList = new ArrayList<>();

    @Before
    public void setUp() {
        view = new VaccineGroup(ApplicationProvider.getApplicationContext());
        setDataForTest(magicDate);
        vaccineCardAdapter = new VaccineCardAdapter(ApplicationProvider.getApplicationContext(), view, "child", vaccineList, alertList);
    }

    public void setDataForTest(String dateTimeString) {
        wrappers = new ArrayList<>();
        wrapper = new VaccineWrapper();
        wrapper.setDbKey(0l);
        wrapper.setName(VaccineRepo.Vaccine.bcg2.display());
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg2);
        wrappers.add(wrapper);
        wrapper = new VaccineWrapper();
        wrapper.setDbKey(0l);
        wrapper.setVaccine(VaccineRepo.Vaccine.opv1);
        wrapper.setName(VaccineRepo.Vaccine.opv1.display());
        wrappers.add(wrapper);
        wrapper = new VaccineWrapper();
        wrapper.setDbKey(0l);
        wrapper.setName(VaccineRepo.Vaccine.measles2.display());
        wrapper.setVaccine(VaccineRepo.Vaccine.measles2);
        wrappers.add(wrapper);

        Type listType = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.VaccineGroup>>() {
        }.getType();
        List<org.smartregister.immunization.domain.jsonmapping.VaccineGroup> vaccines = JsonFormUtils.gson
                .fromJson(VaccineData.vaccines, listType);
        org.smartregister.immunization.domain.jsonmapping.VaccineGroup vaccineData = vaccines.get(0);

        HashMap<String, String> detail = new HashMap<>();
        detail.put("dob", dateTimeString);
        detail.put("gender", "male");
        detail.put("zeir_id", "1");
        detail.put("first_name", "");
        detail.put("last_name", "");
        childdetails = new CommonPersonObjectClient("1", detail, "NME");
        childdetails.setColumnmaps(detail);
        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.measles2.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        Alert alert = new Alert("", "", "", AlertStatus.complete, "", "");
        vaccinelist = new ArrayList<>();
        vaccinelist.add(vaccine);
        vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.bcg2.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        vaccinelist.add(vaccine);
        vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.opv1.display().toLowerCase(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        vaccinelist.add(vaccine);
        alertlist = new ArrayList<>();
        alertlist.add(alert);
        view.setData(vaccineData, childdetails, vaccinelist, alertlist, "child");
    }

    @Test
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() {
        Assert.assertNotNull(new VaccineCardAdapter(context, view, "", vaccineList, alertList));
    }

    @Test
    public void assertGetCountReturnsTheCorrectNumberOfItems() {

        Assert.assertEquals(2, vaccineCardAdapter.getCount());

        //should return null
        Assert.assertNull(vaccineCardAdapter.getItem(0));

        Assert.assertEquals(vaccineCardAdapter.getItemId(0), magicNumber);

    }

    @Test
    public void assertGetViewReturnsVaccineCard() {
        Assert.assertEquals(vaccineCardAdapter.getView(0, null, null) != null, true);
    }

    @Test
    public void updateChildsActiveStatusShouldCheckVaccineCardsForNull() {
        // An exception will be thrown and the test will fail if there is no null check
        HashMap<String, VaccineCard> vaccineCards = Whitebox.getInternalState(vaccineCardAdapter, "vaccineCards");
        HashMap<String, VaccineCard> nullVaccineCards = null;

        ReflectionHelpers.setField(vaccineCardAdapter, "vaccineCards", nullVaccineCards);

        try {
            vaccineCardAdapter.updateChildsActiveStatus();
            ReflectionHelpers.setField(vaccineCardAdapter, "vaccineCards", vaccineCards);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetDueVaccines() {
        ArrayList<VaccineWrapper> dueVaccines = vaccineCardAdapter.getDueVaccines();
        Assert.assertEquals(0, dueVaccines.size());
    }

    @Test
    public void testGetAllVaccineWrappers() {
        ArrayList<VaccineWrapper> vaccineWrappers = vaccineCardAdapter.getAllVaccineWrappers();
        Assert.assertEquals(0, vaccineWrappers.size());
    }

    @Test
    public void testGetVaccineList() {
        List<Vaccine> vaccineList = vaccineCardAdapter.getVaccineList();
        Assert.assertEquals(0, vaccineList.size());
    }

    @Test
    public void testGetAlertList() {
        List<Alert> alerts = vaccineCardAdapter.getAlertList();
        Assert.assertEquals(0, alerts.size());
    }

    @Test
    public void testUpdateVaccineDate() {
        VaccineCardAdapter mockAdapter = Mockito.spy(new VaccineCardAdapter(context, view, "child", vaccinelist, alertlist));

        VaccineWrapper vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setDbKey(0l);
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.opv2);
        vaccineWrapper.setName(VaccineRepo.Vaccine.opv2.display());

        VaccineRepo.Vaccine vaccine = VaccineRepo.Vaccine.opv2;
        vaccine.setPrerequisite(VaccineRepo.Vaccine.opv1);

        Map<String, Object> map = new HashMap<>();
        map.put("status", "due");
        map.put("vaccine", vaccine);

        mockAdapter.updateVaccineDate(map, vaccine, vaccineWrapper, VaccinatorUtils.receivedVaccines(vaccinelist));

        Assert.assertEquals(vaccineWrapper.getVaccineDate().dayOfMonth(), new DateTime().plusDays(28).dayOfMonth());
        Assert.assertEquals(vaccineWrapper.getVaccineDate().monthOfYear(), new DateTime().plusDays(28).monthOfYear());
        Assert.assertEquals(vaccineWrapper.getVaccineDate().year(), new DateTime().plusDays(28).year());
    }

}
