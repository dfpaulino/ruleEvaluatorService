package org.farmtec.res.service.rule.loader.file;

import org.farmtec.res.service.rule.loader.RulesParser;
import org.farmtec.res.service.rule.loader.dto.GroupCompositeDto;
import org.farmtec.res.service.rule.loader.dto.LeafDto;
import org.farmtec.res.service.rule.loader.dto.RuleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dp on 20/01/2021
 */
public class RulesParserFileImpl implements RulesParser {

    private static final Logger logger
            = LoggerFactory.getLogger(RulesParserFileImpl.class);

    private final static String PREDICATE_GROUP_PREFIX = "[P";
    private final static String PREDICATE_TYPE = "type";
    private final static String PREDICATE_TAG = "tag";
    private final static String PREDICATE_VALUE = "value";
    private final static String PREDICATE_OPERATION = "operation";

    private final static String GROUP_GROUP_PREFIX = "[G";
    private final static String GROUP_PREDICATE_LIST = "predicateList";
    private final static String GROUP_OPERATION = "operation";

    private final static String RULE_GROUP_PREFIX = "[Rule_";
    private final static String RULE_GROUP_PREDICTE = "groupPredicate";
    private final static String RULE_PRIORITY = "priority";


    private String filePathName;

    private Map<String, LeafDto> leafDtoMap = new HashMap<>();
    private Map<String, GroupCompositeDto> groupCompositeDtoMap = new HashMap<>();
    private Map<String, RuleDto> ruleDtoMap = new HashMap<>();

    public RulesParserFileImpl(String filePathName) {
        this.filePathName = filePathName;
    }


    @Override
    public boolean loadFile() {
        //clear the Maps for future loading
        leafDtoMap.clear();
        groupCompositeDtoMap.clear();
        ruleDtoMap.clear();
        return openAndReadFile(this.filePathName);
    }

    @Override
    public boolean loadFile(String filePathName) {
        this.filePathName = filePathName;
        return loadFile();
    }

    private boolean openAndReadFile(String fileName) {
        boolean returnFlag = false;

        Path path = Paths.get(fileName);
        String line;
        GroupType currentGroupType = GroupType.NONE;
        String currentGroupName = "";

        LeafDto currentLeafDto = new LeafDto();
        GroupCompositeDto currentGroupCompositeDto = new GroupCompositeDto();
        RuleDto currentRuleDto = new RuleDto();

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            while (null != (line = br.readLine())) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    logger.trace("reading line [{}]", line);
                    //validate group and name
                    if (line.startsWith(PREDICATE_GROUP_PREFIX)) {
                        currentGroupType = GroupType.PREDICATE;
                        currentGroupName = line.substring(1, line.lastIndexOf("]"));
                        currentLeafDto = new LeafDto();
                    } else if (line.startsWith(GROUP_GROUP_PREFIX)) {
                        currentGroupType = GroupType.GROUP_COMPOSITE;
                        currentGroupName = line.substring(1, line.lastIndexOf("]"));
                        currentGroupCompositeDto = new GroupCompositeDto();
                    } else if (line.startsWith(RULE_GROUP_PREFIX)) {
                        currentGroupType = GroupType.RULE;
                        currentGroupName = line.substring(1, line.lastIndexOf("]"));
                        currentRuleDto = new RuleDto();
                    } else {
                        //remove any spaces, and split by =
                        String[] attributeValuePair = line.split("=");
                        if (attributeValuePair.length == 2) {
                            attributeValuePair[0] = attributeValuePair[0].trim();
                            attributeValuePair[1] = attributeValuePair[1].trim();
                            switch (currentGroupType) {
                                case PREDICATE:
                                    if (setCurrentLeafAttrAndCheckIsComplete(currentLeafDto,
                                            attributeValuePair[0].toLowerCase(),
                                            attributeValuePair[1])) {
                                        leafDtoMap.put(currentGroupName, currentLeafDto);
                                        currentGroupType = GroupType.NONE;
                                        logger.info("Predicate [{}] was added with value [{}]"
                                                , currentGroupName, currentLeafDto.toString());
                                    }
                                    break;
                                case GROUP_COMPOSITE:
                                    if (setCurrentGroupCompositeAttrAndCheckIsComplete(currentGroupCompositeDto,
                                            attributeValuePair[0],
                                            attributeValuePair[1])) {
                                        groupCompositeDtoMap.put(currentGroupName, currentGroupCompositeDto);
                                        currentGroupType = GroupType.NONE;
                                        logger.info("Group [{}] was added with value [{}]"
                                                , currentGroupName, currentGroupCompositeDto.toString());
                                    }
                                    break;
                                case RULE:
                                    if (setCurrentRuleAttrAndCheckIsComplete(currentRuleDto,
                                            attributeValuePair[0],
                                            attributeValuePair[1])) {
                                        ruleDtoMap.put(currentGroupName, currentRuleDto);
                                        currentGroupType = GroupType.NONE;
                                        logger.info("Rule [{}] was added with value [{}]"
                                                , currentGroupName, currentRuleDto.toString());
                                    }
                                    break;
                                default:
                                    logger.warn("elements out of order? [{}]", line);
                            }
                        } // end ttributeValuePair.length == 2

                    }
                }

            }
        } catch (Exception e) {
            logger.error("Exception {[]}", e);
        }
        return leafDtoMap.size() > 1 && groupCompositeDtoMap.size() > 0 && ruleDtoMap.size() > 0;
    }

    private boolean setCurrentLeafAttrAndCheckIsComplete(LeafDto leafDto, String attributeName, String attributeValue) {
        switch (attributeName) {
            case PREDICATE_TYPE:
                leafDto.setType(attributeValue);
                break;
            case PREDICATE_TAG:
                leafDto.setTag(attributeValue);
                break;
            case PREDICATE_VALUE:
                leafDto.setValue(attributeValue);
                break;
            case PREDICATE_OPERATION:
                leafDto.setOperation(attributeValue);
                break;
        }
        return leafDto.isComplete();
    }


    private boolean setCurrentGroupCompositeAttrAndCheckIsComplete(GroupCompositeDto groupCompositeDto, String attributeName, String attributeValue) {
        switch (attributeName) {
            case GROUP_PREDICATE_LIST:
                groupCompositeDto.setPredicateNames(Arrays.asList(attributeValue.split(",")));
                break;
            case GROUP_OPERATION:
                groupCompositeDto.setOperation(attributeValue);
                break;
        }
        return groupCompositeDto.isComplete();
    }

    private boolean setCurrentRuleAttrAndCheckIsComplete(RuleDto ruleDto, String attributeName, String attributeValue) {
        switch (attributeName) {
            case RULE_GROUP_PREDICTE:
                ruleDto.setPredicateName(attributeValue);
                break;
            case RULE_PRIORITY:
                try {
                    ruleDto.setPriority(Integer.valueOf(attributeValue));
                } catch (NumberFormatException nfe) {
                    logger.error("cant parse priority [{}]", nfe.getMessage());
                }
                break;
        }
        return ruleDto.isComplete();
    }

    @Override
    public Map<String, LeafDto> getRuleLeafsDto() {
        return this.leafDtoMap;
    }

    @Override
    public Map<String, GroupCompositeDto> getGroupCompositesDto() {
        return this.groupCompositeDtoMap;
    }

    @Override
    public Map<String, RuleDto> getRulesDto() {
        return this.ruleDtoMap;
    }

    public enum GroupType {
        PREDICATE, GROUP_COMPOSITE, RULE, NONE
    }

}
