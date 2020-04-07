package business;
import java.util.Comparator;

public class BookComparator implements Comparator<Books>{
	public int compare(Books b1, Books b2) {
		return b1.getBookNo() < b2.getBookNo() ? -1 : 1;
	}
}