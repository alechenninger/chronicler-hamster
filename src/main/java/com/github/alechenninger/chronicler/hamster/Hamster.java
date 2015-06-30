package com.github.alechenninger.chronicler.hamster;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.temporal.TemporalAccessor;
import java.util.List;

public interface Hamster {
  List<Activity> exportReport(TemporalAccessor start, TemporalAccessor end) throws IOException;

  List<Activity> deserializeReport(InputStream report) throws IOException;

  default List<Activity> deserializeReport(Path reportPath) throws IOException {
    return deserializeReport(new FileInputStream(reportPath.toFile()));
  }
}
