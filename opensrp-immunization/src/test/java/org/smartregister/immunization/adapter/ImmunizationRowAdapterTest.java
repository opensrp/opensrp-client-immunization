package org.smartregister.immunization.adapter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.view.ImmunizationRowGroup;
import org.smartregister.util.JsonFormUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */
@PrepareForTest ({ImmunizationLibrary.class})
@Config (shadows = {FontTextViewShadow.class, ImageUtilsShadow.class, ImmunizationRowCardShadow.class})
@PowerMockIgnore ({"javax.xml.*", "org.xml.sax.*", "org.w3c.dom.*", "org.springframework.context.*", "org.apache.log4j.*"})
public class ImmunizationRowAdapterTest extends BaseUnitTest {

    private final String magicDate = "1985-07-24T00:00:00.000Z";
    private final int magicNumber = 231231;
    @Mock
    private Context context;
    @Mock
    private org.smartregister.Context context_;
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
        org.mockito.MockitoAnnotations.initMocks(this);
        CoreLibrary.init(context_);
        view = new ImmunizationRowGroup(RuntimeEnvironment.application, false);
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
        vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.opv1.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        vaccinelist.add(vaccine);
        alertlist = new ArrayList<>();
        alertlist.add(alert);
        view.setData(vaccineData, childdetails, vaccinelist, alertlist);
    }

    @Test
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws JSONException {
        junit.framework.Assert.assertNotNull(new ImmunizationRowAdapter(context, view, true, vaccinelist, alertlist));
    }

    @Test
    public void assertGetCountTestReturnsCount() throws Exception {


        ImmunizationRowAdapter immunizationRowAdapter = new ImmunizationRowAdapter(context, view, true, vaccinelist,
                alertlist);
        junit.framework.Assert.assertNotNull(immunizationRowAdapter);
        int len = new JSONArray(VaccineData.vaccines).getJSONObject(0).getJSONArray("vaccines").length();
        junit.framework.Assert.assertEquals(len, immunizationRowAdapter.getCount());

        //should return null
        junit.framework.Assert.assertNull(immunizationRowAdapter.getItem(0));

        junit.framework.Assert.assertEquals(immunizationRowAdapter.getItemId(0), magicNumber);

    }

    @Test
    public void assertGetViewReturnsVaccineGroup() {
        ImmunizationRowAdapter immunizationRowAdapter = new ImmunizationRowAdapter(RuntimeEnvironment.application, view,
                true, vaccinelist, alertlist);

        junit.framework.Assert.assertEquals(immunizationRowAdapter.getView(0, null, null) != null, true);

    }

}
