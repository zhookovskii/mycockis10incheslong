package expression.parser;

import expression.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpressionParser implements TripleParser {
    @Override
    public TripleExpression parse(String expression) {
        List<Element> tokens = analysis(expression);
        Iterator iterator = new Iterator(tokens);
        return expr(iterator);
    }

    private List<Element> analysis(String expression) {
        Map<Character, Token> map = Map.of(
                '/', Token.DIV,
                '+', Token.PLUS,
                '-', Token.MINUS,
                '*', Token.MULTIPLY,
                '(', Token.LEFT,
                ')', Token.RIGHT,
                'x', Token.VAR,
                'y', Token.VAR,
                'z', Token.VAR
        );
        List<Element> tokens = new ArrayList<>();
        int pos = 0;
        int line = 0;
        while (pos < expression.length()) {
            char curr = expression.charAt(pos);
            if (map.get(curr) != null) {
                tokens.add(new Element(map.get(curr), curr, pos, line));
                pos++;
            } else {
                if (curr >= '0' && curr <= '9' || curr == '.') {
                    StringBuilder builder = new StringBuilder();
                    do {
                        builder.append(curr);
                        pos++;
                        if (pos >= expression.length()) {
                            break;
                        }
                        curr = expression.charAt(pos);
                    } while (curr >= '0' && curr <= '9' || curr == '.');
                    tokens.add(new Element(Token.NUM, builder.toString(), pos, line));
                } else {
                    if (curr == 'l') {
                        if (expression.charAt(pos + 1) == '0') {
                            tokens.add(new Element(Token.LEAD, "l0", pos, line));
                            pos += 2;
                        } else {
                            throw new IllegalArgumentException("Expected l0, found: " + expression.substring(pos));
                        }
                    } else {
                        if (curr == 't') {
                            if (expression.charAt(pos + 1) == '0') {
                                tokens.add(new Element(Token.TAIL, "t0", pos, line));
                                pos += 2;
                            } else {
                                throw new IllegalArgumentException("Expected t0, found: " + expression.substring(pos));
                            }
                        } else {
                            if (curr == 'm') {
                                if (expression.charAt(pos + 1) == 'i' && expression.charAt(pos + 2) == 'n') {
                                    tokens.add(new Element(Token.MIN, "min", pos, line));
                                    pos += 3;
                                } else {
                                    if (expression.charAt(pos + 1) == 'a' && expression.charAt(pos + 2) == 'x') {
                                        tokens.add(new Element(Token.MAX, "max", pos, line));
                                        pos += 3;
                                    } else {
                                        if (!Character.isWhitespace(curr)) {
                                            throw new IllegalArgumentException("Unexpected symbol at "
                                                    + pos + " " + expression.substring(pos));
                                        }
                                        pos++;
                                    }
                                }
                            } else {
                                if (!Character.isWhitespace(curr)) {
                                    throw new IllegalArgumentException("Unexpected symbol at "
                                            + pos + " " + expression.substring(pos));
                                }
                                pos++;
                            }
                        }
                    }
                }
            }
        }
        tokens.add(new Element(Token.END, "", pos, line));
        return tokens;
    }

    private static class Iterator {
        private int pos;
        public List<Element> elements;

        public Iterator(List<Element> elements) {
            this.elements = elements;
        }

        public Element next() {
            return elements.get(pos++);
        }

        public void prev() {
            pos--;
        }

        public int getPos() {
            return pos;
        }
    }

    public NewExpression factor(Iterator iterator) {
        Element element = iterator.next();
        switch (element.token) {
            case LEAD:
                NewExpression lead = factor(iterator);
                return new LeadNulls(lead);
            case TAIL:
                NewExpression tail = factor(iterator);
                return new TailNulls(tail);
            case MINUS:
                Token next_token = iterator.next().token;
                iterator.prev();
                NewExpression value = factor(iterator);
                if (next_token == Token.LEFT) {
                    return new Negate(new BracketExpression(value));
                } else {
                    return new Negate(value);
                }
            case VAR:
                return new Variable(element.input);
            case NUM:
                return new Const(element.input);
            case LEFT:
                value = expr(iterator);
                element = iterator.next();
                if (element.token != Token.RIGHT) {
                    throw new IllegalArgumentException("Expected closing bracket at " + iterator.getPos());
                }
                return value;
            default:
                throw new IllegalArgumentException("Expected closing bracket at " + iterator.getPos());
        }
    }

    public NewExpression multdiv(Iterator iterator) {
        NewExpression value = factor(iterator);
        while (true) {
            Element element = iterator.next();
            switch (element.token) {
                case MULTIPLY:
                    value = new Multiply(value, factor(iterator));
                    break;
                case DIV:
                    value = new Divide(value, factor(iterator));
                    break;
                default:
                    iterator.prev();
                    return value;
            }
        }
    }

    public NewExpression plusminus(Iterator iterator) {
        NewExpression value = multdiv(iterator);
        while (true) {
            Element element = iterator.next();
            switch (element.token) {
                case PLUS:
                    value = new Add(value, multdiv(iterator));
                    break;
                case MINUS:
                    value = new Subtract(value, multdiv(iterator));
                    break;
                default:
                    iterator.prev();
                    return value;
            }
        }
    }

    public NewExpression expr(Iterator iterator) {
        Element element = iterator.next();
        if (element.token == Token.END) {
            return new Const("0");
        }
        iterator.prev();
        return minMax(iterator);
    }

    public NewExpression minMax(Iterator iterator) {
        NewExpression value = plusminus(iterator);
        while (true) {
            Element element = iterator.next();
            switch (element.token) {
                case MIN:
                    value = new Min(value, plusminus(iterator));
                    break;
                case MAX:
                    value = new Max(value, plusminus(iterator));
                    break;
                default:
                    iterator.prev();
                    return value;
            }
        }
    }
}