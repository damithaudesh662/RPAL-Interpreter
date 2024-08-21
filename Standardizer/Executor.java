package Standardizer;

import java.util.ArrayList;
import java.util.List;

import Analyzer_Parser.*;

import Analyzer_Parser.Node;
import CSEMachine.CSEMachine;
import CSEMachine.CSEMachineFactory;


public class Executor {
	public static String evaluvate(String filename, boolean isPrintAST, boolean isPrintST){

		Analyzer scanner = new Analyzer(filename);
		List<Token> tokens;
		List<Node> AST;
		try {
			tokens = scanner.scan();
			if(tokens.isEmpty()){
				System.out.println("This is an empty program");
				return "";
			}
			Parser parser = new Parser(tokens);
			AST=parser.parse();

			ArrayList<String> stringAST = parser.convertAST_toStringAST();
			if(isPrintAST){
				for(String string: stringAST){
					System.out.println(string);
				}
			}

			ASTFactory astf =new  ASTFactory();
			AST ast = astf.getAbstractSyntaxTree(stringAST);

			ast.standardize();
			if(isPrintST) ast.printAst();

			CSEMachineFactory factory = new CSEMachineFactory();
			CSEMachine csemachine = factory.getCSEMachine(ast);
			return csemachine.getAnswer();
		} catch (LocalException e) {
			System.out.println(e.getMessage());
		}

		return null;



	}
}