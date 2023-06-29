package org.smartregister.immunization.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.adapter.ServiceCardAdapter;
import org.smartregister.immunization.domain.GroupState;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.State;
import org.smartregister.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by keyman on 15/05/2017.
 */

public class ServiceGroup extends LinearLayout implements View.OnClickListener {
    private Context context;
    private TextView nameTV;
    private ExpandableHeightGridView servicesGV;
    private ServiceCardAdapter serviceCardAdapter;
    private Map<String, List<ServiceType>> serviceTypeMap;
    private CommonPersonObjectClient childDetails;
    private List<ServiceRecord> serviceRecordList;
    private List<Alert> alertList;
    private GroupState groupState;
    private OnServiceClickedListener onServiceClickedListener;
    private OnServiceUndoClickListener onServiceUndoClickListener;
    private SimpleDateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
    private boolean modalOpen;
    private ImmunizationLibrary immunizationLibraryInstance;

    private boolean isChildActive = true;

    public ServiceGroup(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_service_group, this, true).setFilterTouchesWhenObscured(true);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        nameTV = findViewById(R.id.name_tv);
        servicesGV = findViewById(R.id.services_gv);
        servicesGV.setExpanded(true);
        TextView recordAllTV = findViewById(R.id.record_all_tv);
        recordAllTV.setOnClickListener(this);
    }

    public ServiceGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ServiceGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ServiceGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public CommonPersonObjectClient getChildDetails() {
        return childDetails;
    }

    public List<String> getServiceTypeKeys() {
        List<String> keys = new ArrayList<>();
        if (serviceTypeMap == null || serviceTypeMap.isEmpty()) {
            return keys;
        }

        for (String key : serviceTypeMap.keySet()) {
            keys.add(key);
        }

        return keys;
    }

    public List<ServiceRecord> getServiceRecordList() {
        return serviceRecordList;
    }

    public void setServiceRecordList(List<ServiceRecord> serviceRecordList) {
        this.serviceRecordList = serviceRecordList;
    }

    public List<Alert> getAlertList() {
        return alertList;
    }

    public void setAlertList(List<Alert> alertList) {
        this.alertList = alertList;
    }

    public void setData(CommonPersonObjectClient childDetails, Map<String, List<ServiceType>> serviceTypeMap,
                        List<ServiceRecord> serviceRecordList, List<Alert> alerts) {
        this.childDetails = childDetails;
        this.serviceTypeMap = serviceTypeMap;
        this.serviceRecordList = serviceRecordList;
        alertList = alerts;
        updateViews();
    }

    /**
     * This method will update all views, including service cards in this group
     */
    public void updateViews() {
        groupState = GroupState.IN_PAST;
        if (serviceTypeMap != null) {
            String dobString = Utils.getValue(childDetails.getColumnmaps(), "dob", false);
            DateTime dateTime = new DateTime(dobString);
            Date dob = dateTime.toDate();
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            long timeDiff = today.getTimeInMillis() - dob.getTime();

            if (timeDiff < today.getTimeInMillis()) {
                groupState = GroupState.IN_PAST;
            } else if (timeDiff > (today.getTimeInMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))) {
                groupState = GroupState.IN_FUTURE;
            } else {
                groupState = GroupState.CURRENT;
            }
            updateStatusViews();
            updateServiceCards();
        }
    }

    private void updateStatusViews() {

        String recurringServices = getResources().getString(R.string.recurring_services);
        switch (groupState) {
            case IN_PAST:
                nameTV.setText(recurringServices);
                break;
            case CURRENT:
                nameTV.setText(String.format(context.getString(R.string.due_),
                        recurringServices, context.getString(R.string.today)));
                break;
            case IN_FUTURE:
                String dobString = Utils.getValue(childDetails.getColumnmaps(), "dob", false);
                Calendar dobCalender = Calendar.getInstance();
                DateTime dateTime = new DateTime(dobString);
                dobCalender.setTime(dateTime.toDate());

                //dobCalender.add(Calendar.DATE, serviceData.getInt("days_after_birth_due"));

                nameTV.setText(String.format(context.getString(R.string.due_),
                        recurringServices,
                        READABLE_DATE_FORMAT.format(dobCalender.getTime())));
                break;
            default:
                break;
        }

    }

    private void updateServiceCards() {
        if (serviceCardAdapter == null) {
            serviceCardAdapter = new ServiceCardAdapter(context, this, serviceRecordList, alertList, serviceTypeMap);
            serviceCardAdapter.setChildActive(isChildActive);
            servicesGV.setAdapter(serviceCardAdapter);

            servicesGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    if (!(v instanceof ServiceCard)) {
                        return;
                    }

                    v.setEnabled(false);

                    ServiceCard serviceCard = (ServiceCard) v;
                    State state = serviceCard.getState();
                    if (state != null) {
                        switch (state) {
                            case DUE:
                            case OVERDUE:
                                if (onServiceClickedListener != null) {
                                    onServiceClickedListener.onClick(ServiceGroup.this, serviceCard.getServiceWrapper());
                                }
                                break;
                            case EXPIRED:
                                if (onServiceClickedListener != null && getImmunizationLibraryInstance().isAllowExpiredVaccineEntry()) {
                                    onServiceClickedListener.onClick(ServiceGroup.this, serviceCard.getServiceWrapper());
                                }
                                break;
                            case DONE_CAN_BE_UNDONE:
                                onUndoClick(serviceCard);
                                break;
                            default:
                                break;
                        }
                    }

                    v.setEnabled(true);
                }
            });
        } else {
            serviceCardAdapter.updateAll();
        }

    }

    public void onUndoClick(ServiceCard serviceCard) {
        if (onServiceUndoClickListener != null) {
            onServiceUndoClickListener.onUndoClick(this, serviceCard.getServiceWrapper());
        }
    }

    public void setOnServiceUndoClickListener(OnServiceUndoClickListener onServiceUndoClickListener) {
        this.onServiceUndoClickListener = onServiceUndoClickListener;
    }

    public void setChildActive(boolean childActive) {
        isChildActive = childActive;
    }

    public void updateChildsActiveStatus() {

        if (serviceCardAdapter != null) {
            serviceCardAdapter.setChildActive(isChildActive);
            serviceCardAdapter.updateChildsActiveStatus();
        }

        if (servicesGV != null) {
            servicesGV.invalidateViews();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO implement in case of Record ALL
    }

    public void setOnServiceClickedListener(OnServiceClickedListener onServiceClickedListener) {
        this.onServiceClickedListener = onServiceClickedListener;
    }

    public boolean isModalOpen() {
        return modalOpen;
    }

    public void setModalOpen(boolean modalOpen) {
        this.modalOpen = modalOpen;
    }

    public void updateAllWrapperStatus() {
        if (serviceCardAdapter != null) {
            serviceCardAdapter.updateAllWrapperStatus(childDetails);
        }
    }

    public void updateWrapper(ServiceWrapper wrapper) {
        if (serviceCardAdapter != null) {
            serviceCardAdapter.updateWrapper(wrapper);
        }
    }

    public void updateWrapperStatus(ArrayList<ServiceWrapper> wrappers) {
        if (serviceCardAdapter != null) {
            serviceCardAdapter.updateWrapperStatus(wrappers, childDetails);
        }
    }

    public ExpandableHeightGridView getServicesGV() {
        return servicesGV;
    }

    public ServiceCardAdapter getServiceCardAdapter() {
        return serviceCardAdapter;
    }

    public interface OnServiceClickedListener {
        void onClick(ServiceGroup serviceGroup, ServiceWrapper serviceWrapper);
    }

    public ImmunizationLibrary getImmunizationLibraryInstance() {
        if (immunizationLibraryInstance == null) {
            immunizationLibraryInstance = ImmunizationLibrary.getInstance();
        }
        return immunizationLibraryInstance;
    }

    public void setImmunizationLibraryInstance(ImmunizationLibrary immunizationLibrary) {
        this.immunizationLibraryInstance = immunizationLibrary;
    }

    public interface OnServiceUndoClickListener {
        void onUndoClick(ServiceGroup serviceGroup, ServiceWrapper serviceWrapper);

    }
}
