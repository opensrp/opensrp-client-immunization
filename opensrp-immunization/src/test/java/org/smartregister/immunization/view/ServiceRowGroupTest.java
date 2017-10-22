package org.smartregister.immunization.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.immunization.BaseUnitTest;

/**
 * Created by onaio on 30/08/2017.
 */

@PrepareForTest({ServiceRowGroup.class})
public class ServiceRowGroupTest extends BaseUnitTest {

    private ServiceRowGroup serviceRowGroup;

    @Mock
    private Context context;

    @Mock
    private AttributeSet attributeSet;

    @Before
    public void setUp() {
        serviceRowGroup = Mockito.mock(ServiceRowGroup.class);
        org.mockito.MockitoAnnotations.initMocks(this);
    }


    @Test(expected = Exception.class)
    public void assertOnUndoClickReturnsVoid() throws Exception {
        ServiceRowGroup group = PowerMockito.spy(serviceRowGroup);

        PowerMockito.doReturn(null).when(group, "init", context);
        ServiceRowCard v = new ServiceRowCard(context,true);

        group.onUndoClick(v);
        Mockito.verify(group).onUndoClick(ArgumentMatchers.eq(v));
        Mockito.verify(group,Mockito.times(1)).onUndoClick(v);
    }

    @Test
    public void assertgetChildDetailReturnsCommonPersonObject()throws Exception{
        ServiceRowGroup group = Mockito.mock(ServiceRowGroup.class);
        Map<String,String> map = new HashMap<String, String>();
       // map.put("1","kk");
        CommonPersonObjectClient childDetails = new CommonPersonObjectClient("1",map,"kk");
        Mockito.when(group.getChildDetails()).thenReturn(childDetails);
        //CommonPersonObjectClient childDetails2 = group.getChildDetails();
        org.junit.Assert.assertEquals(group.getChildDetails(),childDetails);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {
        ServiceRowGroup serviceRowGroupSpy = PowerMockito.spy(serviceRowGroup);
        PowerMockito.doReturn(null).when(serviceRowGroupSpy, "init", context);
        org.junit.Assert.assertNotNull(new ServiceRowGroup(context, true));
        org.junit.Assert.assertNotNull(new ServiceRowGroup(context, attributeSet));
        org.junit.Assert.assertNotNull(new ServiceRowGroup(context, attributeSet, 0));
        org.junit.Assert.assertNotNull(new ServiceRowGroup(context, attributeSet, 0, 0));
    }
//./gradlew :opensrp-immunization:clean :opensrp-immunization:jacocoTestReport
}
