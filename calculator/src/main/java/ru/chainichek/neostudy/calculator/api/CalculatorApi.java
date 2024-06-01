package ru.chainichek.neostudy.calculator.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.calculator.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.calculator.dto.score.CreditDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.dto.util.ErrorMessage;

import java.util.List;

@RequestMapping("/calculator")
@Tag(name = "Calculator", description = "Вычисляет условия и график кредитования")
public interface CalculatorApi {

    @Operation(summary = "Расчёт возможных условий кредита",
            description = "Принимает заявку на кредит и возвращает список возможных условий кредитования")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Список возможных условий кредита",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = LoanOfferDto.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Некорректные данные запроса",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @PostMapping("/offers")
    ResponseEntity<List<LoanOfferDto>> getOffers(@RequestBody @Valid @NotNull LoanStatementRequestDto loanStatementRequest);

    @Operation(summary = "Валидация присланных данных + полный расчет параметров кредита",
            description = "Проводит валидацию данных заявки на кредит и выполняет полный расчет параметров кредита")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Параметры кредита",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreditDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/calc")
    ResponseEntity<CreditDto> calculateCredit(@RequestBody @Valid @NotNull ScoringDataDto scoringData);
}
