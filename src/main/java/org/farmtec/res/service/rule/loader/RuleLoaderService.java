package org.farmtec.res.service.rule.loader;

import org.farmtec.res.service.model.Rule;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by dp on 19/01/2021
 */
public interface RuleLoaderService {

    List<Rule> getRules();

    Future<Boolean> refreshRules();

    public Date getLastUpdateTime();

    public boolean isLoadRuleDone();

    public boolean isLoadRuleSuccess();
}
