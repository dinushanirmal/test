package com.bank.retail.common;

/**
 * Every tool endpoint returns this shape.
 * `data` is consumed by the LLM; `uiSchema` is passed through to the Angular renderer.
 */
public record ToolResponse<T>(T data, UiSchema uiSchema) {}
