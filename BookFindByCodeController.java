/*
* 書籍検索を行うビジネスロジッククラス
*/
package business;
import java.sql.Connection;
import java.sql.SQLException;
import dao.BooksDAO;
import dao.ConnectionManager;
public class BookFindByCodeController {
/*
* 書籍検索を行うためのメソッド
* 引数：
* bookNo:この値をキーとして書籍検索を行う（書籍番号）
* 戻り値：なし
*/
public void execute(int bookNo) {
// ConnectionManagerクラスのgetConnection()メソッドを呼び出し、変数conに代入する。
	Connection con = ConnectionManager.getConnection();
// conを引数としてBooksDAOクラスのコンストラクタを呼び出して、初期化する。
	BooksDAO dao = new BooksDAO(con);
	try {
// bookNoを引数としてBooksDAOクラスのfindByBookNo(bookNo:int)メソッドを呼び出し、結果をBooks型の変数bookに格納する。
		Books book = dao.findByBookNo(bookNo);
		if(book != null) {
// BooksクラスのgetBookNo()、getBookName()、getCompName()、getPrice()、getStock()を呼び出し、それぞれ表示する。
			System.out.println("書籍番号：" +
				book.getBookNo());
			System.out.println("書籍名：" +
				book.getBookName());
			System.out.println("出版社：" +
				book.getCompName());
			System.out.println("価格：" + book.getPrice());
			System.out.println("在庫数：" +
				book.getStock());
		} else {
			System.out.println("該当する書籍がありません");
		}
	}
	catch(SQLException e) {}
// Connectionクラスのclose()メソッドを呼び出し、Connectionオブジェクトをクローズする。
	finally{
		if(con!=null)
			try{
				con.close();
			}
			catch(SQLException e) {
			}
		}
	}
}