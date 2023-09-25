import static org.junit.jupiter.api.Assertions.*;

class ArithmeticSolverTest {

    @org.junit.jupiter.api.Test
    void solveTest() {
        ArithmeticSolver solver = new ArithmeticSolver();
        double result = solver.solve("2 + 5 - 4");
        assertEquals(3.0, result, 0.0001);

        result = solver.solve(" (3 + 4) * 7");
        assertEquals(49.0, result, 0.0001);

        result = solver.solve(" ((3 + 4) * 3) + 7");
        assertEquals(28.0, result, 0.0001);

        result = solver.solve(" ((3 + 4) * 3) / 5");
        assertEquals(4.2, result, 0.0001);


    }
}