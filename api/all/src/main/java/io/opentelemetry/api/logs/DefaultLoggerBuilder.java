/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.logs;

import io.opentelemetry.api.common.Attributes;

class DefaultLoggerBuilder implements LoggerBuilder {
  private static final DefaultLoggerBuilder INSTANCE = new DefaultLoggerBuilder();

  static LoggerBuilder getInstance() {
    return INSTANCE;
  }

  @Override
  public LoggerBuilder setSchemaUrl(String schemaUrl) {
    return this;
  }

  @Override
  public LoggerBuilder setInstrumentationVersion(String instrumentationScopeVersion) {
    return this;
  }

  @Override
  public LoggerBuilder setEventDomain(String eventDomain) {
    return this;
  }

  @Override
  public LoggerBuilder setSetContext(boolean setContext) {
    return this;
  }

  @Override
  public LoggerBuilder setAttributes(Attributes attributes) {
    return this;
  }

  @Override
  public Logger build() {
    return DefaultLogger.getInstance();
  }
}
