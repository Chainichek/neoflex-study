package ru.chainichek.neostudy.calculator.service.calculation;

import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;

import java.math.BigDecimal;

public interface ScoreRateCalculator {
    BigDecimal execute(ScoringDataDto scoringData);
}
