package org.example;

public interface Distance<T1, T2> {
    double calculate(T1 f1, T2 f2);
    //generic types representing the two objects for which we are calculating the distance
}
