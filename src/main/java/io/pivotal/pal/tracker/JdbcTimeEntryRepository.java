package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.Objects.requireNonNull;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private final JdbcTemplate template;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry entry) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)";
        template.update(updateTable(entry, query), keyHolder);
        return find(requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public TimeEntry find(long id) {
        String query = "SELECT * FROM time_entries WHERE id = " + id;
        try {
            return template.queryForObject(query, timeEntryRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public List<TimeEntry> list() {
        return template.query("SELECT * FROM time_entries", timeEntryRowMapper);
    }

    @Override
    public TimeEntry update(long id, TimeEntry entry) {
        String query = "UPDATE time_entries " +
                "SET project_id=?, user_id=?, date=?, hours=? " +
                "WHERE id=?";
        template.update(query,
                entry.getProjectId(),
                entry.getUserId(),
                Date.valueOf(entry.getDate()),
                entry.getHours(),
                id);
        return find(id);
    }

    @Override
    public void delete(long id) {
        template.update("DELETE FROM time_entries WHERE id = " + id);
    }

    private RowMapper<TimeEntry> timeEntryRowMapper = (rs, rowNum) -> TimeEntry.builder()
            .id(rs.getLong("id"))
            .projectId(rs.getLong("project_id"))
            .userId(rs.getLong("user_id"))
            .date(rs.getDate("date").toLocalDate())
            .hours(rs.getInt("hours"))
            .build();

    private PreparedStatementCreator updateTable(TimeEntry entry, String query) {
        return connection -> {
            final PreparedStatement statement = connection.prepareStatement(query, RETURN_GENERATED_KEYS);
            statement.setLong(1, entry.getProjectId());
            statement.setLong(2, entry.getUserId());
            statement.setDate(3, Date.valueOf(entry.getDate()));
            statement.setInt(4, entry.getHours());
            return statement;
        };
    }
}
