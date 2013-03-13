package engine.calculation.functions;

import engine.expressions.Function;
import engine.calculation.FunctionVisitor;

/**
 * User: Oleksiy Pylypenko
 * At: 2/8/13  1:51 PM
 */
public class Power extends BinaryOperator {
    public Power(Function leftSide, Function rightSide) {
        super(leftSide, Type.POWER, rightSide);
    }

    @Override
    public void accept(FunctionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Power)) return false;

        return super.equals(o);
    }
}