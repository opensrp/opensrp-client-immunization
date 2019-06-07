package org.smartregister.immunization.sample.tabfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.joda.time.DateTime;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.fragment.ServiceEditDialogFragment;
import org.smartregister.immunization.fragment.VaccinationEditDialogFragment;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.sample.DetailActivity;
import org.smartregister.immunization.sample.R;
import org.smartregister.immunization.util.VaccinateActionUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.immunization.view.ImmunizationRowGroup;
import org.smartregister.immunization.view.ServiceRowGroup;
import org.smartregister.repository.DetailsRepository;
import org.smartregister.service.AlertService;
import org.smartregister.util.Utils;
import org.smartregister.view.customcontrols.CustomFontTextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ImmunizationFragment extends Fragment {

    private LayoutInflater inflater;
    private ViewGroup container;
    private CommonPersonObjectClient childDetails;
    private ArrayList<ImmunizationRowGroup> vaccineGroups;
    private ArrayList<ServiceRowGroup> serviceRowGroups;
    private static final String DIALOG_TAG = "ChildImmunoActivity_DIALOG_TAG";
    private AlertService alertService;
    private LinearLayout fragmentContainer;

    public ImmunizationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        if (this.getArguments() != null) {
            Serializable serializable = getArguments().getSerializable(DetailActivity.EXTRA_CHILD_DETAILS);
            if (serializable != null && serializable instanceof CommonPersonObjectClient) {
                childDetails = (CommonPersonObjectClient) serializable;
            }
        }
        View underFiveFragment = inflater.inflate(R.layout.child_under_five_fragment, container, false);
        fragmentContainer = (LinearLayout) underFiveFragment.findViewById(R.id.container);

        alertService = ImmunizationLibrary.getInstance().context().alertService();

        DetailsRepository detailsRepository = ((DetailActivity) getActivity()).getDetailsRepository();
        childDetails = childDetails != null ? childDetails : ((DetailActivity) getActivity()).getChildDetails();

        loadView();
        return underFiveFragment;
    }

    public void loadView() {
        if (fragmentContainer != null) {
            updateVaccinationViews(fragmentContainer, true);
            updateServiceViews(fragmentContainer, true);
        }
    }


    private void updateVaccinationViews(LinearLayout fragmentContainer, boolean editmode) {
        vaccineGroups = new ArrayList<>();
        VaccineRepository vaccineRepository = ImmunizationLibrary.getInstance().vaccineRepository();
        List<Vaccine> vaccineList = vaccineRepository.findByEntityId(childDetails.entityId());
        LinearLayout vaccineGroupCanvasLL = (LinearLayout) fragmentContainer.findViewById(R.id.immunizations);
        vaccineGroupCanvasLL.removeAllViews();

        CustomFontTextView title = new CustomFontTextView(getActivity());
        title.setAllCaps(true);
        title.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
        title.setTextColor(getResources().getColor(R.color.text_black));
        title.setText(getString(R.string.immunizations));
        vaccineGroupCanvasLL.addView(title);

        List<Alert> alertList = new ArrayList<>();
        if (alertService != null) {
            alertList = alertService.findByEntityIdAndAlertNames(childDetails.entityId(),
                    VaccinateActionUtils.allAlertNames("child"));
        }

        List<VaccineGroup> supportedVaccines = VaccinatorUtils.getSupportedVaccines(getActivity());
        for (VaccineGroup vaccineGroupObject : supportedVaccines) {

            VaccinateActionUtils.addBcg2SpecialVaccine(getActivity(), vaccineGroupObject, vaccineList);

            ImmunizationRowGroup curGroup = new ImmunizationRowGroup(getActivity(), editmode);
            curGroup.setData(vaccineGroupObject, childDetails, vaccineList, alertList);
            curGroup.setOnVaccineUndoClickListener(new ImmunizationRowGroup.OnVaccineUndoClickListener() {
                @Override
                public void onUndoClick(ImmunizationRowGroup vaccineGroup, VaccineWrapper vaccine) {
                    addVaccinationDialogFragment(Arrays.asList(vaccine), vaccineGroup);

                }
            });

            vaccineGroupCanvasLL.addView(curGroup);
            vaccineGroups.add(curGroup);
        }
    }

    private void updateServiceViews(LinearLayout fragmentContainer, boolean editmode) {
        serviceRowGroups = new ArrayList<>();

        List<ServiceRecord> serviceRecords = new ArrayList<>();

        RecurringServiceTypeRepository recurringServiceTypeRepository = ImmunizationLibrary.getInstance().recurringServiceTypeRepository();
        RecurringServiceRecordRepository recurringServiceRecordRepository = ImmunizationLibrary.getInstance().recurringServiceRecordRepository();

        if (recurringServiceRecordRepository != null) {
            serviceRecords = recurringServiceRecordRepository.findByEntityId(childDetails.entityId());
        }

        Map<String, List<ServiceType>> serviceTypeMap = new LinkedHashMap<>();
        if (recurringServiceTypeRepository != null) {
            List<ServiceType> serviceTypes = recurringServiceTypeRepository.fetchAll();
            for (ServiceType serviceType : serviceTypes) {
                String type = serviceType.getType();
                List<ServiceType> serviceTypeList = serviceTypeMap.get(type);
                if (serviceTypeList == null) {
                    serviceTypeList = new ArrayList<>();
                }
                serviceTypeList.add(serviceType);
                serviceTypeMap.put(type, serviceTypeList);
            }
        }

        LinearLayout serviceGroupCanvasLL = (LinearLayout) fragmentContainer.findViewById(R.id.services);
        serviceGroupCanvasLL.removeAllViews();

        CustomFontTextView title = new CustomFontTextView(getActivity());
        title.setAllCaps(true);
        title.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
        title.setTextColor(getResources().getColor(R.color.text_black));
        title.setText(getString(R.string.recurring));
        serviceGroupCanvasLL.addView(title);

        List<Alert> alertList = new ArrayList<>();
        if (alertService != null) {
            alertList = alertService.findByEntityIdAndAlertNames(childDetails.entityId(),
                    VaccinateActionUtils.allAlertNames(serviceTypeMap.values()));
        }

        try {
            for (String type : serviceTypeMap.keySet()) {
                ServiceRowGroup curGroup = new ServiceRowGroup(getActivity(), editmode);
                curGroup.setData(childDetails, serviceTypeMap.get(type), serviceRecords, alertList);
                curGroup.setOnServiceUndoClickListener(new ServiceRowGroup.OnServiceUndoClickListener() {
                    @Override
                    public void onUndoClick(ServiceRowGroup serviceRowGroup, ServiceWrapper service) {
                        addServiceDialogFragment(service, serviceRowGroup);
                    }
                });

                serviceGroupCanvasLL.addView(curGroup);
                serviceRowGroups.add(curGroup);
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), Log.getStackTraceString(e));
        }

    }

    public void addVaccinationDialogFragment(List<VaccineWrapper> vaccineWrappers, ImmunizationRowGroup vaccineGroup) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        String dobString = Utils.getValue(childDetails.getColumnmaps(), "dob", false);
        Date dob = Calendar.getInstance().getTime();
        if (!TextUtils.isEmpty(dobString)) {
            DateTime dateTime = new DateTime(dobString);
            dob = dateTime.toDate();
        }

        List<Vaccine> vaccineList = ImmunizationLibrary.getInstance().vaccineRepository()
                .findByEntityId(childDetails.entityId());
        if (vaccineList == null) vaccineList = new ArrayList<>();

        VaccinationEditDialogFragment vaccinationDialogFragment = VaccinationEditDialogFragment.newInstance(getActivity(), dob, vaccineList, vaccineWrappers, vaccineGroup, true);
        vaccinationDialogFragment.show(ft, DIALOG_TAG);
    }

    public void addServiceDialogFragment(ServiceWrapper serviceWrapper, ServiceRowGroup serviceRowGroup) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        String dobString = Utils.getValue(childDetails.getColumnmaps(), "dob", false);
        DateTime dob = DateTime.now();
        if (!TextUtils.isEmpty(dobString)) {
            dob = new DateTime(dobString);
        }

        List<ServiceRecord> serviceRecordList = ImmunizationLibrary.getInstance().recurringServiceRecordRepository()
                .findByEntityId(childDetails.entityId());

        ServiceEditDialogFragment serviceEditDialogFragment = ServiceEditDialogFragment.newInstance(dob, serviceRecordList, serviceWrapper, serviceRowGroup, true);
        serviceEditDialogFragment.show(ft, DIALOG_TAG);
    }

    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

}
