/** Tester.java
 *
 * By: Gus Silva & Anil Jethani
 * A class to test the Skip List
 * based Dictionary."
 */
public class Tester {

    public static void main(String[] args)
    {
        Dictionary<Integer, Integer> dict = new SkipList();
        dict.insert(7, 7);
        dict.insert(8,8);
        dict.insert(2,2);
        dict.insert(10,10);
        dict.insert(50,50);
        dict.insert(21,21);

        System.out.println("Dictionary Size: " + dict.size());
        dict.print();
        dict.remove(10);
        dict.print();
        dict.removeAny();
        dict.print();

    }
}
