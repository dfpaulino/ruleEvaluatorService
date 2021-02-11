package org.farmtec.res.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.service.model.Rule;

import java.util.Date;
import java.util.List;

/**
 * Created by dp on 11/02/2021
 */
public interface RuleService {
    List<Rule> getRuleList();

    boolean updateRules() throws Exception;

    Date getLastUpdate();

    List<Rule> test(JsonNode jsonNode) throws Exception;

}
