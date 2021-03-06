package org.farmtec.res.service.rule.loader;

import net.jcip.annotations.GuardedBy;
import org.farmtec.res.enums.LogicalOperation;
import org.farmtec.res.enums.Operation;
import org.farmtec.res.enums.SupportedTypes;
import org.farmtec.res.rules.RuleComponent;
import org.farmtec.res.service.builder.utils.RuleBuilderUtil;
import org.farmtec.res.service.model.Rule;
import org.farmtec.res.service.rule.loader.dto.GroupCompositeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
/**
 * Created by dp on 19/01/2021
 * Loads rules using a RulesParser
 * The load is async, so check if the rules were properly loaded
 * by {@code is isLoadRuleDone()} and {@code isLoadRuleSuccess()}
 */
public class RuleLoaderServiceImpl implements RuleLoaderService {

    private static final Logger logger
            = LoggerFactory.getLogger(RuleLoaderServiceImpl.class);

    private final RulesParser rulesParser;
    private final ExecutorService executor;

    volatile private List<Rule> ruleList;

    @GuardedBy("this")
    private Date lastUpdateTime;
    @GuardedBy("this")
    private boolean isLoadRuleDone = false;
    @GuardedBy("this")
    private boolean isLoadRuleSuccess = false;

    public RuleLoaderServiceImpl(RulesParser rulesParser) {
        this.rulesParser = rulesParser;
        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * @return {@link List<Rule>}
     */
    @Override
    public List<Rule> getRules() {
        return this.ruleList;
    }

    /**
     * Submits the refresh task
     */
    @Override
    public synchronized Future<Boolean> refreshRules() {
        logger.info("refresh task will be submitted");
        Future<Boolean> f = executor.submit(this::refreshRulesTask);
        return f;
    }

    public Boolean refreshRulesTask() {
        synchronized (this) {
            isLoadRuleDone = false;
            isLoadRuleSuccess = false;
        }

        logger.info("starting loading rules");

        try {
            if (rulesParser.loadRules()) {
                logger.info("rules ready to be loaded into memory");
                List<Rule> freshRules = createRules();
                if (!freshRules.isEmpty()) {
                    synchronized (this) {
                        ruleList = freshRules;
                        lastUpdateTime = new Date();
                        isLoadRuleDone = true;
                        isLoadRuleSuccess = true;
                    }
                }
            } else {
                logger.error("failed to load rules");
            }
        } catch (Exception e) {
            logger.error("failed to load rules Exception", e);
        }
        return true;
    }

    private List<Rule> createRules() {

        //load the base predicates
        Map<String, RuleComponent> ruleComponentMap = rulesParser.getRuleLeafsDto().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> RuleBuilderUtil.RulePredicateBuilder
                                .newInstance()
                                .setType(SupportedTypes.getSupportedTypeFrom(entry.getValue().getType()))
                                .setTag(entry.getValue().getTag())
                                .setOperation(Operation.fromString(entry.getValue().getOperation()))
                                .setValue(entry.getValue().getValue()).build()
                ));

        //now that we have loaded the base Predicates (leafs of the tree), we can build the rules
        // a Rule will have 1 GroupComposite (which will have a tree of other )
        //for each rule get the ruleComponent using the {getAndCreateRuleComponent} recusive method

        List<Rule> rules = new ArrayList<>();
        for (String ruleName : rulesParser.getRulesDto().keySet()) {
            rules.add(RuleBuilderUtil.RuleBuilder.newInstance()
                    .setPriority(rulesParser.getRulesDto().get(ruleName).getPriority())
                    .setName(ruleName)
                    .setActions(new ArrayList<>())
                    .setRuleComponent(getAndCreateRuleComponent(rulesParser.getRulesDto().get(ruleName).getPredicateName(),
                            ruleComponentMap,
                            rulesParser.getGroupCompositesDto()))
                    .build());
        }

        return rules;
    }

    /**
     * This is a recursive method to load the tree of groups.
     * the leafs of the tree, AKA the base predicates must be preloaded in {@code ruleComponentMap}
     * The method will populate the {@code ruleComponentMap} as it create new {@link RuleComponent}
     *
     * @param groupName            name of the group to buid
     * @param ruleComponentMap     Map of built ruleComponents. It must have at least the base predicate, leafs of the rule tree
     * @param groupCompositeDtoMap Map of groupCompostitDto {@link GroupCompositeDto}
     * @return {@link RuleComponent} fully compleete ruleComponent. this is loaded into {@code ruleComponentMap}
     */
    private RuleComponent getAndCreateRuleComponent(String groupName,
                                                    Map<String, RuleComponent> ruleComponentMap,
                                                    Map<String, GroupCompositeDto> groupCompositeDtoMap) {
        //build the tree
        if (!ruleComponentMap.containsKey(groupName)) {
            logger.debug("searching for [{}]", groupName);

            GroupCompositeDto groupCompositeDto = groupCompositeDtoMap.get(groupName);

            List<RuleComponent> ruleComponentsForThisRuleComponent = new ArrayList<>();
            LogicalOperation logicalOperation = LogicalOperation.getLogicalOperation(groupCompositeDto.getOperation());

            logger.info("getting groupComponent [{}]", groupName);
            for (String name : groupCompositeDto.getPredicateNames()) {
                ruleComponentsForThisRuleComponent.add(
                        getAndCreateRuleComponent(name, ruleComponentMap, groupCompositeDtoMap)
                );
                logger.info("Adding ruleComponent to list with name [{}] for groupName [{}]", name, groupName);
            }
            logger.info("Loading groupComponent [{}] to Map", groupName);
            //create a RuleCOmponent, and update the groupRuleComponent Map
            RuleComponent rc = RuleBuilderUtil.RuleComponentBuilder.newInstance()
                    .setLogicalOperation(logicalOperation)
                    .setRuleComponentList(ruleComponentsForThisRuleComponent).build();
            //update the structure with the new ruleComponent to use for a next iteration
            ruleComponentMap.put(groupName, rc);

            return rc;
        } else {
            logger.info("groupComponent  [{}] already in Collection", groupName);
            return ruleComponentMap.get(groupName);
        }
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public boolean isLoadRuleDone() {
        return isLoadRuleDone;
    }

    public boolean isLoadRuleSuccess() {
        return isLoadRuleSuccess;
    }
}

