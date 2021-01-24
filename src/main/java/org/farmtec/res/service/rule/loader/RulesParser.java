package org.farmtec.res.service.rule.loader;

import org.farmtec.res.rules.RuleComponent;
import org.farmtec.res.service.rule.loader.dto.GroupCompositeDto;
import org.farmtec.res.service.rule.loader.dto.LeafDto;
import org.farmtec.res.service.rule.loader.dto.RuleDto;

import java.util.List;
import java.util.Map;

/**
 * Created by dp on 19/01/2021
 */
public interface RulesParser {

    boolean loadFile();

    boolean loadFile(String filePathName);

    Map<String, LeafDto> getRuleLeafsDto();

    Map<String, GroupCompositeDto> getGroupCompositesDto();

    Map<String, RuleDto> getRulesDto();
}
