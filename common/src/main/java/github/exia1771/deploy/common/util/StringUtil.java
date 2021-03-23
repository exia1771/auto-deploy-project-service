package github.exia1771.deploy.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {

	private final String BLANK_CHARACTER = "^[\\s]+$";

	public Boolean isLengthBetween(String str, int min, int max) {
		int length = 0;
		if (str == null) {
			return length >= min;
		} else {
			length = str.length();
			return min <= length && length <= max;
		}
	}

	public boolean isBlank(String str) {
		if (str == null || str.length() == 0 || str.trim().length() == 0) {
			return true;
		} else {
			return str.matches(BLANK_CHARACTER);
		}
	}
}
