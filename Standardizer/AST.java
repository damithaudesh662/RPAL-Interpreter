package Standardizer;

import java.util.ArrayList;

public class AST {
    private Node root;
    
    public AST(Node root) {
        this.setRoot(root);
    }
    
    public void setRoot(Node root) {
        this.root = root;
    }
    
    public Node getRoot() {
        return this.root;
    }
    
    public void standardize() {  
        if (!this.root.isStandardized) {
            this.root.standardize();
        }
    }

    private void preOrderTraverse(Node node, int depth) {
        StringBuilder indent = new StringBuilder();
        for (int n = 0; n < depth; n++) {
            indent.append(".");
        }
        System.out.println(indent + node.getData());

        for (Node child : node.children) {
            preOrderTraverse(child, depth + 1);
        }
    }


    public void printAst() {
        this.preOrderTraverse(this.getRoot(), 0);
    }
}

class ASTFactory {
    public ASTFactory() {

    }

    public AST getAbstractSyntaxTree(ArrayList<String> data) {
        Node root = NodeFactory.getNode(data.get(0), 0);
        Node previous_node = root;
        int current_depth = 0;

        for (String s: data.subList(1, data.size())) {
            int i = 0;                                                          
            int d = 0;                                                          

            while (s.charAt(i) == '.') {
                d++;
                i++;
            }

            Node current_node = NodeFactory.getNode(s.substring(i), d);

            if (current_depth < d) {
                previous_node.children.add(current_node);
                current_node.setParent(previous_node);
            } else {
                while (previous_node.getDepth() != d) {
                    previous_node = previous_node.getParent();
                }
                previous_node.getParent().children.add(current_node);
                current_node.setParent(previous_node.getParent());
            }

            previous_node = current_node;
            current_depth = d;
        }
        return new AST(root);
    }
}
