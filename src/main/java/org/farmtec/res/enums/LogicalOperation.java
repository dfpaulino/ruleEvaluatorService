package org.farmtec.res.enums;

public enum LogicalOperation {
    AND {
        @Override
        public boolean test (boolean a,boolean b) {
            return a&&b;
        }
    },
    OR {
        @Override
        public boolean test (boolean a,boolean b) {
            return a||b;
        }
    };

    public abstract boolean test (boolean a,boolean b);
}
