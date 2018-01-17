package com.sac.campusborrow.model;

/**
 * Created by ionut on 1/17/2018.
 */

public enum Status {
    DISPONIBIL("DISPONIBIL"),
    INCHIRIAT ("INCHIRIAT");

    private final String status;

    Status(String s) {
        status = s;
    }
}
