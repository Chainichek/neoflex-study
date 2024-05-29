package ru.chainichek.neostudy.calculator.service.calculation;

import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;

public interface CheckCalculator {
    void execute(ScoringDataDto scoringData);
}
