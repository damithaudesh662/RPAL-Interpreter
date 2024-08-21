package Analyzer_Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Analyzer {
    private String inputFileName;
    private List<Token> tokens;	

    public Analyzer(String inputFileName) {
        this.inputFileName = inputFileName;
        tokens = new ArrayList<>();
    }

    public List<Token> scan() throws LocalException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            int lineCount=0;
            while ((line = reader.readLine()) != null) {
            	lineCount++;
                try {
					tokenizeLine(line);
				} catch (LocalException e) {
					throw new LocalException(e.getMessage()+" in LINE: "+lineCount+"\nERROR in lexical_analysis.");
				}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tokens;
    }

    private void tokenizeLine(String line) throws LocalException {
    	String digit = "[0-9]";
    	String letter = "[a-zA-Z]";
    	Pattern operatorSymbol = Pattern.compile("[+\\-*/<>&.@/:=~|$!#%^_\\[\\]{}\"`\\?]");
    	Pattern escape = Pattern.compile("(\\\\'|\\\\t|\\\\n|\\\\\\\\)");
    	
        Pattern identifierPattern = Pattern.compile(letter+"("+letter+"|"+digit+"|"+"_)*");
        Pattern integerPattern = Pattern.compile(digit + "+");
        Pattern operatorPattern = Pattern.compile(operatorSymbol + "+");
        
        Pattern punctuationPattern = Pattern.compile("[(),;]");
        Pattern spacesPattern = Pattern.compile("(\\s|\\t)+");
        
        Pattern stringPattern = Pattern.compile("'("+letter+"|"+digit+"|"+operatorSymbol+"|"+escape+"|"+punctuationPattern+"|"+spacesPattern+")*'");
        Pattern commentPattern = Pattern.compile("//.*");


        Matcher matcher;
        
        int currentIndex = 0;	
        while (currentIndex < line.length()) {
            char currentChar = line.charAt(currentIndex);

            Matcher spaceMatcher = spacesPattern.matcher(line.substring(currentIndex));
            Matcher commentMatcher = commentPattern.matcher(line.substring(currentIndex));
            if(commentMatcher.lookingAt()) {
            	String comment = commentMatcher.group();
            	currentIndex += comment.length();
            	continue;
            }
            if (spaceMatcher.lookingAt()) {
            	String space = spaceMatcher.group();
          		currentIndex += space.length();
          		continue;
            }

            matcher = identifierPattern.matcher(line.substring(currentIndex));
            if (matcher.lookingAt()) {
                String identifier = matcher.group();

                List<String> keywords = List.of(
                        "let", "in", "fn", "where", "aug", "or", "not", "gr", "ge", "ls", 
                        "le", "eq", "ne", "true", "false", "nil", "dummy", "within", "and", "rec"
                    );
            if(keywords.contains(identifier))
                	tokens.add(new Token(TokenType.KEYWORD, identifier));
                else
                	tokens.add(new Token(TokenType.IDENTIFIER, identifier));
                currentIndex += identifier.length();
                continue;
            }

            matcher = integerPattern.matcher(line.substring(currentIndex));
            if (matcher.lookingAt()) {
                String integer = matcher.group();
                tokens.add(new Token(TokenType.INTEGER, integer));
                currentIndex += integer.length();
                continue;
            }

            matcher = operatorPattern.matcher(line.substring(currentIndex));
            if (matcher.lookingAt()) {
                String operator = matcher.group();
                tokens.add(new Token(TokenType.OPERATOR, operator));
                currentIndex += operator.length();
                continue;
            }

            matcher = stringPattern.matcher(line.substring(currentIndex));
            if (matcher.lookingAt()) {
                String string = matcher.group();
                tokens.add(new Token(TokenType.STRING, string));
                currentIndex += string.length();
                continue;
            }

            matcher = punctuationPattern.matcher(Character.toString(currentChar));
            if (matcher.matches()) {
                tokens.add(new Token(TokenType.PUNCTUATION, Character.toString(currentChar)));
                currentIndex++;
                continue;
            }

            throw new LocalException("failed to tokenize : "+currentChar+" at INDEX: "+currentIndex);
            	
        }    
    }

}
