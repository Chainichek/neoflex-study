package ru.chainichek.neostudy.deal.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.chainichek.neostudy.deal.dto.util.ErrorMessage;
import ru.chainichek.neostudy.deal.dto.util.InternalErrorMessage;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;

import java.util.UUID;

@RequestMapping("/deal/admin")
public interface AdminApi {
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
//            @ApiResponse(responseCode = "403",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = ErrorMessage.class)
//                    )
//            ),
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
                                      @RequestParam("status") ApplicationStatus status);
}
