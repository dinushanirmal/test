package com.bank.agent.skill;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class SkillLoader {

    @Bean
    public SkillRegistry skillRegistry() throws IOException {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:skills/*.yaml");

        Map<String, Skill> skills = new LinkedHashMap<>();
        for (Resource resource : resources) {
            Skill skill = yamlMapper.readValue(resource.getInputStream(), Skill.class);
            skills.put(skill.getId(), skill);
        }
        return new SkillRegistry(skills);
    }
}
