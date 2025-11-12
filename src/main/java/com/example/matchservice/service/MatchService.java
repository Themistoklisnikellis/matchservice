package com.example.matchservice.service;

import com.example.matchservice.model.Match;
import com.example.matchservice.repository.MatchRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    public void createMatch(Match match) {
        matchRepository.save(match);
    }

    public void createMatchesBatch(List<Match> matches) {
        matchRepository.saveAll(matches);
    }

    public void updateMatch(Long id, Match updatedMatch) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Match not found", 1));

        match.setDescription(updatedMatch.getDescription());
        match.setMatch_date(updatedMatch.getMatch_date());
        match.setMatch_time(updatedMatch.getMatch_time());
        match.setTeam_a(updatedMatch.getTeam_a());
        match.setTeam_b(updatedMatch.getTeam_b());
        match.setSport(updatedMatch.getSport());

        matchRepository.save(match);
    }

    public void patchMatch(Long id, Map<String, Object> updates) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Find match or throw 404
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Match not found", 1));

        // Update fields if present
        if (updates.containsKey("description")) {
            match.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("match_date")) {
            String dateStr = (String) updates.get("match_date");
            match.setMatch_date(LocalDate.parse(dateStr, dateFormatter)); // may throw DateTimeParseException
        }
        if (updates.containsKey("match_time")) {
            String timeStr = (String) updates.get("match_time");
            match.setMatch_time(LocalTime.parse(timeStr, timeFormatter)); // may throw DateTimeParseException
        }
        if (updates.containsKey("team_a")) {
            match.setTeam_a((String) updates.get("team_a"));
        }
        if (updates.containsKey("team_b")) {
            match.setTeam_b((String) updates.get("team_b"));
        }
        if (updates.containsKey("sport")) {
            match.setSport(Match.Sport.valueOf((String) updates.get("sport"))); // may throw IllegalArgumentException
        }

        matchRepository.save(match);
    }

    public void deleteMatch(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new EmptyResultDataAccessException(1);
        }
        matchRepository.deleteById(id);
    }
}
