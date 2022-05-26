/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.sdk.metrics.internal.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.util.concurrent.AtomicDouble;
import io.github.netmikey.logunit.api.LogCapturer;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.internal.testing.slf4j.SuppressLogger;
import io.opentelemetry.sdk.common.InstrumentationScopeInfo;
import io.opentelemetry.sdk.metrics.InstrumentType;
import io.opentelemetry.sdk.metrics.InstrumentValueType;
import io.opentelemetry.sdk.metrics.export.MetricReader;
import io.opentelemetry.sdk.metrics.internal.descriptor.InstrumentDescriptor;
import io.opentelemetry.sdk.metrics.internal.export.RegisteredReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressLogger(CallbackRegistration.class)
@ExtendWith(MockitoExtension.class)
class CallbackRegistrationTest {

  private static final InstrumentationScopeInfo INSTRUMENTATION_SCOPE_INFO =
      InstrumentationScopeInfo.create("meter");
  private static final InstrumentDescriptor LONG_INSTRUMENT =
      InstrumentDescriptor.create(
          "long-counter",
          "description",
          "unit",
          InstrumentType.OBSERVABLE_COUNTER,
          InstrumentValueType.LONG);
  private static final InstrumentDescriptor DOUBLE_INSTRUMENT =
      InstrumentDescriptor.create(
          "double-counter",
          "description",
          "unit",
          InstrumentType.OBSERVABLE_COUNTER,
          InstrumentValueType.LONG);

  @RegisterExtension
  LogCapturer logs = LogCapturer.create().captureForType(CallbackRegistration.class);

  @Mock private MetricReader reader;
  @Mock private AsynchronousMetricStorage<?, ?> storage1;
  @Mock private AsynchronousMetricStorage<?, ?> storage2;
  @Mock private AsynchronousMetricStorage<?, ?> storage3;

  private RegisteredReader registeredReader;
  private SdkObservableMeasurement measurement1;
  private SdkObservableMeasurement measurement2;

  @BeforeEach
  void setup() {
    registeredReader = RegisteredReader.create(reader);
    when(storage1.getRegisteredReader()).thenReturn(registeredReader);
    when(storage2.getRegisteredReader()).thenReturn(registeredReader);
    when(storage3.getRegisteredReader()).thenReturn(registeredReader);
    measurement1 =
        SdkObservableMeasurement.create(
            INSTRUMENTATION_SCOPE_INFO, DOUBLE_INSTRUMENT, Collections.singletonList(storage1));
    measurement2 =
        SdkObservableMeasurement.create(
            INSTRUMENTATION_SCOPE_INFO, LONG_INSTRUMENT, Arrays.asList(storage2, storage3));
  }

  @Test
  void callbackDescription() {
    assertThatThrownBy(() -> CallbackRegistration.callbackDescription(Collections.emptyList()))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Callback with no instruments is not allowed");
    assertThat(CallbackRegistration.callbackDescription(Collections.singletonList(LONG_INSTRUMENT)))
        .isEqualTo("Instrument long-counter");
    assertThat(
            CallbackRegistration.callbackDescription(
                Arrays.asList(LONG_INSTRUMENT, DOUBLE_INSTRUMENT)))
        .isEqualTo("BatchCallback([long-counter,double-counter])");
  }

  @Test
  void invokeCallback_Double() {
    AtomicDouble counter = new AtomicDouble();
    Runnable callback =
        () ->
            measurement1.record(
                counter.addAndGet(1.1), Attributes.builder().put("key", "val").build());
    CallbackRegistration callbackRegistration =
        CallbackRegistration.create(Collections.singletonList(measurement1), callback);

    callbackRegistration.invokeCallback(registeredReader);

    assertThat(counter.get()).isEqualTo(1.1);
    verify(storage1).recordDouble(1.1, Attributes.builder().put("key", "val").build());
    verify(storage2, never()).recordDouble(anyDouble(), any());
    verify(storage3, never()).recordDouble(anyDouble(), any());
  }

  @Test
  void invokeCallback_Long() {
    AtomicLong counter = new AtomicLong();
    Runnable callback =
        () ->
            measurement2.record(
                counter.incrementAndGet(), Attributes.builder().put("key", "val").build());
    CallbackRegistration callbackRegistration =
        CallbackRegistration.create(Collections.singletonList(measurement2), callback);

    callbackRegistration.invokeCallback(registeredReader);

    assertThat(counter.get()).isEqualTo(1);
    verify(storage1, never()).recordLong(anyLong(), any());
    verify(storage2).recordLong(1, Attributes.builder().put("key", "val").build());
    verify(storage3).recordLong(1, Attributes.builder().put("key", "val").build());
  }

  @Test
  void invokeCallback_MultipleMeasurements() {
    AtomicDouble doubleCounter = new AtomicDouble();
    AtomicLong longCounter = new AtomicLong();
    Runnable callback =
        () -> {
          measurement1.record(
              doubleCounter.addAndGet(1.1), Attributes.builder().put("key", "val").build());
          measurement2.record(
              longCounter.incrementAndGet(), Attributes.builder().put("key", "val").build());
        };
    CallbackRegistration callbackRegistration =
        CallbackRegistration.create(Arrays.asList(measurement1, measurement2), callback);

    callbackRegistration.invokeCallback(registeredReader);

    assertThat(doubleCounter.get()).isEqualTo(1.1);
    assertThat(longCounter.get()).isEqualTo(1);
    verify(storage1).recordDouble(1.1, Attributes.builder().put("key", "val").build());
    verify(storage2).recordLong(1, Attributes.builder().put("key", "val").build());
    verify(storage3).recordLong(1, Attributes.builder().put("key", "val").build());
  }

  @Test
  void invokeCallback_NoStorage() {
    SdkObservableMeasurement measurement =
        SdkObservableMeasurement.create(
            INSTRUMENTATION_SCOPE_INFO, LONG_INSTRUMENT, Collections.emptyList());
    AtomicLong counter = new AtomicLong();
    Runnable callback =
        () ->
            measurement.record(
                counter.incrementAndGet(), Attributes.builder().put("key", "val").build());
    CallbackRegistration callbackRegistration =
        CallbackRegistration.create(Collections.singletonList(measurement), callback);

    callbackRegistration.invokeCallback(registeredReader);

    assertThat(counter.get()).isEqualTo(0);
  }

  @Test
  void invokeCallback_MultipleMeasurements_ThrowsException() {
    Runnable callback =
        () -> {
          throw new RuntimeException("Error!");
        };
    CallbackRegistration callbackRegistration =
        CallbackRegistration.create(Arrays.asList(measurement1, measurement2), callback);

    callbackRegistration.invokeCallback(registeredReader);

    verify(storage1, never()).recordDouble(anyDouble(), any());
    verify(storage2, never()).recordDouble(anyDouble(), any());
    verify(storage3, never()).recordDouble(anyDouble(), any());
    logs.assertContains(
        "An exception occurred invoking callback for BatchCallback([double-counter,long-counter])");
  }

  @Test
  void invokeCallback_SingleMeasurement_ThrowsException() {
    Runnable callback =
        () -> {
          throw new RuntimeException("Error!");
        };
    CallbackRegistration callbackRegistration =
        CallbackRegistration.create(Collections.singletonList(measurement2), callback);

    callbackRegistration.invokeCallback(registeredReader);

    verify(storage1, never()).recordDouble(anyDouble(), any());
    verify(storage2, never()).recordDouble(anyDouble(), any());
    verify(storage3, never()).recordDouble(anyDouble(), any());

    logs.assertContains("An exception occurred invoking callback for Instrument long-counter");
  }
}
