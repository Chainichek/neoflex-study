package ru.chainichek.neostudy.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.chainichek.neostudy.deal.dto.calculation.CreditDto;
import ru.chainichek.neostudy.deal.model.credit.Credit;

@Mapper
public interface CreditMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creditStatus", ignore = true)
    Credit mapToCredit(CreditDto creditDto);
}
