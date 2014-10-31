package licenta_md;

/*
 * The different types of nodes in a modular decomposition tree. 
 */
public enum MDNodeType {
	PRIME,SERIES,PARALLEL;

	/* Returns true iff this type is degenerate. */
	public boolean isDegenerate() {
		return (this == PARALLEL || this == SERIES);
	}
}
