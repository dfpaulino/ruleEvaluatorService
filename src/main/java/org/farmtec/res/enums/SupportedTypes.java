package org.farmtec.res.enums;

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

            default:
                throw new RuntimeException("Type not supported");
        }
        return t;
    }

}
