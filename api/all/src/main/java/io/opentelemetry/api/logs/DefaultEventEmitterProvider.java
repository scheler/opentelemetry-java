/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.logs;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
class DefaultEventEmitterProvider implements EventEmitterProvider {

  private static final EventEmitterProvider INSTANCE = new DefaultEventEmitterProvider();

  static EventEmitterProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public EventEmitter get(String instrumentationScopeName) {
    return DefaultEventEmitter.getInstance();
  }

  @Override
  public EventEmitter get(String instrumentationScopeName, String instrumentationScopeVersion) {
    return DefaultEventEmitter.getInstance();
  }

  private DefaultEventEmitterProvider() {}
}
