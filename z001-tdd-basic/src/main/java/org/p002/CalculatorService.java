package org.p002;

public class CalculatorService {

    public int add(int a, int b) {
        return a + b;
    }

    public boolean isEven(int number) {
        return number % 2 == 0;
    }

    public Object getNull() {
        return null;
    }

    public String getMessage() {
        return "Hello";
    }

    public int[] getNumbers() {
        return new int[]{1, 2, 3};
    }

    public int divide(int a, int b) {
        if(b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }
}
