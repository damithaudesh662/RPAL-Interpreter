package Analyzer_Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Parser {
	private final List<Token> CharacterTokensList;
	private final List<Node> ASTree; // Last element will be root of the tree
	private final ArrayList<String> stringAST;

	public Parser(List<Token> CharacterTokensList) {
		this.CharacterTokensList = CharacterTokensList;
		ASTree = new ArrayList<>();
		stringAST = new ArrayList<>();
	}

	public List<Node> parse() {
		CharacterTokensList.add(new Token(TokenType.EndOfTokens, ""));
		E();
		if (CharacterTokensList.get(0).type.equals(TokenType.EndOfTokens)) {
			return ASTree;
		} else {
			System.out.println("There is an error in the parsing process");
			return null;
		}
	}

	public ArrayList<String> convertAST_toStringAST() {
		String dots = "";
		List<Node> stack = new ArrayList<>();

		while (!ASTree.isEmpty()) {
			if (stack.isEmpty()) {
				if (ASTree.get(ASTree.size()-1).noOfChildren == 0) {
					addStrings(dots, ASTree.remove(ASTree.size()-1));
				} else {
					Node node = ASTree.remove(ASTree.size()-1);
					stack.add(node);
				}
			} else {
				if (ASTree.get(ASTree.size()-1).noOfChildren > 0) {
					Node node = ASTree.remove(ASTree.size()-1);
					stack.add(node);
					dots += ".";
				} else {
					stack.add(ASTree.remove(ASTree.size()-1));
					dots += ".";
					while (stack.get(stack.size()-1).noOfChildren == 0) {
						addStrings(dots, stack.remove(stack.size()-1));
						if (stack.isEmpty()) break;
						dots = dots.substring(0, dots.length() - 1);
						Node node = stack.remove(stack.size()-1);
						node.noOfChildren--;
						stack.add(node);

					}
				}

			}
		}
		Collections.reverse(stringAST);
		return stringAST;
	}

	void addStrings(String dots, Node node) {
		if (node.type == Characters.identifier) {
			stringAST.add(dots + "<ID:" + node.value + ">");
		} else if (node.type == Characters.integer) {
			stringAST.add(dots + "<INT:" + node.value + ">");
		} else if (node.type == Characters.string) {
			stringAST.add(dots + "<STR:" + node.value + ">");
		} else if (node.type == Characters.true_value ||
				node.type == Characters.false_value ||
				node.type == Characters.nil ||
				node.type == Characters.dummy) {
			stringAST.add(dots + "<" + node.value + ">");
		} else if (node.type == Characters.fcn_form) {
			stringAST.add(dots + "function_form");
		} else {
			stringAST.add(dots + node.value);
		}
	}


	void E() {
		int n = 0;
		Token token = CharacterTokensList.get(0);
		if (token.type.equals(TokenType.KEYWORD) && Arrays.asList("let", "fn").contains(token.value)) {
			if (token.value.equals("let")) {
				CharacterTokensList.remove(0);
				D();
				if (!CharacterTokensList.get(0).value.equals("in")) {
					System.out.println("Parse error at E");
				}
				CharacterTokensList.remove(0);
				E();
				ASTree.add(new Node(Characters.let, "let", 2));

			} else {
				CharacterTokensList.remove(0);
				do {
					Vb();
					n++;
				} while (CharacterTokensList.get(0).type.equals(TokenType.IDENTIFIER) || CharacterTokensList.get(0).value.equals("("));
				if (!CharacterTokensList.get(0).value.equals(".")) {
					System.out.println("Parse error at E");

				}
				CharacterTokensList.remove(0);
				E();
				ASTree.add(new Node(Characters.lambda, "lambda", n + 1));
			}
		} else
			Ew();
	}

	void Ew() {
		T();
		if (CharacterTokensList.get(0).value.equals("where")) {
			CharacterTokensList.remove(0); 
			Dr();
			ASTree.add(new Node(Characters.where, "where", 2));
		}

	}

	void T() {
		Ta();
		int n = 1;
		while (CharacterTokensList.get(0).value.equals(",")) {
			CharacterTokensList.remove(0); 
			Ta();
			++n;
		}
		if (n > 1) {
			ASTree.add(new Node(Characters.tau, "tau", n));
		}
	}


	void Ta() {
		Tc();
		while (CharacterTokensList.get(0).value.equals("aug")) {
			CharacterTokensList.remove(0); 
			Tc();
			ASTree.add(new Node(Characters.aug, "aug", 2));
		}
	}


	void Tc() {
		B();
		if (CharacterTokensList.get(0).value.equals("->")) {
			CharacterTokensList.remove(0);
			Tc();
			if (!CharacterTokensList.get(0).value.equals("|")) {
				System.out.println("Parse error at Tc: conditional '|' expected");
			}
			CharacterTokensList.remove(0); 
			Tc();
			ASTree.add(new Node(Characters.conditional, "->", 3));
		}
	}


	void B() {
		Bt();
		while (CharacterTokensList.get(0).value.equals("or")) {
			CharacterTokensList.remove(0); 
			Bt();
			ASTree.add(new Node(Characters.op_or, "or", 2));
		}
	}


	void Bt() {

		Bs();
		while (CharacterTokensList.get(0).value.equals("&")) {
			CharacterTokensList.remove(0); 
			Bs();
			ASTree.add(new Node(Characters.op_and, "&", 2));
		}
	}

	void Bs() {
		if (CharacterTokensList.get(0).value.equals("not")) {
			CharacterTokensList.remove(0);
			Bp();
			ASTree.add(new Node(Characters.op_not, "not", 1));
		} else Bp();
	}


	void Bp() {
		A();
		Token token = CharacterTokensList.get(0);
		if (Arrays.asList(">", ">=", "<", "<=").contains(token.value)
				|| Arrays.asList("gr", "ge", "ls", "le", "eq", "ne").contains(token.value)) {
			CharacterTokensList.remove(0);
			A();
			if (token.value.equals(">")) {
				ASTree.add(new Node(Characters.op_compare, "gr", 2));
			} else if (token.value.equals(">=")) {
				ASTree.add(new Node(Characters.op_compare, "ge", 2));
			} else if (token.value.equals("<")) {
				ASTree.add(new Node(Characters.op_compare, "ls", 2));
			} else if (token.value.equals("<=")) {
				ASTree.add(new Node(Characters.op_compare, "le", 2));
			} else {
				ASTree.add(new Node(Characters.op_compare, token.value, 2));
			}
		}
	}


	void A() {
		if (CharacterTokensList.get(0).value.equals("+")) {
			CharacterTokensList.remove(0); 
			At();
		} else if (CharacterTokensList.get(0).value.equals("-")) {
			CharacterTokensList.remove(0); 
			At();
			ASTree.add(new Node(Characters.op_neg, "neg", 1));
		} else {
			At();
		}
		while (Arrays.asList("+", "-").contains(CharacterTokensList.get(0).value)) {
			Token currentToken = CharacterTokensList.get(0);
			CharacterTokensList.remove(0);
			At();
			if (currentToken.value.equals("+")) ASTree.add(new Node(Characters.op_plus, "+", 2));
			else ASTree.add(new Node(Characters.op_minus, "-", 2));
		}

	}


	void At() {
		Af();
		while (Arrays.asList("*", "/").contains(CharacterTokensList.get(0).value)) {
			Token currentToken = CharacterTokensList.get(0);
			CharacterTokensList.remove(0);
			Af();
			if (currentToken.value.equals("*")) ASTree.add(new Node(Characters.op_mul, "*", 2));
			else ASTree.add(new Node(Characters.op_div, "/", 2));
		}
	}

	void Af() {
		Ap();
		if (CharacterTokensList.get(0).value.equals("**")) {
			CharacterTokensList.remove(0);
			Af();
			ASTree.add(new Node(Characters.op_pow, "**", 2));
		}
	}

	void Ap() {
		R();
		while (CharacterTokensList.get(0).value.equals("@")) {
			CharacterTokensList.remove(0); //Remove @ operator

			if (!CharacterTokensList.get(0).type.equals(TokenType.IDENTIFIER)) {
				System.out.println("Parsing error at Ap");

			}
			ASTree.add(new Node(Characters.identifier, CharacterTokensList.get(0).value, 0));
			CharacterTokensList.remove(0);

			R();
			ASTree.add(new Node(Characters.at, "@", 3));
		}
	}

	void R() {
		Rn();
		while ((Arrays.asList(TokenType.IDENTIFIER, TokenType.INTEGER, TokenType.STRING).contains(CharacterTokensList.get(0).type))
				|| (Arrays.asList("true", "false", "nil", "dummy").contains(CharacterTokensList.get(0).value))
				|| (CharacterTokensList.get(0).value.equals("("))) {

			Rn();
			ASTree.add(new Node(Characters.gamma, "gamma", 2));

		}
	}

	void Rn() {
		if (CharacterTokensList.get(0).type == TokenType.IDENTIFIER) {
			ASTree.add(new Node(Characters.identifier, CharacterTokensList.get(0).value, 0));
			CharacterTokensList.remove(0);
		} else if (CharacterTokensList.get(0).type == TokenType.INTEGER) {
			ASTree.add(new Node(Characters.integer, CharacterTokensList.get(0).value, 0));
			CharacterTokensList.remove(0);
		} else if (CharacterTokensList.get(0).type == TokenType.STRING) {
			ASTree.add(new Node(Characters.string, CharacterTokensList.get(0).value, 0));
			CharacterTokensList.remove(0);
		} else if (CharacterTokensList.get(0).type == TokenType.KEYWORD) {
			String keyword = CharacterTokensList.get(0).value;
			if (keyword.equals("true")) {
				ASTree.add(new Node(Characters.true_value, keyword, 0));
			} else if (keyword.equals("false")) {
				ASTree.add(new Node(Characters.false_value, keyword, 0));
			} else if (keyword.equals("nil")) {
				ASTree.add(new Node(Characters.nil, keyword, 0));
			} else if (keyword.equals("dummy")) {
				ASTree.add(new Node(Characters.dummy, keyword, 0));
			} else {
				System.out.println("Parse Error at Rn");
			}
			CharacterTokensList.remove(0);
		} else if (CharacterTokensList.get(0).type == TokenType.PUNCTUATION) {
			if (CharacterTokensList.get(0).value.equals("(")) {
				CharacterTokensList.remove(0);
				E();
				if (!CharacterTokensList.get(0).value.equals(")")) {
					System.out.println("Parsing error at Rn");
				}
				CharacterTokensList.remove(0);
			} else {
				System.out.println("Parsing error at Rn");
			}
		} else {
			System.out.println("Parsing error at Rn");
		}
	}

	void D(){
		Da();
		if(CharacterTokensList.get(0).value.equals("within")){
			CharacterTokensList.remove(0);
			D();
			ASTree.add(new Node(Characters.within,"within",2));
		}
	}

	void Da(){
		Dr();
		int n = 1;
		while(CharacterTokensList.get(0).value.equals("and")){
			CharacterTokensList.remove(0);
			Dr();
			n++;
		}
		if(n>1) ASTree.add(new Node(Characters.and,"and",n));
	}


	void Dr(){
		boolean isRec = false;
		if(CharacterTokensList.get(0).value.equals("rec")){
			CharacterTokensList.remove(0);
			isRec = true;
		}
		Db();
		if (isRec) {
			ASTree.add(new Node(Characters.rec,"rec",1));
		}
	}

	void Db() {
		if( CharacterTokensList.get(0).type.equals(TokenType.PUNCTUATION) && CharacterTokensList.get(0).value.equals("(")){
			CharacterTokensList.remove(0);
			D();
			if(!CharacterTokensList.get(0).value.equals(")")) {
				System.out.println("Parsing error at Db");
			}
			CharacterTokensList.remove(0);
		}
		else if(CharacterTokensList.get(0).type.equals(TokenType.IDENTIFIER)){
			if(CharacterTokensList.get(1).value.equals("(") || CharacterTokensList.get(1).type.equals(TokenType.IDENTIFIER)) {
				ASTree.add(new Node(Characters.identifier,CharacterTokensList.get(0).value,0));
				CharacterTokensList.remove(0);

				int n = 1;
				while(CharacterTokensList.get(0).type.equals(TokenType.IDENTIFIER) || CharacterTokensList.get(0).value.equals("(")) {
					Vb();
					n++;
				}
				if(!CharacterTokensList.get(0).value.equals("=")) {
					System.out.println("Parsing error at Db");

				}
				CharacterTokensList.remove(0);
				E();

				ASTree.add(new Node(Characters.fcn_form,"fcn_form",n+1));

			}
			else if (CharacterTokensList.get(1).value.equals("=")) {
				ASTree.add(new Node(Characters.identifier,CharacterTokensList.get(0).value,0));
				CharacterTokensList.remove(0);
				CharacterTokensList.remove(0);
				E();
				ASTree.add(new Node(Characters.equal,"=",2));
			}
			else if (CharacterTokensList.get(1).value.equals(",")) {
				Vl();
				if (!CharacterTokensList.get(0).value.equals("=")) {
					System.out.println("Parsing error at Db");
				}
				CharacterTokensList.remove(0);
				E();

				ASTree.add(new Node(Characters.equal, "=", 2));
			}
		}
	}

	void Vb(){
		if(CharacterTokensList.get(0).type.equals(TokenType.PUNCTUATION) && CharacterTokensList.get(0).value.equals("(")) {
			CharacterTokensList.remove(0);
			boolean isVl=false;

			if(CharacterTokensList.get(0).type .equals(TokenType.IDENTIFIER) ){
				Vl();
				isVl = true;
			}
			if(!CharacterTokensList.get(0).value.equals(")")){
				System.out.println("Unmatched )");
			}

			CharacterTokensList.remove(0);
			if(!isVl) ASTree.add(new Node(Characters.empty_params,"()",0));

		} else if(CharacterTokensList.get(0).type .equals(TokenType.IDENTIFIER) ){
			ASTree.add(new Node(Characters.identifier,CharacterTokensList.get(0).value,0));
			CharacterTokensList.remove(0);
		}

	}

	void Vl() {
		int n = 0;
		for (;;) {
			if (n > 0) {
				CharacterTokensList.remove(0);
			}
			if (!CharacterTokensList.get(0).type.equals(TokenType.IDENTIFIER)) {
				System.out.println("Parse error");
				break;
			}
			ASTree.add(new Node(Characters.identifier, CharacterTokensList.get(0).value, 0));

			CharacterTokensList.remove(0);
			n++;
			if (!CharacterTokensList.get(0).value.equals(",")) {
				break;
			}
		}
		ASTree.add(new Node(Characters.comma, ",", n));
	}


}