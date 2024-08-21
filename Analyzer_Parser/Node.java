package Analyzer_Parser;

public class Node {
	public Characters type;
	public String value;
	public int noOfChildren; //root node depth is 0
	
	public Node(Characters type, String value, int children) {
		this.type=type;
		this.value=value;
		this.noOfChildren=children;
	}

}

enum Characters {
	let,
	fcn_form,
	identifier,
	integer,
	string,
	where,
	gamma,
	lambda,
	tau,
	rec,
	aug,
	conditional,
	op_or,
	op_and,
	op_not,
	op_compare,
	op_plus,
	op_minus,
	op_neg,
	op_mul,
	op_div,
	op_pow,
	at,
	true_value,
	false_value,
	nil,
	dummy,
	within,
	and,
	equal,
	comma,
	empty_params,
}