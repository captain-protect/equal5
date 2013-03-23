package engine.calculation.vector.opeartions;

import engine.calculation.functions.MathFunctionType;
import engine.calculation.functions.MathFunctionTypeVisitor;

/**
 * User: Oleksiy Pylypenko
 * At: 3/13/13  4:37 PM
 */
public class MathVectorOperation extends VectorOperation {
    private MathFunctionType type;
    private int[] slots;
    private int resultSlot;

    public MathVectorOperation(MathFunctionType type, int[] slots, int resultSlot) {
        this.type = type;
        this.slots = slots;
        this.resultSlot = resultSlot;
    }

    @Override
    public void apply(int size, double[][] data) {
        type.accept(new MathVectorCalc(size, data));
    }

    @Override
    public boolean applicable(boolean[] calculated) {
        for (int slot : slots) {
            if (!calculated[slot]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void markCalculated(boolean[] calculatedSlots) {
        calculatedSlots[resultSlot] = true;
    }

    private class MathVectorCalc implements MathFunctionTypeVisitor {
        private final int size;
        private final double[][] data;
        private final double[] resultVector;
        private final double[] inputVector;

        public MathVectorCalc(int size, double[][] data) {
            this.size = size;
            this.data = data;

            resultVector = data[resultSlot];
            inputVector = data[slots[0]];
        }

        @Override
        public void sin() {
            for (int i = 0; i < size; i++) {
                resultVector[i] = Math.sin(inputVector[i]);
            }
        }

        @Override
        public void cos() {
            for (int i = 0; i < size; i++) {
                resultVector[i] = Math.cos(inputVector[i]);
            }
        }

        @Override
        public void signum() {
            for (int i = 0; i < size; i++) {
                resultVector[i] = Math.signum(inputVector[i]);
            }
        }

        @Override
        public void identity() {
            System.arraycopy(inputVector, 0, resultVector, 0, size);
        }
    }
}
