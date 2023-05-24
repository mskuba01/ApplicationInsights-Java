// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.applicationinsights.smoketest.schemav2;

import javax.annotation.Nullable;

/**
 * This class lets its users to define an interval of time which can be defined in terms of days,
 * hours, minutes, seconds and milliseconds.
 *
 * <p>It has various constructors to let the user easily define an interval of time.
 */
public final class Duration {

  private static final long SECONDS_IN_ONE_MINUTE = 60;
  private static final long SECONDS_IN_ONE_HOUR = 3600;
  private static final long SECONDS_IN_ONE_DAY = 86400;
  private static final long MINUTES_IN_ONE_HOUR = 60;
  private static final long HOURS_IN_ONE_DAY = 24;

  private final long days;
  private final int hours;
  private final int minutes;
  private final int seconds;
  private final int milliseconds;

  /**
   * The interval is set by setting all the possible values.
   *
   * @param days Day(s).
   * @param hours Hour(s) in range [-23, 23].
   * @param minutes Minute(s) in range [-59, 59].
   * @param seconds Second(s) in range [-59, 59].
   * @param milliseconds Milliseconds in range [0, 999].
   */
  public Duration(long days, int hours, int minutes, int seconds, int milliseconds) {
    this.days = days;
    this.hours = hours;
    this.minutes = minutes;
    this.seconds = seconds;
    this.milliseconds = milliseconds;
  }

  /**
   * The duration is defined by milliseconds. The class will calculate the number of days, hours,
   * minutes, seconds and milliseconds from that value.
   *
   * @param duration The duration in milliseconds.
   */
  public Duration(long duration) {
    milliseconds = (int) (duration % 1000);

    long durationInSeconds = duration / 1000;
    seconds = (int) (durationInSeconds % SECONDS_IN_ONE_MINUTE);
    minutes = (int) ((durationInSeconds / SECONDS_IN_ONE_MINUTE) % MINUTES_IN_ONE_HOUR);
    hours = (int) ((durationInSeconds / SECONDS_IN_ONE_HOUR) % HOURS_IN_ONE_DAY);
    days = durationInSeconds / SECONDS_IN_ONE_DAY;
  }

  /**
   * Gets the days part of the duration.
   *
   * @return The days part of the duration.
   */
  public long getDays() {
    return days;
  }

  /**
   * Gets the hours part of the duration.
   *
   * @return The hours part of the duration.
   */
  public int getHours() {
    return hours;
  }

  /**
   * Gets the minutes part of the duration.
   *
   * @return The minutes part of the duration.
   */
  public int getMinutes() {
    return minutes;
  }

  /**
   * Gets the seconds part of the duration.
   *
   * @return The seconds part of the duration.
   */
  public int getSeconds() {
    return seconds;
  }

  /**
   * Gets the milliseconds part of the duration.
   *
   * @return The milliseconds part of the duration.
   */
  public int getMilliseconds() {
    return milliseconds;
  }

  /**
   * Gets the total milliseconds of the duration.
   *
   * @return The total milliseconds of the duration.
   */
  public long getTotalMilliseconds() {
    return (days * SECONDS_IN_ONE_DAY * 1000L)
        + (hours * SECONDS_IN_ONE_HOUR * 1000L)
        + (minutes * SECONDS_IN_ONE_MINUTE * 1000L)
        + (seconds * 1000L)
        + milliseconds;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(24); // length optimized for duration < 1 min
    if (days != 0) {
      appendTwoDigits(sb, days);
      sb.append('.');
    }
    appendTwoDigits(sb, hours);
    sb.append(':');
    appendTwoDigits(sb, minutes);
    sb.append(':');
    appendTwoDigits(sb, seconds);
    if (milliseconds != 0) {
      sb.append('.');
      appendThreeDigits(sb, milliseconds);
      sb.append("0000");
    }
    return sb.toString();
  }

  private static void appendTwoDigits(StringBuilder sb, long value) {
    if (value < 0) {
      sb.append('-');
      value = -value;
    }
    if (value < 10) {
      sb.append('0');
    }
    sb.append(value);
  }

  private static void appendTwoDigits(StringBuilder sb, int value) {
    if (value < 0) {
      sb.append('-');
      value = -value;
    }
    if (value < 10) {
      sb.append('0');
    }
    sb.append(value);
  }

  private static void appendThreeDigits(StringBuilder sb, int value) {
    if (value < 0) {
      sb.append('-');
      value = -value;
    }
    if (value < 10) {
      sb.append("00");
    } else if (value < 100) {
      sb.append('0');
    }
    sb.append(value);
  }

  @Override
  public boolean equals(@Nullable Object other) {
    if (this == other) {
      return true;
    }

    if (!(other instanceof Duration)) {
      return false;
    }

    Duration that = (Duration) other;
    return this.days == that.getDays()
        && this.hours == that.getHours()
        && this.minutes == that.getMinutes()
        && this.seconds == that.getSeconds()
        && this.milliseconds == that.getMilliseconds();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + (int) (days ^ (days >>> 32));
    hash = 89 * hash + hours;
    hash = 89 * hash + minutes;
    hash = 89 * hash + seconds;
    hash = 89 * hash + milliseconds;
    return hash;
  }
}
