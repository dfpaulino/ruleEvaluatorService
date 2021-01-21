package org.farmtec.res.service.rule.loader.file;

import org.farmtec.res.enums.Operation;
import org.farmtec.res.predicate.factory.PredicateFactory;
import org.farmtec.res.predicate.factory.impl.PredicateGeneratorForInt;
import org.farmtec.res.predicate.factory.impl.PredicateGeneratorForStr;
import org.farmtec.res.rules.RuleComponent;
import org.farmtec.res.service.builder.utils.RuleBuilderUtil;
import org.farmtec.res.service.model.Rule;
import org.farmtec.res.service.rule.loader.RuleLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                createRules();
            } else {
                logger.error("failed to load rules");
                loadRules.cancel(true);
            }
        } catch (Exception e) {
            logger.error("failed to load rules Exception", e);
        }
    }

    private boolean createRules() {


        Map<String, RuleComponent> leafs = fileParser.getRuleLeafsDto().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> RuleBuilderUtil.RulePredicateBuilder
                                .newInstance()
                                .setType(getTypeFromDto(entry.getValue().getType()))
                                .setTag(entry.getValue().getTag())
                                .setOperation(Operation.fromString(entry.getValue().getOperation()))
                                .setValue(entry.getValue().getValue()).build()
                ));
        return true;
    }

    private Class<?> getTypeFromDto(String type) {
        //default
        Class<?> t = String.class;
        switch (type.toLowerCase()) {
            case "string":
                t = String.class;
            case "int":
                t = Integer.class;
        }
        return t;
    }
}
