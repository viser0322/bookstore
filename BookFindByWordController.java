/*
* 書籍名検索を行うビジネスロジッククラス
*/
package business;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import dao.BooksDAO;
import dao.ConnectionManager;
public class BookFindByWordController {
/*
* 書籍名検索を行うためのメソッド
* 引数：
* bookName:この値をキーとして書籍名検索を行う（書籍名）
* 戻り値：なし
*/
public void execute(String bookName) {
// ConnectionManagerクラスのgetConnection()メソッドを呼び出し、変数conに代入する。
	Connection con = ConnectionManager.getConnection();
// conを引数としてBooksDAOクラスのコンストラクタを呼び出して、初期化する。
	BooksDAO dao = new BooksDAO(con);
	try {
// bookNameを引数としてBooksDAOクラスのfindByBookName(bookName:String)メソッドを呼び出し、結果をArrayList<Books>型のbooksに格納する。
		ArrayList<Books> books = dao.findByBookName(bookName);
// booksにデータがある間BooksクラスのgetBookNo()、getBookName()、getCompName()、getPrice()、getStock()を呼び出し、それぞれ表示することを繰り返す。
		if(books.size() != 0) {
			for(Books book:books) {
				System.out.println("書籍番号：" +
					book.getBookNo());
				System.out.println("書籍名：" +
					book.getBookName());
				System.out.println("出版社：" +
					book.getCompName());
				System.out.println("価格：" +
					book.getPrice());
				System.out.println("在庫数：" +
					book.getStock());
				System.out.println();
			}
		} else {
			System.out.println("該当する書籍がありません");
		}
	}catch(SQLException e) {}
// Connectionクラスのclose()メソッドを呼び出し、Connectionオブジェクトをクローズする。
	finally{
		if(con!=null)
			try{
				con.close();
			}
			catch(SQLException e) {}
		}
	}
}