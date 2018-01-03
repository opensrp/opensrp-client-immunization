package org.smartregister.immunization.fragment.mock;

import android.app.Service;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;

import org.smartregister.util.OpenSRPImageLoader;

import java.util.ArrayList;

/**
 * Created by kaderchowdhury on 12/12/17.
 */

public class OpenSRPImageLoaderMock extends OpenSRPImageLoader {
    public OpenSRPImageLoaderMock(FragmentActivity activity) {
        super(activity);
    }

    public OpenSRPImageLoaderMock(Service service, int defaultPlaceHolderResId) {
        super(service, defaultPlaceHolderResId);
    }

    public OpenSRPImageLoaderMock(Context context, int defaultPlaceHolderResId) {
        super(context, defaultPlaceHolderResId);
    }

    public OpenSRPImageLoaderMock(FragmentActivity activity, int defaultPlaceHolderResId) {
        super(activity, defaultPlaceHolderResId);
    }

    public OpenSRPImageLoaderMock(FragmentActivity activity, ArrayList<Drawable> placeHolderDrawables) {
        super(activity, placeHolderDrawables);
    }

}
