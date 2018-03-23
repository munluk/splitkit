package com.splitkit.exception;

/**
 * Exception that to is thrown when errors occur in the split calculations
 */
public class SplitException extends UserInputException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SplitException(){};
    // Constructor that accepts a message
    public SplitException(String message)
    {
       super(message);
    }
}
