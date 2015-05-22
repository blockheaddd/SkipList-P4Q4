/** SkipNode.java
 *
 * Source code example for "A Practical Introduction to Data
 * Structures and Algorithm Analysis, 3rd Edition (Java)"
 * by Clifford A. Shaffer
 * Copyright 2008-2011 by Clifford A. Shaffer
 */

public class SkipNode<Key extends Comparable<? super Key>,E> {
    private E element;
    private Key key;
    private int level;

    public SkipNode<Key,E>[] forward;

    public E element() { return element; }
    public Key key() { return key; }
    public int level() { return level; }

    public SkipNode(Key k, E r, int lvl) {
        key = k;
        element = r;
        level = lvl;
        forward = (SkipNode<Key,E>[])new SkipNode[level+1];
        for (int i=0; i<level; i++)
            forward[i] = null;
    }
}