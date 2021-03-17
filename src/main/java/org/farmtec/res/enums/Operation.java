package org.farmtec.res.enums;

/**
 * Operation between {@code org.farmtec.res.rules.RuleComponent}
 */
public enum Operation {
    EQ("EQ"),
    NEQ("NEQ"),
    GT("GT"),
    LT("LT"),
    GTE("GTE"),
    LTE("LTE"),
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
        throw new IllegalArgumentException("Invalid Operation");
    }
}
