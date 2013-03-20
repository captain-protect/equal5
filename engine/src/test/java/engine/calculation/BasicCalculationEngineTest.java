package engine.calculation;

import engine.calculation.evaluator.FunctionEvaluator;
import engine.calculation.evaluator.ImmediateFunctionEvaluator;
import engine.calculation.functions.*;
import engine.calculation.tasks.CalculationParameters;
import engine.calculation.tasks.CalculationResults;
import engine.calculation.tasks.ViewportBounds;
import engine.calculation.tasks.ViewportSize;
import engine.expressions.Equation;
import engine.locus.DrawToImage;
import engine.locus.PixelDrawable;
import engine.locus.RectRange;
import org.junit.Test;

import java.io.File;

/**
 * User: Oleksiy Pylypenko
 * Date: 2/8/13
 * Time: 10:24 PM
 */
public class BasicCalculationEngineTest {
    @Test
    public void testCalculate() throws Exception {
        FunctionEvaluator evaluator = new ImmediateFunctionEvaluator();
        BasicCalculationEngine eng = new BasicCalculationEngine(evaluator);

        Equation eq = new Equation(new Variable("y"),
                                Equation.Type.EQUAL,
                                new Subtraction(new Constant(1),
                                new Power(
                                new Addition(new Division(new Variable("x"), new Constant(2)),
                                        new Constant(-0.5)),
                                        new Constant(2))));

        ViewportSize size = new ViewportSize(800, 800);
        CalculationParameters params = new CalculationParameters(
                new ViewportBounds(0, 0, 1, 1),
                size, eq);
        CalculationResults results = eng.calculate(params);

        RectRange range = RectRange.fromViewportSize(size);
        DrawToImage drawer = new DrawToImage(range);
        for (PixelDrawable drawable : results.getDrawables()) {
            drawable.draw(range, drawer);
        }
        drawer.writePng(new File("test1.png"));
    }

}
