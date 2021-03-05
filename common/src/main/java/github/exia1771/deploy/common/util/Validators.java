package github.exia1771.deploy.common.util;

import cn.hutool.core.util.StrUtil;

import javax.validation.ValidationException;

public abstract class Validators {

    private static final String NOT_NULL_MESSAGE = " 是必填项 ";
    private static final String NOT_BLANK_MESSAGE = " 不能由空字符组成 ";
    private static final String NOT_LENGTH_BETWEEN_MESSAGE = " 字符长度不在指定的区间内 ";
    private static final String NOT_MATCH_REGEX_MESSAGE = " 请输入正确的格式 ";

    public static void requireNotNull(String fieldName, Object field) {
        requireNotNull(fieldName, field, null);
    }

    public static void requireNotNull(String fieldName, Object field, String tip) {
        if (field == null) {
            String message = tip == null ? fieldName + NOT_NULL_MESSAGE : tip;
            throw new ValidationException(message);
        }
    }

    public static void requireNotBlank(String fieldName, String field) {
        requireNotBlank(fieldName, field, null);
    }

    public static void requireNotBlank(String fieldName, String field, String tip) {
        requireNotNull(fieldName, field);
        if (StrUtil.isBlank(field)) {
            String message = tip == null ? fieldName + NOT_BLANK_MESSAGE : tip;
            throw new ValidationException(message);
        }
    }

    public static void requireLength(String fieldName, String field, int min, int max) {
        requireLength(fieldName, field, min, max, null);
    }

    public static void requireLength(String fieldName, String field, int min, int max, String tip) {
        requireNotBlank(fieldName, field);
        if (!Strings.isLengthBetween(field, min, max)) {
            String defaultMessage = fieldName + NOT_LENGTH_BETWEEN_MESSAGE + ": [" + min + "," + max + "]";
            String message = tip == null ? defaultMessage : tip;
            throw new ValidationException(message);
        }
    }

    public static void requireMatchRegex(String fieldName, String field, String regex) {
        requireMatchRegex(fieldName, field, regex, null);
    }

    public static void requireMatchRegex(String fieldName, String field, String regex, String tip) {
        requireNotBlank(fieldName, field);
        if (!field.matches(regex)) {
            String message = tip == null ? fieldName + NOT_MATCH_REGEX_MESSAGE : tip;
            throw new ValidationException(message);
        }
    }
}
