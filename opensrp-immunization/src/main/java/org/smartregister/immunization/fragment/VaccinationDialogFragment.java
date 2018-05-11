package org.smartregister.immunization.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vijay.jsonwizard.customviews.CheckBox;
import com.vijay.jsonwizard.customviews.RadioButton;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.smartregister.immunization.R;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineSchedule;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.listener.VaccinationActionListener;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.immunization.util.Utils;
import org.smartregister.util.DatePickerUtils;
import org.smartregister.util.OpenSRPImageLoader;
import org.smartregister.view.activity.DrishtiApplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("ValidFragment")
public class VaccinationDialogFragment extends DialogFragment {
    private List<VaccineWrapper> tags;
    private VaccinationActionListener listener;
    private Date dateOfBirth;
    private List<Vaccine> issuedVaccines;
    public static final String DIALOG_TAG = "VaccinationDialogFragment";
    public static final String WRAPPER_TAG = "tag";
    private boolean disableConstraints;
    private Calendar dcToday;
    private DialogInterface.OnDismissListener onDismissListener;
    private Integer defaultImageResourceID;
    private Integer defaultErrorImageResourceID;

    public static VaccinationDialogFragment newInstance(Date dateOfBirth,
                                                        List<Vaccine> issuedVaccines,
                                                        ArrayList<VaccineWrapper> tags) {

        VaccinationDialogFragment vaccinationDialogFragment = new VaccinationDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(WRAPPER_TAG, tags);
        vaccinationDialogFragment.setArguments(args);
        vaccinationDialogFragment.setDateOfBirth(dateOfBirth);
        vaccinationDialogFragment.setIssuedVaccines(issuedVaccines);
        vaccinationDialogFragment.setDisableConstraints(false);

        return vaccinationDialogFragment;
    }

