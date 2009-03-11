package com.smartitengineering.util.simple;

import com.smartitengineering.util.simple.io.StringInputStream;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class StringInputStreamTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StringInputStreamTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( StringInputStreamTest.class  );
    }

    public void testStream()
    {
        try {
            new StringInputStream(null);
            fail("Should not be able to construct!");
        }
        catch(IllegalArgumentException exception) {
        }
        catch(Exception exception) {
            fail(exception.getMessage());
        }
        String string = "test";
        InputStream stream = new StringInputStream(string);
        for(int i = 0; i < string.length(); ++i) {
            try {
                assertEquals(string.charAt(i), (char) stream.read());
            }
            catch (IOException ex) {
                fail(ex.getMessage());
            }
        }
    }
    
    public void testAvailable() {
        String string = "test";
        InputStream stream = new StringInputStream(string);
        try {
            assertEquals(string.length(), stream.available());
            stream.read();
            assertEquals(string.length() - 1, stream.available());
            stream.mark(4);
            stream.read(new byte[2]);
            assertEquals(string.length() - 3, stream.available());
            stream.reset();
            assertEquals(string.length() - 1, stream.available());
            stream.mark(1);
            stream.read(new byte[3], 0, 2);
            assertEquals(string.length() - 3, stream.available());
            stream.reset();
            assertEquals(string.length() - 1, stream.available());
            assertEquals(string.length() - 1, stream.read(new byte[stream.available()]));
        }
        catch (IOException ex) {
            fail(ex.getMessage());
        }
        
    }
}
