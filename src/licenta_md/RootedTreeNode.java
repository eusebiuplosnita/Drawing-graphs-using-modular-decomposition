package licenta_md;

import java.util.Collection;
import java.util.LinkedList;

/*
 * A node in a rooted tree.
 */
public class RootedTreeNode {

	// The parent of this node.
	private RootedTreeNode parent;
	
	// The first child of this node.
	private RootedTreeNode firstChild;
	
	// This node's sibling to its left.
	private RootedTreeNode leftSibling;
	
	// This node's sibling to its right.
	private RootedTreeNode rightSibling;
	
	// The number of this node's children.
	private int numChildren;

	
	/* The default constructor. */
	public RootedTreeNode() {
		parent = null;
		firstChild = null;
		leftSibling = null;
		rightSibling = null;
		numChildren = 0;
	}
		
	/* 
	 * Creates a node with a single child.
	 * @param child The child of this node.
	 */ 
        public void addChild(RootedTreeNode child) {
		child.remove();
		if (getFirstChild() != null) {
			firstChild.leftSibling = child;
			child.rightSibling = getFirstChild();
		}
		
		firstChild = child;
		child.parent = this;
		numChildren++;
	}
	
	/* Returns the number of this node's children. */
	public int getNumChildren() {
		return numChildren;
	}
	
	
	/* Returns true iff this node has no children. */
        public boolean hasNoChildren() {
		return (getNumChildren() == 0);
	}
	
	
	/* Returns true iff this node has a single child. */ 
	public boolean hasOnlyOneChild() {
		return (getNumChildren() == 1);
	}
	
	
	/* 
	 * Replaces this node in its tree with the supplied node.  This node's
	 * takes its children with it when it is removed.  That is, the supplied
	 * node does not assume its children.  Thus, this method might be better
	 * referred to as 'replaceSubtreeWith'.
	 * @param replacement The node to replace this one in the tree.
	 */
	public void replaceWith(RootedTreeNode replacement) {
		replacement.remove();
		replacement.leftSibling = getLeftSibling();
		replacement.rightSibling = getRightSibling();
		if (getLeftSibling() != null) {leftSibling.rightSibling = replacement; }
		if (getRightSibling() != null) {rightSibling.leftSibling = replacement; }
		replacement.parent = getParent();
		if (getParent() != null && getParent().getFirstChild() == this) { 
			parent.firstChild = replacement; 
		}
		parent = null;
		leftSibling = null;
		rightSibling = null;		
	}
	
	/* 
	 * Removes this node from its tree.  The node takes its children with it
	 * as it is removed, and so this method might be better called 'removeSubtree'.
	 */
	public void remove() {
		if (getParent() != null) { parent.numChildren--; }
		if (getLeftSibling() != null) { leftSibling.rightSibling = getRightSibling(); }
		if (getRightSibling() != null) { rightSibling.leftSibling = getLeftSibling(); }
		if (getParent() != null && getParent().getFirstChild() == this) { 
			parent.firstChild = getRightSibling(); 
		}
		parent = null;
		leftSibling = null;
		rightSibling = null;
	}
	
	
	/*
	 * Insert supplied node as the left sibling of this node.
	 * @param justBefore The node to be made this node's left sibling.
	 */
	public void insertBefore(RootedTreeNode justBefore) {
		remove();
		leftSibling = justBefore.getLeftSibling();
		if (justBefore.getLeftSibling() != null) { 
			justBefore.leftSibling.rightSibling = this; 
		}
		
		rightSibling = justBefore;
		justBefore.leftSibling = this;
		
		parent = justBefore.getParent();
		
		if (justBefore.getParent() != null) {
			justBefore.parent.numChildren++;
		}
		
		if (justBefore.getParent() != null && 
				justBefore.getParent().getFirstChild() == justBefore) { 
			parent.firstChild = this; 
		}
		
				
	}
	
	
	/*
	 * Insert supplied node as the right sibling of this node.
	 * @param justAfter The node to be made this node's right sibling.
	 */
	public void insertAfter(RootedTreeNode justAfter) {
		remove();
		
		rightSibling = justAfter.getRightSibling();
		if (justAfter.getRightSibling() != null) {
			justAfter.rightSibling.leftSibling = this;
		}
		
		leftSibling = justAfter;
		justAfter.rightSibling = this;
		
		parent = justAfter.getParent();
		
		if (justAfter.getParent() != null) {
			justAfter.parent.numChildren++;
		}
	}
	
	
	/*
	 * Moves this node to be the first amongst all its siblings, and thus
	 * its parent's first child.
	 */
	public void makeFirstChild() {
		
		if (getParent().getFirstChild() == this) { return; }
		
		RootedTreeNode newRightSibling = getParent().getFirstChild();
		remove();
		insertBefore(newRightSibling);		
	}

	
	/* Returns the parent of this node. */
	public RootedTreeNode getParent() {
		return parent;
	}