    public static VaccinationDialogFragment newInstance(Date dateOfBirth,
                                                        List<Vaccine> issuedVaccines,
                                                        ArrayList<VaccineWrapper> tags, boolean disableConstraints) {

        VaccinationDialogFragment vaccinationDialogFragment = new VaccinationDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(WRAPPER_TAG, tags);
        vaccinationDialogFragment.setArguments(args);
        vaccinationDialogFragment.setDateOfBirth(dateOfBirth);
        vaccinationDialogFragment.setIssuedVaccines(issuedVaccines);
        vaccinationDialogFragment.setDisableConstraints(disableConstraints);

        return vaccinationDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setIssuedVaccines(List<Vaccine> issuedVaccines) {
        this.issuedVaccines = issuedVaccines;
    }

    public void setDisableConstraints(boolean disableConstraints) {
        this.disableConstraints = disableConstraints;
        if (disableConstraints) {
            Calendar dcToday = Calendar.getInstance();
            VaccineSchedule.standardiseCalendarDate(dcToday);
            this.dcToday = dcToday;
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        Serializable serializable = bundle.getSerializable(WRAPPER_TAG);
        if (serializable != null && serializable instanceof ArrayList) {
            tags = (ArrayList<VaccineWrapper>) serializable;
        }

        if (tags == null || tags.isEmpty()) {
            return null;
        }

        ViewGroup dialogView = (ViewGroup) inflater.inflate(R.layout.vaccination_dialog_view, container, false);
        TextView nameView = (TextView) dialogView.findViewById(R.id.name);
        nameView.setText(tags.get(0).getPatientName());
        TextView numberView = (TextView) dialogView.findViewById(R.id.number);
        numberView.setText(tags.get(0).getPatientNumber());

        final LinearLayout vaccinationNameLayout = (LinearLayout) dialogView.findViewById(R.id.vaccination_name_layout);

        if (tags.size() == 1) {

            String vName = "";
            VaccineWrapper vaccineWrapper = tags.get(0);
            VaccineRepo.Vaccine vaccine = vaccineWrapper.getVaccine();
            if (vaccine != null) {
                vName = vaccine.display();
            } else {
                vName = vaccineWrapper.getName();
            }

            if (vName.contains("/")) {
                String[] names = vName.split("/");
                final List<RadioButton> radios = new ArrayList<>();
                for (int i = 0; i < names.length; i++) {
                    View vaccinationName = inflater.inflate(R.layout.vaccination_name, null);
                    TextView vaccineView = (TextView) vaccinationName.findViewById(R.id.vaccine);

                    String name = names[i].trim();
                    vaccineView.setText(name);

                    View select = vaccinationName.findViewById(R.id.select);
                    select.setVisibility(View.GONE);

                    RadioButton radio = (RadioButton) vaccinationName.findViewById(R.id.radio);
                    radio.setVisibility(View.VISIBLE);
                    if (i != 0) {
                        radio.setChecked(false);
                    }
                    radios.add(radio);

                    vaccinationNameLayout.addView(vaccinationName);
                }

                addRadioClickListener(radios);

                for (int i = 0; i < vaccinationNameLayout.getChildCount(); i++) {
                    View chilView = vaccinationNameLayout.getChildAt(i);
                    chilView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RadioButton childRadio = (RadioButton) view.findViewById(R.id.radio);
                            addRadioClickListener(radios, childRadio);
                        }
                    });
                }

            } else {

                View vaccinationName = inflater.inflate(R.layout.vaccination_name, null);
                TextView vaccineView = (TextView) vaccinationName.findViewById(R.id.vaccine);

                vaccineView.setText(vName);

                View select = vaccinationName.findViewById(R.id.select);
                select.setVisibility(View.GONE);

                vaccinationNameLayout.addView(vaccinationName);
            }
        } else {
            for (VaccineWrapper vaccineWrapper : tags) {

                View vaccinationName = inflater.inflate(R.layout.vaccination_name, null);
                TextView vaccineView = (TextView) vaccinationName.findViewById(R.id.vaccine);

                VaccineRepo.Vaccine vaccine = vaccineWrapper.getVaccine();
                if (vaccineWrapper.getVaccine() != null) {
                    vaccineView.setText(vaccine.display());
                } else {
                    vaccineView.setText(vaccineWrapper.getName());
                }

                vaccinationNameLayout.addView(vaccinationName);
            }

            for (int i = 0; i < vaccinationNameLayout.getChildCount(); i++) {
                View chilView = vaccinationNameLayout.getChildAt(i);
                chilView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox childSelect = (CheckBox) view.findViewById(R.id.select);
                        childSelect.toggle();
                    }
                });
            }

            Button vaccinateToday = (Button) dialogView.findViewById(R.id.vaccinate_today);
            vaccinateToday.setText(vaccinateToday.getText().toString().replace("Vaccination", "Vaccinations"));

            Button vaccinateEarlier = (Button) dialogView.findViewById(R.id.vaccinate_earlier);
            vaccinateEarlier.setText(vaccinateEarlier.getText().toString().replace("Vaccination", "Vaccinations"));
        }

        if (tags.get(0).getId() != null) {
            ImageView mImageView = (ImageView) dialogView.findViewById(R.id.child_profilepic);
            if (tags.get(0).getId() != null) {//image already in local storage most likey ):
                //set profile image by passing the client id.If the image doesn't exist in the image repository then download and save locally
                mImageView.setTag(R.id.entity_id, tags.get(0).getId());

                int defaultImageResId = getDefaultImageResourceID() == null ? ImageUtils.profileImageResourceByGender(tags.get(0).getGender()) : getDefaultImageResourceID();
                int errorImageResId = getDefaultErrorImageResourceID() == null ? ImageUtils.profileImageResourceByGender(tags.get(0).getGender()) : getDefaultErrorImageResourceID();
                DrishtiApplication.getCachedImageLoaderInstance().getImageByClientId(tags.get(0).getId(), OpenSRPImageLoader.getStaticImageListener((ImageView) mImageView, defaultImageResId, errorImageResId));
            }
        }

        final DatePicker earlierDatePicker = (DatePicker) dialogView.findViewById(R.id.earlier_date_picker);

        String color = tags.get(0).getColor();
        Button status = (Button) dialogView.findViewById(R.id.status);
        if (status != null) {
            status.setBackgroundColor(StringUtils.isBlank(color) ? Color.WHITE : Color.parseColor(color));
        }

        final Button set = (Button) dialogView.findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

                ArrayList<VaccineWrapper> tagsToUpdate = new ArrayList<VaccineWrapper>();

                int day = earlierDatePicker.getDayOfMonth();
                int month = earlierDatePicker.getMonth();
                int year = earlierDatePicker.getYear();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                DateTime dateTime = new DateTime(calendar.getTime());
                if (tags.size() == 1) {
                    VaccineWrapper tag = tags.get(0);
                    String radioName = findSelectRadio(vaccinationNameLayout);
                    if (radioName != null) {
                        tag.setName(radioName);
                    }

                    if (validateVaccinationDate(tag, dateTime.toDate())) {
                        tag.setUpdatedVaccineDate(dateTime, false);
                        tagsToUpdate.add(tag);
                    } else {
                        Toast.makeText(VaccinationDialogFragment.this.getActivity(),
                                String.format(getString(R.string.cannot_record_vaccine), tag.getName()),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    List<String> selectedCheckboxes = findSelectedCheckBoxes(vaccinationNameLayout);
                    for (String checkedName : selectedCheckboxes) {
                        VaccineWrapper tag = searchWrapperByName(checkedName);
                        if (tag != null) {
                            if (validateVaccinationDate(tag, dateTime.toDate())) {
                                tag.setUpdatedVaccineDate(dateTime, false);
                                tagsToUpdate.add(tag);
                            } else {
                                Toast.makeText(VaccinationDialogFragment.this.getActivity(),
                                        String.format(getString(R.string.cannot_record_vaccine),
                                                tag.getName()), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                listener.onVaccinateEarlier(tagsToUpdate, view);

            }
        });

        final Button vaccinateToday = (Button) dialogView.findViewById(R.id.vaccinate_today);
        vaccinateToday.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

                ArrayList<VaccineWrapper> tagsToUpdate = new ArrayList<VaccineWrapper>();

                Calendar calendar = Calendar.getInstance();
                DateTime dateTime = new DateTime(calendar.getTime());
                if (tags.size() == 1) {
                    VaccineWrapper tag = tags.get(0);
                    tag.setUpdatedVaccineDate(dateTime, true);

                    String radioName = findSelectRadio(vaccinationNameLayout);
                    if (radioName != null) {
                        tag.setName(radioName);
                    }
                    tagsToUpdate.add(tag);
                } else {
                    List<String> selectedCheckboxes = findSelectedCheckBoxes(vaccinationNameLayout);
                    for (String checkedName : selectedCheckboxes) {
                        VaccineWrapper tag = searchWrapperByName(checkedName);
                        if (tag != null) {
                            tag.setUpdatedVaccineDate(dateTime, true);
                            tagsToUpdate.add(tag);
                        }
                    }
                }

                listener.onVaccinateToday(tagsToUpdate, view);

            }
        });

        final Button vaccinateEarlier = (Button) dialogView.findViewById(R.id.vaccinate_earlier);
        vaccinateEarlier.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaccinateEarlier.setVisibility(View.GONE);
                earlierDatePicker.setVisibility(View.VISIBLE);
                set.setVisibility(View.VISIBLE);

                DatePickerUtils.themeDatePicker(earlierDatePicker, new char[]{'d', 'm', 'y'});
            }
        });

        updateDateRanges(vaccinateToday, vaccinateEarlier, set, earlierDatePicker);

        Button cancel = (Button) dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return dialogView;
    }

    private boolean validateVaccinationDate(VaccineWrapper vaccine, Date date) {
        // Assuming that the vaccine wrapper provided to this method isn't one where there's more than one vaccine in a wrapper
        Date minDate;
        Date maxDate;

        if (disableConstraints) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateOfBirth);
            VaccineSchedule.standardiseCalendarDate(calendar);

            minDate = calendar.getTime();
            maxDate = dcToday.getTime();
        } else {
            minDate = getMinVaccineDate(vaccine.getName());
            maxDate = getMaxVaccineDate(vaccine.getName());
        }
        Calendar vaccineDate = Calendar.getInstance();
        vaccineDate.setTime(date);
        VaccineSchedule.standardiseCalendarDate(vaccineDate);
        boolean result = true;

        // A null min date means the vaccine is not due (probably because the prerequisite hasn't been done yet)
        result = result && minDate != null;

        // Check if vaccination was done before min date
        if (minDate != null) {
            Calendar min = Calendar.getInstance();
            min.setTime(minDate);
            VaccineSchedule.standardiseCalendarDate(min);

            result = result && min.getTimeInMillis() <= vaccineDate.getTimeInMillis();
        }

        // A null max date means the vaccine doesn't have a max date check
        //Check if vaccination was done after the max date
        if (maxDate != null) {
            Calendar max = Calendar.getInstance();
            max.setTime(maxDate);
            VaccineSchedule.standardiseCalendarDate(max);

            result = result && vaccineDate.getTimeInMillis() <= max.getTimeInMillis();
        }

        return result;
    }

    /**
     * This method updates the allowed date ranges in the views
     *
     * @param vaccinateToday    The 'Vaccination done today' button
     * @param earlierDatePicker Date picker for selecting a previous date for a vaccine
     */
    private void updateDateRanges(Button vaccinateToday, Button vaccinateEarlier, Button set, DatePicker earlierDatePicker) {
        Calendar today = Calendar.getInstance();
        VaccineSchedule.standardiseCalendarDate(today);
        Calendar minDate = null;
        Calendar maxDate = null;

        if (disableConstraints) {
            minDate = Calendar.getInstance();
            minDate.setTime(dateOfBirth);
            maxDate = dcToday;
        } else {
            for (VaccineWrapper curVaccine : tags) {
                if (!curVaccine.getName().contains("/")) {
                    minDate = updateMinVaccineDate(minDate, curVaccine.getName());
                    maxDate = updateMaxVaccineDate(maxDate, curVaccine.getName());
                } else {
                    String[] sisterVaccines = curVaccine.getName().split(" / ");
                    for (int i = 0; i < sisterVaccines.length; i++) {
                        minDate = updateMinVaccineDate(minDate, sisterVaccines[i]);
                        maxDate = updateMaxVaccineDate(maxDate, sisterVaccines[i]);
                    }
                }
            }
        }

        VaccineSchedule.standardiseCalendarDate(minDate);
        VaccineSchedule.standardiseCalendarDate(maxDate);

        if (maxDate.getTimeInMillis() >= minDate.getTimeInMillis()) {
            vaccinateToday.setTextColor(getActivity().getResources().getColor(R.color.white));
            vaccinateToday.setBackground(getActivity().getResources().getDrawable(R.drawable.vaccination_today_bg));
            vaccinateEarlier.setBackground(getActivity().getResources().getDrawable(R.drawable.vaccination_earlier_bg));
            if (today.getTimeInMillis() >= minDate.getTimeInMillis()
                    && today.getTimeInMillis() <= maxDate.getTimeInMillis()) {
                vaccinateToday.setClickable(true);
                vaccinateToday.setVisibility(View.VISIBLE);

                vaccinateEarlier.setVisibility(View.VISIBLE);
                earlierDatePicker.setVisibility(View.GONE);
                set.setVisibility(View.GONE);
            } else {
                vaccinateToday.setClickable(false);
                vaccinateToday.setVisibility(View.GONE);

                vaccinateEarlier.setVisibility(View.GONE);
                earlierDatePicker.setVisibility(View.VISIBLE);
                set.setVisibility(View.VISIBLE);

                DatePickerUtils.themeDatePicker(earlierDatePicker, new char[]{'d', 'm', 'y'});
            }

            earlierDatePicker.setMinDate(minDate.getTimeInMillis());
            earlierDatePicker.setMaxDate(maxDate.getTimeInMillis());
        } else {
            vaccinateToday.setClickable(false);
            vaccinateToday.setTextColor(getActivity().getResources().getColor(R.color.client_list_grey));
            vaccinateToday.setBackground(getActivity().getResources().getDrawable(R.drawable.vaccination_today_bg_disabled));
            vaccinateEarlier.setClickable(false);
            vaccinateEarlier.setBackground(getActivity().getResources().getDrawable(R.drawable.vaccination_earlier_bg_disabled));
            Toast.makeText(getActivity(), R.string.problem_applying_vaccine_constraints, Toast.LENGTH_LONG).show();
        }
    }

    private Calendar updateMinVaccineDate(Calendar minDate_, String vaccineName) {
        Date dueDate = getMinVaccineDate(vaccineName);
        Calendar minDate = minDate_;
        if (dueDate == null
                || dueDate.getTime() < dateOfBirth.getTime()) {
            dueDate = dateOfBirth;
        }

        if (minDate == null) {
            minDate = Calendar.getInstance();
            minDate.setTime(dueDate);
        } else if (dueDate.getTime() > minDate.getTimeInMillis()) {
            minDate.setTime(dueDate);
        }

        return minDate;
    }

    private Calendar updateMaxVaccineDate(Calendar maxDate_, String vaccineName) {
        Date expiryDate = getMaxVaccineDate(vaccineName);
        Calendar maxDate = maxDate_;
        if (expiryDate == null
                || expiryDate.getTime() > Calendar.getInstance().getTimeInMillis()) {
            expiryDate = Calendar.getInstance().getTime();
        }

        if (maxDate == null) {
            maxDate = Calendar.getInstance();
            maxDate.setTime(expiryDate);
        } else if (expiryDate.getTime() < maxDate.getTimeInMillis()) {
            maxDate.setTime(expiryDate);
        }

        return maxDate;
    }

    private Date getMinVaccineDate(String vaccineName) {
        VaccineSchedule curVaccineSchedule = VaccineSchedule.getVaccineSchedule("child",
                vaccineName);
        Date minDate = null;
        if (curVaccineSchedule == null) {
            curVaccineSchedule = VaccineSchedule.getVaccineSchedule("woman",
                    vaccineName);
        }
        if (curVaccineSchedule != null) {
            minDate = curVaccineSchedule.getDueDate(issuedVaccines, dateOfBirth);
        }

        return minDate;
    }

    private Date getMaxVaccineDate(String vaccineName) {
        VaccineSchedule curVaccineSchedule = VaccineSchedule.getVaccineSchedule("child",
                vaccineName);
        Date maxDate = null;
        if (curVaccineSchedule == null) {
            curVaccineSchedule = VaccineSchedule.getVaccineSchedule("woman",
                    vaccineName);
        }
        if (curVaccineSchedule != null) {
            maxDate = curVaccineSchedule.getExpiryDate(issuedVaccines, dateOfBirth);
        }

        return maxDate;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (VaccinationActionListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement VaccinationActionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // without a handler, the window sizes itself correctly
        // but the keyboard does not show up
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Window window = null;
                if (getDialog() != null) {
                    window = getDialog().getWindow();
                }

                if (window == null) {
                    return;
                }

                Point size = new Point();

                Display display = window.getWindowManager().getDefaultDisplay();
                display.getSize(size);

                int width = size.x;
                double widthFactor = Utils.calculateDialogWidthFactor(getActivity());

                window.setLayout((int) (width * widthFactor), FrameLayout.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
            }
        });
    }

    private VaccineWrapper searchWrapperByName(String name) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }

        for (VaccineWrapper tag : tags) {
            if (tag.getVaccine() != null) {
                if (tag.getVaccine().display().equals(name)) {
                    return tag;
                }
            } else {
                if (tag.getName().equals(name)) {
                    return tag;
                }
            }
        }
        return null;
    }

    private void addRadioClickListener(final List<RadioButton> radios) {
        for (final RadioButton radio : radios) {
            radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (RadioButton otherRadio : radios) {
                        otherRadio.setChecked(false);
                    }
                    radio.setChecked(true);
                }
            });
        }
    }

    private void addRadioClickListener(final List<RadioButton> radios, RadioButton radio) {
        for (RadioButton otherRadio : radios) {
            otherRadio.setChecked(false);
        }
        radio.setChecked(true);
    }

    private List<String> findSelectedCheckBoxes(LinearLayout vaccinationNameLayout) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < vaccinationNameLayout.getChildCount(); i++) {
            View chilView = vaccinationNameLayout.getChildAt(i);
            CheckBox selectChild = (CheckBox) chilView.findViewById(R.id.select);
            if (selectChild.isChecked()) {
                TextView childVaccineView = (TextView) chilView.findViewById(R.id.vaccine);
                String checkedName = childVaccineView.getText().toString();
                names.add(checkedName);
            }
        }

        return names;
    }

    private String findSelectRadio(LinearLayout vaccinationNameLayout) {
        for (int i = 0; i < vaccinationNameLayout.getChildCount(); i++) {
            View chilView = vaccinationNameLayout.getChildAt(i);
            RadioButton radioChild = (RadioButton) chilView.findViewById(R.id.radio);
            if (radioChild.getVisibility() == View.VISIBLE && radioChild.isChecked()) {
                TextView childVaccineView = (TextView) chilView.findViewById(R.id.vaccine);
                return childVaccineView.getText().toString();
            }
        }
        return null;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public Integer getDefaultImageResourceID() {
        return defaultImageResourceID;
    }

    public void setDefaultImageResourceID(Integer defaultImageResourceID) {
        this.defaultImageResourceID = defaultImageResourceID;
    }

    public Integer getDefaultErrorImageResourceID() {
        return defaultErrorImageResourceID;
    }

    public void setDefaultErrorImageResourceID(Integer defaultErrorImageResourceID) {
        this.defaultErrorImageResourceID = defaultErrorImageResourceID;
    }
}
