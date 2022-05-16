/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.logs;

import javax.annotation.concurrent.ThreadSafe;

/**
 * A registry for creating named {@link Logger}s. The name <i>Provider</i> is for consistency
 * with other languages and it is <b>NOT</b> loaded using reflection.
 *
 * @see Logger
 */
@ThreadSafe
public interface LoggerProvider {

  /**
   * Returns a no-op {@link LoggerProvider} which only creates no-op {@link LogRecord}s which do
   * not record nor are emitted.
   */
  static LoggerProvider noop() {
    return DefaultLoggerProvider.getInstance();
  }

  /**
   * Gets or creates a named Logger instance.
   *
   * @param instrumentationScopeName A name uniquely identifying the instrumentation scope, such as
   *     the instrumentation library, package, or fully qualified class name. Must not be null.
   * @return an Logger instance.
   */
  Logger get(String instrumentationScopeName);

  /**
   * Gets or creates a named and versioned Logger instance.
   *
   * @param instrumentationScopeName A name uniquely identifying the instrumentation scope, such as
   *     the instrumentation library, package, or fully qualified class name. Must not be null.
   * @param instrumentationScopeVersion The version of the instrumentation scope (e.g., "1.0.0").
   * @return an Logger instance.
   */
  Logger get(String instrumentationScopeName, String instrumentationScopeVersion);

  /**
   * Creates a LoggerBuilder for a named {@link Logger} instance.
   *
   * @param instrumentationScopeName A name uniquely identifying the instrumentation scope, such as
   *     the instrumentation library, package, or fully qualified class name. Must not be null.
   * @return a LoggerBuilder instance.
   */
  default LoggerBuilder loggerBuilder(String instrumentationScopeName) {
    return DefaultLoggerBuilder.getInstance();
  }
}
