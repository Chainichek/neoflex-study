package ru.chainichek.neostudy.deal.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.chainichek.neostudy.deal.dto.admin.StatementDto;
import ru.chainichek.neostudy.deal.dto.util.ErrorMessage;
import ru.chainichek.neostudy.deal.dto.util.InternalErrorMessage;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.util.UUID;

@RequestMapping("/deal/admin")
@SecurityRequirement(name = "X_API_KEY")
public interface AdminApi {
    @Operation(
            summary = "Getting statement by id",
            description = "Returns statement entity by following id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful statement return",
                    content = @Content(
                            schema = @Schema(implementation = Statement.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(responseCode = "403",
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
    @GetMapping("/statement/{statementId}")
    ResponseEntity<StatementDto> getStatement(@PathVariable("statementId") UUID statementId);
    @Operation(
            summary = "Updating statement status",
            description = "Updates statement status with following id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful status update",
                    content = @Content(
                            schema = @Schema()
                    )
            ),
            @ApiResponse(responseCode = "401",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(responseCode = "403",
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
    @PutMapping("/statement/{statementId}/status")
    ResponseEntity<Void> updateStatus(@PathVariable("statementId") UUID statementId,
                                      @RequestBody ApplicationStatus status);
}