	/* Returns the first child of this node. */
	public RootedTreeNode getFirstChild() {
		return firstChild;
	}
	

	/* Returns the left sibling of this node. */
	public RootedTreeNode getLeftSibling() {
		return leftSibling;
	}


	/* Returns the right sibling of this node. */
	public RootedTreeNode getRightSibling() {
		return rightSibling;
	}

	
	/* Returns a collection of the leaves of the subtree rooted at this node. */
	public Collection<RootedTreeNode> getLeaves() {
		
		LinkedList<RootedTreeNode> leaves = new LinkedList<RootedTreeNode>();
		
		if (isALeaf()) { leaves.add(this); }
		else {
			RootedTreeNode currentChild = getFirstChild();
			while(currentChild != null) {
				leaves.addAll(currentChild.getLeaves());
				currentChild = currentChild.getRightSibling();
			}
		}
		return leaves;
	}

	
	/* Returns true iff this node is a leaf, i.e. it has no children. */
	public boolean isALeaf() {
		return hasNoChildren();
	}
	
	
	/*
	 * Adds as children the children from the supplied node.
	 * @param from The node whose children are to be added to this node's children.
	 */
	public void addChildrenFrom(RootedTreeNode from) {
		RootedTreeNode currentChild = from.getFirstChild();
		while (currentChild != null) {
			RootedTreeNode nextChild = currentChild.getRightSibling();
			addChild(currentChild);
			currentChild = nextChild;
		}
	}
	
	
	/* 
	 * Replaces this node in its subtree by its children.  That is, the children
	 * of this node are made to become children of this node's parent, and this
	 * node is removed from its tree.
	 */
	protected void replaceThisByItsChildren() {
		
		RootedTreeNode currentChild = getFirstChild();
		while (currentChild != null) {
			RootedTreeNode nextChild = currentChild.getRightSibling();
			currentChild.insertBefore(this);
			currentChild = nextChild;
		}
		this.remove();
	}
	
	
	/*
	 * Replaces the children of this node with the node supplied.  The
	 * children are removed from this node's tree.
	 * @param replacement This node's new child.
	 */
	protected void replaceChildrenWith(RootedTreeNode replacement) {
		RootedTreeNode currentChild = getFirstChild();
		while (currentChild != null) {
			RootedTreeNode nextChild = currentChild.getRightSibling();
			currentChild.remove();
			currentChild = nextChild;
		}
		addChild(replacement);
	}

	
	/* Returns true iff this node is the root of a tree. */ 
	public boolean isRoot() {
		return (getParent() == null);
	}
	
	
	/*
	 * Returns a string representation of the subtree rooted at this node.
	 * The subtree is enclosed in brackets, the number of this node's children
	 * is supplied, followed by the representations of its children's subtrees
	 * in order.
	 * @return The string representation of the subtree rooted at this node.
	 */
	public String toString() {
		String result = "(numChildren=" + getNumChildren() + " ";
		RootedTreeNode currentChild = getFirstChild();
		if (currentChild != null) { 
			result += currentChild;
			currentChild = currentChild.getRightSibling();
		}
		while (currentChild != null) {
			result += ", " + currentChild;
			currentChild = currentChild.getRightSibling();
		}
		return result + ")";
	}
}
