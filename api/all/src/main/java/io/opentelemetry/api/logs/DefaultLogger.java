/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.logs;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.context.Context;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.ThreadSafe;

/** No-op implementations of {@link DefaultLogger}. */
@ThreadSafe
final class DefaultLogger implements Logger {

  private static final DefaultLogger INSTANCE = new DefaultLogger();

  static DefaultLogger getInstance() {
    return INSTANCE;
  }

  private DefaultLogger() {}

  @Override
  public LogRecordBuilder eventBuilder(String eventName) {
    return NoopLogRecordBuilder.create();
  }

  @Override
  public LogRecordBuilder logRecordBuilder() {
    return NoopLogRecordBuilder.create();
  }

  @Override
  public void logEvent(String name) {}

  @Override
  public void logEvent(String name, Attributes attributes) {}

  @Override
  public void logEvent(String name, Attributes attributes, long timestamp, TimeUnit unit) {}

  @Override
  public void logEvent(String name, Attributes attributes, Instant timestamp) {}

  @Override
  public void logEvent(String eventName, String description) {}

  @Override
  public void logEvent(String eventName, String description, Attributes attributes) {}

  @Override
  public void logEvent(
      String eventName, String description, Attributes attributes, long timestamp, TimeUnit unit) {}

  @Override
  public void logEvent(
      String eventName, String description, Attributes attributes, Instant timestamp) {}

  @Override
  public void logException(Throwable t) {}

  @Override
  public void logException(Throwable t, Attributes attributes) {}

  // Noop implementation of LogRecordBuilder.
  private static final class NoopLogRecordBuilder implements LogRecordBuilder {
    static final NoopLogRecord LOGRECORD_INSTANCE = new NoopLogRecord();

    static NoopLogRecordBuilder create() {
      return new NoopLogRecordBuilder();
    }

    private NoopLogRecordBuilder() {}

    @Override
    public LogRecordBuilder setEpoch(long timestamp, TimeUnit unit) {
      return this;
    }

    @Override
    public LogRecordBuilder setEpoch(Instant instant) {
      return this;
    }

    @Override
    public LogRecordBuilder setContext(Context context) {
      return this;
    }

    @Override
    public LogRecordBuilder setSeverity(Severity severity) {
      return this;
    }

    @Override
    public LogRecordBuilder setSeverityText(String severityText) {
      return this;
    }

    @Override
    public LogRecordBuilder setBody(String body) {
      return this;
    }

    @Override
    public LogRecordBuilder setAttributes(Attributes attributes) {
      return this;
    }

    @Override
    public LogRecord build() {
      return LOGRECORD_INSTANCE;
    }

    @Override
    public void emit() {}

    @Override
    public LogRecordBuilder setEvent(String name, Attributes attributes) {
      return this;
    }

    @Override
    public LogRecordBuilder recordException(Throwable t, Attributes additionalAttributes) {
      return this;
    }
  }

  // Noop implementation of LogRecordBuilder.
  private static final class NoopLogRecord implements LogRecord {
    @Override
    public LogRecord setEpoch(long timestamp, TimeUnit unit) {
      return this;
    }

    @Override
    public LogRecord setEpoch(Instant instant) {
      return this;
    }

    @Override
    public LogRecord setContext(Context context) {
      return this;
    }

    @Override
    public LogRecord setSeverity(Severity severity) {
      return this;
    }

    @Override
    public LogRecord setSeverityText(String severityText) {
      return this;
    }

    @Override
    public LogRecord setBody(String body) {
      return this;
    }

    @Override
    public LogRecord setAttributes(Attributes attributes) {
      return this;
    }

    @Override
    public void emit() {}

    @Override
    public LogRecord setEvent(String name, Attributes attributes) {
      return this;
    }

    @Override
    public LogRecord recordException(Throwable t, Attributes additionalAttributes) {
      return this;
    }
  }
}
