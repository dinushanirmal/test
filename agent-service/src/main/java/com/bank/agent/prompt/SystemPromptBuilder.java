package com.bank.agent.prompt;

import com.bank.agent.skill.SkillRegistry;
import org.springframework.stereotype.Component;

@Component
public class SystemPromptBuilder {

    private final SkillRegistry skillRegistry;

    public SystemPromptBuilder(SkillRegistry skillRegistry) {
        this.skillRegistry = skillRegistry;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("""
            You are a banking assistant. You help customers check balances and transfer funds.

            RULES:
            - Always use the available tools. Never invent data or fabricate balances.
            - Call only the tools offered for the current request.
            - Stop calling tools as soon as you have the result needed for this step.
            - executeTransfer is NOT available — never attempt to call it.
            - DEFAULT CUSTOMER ID: C001 (use this unless the user specifies otherwise).

            SKILLS:
            """);

        skillRegistry.all().forEach(skill -> {
            sb.append("\n=== ").append(skill.getName()).append(" ===\n");
            sb.append(skill.getSystemInstructions()).append("\n");
        });

        return sb.toString();
    }
}
