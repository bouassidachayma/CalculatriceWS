package service;

import javax.jws.WebService;

@WebService(endpointInterface = "service.ICalculatrice") //  connects the implementation to the interface that defines the service contract.
public class Calculatrice implements ICalculatrice {

    @Override // indicates that somme() implements the interface method.
    public double somme(double a, double b) {
        return a + b;
    }

    @Override
    public double multiplication(double a, double b) {
        return a * b;
    }

    @Override
    public double soustraction(double a, double b) {
        return a - b;
    }

    @Override
    public double division(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException(
                    "Division by zero is not allowed."
            );
        }
        return a / b;
    }
}
