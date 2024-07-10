package ru.chainichek.neostudy.deal.mapper;

import org.mapstruct.Mapper;
import ru.chainichek.neostudy.deal.dto.admin.StatementDto;
import ru.chainichek.neostudy.deal.model.statement.Statement;

@Mapper
public interface AdminMapper {
    StatementDto mapToStatementDto(Statement statement);
}
