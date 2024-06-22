package ru.chainichek.neostudy.statement.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.chainichek.neostudy.statement.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.statement.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.statement.dto.util.InternalErrorMessage;

import java.util.List;

@RequestMapping("/statement")
@Tag(name = "Statement", description = "Validates and prescores loan offers")
public interface StatementApi {

    @Operation(summary = "Prescoring + request to calculate possible loan terms",
            description = "Calculates possible loan terms based on the provided request data")
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

    @Operation(
            summary = "Selection of one of the offers",
            description = "Allows selecting one of the proposed loan terms"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Successful offer selection",
                            content = @Content(
                                    schema = @Schema()
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Statement was not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "412",
                            description = "Statement has already been approved",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = InternalErrorMessage.class)
                            )
                    )
            }
    )
    @PostMapping("/offer")
    ResponseEntity<Void> selectOffer(@RequestBody @NotNull LoanOfferDto loanOffer);
}
