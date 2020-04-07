/*
* 書籍更新（在庫情報の更新）のためのビジネスロジッククラス
*/
package business;
import java.sql.Connection;
import java.sql.SQLException;
import dao.BooksDAO;
import dao.ConnectionManager;
public class BookUpdateController {
/*
* 書籍の在庫情報を更新するためのメソッド
* 引数：
* bookNo: 更新対象となる書籍の書籍番号
* stock:　新しい在庫数
* 戻り値：なし
*/
public void execute(int bookNo, int stock){
	Connection con = ConnectionManager.getConnection();
	BooksDAO dao = new BooksDAO(con);
	try {
		Books book = dao.findByBookNo(bookNo);
		book.setStock(stock);
		if(dao.update(book)) {
			System.out.println("在庫情報を更新しました");
		}
		else {
			System.out.println("在庫情報の更新に失敗しました");
		}
	}
	catch (Exception e) {
		System.out.println("該当する書籍が見つかりません");
	}
	finally {
		if(con != null) {
			try {
				con.close();
			}
			catch(SQLException e) {}
		}
	}
}
}