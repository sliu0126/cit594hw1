import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class DocumentIteratorTest {

	@Test
	public void testHasNext() {
		Reader r = new StringReader("this is for the test");
		DocumentIterator docuIt = new DocumentIterator(r, 2);
		assertEquals("thisis", docuIt.next());
		assertEquals("isfor", docuIt.next());
		assertEquals("forthe", docuIt.next());
		assertEquals("thetest", docuIt.next());
		
	}

}