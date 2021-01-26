package org.farmtec.res.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum LogicalOperation {
    AND {
        @Override
        public boolean test (boolean a,boolean b) {
            return a&&b;
        }
    },
    OR {
        @Override
        public boolean test (boolean a, boolean b) {
            return a||b;
        }
    },
    NOT {
        @Override
        public boolean test(boolean a, boolean b) {
            return !a;
        }
    };

    private static final Logger logger
            = LoggerFactory.getLogger(LogicalOperation.class);

    public abstract boolean test (boolean a,boolean b);

    public static LogicalOperation getLogicalOperation(String op) {
        LogicalOperation logicalOperation = null;
        switch (op.toUpperCase()) {
            case "AND":
                logicalOperation = LogicalOperation.AND;
                break;
            case "OR":
                logicalOperation = LogicalOperation.OR;
                break;
            case "NOT":
                logicalOperation = LogicalOperation.NOT;
                break;
        }
        if (logicalOperation == null) {
            logger.error("Operation [{}] not supported", op);
            throw new RuntimeException("Operation Not Supported");
        }
        return logicalOperation;
    }
}
