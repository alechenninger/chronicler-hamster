package com.github.alechenninger.chronicler.hamster;

import java.io.IOException;

public interface ProcessLauncher {
  Process launch(String command, String... args) throws IOException;

  class RuntimeProcessLauncher implements ProcessLauncher {
    @Override
    public Process launch(String command, String... args) throws IOException {
      String execCmd = makeCmd(command, args);
      return Runtime.getRuntime().exec(execCmd);
    }

    private static String makeCmd(String first, String[] more) {
      StringBuilder joined = new StringBuilder(first);

      if (more != null) {
        for (String another : more) {
          joined.append(" ").append(another);
        }
      }

      return joined.toString();
    }
  }
}
