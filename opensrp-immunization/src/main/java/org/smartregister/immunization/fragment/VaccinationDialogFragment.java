package org.smartregister.immunization.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.vijay.jsonwizard.customviews.CheckBox;
import com.vijay.jsonwizard.customviews.RadioButton;
import com.vijay.jsonwizard.utils.NativeFormsProperties;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineSchedule;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.listener.VaccinationActionListener;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.immunization.util.Utils;
import org.smartregister.immunization.util.VaccinatorUtils;
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
    public static final String DIALOG_TAG = "VaccinationDialogFragment";
    public static final String WRAPPER_TAG = "tag";
    private List<VaccineWrapper> tags;
    private VaccinationActionListener listener;
    private Date dateOfBirth;
    private List<Vaccine> issuedVaccines;
    private boolean disableConstraints;
    private Calendar dcToday;
    private DialogInterface.OnDismissListener onDismissListener;
    private Integer defaultImageResourceID;
    private Integer defaultErrorImageResourceID;
    private boolean isNumericDatePicker = Utils.isPropertyTrue(NativeFormsProperties.KEY.WIDGET_DATEPICKER_IS_NUMERIC);

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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
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

    @Override
    public void onAttach(Context activity) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if (bundle != null) {

            Serializable serializable = bundle.getSerializable(WRAPPER_TAG);
            if (serializable != null && serializable instanceof ArrayList) {
                tags = (ArrayList<VaccineWrapper>) serializable;
            }

            if (tags == null || tags.isEmpty()) {
                return null;
            }
        }

        ViewGroup dialogView = (ViewGroup) inflater.inflate(R.layout.vaccination_dialog_view, container, false);
        dialogView.setFilterTouchesWhenObscured(true);
        TextView nameView = dialogView.findViewById(R.id.name);
        nameView.setText(tags.get(0).getPatientName());
        TextView numberView = dialogView.findViewById(R.id.number);
        numberView.setText(tags.get(0).getPatientNumber());

        final LinearLayout vaccinationNameLayout = dialogView.findViewById(R.id.vaccination_name_layout);

        final Button vaccinateToday = dialogView.findViewById(R.id.vaccinate_today);
        final Button vaccinateEarlier = dialogView.findViewById(R.id.vaccinate_earlier);

        if (tags.size() == 1) {

            String vName;
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
                    TextView vaccineView = vaccinationName.findViewById(R.id.vaccine);

                    String name = names[i].trim();
                    vaccineView.setText(VaccinatorUtils.getTranslatedVaccineName(getActivity(), name));

                    View select = vaccinationName.findViewById(R.id.select);
                    select.setVisibility(View.GONE);

                    RadioButton radio = vaccinationName.findViewById(R.id.radio);
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
                            RadioButton childRadio = view.findViewById(R.id.radio);
                            addRadioClickListener(radios, childRadio);
                        }
                    });
                }

            } else {

                View vaccinationName = inflater.inflate(R.layout.vaccination_name, null);
                TextView vaccineView = vaccinationName.findViewById(R.id.vaccine);

                if (vaccineView != null) {

                    vaccineView.setText(VaccinatorUtils.getTranslatedVaccineName(getActivity(), vName));

                    View select = vaccinationName.findViewById(R.id.select);
                    select.setVisibility(View.GONE);

                    vaccinationNameLayout.addView(vaccinationName);
                }
            }

        } else {
            for (VaccineWrapper vaccineWrapper : tags) {

                View vaccinationName = inflater.inflate(R.layout.vaccination_name, null);
                TextView vaccineView = vaccinationName.findViewById(R.id.vaccine);

                VaccineRepo.Vaccine vaccine = vaccineWrapper.getVaccine();
                if (vaccineWrapper.getVaccine() != null) {
                    vaccineView.setText(VaccinatorUtils.getTranslatedVaccineName(getActivity(), vaccine.display()));
                } else {
                    vaccineView.setText(VaccinatorUtils.getTranslatedVaccineName(getActivity(), vaccineWrapper.getName()));
                }

                vaccineView.setTag(R.id.vaccine, vaccineWrapper.getName());
                vaccinationName.setTag(vaccineWrapper.getName());
                if (vaccineWrapper.getNotGivenCondition() != null) {
                    vaccinationName.setTag(R.id.not_give_condition_id, vaccineWrapper.getNotGivenCondition());
                }

                vaccinationNameLayout.addView(vaccinationName);
            }

            for (int i = 0; i < vaccinationNameLayout.getChildCount(); i++) {
                View chilView = vaccinationNameLayout.getChildAt(i);
                chilView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox childSelect = view.findViewById(R.id.select);
                        childSelect.toggle();
                        // toggle other in not given condition
                        if (view.getTag(R.id.not_give_condition_id) != null) {
                            View chilView2 = vaccinationNameLayout.findViewWithTag(view.getTag(R.id.not_give_condition_id));
                            if (chilView2 != null) {
                                CheckBox childSelect2 = chilView2.findViewById(R.id.select);
                                childSelect2.setChecked(!childSelect.isChecked());
                            }
                        }
                    }
                });
            }

            // handle for when actual checkbox is clicked
            for (int i = 0; i < vaccinationNameLayout.getChildCount(); i++) {
                final View chilView = vaccinationNameLayout.getChildAt(i);
                CheckBox childSelect = chilView.findViewById(R.id.select);

                childSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chilView.getTag(R.id.not_give_condition_id) != null) {
                            View chilView2 = vaccinationNameLayout
                                    .findViewWithTag(chilView.getTag(R.id.not_give_condition_id));
                            if (chilView2 != null) {
                                CheckBox childSelect2 = chilView2.findViewById(R.id.select);
                                childSelect2.setChecked(!((CompoundButton) v).isChecked());
                            }
                        }
                    }
                });
            }

            //uncheck mutually exclusive vaccines on starting up
            for (int i = 0; i < vaccinationNameLayout.getChildCount(); i++) {
                View chilView = vaccinationNameLayout.getChildAt(i);
                CheckBox childSelect = chilView.findViewById(R.id.select);

                if (chilView.getTag(R.id.not_give_condition_id) != null) {
                    View chilView2 = vaccinationNameLayout.findViewWithTag(chilView.getTag(R.id.not_give_condition_id));
                    if (chilView2 != null) {
                        CheckBox childSelect2 = chilView2.findViewById(R.id.select);
                        childSelect2.setChecked(!childSelect.isChecked());
                    }
                }
            }

            vaccinateToday.setText(R.string.vaccines_done_today);
            vaccinateEarlier.setText(R.string.vaccines_done_earlier);
        }

        if (tags.get(0).getId() != null) {
            ImageView mImageView = dialogView.findViewById(R.id.child_profilepic);
            if (tags.get(0).getId() != null) {//image already in local storage most likey ):
                //set profile image by passing the client id.If the image doesn't exist in the image repository then download and save locally
                mImageView.setTag(R.id.entity_id, tags.get(0).getId());

                int defaultImageResId = getDefaultImageResourceID() == null ? ImageUtils
                        .profileImageResourceByGender(tags.get(0).getGender()) : getDefaultImageResourceID();
                int errorImageResId = getDefaultErrorImageResourceID() == null ? ImageUtils
                        .profileImageResourceByGender(tags.get(0).getGender()) : getDefaultErrorImageResourceID();
                DrishtiApplication.getCachedImageLoaderInstance().getImageByClientId(tags.get(0).getId(), OpenSRPImageLoader
                        .getStaticImageListener(mImageView, defaultImageResId, errorImageResId));
            }
        }

        final DatePicker earlierDatePicker = dialogView.findViewById(isNumericDatePicker ? R.id.earlier_date_picker_numeric : R.id.earlier_date_picker);

        String color = tags.get(0).getColor();
        Button status = dialogView.findViewById(R.id.status);
        if (status != null) {
            status.setBackgroundColor(StringUtils.isBlank(color) ? Color.WHITE : Color.parseColor(color));
        }

        final Button set = dialogView.findViewById(R.id.set);
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
                        Toast.makeText(getActivity(),
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
                                Toast.makeText(getActivity(),
                                        String.format(getString(R.string.cannot_record_vaccine),
                                                tag.getName()), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                listener.onVaccinateEarlier(tagsToUpdate, view);

            }
        });

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

        vaccinateEarlier.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaccinateEarlier.setVisibility(View.GONE);
                earlierDatePicker.setVisibility(View.VISIBLE);
                vaccinateToday.setVisibility(View.GONE);
                set.setVisibility(View.VISIBLE);

                DatePickerUtils.themeDatePicker(earlierDatePicker, new char[]{'d', 'm', 'y'});
            }
        });

        updateDateRanges(vaccinateToday, vaccinateEarlier, set, earlierDatePicker);

        if (ImmunizationLibrary.getInstance().isAllowExpiredVaccineEntry()) {
            vaccinateToday.setVisibility(AlertStatus.expired.value().equals(tags.get(0).getStatus()) ? View.GONE : View.VISIBLE);//Determine whether to show today for expired
        }

        Button cancel = dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return dialogView;
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

    private void addRadioClickListener(List<RadioButton> radios, RadioButton radio) {
        for (RadioButton otherRadio : radios) {
            otherRadio.setChecked(false);
        }
        radio.setChecked(true);
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

    private String findSelectRadio(LinearLayout vaccinationNameLayout) {
        for (int i = 0; i < vaccinationNameLayout.getChildCount(); i++) {
            View chilView = vaccinationNameLayout.getChildAt(i);
            RadioButton radioChild = chilView.findViewById(R.id.radio);
            if (radioChild.getVisibility() == View.VISIBLE && radioChild.isChecked()) {
                TextView childVaccineView = chilView.findViewById(R.id.vaccine);
                return childVaccineView.getText().toString();
            }
        }
        return null;
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

    private List<String> findSelectedCheckBoxes(LinearLayout vaccinationNameLayout) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < vaccinationNameLayout.getChildCount(); i++) {
            View chilView = vaccinationNameLayout.getChildAt(i);
            CheckBox selectChild = chilView.findViewById(R.id.select);
            if (selectChild.isChecked()) {
                TextView childVaccineView = chilView.findViewById(R.id.vaccine);
                String checkedName = childVaccineView.getTag(R.id.vaccine).toString();
                names.add(checkedName);
            }
        }

        return names;
    }

    private VaccineWrapper searchWrapperByName(String name) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }

        for (VaccineWrapper tag : tags) {
            if (tag.getVaccine() != null) {
                if (VaccinatorUtils.cleanVaccineName(tag.getVaccine().display()).equals(VaccinatorUtils.cleanVaccineName(name))) {
                    return tag;
                }
            } else {
                if (VaccinatorUtils.cleanVaccineName(tag.getName()).equals(VaccinatorUtils.cleanVaccineName(name))) {
                    return tag;
                }
            }
        }
        return null;
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
                    for (String sisterVaccine : sisterVaccines) {
                        minDate = updateMinVaccineDate(minDate, sisterVaccine);
                        maxDate = updateMaxVaccineDate(maxDate, sisterVaccine);
                    }
                }
            }
        }

        Pair<Calendar, Calendar> vaccineMinMaxDatePair = VaccinatorUtils.getVaccineMinimumAndMaximumDate(tags, issuedVaccines);
        if (vaccineMinMaxDatePair.first != null && vaccineMinMaxDatePair.second != null) {
            minDate = vaccineMinMaxDatePair.first;
            maxDate = vaccineMinMaxDatePair.second;
        }

        if (minDate != null && maxDate != null && getActivity() != null) {
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
                vaccinateEarlier
                        .setBackground(getActivity().getResources().getDrawable(R.drawable.vaccination_earlier_bg_disabled));
                Toast.makeText(getActivity(), R.string.problem_applying_vaccine_constraints, Toast.LENGTH_LONG).show();
            }
        }
    }

    private Date getMinVaccineDate(String vaccineName) {
        VaccineSchedule curVaccineSchedule = VaccineSchedule.getVaccineSchedule("child", vaccineName);
        Date minDate = null;
        if (curVaccineSchedule == null) {
            curVaccineSchedule = VaccineSchedule.getVaccineSchedule("woman", vaccineName);
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

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
