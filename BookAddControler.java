/*
* 書籍登録のためのビジネスロジッククラス
*/
package business;
import java.sql.Connection;
import java.sql.SQLException;
import dao.BooksDAO;
import dao.CategoriesDAO;
import dao.CompaniesDAO;
import dao.ConnectionManager;
//import business.*;
public class BookAddController {
/*
* 書籍登録を行うメソッド
* 引数：
* bookName: 書籍名 not null
* writer: 著者名
* price: 売価
* cost: 原価
* stock: 在庫数
* cateNo: 分類番号
* compNo: 出版社番号
* 戻り値：なし
*/
public void execute(String bookName, String writer, int price,
	int cost, int stock, int cateNo, int compNo) {
	Connection con = null;
	try {
		con = ConnectionManager.getConnection();
		Companies comp = null;
		Categories cate = null;
		BooksDAO booksdao = new BooksDAO(con);
		CompaniesDAO companiesdao = new
		CompaniesDAO(con); //companiesオブジェクトの生成
		comp = companiesdao.findByCompNo(compNo);
		CategoriesDAO categoriesdao = new
		CategoriesDAO(con); //categoriesオブジェクトの生成
		cate = categoriesdao.findByCateNo(cateNo);

		if(cate == null){
			System.out.println("該当する分類番号がありません");
			System.out.println("書籍登録に失敗しました");
		}
		else if(comp == null) {
			System.out.println("該当する出版社番号がありません");
			System.out.println("書籍登録に失敗しました");
		}
		else {
			int bookNo = booksdao.getNewBookNo();
			Books book = new Books(bookNo,
				bookName, writer,
				price,
				cost, stock, cate,
				comp);
			boolean b = booksdao.addBook(book);
			if(b) {
				System.out.println("書籍登録を完了しました");
			}
			else {
				System.out.println("書籍登録に失敗しました");
			}
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}finally {
		try {
			if(con != null) {
				con.close();
			}
		} catch (SQLException e) { }
	}
}
}
