package engine.calculation;

import engine.calculation.functions.*;
import engine.expressions.Equation;
import engine.expressions.Function;
import engine.expressions.ParboiledExpressionParser;
import engine.expressions.ParsingException;
import engine.locus.PixelDrawable;
import engine.locus.RectRange;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.fail;

/**
 * User: Oleksiy Pylypenko
 * Date: 2/8/13
 * Time: 10:24 PM
 */
public class VectorCalculationEngineTest {
    private static final File DIR = new File("testimgs");

    private CalculationEngine eng;

    @Before
    public void setUp() throws Exception {
        DIR.mkdirs();
        eng = new VectorCalculationEngine();
    }

    @Test
    public void testCalculate() throws Exception {
        Equation eq1 = eq("y", "1-(x/2-0.5)*(x/2-0.5)");
        Equation eq2 = eq("(x-0.5)*(x-0.5)+(y-0.5)*(y-0.5)", "0.1");
        Equation eq3 = eq("(x-0.5)*(x-0.5)*2+(y-0.5)*(y-0.5)", "0.1");
        draw("test1.png", eq1);
        draw("test2.png", eq2);
        draw("test3.png", eq3);
    }


    void draw(String file, Equation... eqs) {
        RectRange size = new RectRange(0, 0, 1000, 1000);

        PixelDrawable []drawables = eng.calculate(size.getWidth(), size.getHeight(), eqs);

        DrawToImage drawer = new DrawToImage(size);
        for (PixelDrawable drawable : drawables) {
            drawable.draw(drawable.getSize(), drawer);
        }

        try {
            drawer.writePng(new File(DIR, file));
        } catch (IOException e) {
            fail("IOException: " + e);
        }
    }

    private Equation eq(String left, String right) {
        ParboiledExpressionParser ep = new ParboiledExpressionParser();
        try {
            Function leftExpr = ep.parseFunction(left);
            Function rightExpr = ep.parseFunction(right);
            return new Equation(leftExpr, Equation.Type.EQUAL, rightExpr);
        } catch (ParsingException e) {
            throw new RuntimeException(e);
        }
    }
}
