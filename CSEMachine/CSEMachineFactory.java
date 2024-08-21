package CSEMachine;

import java.util.ArrayList;
import java.util.Stack;

import Standardizer.AST;
import Standardizer.Node;

public class CSEMachineFactory {
    private E eSymbol = new E(0);
    private int i = 1;
    private int j = 0;
    
    public CSEMachineFactory() {
        
    }

    public Symbol getSymbol(Node node) {
        String data = node.getData();
        if (data.equals("not") || data.equals("neg")) {
            return new Uop(data);
        }
        if (data.equals("+") || data.equals("-") || data.equals("*") ||
                data.equals("/") || data.equals("**") || data.equals("&") ||
                data.equals("or") || data.equals("eq") || data.equals("ne") ||
                data.equals("ls") || data.equals("le") || data.equals("gr") ||
                data.equals("ge") || data.equals("aug")) {
            return new Bop(data);
        }

        if (data.equals("gamma")) {
            return new Gamma();
        }
        if (data.equals("tau")) {
            return new Tau(node.children.size());
        }
        if (data.equals("<Y*>")) {
            return new Ystar();
        }

        if (data.startsWith("<ID:")) {
            return new Id(data.substring(4, data.length() - 1));
        }
        if (data.startsWith("<INT:")) {
            return new Int(data.substring(5, data.length() - 1));
        }
        if (data.startsWith("<STR:")) {
            return new Str(data.substring(6, data.length() - 2));
        }
        if (data.startsWith("<nil")) {
            return new Tup();
        }
        if (data.equals("<true>")) {
            return new Bool("true");
        }
        if (data.equals("<false>")) {
            return new Bool("false");
        }
        if (data.equals("<dummy>")) {
            return new Dummy();
        }

        System.out.println("Err node: " + data);
        return new Err();
    }


    public B getB(Node node) {
        B b = new B();
        b.symbols = this.getPreOrderTraverse(node);
        return b;
    }

    public Lambda getLambda(Node node) {
        Lambda lambda = new Lambda(this.i++);
        lambda.setDelta(this.getDelta(node.children.get(1)));
        Node identifierNode = node.children.get(0);

        if (",".equals(identifierNode.getData())) {
            for (Node identifier : identifierNode.children) {
                lambda.identifiers.add(new Id(identifier.getData().substring(4, identifier.getData().length() - 1)));
            }
        } else {
            lambda.identifiers.add(new Id(identifierNode.getData().substring(4, identifierNode.getData().length() - 1)));
        }

        return lambda;
    }


    private ArrayList<Symbol> getPreOrderTraverse(Node node) {
        ArrayList<Symbol> symbols = new ArrayList<>();

        Stack<Node> stack = new Stack<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            if ("lambda".equals(currentNode.getData())) {
                symbols.add(this.getLambda(currentNode));
            } else if ("->".equals(currentNode.getData())) {
                symbols.add(this.getDelta(currentNode.children.get(1)));
                symbols.add(this.getDelta(currentNode.children.get(2)));
                symbols.add(new Beta());
                symbols.add(this.getB(currentNode.children.get(0)));
            } else {
                symbols.add(this.getSymbol(currentNode));
                for (int i = currentNode.children.size() - 1; i >= 0; i--) {
                    stack.push(currentNode.children.get(i));
                }
            }
        }

        return symbols;
    }

    public Delta getDelta(Node node) {
        Delta delta = new Delta(this.j++);
        delta.symbols = this.getPreOrderTraverse(node);
        return delta;        
    }
    
    public ArrayList<Symbol> getControl(AST ast) {
        ArrayList<Symbol> control = new ArrayList<Symbol>();
        control.add(this.eSymbol);
        control.add(this.getDelta(ast.getRoot()));
        return control;
    }
    
    public ArrayList<Symbol> getStack() {
        ArrayList<Symbol> stack = new ArrayList<Symbol>();
        stack.add(this.eSymbol);
        return stack;
    }
    
    public ArrayList<E> getEnvironment() {
        ArrayList<E> environment = new ArrayList<E>();
        environment.add(this.eSymbol);
        return environment;
    }
    
    public CSEMachine getCSEMachine(AST ast) {        
        return new CSEMachine(this.getControl(ast), this.getStack(), this.getEnvironment());
    }
}
