package com.example.matchservice.service;

import com.example.matchservice.model.MatchOdds;
import com.example.matchservice.repository.MatchOddsRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class MatchOddsService {

    private final MatchOddsRepository matchOddsRepository;

    public MatchOddsService(MatchOddsRepository matchOddsRepository) {
        this.matchOddsRepository = matchOddsRepository;
    }

    public List<MatchOdds> getAllOdds() {
        return matchOddsRepository.findAll();
    }

    public Optional<MatchOdds> getOddById(Long id) {
        return matchOddsRepository.findById(id);
    }

    public void createOdd(MatchOdds odd) {
        try {
            matchOddsRepository.save(odd);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Duplicate odd or invalid match_id");
        }
    }

    public void createOddsBatch(List<MatchOdds> odds) {
        try {
            matchOddsRepository.saveAll(odds);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Duplicate odd or invalid match_id in batch");
        }
    }

    public void patchOdd(Long id, Map<String, Object> updates) {
        MatchOdds odd = matchOddsRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Match odd not found", 1));

        // Only allow updating "odd" field
        if (updates.size() != 1 || !updates.containsKey("odd")) {
            throw new IllegalArgumentException("Only 'odd' field can be modified.");
        }

        try {
            odd.setOdd(Double.valueOf(updates.get("odd").toString()));
            matchOddsRepository.save(odd);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid value for odd. Must be a number.");
        }
    }

    public void deleteOdd(Long id) {
        if (!matchOddsRepository.existsById(id)) {
            throw new EmptyResultDataAccessException(1);
        }
        matchOddsRepository.deleteById(id);
    }
}
