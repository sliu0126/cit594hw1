import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DocumentIterator implements Iterator<String> {

	// set up several private variables and object
	private int n;
	private Reader r;
	private int c = -1;

	// constructor setup
	public DocumentIterator(Reader r, int n) {
		this.n = n;
		this.r = r;
		skipNonLetters();
	}

	private void skipNonLetters() {
		try {
			this.c = this.r.read();

			while (!Character.isLetter(this.c) && this.c != -1) {
				this.c = this.r.read();
			}
		} catch (IOException e) {
			this.c = -1;
		}
	}

	@Override
	public boolean hasNext() {

		return (c != -1);
	}

	/**
	 * Get the content
	 */
	@Override
    public String next() {
       
		// set up variables
		int num = n;
      
        String ans = "";
        String tmp = "";

        
        // check if there is more content or not
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        
        try {
            //get the n-word sequence 
            while (num > 0 && hasNext()) {
                tmp = ans;
                
                while (Character.isLetter(this.c)) {
                    ans = ans + (char)this.c;
                    this.c = this.r.read();
                }
                
                if (!tmp.equals(ans)) {
                    num = num - 1;
                }
                
                if (num == n - 1) {
                    this.r.mark(1000);
                }
                
                skipNonLetters();
            }
            //no more words
            if (this.c == -1) {
                this.r.mark(1000);
            }
            
            this.r.reset();
            this.c = this.r.read();
            
            
        } catch (IOException e) {
        	
            throw new NoSuchElementException();
        }

        return ans;
    }

}
