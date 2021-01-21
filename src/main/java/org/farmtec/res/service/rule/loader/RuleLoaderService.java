package org.farmtec.res.service.rule.loader;

import org.farmtec.res.service.model.Rule;

import java.util.List;

/**
 * Created by dp on 19/01/2021
 */
public interface RuleLoaderService {

    List<Rule> getRules();

    void refreshRules();

}
