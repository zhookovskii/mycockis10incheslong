package expression;

import java.util.Objects;

public abstract class AbstractOperation {
    protected final NewExpression first;
    protected final NewExpression second;

    public AbstractOperation(NewExpression first, NewExpression second) {
        this.first = first;
        this.second = second;
    }

    public String toString(String operation) {
        return "(" + first.toString() + " " + operation + " " + second.toString() + ")";
    }


    @Override
    public boolean equals(Object e) {
        if (e instanceof AbstractOperation) {
            return ((AbstractOperation) e).first.equals(this.first) && ((AbstractOperation) e).second.equals(this.second)
                    && this.getClass() == e.getClass();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second, this.getClass());
    }
}
