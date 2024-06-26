package ru.chainichek.neostudy.statement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.statement.dto.util.ErrorMessage;
import ru.chainichek.neostudy.statement.dto.util.InternalErrorMessage;
import ru.chainichek.neostudy.statement.exception.ForbiddenException;
import ru.chainichek.neostudy.statement.exception.NotFoundException;
import ru.chainichek.neostudy.statement.exception.ValidationException;
import ru.chainichek.neostudy.statement.exception.WrongStatusException;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeignConfigTest {
    @InjectMocks
    FeignConfig.RestMessageErrorDecoder errorDecoder;
    @Mock
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        errorDecoder = new FeignConfig.RestMessageErrorDecoder(mapper);
    }

    @SneakyThrows
    @Test
    void restMessageErrorDecoderDecode_whenResponseStatusIsBadRequest_thenReturnValidationException() {
        Response response = mock(Response.class);
        Response.Body body = mock(Response.Body.class);
        InputStream inputStream = mock(InputStream.class);

        when(response.status()).thenReturn(400);
        when(response.body()).thenReturn(body);
        when(body.asInputStream()).thenReturn(inputStream);

        ErrorMessage errorMessage = mock(ErrorMessage.class);

        when(mapper.readValue(inputStream, ErrorMessage.class)).thenReturn(errorMessage);

        Exception exception = errorDecoder.decode(null, response);

        assertNotNull(exception);
        assertThat(exception).isInstanceOf(ValidationException.class);
    }


    @SneakyThrows
    @Test
    void restMessageErrorDecoderDecode_whenResponseStatusIsForbidden_thenReturnForbiddenException() {
        Response response = mock(Response.class);
        Response.Body body = mock(Response.Body.class);
        InputStream inputStream = mock(InputStream.class);

        when(response.status()).thenReturn(403);
        when(response.body()).thenReturn(body);
        when(body.asInputStream()).thenReturn(inputStream);

        ErrorMessage errorMessage = mock(ErrorMessage.class);

        when(mapper.readValue(inputStream, ErrorMessage.class)).thenReturn(errorMessage);

        Exception exception = errorDecoder.decode(null, response);

        assertNotNull(exception);
        assertThat(exception).isInstanceOf(ForbiddenException.class);
    }

    @SneakyThrows
    @Test
    void restMessageErrorDecoderDecode_whenResponseStatusIsNotFound_thenReturnNotFoundException() {
        Response response = mock(Response.class);
        Response.Body body = mock(Response.Body.class);
        InputStream inputStream = mock(InputStream.class);

        when(response.status()).thenReturn(404);
        when(response.body()).thenReturn(body);
        when(body.asInputStream()).thenReturn(inputStream);

        ErrorMessage errorMessage = mock(ErrorMessage.class);

        when(mapper.readValue(inputStream, ErrorMessage.class)).thenReturn(errorMessage);

        Exception exception = errorDecoder.decode(null, response);

        assertNotNull(exception);
        assertThat(exception).isInstanceOf(NotFoundException.class);
    }

    @SneakyThrows
    @Test
    void restMessageErrorDecoderDecode_whenResponseStatusIsConflict_thenReturnWrongStatusException() {
        Response response = mock(Response.class);
        Response.Body body = mock(Response.Body.class);
        InputStream inputStream = mock(InputStream.class);

        when(response.status()).thenReturn(412);
        when(response.body()).thenReturn(body);
        when(body.asInputStream()).thenReturn(inputStream);

        ErrorMessage errorMessage = mock(ErrorMessage.class);

        when(mapper.readValue(inputStream, ErrorMessage.class)).thenReturn(errorMessage);

        Exception exception = errorDecoder.decode(null, response);

        assertNotNull(exception);
        assertThat(exception).isInstanceOf(WrongStatusException.class);
    }

    @SneakyThrows
    @Test
    void restMessageErrorDecoderDecode_whenResponseStatusIsInternalServerError_thenReturnRuntimeException() {
        Response response = mock(Response.class);
        Response.Body body = mock(Response.Body.class);
        InputStream inputStream = mock(InputStream.class);

        when(response.status()).thenReturn(500);
        when(response.body()).thenReturn(body);
        when(body.asInputStream()).thenReturn(inputStream);

        InternalErrorMessage errorMessage = mock(InternalErrorMessage.class);

        when(mapper.readValue(inputStream, InternalErrorMessage.class)).thenReturn(errorMessage);

        Exception exception = errorDecoder.decode(null, response);

        assertNotNull(exception);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @SneakyThrows
    @Test
    void restMessageErrorDecoderDecode_whenResponseStatusIsMoreThan400AndUnknown_thenReturnException() {
        ErrorDecoder decoder = mock(ErrorDecoder.class);

        try {
            var decoderField = FeignConfig.RestMessageErrorDecoder.class.getDeclaredField("errorDecoder");
            decoderField.setAccessible(true);
            decoderField.set(errorDecoder, decoder);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Response response = mock(Response.class);
        Response.Body body = mock(Response.Body.class);
        InputStream inputStream = mock(InputStream.class);

        when(response.status()).thenReturn(499);
        when(response.body()).thenReturn(body);
        when(body.asInputStream()).thenReturn(inputStream);

        ErrorMessage errorMessage = mock(ErrorMessage.class);

        when(mapper.readValue(inputStream, ErrorMessage.class)).thenReturn(errorMessage);
        when(decoder.decode(null, response)).thenReturn(new Exception());

        Exception exception = errorDecoder.decode(null, response);

        assertNotNull(exception);
        assertThat(exception).isInstanceOf(Exception.class);
    }

    @SneakyThrows
    @Test
    void restMessageErrorDecoderDecode_whenResponseStatusIsLessThan400AndUnknown_thenReturnException() {
        ErrorDecoder decoder = mock(ErrorDecoder.class);

        try {
            var decoderField = FeignConfig.RestMessageErrorDecoder.class.getDeclaredField("errorDecoder");
            decoderField.setAccessible(true);
            decoderField.set(errorDecoder, decoder);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Response response = mock(Response.class);

        when(response.status()).thenReturn(-1);

        when(decoder.decode(null, response)).thenReturn(new Exception());

        Exception exception = errorDecoder.decode(null, response);

        assertNotNull(exception);
        assertThat(exception).isInstanceOf(Exception.class);
    }

    @SneakyThrows
    @Test
    void restMessageErrorDecoderDecode_whenResponseStatusIsInternalServerErrorAndBodyAsInputStreamThrowsIOException_thenReturnIOException() {
        Response response = mock(Response.class);
        Response.Body body = mock(Response.Body.class);

        when(response.status()).thenReturn(500);
        when(response.body()).thenReturn(body);

        IOException e = mock(IOException.class);
        when(body.asInputStream()).thenThrow(e);

        Exception exception = errorDecoder.decode(null, response);

        assertNotNull(exception);
        assertThat(exception).isInstanceOf(IOException.class);
        assertEquals(e, exception);
    }

    @SneakyThrows
    @Test
    void restMessageErrorDecoderDecode_whenResponseStatusIsInternalServerErrorAndMapperReadValueThrowsIOException_thenReturnIOException() {
        Response response = mock(Response.class);
        Response.Body body = mock(Response.Body.class);
        InputStream inputStream = mock(InputStream.class);

        when(response.status()).thenReturn(500);
        when(response.body()).thenReturn(body);
        when(body.asInputStream()).thenReturn(inputStream);

        IOException e = mock(IOException.class);
        when(mapper.readValue(inputStream, InternalErrorMessage.class)).thenThrow(e);

        Exception exception = errorDecoder.decode(null, response);

        assertNotNull(exception);
        assertThat(exception).isInstanceOf(IOException.class);
        assertEquals(e, exception);
    }

    @SneakyThrows
    @Test
    void restMessageErrorDecoderDecode_whenResponseStatusIsClientServerErrorAndBodyAsInputStreamThrowsIOException_thenReturnIOException() {
        Response response = mock(Response.class);
        Response.Body body = mock(Response.Body.class);

        when(response.status()).thenReturn(400);
        when(response.body()).thenReturn(body);

        IOException e = mock(IOException.class);
        when(body.asInputStream()).thenThrow(e);

        Exception exception = errorDecoder.decode(null, response);

        assertNotNull(exception);
        assertThat(exception).isInstanceOf(IOException.class);
        assertEquals(e, exception);
    }

    @SneakyThrows
    @Test
    void restMessageErrorDecoderDecode_whenResponseStatusIsClientServerErrorAndMapperReadValueThrowsIOException_thenReturnIOException() {
        Response response = mock(Response.class);
        Response.Body body = mock(Response.Body.class);
        InputStream inputStream = mock(InputStream.class);

        when(response.status()).thenReturn(400);
        when(response.body()).thenReturn(body);
        when(body.asInputStream()).thenReturn(inputStream);

        IOException e = mock(IOException.class);
        when(mapper.readValue(inputStream, ErrorMessage.class)).thenThrow(e);

        Exception exception = errorDecoder.decode(null, response);

        assertNotNull(exception);
        assertThat(exception).isInstanceOf(IOException.class);
        assertEquals(e, exception);
    }
}