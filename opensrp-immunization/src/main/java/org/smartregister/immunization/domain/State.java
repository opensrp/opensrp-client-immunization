package org.smartregister.immunization.domain;

/**
 * Created by keyman on 4/4/2018.
 */

public enum State {
    DONE_CAN_BE_UNDONE,
    DONE_CAN_NOT_BE_UNDONE,
    DUE,
    NOT_DUE,
    OVERDUE,
    EXPIRED
}
