package org.farmtec.res.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.service.model.Rule;
import org.farmtec.res.service.rule.loader.RuleLoaderService;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Contains the Set Of Rules to be tested Rule List Is thread safe as long there is only 1 Thread
 * that can modify RuleList All other Thread are read only
 */
public class RuleServiceImpl implements RuleService {

  // Volatile, R/W are done directly in the RAM. this wont loaded in to the L1/L2 cpu cache


  // The ruleList is a reference to a List, so if the Writer modifies the reference to a new Loaded list
  // the address will be seen to all Reader threads
  //
  //This wont have effect on the members (members are not volatile!), only on the variable that holds the address of the List
  // trade off for not using lock's. will this be faster?
  private volatile List<Rule> ruleList;
  private Date lastUpdate;
  private final RuleLoaderService ruleLoaderService;

  private Lock updateRulesLock = new ReentrantLock();

  public RuleServiceImpl(RuleLoaderService ruleLoaderService) {
    this.ruleLoaderService = ruleLoaderService;
  }

  public List<Rule> getRuleList() {
    return ruleList;
  }

  public boolean updateRules() throws Exception {
    boolean updateRulesIsSuccessful = false;

    if (updateRulesLock.tryLock(10, TimeUnit.SECONDS)) {
      try {
        Future<Boolean> loadTask = ruleLoaderService.refreshRules();
        //block until request is completed
        boolean isCompleted = loadTask.get();
        if (isCompleted) {
          if (ruleLoaderService.isLoadRuleDone() && ruleLoaderService.isLoadRuleSuccess()) {
            this.setRuleList(ruleLoaderService.getRules());
            updateRulesIsSuccessful = true;
          }
        }
      } finally {
        updateRulesLock.unlock();
      }
    } else {
      System.out.println("Timed out to load rules...");
    }

    return updateRulesIsSuccessful;
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
    return this.ruleList.stream().filter(rule -> {
          boolean isMatchFilter = false;
          if (rule.getFilter().isEmpty()) {
            isMatchFilter = true;
          } else {
            if (!jsonNode.get("filter").isNull()) {
              String filter = jsonNode.get("filter").asText();
              isMatchFilter = rule.getFilter().equalsIgnoreCase(filter);
            }
          }
          return isMatchFilter;
        }
    )
        .filter((rule) -> rule.test(jsonNode))
        .collect(Collectors.toList());
  }

}
