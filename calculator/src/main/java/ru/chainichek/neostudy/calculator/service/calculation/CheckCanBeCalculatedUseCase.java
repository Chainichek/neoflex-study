package ru.chainichek.neostudy.calculator.service.calculation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.model.EmploymentStatus;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.exception.ForbiddenException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.Period;

@Service
public class CheckCanBeCalculatedUseCase implements CheckCalculator {
    private final MathContext calculationMathContext;

    public CheckCanBeCalculatedUseCase(final @Qualifier("calculationMathContext") MathContext calculationMathContext) {
        this.calculationMathContext = calculationMathContext;
    }

    @Override
    public void execute(final ScoringDataDto scoringData) {
        if (scoringData.employment().employmentStatus() == EmploymentStatus.UNEMPLOYED) {
            throw new ForbiddenException("Cannot offer a loan for unemployed");
        }

        if (scoringData.employment().salary()
                    .multiply(BigDecimal.valueOf(25), calculationMathContext)
                    .compareTo(scoringData.amount()) < 0) {
            throw new ForbiddenException("Cannot offer a loan whose amount exceeds 25 salaries");
        }

        final int age = Period.between(scoringData.birthdate(), LocalDate.now()).getYears();
        if (age > 65 || age < 20) {
            throw new ForbiddenException("Cannot offer a loan to those under 20 or over 65");
        }

        if (scoringData.employment().workExperienceTotal() < 18 || scoringData.employment().workExperienceCurrent() < 3) {
            throw new ForbiddenException("Cannot offer a loan to those whose total experience is less 18 months or whose current experience is less 3 months");
        }
    }
}
