package org.farmtec.res.rules.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;
import org.farmtec.res.rules.RuleComponent;
import org.farmtec.res.service.exceptions.InvalidOperation;
import org.immutables.value.Value;

import java.util.function.Predicate;

@Value.Immutable
public abstract class StringRuleLeaf implements RuleComponent {

    @Value.Parameter
    public abstract String getTag();

    @Value.Parameter
    public abstract Operation getOperation();

    @Value.Parameter
    public abstract String getValue();

    @Value.Parameter
    public abstract Class<?> getType();

    @Value.Parameter
    public abstract Predicate<String> getPredicate();

    @Override
    public boolean testRule(JsonNode jsonNode) {

        boolean bool = false;
        if (jsonNode.has(this.getTag())) {
            bool = this.getPredicate().test(jsonNode.get(this.getTag()).asText());
        }
        return bool;
    }
}

