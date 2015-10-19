package com.bentoweymouth.mickeyhli.bentodeviceorder.util;

/**
 * Created by mickeyhli on 18/10/15.
 */
public class Tuple<S,T> {
    private S s;
    private T t;

    public Tuple(S s,T t){
        this.s = s;
        this.t = t;
    }

    public S getFirst(){
        return s;
    }

    public T getSecond(){
        return t;
    }

    public String toString(){
        return s.toString() + "  " + t.toString();
    }
}
