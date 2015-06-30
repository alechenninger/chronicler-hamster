package com.github.alechenninger.chronicler.hamster;

import static java.time.format.DateTimeFormatter.ofPattern;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

public class ExternalHamster implements Hamster {
  private static final ObjectMapper xmlMapper = new XmlMapper();
  private static final DateTimeFormatter TIME_FORMATTER = ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final String HAMSTER_EXEC_NAME = "hamster";

  private final ProcessLauncher processLauncher;

  public ExternalHamster(ProcessLauncher processLauncher) {
    this.processLauncher = processLauncher;
  }

  @Override
  public List<Activity> exportReport(TemporalAccessor start, TemporalAccessor end) throws
      IOException {
    String startArg = TIME_FORMATTER.format(start);
    String endArg = TIME_FORMATTER.format(end);
    Process process = processLauncher.launch(HAMSTER_EXEC_NAME, "export xml", startArg, endArg);
    return deserializeReport(process.getInputStream());
  }

  @Override
  public List<Activity> deserializeReport(Path reportPath) throws IOException {
    return deserializeReport(new FileInputStream(reportPath.toFile()));
  }

  @Override
  public List<Activity> deserializeReport(InputStream report) throws IOException {
    return xmlMapper.readValue(report, new TypeReference<List<Activity>>() {});
  }
}
