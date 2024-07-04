package ru.chainichek.neostudy.deal.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.chainichek.neostudy.deal.dto.util.ErrorMessage;
import ru.chainichek.neostudy.deal.dto.util.InternalErrorMessage;

import java.util.UUID;

@RequestMapping("/deal/document")
public interface DocumentApi {
    @Operation(
            summary = "Creating a request to send documents",
            description = "Produces the request for sending loan offer documents."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful request production",
                    content = @Content(
                            schema = @Schema()
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Statement was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(responseCode = "412",
                    description = "Statement has not been already calculated",
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
    @PostMapping("/{statementId}/send")
    ResponseEntity<Void> sendDocuments(@PathVariable("statementId") UUID statementId);

    @Operation(
            summary = "Creating a request to sign documents",
            description = "Produces the request for signing loan offer documents."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful request production",
                    content = @Content(
                            schema = @Schema()
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Statement was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(responseCode = "412",
                    description = "Statement documents has not been already requested to send",
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
    @PostMapping("/{statementId}/sign")
    ResponseEntity<Void> signDocuments(@PathVariable("statementId") UUID statementId);

    @Operation(
            summary = "Signing loan offer documents",
            description = "Verifies client sign and completes loan offer creation."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful document signing",
                    content = @Content(
                            schema = @Schema()
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "Code is invalid",
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
            @ApiResponse(responseCode = "412",
                    description = "Statement documents has not been already requested to sign",
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
    @PostMapping("/{statementId}/code")
    ResponseEntity<Void> verifyCode(@PathVariable("statementId") UUID statementId,
                                    @RequestParam("code") String code);
}
