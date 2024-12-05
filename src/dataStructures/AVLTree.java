package dataStructures;                                         

/**
 * AVL tree implementation
 * 
 * @author AED team
 * @version 1.0
 *
 * @param <K> Generic type Key, must extend comparable
 * @param <V> Generic type Value 
 */
public class AVLTree<K extends Comparable<K>, V> 
    extends AdvancedBSTree<K,V> implements OrderedDictionary<K,V>
{                                                                   

    protected AVLTree(AVLNode<Entry<K,V>> node) {
        root = node;
    }

    public AVLTree() {
        this(null);
    }

    /**
     * Rebalance method called by insert and remove.  Traverses the path from
     * zPos to the root. For each node encountered, we recompute its height
     * and perform a trinode restructuring if it's unbalanced.
     * the rebalance is completed with O(log n) running time
     */
    void rebalance(AVLNode<Entry<K,V>> zPos) {
        if (zPos == null) return;

        // Update height of starting position for internal nodes
        if (zPos.isInternal())
            zPos.setHeight();

        // Traverse up the tree towards the root
        while (zPos != null) {
            zPos.setHeight();  // Update height at current level

            if (!zPos.isBalanced()) {
                // Get the trinode configuration
                AVLNode<Entry<K,V>> yPos = zPos.tallerChild();  // Always exists when unbalanced
                AVLNode<Entry<K,V>> xPos = yPos.tallerChild();  // Always exists in valid AVL tree

                // Perform restructuring
                zPos = (AVLNode<Entry<K,V>>) restructure(xPos);

                // Update heights in the restructured subtree
                ((AVLNode<Entry<K,V>>)zPos.getLeft()).setHeight();
                ((AVLNode<Entry<K,V>>)zPos.getRight()).setHeight();
                zPos.setHeight();
            }

            // Move up to parent
            zPos = (AVLNode<Entry<K,V>>) zPos.getParent();
        }
    }


    @Override
    public V insert( K key, V value )
    {
        V valueToReturn=null;
        AVLNode<Entry<K,V>> newNode= (AVLNode<Entry<K,V>>)this.findNode(key);   // node where the new entry is being inserted (if find(key)==null)

        if ( newNode == null || newNode.getElement().getKey().compareTo(key) != 0 )
        { // Key does not exist, node is "parent"
            AVLNode<Entry<K,V>> newLeaf = new AVLNode<>(new EntryClass<>(key, value));
            this.linkSubtreeInsert(newLeaf, newNode);
            currentSize++;
        }
        else
        {
            valueToReturn = newNode.getElement().getValue();
            newNode.setElement(new EntryClass<>(key, value));
        }
        if(newNode != null) //(if find(key)==null)
            rebalance(newNode); // rebalance up from the insertion node
        return valueToReturn;
    }

   @Override
   public V remove(K key) {
       V valueToReturn = null;
       AVLNode<Entry<K,V>> node = (AVLNode<Entry<K,V>>)this.findNode(key);

       if (node == null || node.getElement().getKey().compareTo(key) != 0)
           return null;
       else {
           valueToReturn = node.getElement().getValue();

           if (node.getLeft() == null) {
               AVLNode<Entry<K,V>> startRebalance = (AVLNode<Entry<K,V>>)node.getParent();
               this.linkSubtreeRemove(node.getRight(), node.getParent(), node);
               rebalance(startRebalance);
           }
           else if (node.getRight() == null) {
               AVLNode<Entry<K,V>> startRebalance = (AVLNode<Entry<K,V>>)node.getParent();
               this.linkSubtreeRemove(node.getLeft(), node.getParent(), node);
               rebalance(startRebalance);
           }
           else {
               AVLNode<Entry<K,V>> minNode = (AVLNode<Entry<K,V>>)this.minNode(node.getRight());
               node.setElement(minNode.getElement());
               // We physically remove minNode, so start rebalancing from its parent
               AVLNode<Entry<K,V>> startRebalance = (AVLNode<Entry<K,V>>)minNode.getParent();
               this.linkSubtreeRemove(minNode.getRight(), minNode.getParent(), minNode);
               rebalance(startRebalance);
           }
           currentSize--;
       }
       return valueToReturn;
   }

}
