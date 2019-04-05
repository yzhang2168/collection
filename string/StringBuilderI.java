package string;


public final class StringBuilderI {

    private static final int INIT_CAPACITY = 16;
    private char[] array;
    private int size;

    public StringBuilderI() {
        array = new char[INIT_CAPACITY];
        size = 0;
    }

    public StringBuilderI(char[] chars) {
        array = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            array[i] = chars[i];
        }
        size = chars.length;
    }

    public StringBuilderI(String s) {
        array = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            array[i] = s.charAt(i);
        }
        size = s.length();
    }

    public StringBuilderI(StringBuilderI s) {
        array = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            array[i] = s.charAt(i);
        }
        size = s.length();
    }

    public int length() {
        return size;
    }

    public StringBuilderI append(int i) {
        int length = 1;
        int n = i;
        while (n / 10 >= 1) {
            n = n / 10;
            length++;
        }
        //System.out.println(length);

        while (length() + length > array.length) {
            expandCapacity();
            //System.out.println("expanded capacity");
        }
        
        for (int j = length, k = (int) Math.pow(10, length - 1); j > 0; j--, k = k / 10) {
            array[size++] = (char) (i / k + '0');
            i = i % k;
        }
        return this; // not a new StringBuilderI
    }

    public StringBuilderI append(String s) {
        while (length() + s.length() > array.length) {
            expandCapacity();
        }
        for (int i = 0; i < s.length(); i++) {
            array[size++] = s.charAt(i);
        }
        return this; // not a new StringBuilderI
    }

    private void expandCapacity() {
        char[] old = array;
        array = new char[old.length * 2];
        for (int i = 0; i < old.length; i++) {
            array[i] = old[i];
        }        
    }

    public char charAt(int index) {
        if (index < 0 || index >= size) {
            throw new StringIndexOutOfBoundsException("invalid index");
        }
        return array[index];
    }

    public StringBuilderI toLowerCase() {
        char[] temp = new char[length()];
        for (int i = 0; i < length(); i++) {
            temp[i] = (array[i] >= 'A' && array[i] <= 'Z') ? (char) (array[i] + 32) : array[i];
        }
        return new StringBuilderI(temp);
    }

    public StringBuilderI substring(int begin, int end) {
        char[] temp = new char[end - begin];
        for (int i = begin, j = 0; i < end; i++, j++) {
            temp[j] = array[i];
        }
        return new StringBuilderI(temp);
    }
    
    public StringBuilderI substring(int begin) {
        char[] temp = new char[array.length - begin];
        for (int i = begin, j = 0; i < array.length - begin; i++, j++) {
            temp[j] = array[i];
        }
        return new StringBuilderI(temp);
    }
    
    public StringBuilderI reverse() {
        char[] temp = new char[size];
        for (int i = 0, j = size - 1; i < size; i++, j--) {
            temp[i] = array[j];
        }
        return new StringBuilderI(temp);
    }

    public String toString() {
        // String(char[] value, int offset, int count)
        return new String(array, 0, size); 
    }
    
    public static void main(String[] args) {
        StringBuilderI sb = new StringBuilderI("abc");
        System.out.println(sb);
        
        sb.append("def");
        System.out.println(sb.toString());
        
        sb.append(123);
        System.out.println(sb.toString());      

        System.out.println(sb.substring(1));
        System.out.println(sb.reverse());
        
        StringBuilder sb2 = new StringBuilder("ab");
        System.out.println(sb2.substring(1));

    }
}
