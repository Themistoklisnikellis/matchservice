package com.example.matchservice.controller;

import com.example.matchservice.model.Match;
import com.example.matchservice.model.MatchOdds;
import com.example.matchservice.service.MatchService;
import com.example.matchservice.service.MatchOddsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MatchController {

    private final MatchService matchService;
    private final MatchOddsService matchOddsService;

    // Match endpoints
    @GetMapping("/matches")
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/matches/{id}")
    public Match getMatchById(@PathVariable Long id) {
        return matchService.getMatchById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Match not found", 1));
    }

    @PostMapping(value = "/matches", consumes = "application/json")
    public ResponseEntity<Map<String, String>> createMatch(@Valid @RequestBody Match match) {
        matchService.createMatch(match);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Match created successfully"
        ));
    }


    @PostMapping(value = "/matches/batch", consumes = "application/json")
    public ResponseEntity<Map<String, String>> createMatchesBatch(@Valid @RequestBody List<Match> matches) {
        matchService.createMatchesBatch(matches);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Matches created successfully"
        ));
    }

    @PutMapping(value = "/matches/{id}", consumes = "application/json")
    public ResponseEntity<Map<String, String>> updateMatch(@PathVariable Long id, @Valid @RequestBody Match match) {
        matchService.updateMatch(id, match);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Match updated successfully"
        ));
    }

    @PatchMapping(value = "/matches/{id}", consumes = "application/json")
    public ResponseEntity<Map<String, String>> patchMatch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        matchService.patchMatch(id, updates);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Match partially updated successfully"
        ));
    }

    @DeleteMapping("/matches/{id}")
    public ResponseEntity<Map<String, String>> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Match deleted successfully"
        ));
    }

    // Match odds endpoints

    @GetMapping("/odds")
    public List<MatchOdds> getAllOdds() {
        return matchOddsService.getAllOdds();
    }

    @GetMapping("/odds/{id}")
    public MatchOdds getOddById(@PathVariable Long id) {
        return matchOddsService.getOddById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Match odd not found", 1));
    }

    @PostMapping(value = "/odds", consumes = "application/json")
    public ResponseEntity<Map<String, String>> createOdd(@Valid @RequestBody MatchOdds odd) {
        matchOddsService.createOdd(odd);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Odd created successfully"
        ));
    }

    @PostMapping(value = "/odds/batch", consumes = "application/json")
    public ResponseEntity<Map<String, String>> createOddsBatch(@Valid @RequestBody List<MatchOdds> oddsList) {
        matchOddsService.createOddsBatch(oddsList);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Odds created successfully"
        ));
    }

    @PatchMapping(value = "/odds/{id}", consumes = "application/json")
    public ResponseEntity<Map<String, String>> patchOdd(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        matchOddsService.patchOdd(id, updates);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Odd updated successfully"
        ));
    }

    @DeleteMapping("/odds/{id}")
    public ResponseEntity<Map<String, String>> deleteOdd(@PathVariable Long id) {
        matchOddsService.deleteOdd(id);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Odd deleted successfully"
        ));
    }

}

