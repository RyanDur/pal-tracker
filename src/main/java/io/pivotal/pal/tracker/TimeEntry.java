package io.pivotal.pal.tracker;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDate;
import java.util.Objects;

@JsonDeserialize(builder = TimeEntry.Builder.class)
public class TimeEntry {
    private long id;
    private long projectId;
    private long userId;
    private LocalDate date;
    private int hours;

    public TimeEntry(long id, long projectId, long userId, LocalDate date, int hours) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
        this.date = date;
        this.hours = hours;
    }

    public TimeEntry(long projectId, long userId, LocalDate date, int hours) {
        this.projectId = projectId;
        this.userId = userId;
        this.date = date;
        this.hours = hours;
    }

    public TimeEntry() {

    }

    private TimeEntry(Builder builder) {
        id = builder.id;
        projectId = builder.projectId;
        userId = builder.userId;
        date = builder.date;
        hours = builder.hours;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder copy() {
        return TimeEntry.builder()
                .id(id)
                .date(date)
                .hours(hours)
                .projectId(projectId)
                .userId(userId);
    }

    public long getId() {
        return id;
    }

    public long getProjectId() {
        return projectId;
    }

    public long getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getHours() {
        return hours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeEntry timeEntry = (TimeEntry) o;
        return projectId == timeEntry.projectId &&
                userId == timeEntry.userId &&
                hours == timeEntry.hours &&
                Objects.equals(date, timeEntry.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectId, userId, date, hours);
    }

    @Override
    public String toString() {
        return "TimeEntry{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", userId=" + userId +
                ", date=" + date +
                ", hours=" + hours +
                '}';
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private long id;
        private long projectId;
        private long userId;
        private LocalDate date;
        private int hours;

        private Builder() {
        }

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder projectId(long val) {
            projectId = val;
            return this;
        }

        public Builder userId(long val) {
            userId = val;
            return this;
        }

        public Builder date(LocalDate val) {
            date = val;
            return this;
        }

        public Builder hours(int val) {
            hours = val;
            return this;
        }

        public TimeEntry build() {
            return new TimeEntry(this);
        }
    }
}
