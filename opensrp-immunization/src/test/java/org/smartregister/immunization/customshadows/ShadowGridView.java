package org.smartregister.immunization.customshadows;

import android.widget.GridView;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowAdapterView;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(GridView.class)
public class ShadowGridView extends ShadowAdapterView {
    @RealObject
    private GridView realGridView;
}
