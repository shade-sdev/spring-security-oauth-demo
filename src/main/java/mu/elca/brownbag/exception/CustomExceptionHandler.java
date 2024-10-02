package mu.elca.brownbag.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import shade.dev.local.exception.WebCoreException;

@ControllerAdvice(basePackages = "mu.elca.brownbag")
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(WebCoreException.class)
    public ResponseEntity<ProblemDetail> handleWebCoreException(WebCoreException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getMessage());
        problemDetail.setTitle(ex.getCode());
        return ResponseEntity.status(ex.getStatusCode()).body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex,
                                                             @Nullable Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatusCode statusCode,
                                                             @NonNull WebRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(statusCode, ex.getMessage());
        problemDetail.setTitle("INTERNAL_ERROR");
        return ResponseEntity.status(statusCode).body(problemDetail);
    }

}
