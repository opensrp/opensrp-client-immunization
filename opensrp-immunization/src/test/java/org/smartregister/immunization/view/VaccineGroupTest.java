package org.smartregister.immunization.view;

import com.google.gson.reflect.TypeToken;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.adapter.VaccineCardAdapter;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.view.mock.ViewAttributes;
import org.smartregister.util.AppProperties;
import org.smartregister.util.JsonFormUtils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineGroupTest extends BaseUnitTest {

    private final String magicDate = "1985-07-24T00:00:00.000Z";
    private VaccineGroup view;
    private org.smartregister.immunization.domain.jsonmapping.VaccineGroup vaccineData;
    private CommonPersonObjectClient childDetails;
    private ArrayList<Vaccine> vaccinesList;
    private ArrayList<Alert> alertList;
    private VaccineWrapper wrapper;
    private ArrayList<VaccineWrapper> wrappers;

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private AppProperties properties;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
        view = new VaccineGroup(RuntimeEnvironment.application);

        Mockito.doReturn(properties).when(immunizationLibrary).getProperties();
        view.setImmunizationLibraryInstance(immunizationLibrary);
    }

    @Test
    public void assertGetAlertListNotNull() {
        view.setAlertList(new ArrayList<Alert>());
        Assert.assertNotNull(view.getAlertList());
    }

    @Test
    public void assertGetVaccineListNotNull() {
        view.setVaccineList(new ArrayList<Vaccine>());
        Assert.assertNotNull(view.getVaccineList());
    }

    @Test
    public void assertEqualsVaccineData() {
        setDataForTest(magicDate);
        Assert.assertEquals(view.getVaccineData(), vaccineData);
    }

    public void setDataForTest(String dateTimeString) {
        wrappers = new ArrayList<>();
        wrapper = new VaccineWrapper();
        wrapper.setName(VaccineRepo.Vaccine.bcg2.display());
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg2);
        wrappers.add(wrapper);
        wrapper = new VaccineWrapper();
        wrapper.setVaccine(VaccineRepo.Vaccine.opv1);
        wrapper.setName(VaccineRepo.Vaccine.opv1.display());
        wrappers.add(wrapper);
        wrapper = new VaccineWrapper();
        wrapper.setName(VaccineRepo.Vaccine.measles2.display());
        wrapper.setVaccine(VaccineRepo.Vaccine.measles2);
        wrappers.add(wrapper);

        Type listType = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.VaccineGroup>>() {
        }.getType();
        List<org.smartregister.immunization.domain.jsonmapping.VaccineGroup> vaccines = JsonFormUtils.gson
                .fromJson(VaccineData.vaccines, listType);

        vaccineData = vaccines.get(0);

        HashMap<String, String> detail = new HashMap<String, String>();
        detail.put("dob", dateTimeString);
        childDetails = new CommonPersonObjectClient("1", detail, "NME");
        childDetails.setColumnmaps(detail);
        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.measles2.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        Alert alert = new Alert("", "", "", AlertStatus.complete, "", "");
        vaccinesList = new ArrayList<>();
        vaccinesList.add(vaccine);
        vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.bcg2.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        vaccinesList.add(vaccine);
        vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.opv1.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        vaccinesList.add(vaccine);
        alertList = new ArrayList<>();
        alertList.add(alert);
        view.setData(vaccineData, childDetails, vaccinesList, alertList, "mother");
        view.setData(vaccineData, childDetails, vaccinesList, alertList, "child");
    }

    @Test
    public void assertEqualsChildDetails() {
        setDataForTest(magicDate);
        Assert.assertEquals(view.getChildDetails(), childDetails);
    }

    @Test
    public void assertEqualsVaccineList() {
        setDataForTest(magicDate);
        Assert.assertEquals(view.getVaccineList(), vaccinesList);
    }

    @Test
    public void assertEqualsAlertList() {
        setDataForTest(magicDate);
        Assert.assertEquals(view.getAlertList(), alertList);
    }

    @Test
    public void assertUpdateViewsWithDifferentTimeWillSetVaccineAdapter() {
        Assert.assertEquals(view.getDueVaccines().size(), 0);
        Assert.assertEquals(view.getAllVaccineWrappers().size(), 0);

        setDataForTest(magicDate);
        view.updateViews();
        view.updateViews(wrappers);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        setDataForTest(format.format(new Date()) + "T00:00:00.000Z");
        view.updateViews(wrappers);
        setDataForTest("2018-01-01T00:00:00.000Z");
        view.updateViews(wrappers);
        Assert.assertNotNull(view.getDueVaccines());
        Assert.assertNotNull(view.getAllVaccineWrappers());
    }

    @Test
    public void verifyOnClickCallsOnRecordAllClickListenerAndOnVaccineClickedListener() {

        setDataForTest(magicDate);
        view.updateViews();
        view.updateViews(wrappers);

        // Record All
        VaccineGroup.OnRecordAllClickListener onRecordAllClickListener = Mockito
                .mock(VaccineGroup.OnRecordAllClickListener.class);
        view.setOnRecordAllClickListener(onRecordAllClickListener);

        view.onClick(view.findViewById(R.id.record_all_tv));
        Mockito.verify(onRecordAllClickListener).onClick(org.mockito.ArgumentMatchers.any(VaccineGroup.class),
                org.mockito.ArgumentMatchers.any(ArrayList.class));

        // Vaccine Clicked
        VaccineGroup.OnVaccineClickedListener onVaccineClickListener = Mockito
                .mock(VaccineGroup.OnVaccineClickedListener.class);
        view.setOnVaccineClickedListener(onVaccineClickListener);

        ExpandableHeightGridView expandableHeightGridView = view.getVaccinesGV();
        VaccineCardAdapter adapter = view.getVaccineCardAdapter();

        VaccineCard vaccineCard = new VaccineCard(RuntimeEnvironment.application);
        wrapper = new VaccineWrapper();
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg);
        vaccineCard.setVaccineWrapper(wrapper);

        vaccineCard.setState(State.NOT_DUE);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.never()).onClick(org.mockito.ArgumentMatchers.any(VaccineGroup.class),
                org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        vaccineCard.setState(State.DONE_CAN_BE_UNDONE);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.never()).onClick(org.mockito.ArgumentMatchers.any(VaccineGroup.class),
                org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        vaccineCard.setState(State.DONE_CAN_NOT_BE_UNDONE);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.never()).onClick(org.mockito.ArgumentMatchers.any(VaccineGroup.class),
                org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        //If expiry retroactive data entry Not Allowed
        vaccineCard.setState(State.EXPIRED);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.times(0)).onClick(org.mockito.ArgumentMatchers.any(VaccineGroup.class),
                org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        //If expiry retroactive data entry Allowed
        Mockito.doReturn(true).when(immunizationLibrary).isAllowExpiredVaccineEntry();
        vaccineCard.setState(State.EXPIRED);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.times(1)).onClick(org.mockito.ArgumentMatchers.any(VaccineGroup.class),
                org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        vaccineCard.setState(State.DUE);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.times(2)).onClick(view, wrapper);

        vaccineCard.setState(State.OVERDUE);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.times(3)).onClick(view, wrapper);

        // UNDO
        VaccineGroup.OnVaccineUndoClickListener onVaccineUndoClickListener = Mockito
                .mock(VaccineGroup.OnVaccineUndoClickListener.class);
        view.setOnVaccineUndoClickListener(onVaccineUndoClickListener);

        vaccineCard.setState(State.DONE_CAN_NOT_BE_UNDONE);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(VaccineGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        vaccineCard.setState(State.DUE);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(VaccineGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        vaccineCard.setState(State.OVERDUE);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(VaccineGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        vaccineCard.setState(State.EXPIRED);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(VaccineGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        vaccineCard.setState(State.NOT_DUE);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(VaccineGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        vaccineCard.setState(State.DONE_CAN_BE_UNDONE);
        expandableHeightGridView.performItemClick(vaccineCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.times(1)).onUndoClick(view, wrapper);
    }

    @Test
    public void assertUpdateWrapperStatusCallsUpdateWrapperStatus() {
        setDataForTest(magicDate);
        view.updateWrapperStatus(wrappers, VaccineRepository.TYPE_Synced);
        wrapper = new VaccineWrapper();
        wrapper.setName(VaccineRepo.Vaccine.bcg2.display());
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg2);
        view.updateWrapper(wrapper);
        wrapper = new VaccineWrapper();
        wrapper.setName(VaccineRepo.Vaccine.bcg2.display() + "/:D");
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg2);
        view.updateWrapper(wrapper);
        Assert.assertNotNull(view.getAllVaccineWrappers());
    }

    @Test
    public void assertIsModalOpenReturnsBoolean() {
        view.setModalOpen(true);
        Assert.assertEquals(view.isModalOpen(), true);
        view.setModalOpen(false);
        Assert.assertEquals(view.isModalOpen(), false);
    }

    @Test
    public void assertConstructorsNotNull() {
        Assert.assertNotNull(new VaccineGroup(RuntimeEnvironment.application));
        Assert.assertNotNull(new VaccineGroup(RuntimeEnvironment.application, ViewAttributes.attrs));
        Assert.assertNotNull(new VaccineGroup(RuntimeEnvironment.application, ViewAttributes.attrs, 0));
        Assert.assertNotNull(new VaccineGroup(RuntimeEnvironment.application, ViewAttributes.attrs, 0, 0));
    }

}
