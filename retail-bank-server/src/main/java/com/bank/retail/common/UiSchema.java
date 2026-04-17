package com.bank.retail.common;

import java.util.List;

/**
 * Schema-driven UI contract returned alongside tool data.
 * The Angular renderer uses `type` to look up the correct component.
 */
public record UiSchema(String type, Object data, List<UiAction> actions) {}
