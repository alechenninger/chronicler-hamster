package com.github.alechenninger.chronicler.hamster;

import java.time.temporal.TemporalAccessor;

class TimeRange {
  final TemporalAccessor start;
  final TemporalAccessor end;

  TimeRange(TemporalAccessor start, TemporalAccessor end) {
    this.start = start;
    this.end = end;
  }
}
