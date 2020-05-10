package org.farmtec.res.service;

import org.farmtec.res.service.model.Rule;

import java.util.Date;
import java.util.List;

/**
 * Contains the Set Of Rules to be tested
 * Rule List Is thread safe as there is only 1 Thread
 * that can modify RuleList
 * All other Thread are read only
 */
public class RuleService {

    private volatile List<Rule> RuleList;
    private Date lastUpdate;

    public List<Rule> getRuleList() {
        return RuleList;
    }

    public void setRuleList(List<Rule> ruleList) {
        RuleList = ruleList;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


}
