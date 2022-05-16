/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.logs;

import io.opentelemetry.api.common.Attributes;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.ThreadSafe;

/**
 * A {@link Logger} is the entry point into an event pipeline.
 *
 * <p>Obtain a LogRecord builder via {@link #eventBuilder(String)}, add properties using the setters.
 */
@ThreadSafe
public interface Logger {

  /**
   * Return a {@link LogRecordBuilder} instance with the event.name attribute set to eventName.
   *
   * <p>Build the LogRecord using the {@link LogRecordBuilder} setters.
   */
  LogRecordBuilder eventBuilder(String eventName);


  /**
   * Create an Event based on LogRecord
   *
   * <p>The method event(String name, Attributes attributes) is equivalent to
   * eventBuilder(name).build().setAttributes(attributes).emit()
   *
   * @param name the name of the event.
   */
  void logEvent(String name);
  void logEvent(String name, Attributes attributes);
  void logEvent(String name, Attributes attributes, long timestamp, TimeUnit unit); // goes into time_unix_nano
  void logEvent(String name, Attributes attributes, Instant timestamp);

  void logException(Throwable t, Attributes attributes);

}
