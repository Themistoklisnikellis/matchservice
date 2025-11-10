package com.example.matchservice.controller;

import com.example.matchservice.model.Match;
import com.example.matchservice.model.MatchOdds;
import com.example.matchservice.service.MatchService;
import com.example.matchservice.service.MatchOddsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // --- MATCH ENDPOINTS ---
    @GetMapping("/matches")
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/matches/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable Long id) {
        return matchService.getMatchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/matches", consumes = "application/json")
    public ResponseEntity<Map<String, String>> createMatch(@Valid @RequestBody Match match) {
        matchService.createMatch(match);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Match created successfully"));
    }

    @PostMapping(value = "/matches/batch", consumes = "application/json")
    public ResponseEntity<Map<String, String>> createMatchesBatch(@Valid @RequestBody List<Match> matches) {
        matchService.createMatchesBatch(matches);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Matches created successfully"));
    }

    @PutMapping(value = "/matches/{id}", consumes = "application/json")
    public ResponseEntity<Map<String, String>> updateMatch(@PathVariable Long id, @Valid @RequestBody Match match) {
        boolean updated = matchService.updateMatch(id, match);
        if (updated) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Match updated successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", "Match not found"));
        }
    }

    @PatchMapping(value = "/matches/{id}", consumes = "application/json")
    public ResponseEntity<Map<String, String>> patchMatch(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        boolean updated = matchService.patchMatch(id, updates);
        if (updated) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Match partially updated successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", "Match not found"));
        }
    }

    @DeleteMapping("/matches/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        return matchService.deleteMatch(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // --- MATCH ODDS ENDPOINTS ---

    @GetMapping("/odds")
    public List<MatchOdds> getAllOdds() {
        return matchOddsService.getAllOdds();
    }

    @GetMapping("/odds/{id}")
    public ResponseEntity<MatchOdds> getOddById(@PathVariable Long id) {
        return matchOddsService.getOddById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/odds", consumes = "application/json")
    public ResponseEntity<Map<String, String>> createOdd(@Valid @RequestBody MatchOdds odd) {
        try {
            matchOddsService.createOdd(odd);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Match odd created successfully"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping(value = "/odds/batch", consumes = "application/json")
    public ResponseEntity<Map<String, String>> createOddsBatch(@Valid @RequestBody List<MatchOdds> oddsList) {
        try {
            matchOddsService.createOddsBatch(oddsList);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Match odds created successfully"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PutMapping(value = "/odds/{id}", consumes = "application/json")
    public ResponseEntity<Map<String, String>> updateOdd(@PathVariable Long id, @Valid @RequestBody MatchOdds odd) {
        boolean updated = matchOddsService.updateOdd(id, odd);
        if (updated) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Match odd updated successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", "Match odd not found"));
        }
    }

    @PatchMapping(value = "/odds/{id}", consumes = "application/json")
    public ResponseEntity<Map<String, String>> patchOdd(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        boolean updated = matchOddsService.patchOdd(id, updates);
        if (updated) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Match odd partially updated successfully"));
        } else {
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", "Match odd not found"));
        }
    }

    @DeleteMapping("/odds/{id}")
    public ResponseEntity<Void> deleteOdd(@PathVariable Long id) {
        return matchOddsService.deleteOdd(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}

