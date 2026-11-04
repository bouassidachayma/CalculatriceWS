package service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService  // identifies the interface as a SOAP web service contract.
public interface ICalculatrice {

    @WebMethod  // exposes somme() as an operation clients can call.
    double somme(
            // @WebParam: gives the input parameters meaningful names in the SOAP request.
            // double: allows the operation to return a numeric result.
            @WebParam(name = "a") double a,
            @WebParam(name = "b") double b
    );

    @WebMethod double multiplication(
            @WebParam(name = "a") double a,
            @WebParam(name = "b") double b
    );

    @WebMethod double soustraction(
            @WebParam(name = "a") double a,
            @WebParam(name = "b") double b
    );

    @WebMethod double division(
            @WebParam(name = "a") double a,
            @WebParam(name = "b") double b
    );

}