/**
 * 
 */
package main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Vector;

/**
 * @author lm
 *
 */
public class FileOperations {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileOperations fileOperations = new FileOperations();

//		fileOperations.byteIO();

//		 try {
//		 fileOperations.charIO();
//		 } catch (IOException e) {
//		   e.printStackTrace();
//		 }

//		 fileOperations.fileIO();

//		 fileOperations.fileCreate("file1.txt");

//		 fileOperations.serializeStrings();
		 fileOperations.serializeStringsByteArray();

//		 fileOperations.serializeVector();
		 fileOperations.serializeVectorByteArray();

//		 fileOperations.serializeObject();
		 fileOperations.serializeObjectByteArray();
	}

	/**
	 * Byte I/O - read raw bytes from 'input.txt and write them to 'output.txt'
	 * 
	 * @throws IOException LOCALLY - the code is significantly longer
	 */
	private void byteIO() {
		FileInputStream in = null;
		FileOutputStream out = null;

		try {
			in = new FileInputStream("input.txt");
			out = new FileOutputStream("output.txt");
			int c;
			while ((c = in.read()) != -1)
				out.write(c);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * Character I/O - read chars (Unicode - 2B) from 'input.txt
	 * and write them* to 'output.txt'
	 * 
	 * @throws IOException - MUST be handled in the caller
	 */
	private void charIO() throws IOException {
		FileReader in = null;
		FileWriter out = null;

		try {
			in = new FileReader("input.txt");
			out = new FileWriter("output.txt");
			int c;
			while ((c = in.read()) != -1)
				out.write(c);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	/** Write strings into a file and read them
	 * 
	 */
	private void fileIO() {
		try {
			String str[] = { "Hello", " world !" };
			OutputStream os = new FileOutputStream("file.txt");
			for (String s : str)
				os.write(s.getBytes()); // writes the bytes in a string object
			os.close();

			InputStream is = new FileInputStream("file.txt");
			int size = is.available();

			for (int i = 0; i < size; i++)
				System.out.print((char) is.read() + "  ");
			is.close();
		} catch (IOException e) {
			System.out.print("IOException");
		}
	}

	private void fileCreate(String fn) {
		try {
			// create new File object
			File f = new File(fn);
			// find the absolute path
			String a = f.getAbsolutePath();
			// prints absolute path and executable bit state
			System.out.println(a + " is executable: " + f.canExecute());
			// prints other info
			System.out.println(a + " is hidden : " + f.isHidden());
			System.out.println(a + " is writable : " + f.canWrite());
			// finally create a file on disk
			System.out.println("createNewFile() returned: " + f.createNewFile());
			Thread.sleep(5000);
			// now delete it
			System.out.println("delete() returned: " + f.delete());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean serialize(Object obj, String path) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
			out.writeObject(obj);
			out.close();
			System.out.println("Serialized data is saved in " + path);
			return true;
		} catch (IOException i) {
			i.printStackTrace();
			return false;
		}
	}

	private Object deserialize(String path) throws ClassNotFoundException {
		Object obj = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
			obj = in.readObject();
			in.close();
			System.out.println("Serialized data is retrieved from " + path);
			return obj;
		} catch (IOException i) {
			i.printStackTrace();
			return obj;
		}
	}

	/**
	 * serializing the object passed as the 1st argument
	 * to a byte array which is returned as the result 
	 * @param obj - object to serialize
	 * @return - byte array
	 */
	private byte[] serialize(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
		  out = new ObjectOutputStream(bos);   
			out.writeObject(obj);
		  out.flush();
		  return bos.toByteArray();
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		finally {
		  try {
		    bos.close();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
	  }
	}
	
	/**
	 * deserializing the object from the byte array passed as the 1st argument 
	 * 
	 * @param path
	 *         - byte array that contains serialized object
	 * @return - the object on success, null on failure
	 */
	private Object deserialize(byte[] bytes) throws ClassNotFoundException {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream in = new ObjectInputStream(bis);
			obj = in.readObject();
			in.close();
			System.out.println("Serialized data is retrieved from bytes array");
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
			return obj;
		}
	}

	// serialize an array of Strings to a byte array 
	void serializeStringsByteArray() {
		String[] names = { "First", "Second", "Third" };
		byte[] bytes = serialize(names);
		if (bytes != null) {
			String[] rd = null;
			try {
				rd = (String[]) deserialize(bytes);
				for (String s : rd)
					System.out.println(s);
			} catch (ClassNotFoundException c) {
				System.out.println("data not found");
				c.printStackTrace();
			}
		}
	}


	// serialize an array of Strings to a file  
	void serializeStrings() {
		String[] names = { "First", "Second", "Third" };
		String path = "./Object.ser";
		if (serialize(names, path) != false) {
			String[] rd = null;
			try {
				rd = (String[]) deserialize(path);
				for (String s : rd)
					System.out.println(s);
			} catch (ClassNotFoundException c) {
				System.out.println("data not found");
				c.printStackTrace();
			}
		}
	}

	// serialize a Vector to a byte array 
	void serializeVectorByteArray() {		
		Vector v = new Vector();
		v.addElement(new Integer(1));
		v.addElement(Integer.valueOf(2));
		v.addElement(Float.valueOf(3.14f));
		v.addElement(Double.valueOf(3.1415d));
		byte[] bytes = serialize(v);
		if (bytes != null) {
			Vector rd;
			try {
				rd = (Vector) deserialize(bytes);
				System.out.println(rd);
			} catch (ClassNotFoundException c) {
				System.out.println("data not found");
				c.printStackTrace();
			}
		}
	}
	
	// serialize a Vector to a file
	void serializeVector() {
		Vector v = new Vector();
		v.addElement(new Integer(1));
		v.addElement(Integer.valueOf(2));
		v.addElement(Float.valueOf(3.14f));
		v.addElement(Double.valueOf(3.1415d));
		String path = "./Object.ser";
		if (serialize(v, path) != false) {
			Vector rd = null;
			try {
				rd = (Vector) deserialize(path);
				System.out.println(rd);
			} catch (ClassNotFoundException c) {
				System.out.println("data not found");
				c.printStackTrace();
			}
		}
	}

	// serialize an Object to a file
	void serializeObject() {
		User o = new User("lm", "********");
		String path = "./Object.ser";
		if (serialize(o, path) != false) {
			User rd;
			try {
				rd = (User) deserialize(path);
				System.out.println(rd);
			} catch (ClassNotFoundException c) {
				System.out.println("data not found");
				c.printStackTrace();
			}
		}
	}
	
	// serialize an Object to a byte array
	void serializeObjectByteArray() {
		User o = new User("lm", "********");
		byte[] bytes = serialize(o); 
		if (bytes != null) {
			User rd;
			try {
				rd = (User) deserialize(bytes);
				System.out.println(rd);
			} catch (ClassNotFoundException c) {
				System.out.println("data not found");
				c.printStackTrace();
			}
		}
	}

}
