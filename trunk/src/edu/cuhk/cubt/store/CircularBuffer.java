package edu.cuhk.cubt.store;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

/**
 * A Circular Buffer implementation, used to store data temporally 
 * @author PKM
 * @see http://stackoverflow.com/questions/590069/how-would-you-code-an-efficient-circular-buffer-in-java-or-c
 * @param <T>
 */
public class CircularBuffer<T> {
	
	T[] buffer;
	//private int tail;	//tail is not useful, it is always 0 when not full, and always equal to head when full.
	int head;
	boolean full;
	
	@SuppressWarnings("unchecked")
	public CircularBuffer(int bufferSize){
		buffer = (T[])new Object[bufferSize];
		//tail = 0;
		head = 0;
		full = false;
	}
	
	public synchronized void add(T toAdd){
		buffer[head++] = toAdd;
		full = (full || head == buffer.length);
		
		//if(full) tail = (tail + 1) % buffer.length; 
		head = head % buffer.length;
	}
	
	public synchronized T getLast(int n){
		if(n >= buffer.length) throw new BufferOverflowException();
		int ptr = head - n - 1; 
		if(ptr < 0){
			if(!full) throw new BufferUnderflowException();
			else ptr += buffer.length; 
		}
		
		return buffer[ptr];
	}
	
	public synchronized T getLast(){
		return getLast(0);
	}
	
	/**
	 * returns the size of buffer
	 * @return the size of buffer
	 */
	public int size(){
		return buffer.length;
	}
	
	/**
	 * returns the number of item in buffer
	 * @return the number of item in buffer
	 */
	public synchronized int count(){
		return (full)? buffer.length : head;
	}
	
}
