package mu.elca.brownbag.exception;

import shade.dev.local.exception.WebCoreException;
import shade.dev.local.exception.model.WebErrorCode;

public class NotFoundException extends WebCoreException {

    public NotFoundException(Class<?> clazz) {
        super("not found", clazz);
    }

    @Override
    protected WebErrorCode getErrorCode() {
        return HttpErrorCode.NOT_FOUND;
    }

}
