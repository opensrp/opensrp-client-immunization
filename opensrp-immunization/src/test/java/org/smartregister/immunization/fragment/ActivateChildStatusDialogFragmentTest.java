package org.smartregister.immunization.fragment;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.test.core.app.ApplicationProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.R;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 10/04/2018.
 */
public class ActivateChildStatusDialogFragmentTest extends BaseUnitTest {

    @Mock
    private LayoutInflater layoutInflater;

    @Mock
    private ViewGroup viewGroup;

    @Mock
    private Bundle bundle;

    @Mock
    private FragmentActivity activity;

    @Mock
    private View view;

    @Mock
    private TextView textView;

    @Mock
    private Button button;


    @Test
    public void newInstanceShouldCreateNonNullInstance() {
        ActivateChildStatusDialogFragment activateChildStatusDialogFragment = ActivateChildStatusDialogFragment
                .newInstance("her", "inactive", 0);

        Assert.assertNotNull(activateChildStatusDialogFragment);
    }

    @Test
    public void testSetFilterTouchesWhenObscuredSetsFlagToTrue() {

        ActivateChildStatusDialogFragment activateChildStatusDialogFragment = Mockito.spy(ActivateChildStatusDialogFragment
                .newInstance("her", "inactive", 0));

        Mockito.doReturn(activity).when(activateChildStatusDialogFragment).getActivity();

        Mockito.doReturn(ApplicationProvider.getApplicationContext().getResources().getString(R.string.activate_child_status_dialog_title)).when(activity).getString(R.string.activate_child_status_dialog_title);

        Mockito.doReturn(layoutInflater).when(activity).getLayoutInflater();
        Mockito.doReturn(view).when(layoutInflater).inflate(R.layout.dialog_fragment_activate_child_status, viewGroup, false);
        Mockito.doReturn(button).when(view).findViewById(android.R.id.button1);
        Mockito.doReturn(button).when(view).findViewById(android.R.id.button2);
        Mockito.doReturn(textView).when(view).findViewById(R.id.tv_dialog_activate_child_title);


        activateChildStatusDialogFragment.onCreateView(layoutInflater, viewGroup, bundle);

        Mockito.verify(view).setFilterTouchesWhenObscured(true);
    }

}