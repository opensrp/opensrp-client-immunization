package org.smartregister.immunization.repository;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.repository.Repository;
import org.smartregister.util.AppProperties;

import java.util.Calendar;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 27-02-2020.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = {27})
public class RecurringServiceRecordRepositoryRobolectricTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private RecurringServiceRecordRepository recurringServiceRecordRepository;

    @Mock
    private SQLiteDatabase database;

    @Before
    public void setUp() throws Exception {
        Context context = Mockito.mock(Context.class);
        Mockito.doReturn(new AppProperties()).when(context).getAppProperties();
        ImmunizationLibrary.init(context, Mockito.mock(Repository.class), Mockito.mock(CommonFtsObject.class), 1, 1);
        recurringServiceRecordRepository = Mockito.spy(new RecurringServiceRecordRepository());
        Mockito.doReturn(database).when(recurringServiceRecordRepository).getReadableDatabase();
    }

    @After
    public void tearDown() throws Exception {
        ReflectionHelpers.setStaticField(ImmunizationLibrary.class, "instance", null);
    }

    @Test
    public void findUnSyncedBeforeTimeShouldCallCalendarMinusMinutes() {
        long timeNow = Calendar.getInstance().getTime().getTime();
        ArgumentCaptor<String[]> selectionArgsCaptor = ArgumentCaptor.forClass(String[].class);

        Mockito.doReturn(null).when(database).query(Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(), selectionArgsCaptor.capture(), Mockito.nullable(String.class), Mockito.nullable(String.class), Mockito.nullable(String.class), Mockito.nullable(String.class));

        recurringServiceRecordRepository.findUnSyncedBeforeTime(6);

        String[] selectionArgs = selectionArgsCaptor.getValue();
        long actualTime = Long.parseLong(selectionArgs[0]);

        Assert.assertEquals(timeNow - (6 * 60 * 1000), actualTime, 1000L);
    }
}
