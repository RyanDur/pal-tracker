package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository extends TimeEntryRepository {

    private final HashMap<Long, TimeEntry> repo;
    private long latestId;

    public InMemoryTimeEntryRepository() {
        repo = new HashMap<>();
        latestId = 1L;
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry copy = timeEntry.copy().id(latestId).build();
        repo.put(copy.getId(), copy);
        latestId++;
        return copy;
    }

    @Override
    public TimeEntry find(long id) {
        return repo.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(repo.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (!repo.containsKey(id)) return null;
        final TimeEntry entry = timeEntry.copy().id(id).build();
        repo.put(id, entry);
        return entry;
    }

    @Override
    public void delete(long id) {
        repo.remove(id);
    }
}
