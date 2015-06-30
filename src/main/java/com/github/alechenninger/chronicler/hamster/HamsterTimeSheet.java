package com.github.alechenninger.chronicler.hamster;

import com.github.alechenninger.chronicler.TimeEntry;
import com.github.alechenninger.chronicler.TimeEntryCoordinates;
import com.github.alechenninger.chronicler.TimeSheet;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HamsterTimeSheet implements TimeSheet {
  private static final Logger logger =  Logger.getLogger(HamsterTimeSheet.class.getName());

  private final List<Activity> activities;
  private final Map<String, TimeEntryCoordinates> categoryMap;

  public HamsterTimeSheet(List<Activity> activities,
      Map<String, TimeEntryCoordinates> categoryMap) {
    this.activities = Objects.requireNonNull(activities, "activites");
    this.categoryMap = Objects.requireNonNull(categoryMap, "categoryMap");
  }

  @Override
  public List<TimeEntry> getEntries() {
    return activities.stream()
        .filter(this::hasMappedCategory)
        .map(a -> new TimeEntry(categoryMap.get(a.getCategory()), a.getStartTime(),
            (float) a.getDurationInMinutes() / 60f))
        .collect(Collectors.toList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    HamsterTimeSheet that = (HamsterTimeSheet) o;

    if (!activities.equals(that.activities)) {
      return false;
    }
    if (!categoryMap.equals(that.categoryMap)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = activities.hashCode();
    result = 31 * result + categoryMap.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "HamsterTimeSheet{" +
        "activities=" + activities +
        ", categoryMap=" + categoryMap +
        '}';
  }

  private boolean hasMappedCategory(Activity activity) {
    boolean isMapped = categoryMap.containsKey(activity.getCategory());

    if (!isMapped) {
      logger.warning("Activity found with unmapped category: " + activity);
    }

    return isMapped;
  }
}
