/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.logs;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.context.Context;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Used to construct and emit LogRecord based events from an {@link Logger}.
 *
 * <p>Obtain a {@link LogRecordBuilder} via {@link Logger#eventBuilder(String)}, add properties using the
 * setters, and emit the log record by calling {@link #emit()}.
 */
public interface LogRecordBuilder {

  /** Set the epoch timestamp using the timestamp and unit.
   *
   * <p>This is the time when the event occured.
   * */
  LogRecordBuilder setEpoch(long timestamp, TimeUnit unit);

  /** Set the epoch timestamp using the instant.
   *
   * <p>This is the time when the event occured.
   * */
  LogRecordBuilder setEpoch(Instant instant);

  /** Set the context. */
  LogRecordBuilder setContext(Context context);

  /** Set the severity. */
  LogRecordBuilder setSeverity(Severity severity);

  /** Set the severity text. */
  LogRecordBuilder setSeverityText(String severityText);

  /** Set the body string. */
  LogRecordBuilder setBody(String body);

  /** Set the attributes. */
  LogRecordBuilder setAttributes(Attributes attributes);

  /** Build and return {@link LogRecord}. */
  LogRecord build();

  /** Mark the completion of building LogRecord.
   *
   * <p>Only the timing of the first emit call for a given {@code LogRecord} will be recorded, and
   * implementations are free to ignore all further calls.   * */
  void emit();

  /** Set the LogRecord as being an event using name as the event name, along with the attributes.
   *
   * <p>This method is equivalent to Span.addEvent and provides a path for moving to using LogRecord
   * for Span Events as against Span.Event. The event name itself is set as another attribute with
   * the semantic convention of "event.name" as the attribute key. If there is an attribute
   * with this key in the attributes parameter it will be overridden.
   *
   * @param name the name of the event.
   * @param attributes the attributes that will be added.
   * @return this.
   */
  LogRecordBuilder setEvent(String name, Attributes attributes);

  /**
   * Records information about the {@link Throwable} to the {@link LogRecord}.
   *
   * @param t the {@link Throwable} to record.
   * @param additionalAttributes the additional {@link Attributes} to record.
   * @return this.
   */
  LogRecordBuilder recordException(Throwable t, Attributes additionalAttributes);
}
