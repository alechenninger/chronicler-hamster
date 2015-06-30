package com.github.alechenninger.chronicler.hamster;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

public class Activity {
  private final String category;
  private final String description;
  private final String name;
  private final String tags;
  private final int durationInMinutes;
  private final ZonedDateTime startTime;
  private final ZonedDateTime endTime;

  @JsonCreator
  public Activity(@JsonProperty("category") String category,
      @JsonProperty("description") String description, @JsonProperty("name") String name,
      @JsonProperty("tags") String tags,
      @JsonProperty("duration_minutes") int durationInMinutes,
      @JsonProperty("end_time")
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
      Date endTime,
      @JsonProperty("start_time")
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
      Date startTime) {
    this.category = Objects.requireNonNull(category, "category");
    this.description = Objects.requireNonNull(description, "description");
    this.name = Objects.requireNonNull(name, "name");
    this.tags = Objects.requireNonNull(tags, "tags");
    this.durationInMinutes = Objects.requireNonNull(durationInMinutes, "durationInMinutes");

    Objects.requireNonNull(endTime, "endTime");
    Objects.requireNonNull(startTime, "startTime");

    this.endTime = endTime.toInstant().atZone(ZoneId.systemDefault());
    this.startTime = startTime.toInstant().atZone(ZoneId.systemDefault());
  }

  public String getCategory() {
    return category;
  }

  public String getDescription() {
    return description;
  }

  public String getName() {
    return name;
  }

  public String getTags() {
    return tags;
  }

  public int getDurationInMinutes() {
    return durationInMinutes;
  }

  public ZonedDateTime getStartTime() {
    return startTime;
  }

  public ZonedDateTime getEndTime() {
    return endTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Activity activity = (Activity) o;
    return Objects.equals(durationInMinutes, activity.durationInMinutes) &&
        Objects.equals(category, activity.category) &&
        Objects.equals(description, activity.description) &&
        Objects.equals(name, activity.name) &&
        Objects.equals(tags, activity.tags) &&
        Objects.equals(startTime, activity.startTime) &&
        Objects.equals(endTime, activity.endTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, description, name, tags, durationInMinutes, startTime, endTime);
  }

  @Override
  public String toString() {
    return "Activity{" +
        "category='" + category + '\'' +
        ", description='" + description + '\'' +
        ", name='" + name + '\'' +
        ", tags='" + tags + '\'' +
        ", durationInMinutes=" + durationInMinutes +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        '}';
  }
}
