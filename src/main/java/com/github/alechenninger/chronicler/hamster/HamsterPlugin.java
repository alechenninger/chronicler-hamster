package com.github.alechenninger.chronicler.hamster;

import com.github.alechenninger.chronicler.Plugin;
import com.github.alechenninger.chronicler.PluginInfo;
import com.github.alechenninger.chronicler.TimeSheetFactory;

public class HamsterPlugin implements Plugin {
  @Override
  public TimeSheetFactory timeSheetFactory() {
    return new HamsterTimeSheetFactory();
  }

  @Override
  public PluginInfo info() {
    return new HamsterPluginInfo();
  }
}
