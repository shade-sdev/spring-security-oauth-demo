package mu.elca.brownbag.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import shade.dev.local.exception.model.WebErrorCode;

@Getter
@RequiredArgsConstructor
public enum HttpErrorCode implements WebErrorCode {

    NOT_FOUND("NOT_FOUND", HttpStatus.NOT_FOUND);

    private final String code;
    private final HttpStatus status;

}
