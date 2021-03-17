package org.farmtec.res.enums;

import java.time.LocalTime;

/**
 * Created by dp on 24/01/2021
 */
public enum SupportedTypes {
    INTEGER("integer"), STRING("string");

    private String type;

    SupportedTypes(String type) {
        this.type = type;
    }

    public static Class<?> getSupportedTypeFrom(String type) {

        Class<?> t;
        switch (type.toLowerCase()) {
            case "string":
                t = String.class;
                break;
            case "int":
                t = Integer.class;
                break;
            case "integer":
                t = Integer.class;
                break;
            case "long":
                t = Long.class;
                break;
            case "time":
                t = LocalTime.class;
                break;

            default:
                throw new IllegalArgumentException("Type "+ type.toLowerCase() +" not supported");
        }
        return t;
    }

}
