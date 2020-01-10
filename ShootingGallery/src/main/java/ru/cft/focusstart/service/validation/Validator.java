package ru.cft.focusstart.service.validation;

import ru.cft.focusstart.exception.InvalidParametersException;

public final class Validator {

    private Validator() {}

    public static void checkNotNull(String parameterName, Object value) throws InvalidParametersException {
        if (value == null) {
            throw new InvalidParametersException(String.format("Parameter '%s' must be specified", parameterName));
        }
    }

    public static void checkNull(String parameterName, Object value) throws InvalidParametersException {
        if (value != null) {
            throw new InvalidParametersException(String.format("Parameter '%s' must be not specified", parameterName));
        }
    }

    public static void checkSize(String parameterName, String value, int minSize, int maxSize) throws InvalidParametersException {
        checkNotNull(parameterName, value);

        if (value.length() < minSize || value.length() > maxSize) {
            throw new InvalidParametersException(
                    String.format(
                            "Parameter '%s' must contain at least %d characters and no more than %d characters",
                            parameterName,
                            minSize,
                            maxSize
                    )
            );
        }
    }
}
