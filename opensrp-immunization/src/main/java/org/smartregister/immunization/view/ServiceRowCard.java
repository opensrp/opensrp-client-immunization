package org.smartregister.immunization.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.R;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.util.VaccinateActionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by raihan on 13/03/2017.
 */

public class ServiceRowCard extends LinearLayout {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private Context context;
    private Button statusIV;
    private TextView nameTV;
    private TextView StatusTV;
    private Button undoB;
    private State state;
    private ServiceWrapper serviceWrapper;
    private boolean editmode;
    private boolean statusForMoreThanThreeMonths = false;

    public ServiceRowCard(Context context, boolean editmode) {
        super(context);
        this.editmode = editmode;
        init(context);
    }

    public ServiceRowCard(Context context) {
        super(context);
        init(context);
    }

    public ServiceRowCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ServiceRowCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ServiceRowCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_immunization_row_card, this, true);
        statusIV = (Button) findViewById(R.id.status_iv);
        StatusTV = (TextView) findViewById(R.id.status_text_tv);
        nameTV = (TextView) findViewById(R.id.name_tv);
        undoB = (Button) findViewById(R.id.undo_b);
    }

    public void setServiceWrapper(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
        updateState();
    }

    public ServiceWrapper getServiceWrapper() {
        return serviceWrapper;
    }

    public void updateState() {
        this.state = State.NOT_DUE;
        if (serviceWrapper != null) {
            Date dateDone = getDateDone();
            boolean isSynced = isSynced();
            String status = getStatus();

            if (dateDone != null) {// Vaccination was done
                if (isSynced) {
                    this.state = State.DONE_CAN_NOT_BE_UNDONE;
                } else {
                    this.state = State.DONE_CAN_BE_UNDONE;
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
                    } else if ("expired".equalsIgnoreCase(getStatus())) {
                        state = State.EXPIRED;
                    }
                }

//                Calendar today = Calendar.getInstance();
//                today.set(Calendar.HOUR_OF_DAY, 0);
//                today.set(Calendar.MINUTE, 0);
//                today.set(Calendar.SECOND, 0);
//                today.set(Calendar.MILLISECOND, 0);
//                if (getDateDue().getTime() > (today.getTimeInMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))) {
//                    // Vaccination due more than one day from today
//                    this.state = State.NOT_DUE;
//                } else if (getDateDue().getTime() < (today.getTimeInMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))) {
//                    // Vaccination overdue
//                    this.state = State.OVERDUE;
//                } else {
//                    this.state = State.DUE;
//                }
            }
            updateStateUi();
        }

    }

    public State getState() {
        if (this.state == null) {
            updateState();
        }
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    private void updateStateUi() {
        if (getDbKey() != null) {
            statusForMoreThanThreeMonths = VaccinateActionUtils.moreThanThreeMonths(getCreatedAt());
        }

        statusIV.setVisibility(VISIBLE);
        switch (state) {
            case NOT_DUE:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setBackgroundResource(R.drawable.vaccine_card_background_white);
                undoB.setVisibility(INVISIBLE);
                nameTV.setVisibility(VISIBLE);
                nameTV.setTextColor(context.getResources().getColor(R.color.silver));
                nameTV.setText(getVaccineName());
                StatusTV.setText(getDateDue() == null ? "" : DATE_FORMAT.format(getDateDue()));
                break;
            case DUE:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setBackgroundResource(R.drawable.vaccine_card_background_blue);
                undoB.setVisibility(INVISIBLE);
                nameTV.setVisibility(VISIBLE);
                nameTV.setText(getVaccineName());
                StatusTV.setText(getDateDue() == null ? "" : DATE_FORMAT.format(getDateDue()));
                break;
            case DONE_CAN_BE_UNDONE:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setBackgroundResource(R.drawable.vaccine_card_background_green);
                if (editmode && !statusForMoreThanThreeMonths) {
                    undoB.setVisibility(VISIBLE);
                } else {
                    undoB.setVisibility(INVISIBLE);
                }
                nameTV.setVisibility(VISIBLE);
                nameTV.setText(getVaccineName());
                StatusTV.setText(DATE_FORMAT.format(getDateDone()));
                break;
            case DONE_CAN_NOT_BE_UNDONE:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setBackgroundResource(R.drawable.vaccine_card_background_green);
                if (editmode && !statusForMoreThanThreeMonths) {
                    undoB.setVisibility(VISIBLE);
                } else {
                    undoB.setVisibility(INVISIBLE);
                }
                nameTV.setVisibility(VISIBLE);
                nameTV.setText(getVaccineName());
                StatusTV.setText(DATE_FORMAT.format(getDateDone()));
                break;
            case OVERDUE:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setBackgroundResource(R.drawable.vaccine_card_background_red);
                undoB.setVisibility(INVISIBLE);
                nameTV.setVisibility(VISIBLE);
                nameTV.setText(getVaccineName());
                StatusTV.setText(getDateDue() == null ? "" : DATE_FORMAT.format(getDateDue()));
                break;
            case EXPIRED:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setBackgroundResource(R.drawable.vaccine_card_background_white);
                undoB.setVisibility(INVISIBLE);
                nameTV.setText(getVaccineName());
                StatusTV.setText("Expired");
                StatusTV.setTextColor(context.getResources().getColor(R.color.silver));
                break;
            default:
                break;
        }
    }

    private String getVaccineName() {
        if (serviceWrapper != null) {
            return serviceWrapper.getName();
        }
        return null;
    }

    private Date getDateDue() {
        if (serviceWrapper != null) {
            DateTime vaccineDate = serviceWrapper.getVaccineDate();
            if (vaccineDate != null) return vaccineDate.toDate();
        }
        return null;
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

    private Alert getAlert() {
        if (serviceWrapper != null) {
            return serviceWrapper.getAlert();
        }
        return null;
    }

    private String getStatus() {
        if (serviceWrapper != null) {
            return serviceWrapper.getStatus();
        }
        return null;
    }

    private Date getCreatedAt() {
        if (serviceWrapper != null) {
            return serviceWrapper.getCreatedAt();
        }
        return null;
    }

    private Long getDbKey() {
        if (serviceWrapper != null) {
            return serviceWrapper.getDbKey();
        }
        return null;
    }

    public Button getUndoB() {
        return undoB;
    }

    public boolean isEditmode() {
        return editmode;
    }

    public boolean isStatusForMoreThanThreeMonths() {
        return statusForMoreThanThreeMonths;
    }
}
