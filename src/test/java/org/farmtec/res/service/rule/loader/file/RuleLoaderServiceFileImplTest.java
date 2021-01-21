package org.farmtec.res.service.rule.loader.file;

import org.farmtec.res.service.rule.loader.RuleLoaderService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dp on 21/01/2021
 */
class RuleLoaderServiceFileImplTest {

    @Test
    void getRules() {
    }

    @Test
    void refreshRules() {
        //given
        RuleLoaderService ruleLoaderService = new RuleLoaderServiceFileImpl(
                new FileParserImpl("src/test/resources/rules101.cfg"));
        //when
        ruleLoaderService.refreshRules();
    }
}