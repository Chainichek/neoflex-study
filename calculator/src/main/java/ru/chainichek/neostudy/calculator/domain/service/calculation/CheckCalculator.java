package ru.chainichek.neostudy.calculator.domain.service.calculation;

import ru.chainichek.neostudy.calculator.domain.entity.dto.score.ScoringDataDto;

public interface CheckCalculator {
    void execute(ScoringDataDto scoringData);
}
