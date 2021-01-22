package org.farmtec.res.service.rule.loader.file;

import org.farmtec.res.enums.LogicalOperation;
import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;
import org.farmtec.res.predicate.factory.impl.PredicateGeneratorForInt;
import org.farmtec.res.predicate.factory.impl.PredicateGeneratorForStr;
import org.farmtec.res.rules.RuleComponent;
import org.farmtec.res.rules.impl.ImmutableRuleGroupComposite;
import org.farmtec.res.service.builder.utils.RuleBuilderUtil;
import org.farmtec.res.service.model.Rule;
import org.farmtec.res.service.rule.loader.RuleLoaderService;
import org.farmtec.res.service.rule.loader.dto.GroupCompositeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.farmtec.res.enums.Operation.EQ;

/**
 * Created by dp on 19/01/2021
 */
public class RuleLoaderServiceFileImpl implements RuleLoaderService {

    private static final Logger logger
            = LoggerFactory.getLogger(RuleLoaderServiceFileImpl.class);

    private final FileParser fileParser;

    private final ExecutorService executor;

    private List<Rule> ruleList;

    public RuleLoaderServiceFileImpl(FileParser fileParser) {
        this.fileParser = fileParser;
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public List<Rule> getRules() {
        return this.ruleList;
    }

    @Override
    public void refreshRules() {
        // RulesLoaderTask rulesLoaderTask = new RulesLoaderTask();
        // Callable<Boolean> task = () -> fileParser.loadFile();
        logger.info("starting loading rules on task");
        Future<Boolean> loadRules = executor.submit(
                () -> fileParser.loadFile()
        );
        try {
            if (loadRules.get(5, TimeUnit.SECONDS)) {
                logger.info("rules ready to be loaded into memory");
                List<Rule> freshRules = createRules();
                if (!freshRules.isEmpty()) {
                    ruleList = freshRules;
                }
            } else {
                logger.error("failed to load rules");
                loadRules.cancel(true);
            }
        } catch (Exception e) {
            logger.error("failed to load rules Exception", e);
        }
    }

    private List<Rule> createRules() {

        //load the base predicates
        Map<String, RuleComponent> ruleComponentMap = fileParser.getRuleLeafsDto().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> RuleBuilderUtil.RulePredicateBuilder
                                .newInstance()
                                .setType(getTypeFromDto(entry.getValue().getType()))
                                .setTag(entry.getValue().getTag())
                                .setOperation(Operation.fromString(entry.getValue().getOperation()))
                                .setValue(entry.getValue().getValue()).build()
                ));

        List<Rule> rules = new ArrayList<>();
        for (String ruleName : fileParser.getRulesDto().keySet()) {
            rules.add(RuleBuilderUtil.RuleBuilder.newInstance()
                    .setPriority(fileParser.getRulesDto().get(ruleName).getPriority())
                    .setName(ruleName)
                    .setActions(new ArrayList<>())
                    .setRuleComponent(getRuleComponentsFromGroupCompositesDto(fileParser.getRulesDto().get(ruleName).getPredicateName(),
                            ruleComponentMap,
                            fileParser.getGroupCompositesDto()))
                    .build());
        }

        return rules;
    }

    //TODO this needs to go somewhere else...NOT HERE!!
    private Class<?> getTypeFromDto(String type) {
        //default
        Class<?> t = String.class;
        switch (type.toLowerCase()) {
            case "string":
                t = String.class;
                break;
            case "int":
                t = Integer.class;
                break;
        }
        return t;
    }

    private RuleComponent getRuleComponentsFromGroupCompositesDto(String groupName,
                                                                  Map<String, RuleComponent> groupRuleComponent, Map<String, GroupCompositeDto> groupCompositeDtoMap) {
        //build the tree
        if (!groupRuleComponent.containsKey(groupName)) {

            GroupCompositeDto groupCompositeDto = groupCompositeDtoMap.get(groupName);
            List<RuleComponent> ruleComponents = new ArrayList<>();
            LogicalOperation logicalOperation = LogicalOperation.getLogicalOperation(groupCompositeDto.getOperation());
            logger.info("getting groupComponent [{}]", groupName);
            for (String name : groupCompositeDto.getPredicateNames()) {
                // groupRuleComponent.put(name,
                //         getRuleComponentsFromGroupCompositesDto(name, groupRuleComponent, groupCompositeDtoMap).get(name));
                ruleComponents.add(
                        getRuleComponentsFromGroupCompositesDto(name, groupRuleComponent, groupCompositeDtoMap)
                );
                logger.info("Adding ruleComponent to list with name [{}] for groupName [{}]", name, groupName);
            }
            logger.info("Loading groupComponent [{}] to Map", groupName);
            RuleComponent rc = RuleBuilderUtil.RuleComponentBuilder.newInstance()
                    .setLogicalOperation(logicalOperation)
                    .setRuleComponentList(ruleComponents).build();
            groupRuleComponent.put(groupName, rc);
            return rc;
        } else {
            logger.info("groupComponent  [{}] already loaded in MAP", groupName);
            return groupRuleComponent.get(groupName);
        }
    }
}
