package org.lele.product.exception;

/**
 * org.lele.product.exception
 *
 * @author: lele
 * @date: 2020-05-26
 */
public class MallProductException extends Exception{
    public MallProductException( String message ){
        super(message);
    }
    public MallProductException( Exception e ){
        super(e);
    }
}
