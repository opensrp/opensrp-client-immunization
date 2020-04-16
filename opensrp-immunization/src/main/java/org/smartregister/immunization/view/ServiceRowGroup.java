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
import org.smartregister.immunization.R;
import org.smartregister.immunization.adapter.ServiceRowAdapter;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by keyman on 15/05/2017.
 */

public class ServiceRowGroup extends LinearLayout implements View.OnClickListener {
    public boolean editmode;
    private Context context;
    private TextView nameTV;
    private ExpandableHeightGridView servicesGV;
    private ServiceRowAdapter serviceRowAdapter;
    private List<ServiceType> serviceTypeList;
    private CommonPersonObjectClient childDetails;
    private List<ServiceRecord> serviceRecordList;
    private List<Alert> alertList;
    private GroupState groupState;
    private OnServiceClickedListener onServiceClickedListener;
    private OnServiceUndoClickListener onServiceUndoClickListener;
    private SimpleDateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
    private boolean modalOpen;

    public ServiceRowGroup(Context context, boolean editmode) {
        super(context);
        this.editmode = editmode;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_immunization_row_group, this, true).setFilterTouchesWhenObscured(true);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        nameTV = findViewById(R.id.name_tv);
        nameTV.setVisibility(GONE);
        servicesGV = findViewById(R.id.vaccines_gv);
        servicesGV.setExpanded(true);
        TextView recordAllTV = findViewById(R.id.record_all_tv);
        recordAllTV.setOnClickListener(this);
        recordAllTV.setVisibility(GONE);
    }

    public ServiceRowGroup(Context context) {
        super(context);
        init(context);
    }

    public ServiceRowGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ServiceRowGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @TargetApi (Build.VERSION_CODES.LOLLIPOP)
    public ServiceRowGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public List<ServiceType> getServiceTypes() {
        return serviceTypeList;
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

    public void setData(CommonPersonObjectClient childDetails, List<ServiceType> serviceTypeList,
                        List<ServiceRecord> serviceRecordList, List<Alert> alerts) {
        this.childDetails = childDetails;
        this.serviceTypeList = serviceTypeList;
        this.serviceRecordList = serviceRecordList;
        alertList = alerts;
        updateViews();
    }

    /**
     * This method will update all views, including service cards in this group
     */
    public void updateViews() {
        updateViews(null);
    }

    /**
     * This method will update service group views, and the service cards corresponding to the list of {@link
     * ServiceWrapper}s specified
     *
     * @param servicesToUpdate
     *         List of services who's views we want updated, or NULL if we want to update all service views
     */
    public void updateViews(ArrayList<ServiceWrapper> servicesToUpdate) {
        groupState = GroupState.IN_PAST;
        if (serviceTypeList != null) {
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
            updateServiceRowCards(servicesToUpdate);
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

    private void updateServiceRowCards(ArrayList<ServiceWrapper> servicesToUpdate) {
        if (serviceRowAdapter == null) {
            serviceRowAdapter = new ServiceRowAdapter(context, this, editmode, serviceTypeList, serviceRecordList,
                    alertList);
            servicesGV.setAdapter(serviceRowAdapter);

            servicesGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    if (!(v instanceof ServiceRowCard)) {
                        return;
                    }

                    v.setEnabled(false);

                    ServiceRowCard serviceRowCard = (ServiceRowCard) v;
                    State state = serviceRowCard.getState();
                    if (state != null) {
                        switch (state) {
                            case DUE:
                            case OVERDUE:
                                if (onServiceClickedListener != null) {
                                    onServiceClickedListener
                                            .onClick(ServiceRowGroup.this, serviceRowCard.getServiceWrapper());
                                }
                                break;
                            case DONE_CAN_NOT_BE_UNDONE:
                            case DONE_CAN_BE_UNDONE:
                                if (serviceRowCard.isEditmode() && !serviceRowCard.isStatusForMoreThanThreeMonths()) {
                                    onUndoClick(serviceRowCard);
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    v.setEnabled(true);

                }
            });
        }

        if (serviceRowAdapter != null) {
            serviceRowAdapter.update(servicesToUpdate);
        }

    }

    public void onUndoClick(ServiceRowCard serviceRowCard) {
        if (onServiceUndoClickListener != null) {
            onServiceUndoClickListener.onUndoClick(this, serviceRowCard.getServiceWrapper());
        }
    }

    public void setOnServiceUndoClickListener(OnServiceUndoClickListener onServiceUndoClickListener) {
        this.onServiceUndoClickListener = onServiceUndoClickListener;
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

    public void updateWrapperStatus(ServiceWrapper serviceWrapper) {
        if (serviceRowAdapter != null) {
            serviceRowAdapter.updateWrapperStatus(serviceWrapper, getChildDetails());
        }
    }

    public CommonPersonObjectClient getChildDetails() {
        return childDetails;
    }

    public void updateWrapper(ServiceWrapper wrapper) {
        if (serviceRowAdapter != null) {
            serviceRowAdapter.updateWrapper(wrapper);
        }
    }

    public ExpandableHeightGridView getServicesGV() {
        return servicesGV;
    }

    public ServiceRowAdapter getServiceRowAdapter() {
        return serviceRowAdapter;
    }

    public interface OnServiceClickedListener {
        void onClick(ServiceRowGroup serviceRowGroup, ServiceWrapper serviceWrapper);
    }

    public interface OnServiceUndoClickListener {
        void onUndoClick(ServiceRowGroup serviceRowGroup, ServiceWrapper serviceWrapper);

    }
}
