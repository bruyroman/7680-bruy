package ru.cft.focusstart.service.validation;

import ru.cft.focusstart.entity.InstructorCategory;
import ru.cft.focusstart.exception.InvalidParametersException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static void checkCategory(String parameterName, String category) throws InvalidParametersException {
        try {
            if (category != null && category.length() > 0) {
                InstructorCategory.valueOf(category);
            }
        } catch (Exception e) {
            List<String> categories = Arrays.stream(InstructorCategory.values()).map(x -> x.getName()).collect(Collectors.toList());
            throw new InvalidParametersException(
                    String.format("Parameter '%s' must match one of the categories: %s",
                            parameterName,
                            String.join(",", categories)
                    )
            );
        }
    }

    public static void checkLocalDateTime(String parameterName, String dateTime) throws InvalidParametersException {
        try {
            LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            throw new InvalidParametersException(String.format("Parameter '%s' is invalid", parameterName));
        }
    }
}
