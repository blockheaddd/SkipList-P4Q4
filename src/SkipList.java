/** SkipList.java
 *
 * By: Gus Silva and Anil Jethani
 *
 * Some code is from "A Practical Introduction to Data
 * Structures and Algorithm Analysis, 3rd Edition (Java)"
 * by Clifford A. Shaffer
 * Copyright 2008-2011 by Clifford A. Shaffer
 */

import java.util.Random;

public class SkipList<Key extends Comparable<? super Key>, E> implements Dictionary<Key, E> {

    private SkipNode<Key,E> head;
    private int level, size;

    SkipList(){
        clear(); //Clear function initializes new skip list
    }

    @Override
    public void clear() {
        level = -1;
        size = 0;
        head = new SkipNode<>(null,null,0);
    }

    @Override
    public void insert(Key k, E e) {
        int newLevel = randomLevel();
        if( newLevel > level)
            adjustHead(newLevel);

        SkipNode<Key, E> temp = head;
        SkipNode<Key, E>[] update = (SkipNode<Key,E>[]) new SkipNode[level+1];
        for(int i=level; i >= 0; i--)
        {
            while(temp.forward[i] != null && k.compareTo(temp.forward[i].key())>0)
            {
                temp = temp.forward[i];
            }
            update[i] = temp;
        }
        System.out.println("Inserting: " + k + "  |  after:  " + temp.element());
        temp = new SkipNode<>(k,e,newLevel);
        for(int i=0; i <= newLevel; i++)
        {
            temp.forward[i] = update[i].forward[i];
            update[i].forward[i] = temp;
        }
        size++;
    }

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

    @Override
    public E find(Key k) {

        SkipNode<Key, E> temp = head;

        for(int i = level; i >= 0; i--)
        {
            while(temp.forward[i] != null && k.compareTo(temp.forward[i].key())>0)
            {
                temp = temp.forward[i];
            }
        }
        temp = temp.forward[0];

        if(temp != null && k.compareTo(temp.key()) == 0)
            return temp.element();  //Return element
        else
            return null; //Not Found
    }

    @Override
    public int size() {
        return size;
    }


    /** Function to adjust head nodes height */
    private void adjustHead(int newHeight)
    {
        SkipNode<Key,E> temp = head;    //
        head = new SkipNode<Key,E>(null,null,newHeight);
        for(int i=0; i < level; i++)    //Give old head nodes to new head
            temp.forward[i] = head.forward[i];

        level = newHeight;
    }

    /** Pick a level using a geometric distribution */
    private int randomLevel() {
        Random rand = new Random();
        int lev;
        for (lev=0; Math.abs(rand.nextInt()%2) == 0; lev++);
            // Do nothing
        return lev;
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
}
