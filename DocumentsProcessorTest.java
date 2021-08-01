import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Test;

public class DocumentsProcessorTest {
	DocumentsProcessor docuPro = new DocumentsProcessor();
	// for own tests
	String pathToRead = "/Users/danielliu/eclipse-workspace/594_HW1_v14/src/test";
	String pathToWrite = "/Users/danielliu/eclipse-workspace/594_HW1_v14/src/write.txt";
	
	// for autograder tests
//	String pathToRead = "/autograder/submission/test";
//	String pathToWrite = "/autograder/submission/write.txt";
	
	
	Map<String, List<String>> map = docuPro.processDocuments(pathToRead, 3);
	List<Tuple<String, Integer>> tupleList = docuPro.storeNWordSequences(map, pathToWrite);
	TreeSet<Similarities> treeSet = docuPro.computeSimilarities(pathToWrite, tupleList);

	@Test
	public void testProcessDocuments() {
		ArrayList<String> arrLst = new ArrayList<String>();
		arrLst.add("applebananacandy");
		arrLst.add("bananacandydeer");
		arrLst.add("candydeerapple");
		arrLst.add("deerapplebanana");
		
		assertEquals(map.get("test.txt"), arrLst);
	}

	@Test
	public void testStoreNWordSequences() {
		
		Tuple testT = tupleList.get(0);
		assertEquals(testT.getLeft(), "test.txt");
		
		assertEquals(testT.getRight().toString(), "64");
	}

	@Test
	public void testComputeSimilarities() {
		Similarities testSim = treeSet.first();

		assertEquals(testSim.getFile1(), "test1.txt");
		assertEquals(testSim.getFile2(), "test2.txt");
		assertEquals(testSim.getCount(), 2);
	}

	@Test
	public void testPrintSimilarities() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(output));
		
		docuPro.printSimilarities(treeSet, 1);
		
		assertEquals(output.toString(), "2: test1.txt, test2.txt");
	}



}
