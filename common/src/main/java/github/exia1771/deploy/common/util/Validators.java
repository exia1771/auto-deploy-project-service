package github.exia1771.deploy.common.util;

import com.alibaba.fastjson.JSON;

import javax.validation.ValidationException;
import java.util.Objects;
import java.util.function.Function;

public abstract class Validators {

	private static final String NOT_NULL_MESSAGE = " 是必填项 ";
	private static final String NOT_BLANK_MESSAGE = " 不能由空字符组成 ";
	private static final String NOT_LENGTH_BETWEEN_MESSAGE = " 字符长度不在指定的区间[%s, %s]内 ";
	private static final String NOT_MATCH_REGEX_MESSAGE = " 请输入正确的格式 ";
	private static final String NOT_BETWEEN_SIZE_MESSAGE = " 请输入正确范围在[%s, %s]的数字 ";

	public static void requireNotNull(String fieldName, Object field) {
		requireNotNull(fieldName, field, null);
	}

	public static void requireNotNull(String fieldName, Object field, String tip) {
		if (field == null) {
			String message = tip == null ? fieldName + NOT_NULL_MESSAGE : tip;
			throw new ValidationException(message);
		}
	}

	public static void requireSize(String fieldName, Number field, Number min, Number max) {
		requireSize(fieldName, field, min, max, null);
	}

	public static void requireSize(String fieldName, Number field, Number min, Number max, String tip) {
		requireNotNull(fieldName, field);
		if (field.doubleValue() < min.doubleValue() || field.doubleValue() > max.doubleValue()) {
			String message = tip == null ? fieldName + String.format(NOT_BETWEEN_SIZE_MESSAGE, min, max) : tip;
			throw new ValidationException(message);
		}
	}

	public static void requireNotBlank(String fieldName, String field) {
		requireNotBlank(fieldName, field, null);
	}

	public static void requireNotBlank(String fieldName, String field, String tip) {
		requireNotNull(fieldName, field);
		if (StringUtil.isBlank(field)) {
			String message = tip == null ? fieldName + NOT_BLANK_MESSAGE : tip;
			throw new ValidationException(message);
		}
	}

	public static void requireLength(String fieldName, String field, int min, int max, boolean notBlank) {
		requireLength(fieldName, field, min, max, null, notBlank);
	}

	public static void requireLength(String fieldName, String field, int min, int max, String tip, boolean notBlank) {
		if (notBlank) {
			requireNotBlank(fieldName, field);
		}

		if (!StringUtil.isLengthBetween(field, min, max)) {
			String defaultMessage = fieldName + String.format(NOT_LENGTH_BETWEEN_MESSAGE, min, max);
			String message = tip == null ? defaultMessage : tip;
			throw new ValidationException(message);
		}
	}

	public static void requireMatchRegex(String fieldName, String field, String regex) {
		requireMatchRegex(fieldName, field, regex, null);
	}

	public static void requireMatchRegex(String fieldName, String field, String regex, String tip) {
		requireNotNull(fieldName, field);
		if (!field.matches(regex)) {
			String message = tip == null ? fieldName + NOT_MATCH_REGEX_MESSAGE : tip;
			throw new ValidationException(message);
		}
	}

	public static <T, R> void requireClassType(String fieldName, T field, Function<T, R> convert) {
		requireClassType(fieldName, field, convert, false);
	}

	public static <T, R> void requireClassType(String fieldName, T field, Function<T, R> convert, boolean nullable) {
		try {
			R apply = convert.apply(field);
			if (!nullable) {
				Objects.requireNonNull(apply);
			}
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

	public static void main(String[] args) {
		Validators.requireClassType("端口号", "[", JSON::parseArray);
	}
}
