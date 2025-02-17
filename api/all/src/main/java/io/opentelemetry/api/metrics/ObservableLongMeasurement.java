/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.api.metrics;

import io.opentelemetry.api.common.Attributes;

/** An interface for observing measurements with {@code long} values. */
public interface ObservableLongMeasurement extends ObservableMeasurement {

  /**
   * Records a measurement.
   *
   * @param value The measurement amount.
   */
  void record(long value);

  /**
   * Records a measurement with a set of attributes.
   *
   * @param value The measurement amount.
   * @param attributes A set of attributes to associate with the count.
   */
  void record(long value, Attributes attributes);
}
