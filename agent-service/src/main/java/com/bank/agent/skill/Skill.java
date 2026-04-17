package com.bank.agent.skill;

import java.util.List;

public class Skill {

    private String id;
    private String name;
    private List<String> intentKeywords;
    private String systemInstructions;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getIntentKeywords() { return intentKeywords; }
    public void setIntentKeywords(List<String> intentKeywords) { this.intentKeywords = intentKeywords; }

    public String getSystemInstructions() { return systemInstructions; }
    public void setSystemInstructions(String systemInstructions) { this.systemInstructions = systemInstructions; }
}
