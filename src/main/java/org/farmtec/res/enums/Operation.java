package org.farmtec.res.enums;

public enum Operation {
    EQ("=="),
    NEQ("<>"),
    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),
    CONTAINS("CONTAINS"),
    NOT_CONTAINS("NOT CONTAINS");

    private String op;

    Operation(String op) {
        this.op = op;
    }

    public static Operation fromString(String str) {
        for(Operation op:Operation.values()) {
            if(op.op.equalsIgnoreCase(str)) {
                return op;
            }
        }
        return null;

    }

}
