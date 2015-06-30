package com.github.alechenninger.chronicler.hamster;

import com.github.alechenninger.chronicler.ChroniclerException;
import com.github.alechenninger.chronicler.TimeEntryCoordinates;
import com.github.alechenninger.chronicler.TimeSheet;
import com.github.alechenninger.chronicler.TimeSheetFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HamsterTimeSheetFactory implements TimeSheetFactory {
  private static final ObjectMapper jsonMapper = new ObjectMapper();

  private final Hamster hamster;
  private final Clock clock;

  public HamsterTimeSheetFactory(Hamster hamster, Clock clock) {
    this.hamster = hamster;
    this.clock = clock;
  }

  @Override
  public TimeSheet getTimeSheet(String[] additionalArgs) {
    return getTimeSheet(additionalArgs, Optional.empty());
  }

  @Override
  public TimeSheet getTimeSheet(String[] additionalArgs, ZonedDateTime lastRecordedEntryTime) {
    return getTimeSheet(additionalArgs, Optional.of(lastRecordedEntryTime));
  }

  private TimeSheet getTimeSheet(String[] additionalArgs, Optional<ZonedDateTime> lastRecordedEntryTime) {
    try {
      HamsterTimeSheetOptions options = new HamsterTimeSheetOptions(additionalArgs);
      Map<String, TimeEntryCoordinates> categoryMap = deserializeCategoryMap(options.categoryMap());
      List<Activity> activities = getActivities(options, lastRecordedEntryTime);

      return new HamsterTimeSheet(activities, categoryMap);
    } catch (ParseException | IOException e) {
      throw new ChroniclerException(e);
    }
  }

  private List<Activity> getActivities(HamsterTimeSheetOptions options,
      Optional<ZonedDateTime> lastRecordedEntryTime) throws IOException {
    if (options.isReportSpecified()) {
      return hamster.deserializeReport(options.report());
    }

    if (!options.autoGenerateReport()) {
      throw new ChroniclerException("Please specify a report or ask for one to be auto generated.");
    }

    if (!lastRecordedEntryTime.isPresent()) {
      throw new ChroniclerException("Looks like you asked for a report to be generated, but have "
          + "no previously recorded time entries. Rather than trying to generate a report from "
          + "the beginning of time, please provide a manually generated report.");
    }

    TemporalAccessor start = lastRecordedEntryTime.get().withZoneSameInstant(ZoneId.systemDefault());
    TemporalAccessor end = ZonedDateTime.now(clock);

    return hamster.exportReport(start, end);
  }

  private Map<String, TimeEntryCoordinates> deserializeCategoryMap(Path categoryMapPath)
      throws IOException {
    return jsonMapper.readValue(
        categoryMapPath.toFile(),
        new TypeReference<Map<String, TimeEntryCoordinates>>() {});
  }

  public static void main(String[] args) {
    new HamsterTimeSheetFactory(new ExternalHamster(new ProcessLauncher.RuntimeProcessLauncher()), Clock.systemDefaultZone())
        .getTimeSheet(new String[]{"-hc", "categories.json", "-ha"}, ZonedDateTime.now().minus(5,
            ChronoUnit.DAYS));
  }
}
