package engine.calculation.functions;

import engine.calculation.AbstractFunction;
import engine.calculation.FunctionVisitor;

/**
 * User: Oleksiy Pylypenko
 * At: 2/8/13  1:48 PM
 */
public class Constant extends AbstractFunction {
    private final double value;

    public Constant(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public void accept(FunctionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Constant)) return false;

        Constant constant = (Constant) o;

        if (Double.compare(constant.value, value) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        long temp = value != +0.0d ? Double.doubleToLongBits(value) : 0L;
        return (int) (temp ^ (temp >>> 32));
    }
}