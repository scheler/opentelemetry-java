/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.logs;

import javax.annotation.concurrent.ThreadSafe;

/**
 * A registry for creating named {@link EventEmitter}s. The name <i>Provider</i> is for consistency
 * with other languages and it is <b>NOT</b> loaded using reflection.
 *
 * @see EventEmitter
 */
@ThreadSafe
public interface EventEmitterProvider {

  /**
   * Returns a no-op {@link EventEmitterProvider} which only creates no-op {@link LogRecord}s which do
   * not record nor are emitted.
   */
  static EventEmitterProvider noop() {
    return DefaultEventEmitterProvider.getInstance();
  }

  /**
   * Gets or creates a named EventEmitter instance.
   *
   * @param instrumentationScopeName A name uniquely identifying the instrumentation scope, such as
   *     the instrumentation library, package, or fully qualified class name. Must not be null.
   * @return an EventEmitter instance.
   */
  EventEmitter get(String instrumentationScopeName);

  /**
   * Gets or creates a named and versioned EventEmitter instance.
   *
   * @param instrumentationScopeName A name uniquely identifying the instrumentation scope, such as
   *     the instrumentation library, package, or fully qualified class name. Must not be null.
   * @param instrumentationScopeVersion The version of the instrumentation scope (e.g., "1.0.0").
   * @return an EventEmitter instance.
   */
  EventEmitter get(String instrumentationScopeName, String instrumentationScopeVersion);

  /**
   * Creates a EventEmitterBuilder for a named {@link EventEmitter} instance.
   *
   * @param instrumentationScopeName A name uniquely identifying the instrumentation scope, such as
   *     the instrumentation library, package, or fully qualified class name. Must not be null.
   * @return a EventEmitterBuilder instance.
   */
  default EventEmitterBuilder eventEmitterBuilder(String instrumentationScopeName) {
    return DefaultEventEmitterBuilder.getInstance();
  }
}
