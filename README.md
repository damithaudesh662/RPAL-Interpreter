# RPAL-Interpreter

# Problem Description
You are required to implement a lexical analyzer and a parser for the RPAL language. Refer
RPAL_Lex.pdf for the lexical rules and RPAL_Grammar.pdf for the grammar details.
You should not use ‘lex’. ‘yacc’ or any such tool.
Output of the parser should be the Abstract Syntax Tree (AST) for the given input program. Then
you need to implement an algorithm to convert the Abstract Syntax Tree (AST) in to Standardize
Tree (ST) and implement CSE machine.
Your program should be able to read an input file which contains a RPAL program.
Output of your program should match the output of “rpal.exe“ for the relevant program. 

# Input Format
let Sum(A) = Psum (A,Order A )
where rec Psum (T,N) = N eq 0 -> 0
 | Psum(T,N-1)+T N
in Print ( Sum (1,2,3,4,5) )

# Execution
javac mypal.java
java mypal.java input.txt
java mypal.java -ast input.txt
