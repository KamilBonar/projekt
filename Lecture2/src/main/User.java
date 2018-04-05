/**
 * 
 */
package main;

import java.io.Serializable;

/** This class was intended to demonstrate how to serialize the objects of any class
 * @author lm
 *
 */
public class User implements Serializable {	// we have to implement Serializable
	String name;
	String pass;
	
	public User(String name, String pass) {
		super();
		this.name = name;
		this.pass = pass;
	}

	public String getName() {
		return name;
	}

	public String getPass() {
		return pass;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", pass=" + pass + "]";
	}
}
