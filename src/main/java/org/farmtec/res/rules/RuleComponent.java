package org.farmtec.res.rules;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.enums.Operation;

public interface RuleComponent {

    public  boolean testRule(JsonNode jsonNode);

}
