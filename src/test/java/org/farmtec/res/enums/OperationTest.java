package org.farmtec.res.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OperationTest {

    @Test
    void fromString_equals() {
        //given
        String eq = "EQ";
        //when
        Operation operation=Operation.fromString(eq);
        //then
        assertThat(operation).isEqualTo(Operation.EQ);
    }

    @Test
    void fromString_not_equals() {
        //given
        String eq = "NEQ";
        //when
        Operation operation=Operation.fromString(eq);
        //then
        assertThat(operation).isEqualTo(Operation.NEQ);
    }
    @Test
    void fromString_lessThan() {
        //given
        String eq = "LT";
        //when
        Operation operation=Operation.fromString(eq);
        //then
        assertThat(operation).isEqualTo(Operation.LT);
    }
    @Test
    void fromString_greaterThan() {
        //given
        String eq = "GT";
        //when
        Operation operation=Operation.fromString(eq);
        //then
        assertThat(operation).isEqualTo(Operation.GT);
    }
    void fromString_lessOrEqualsThan() {
        //given
        String eq = "LTE";
        //when
        Operation operation=Operation.fromString(eq);
        //then
        assertThat(operation).isEqualTo(Operation.LTE);
    }
    @Test
    void fromString_greaterOrEqualsThan() {
        //given
        String eq = "GTE";
        //when
        Operation operation=Operation.fromString(eq);
        //then
        assertThat(operation).isEqualTo(Operation.GTE);
    }

    void fromString_Contains() {
        //given
        String eq="CONTAINS";
        //when
        Operation operation=Operation.fromString(eq);
        //then
        assertThat(operation).isEqualTo(Operation.CONTAINS);
    }
    @Test
    void fromString_NotContains() {
        //given
        String eq="NOT CONTAINS";
        //when
        Operation operation=Operation.fromString(eq);
        //then
        assertThat(operation).isEqualTo(Operation.NOT_CONTAINS);
    }
    @Test
    void fromString_Unkown_shouldBeNull() {
        //given
        String eq="?.";
        //when
        Operation operation=Operation.fromString(eq);
        //then
        assertThat(operation).isNull();
    }
}