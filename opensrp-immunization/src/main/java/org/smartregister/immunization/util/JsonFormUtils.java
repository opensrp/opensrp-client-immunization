package org.smartregister.immunization.util;

import android.content.Context;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.repository.EventClientRepository;

import java.util.Date;

/**
 * Created by keyman on 31/07/2017.
 */
public class JsonFormUtils extends org.smartregister.util.JsonFormUtils {

    public static void createVaccineEvent(Context context, Vaccine vaccine, String eventType, String entityType,
                                          JSONArray fields) {
        try {
            EventClientRepository db = ImmunizationLibrary.getInstance().eventClientRepository();

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

            event.setClientApplicationVersion(ImmunizationLibrary.getInstance().getApplicationVersion());
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
            Log.e(TAG, e.toString(), e);
        }
    }

    public static void createServiceEvent(Context context, ServiceRecord serviceRecord, String eventType, String entityType,
                                          JSONArray fields) {
        try {
            EventClientRepository db = ImmunizationLibrary.getInstance().eventClientRepository();

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

            event.setClientApplicationVersion(ImmunizationLibrary.getInstance().getApplicationVersion());
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
            Log.e(TAG, e.toString(), e);
        }
    }

}


