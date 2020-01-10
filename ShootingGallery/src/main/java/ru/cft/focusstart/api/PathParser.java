package ru.cft.focusstart.api;

import ru.cft.focusstart.exception.ObjectNotFoundException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class PathParser {

    private PathParser() {}

    static Long getPathPart(String path, String pattern, String parameterName) {
        Matcher matcher = Pattern.compile(pattern).matcher(path);
        if (!matcher.find()) {
            throw new ObjectNotFoundException("Object not found");
        }
        try {
            return Long.parseLong(matcher.group(parameterName));
        } catch (Exception e) {
            throw new ObjectNotFoundException("Invalid object id");
        }
    }
}
