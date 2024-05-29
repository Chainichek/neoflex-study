package ru.chainichek.neostudy.calculator.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.domain.entity.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.domain.entity.dto.score.ScoringDataDto;

@Service
@AllArgsConstructor
public class ScoreUseCase {
    private final ValidateScoringDataUseCase scoringDataValidator;

    public CreditDto execute(final ScoringDataDto scoringData) {
        scoringDataValidator.execute(scoringData);

    }
}
