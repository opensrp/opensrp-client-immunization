package org.smartregister.immunization.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.R;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.util.DisplayUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by keyman on 21/02/2017.
 */

public class ServiceCard extends LinearLayout {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
    private static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("dd/MM");
    private Context context;
    private ImageView statusIV;
    private TextView nameTV;
    private Button undoB;
    private State state;
    private ServiceWrapper serviceWrapper;
    private boolean isChildActive = true;

    public ServiceCard(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_vaccination_card, this, true).setFilterTouchesWhenObscured(true);
        statusIV = findViewById(R.id.status_iv);
        nameTV = findViewById(R.id.name_tv);
        undoB = findViewById(R.id.undo_b);
    }

    public ServiceCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ServiceCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ServiceCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public ServiceWrapper getServiceWrapper() {
        return serviceWrapper;
    }

    public void setServiceWrapper(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
        updateState();
    }

    public void updateState() {
        state = State.NOT_DUE;
        if (serviceWrapper != null) {
            Date dateDone = getDateDone();
            boolean isSynced = isSynced();
            String status = getStatus();

            if (dateDone != null) {// Vaccination was done
                if (isSynced) {
                    state = State.DONE_CAN_NOT_BE_UNDONE;
                } else {
                    state = State.DONE_CAN_BE_UNDONE;
                }
            } else {// Vaccination has not been done
                if (status != null) {
                    if (status.equalsIgnoreCase("due")) {
                        Alert alert = getAlert();
                        if (alert == null) {
                            //state = State.NO_ALERT;
                        } else if (alert.status().value().equalsIgnoreCase("normal")) {
                            state = State.DUE;
                        } else if (alert.status().value().equalsIgnoreCase("upcoming")) {
                            //state = State.UPCOMING;
                        } else if (alert.status().value().equalsIgnoreCase("urgent")) {
                            state = State.OVERDUE;
                        } else if (alert.status().value().equalsIgnoreCase("expired")) {
                            state = State.EXPIRED;
                        }
                    } else if (serviceWrapper.getStatus().equalsIgnoreCase("expired")) {
                        state = State.EXPIRED;
                    }
                }

                /*
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);
                if (dateDue.getTime() > (today.getTimeInMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))) {
                    // Vaccination due more than one day from today
                    this.state = State.NOT_DUE;
                } else if (dateDue.getTime() < (today.getTimeInMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))) {
                    // Vaccination overdue
                    this.state = State.OVERDUE;
                } else {
                    this.state = State.DUE;
                } */
            }
            updateStateUi();
            updateChildsActiveStatus();
        }
    }

    private Date getDateDone() {
        if (serviceWrapper != null) {
            DateTime dateDone = serviceWrapper.getUpdatedVaccineDate();
            if (dateDone != null) return dateDone.toDate();
        }

        return null;
    }

    private boolean isSynced() {
        if (serviceWrapper != null) {
            return serviceWrapper.isSynced();
        }
        return false;
    }

    private String getStatus() {
        if (serviceWrapper != null) {
            return serviceWrapper.getStatus();
        }
        return null;
    }

    private Alert getAlert() {
        if (serviceWrapper != null) {
            return serviceWrapper.getAlert();
        }
        return null;
    }

    private void updateStateUi() {
        setVisibility(VISIBLE);
        switch (state) {
            case NOT_DUE:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setVisibility(GONE);
                undoB.setVisibility(GONE);
                nameTV.setVisibility(VISIBLE);
                nameTV.setTextColor(context.getResources().getColor(R.color.silver));
                nameTV.setText(getServiceName());
                setVisibility(VISIBLE);
                break;
            case DUE:
                setBackgroundResource(R.drawable.vaccine_card_background_blue);
                statusIV.setVisibility(GONE);
                undoB.setVisibility(GONE);
                nameTV.setVisibility(VISIBLE);
                nameTV.setTextColor(context.getResources().getColor(android.R.color.white));
                nameTV.setText(String.format(context.getString(R.string.record_), getServiceName()));
                break;
            case DONE_CAN_BE_UNDONE:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setVisibility(VISIBLE);
                undoB.setVisibility(VISIBLE);
                nameTV.setVisibility(VISIBLE);
                nameTV.setTextColor(context.getResources().getColor(R.color.silver));

                SimpleDateFormat dateFormatToUse = SHORT_DATE_FORMAT;
                if (DisplayUtils.getScreenSize((Activity) context) > 7.2) {
                    dateFormatToUse = DATE_FORMAT;
                }

                nameTV.setText(getServiceName() + " - " + dateFormatToUse.format(getDateDone()));
                break;
            case DONE_CAN_NOT_BE_UNDONE:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setVisibility(VISIBLE);
                undoB.setVisibility(GONE);
                nameTV.setVisibility(VISIBLE);
                nameTV.setTextColor(context.getResources().getColor(R.color.silver));
                nameTV.setText(getServiceName() + " - " + DATE_FORMAT.format(getDateDone()));
                break;
            case OVERDUE:
                setBackgroundResource(R.drawable.vaccine_card_background_red);
                statusIV.setVisibility(GONE);
                undoB.setVisibility(GONE);
                nameTV.setVisibility(VISIBLE);
                nameTV.setTextColor(context.getResources().getColor(android.R.color.white));
                String serviceName = getServiceName();
                if (getDateDue() != null) {
                    nameTV.setText(String.format(context.getString(R.string.record_due_),
                            serviceName, DATE_FORMAT.format(getDateDue())));
                } else {
                    nameTV.setText(String.format(context.getString(R.string.record_), serviceName));
                }
                break;
            case EXPIRED:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setVisibility(GONE);
                undoB.setVisibility(GONE);
                nameTV.setVisibility(VISIBLE);
                nameTV.setTextColor(context.getResources().getColor(R.color.silver));
                nameTV.setText(context.getResources().getString(R.string.expired_colon, getServiceName()));
                break;
            default:
                break;
        }
    }

    public void updateChildsActiveStatus() {
        if (isChildActive) {
            getBackground().setAlpha(IMConstants.ACTIVE_WIDGET_ALPHA);
        } else {
            getBackground().setAlpha(IMConstants.INACTIVE_WIDGET_ALPHA);
        }
    }

    private String getServiceName() {
        String name = serviceWrapper.getName();

        try {
            name = VaccinatorUtils.getTranslatedVaccineName(context, name);
        } catch (Exception e) {
            Log.i(VaccineGroup.class.getCanonicalName(), e.getMessage(), e);
        }

        return name;
    }

    private Date getDateDue() {
        if (serviceWrapper != null) {
            DateTime vaccineDate = serviceWrapper.getVaccineDate();
            if (vaccineDate != null) return vaccineDate.toDate();
        }
        return null;
    }

    public void setChildActive(boolean childActive) {
        isChildActive = childActive;
    }

    public State getState() {
        if (state == null) {
            updateState();
        }
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Button getUndoB() {
        return undoB;
    }
}
