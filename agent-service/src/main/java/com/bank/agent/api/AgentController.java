package com.bank.agent.api;

import com.bank.agent.agent.AgentLoop;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentLoop agentLoop;

    public AgentController(AgentLoop agentLoop) {
        this.agentLoop = agentLoop;
    }

    @PostMapping("/message")
    public AgentResponse message(@RequestBody AgentRequest request) {
        return agentLoop.process(request.sessionId(), request.message());
    }

    @PostMapping("/confirm")
    public AgentResponse confirm(@RequestBody ConfirmRequest request) {
        return agentLoop.confirm(request.sessionId(), request.action());
    }
}
