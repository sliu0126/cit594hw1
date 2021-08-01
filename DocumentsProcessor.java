import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;

public class DocumentsProcessor implements IDocumentsProcessor {

	/**
	 * Process Documents to words sequence
	 * 
	 * @param directoryPath: file path
	 * @param n:             the number of word sequence
	 * @return map
	 */
	@Override
	public Map<String, List<String>> processDocuments(String directoryPath, int n) {
		// TODO Auto-generated method stub
		Map<String, List<String>> mp = new HashMap<String, List<String>>();

		// file directory
		File dir = new File(directoryPath);
		// create a list for storing files
		File[] fileList = dir.listFiles();

		// put file content into a hashmap for each file
		if (fileList != null) {
			for (File eachfile : fileList) {
				try {

					// create file reader and bufferedreader
					Reader r = new FileReader(eachfile);
					BufferedReader br = new BufferedReader(r);

					// use documentiterator to read content
					DocumentIterator d = new DocumentIterator(br, n);
					List<String> list = new ArrayList<String>();

					// keep adding content to the list while it still has the content
					while (d.hasNext()) {
						list.add(d.next());
					}

					// put the file with name into the map
					mp.put(eachfile.getName(), list);

				} catch (IOException e) {
					continue;
				}
			}

			// throw no such element exception
		} else {
			throw new NoSuchElementException();
		}

		// return the map at the end
		return mp;
	}

	/**
	 * Keep word sequences into list
	 * 
	 * @param docs:          documents to process
	 * @param nwordFilePath: file path
	 * @return list
	 */
	@Override
	public List<Tuple<String, Integer>> storeNWordSequences(Map<String, List<String>> docs, String nwordFilePath) {
		// TODO Auto-generated method stub

		List<Tuple<String, Integer>> tList = new ArrayList<Tuple<String, Integer>>();
		// initialize n to 0
		int n = 0;

		try {
			File file = new File(nwordFilePath);
			// check if the file exists
			boolean fileExistence = file.exists();

			// delete then create new file when the file exists
			if (fileExistence == true) {
				// delete
				file.delete();
				// create new file
				file.createNewFile();

				// when file does not exist
			} else if (fileExistence == false) {
				// create new file
				file.createNewFile();

				// otherwise throw exception
			} else {
				throw new IOException();
			}

			RandomAccessFile rr = new RandomAccessFile(file, "rw");
			// iterate inside the map
			for (String doc : docs.keySet()) {

				// construct the tuple
				Tuple<String, Integer> tup = new Tuple<String, Integer>(new String(), 0);

				// use for loop through docpath
				for (String docPath : docs.get(doc)) {
					// set file pointer offset to the length
					rr.seek(rr.length());
					rr.writeBytes(docPath + " ");

					// update the integer n
					n = n + docPath.length();
					n = n + 1;
				}

				// setup the left and right for this tuple
				tup.setLeft(doc);
				tup.setRight(n);
				// then store this tuple into the list
				tList.add(tup);

				// reset n to 0
				n = 0;
			}

			// close the random file access
			rr.close();

			// catch statement
		} catch (IOException e) {
			e.printStackTrace();
		}

		// return the list at the end
		return tList;

	}

	@Override
	public TreeSet<Similarities> computeSimilarities(String nwordFilePath, List<Tuple<String, Integer>> fileindex) {
		// TODO Auto-generated method stub

		// initialize multiple counts to zeros
		int count = 0;
		int leftCount = 0;
		int rightCount = 0;

		// initialize the index and search counts
		int index = fileindex.size();
		int leftSearch = 0;
		int rightSearch = 0;

		// create a new treeset
		TreeSet<Similarities> treeS = new TreeSet<Similarities>();

		// try catch statement
		try {

			// get random access from the file
			RandomAccessFile ranAF = new RandomAccessFile(nwordFilePath, "r");

			// loop through the index
			for (int i = 0; i < index - 1; i++) {
				// use seek method in random file acess to search left
				ranAF.seek(leftSearch);

				leftCount = fileindex.get(i).getRight();

				// create byte the object
				byte[] leftByte = new byte[leftCount];

				ranAF.read(leftByte);

				String leftByteString = new String(leftByte);

				HashSet<String> leftHashSet = new HashSet<String>();

				// add each left string byte to the lefthashset
				for (String leftSB : leftByteString.split(" ")) {
					leftHashSet.add(leftSB);
				}

				// loop through the index
				for (int j = i + 1; j < index; j++) {

					// get right int
					int rightInt = fileindex.get(j - 1).getRight();

					// update right search number by adding right int part
					rightSearch = rightSearch + rightInt;

					// do right search
					ranAF.seek(rightSearch);

					// get right count from file index
					rightCount = fileindex.get(j).getRight();
					// then get right byte with the right count
					byte[] rightByte = new byte[rightCount];

					// read right byte
					ranAF.read(rightByte);
					// and get right byte string
					String rightByteString = new String(rightByte);

					// construct a new hashset for right part
					HashSet<String> rightHashSet = new HashSet<String>();

					// get left string
					String leftString = fileindex.get(j).getLeft();

					// add each right string byte to right hashmap
					for (String rightSB : rightByteString.split(" ")) {
						rightHashSet.add(rightSB);
					}

					// check and count the content inside the leftHashSet
					for (String content : leftHashSet) {
						if (rightHashSet.contains(content)) {
							// update the count
							count = count + 1;
						}
					}

					// create similarity for leftS and leftString
					String tmp = fileindex.get(i).getLeft();
					Similarities simLeft = new Similarities(tmp, leftString);
					simLeft.setCount(count);

					treeS.add(simLeft);
					count = 0;

				}

				// reset the rightCount to zero
				rightCount = 0;
				// add up to get leftSearch count
				leftSearch = leftSearch + leftCount;
				// set right and left search counts equal
				rightSearch = leftSearch;
			}
			// do not forget to close the file access
			ranAF.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// return the treeSet filled with results
		return treeS;

	}

	/**
	 * Print out the similarities
	 * 
	 * @param sims:      tree set that keep similarities
	 * @param threshold: a limit to filter
	 */
	@Override
	public void printSimilarities(TreeSet<Similarities> sims, int threshold) {
		// TODO Auto-generated method stub

		for (Similarities sim : sims) {

			// case when similarity count is larger then the threshold

			boolean countFlag = (sim.getCount() > threshold);
            if (countFlag == true) {
                String tmpS = String.valueOf(sim.getCount()) + ": " + sim.getFile1()+ ", " +sim.getFile2();
                System.out.print(tmpS);
            }
		}
	}
	
	

}
