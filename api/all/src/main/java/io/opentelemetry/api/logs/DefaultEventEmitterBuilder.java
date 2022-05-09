/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.logs;

class DefaultEventEmitterBuilder implements EventEmitterBuilder {
  private static final DefaultEventEmitterBuilder INSTANCE = new DefaultEventEmitterBuilder();

  static EventEmitterBuilder getInstance() {
    return INSTANCE;
  }

  @Override
  public EventEmitterBuilder setSchemaUrl(String schemaUrl) {
    return this;
  }

  @Override
  public EventEmitterBuilder setInstrumentationVersion(String instrumentationScopeVersion) {
    return this;
  }

  @Override
  public EventEmitter build() {
    return DefaultEventEmitter.getInstance();
  }
}
