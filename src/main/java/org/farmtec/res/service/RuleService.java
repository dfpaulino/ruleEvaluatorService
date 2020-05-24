package org.farmtec.res.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.service.model.Rule;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * Contains the Set Of Rules to be tested
 * Rule List Is thread safe as long there is only 1 Thread
 * that can modify RuleList
 * All other Thread are read only
 */
public class RuleService {

    // Volatile, R/W are done directly in the RAM. this wont loaded in to the L1/L2 cpu cache


    // The ruleList is a reference to a List, so if the Writer modifies the reference to a new Loaded list
    // the address will be seen to all Reader threads
    //
    //This wont have effect on the members (members are not volatile!), only on the variable that holds the address of the List
    // trade off for not using lock's. will this be faster?
    private volatile List<Rule> ruleList;
    private Date lastUpdate;
    private ForkJoinPool forkJoinPool;

    public RuleService() {

    }

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Rule> ruleList) {
        this.ruleList = Collections.unmodifiableList(ruleList);
        setLastUpdate(new Date());
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    private void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<Rule> test(JsonNode jsonNode) throws Exception {
        return this.ruleList.stream()
                .filter((rule) -> rule.test(jsonNode))
                .collect(Collectors.toList());
    }


}
