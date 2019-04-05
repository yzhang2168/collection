package string;

public class Atoi {

	public static int atoi(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}

		int i = 0;
		int n = s.length();

		// leading spaces
		while (i < n && s.charAt(i) == ' ') {
			i++;
		}

		// spaces only
		if (i == n) {
			return 0;
		}

		// if there's a sign
		boolean positive = true;
		if (s.charAt(i) == '+' || s.charAt(i) == '-') {
			positive = s.charAt(i) == '+';
			i++;
		}

		boolean trailingSpace = false;
		long sum = 0;
		for (; i < n; i++) {
			char c = s.charAt(i);
			if (c == ' ') {
				trailingSpace = true;
			} else if (c < '0' || c > '9') {
				return 0;
			} else {
				if (trailingSpace) {
					return 0;
				} else {
					sum = sum * 10 + c - '0';
					// sum is positive now
					// MAX_VALUE is 1 less than MIN_VALUE 2147483648
					// if input is positive, sum could overflow now
					if (sum > (long) Integer.MAX_VALUE + 1) {
						break;
					}
				}
			}
		}

		// put back sign
		sum = positive ? sum : -sum;

		// check int overflow
		if (sum > (long) Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		} else if (sum < (long) Integer.MIN_VALUE) {
			return Integer.MIN_VALUE;
		} else {
			return (int) sum;
		}
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
				if (!seenE && (seenNumber || seenDot) || seenNumberAfterE) {
					return false;
				} else if ((!seenE && seenSignBeforeE) || seenSignAfterE) {
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

	public static void main(String[] args) {
		System.out.println(Integer.valueOf('a').hashCode());
		System.out.println("trim string: " + "  111 ".trim());
		System.out.println("empty string: " + atoi(""));
		System.out.println("spaces only : " + atoi("   "));
		System.out.println("spaces in the middle: " + atoi(" 1 2 "));
		System.out.println("valid positive: " + atoi(" 123    "));
		System.out.println("valid negative: " + atoi(" -123   "));
		System.out.println("int max: " + atoi("2147483647   "));
		System.out.println("int min: " + atoi("-2147483648    "));
		System.out.println("int overflow: " + atoi("2147483648   "));
		System.out.println("int overflow: " + atoi("-2147483649   "));
		System.out.println("=============================");
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
