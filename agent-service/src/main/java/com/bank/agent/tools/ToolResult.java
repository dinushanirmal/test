package com.bank.agent.tools;

public record ToolResult(Object data, Object uiSchema) {

    public boolean hasUiSchema() {
        return uiSchema != null;
    }
}
