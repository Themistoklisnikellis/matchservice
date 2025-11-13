package com.example.matchservice.service;

import com.example.matchservice.model.MatchOdds;
import com.example.matchservice.repository.MatchOddsRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;
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

    public MatchOdds getOddById(Long id) {
        return matchOddsRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    public void createOdd(MatchOdds odd) {
        matchOddsRepository.save(odd);
    }

    public void createOddsBatch(List<MatchOdds> odds) {
        matchOddsRepository.saveAll(odds);
    }

    public void patchOdd(Long id, Map<String, Object> updates) {
        MatchOdds odd = matchOddsRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));

        // Only allow updating "odd" field
        if (updates.size() != 1 || !updates.containsKey("odd")) {
            throw new IllegalArgumentException("Only 'odd' field can be modified.");
        }

        odd.setOdd(Double.valueOf(updates.get("odd").toString()));
        matchOddsRepository.save(odd);
    }

    public void deleteOdd(Long id) {
        if (!matchOddsRepository.existsById(id)) {
            throw new EmptyResultDataAccessException(1);
        }
        matchOddsRepository.deleteById(id);
    }
}
