package org.smartregister.immunization.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.vijay.jsonwizard.utils.NativeFormsProperties;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.smartregister.immunization.R;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceSchedule;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.listener.ServiceActionListener;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.immunization.util.Utils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.util.DatePickerUtils;
import org.smartregister.util.OpenSRPImageLoader;
import org.smartregister.view.activity.DrishtiApplication;

import java.util.Calendar;
import java.util.List;

@SuppressLint("ValidFragment")
public class ServiceEditDialogFragment extends DialogFragment {
    public static final String DIALOG_TAG = "ServiceEditDialogFragment";
    private final ServiceWrapper tag;
    private final View viewGroup;
    private ServiceActionListener listener;
    private List<ServiceRecord> issuedServices;
    private DateTime dateOfBirth;
    private boolean disableConstraints;
    private DateTime dcToday;
    private boolean isNumericDatePicker = Utils.isPropertyTrue(NativeFormsProperties.KEY.WIDGET_DATEPICKER_IS_NUMERIC);

    private ServiceEditDialogFragment(List<ServiceRecord> issuedServices, ServiceWrapper tag, View viewGroup) {
        this.issuedServices = issuedServices;
        this.tag = tag;
        this.viewGroup = viewGroup;
        disableConstraints = false;
    }

    private ServiceEditDialogFragment(DateTime dateOfBirth, List<ServiceRecord> issuedServices, ServiceWrapper tag,
                                      View viewGroup, boolean disableConstraints) {
        this.issuedServices = issuedServices;
        this.tag = tag;
        this.viewGroup = viewGroup;

        this.dateOfBirth = dateOfBirth;
        this.disableConstraints = disableConstraints;
        if (disableConstraints) {
            dcToday = ServiceSchedule.standardiseDateTime(DateTime.now());
        }
    }

    public static ServiceEditDialogFragment newInstance(
            List<ServiceRecord> issuedServices,
            ServiceWrapper tag, View viewGroup) {
        return new ServiceEditDialogFragment(issuedServices, tag, viewGroup);
    }

