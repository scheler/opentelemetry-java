/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.logs;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.context.Context;
import io.opentelemetry.api.logs.Severity;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Used to construct and emit logs from a {@link EventEmitter}.
 *
 * <p>Obtain a {@link LogRecordBuilder} via {@link EventEmitter#eventBuilder(String)}, add properties using the
 * setters, and emit the event by calling {@link #emit()}.
 */
public interface LogRecord {

  /** Set the epoch timestamp using the timestamp and unit.
   *
   * <p>This is the time when the event occured.
   * */
  LogRecord setEpoch(long timestamp, TimeUnit unit);

  /** Set the epoch timestamp using the instant.
   *
   * <p>This is the time when the event occured.
   * */
  LogRecord setEpoch(Instant instant);

  /** Set the context. */
  LogRecord setContext(Context context);

  /** Set the severity. */
  LogRecord setSeverity(Severity severity);

  /** Set the severity text. */
  LogRecord setSeverityText(String severityText);

  /** Set the body string. */
  LogRecord setBody(String body);

  /** Set the attributes.
   *
   * <p> Merges with the attributes already present
   * */
  LogRecord setAttributes(Attributes attributes);

  /** Mark the completion of building LogRecord.
   *
   * <p>Only the timing of the first emit call for a given {@code LogRecord} will be recorded in the
   * <i>observed timestamp</i>, and implementations are free to ignore all further calls.
   * */
  void emit();

  /** Set the LogRecord as being an event using name as the event name, along with the attributes.
   *
   * <p>This method is equivalent to Span.addEvent and provides a path for moving to using LogRecord
   * for Span Events as against Span.Event. The event name itself is set as another attribute with
   * the semantic convention of "otel.event.name" as the attribute key.
   *
   * @param name the name of the event.
   * @param attributes the attributes that will be added; these are associated with this event, not
   *     the {@code Span} as for {@code setAttribute()}.
   * @return this.
   */
  LogRecord setEvent(String name, Attributes attributes);

  /**
   * Records information about the {@link Throwable} to the {@link LogRecord}.
   *
   * @param t the {@link Throwable} to record.
   * @param additionalAttributes the additional {@link Attributes} to record.
   * @return this.
   */
  LogRecord recordException(Throwable t, Attributes additionalAttributes);
}
