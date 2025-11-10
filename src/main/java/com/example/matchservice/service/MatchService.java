package com.example.matchservice.service;

import com.example.matchservice.model.Match;
import com.example.matchservice.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public List<Match> createMatchesBatch(List<Match> matches) {
        return matchRepository.saveAll(matches);
    }

    public boolean updateMatch(Long id, Match updatedMatch) {
        return matchRepository.findById(id).map(match -> {
            match.setDescription(updatedMatch.getDescription());
            match.setMatch_date(updatedMatch.getMatch_date());
            match.setMatch_time(updatedMatch.getMatch_time());
            match.setTeam_a(updatedMatch.getTeam_a());
            match.setTeam_b(updatedMatch.getTeam_b());
            match.setSport(updatedMatch.getSport());
            matchRepository.save(match);
            return true;
        }).orElse(false);
    }

    public boolean patchMatch(Long id, Map<String, Object> updates) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return matchRepository.findById(id).map(match -> {
            try {
                if (updates.containsKey("description")) {
                    match.setDescription((String) updates.get("description"));
                }

                if (updates.containsKey("match_date")) {
                    String dateStr = (String) updates.get("match_date");
                    LocalDate date = LocalDate.parse(dateStr, dateFormatter);
                    match.setMatch_date(date);
                }

                if (updates.containsKey("match_time")) {
                    String timeStr = (String) updates.get("match_time");
                    LocalTime time = LocalTime.parse(timeStr, timeFormatter);
                    match.setMatch_time(time);
                }

                if (updates.containsKey("team_a")) {
                    match.setTeam_a((String) updates.get("team_a"));
                }

                if (updates.containsKey("team_b")) {
                    match.setTeam_b((String) updates.get("team_b"));
                }

                if (updates.containsKey("sport")) {
                    match.setSport(Match.Sport.valueOf((String) updates.get("sport")));
                }

                matchRepository.save(match);
                return true;

            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date or time format: " + e.getParsedString());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid sport value: " + updates.get("sport"));
            }
        }).orElse(false);
    }

    public boolean deleteMatch(Long id) {
        if (matchRepository.existsById(id)) {
            matchRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
