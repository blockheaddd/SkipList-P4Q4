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
        //System.out.println(temp.element());
        while(temp.forward[0] != null)
        {
            temp = temp.forward[0];
            System.out.print(temp.element()+"   ");
        }
        System.out.println();
    }

  public int size() { return size; }

    @Override
    public E remove(Key k) {
        SkipNode<Key, E> temp = head;
        E returnElem = null;

        for(int i = level; i >= 0; i--) //Find element to remove
        {
            while(temp.forward[i] != null && k.compareTo(temp.forward[i].key())>0)
            {
                temp = temp.forward[i];
            }
        }


        if(temp.forward[0] != null && k.compareTo(temp.forward[0].key()) == 0) {

            returnElem = temp.forward[0].element();  //Return element
            System.out.println("REMOVING: " + returnElem);
            int lvl = temp.forward[0].level();
            for(int i=0; i <= lvl; i++)
            {
                temp.forward[i] = temp.forward[i].forward[i]; //Reassign pointers to prev Node
            }

            size--; //Reduce Size
            return returnElem;
        }
        else {
            System.out.println(k + " NOT FOUND!");
            return null;
        }//Not Found
    }


    /**
     * Since remove any is a function to remove an
     * arbitrary element, we have implemented it
     * to remove the last node for efficiency
     * and simplicity
     * @return - the removed nodes element
     */
    @Override
    public E removeAny() {
        SkipNode<Key,E> temp = head;
        E returnElem = null;
        while(temp.forward[0].forward[0] != null)
            temp = temp.forward[0];

        returnElem = temp.forward[0].element();

        System.out.println("(Remove Any) Removing: " + returnElem);

        for(int i=0; i < temp.level(); i++)
            temp.forward[i] = null;

        size--; //Reduce Size

        return returnElem;
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