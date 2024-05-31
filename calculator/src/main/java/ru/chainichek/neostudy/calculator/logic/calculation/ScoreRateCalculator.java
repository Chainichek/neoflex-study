package ru.chainichek.neostudy.calculator.logic.calculation;

import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;

import java.math.BigDecimal;

public interface ScoreRateCalculator {
    BigDecimal calculateScoreRate(ScoringDataDto scoringData);
}
