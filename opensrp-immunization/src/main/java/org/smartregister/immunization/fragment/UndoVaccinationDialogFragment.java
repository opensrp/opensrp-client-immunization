package org.smartregister.immunization.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import org.smartregister.immunization.R;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.listener.VaccinationActionListener;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.immunization.util.Utils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.util.OpenSRPImageLoader;
import org.smartregister.view.activity.DrishtiApplication;

import java.io.Serializable;

@SuppressLint("ValidFragment")
public class UndoVaccinationDialogFragment extends DialogFragment {
    public static final String DIALOG_TAG = "UndoVaccinationDialogFragment";
    public static final String WRAPPER_TAG = "tag";
    private VaccineWrapper tag;
    private VaccinationActionListener listener;
    private DialogInterface.OnDismissListener onDismissListener;
    private Integer defaultImageResourceID;
    private Integer defaultErrorImageResourceID;

    public static UndoVaccinationDialogFragment newInstance(
            VaccineWrapper tag) {

        UndoVaccinationDialogFragment undoVaccinationDialogFragment = new UndoVaccinationDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(WRAPPER_TAG, tag);
        undoVaccinationDialogFragment.setArguments(args);

        return undoVaccinationDialogFragment;
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
        Serializable serializable = bundle.getSerializable(WRAPPER_TAG);
        if (serializable != null && serializable instanceof VaccineWrapper) {
            tag = (VaccineWrapper) serializable;
        }

        if (tag == null) {
            return null;
        }

        ViewGroup dialogView = (ViewGroup) inflater.inflate(R.layout.undo_vaccination_dialog_view, container, false);
        dialogView.setFilterTouchesWhenObscured(true);
        TextView nameView = dialogView.findViewById(R.id.name);
        nameView.setText(tag.getPatientName());
        TextView numberView = dialogView.findViewById(R.id.number);
        numberView.setText(tag.getPatientNumber());

        TextView vaccineView = dialogView.findViewById(R.id.vaccine);
        VaccineRepo.Vaccine vaccine = tag.getVaccine();
        if (vaccine != null) {
            vaccineView.setText(VaccinatorUtils.getTranslatedVaccineName(getActivity(), tag.getVaccine().display()));
        } else {

            String name = VaccinatorUtils.getTranslatedVaccineName(getActivity(), tag.getName());
            vaccineView.setText(name);
        }


        if (tag.getId() != null) {
            ImageView mImageView = dialogView.findViewById(R.id.child_profilepic);
            if (tag.getId() != null) {//image already in local storage most likey ):
                //set profile image by passing the client id.If the image doesn't exist in the image repository then download and save locally
                mImageView.setTag(R.id.entity_id, tag.getId());

                int defautltImageResId = getDefaultImageResourceID() == null ? ImageUtils
                        .profileImageResourceByGender(tag.getGender()) : getDefaultImageResourceID();
                int errorImageResId = getDefaultErrorImageResourceID() == null ? ImageUtils
                        .profileImageResourceByGender(tag.getGender()) : getDefaultErrorImageResourceID();
                DrishtiApplication.getCachedImageLoaderInstance().getImageByClientId(tag.getId(),
                        OpenSRPImageLoader.getStaticImageListener(mImageView, defautltImageResId, errorImageResId));
            }
        }

        Button vaccinateToday = dialogView.findViewById(R.id.yes_undo);
        vaccinateToday.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

                tag.setName(tag.getDefaultName());
                listener.onUndoVaccination(tag, view);
            }
        });

        Button cancel = dialogView.findViewById(R.id.no_go_back);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return dialogView;
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

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