    public static ServiceEditDialogFragment newInstance(
            DateTime dateOfBirth,
            List<ServiceRecord> issuedServices,
            ServiceWrapper tag, View viewGroup, boolean disableConstraints) {
        return new ServiceEditDialogFragment(dateOfBirth, issuedServices, tag, viewGroup, disableConstraints);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
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
            listener = (ServiceActionListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ServiceActionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (tag == null) {
            return null;
        }

        ViewGroup dialogView = (ViewGroup) inflater.inflate(R.layout.vaccination_edit_dialog_view, container, false);
        dialogView.setFilterTouchesWhenObscured(true);
        TextView nameView = dialogView.findViewById(R.id.name);
        nameView.setText(tag.getPatientName());
        TextView numberView = dialogView.findViewById(R.id.number);
        numberView.setText(tag.getPatientNumber());
        TextView service_date = dialogView.findViewById(R.id.service_date);
        service_date.setText("Service date: " + tag.getUpdatedVaccineDateAsString() + "");
        LinearLayout vaccinationNameLayout = dialogView.findViewById(R.id.vaccination_name_layout);

        View vaccinationName = inflater.inflate(R.layout.vaccination_name_edit_dialog, null);
        TextView vaccineView = vaccinationName.findViewById(R.id.vaccine);


        String name = VaccinatorUtils.getTranslatedVaccineName(getActivity(), tag.getName());
        vaccineView.setText(name);
        vaccinationNameLayout.addView(vaccinationName);


        if (tag.getId() != null) {
            ImageView mImageView = dialogView.findViewById(R.id.child_profilepic);
            if (tag.getId() != null) {//image already in local storage most likey ):
                //set profile image by passing the client id.If the image doesn't exist in the image repository then download and save locally
                mImageView.setTag(R.id.entity_id, tag.getId());
                DrishtiApplication.getCachedImageLoaderInstance().getImageByClientId(tag.getId(), OpenSRPImageLoader
                        .getStaticImageListener(mImageView, ImageUtils.profileImageResourceByGender(tag.getGender()),
                                ImageUtils.profileImageResourceByGender(tag.getGender())));
            }
        }

        final DatePicker earlierDatePicker = dialogView.findViewById(isNumericDatePicker ? R.id.earlier_date_picker_numeric : R.id.earlier_date_picker);

        String color = tag.getColor();
        Button status = dialogView.findViewById(R.id.status);
        if (status != null) {
            status.setBackgroundColor(StringUtils.isBlank(color) ? Color.WHITE : Color.parseColor(color));
        }

        final Button set = dialogView.findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       dismiss();

                                       int day = earlierDatePicker.getDayOfMonth();
                                       int month = earlierDatePicker.getMonth();
                                       int year = earlierDatePicker.getYear();

                                       Calendar calendar = Calendar.getInstance();
                                       calendar.set(Calendar.YEAR, year);
                                       calendar.set(Calendar.MONTH, month);
                                       calendar.set(Calendar.DAY_OF_MONTH, day);
                                       //
                                       DateTime dateTime = new DateTime(calendar.getTime());
                                       tag.setUpdatedVaccineDate(dateTime, true);


                                       listener.onGiveEarlier(tag, viewGroup);

                                   }
                               }


        );

        final Button vaccinateToday = dialogView.findViewById(R.id.vaccinate_today);
        vaccinateToday.setText(vaccinateToday.getText().toString().replace("vaccination", "service"));


        vaccinateToday.setOnClickListener(new Button.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  vaccinateToday.setVisibility(View.GONE);
                                                  earlierDatePicker.setVisibility(View.VISIBLE);
                                                  set.setVisibility(View.VISIBLE);

                                                  DatePickerUtils.themeDatePicker(earlierDatePicker, new char[]{'d', 'm', 'y'});


                                              }
                                          }

        );

        Button vaccinateEarlier = dialogView.findViewById(R.id.vaccinate_earlier);
        vaccinateEarlier.setText(vaccinateEarlier.getText().toString().replace("vaccination", "service"));
        vaccinateEarlier.setOnClickListener(new Button.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dismiss();

                                                    listener.onUndoService(tag, viewGroup);

                                                }
                                            }

        );

        updateDateRanges(earlierDatePicker, set);

        Button cancel = dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        for (int i = 0; i < vaccinationNameLayout.getChildCount(); i++) {
            View chilView = vaccinationNameLayout.getChildAt(i);
            chilView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox childSelect = view.findViewById(R.id.select);
                    if (childSelect != null) {
                        childSelect.toggle();
                    }
                }
            });
        }


        return dialogView;
    }

    /**
     * This method updates the allowed date ranges in the views
     *
     * @param datePicker Date picker for selecting a previous date for a vaccine
     */
    private void updateDateRanges(DatePicker datePicker, Button set) {
        if (tag == null || tag.getDob() == null || tag.getServiceType() == null || issuedServices == null) {
            return;
        }

        DateTime minDate = null;
        DateTime maxDate = null;

        if (disableConstraints) {
            minDate = ServiceSchedule.standardiseDateTime(dateOfBirth);
            maxDate = ServiceSchedule.standardiseDateTime(dcToday);
        } else {
            minDate = ServiceSchedule.standardiseDateTime(updateMinVaccineDate(minDate));
            maxDate = ServiceSchedule.standardiseDateTime(updateMaxVaccineDate(maxDate));
        }

        if (maxDate.getMillis() >= minDate.getMillis()) {
            set.setVisibility(View.GONE);
            datePicker.setMinDate(minDate.getMillis());
            datePicker.setMaxDate(maxDate.getMillis());
        } else {
            set.setVisibility(View.GONE);
            Toast.makeText(getActivity(), R.string.problem_applying_vaccine_constraints, Toast.LENGTH_LONG).show();
        }
    }

    private DateTime updateMinVaccineDate(DateTime minDate_) {
        DateTime dueDate = getMinVaccineDate();
        DateTime minDate = minDate_;
        if (dueDate == null
                || dueDate.getMillis() < tag.getDob().getMillis()) {
            dueDate = tag.getDob();
        }

        if (minDate == null) {
            minDate = dueDate;
        } else if (dueDate.getMillis() > minDate.getMillis()) {
            minDate = dueDate;
        }

        return minDate;
    }

    private DateTime updateMaxVaccineDate(DateTime maxDate_) {
        DateTime expiryDate = getMaxVaccineDate();
        DateTime maxDate = maxDate_;
        if (expiryDate == null
                || expiryDate.getMillis() > DateTime.now().getMillis()) {
            expiryDate = DateTime.now();
        }

        if (maxDate == null) {
            maxDate = expiryDate;
        } else if (expiryDate.getMillis() < maxDate.getMillis()) {
            maxDate = expiryDate;
        }

        return maxDate;
    }

    private DateTime getMinVaccineDate() {
        return VaccinatorUtils.getServiceDueDate(tag.getServiceType(), tag.getDob(), issuedServices);
    }

    private DateTime getMaxVaccineDate() {
        return VaccinatorUtils.getServiceExpiryDate(tag.getServiceType(), tag.getDob());
    }

}
