package org.smartregister.immunization.util;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.Observation;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.repository.EventClientRepository;

import java.util.Date;

import timber.log.Timber;

/**
 * Created by keyman on 31/07/2017.
 */
public class JsonFormUtils extends org.smartregister.util.JsonFormUtils {

    /**
     * This createVaccineEvent method is deprecated, use {@link #createVaccineEvent(Vaccine vaccine, String eventType, String entityType, JSONArray fields, org.smartregister.Context context)} instead which adds application version name.
     */
    @Deprecated
    public static void createVaccineEvent(Context context, Vaccine vaccine, String eventType, String entityType, JSONArray fields) {

        org.smartregister.Context openSRPContext = ImmunizationLibrary.getInstance().context();
        createVaccineEvent(vaccine, eventType, entityType, fields, openSRPContext);
    }

    public static void createVaccineEvent(Vaccine vaccine, String eventType, String entityType, JSONArray fields, org.smartregister.Context context) {
        try {
            EventClientRepository db = context.getEventClientRepository();

            Event event = (Event) new Event()
                    .withBaseEntityId(vaccine.getBaseEntityId())
                    .withIdentifiers(vaccine.getIdentifiers())
                    .withEventDate(vaccine.getDate())
                    .withEventType(eventType)
                    .withLocationId(vaccine.getLocationId())
                    .withProviderId(vaccine.getAnmId())
                    .withEntityType(entityType)
                    .withFormSubmissionId(vaccine.getFormSubmissionId() == null ? generateRandomUUIDString() : vaccine
                            .getFormSubmissionId())
                    .withDateCreated(new Date());

            event.setTeam(vaccine.getTeam());
            event.setTeamId(vaccine.getTeamId());
            event.setChildLocationId(vaccine.getChildLocationId());
            event.addDetails(IMConstants.VaccineEvent.PROGRAM_CLIENT_ID, vaccine.getProgramClientId());
            event.addDetails(AllConstants.DATA_STRATEGY, context.allSharedPreferences().fetchCurrentDataStrategy());

            try {
                addFormSubmissionFieldObservation(AllConstants.DATA_STRATEGY, context.allSharedPreferences().fetchCurrentDataStrategy(), Observation.TYPE.TEXT, event);
            } catch (JSONException jsonException) {
                Timber.e(jsonException);
            }

            event.setClientApplicationVersion(ImmunizationLibrary.getInstance().getApplicationVersion());
            event.setClientApplicationVersionName(ImmunizationLibrary.getInstance().getApplicationVersionName());
            event.setClientDatabaseVersion(ImmunizationLibrary.getInstance().getDatabaseVersion());

            if (fields != null && fields.length() != 0)
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject jsonObject = getJSONObject(fields, i);
                    String value = getString(jsonObject, VALUE);
                    if (StringUtils.isNotBlank(value)) {
                        addObservation(event, jsonObject);
                    }
                }


            if (event != null) {

                JSONObject eventJson = new JSONObject(JsonFormUtils.gson.toJson(event));

                //check if an event already exists and update instead
                if (vaccine.getEventId() != null) {
                    JSONObject existingEvent = db.getEventsByEventId(vaccine.getEventId());
                    eventJson = merge(existingEvent, eventJson);
                }

                //merge if event exists
                db.addEvent(event.getBaseEntityId(), eventJson);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /**
     * This createServiceEvent method is deprecated, use {@link #createServiceEvent(ServiceRecord serviceRecord, String eventType, String entityType, JSONArray fields, org.smartregister.Context context)} instead which adds application version name.
     */
    @Deprecated
    public static void createServiceEvent(Context context, ServiceRecord serviceRecord, String eventType, String entityType, JSONArray fields) {

        org.smartregister.Context openSRPContext = ImmunizationLibrary.getInstance().context();
        createServiceEvent(serviceRecord, eventType, entityType, fields, openSRPContext);
    }

    public static void createServiceEvent(ServiceRecord serviceRecord, String eventType, String entityType, JSONArray fields, org.smartregister.Context context) {
        try {
            EventClientRepository db = context.getEventClientRepository();

            Event event = (Event) new Event()
                    .withBaseEntityId(serviceRecord.getBaseEntityId())
                    .withIdentifiers(serviceRecord.getIdentifiers())
                    .withEventDate(serviceRecord.getDate())
                    .withEventType(eventType)
                    .withLocationId(serviceRecord.getLocationId())
                    .withProviderId(serviceRecord.getAnmId())
                    .withEntityType(entityType)
                    .withFormSubmissionId(
                            serviceRecord.getFormSubmissionId() == null ? generateRandomUUIDString() : serviceRecord
                                    .getFormSubmissionId())
                    .withDateCreated(new Date());

            event.setTeam(serviceRecord.getTeam());
            event.setTeamId(serviceRecord.getTeamId());
            event.setChildLocationId(serviceRecord.getChildLocationId());
            event.addDetails(IMConstants.VaccineEvent.PROGRAM_CLIENT_ID, serviceRecord.getProgramClientId());
            event.addDetails(AllConstants.DATA_STRATEGY, context.allSharedPreferences().fetchCurrentDataStrategy());

            try {
                addFormSubmissionFieldObservation(AllConstants.DATA_STRATEGY, context.allSharedPreferences().fetchCurrentDataStrategy(), Observation.TYPE.TEXT, event);
            } catch (JSONException jsonException) {
                Timber.e(jsonException);
            }

            event.setClientApplicationVersion(ImmunizationLibrary.getInstance().getApplicationVersion());
            event.setClientApplicationVersionName(ImmunizationLibrary.getInstance().getApplicationVersionName());
            event.setClientDatabaseVersion(ImmunizationLibrary.getInstance().getDatabaseVersion());

            if (fields != null && fields.length() != 0)
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject jsonObject = getJSONObject(fields, i);
                    String value = getString(jsonObject, VALUE);
                    if (StringUtils.isNotBlank(value)) {
                        addObservation(event, jsonObject);
                    }
                }

            if (event != null) {

                JSONObject eventJson = new JSONObject(JsonFormUtils.gson.toJson(event));

                //check if an event already exists and update instead
                if (serviceRecord.getEventId() != null) {
                    JSONObject existingEvent = db.getEventsByEventId(serviceRecord.getEventId());
                    eventJson = merge(existingEvent, eventJson);
                }

                //merge if event exists
                db.addEvent(event.getBaseEntityId(), eventJson);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

}


