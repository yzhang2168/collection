import java.util.Arrays;
import java.util.Set;
import java.util.LinkedHashSet;

/*******************************************************************************
* (Perform set operations on hash sets) Create two linked hash sets {"George", *
* "Jim", "John", "Blake", "Kevin", "Michael"} and {"George", "Katie", "Kevin", *
* "Michelle", "Ryan"} and find their union, difference, and intersection.      *
* (You can clone the sets to preserve the original sets from being changed by  *
* these set methods.)                                                          *
*******************************************************************************/
public class SetOperations {

    private static final int LinkedHashSet = 0;

    public static void main(String[] args) {
        Set<String> set1 = new LinkedHashSet<String>(Arrays.asList("George", "Jim", "John", "Blake", "Kevin", "Michael"));
        Set<String> set2 = new LinkedHashSet<String>(Arrays.asList("George", "Katie", "Kevin", "Michelle", "Ryan"));
    
        // union
        Set<String> union = new LinkedHashSet<String>(set1);
        union.addAll(set2);
        System.out.println("union");
        System.out.println(union);
        
        // subtraction
        Set<String> subtraction = new LinkedHashSet<String>(set1);
        subtraction.removeAll(set2);
        System.out.println("subtraction");
        System.out.println(subtraction);
        
        // intersection
        Set<String> intersection = new LinkedHashSet<String>();
        for (String s : set1) {
            if (set2.contains(s)) {
                intersection.add(s);
            }
        }        
        System.out.println("intersection");
        System.out.println(intersection);
    }

}
