package com.github.alechenninger.chronicler.hamster;

import com.github.alechenninger.chronicler.ChroniclerException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HamsterTimeSheetOptions {
  private static Option REPORT = new Option("hr", "report", true,
      "File path for hamster report. Can be relative or absolute. Must also specify a categoryMap.");

  private static final Option CATEGORY_MAP = new Option("hc", "categoryMap", true, "JSON file which "
      + "maps Hamster activity categories to Rally projects, work products, and, optionally, tasks,"
      + " by name.");

  private static final Options OPTIONS = new Options()
      .addOption(CATEGORY_MAP)
      .addOption(REPORT);

  private static final Path DEFAULT_MAP = Paths.get("categories.json");

  private final CommandLine cli;

  public HamsterTimeSheetOptions(String[] args) throws ParseException {
    this(args, new BasicParser());
  }

  public HamsterTimeSheetOptions(String[] args, CommandLineParser parser) throws ParseException {
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

  public static void printHelpMessage() {
    HelpFormatter help = new HelpFormatter();
    help.printHelp("chronicler --source path-to-chronicler-hamster.jar",
        "Use with chronicler to upload hamster reports.",
        OPTIONS,
        "https://github.com/alechenninger/chronicler.git\nhttps://github.com/alechenninger/chronicler-hamster.git",
        true);
  }
}
