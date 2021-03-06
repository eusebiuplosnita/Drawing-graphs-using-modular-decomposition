package licenta_md;

/* 
 * The different type of splits possible as a result of the refinement used
 * by our algorithm.  A MIXED split occurs when a node has undergone both a 
 * left and right split. 
 */
public enum SplitDirection {
	NONE,LEFT,RIGHT,MIXED;
}
