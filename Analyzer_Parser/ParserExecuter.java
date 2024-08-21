package Analyzer_Parser;

import java.util.List;

public class ParserExecuter {

	public static void main(String[] args) {
	     String inputFileName = "input_text.txt";
	      Analyzer scanner = new Analyzer(inputFileName);
	      List<Token> tokens;
	      List<Node> AST;   
			try {
				tokens = scanner.scan();
				Parser parser = new Parser(tokens);
				AST = parser.parse();
				if(AST == null) return;
				List<String> stringAST = parser.convertAST_toStringAST();
				for (String string : stringAST) {
		            System.out.println(string);
		        }
			} catch (LocalException e) {
				System.out.println(e.getMessage());
			}

	}

}
