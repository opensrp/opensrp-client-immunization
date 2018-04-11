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
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.util.VaccinateActionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by raihan on 13/03/2017.
 */

public class ImmunizationRowCard extends LinearLayout {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private Context context;
    private Button statusIV;
    private TextView nameTV;
    private TextView StatusTV;
    private Button undoB;
    private State state;
    private VaccineWrapper vaccineWrapper;
    private boolean editmode;
    private boolean statusForMoreThanThreeMonths = false;

    public ImmunizationRowCard(Context context, boolean editmode) {
        super(context);
        this.editmode = editmode;
        init(context);
    }

    public ImmunizationRowCard(Context context) {
        super(context);
        init(context);
    }

    public ImmunizationRowCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImmunizationRowCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImmunizationRowCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    public void setVaccineWrapper(VaccineWrapper vaccineWrapper) {
        this.vaccineWrapper = vaccineWrapper;
        updateState();
    }

    public VaccineWrapper getVaccineWrapper() {
        return this.vaccineWrapper;
    }

    public void updateState() {
        this.state = State.NOT_DUE;
        if (vaccineWrapper != null) {
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
                    if ("due".equalsIgnoreCase(status)) {
                        Alert alert = getAlert();
                        if (alert != null) {
                            if ("normal".equalsIgnoreCase(alert.status().value())) {
                                state = State.DUE;
                            } else if ("urgent".equalsIgnoreCase(alert.status().value())) {
                                state = State.OVERDUE;
                            } else if ("expired".equalsIgnoreCase(alert.status().value())) {
                                state = State.EXPIRED;
                            }
                        }
                    } else if ("expired".equalsIgnoreCase(getStatus())) {
                        state = State.EXPIRED;
                    }
                }
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
                StatusTV.setText(DATE_FORMAT.format(getDateDue()));
                break;
            case DUE:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setBackgroundResource(R.drawable.vaccine_card_background_blue);
                undoB.setVisibility(INVISIBLE);
                nameTV.setVisibility(VISIBLE);
                nameTV.setText(getVaccineName());
                StatusTV.setText(DATE_FORMAT.format(getDateDue()));
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
                StatusTV.setText(DATE_FORMAT.format(getDateDue()));
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
        if (vaccineWrapper != null) {
            return vaccineWrapper.getName();
        }
        return null;
    }

    private Date getDateDue() {
        if (vaccineWrapper != null) {
            DateTime vaccineDate = vaccineWrapper.getVaccineDate();
            if (vaccineDate != null) return vaccineDate.toDate();
        }
        return null;
    }

    private Date getDateDone() {
        if (vaccineWrapper != null) {
            DateTime dateDone = vaccineWrapper.getUpdatedVaccineDate();
            if (dateDone != null) return dateDone.toDate();
        }

        return null;
    }

    private boolean isSynced() {
        if (vaccineWrapper != null) {
            return vaccineWrapper.isSynced();
        }
        return false;
    }

    private Alert getAlert() {
        if (vaccineWrapper != null) {
            return vaccineWrapper.getAlert();
        }
        return null;
    }

    private String getStatus() {
        if (vaccineWrapper != null) {
            return vaccineWrapper.getStatus();
        }
        return null;
    }

    private Date getCreatedAt() {
        if (vaccineWrapper != null) {
            return vaccineWrapper.getCreatedAt();
        }
        return null;
    }

    private Long getDbKey() {
        if (vaccineWrapper != null) {
            return vaccineWrapper.getDbKey();
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
