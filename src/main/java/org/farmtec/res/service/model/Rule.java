package org.farmtec.res.service.model;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.rules.RuleComponent;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This is immutable, as the rule is loaded into th object
 * and will no change. The potential reload would create a new Rule Object
 * and replace the current.
 * <p>
 * Definition of a Rule
 * Its has  the definition of the rule RuleGroupComposite
 * Priority of the Rule
 * Action of the Rule
 */
@Value.Immutable
public abstract class Rule {
    private static final Logger logger
            = LoggerFactory.getLogger(Rule.class);

    @Value.Parameter
    public abstract String getName();

    @Value.Parameter
    public abstract RuleComponent getRuleGroupComposite();

    @Value.Parameter
    public abstract int getPriority();

    @Value.Parameter
    public abstract List<Action> getActions();

    public boolean test(JsonNode jsonNode) {
        boolean match = false;
        match = this.getRuleGroupComposite().testRule(jsonNode);
        if (logger.isDebugEnabled()) {
            logger.debug("Rule Name: [{}] match: [{}]", this.getName(), match);
        }
        return match;
    }
}
