/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.logs;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
class DefaultLoggerProvider implements LoggerProvider {

  private static final LoggerProvider INSTANCE = new DefaultLoggerProvider();

  static LoggerProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public Logger get(String instrumentationScopeName) {
    return DefaultLogger.getInstance();
  }

  @Override
  public Logger get(String instrumentationScopeName, String instrumentationScopeVersion) {
    return DefaultLogger.getInstance();
  }

  private DefaultLoggerProvider() {}
}
