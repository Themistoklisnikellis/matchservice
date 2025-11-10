package com.example.matchservice.service;

import com.example.matchservice.model.Match;
import com.example.matchservice.model.MatchOdds;
import com.example.matchservice.repository.MatchOddsRepository;
import org.springframework.dao.DataIntegrityViolationException;
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

    public MatchOdds createOdd(MatchOdds odd) {
        try {
            return matchOddsRepository.save(odd);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Duplicate odd or invalid match_id.");
        }
    }

    public List<MatchOdds> createOddsBatch(List<MatchOdds> odds) {
        try {
            return matchOddsRepository.saveAll(odds);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Duplicate odd or invalid match_id.");
        }
    }

    public boolean updateOdd(Long id, MatchOdds updatedOdd) {
        return matchOddsRepository.findById(id).map(odd -> {
            odd.setSpecifier(updatedOdd.getSpecifier());
            odd.setOdd(updatedOdd.getOdd());
            odd.setMatch(updatedOdd.getMatch());
            matchOddsRepository.save(odd);
            return true;
        }).orElse(false);
    }

    public boolean patchOdd(Long id, Map<String, Object> updates) {
        return matchOddsRepository.findById(id).map(odd -> {
            if (updates.containsKey("specifier")) {
                odd.setSpecifier((String) updates.get("specifier"));
            }
            if (updates.containsKey("odd")) {
                odd.setOdd(Double.valueOf(updates.get("odd").toString()));
            }
            matchOddsRepository.save(odd);
            return true;
        }).orElse(false);
    }

    public boolean deleteOdd(Long id) {
        if (matchOddsRepository.existsById(id)) {
            matchOddsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
