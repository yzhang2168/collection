package string;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class StringImpl {

    private char[] chars; // init from one of the constructors, not allowed to be null
    
    // do not shallow copy!
    public StringImpl(char[] chars) {
        this.chars = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            this.chars[i] = chars[i];
        }
    }    
    
    public StringImpl(String s) {
        chars = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            chars[i] = s.charAt(i);
        }
    }
    
    public char charAt(int index) {
        if (index < 0 || index >= length()) {
            throw new StringIndexOutOfBoundsException("invalid index");
        }
        return chars[index];
    }
    
    public int length() {
        return chars.length;
    }
    
    public StringImpl substring(int begin, int end) {
        char[] temp = new char[end - begin];
        for (int i = begin, j = 0; i < end; i++, j++) {
            temp[j] = chars[i];
        }
        return new StringImpl(temp);
    }
    
    public StringImpl substring(int begin) {
        return substring(begin, length());
    }

    public StringImpl toLowerCase() {
        char[] temp = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            temp[i] = chars[i] >= 'A' && chars[i] <= 'Z' ? (char) (chars[i] + 32) : chars[i];
        }
        return new StringImpl(temp);
    }
    
    public StringImpl toUpperCase() {
        char[] temp = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            temp[i] = chars[i] >= 'a' && chars[i] <= 'z' ? (char) (chars[i] - 32) : chars[i];
        }
        return new StringImpl(temp);
    }

    public boolean equals(StringImpl s) {
        if (this == s) {
            return true;
        }
        
        if (this.chars.length != s.length()) {
            return false;
        }
        
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != s.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    public static StringImpl valueOf(int i) {
        int length = 1;
        int n = i;
        while (n / 10 > 0) {
            n = n / 10;
            length++;
        }
        char[] temp = new char[length];
        for (int j = 0, k = (int) Math.pow(10,  length - 1); j < length; j++, k /= 10) {
            temp[j] = Character.forDigit((i / k),  10);
            i %= k;
        }
        
        return new StringImpl(temp);
    }

    
    // do not shallow copy!
    public char[] toChars() {
        char[] temp = new char[length()];
        for (int i = 0; i < length(); i++) {
            temp[i] = chars[i];
        }
        return temp;
    }
    
    public static StringImpl valueOf(boolean b) {
        return b ? new StringImpl("true") : new StringImpl("false"); 
    }
    
    public static StringImpl[] split(StringImpl s, StringImpl regex) {        
        java.util.Set<Character> delimiters = new HashSet<Character>();
        for (int i = 0; i < regex.length(); i++) {
            delimiters.add(regex.charAt(i));
        }
 
        int start = 0;
        List<StringImpl> temp = new ArrayList<StringImpl>();
        
        for (int end = 0; end < s.length(); end++) {
            if (delimiters.contains(s.charAt(end))) {
                temp.add(s.substring(start, end));
                //temp.add(s.substring(end, end + 1)); // delimiter
                start = end + 1;
            }
        }
        temp.add(s.substring(start, s.length()));        
        
        StringImpl[] result = new StringImpl[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            result[i] = temp.get(i);
        }
        return result;
    }
    
    @Override
    public String toString() {
        return new String(chars);
    }
    
    public static void main(String[] args) {
        
        int i = 1;
        char c = (char) (i + '0');
        System.out.println(c);
        
        char[] chars = "abc".toCharArray();
        
        StringImpl s1 = new StringImpl(chars);
        System.out.println(s1);
        System.out.println(new String(chars, 0, 1));
        
        StringImpl[] split1 = StringImpl.split(new StringImpl("ab#12#45"), new StringImpl("#"));
        System.out.println(Arrays.toString(split1)); // [ab, 12, 45]

        StringImpl[] split2 = StringImpl.split(new StringImpl("a?b?gf#e"), new StringImpl("?#"));
        System.out.println(Arrays.toString(split2)); // [a, b, gf, e]

    }
}
