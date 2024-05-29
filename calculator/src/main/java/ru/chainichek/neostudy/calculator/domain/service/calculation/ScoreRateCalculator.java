package ru.chainichek.neostudy.calculator.domain.service.calculation;

import ru.chainichek.neostudy.calculator.domain.entity.dto.score.ScoringDataDto;

import java.math.BigDecimal;

public interface ScoreRateCalculator {
    BigDecimal execute(ScoringDataDto scoringData);
}
