package com.github.alechenninger.chronicler.hamster;

import com.github.alechenninger.chronicler.PluginInfo;

import org.apache.commons.cli.Options;

public class HamsterPluginInfo implements PluginInfo {
  @Override
  public String url() {
    return "https://github.com/alechenninger/chronicler-hamster";
  }

  @Override
  public String name() {
    return "chronicler-hamster";
  }

  @Override
  public Options cmdLineOptions() {
    return HamsterTimeSheetOptions.getOptions();
  }

  @Override
  public String version() {
    return "3.0.0";
  }

  @Override
  public String exampleUsage() {
    return "chronicler -s chronicler-hamster.jar -hc categories.json -hr report.xml\n"
        + "chronicler -s chronicler-hamster.jar -hc categories.json --auto";
  }

  @Override
  public String description() {
    return "Enables hamster xml reports as a Chronicler time sheet source";
  }
}
