package string;

public class IsNumeric {

	public static boolean isNumeric(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}

		str = str.trim();
		if (str.length() == 0) {
			return false;
		}

		boolean seenSignBeforeE = false;
		boolean seenSignAfterE = false;
		boolean seenNumber = false;
		boolean seenDot = false;
		boolean seenE = false;
		boolean seenNumberAfterE = false;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			// 1st char before E or 1st char after E
			if (c == '+' || c == '-') {
				if (!seenE && (seenNumber || seenDot || seenSignBeforeE)) {
					return false;
				} else if (seenNumberAfterE || seenSignAfterE) {
					return false;
				} else if (!seenE) {
					seenSignBeforeE = true;
				} else {
					seenSignAfterE = true;
				}
			} else if (c >= '0' && c <= '9') {
				seenNumber = true;
				if (seenE) {
					seenNumberAfterE = true;
				}
			} else if (c == '.') {
				if (seenDot || seenE) {
					return false;
				} else {
					seenDot = true;
				}
			} else if (c == 'E' || c == 'e') {
				if (!seenE && !seenNumber || seenE) {
					return false;
				} else {
					seenE = true;
				}
			} else {
				return false;
			}
		}

		if (seenE && !seenNumberAfterE) {
			return false;
		}

		return seenNumber;
	}


	/**
	 * “0”: true “0 1”: false “ 0.1 ” true “ .1” true “0.1.” false “1. ” true
	 */
	public static boolean isNumericSimplified(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}

		str = str.trim();
		if (str.length() == 0) {
			return false;
		}

		boolean seenDot = false;
		boolean seenNum = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '.') {
				if (seenDot) {
					return false;
				} else {
					seenDot = true;
				}
			} else if (c < '0' || c > '9') {
				return false;
			} else {
				seenNum = true;
			}
		}

		return seenNum;
	}

	public static void main(String[] args) {
		System.out.println(isNumericSimplified(" 0 ")); // t
		System.out.println(isNumericSimplified(" 0 1"));// f
		System.out.println(isNumericSimplified("0.1"));// t
		System.out.println(isNumericSimplified(".1")); // t
		System.out.println(isNumericSimplified(".1.")); // f
		System.out.println(isNumericSimplified("1.0."));// f
		System.out.println(isNumericSimplified("1.")); // t
		System.out.println(isNumericSimplified(" . ")); // f
	}

}
