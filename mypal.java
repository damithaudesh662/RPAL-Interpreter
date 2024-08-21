import Standardizer.Executor;

public class mypal {
    public static void main(String[] args) {
        String filename;
        boolean isPrintAST = false, isPrintST = false;

        if (args.length == 0) {
            filename = "input_text.txt";
            isPrintAST = true;
            isPrintST = true;
        } else if (args.length == 1) {
            filename = args[0];
        } else if (args.length == 2 && args[0].equalsIgnoreCase("-ast")) {
            filename = args[1];
            isPrintAST = true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("-st")) {
            filename = args[1];
            isPrintST = true;
        } else if (args.length == 3 && ((args[0].equalsIgnoreCase("-ast") && args[1].equalsIgnoreCase("-st")) ||
                (args[0].equalsIgnoreCase("-st") && args[1].equalsIgnoreCase("-ast")))) {
            filename = args[2];
            isPrintAST = true;
            isPrintST = true;
        } else {
            System.out.println("Invalid arguments passed!");
            return;
        }

        // Evaluate the program
        System.out.println(Executor.evaluvate(filename, isPrintAST, isPrintST));
    }
}
