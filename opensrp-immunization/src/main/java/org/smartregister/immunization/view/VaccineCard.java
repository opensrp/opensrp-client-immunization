package org.smartregister.immunization.view;

import static org.smartregister.immunization.util.IMConstants.isInvalidVaccineMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.VaccinateActionUtils;
import org.smartregister.immunization.util.VaccinatorUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by Jason Rogena - jrogena@ona.io on 21/02/2017.
 */

public class VaccineCard extends LinearLayout {
    private static SimpleDateFormat DATE_FORMAT;
    private static SimpleDateFormat SHORT_DATE_FORMAT;
    private Context context;
    private ImageView statusIV;
    private TextView nameTV;
    private Button undoB;
    private TextView invalidB;
    private State state;
    private VaccineWrapper vaccineWrapper;
    private boolean isChildActive = true;
//    private boolean statusInvalidVaccine= false;
    public VaccineCard(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_vaccination_card, this, true);
        statusIV = findViewById(R.id.status_iv);
        nameTV = findViewById(R.id.name_tv);
        undoB = findViewById(R.id.undo_b);
        invalidB = findViewById(R.id.invalid_b);
        //Init date formatters here to allow for dynamic language switching
        DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        SHORT_DATE_FORMAT = new SimpleDateFormat("dd/MM",Locale.ENGLISH);
    }

    public VaccineCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VaccineCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VaccineCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public VaccineWrapper getVaccineWrapper() {
        return vaccineWrapper;
    }

    public void setVaccineWrapper(VaccineWrapper vaccineWrapper) {
        this.vaccineWrapper = vaccineWrapper;
        updateState();
    }

    public void updateState() {
        state = State.NOT_DUE;
        if (vaccineWrapper != null) {
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
                    } else if (vaccineWrapper.getStatus().equalsIgnoreCase("expired")) {
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
            //need to ignore invalid vaccine. applycable for next vaccines
            for (String keys : isInvalidVaccineMap.keySet()){
                Log.e("isInvalidVaccineMap","keys:"+keys+":vaccine:"+vaccineWrapper.getName()+":map:"+isInvalidVaccineMap);

                if(vaccineWrapper.getName().equalsIgnoreCase(getApplicableVaccineName(keys))){
                    Log.e("isInvalidVaccineMap","ignore>>>>"+vaccineWrapper.getName());
                    state = State.NOT_DUE;
                }
//
//
//                if(!vaccineWrapper.getName().equalsIgnoreCase(VaccineRepo.Vaccine.opv0.display())){
//                    if(!keys.equalsIgnoreCase(vaccineWrapper.getName())){
//                        Log.e("isInvalidVaccineMap","keys:"+keys+":vaccine:"+vaccineWrapper.getName()+":"+keys.contains(vaccineWrapper.getName()));
//                        if(keys.contains(vaccineWrapper.getName().split(" ")[0])){
//                            Log.e("isInvalidVaccineMap","ignore>>>>"+vaccineWrapper.getName());
//                            state = State.NOT_DUE;
//                        }
//                    }
//                }

            }
            updateStateUi();
            updateChildsActiveStatus();

        }
    }
    private String getApplicableVaccineName(String keysName){
        switch (keysName){
            case "OPV 1":
                return "OPV 2";
            case "OPV 2":
                return "OPV 3";
            case "Penta 1":
                return "Penta 2";
            case "Penta 2":
                return "Penta 3";
            case "PCV 1":
                return "PCV 2";
            case "PCV 2":
                return "PCV 3";
            case "MR 1":
                return "MR 2";
        }
        return "";
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

    private String getStatus() {
        if (vaccineWrapper != null) {
            return vaccineWrapper.getStatus();
        }
        return null;
    }

    private Alert getAlert() {
        if (vaccineWrapper != null) {
            return vaccineWrapper.getAlert();
        }
        return null;
    }
    public DateTime getVaccineDate() {
        return vaccineWrapper.getUpdatedVaccineDate();
    }
    public DateTime getVaccineDueDate() {
        return vaccineWrapper.getVaccineDate();
    }
    @SuppressLint("SetTextI18n")
    private void updateStateUi() {
        String vaccineName = getVaccineName();
        boolean statusInvalidVaccine = VaccinateActionUtils.isInvalidVaccine(getVaccineDate(),getVaccineDueDate(),vaccineName);
        switch (state) {
            case NOT_DUE:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setVisibility(GONE);
                undoB.setVisibility(GONE);
                invalidB.setVisibility(GONE);
                nameTV.setVisibility(VISIBLE);
                nameTV.setTextColor(context.getResources().getColor(R.color.silver));
                nameTV.setText(getVaccineName());
                break;
            case DUE:
                setBackgroundResource(R.drawable.vaccine_card_background_blue);
                statusIV.setVisibility(GONE);
                undoB.setVisibility(GONE);

                if(statusInvalidVaccine&& vaccineName!=null && !vaccineName.contains("tt")){
                    invalidB.setVisibility(VISIBLE);
                }else{
                    invalidB.setVisibility(GONE);
                }
                nameTV.setVisibility(VISIBLE);
                nameTV.setTextColor(context.getResources().getColor(android.R.color.white));

                nameTV.setText(String.format(context.getString(R.string.record_due_), vaccineName, DATE_FORMAT.format(getDateDue())));
                if (vaccineName.toLowerCase().contains("measles") || vaccineName.toLowerCase().contains("mr")) {
                    nameTV.setText(String.format(context.getString(R.string.mr_due_),
                            vaccineName, DATE_FORMAT.format(getDateDue())));
                }
                break;
            case DONE_CAN_BE_UNDONE:

                if(statusInvalidVaccine && vaccineName!=null&&!vaccineName.contains("tt")){

                    invalidB.setText("Invalid Dose given at \n"+DATE_FORMAT.format(getDateDone()));
                    nameTV.setText(vaccineName+" - Due "+DATE_FORMAT.format(getDateDue()));
                    setBackgroundResource(R.drawable.vaccine_card_background_white);
                    if(VaccinateActionUtils.isValidAfterInvalid(getVaccineDueDate())){
                        nameTV.setText(String.format(context.getString(R.string.record_due_), vaccineName, DATE_FORMAT.format(getDateDue())));
                        nameTV.setTextColor(context.getResources().getColor(android.R.color.white));
                        setBackgroundResource(R.drawable.vaccine_card_background_blue);
                        invalidB.setVisibility(VISIBLE);
                    }else{
                        setBackgroundResource(R.drawable.vaccine_card_background_white);
                        invalidB.setClickable(false);
                        invalidB.setVisibility(VISIBLE);
                    }
                    statusIV.setVisibility(GONE);
                }else{
                    invalidB.setVisibility(GONE);
                    nameTV.setText(vaccineName + " - " + DATE_FORMAT.format(getDateDone()));
                    nameTV.setVisibility(VISIBLE);
                    nameTV.setTextColor(context.getResources().getColor(R.color.silver));
                    setBackgroundResource(R.drawable.vaccine_card_background_white);
                    statusIV.setVisibility(VISIBLE);
                    undoB.setVisibility(GONE);
                }


                break;
            case DONE_CAN_NOT_BE_UNDONE:

                if(statusInvalidVaccine && vaccineName!=null&&!vaccineName.contains("tt")){

                    invalidB.setText("Invalid Dose given at \n"+DATE_FORMAT.format(getDateDone()));
                    nameTV.setText(vaccineName+" - Due "+DATE_FORMAT.format(getDateDue()));
                    setBackgroundResource(R.drawable.vaccine_card_background_white);
                    if(VaccinateActionUtils.isValidAfterInvalid(getVaccineDueDate())){
                        nameTV.setText(String.format(context.getString(R.string.record_due_), vaccineName, DATE_FORMAT.format(getDateDue())));
                        nameTV.setTextColor(context.getResources().getColor(android.R.color.white));
                        setBackgroundResource(R.drawable.vaccine_card_background_blue);
                        invalidB.setVisibility(VISIBLE);
                    }else{
                        setBackgroundResource(R.drawable.vaccine_card_background_white);
                        invalidB.setClickable(false);
                        invalidB.setVisibility(VISIBLE);
                    }
                    statusIV.setVisibility(GONE);
                }else{
                    invalidB.setVisibility(GONE);
                    nameTV.setText(vaccineName + " - " + DATE_FORMAT.format(getDateDone()));
                    nameTV.setVisibility(VISIBLE);
                    nameTV.setTextColor(context.getResources().getColor(R.color.silver));
                    setBackgroundResource(R.drawable.vaccine_card_background_white);
                    statusIV.setVisibility(VISIBLE);
                    undoB.setVisibility(GONE);
                }


                break;
            case OVERDUE:
                setBackgroundResource(R.drawable.vaccine_card_background_red);
                statusIV.setVisibility(GONE);
                undoB.setVisibility(GONE);
                if(statusInvalidVaccine&& vaccineName!=null&&!vaccineName.contains("tt")){
                    invalidB.setVisibility(VISIBLE);
                }else{
                    invalidB.setVisibility(GONE);
                }
                nameTV.setVisibility(VISIBLE);
                nameTV.setTextColor(context.getResources().getColor(android.R.color.white));
                nameTV.setText(String.format(context.getString(R.string.record_due_),
                        vaccineName, DATE_FORMAT.format(getDateDue())));
                if (vaccineName.toLowerCase().contains("measles") || vaccineName.toLowerCase().contains("mr")) {
                    nameTV.setText(String.format(context.getString(R.string.mr_due_),
                            vaccineName, DATE_FORMAT.format(getDateDue())));
                }
                break;
            case EXPIRED:
                setBackgroundResource(R.drawable.vaccine_card_background_white);
                statusIV.setVisibility(GONE);
                undoB.setVisibility(GONE);
                if(statusInvalidVaccine){
                    invalidB.setVisibility(VISIBLE);
                }else{
                    invalidB.setVisibility(GONE);
                }
                nameTV.setVisibility(VISIBLE);
                nameTV.setTextColor(context.getResources().getColor(R.color.silver));
                nameTV.setText(context.getResources().getString(R.string.expired_colon,vaccineName));
                break;
            default:
                break;
        }
    }

    public void updateChildsActiveStatus() {
        if (isChildActive) {
            setBackgroundAlpha(IMConstants.ACTIVE_WIDGET_ALPHA);
        } else {
            setBackgroundAlpha(IMConstants.INACTIVE_WIDGET_ALPHA);
        }
    }

    private String getVaccineName() {
        if (vaccineWrapper != null) {
            String name = vaccineWrapper.getName();

            try {
                name = VaccinatorUtils.getTranslatedVaccineName(context, name);
            } catch (Exception e) {
                Log.i(VaccineGroup.class.getCanonicalName(), e.getMessage(), e);
            }

            return name;
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

    private void setBackgroundAlpha(int alpha) {
        getBackground().setAlpha(alpha);
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
