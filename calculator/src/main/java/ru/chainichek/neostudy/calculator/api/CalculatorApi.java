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
import ru.chainichek.neostudy.calculator.dto.util.InternalErrorMessage;

import java.util.List;

@RequestMapping("/calculator")
@Tag(name = "Calculator", description = "Calculates loan terms and schedule")
public interface CalculatorApi {

    @Operation(summary = "Calculation of possible loan terms",
            description = "Accepts a loan application and returns a list of possible loan terms")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "List of possible loan terms",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = LoanOfferDto.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid request data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = InternalErrorMessage.class)
                            )
                    )
            }
    )
    @PostMapping("/offers")
    ResponseEntity<List<LoanOfferDto>> getOffers(@RequestBody @Valid @NotNull LoanStatementRequestDto loanStatementRequest);

    @Operation(summary = "Validation of submitted data + full calculation of loan parameters",
            description = "Validates the loan application data and performs a full calculation of loan parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Loan parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreditDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Loan application rejection",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = InternalErrorMessage.class)
                    )
            )
    })
    @PostMapping("/calc")
    ResponseEntity<CreditDto> calculateCredit(@RequestBody @Valid @NotNull ScoringDataDto scoringData);
}
