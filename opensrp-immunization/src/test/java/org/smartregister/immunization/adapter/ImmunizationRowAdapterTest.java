package org.smartregister.immunization.adapter;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.immunization.view.ImmunizationRowGroup;
import org.smartregister.util.JsonFormUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by onaio on 30/08/2017.
 */
public class ImmunizationRowAdapterTest extends BaseUnitTest {

    private final String magicDate = "1985-07-24T00:00:00.000Z";
    private final int magicNumber = 231231;
    @Mock
    private Context context;
    private CommonPersonObjectClient childdetails;
    private ArrayList<Vaccine> vaccinelist;
    private ArrayList<Alert> alertlist;
    private VaccineWrapper wrapper;
    private ArrayList<VaccineWrapper> wrappers;
    private ImmunizationRowGroup view;
    @Mock
    private CommonPersonObjectClient commonPersonObjectClient;

    @Before
    public void setUp() {
        view = new ImmunizationRowGroup(ApplicationProvider.getApplicationContext(), false);
        setDataForTest(magicDate);
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
        wrapper.setDbKey(0l);
        wrapper.setName(VaccineRepo.Vaccine.opv0.display());
        wrapper.setVaccine(VaccineRepo.Vaccine.opv0);
        wrapper.setStatus(null);
        wrappers.add(wrapper);

        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();
        List<VaccineGroup> vaccines = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);

        VaccineGroup vaccineData = vaccines.get(0);
        HashMap<String, String> detail = new HashMap<String, String>();
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
        view.setData(vaccineData, childdetails, vaccinelist, alertlist);
    }

    @Test
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws JSONException {
        Assert.assertNotNull(new ImmunizationRowAdapter(context, view, true, vaccinelist, alertlist));
    }

    @Test
    public void assertGetCountTestReturnsCount() throws Exception {


        ImmunizationRowAdapter immunizationRowAdapter = new ImmunizationRowAdapter(context, view, true, vaccinelist,
                alertlist);
        Assert.assertNotNull(immunizationRowAdapter);
        int len = new JSONArray(VaccineData.vaccines).getJSONObject(0).getJSONArray("vaccines").length();
        Assert.assertEquals(len, immunizationRowAdapter.getCount());

        //should return null
        Assert.assertNull(immunizationRowAdapter.getItem(0));

        Assert.assertEquals(immunizationRowAdapter.getItemId(0), magicNumber);

    }

    @Test
    public void assertGetViewReturnsVaccineGroup() {
        ImmunizationRowAdapter immunizationRowAdapter = new ImmunizationRowAdapter(ApplicationProvider.getApplicationContext(), view,
                true, vaccinelist, alertlist);

        Assert.assertNotNull(immunizationRowAdapter.getView(0, null, null));

    }

    @Test
    public void assertVaccineRemovedWithNullStatus() {
        ImmunizationRowAdapter mockAdapter = Mockito.spy(new ImmunizationRowAdapter(context, view, true, vaccinelist, alertlist));

        ReflectionHelpers.callInstanceMethod(mockAdapter, "removeVaccine",
                ReflectionHelpers.ClassParameter.from(String.class, "OPV 0"));


        Assert.assertEquals(view.getVaccineData().vaccines.size(), 1);
    }

    @Test
    public void testUpdateVaccineDate() {
        ImmunizationRowAdapter mockAdapter = Mockito.spy(new ImmunizationRowAdapter(context, view, true, vaccinelist, alertlist));

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
