package dataStructures;

import java.io.Serializable;

/**
 * BST node implementation
 *
 * @author AED team
 * @version 1.0
 *
 * @param <E> Generic Value
 */
public class BSTNode<E> implements Serializable
{



    /**
     * Element stored in the node.
     */
    protected E element;



    /**
     * (Pointer to) the left child.
     *
     */
    protected BSTNode<E> left;

    /**
     * (Pointer to) the right child.
     *
     */
    protected BSTNode<E> right;
    /**
     * (Pointer to) the parent node.
     *
     */
    protected BSTNode<E> parent;

    /**
     * Constructor for BST nodes
     *
     */
    public BSTNode( E elem, BSTNode<E> parent, BSTNode<E> left, BSTNode<E> right )
    {
        this.element = elem;
        this.parent = parent;
        this.left = left;
        this.right = right;
    }
    public BSTNode(E elem) {
        this(elem,null,null,null);
    }

    /**
     * Returns the element of the current node.
     *
     * @return
     */
    E getElement( )
    {
        return element;
    }



    /**
     * Returns the left child node of the current node.
     *
     * @return
     */
    BSTNode<E> getLeft( )
    {
        return left;
    }


    /**
     * Returns the right child node of the current node.
     *
     * @return
     */
    BSTNode<E> getRight( )
    {
        return right;
    }
    /**
     * Returns the parent node of the current node.
     *
     * @return
     */
    BSTNode<E> getParent( )
    {
        return parent;
    }

    boolean isInternal() {
        return left != null || right != null;
    }

    public void setLeft(BSTNode<E> left) {
        this.left = left;
    }

    public void setRight(BSTNode<E> right) {
        this.right = right;
    }

    public void setElement(E element) {
        this.element = element;
    }

    public void setParent(BSTNode<E> parent) {
        this.parent = parent;
    }
}
