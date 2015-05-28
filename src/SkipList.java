import java.util.ArrayList;
import java.util.Random;

/** SkipList.java
 * By: Gus Silva and Anil Jethani
 *
 * Skiplist implementation for Dictionary ADT
 * based off code from "A Practical Introduction to Data
 * Structures and Algorithm Analysis, 3rd Edition (Java)"
 * by Clifford A. Shaffer
 * Copyright 2008-2011 by Clifford A. Shaffer
 * */
class SkipList<Key extends Comparable<? super Key>, E>
         implements Dictionary<Key, E> {
    private SkipNode<Key,E> head;
    private ArrayList<Key> keys = new ArrayList<>();
    private int level;
    private int size;

    public SkipList() {
    head = new SkipNode<Key,E>(null, null, 0);
    level = -1;
    size = 0;
  }

    private void AdjustHead(int newLevel) {
    SkipNode<Key,E> temp = head;
    head = new SkipNode<Key,E>(null, null, newLevel);
    for (int i=0; i<=level; i++)
      head.forward[i] = temp.forward[i];
    level = newLevel;
  }

    /** Function to print the list */
    public void print()
    {
        SkipNode<Key, E> temp = head;
        while(temp.forward[0] != null)
        {
            temp = temp.forward[0];
            System.out.print(temp.element()+"   ");
        }
        System.out.println();
    }

    public int size() { return size; }

    public E remove(Key k) {
        SkipNode<Key, E> temp = head;
        E returnElem = null;
        int currLevel = 0;
        int targetLevel = Integer.MAX_VALUE;

        while(temp != null && temp.forward[0] != null && (currLevel < targetLevel)) //loop through each node
        {
            for(int j = 0; j < temp.level(); j++) //loop through each level of current node
            {
                if((temp.forward[j] != null) &&
                        (temp.forward[j].key().compareTo(k) == 0)) //If forward is the target node
                {
                    //Set Target Level if not set already
                    if(targetLevel == Integer.MAX_VALUE)
                        targetLevel = temp.forward[j].level();

                    //Set return element if not set already
                    if(returnElem == null)
                        returnElem = temp.forward[j].element();

                    //Make pointer skip over removed node
                    temp.forward[j] = temp.forward[j].forward[j];
                }
            }
            temp = temp.forward[0]; //Move to next node

        }
        if(returnElem != null)  //Key was found and removed
        {
            size--;
            keys.remove(k);
            return returnElem;
        }
        else                    //Key not found
            return null;
    }

    /**
     * Since remove any is a function to remove an
     * arbitrary element, we have implemented it
     * to remove the last node for efficiency
     * and simplicity
     * @return - the removed nodes element
     */
    public E removeAny() {
        int randomKey = Math.abs(new Random().nextInt(size));
        return remove(keys.get(randomKey));
    }

    /** Function to clear the list */
    public void clear() {
        level = -1;
        size = 0;
        head = new SkipNode<>(null,null,0);
    }

    /** Pick a level using a geometric distribution */
    int randomLevel() {
      int lev;
      for (lev=0; DSutil.random(2) == 0; lev++); // Do nothing
      return lev;
    }

    /** Insert a record into the skiplist */
    public void insert(Key k, E newValue) {
      int newLevel = randomLevel();  // New node's level
      if (newLevel > level)          // If new node is deeper
        AdjustHead(newLevel);        //   adjust the header
      // Track end of level
      SkipNode<Key,E>[] update =
             (SkipNode<Key,E>[])new SkipNode[level+1];
      SkipNode<Key,E> x = head;        // Start at header node
      for (int i=level; i>=0; i--) { // Find insert position
        while((x.forward[i] != null) &&
              (k.compareTo(x.forward[i].key()) > 0))
          x = x.forward[i];
        update[i] = x;               // Track end at level i
      }
      x = new SkipNode<Key,E>(k, newValue, newLevel);
      for (int i=0; i<=newLevel; i++) {      // Splice into list
        x.forward[i] = update[i].forward[i]; // Who x points to
        update[i].forward[i] = x;            // Who y points to
      }
      keys.add(k);                  //Add new key to keys array
      size++;                       // Increment dictionary size
    }

    /** Skiplist Search */
    public E find(Key searchKey) {
      SkipNode<Key,E> x = head;          // Dummy header node
      for (int i=level; i>=0; i--)       // For each level...
        while ((x.forward[i] != null) && // go forward
               (searchKey.compareTo(x.forward[i].key()) > 0))
          x = x.forward[i];              // Go one last step
      x = x.forward[0];  // Move to actual record, if it exists
      if ((x != null) && (searchKey.compareTo(x.key()) == 0))
        return x.element();              // Got it
      else return null;                  // Its not there
    }

}