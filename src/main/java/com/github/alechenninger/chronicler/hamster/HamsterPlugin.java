package com.github.alechenninger.chronicler.hamster;

import com.github.alechenninger.chronicler.Plugin;
import com.github.alechenninger.chronicler.PluginInfo;
import com.github.alechenninger.chronicler.TimeSheetFactory;

import java.time.Clock;

public class HamsterPlugin implements Plugin {
  private static final ProcessLauncher processLauncher = new ProcessLauncher.RuntimeProcessLauncher();
  private static final Hamster hamster = new ExternalHamster(processLauncher);

  @Override
  public TimeSheetFactory timeSheetFactory() {
    return new HamsterTimeSheetFactory(hamster, Clock.systemDefaultZone());
  }

  @Override
  public PluginInfo info() {
    return new HamsterPluginInfo();
  }
}
