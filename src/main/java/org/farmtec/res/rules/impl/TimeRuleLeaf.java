package org.farmtec.res.rules.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.enums.Operation;
import org.farmtec.res.rules.RuleComponent;
import org.immutables.value.Value;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;

@Value.Immutable
public abstract class TimeRuleLeaf implements RuleComponent {

    @Value.Parameter
    public abstract String getTag();

    @Value.Parameter
    public abstract Operation getOperation();

    @Value.Parameter
    public abstract String getValue();

    @Value.Parameter
    public abstract Class<?> getType();

    @Value.Parameter
    public abstract Predicate<LocalTime> getPredicate();

    @Override
    public boolean testRule(JsonNode jsonNode) {

        boolean bool = false;
        if (jsonNode.has(this.getTag())) {
            try {
                bool = this.getPredicate().test(LocalTime.parse(jsonNode.get(this.getTag()).asText()));
            } catch (DateTimeParseException dte) {
                bool = false;
                dte.printStackTrace();
            }
        }
        return bool;
    }
}

