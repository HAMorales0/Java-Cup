package com.techelevator.dao;

import com.techelevator.model.Tournament;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTournamentDao implements TournamentDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTournamentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tournament> findAll() {
        List<Tournament> tournaments = new ArrayList<>();
        String sql = "SELECT * FROM tournament";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Tournament tournament = mapRowToTournament(results);
            tournaments.add(tournament);
        }
        return tournaments;




    }

    @Override
    public Tournament getTournamentById(int tournamentId) {

        String sql = "SELECT * FROM tournament WHERE tournament_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, tournamentId);
        if (results.next()) {
            return mapRowToTournament(results);
        } else {
            return null;
        }
    }

    @Override
    public void createTournament(Tournament tournament) {
        String sql = "INSERT INTO tournament(tournament_name, tournament_date, number_of_participants, organizer_id) VALUES (?, ?, ?, ?);";

        jdbcTemplate.update(sql, tournament.getTournamentName(), tournament.getTournamentDate(), tournament.getNumberOfParticipants(), tournament.getOrganizerId());

    }

    @Override
    public void updateTournament(int tournamentId, Tournament tournament) {
        String sql = "UPDATE tournament " +
                "SET tournament_name=?, tournament_date=?, number_of_participants=? " +
                "WHERE tournament_id=?;";


        jdbcTemplate.update(sql, tournament.getTournamentName(), tournament.getTournamentDate(), tournament.getNumberOfParticipants(), tournamentId);
    }

    @Override
    public List<Tournament> searchByTournamentName(String search) {
        List<Tournament> tournaments = new ArrayList<>();
        String sql = "SELECT * FROM tournament WHERE tournament_name ILIKE ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%"+search+ "%");
        while(results.next()) {
            Tournament tournament = mapRowToTournament(results);
            tournaments.add(tournament);
        }
        return tournaments;

    }

    @Override
    public List<Tournament> getTournamentsByOrganizerId(int organizerId) {
        List<Tournament> tournaments = new ArrayList<>();
        String sql = "SELECT * FROM tournament WHERE organizer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, organizerId);
        while(results.next()) {
            Tournament tournament = mapRowToTournament(results);
            tournaments.add(tournament);
        }
        return tournaments;
    }

    private Tournament mapRowToTournament(SqlRowSet rs) {
        Tournament tournament = new Tournament();
        tournament.setNumberOfParticipants(rs.getInt("number_of_participants"));
        tournament.setTournamentDate(rs.getDate("tournament_date").toLocalDate());
        tournament.setTournamentId(rs.getInt("tournament_id"));
        tournament.setTournamentName(rs.getString("tournament_name"));
        tournament.setOrganizerId(rs.getInt("organizer_id"));
        return tournament;
    }




}
