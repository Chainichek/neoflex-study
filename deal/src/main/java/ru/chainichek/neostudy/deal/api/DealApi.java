package ru.chainichek.neostudy.deal.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;
import ru.chainichek.neostudy.deal.dto.util.ErrorMessage;
import ru.chainichek.neostudy.deal.dto.util.InternalErrorMessage;

import java.util.List;
import java.util.UUID;

@RequestMapping("/deal")
public interface DealApi {
    @Operation(
            summary = "Calculation of possible loan terms",
            description = "Calculates possible loan terms based on the provided request data"
    )
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
    @PostMapping("/statement")
    ResponseEntity<List<LoanOfferDto>> createStatement(@RequestBody @NotNull LoanStatementRequestDto loanStatementRequest);

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
                            responseCode = "400",
                            description = "Statement has already been approved",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)
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
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = InternalErrorMessage.class)
                            )
                    )
            }
    )
    @PostMapping("/offer/select")
    ResponseEntity<Void> selectOffer(@RequestBody @NotNull LoanOfferDto loanOffer);

    @Operation(
            summary = "Completion of registration and full loan calculation",
            description = "Completes the registration process and performs the full loan calculation."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful offer selection",
                    content = @Content(
                            schema = @Schema()
                    )
            ),

            @ApiResponse(responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Statement was rejected",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Statement has not been already approved or has been already calculated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Statement was not found",
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
    })
    @PostMapping("/calculate/{statementId}")
    ResponseEntity<Void> completeStatement(@PathVariable("statementId") UUID statementId,
                                           @RequestBody @NotNull FinishRegistrationRequestDto finishRegistrationRequest);
}
