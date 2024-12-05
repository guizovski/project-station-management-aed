package dataStructures;                                         

/**
 * Advanced BSTree Data Type implementation
 * @author AED team
 * @version 1.0
 * @param <K> Generic type Key, must extend comparable
 * @param <V> Generic type Value 
 */
public abstract class AdvancedBSTree<K extends Comparable<K>, V> extends BinarySearchTree<K,V>
{
    /**
     * Performs a single left rotation rooted at Y node.
     * Node X was a  right  child  of Y before the  rotation,
     * then Y becomes the left child of X after the rotation.
     * @param Y - root of the rotation
     * @pre: Y has a right child
     */
    protected void rotateLeft(BSTNode<Entry<K,V>> Y) {
        BSTNode<Entry<K,V>> X = Y.getRight(); // X is the right child of Y
        BSTNode<Entry<K,V>> parent = Y.getParent(); // Store Y's parent

        // Update Y's right child
        Y.setRight(X.getLeft());
        if (X.getLeft() != null)
            X.getLeft().setParent(Y);

        // Update X's left child to be Y
        X.setLeft(Y);
        Y.setParent(X);

        // Link X to Y's old parent
        X.setParent(parent);
        if (parent == null)
            root = X;
        else if (parent.getLeft() == Y)
            parent.setLeft(X);
        else
            parent.setRight(X);
    }

    /**
     * Performs a single right rotation rooted at Y node.
     * Node X was a  left  child  of Y before the  rotation,
     * then Y becomes the right child of X after the rotation.
     * @param Y - root of the rotation
     * @pre: Y has a left child
     */
    protected void rotateRight(BSTNode<Entry<K,V>> Y) {
        BSTNode<Entry<K,V>> X = Y.getLeft(); // X is the left child of Y
        BSTNode<Entry<K,V>> parent = Y.getParent(); // Store Y's parent

        // Update Y's left child
        Y.setLeft(X.getRight());
        if (X.getRight() != null)
            X.getRight().setParent(Y);

        // Update X's right child to be Y
        X.setRight(Y);
        Y.setParent(X);

        // Link X to Y's old parent
        X.setParent(parent);
        if (parent == null)
            root = X;
        else if (parent.getLeft() == Y)
            parent.setLeft(X);
        else
            parent.setRight(X);
    }

    /**
     * Performs a tri-node restructuring (a single or double rotation rooted at X node).
     * Assumes the nodes are in one of following configurations:
     *
     * @param X - root of the rotation
     * <pre>
     *          z=c       z=c        z=a         z=a
     *         /  \      /  \       /  \        /  \
     *       y=b  t4   y=a  t4    t1  y=c     t1  y=b
     *      /  \      /  \           /  \         /  \
     *    x=a  t3    t1 x=b        x=b  t4       t2 x=c
     *   /  \          /  \       /  \             /  \
     *  t1  t2        t2  t3     t2  t3           t3  t4
     * </pre>
     * @return the new root of the restructured subtree
     */
    protected BSTNode<Entry<K,V>> restructure(BSTNode<Entry<K,V>> X) {
        BSTNode<Entry<K, V>> Y = X.getParent();
        BSTNode<Entry<K, V>> Z = Y.getParent();

        if ((Y == Z.getLeft()) == (X == Y.getLeft())) {
            // Single rotation case
            if (Y == Z.getLeft())
                rotateRight(Z);
            else
                rotateLeft(Z);
            return Y;
        } else {
            // Double rotation case
            if (X == Y.getRight()) {
                // Left-Right case
                rotateLeft(Y);
                rotateRight(Z);
            } else {
                // Right-Left case
                rotateRight(Y);
                rotateLeft(Z);
            }
            return X;
        }
    }
}

