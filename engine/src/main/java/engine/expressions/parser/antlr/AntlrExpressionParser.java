package engine.expressions.parser.antlr;

import engine.expressions.parser.*;
import engine.expressions.parser.auto_complete.AutocompletionParser;
import org.antlr.runtime.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Oleksiy Pylypenko
 * Date: 3/30/13
 * Time: 10:39 AM
 */
public class AntlrExpressionParser implements ExpressionParser {
    private Map<String, Double> knownConstants = new HashMap<String, Double>();
    private List<String> varList = null;

    @Override
    public Object parse(ClauseType clause, String expression) throws ParsingException {
        try {
            CommonTokenStream tokens = lexer(expression);
            ParserRuleReturnScope ruleReturn = parser(clause, tokens);
            return builder(clause, ruleReturn);
        }catch (ParsingException ex) {
            if (ex.getExpression() == null) {
                ex.setExpression(expression);
            }
            throw ex;
        }
    }

    private CommonTokenStream lexer(String expression) throws ParsingException {
        EqualLexer lex = new EqualLexer(new ANTLRStringStream(expression));
        CommonTokenStream tokens = new CommonTokenStream(lex);
        tokens.fill();
        List<SyntaxError> errors = lex.getSyntaxErrors();
        if (!errors.isEmpty()) {
            throw new ParsingException(errors);
        }
        return tokens;
    }

    private ParserRuleReturnScope parser(ClauseType clause, CommonTokenStream tokens) throws ParsingException {
        EqualParser parser = new EqualParser(tokens);
        ParserRuleReturnScope ruleReturn = null;

        try {
            ruleReturn = clause.accept(parser);
            parser.theEnd();
        } catch (antlr.RecognitionException e) {
            if (parser.getSyntaxErrors().isEmpty()) {
                throw new RuntimeException("parser failed but no errors recorded", e);
            }
        } catch(Exception e) {
            throw new RuntimeException("EqualParser problem" ,e);
        }
        List<SyntaxError> errors = parser.getSyntaxErrors();
        if (!errors.isEmpty()) {
            List<SyntaxError> newErrors = new ArrayList<SyntaxError>();
            for (SyntaxError error : errors) {
                newErrors.add(new SyntaxError(error.getLine(),
                        error.getColumn(), error.getStartIndex(),
                        error.getEndIndex(),
                        SyntaxErrorMessages.incorrectExpression()));
            }
            throw new ParsingException(newErrors);
        }
        return ruleReturn;
    }

    private Object builder(ClauseType clause, ParserRuleReturnScope ruleReturn) throws ParsingException {
        AntlrExpressionBuilder builder = new AntlrExpressionBuilder(knownConstants, varList);
        Object result = null;
        try {
            result = builder.build(clause, ruleReturn);
        } catch (ExpressionBuilderFailure failure) {
            if (builder.getErrors().isEmpty()) {
                throw new RuntimeException("parser failed but no errors recorded", failure);
            }
        }
        if (!builder.getErrors().isEmpty()) {
            throw new ParsingException(builder.getErrors());
        }
        return result;
    }

    @Override
    public AutocompletionParser createAutocompletionParser() {
        return new AntlrAutocompletionParser();
    }

    @Override
    public void setKnownConstants(Map<String, Double> knownConstants) {
        if (knownConstants == null) {
            throw new IllegalArgumentException("knownConstants");
        }
        this.knownConstants = knownConstants;
    }

    @Override
    public void setVarList(List<String> varList) {
        this.varList = varList;
    }

    @Override
    public Map<String, Double> getKnownConstants() {
        return knownConstants;
    }

    @Override
    public List<String> getVarList() {
        return varList;
    }
}
