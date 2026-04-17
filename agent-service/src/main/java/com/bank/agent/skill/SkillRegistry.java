package com.bank.agent.skill;

import java.util.Collection;
import java.util.Map;

public class SkillRegistry {

    private final Map<String, Skill> skills;

    public SkillRegistry(Map<String, Skill> skills) {
        this.skills = skills;
    }

    public Collection<Skill> all() {
        return skills.values();
    }

    public Skill get(String id) {
        return skills.get(id);
    }
}
