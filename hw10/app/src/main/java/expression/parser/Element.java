package expression.parser;

public class Element {
    public Token token;
    public String input;
    public int pos;
    public int line;

    public Element(Token token, String str, int pos, int line)
    {
        this.token = token;
        this.input = str;
        this.pos = pos;
        this.line = line;
    }

    public Element(Token token, Character ch, int pos, int line) {
        this.token = token;
        this.input = ch.toString();
        this.pos = pos;
        this.line = line;
    }

}
