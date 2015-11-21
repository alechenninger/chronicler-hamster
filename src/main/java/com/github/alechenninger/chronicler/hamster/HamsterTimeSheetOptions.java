package com.github.alechenninger.chronicler.hamster;

import static java.time.format.DateTimeFormatter.ofPattern;

import com.github.alechenninger.chronicler.ChroniclerException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class HamsterTimeSheetOptions {
  private static final String TIME_PATTERN = "yyyy-MM-dd";
  private static final DateTimeFormatter TIME_FORMATTER = ofPattern(TIME_PATTERN);

  private static Option REPORT = new Option("hr", "report", true,
      "File path for hamster report. Can be relative or absolute. Must also specify a categoryMap.");

  private static final Option CATEGORY_MAP = new Option("hc", "categoryMap", true, "JSON file which "
      + "maps Hamster activity categories to Rally projects, work products, and, optionally, tasks,"
      + " by name.");

  private static final Option AUTO = new Option("ha", "auto", false, "Instructs the plugin to "
      + "automatically generate a report since the last updated time sheet entry.");

  private static final Option START_DATE = new Option("hs", "startDate", true, "Date to start "
      + "report generation (" + TIME_PATTERN + ")");

  private static final Option END_DATE = new Option("he", "endDate", true, "Date to end "
      + "report generation (" + TIME_PATTERN + ")");

  private static final Options OPTIONS = new Options()
      .addOption(CATEGORY_MAP)
      .addOption(REPORT)
      .addOption(AUTO)
      .addOption(START_DATE)
      .addOption(END_DATE);

  private static final Path DEFAULT_MAP = Paths.get("categories.json");

  private final CommandLine cli;
  private final Clock clock;

  public HamsterTimeSheetOptions(String[] args) throws ParseException {
    this(args, Clock.systemDefaultZone());
  }

  public HamsterTimeSheetOptions(String[] args, Clock clock) throws ParseException {
    this(args, new BasicParser(), clock);
  }

  public HamsterTimeSheetOptions(String[] args, CommandLineParser parser, Clock clock)
      throws ParseException {
    this.clock = clock;

    cli = parser.parse(OPTIONS, args);
  }

  public Path report() {
    if (cli.hasOption(REPORT.getOpt())) {
      return Paths.get(cli.getOptionValue(REPORT.getOpt()));
    }

    throw new ChroniclerException("No report file specified, specify one via " + REPORT);
  }

  public Path categoryMap() {
    if (cli.hasOption(CATEGORY_MAP.getOpt())) {
      return Paths.get(cli.getOptionValue(CATEGORY_MAP.getOpt()));
    }

    if (DEFAULT_MAP.toFile().exists()) {
      return DEFAULT_MAP;
    }

    throw new ChroniclerException("No category map specified, and default (" + DEFAULT_MAP + ") "
        + "not found. A category map is necessary to translate Hamster activity categories to Rally"
        + " time sheet entries. Specify one via " + CATEGORY_MAP);
  }

  public TimeRange timeRange() {
    ZonedDateTime startDate = TIME_FORMATTER.parse(cli.getOptionValue(START_DATE.getOpt()),
        ZonedDateTime::from);
    ZonedDateTime endDate = cli.hasOption(END_DATE.getOpt())
        ? TIME_FORMATTER.parse(cli.getOptionValue(END_DATE.getOpt()), ZonedDateTime::from)
        : ZonedDateTime.now(clock);

    return new TimeRange(startDate, endDate);
  }

  public boolean isTimeRangeSpecified() {
    return cli.hasOption(START_DATE.getOpt());
  }

  public boolean isReportSpecified() {
    return cli.hasOption(REPORT.getOpt());
  }

  public boolean autoGenerateReport() {
    return cli.hasOption(AUTO.getOpt());
  }

  public static Options getOptions() {
    return OPTIONS;
  }
}
