package org.farmtec.res.enums;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LogicalOperationTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    public void logicalOperation_And() {
        assertThat(LogicalOperation.AND.test(true,true)).isTrue();
        assertThat(LogicalOperation.AND.test(true,false)).isFalse();
        assertThat(LogicalOperation.AND.test(false,true)).isFalse();
        assertThat(LogicalOperation.AND.test(false,false)).isFalse();
    }

    @Test
    public void logicalOperation_Or() {
        assertThat(LogicalOperation.OR.test(true,true)).isTrue();
        assertThat(LogicalOperation.OR.test(true,false)).isTrue();
        assertThat(LogicalOperation.OR.test(false,true)).isTrue();
        assertThat(LogicalOperation.OR.test(false,false)).isFalse();
    }

}