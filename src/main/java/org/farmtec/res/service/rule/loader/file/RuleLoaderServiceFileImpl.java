package org.farmtec.res.service.rule.loader.file;

import org.farmtec.res.service.model.Rule;
import org.farmtec.res.service.rule.loader.RuleLoaderService;

import java.util.List;

/**
 * Created by dp on 19/01/2021
 */
public class RuleLoaderServiceFileImpl implements RuleLoaderService {
    private final String fileName;
    private final FileParser fileParser;

    private List<Rule> ruleList;

    public RuleLoaderServiceFileImpl(String fileName, FileParser fileParser) {
        this.fileName = fileName;
        this.fileParser = fileParser;
    }

    @Override
    public List<Rule> getRules() {
        return this.ruleList;
    }

    @Override
    public void refreshRules() {

    }


}
