/*
* Booksテーブル用DAOクラス
*/
package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import business.BookComparator;
import business.Books;
import business.Categories;
import business.Companies;
public class BooksDAO {
	private Connection con = null;
	public BooksDAO(Connection con) {
		this.con = con;
	}
/*
* 全件検索
* 引数：なし
* 戻り値：
* 検索されたすべての書籍インスタンスを含むArrayList
* １件も見つからなかった場合は長さ0のArrayListインスタンス
を返す（nullではない）
*/
public ArrayList<Books> findAll() throws SQLException {
// 戻り値を宣言
	ArrayList<Books> bookArray = new ArrayList<Books>();
// 必要となるDAOクラスのインスタンスを生成
	CategoriesDAO cateDAO = new CategoriesDAO(con);
	CompaniesDAO compDAO = new CompaniesDAO(con);
// SQL文を発行するStatementオブジェクトの獲得
	Statement stmt = con.createStatement();
// SQL文の組み立て
	String sql = "SELECT * FROM BOOKS";
// SQL文の発行
	ResultSet res = stmt.executeQuery(sql);
// ResultSetの読み込み
	while (res.next()) {
// 分類インスタンスの生成
		Categories cat =
		cateDAO.findByCateNo(res.getInt("CATE_NO"));
		Companies comp =
		compDAO.findByCompNo(res.getInt("COMP_NO"));
		Books book = new Books(res.getInt("BOOK_NO"),
			res.getString("BOOK_NAME"),
			res.getString("WRITER"),
			res.getInt("PRICE"),
			res.getInt("COST"),
			res.getInt("STOCK"),
			cat, comp);
		bookArray.add(book);
	}
// Statementオブジェクトのクローズ
	stmt.close();
	return bookArray;
}
/*
* 主キー（BOOK_NO)による書籍検索
* 引数：
* bookNo:検索対象となる書籍番号(BOOK_NO)
* 戻り値：
* bookNoを書籍番号として持つ書籍情報のインスタンス
* 検索に失敗した場合はnullを返す
*/
public Books findByBookNo(int bookNo) throws SQLException {
// 戻り値を格納するBooks型の変数bookを宣言し、nullで初期化する。
	Books book = null;
	PreparedStatement pst= null;
// conを引数としてBooksDAOクラスのコンストラクタを呼び出して、初期化する。
	try {
// メンバ変数のconを引数としてCategoriesDAOクラスのコンストラクタ
		CategoriesDAO cateDao = new CategoriesDAO(con);
// メンバ変数のconを引数としてCompaniesDAOクラスのコンストラクタ
		CompaniesDAO compDao = new CompaniesDAO(con);
// Connection クラスのprepareStatement()メソッドを呼び出し、PreparedStatement オブジェクトを獲得する
// PreparedStatementの取得
		pst = con.prepareStatement("SELECT * FROM books WHERE book_no =	?");
			// PreparedStatementクラスのsetInt()メソッドを呼び出し、入力パラメータに引数bookNoを設定する。
// PreparedStatement クラスのexecuteQuery()メソッドを呼び出し、SQL 文を発行する。
		pst.setInt(1,bookNo);
		ResultSet rs = pst.executeQuery();
// ResultSet クラスのnext()メソッドを呼び出す。
		if (rs.next()) {
			int no = rs.getInt("book_no");
			String bookName = rs.getString("book_name");
			String writer = rs.getString("writer");
			int price = rs.getInt("price");
			int cost = rs.getInt("cost");
			int stock = rs.getInt("stock");
			int cateNo = rs.getInt("cate_no");
			int compNo = rs.getInt("comp_no");
// CategoriesとCompaniesのオブジェクトを取得
			Categories cate = cateDao.findByCateNo(cateNo);
			Companies comp = compDao.findByCompNo(compNo);
// 戻り値をbookに代入
			book = new Books(no, bookName, writer, price,
				cost, stock, cate,
				comp);
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
//PreparedStatement クラスのclose()メソッドを呼び出し、PreparedStatement オブジェクトをクローズする。
	finally{
		if(pst!=null)
			try{
				pst.close();
			}
			catch(SQLException e) {
			}
		}
// 戻り値bookを返す
		return book;
	}
	public ArrayList<Books> findByBookName(String bookName) throws
	SQLException
	{
// 戻り値を格納するArraiList型の変数bookArrayを宣言し、インスタンス化する
		ArrayList<Books> bookArray = new ArrayList<Books>();
		PreparedStatement pst= null;
// conを引数としてBooksDAOクラスのコンストラクタを呼び出して、初期化する。
		try {
// メンバ変数のconを引数としてCategoriesDAOクラスのコンストラクタ
			CategoriesDAO cateDao = new CategoriesDAO(con);
// メンバ変数のconを引数としてCompaniesDAOクラスのコンストラクタ
			CompaniesDAO compDao = new CompaniesDAO(con);
// Connection クラスのprepareStatement()メソッドを呼び出し、PreparedStatement オブジェクトを獲得する。
// PreparedStatementの取得
			pst = con.prepareStatement("SELECT * FROM Books WHERE book_name collate utf8_unicode_ci LIKE ?");	pst = con.prepareStatement("SELECT * FROM Books WHERE book_name LIKE ?");
// PreparedStatementクラスのsetInt()メソッドを呼び出し、入力パラメータに引数bookNoを設定する。
// PreparedStatementクラスのexecuteQuery()メソッドを呼び出し、SQL 文を発行する。
//半角文字
			String halfBookName = Normalizer.normalize(bookName,
				Normalizer.Form.NFKC);
			StringBuilder sb = new StringBuilder(bookName);
			for(int i = 0; i < sb.length(); i++) {
				int c = (int)sb.charAt(i);
				if((c >= 0x21 && c <= 0x5A) || (c >= 0x61 && c
					<= 0x7A))
				{
					sb.setCharAt(i, (char)(c + 0xFEE0));
				}
			}
//全角文字
			String fullBookName = sb.toString();
			pst.setString(1, "%" + halfBookName + "%");
			ResultSet rs = pst.executeQuery();
// ResultSet クラスのnext()メソッドを呼び出す。
			while (rs.next()) {
				int no = rs.getInt("book_no");
				String name = rs.getString("book_name");
				String writer = rs.getString("writer");
				int price = rs.getInt("price");
				int cost = rs.getInt("cost");
				int stock = rs.getInt("stock");
				int cateNo = rs.getInt("cate_no");
				int compNo = rs.getInt("comp_no");
				Categories cate = cateDao.findByCateNo(cateNo);
				Companies comp = compDao.findByCompNo(compNo);
				bookArray.add(new Books(no, name, writer,
					price, cost, stock, cate,
					comp));
			}
			pst.setString(1, "%" + fullBookName + "%");
			rs = pst.executeQuery();
// ResultSet クラスのnext()メソッドを呼び出す。
			while (rs.next()) {
				int no = rs.getInt("book_no");
				String name = rs.getString("book_name");
				String writer = rs.getString("writer");
				int price = rs.getInt("price");
				int cost = rs.getInt("cost");
				int stock = rs.getInt("stock");
				int cateNo = rs.getInt("cate_no");
				int compNo = rs.getInt("comp_no");
				Categories cate = cateDao.findByCateNo(cateNo);
				Companies comp = compDao.findByCompNo(compNo);
				boolean isRegistered = false;
				for(Books book: bookArray) {
					if(book.getBookNo() == no) {
						isRegistered = true;
					}
				}
				if(!isRegistered) {
					bookArray.add(new Books(no, name,
						writer, price, cost, stock, cate,
						comp));
				}
			}
			Collections.sort(bookArray, new BookComparator());
		}catch(SQLException e) {
			e.printStackTrace();
		}
//PreparedStatement クラスのclose()メソッドを呼び出し、PreparedStatement オブジェクトをクローズする。
		finally{
			if(pst!=null)
				try{
					pst.close();
				}
				catch(SQLException e) {
				}
			}
			return bookArray;
		}
/*
* 書籍情報を登録する
* 引数：
* book: 新規に登録する書籍オブジェクト
* 戻り値：
* 登録に成功したらtrue、失敗したらfalse
*/
public boolean addBook(Books book)
throws SQLException {
//追加
	int num = 0;
	if(book==null) {
		return false;
	}
	if(book.getPrice() < 0 || book.getCost() < 0 || book.getStock()
		< 0)
	{
		return false;
	}
	if(book.getBookName() == null || book.getWriter() == null ||
		book.getCategory() == null
		||
		book.getCompany() == null) {
		throw new SQLException();
}
else {
	PreparedStatement pst = con.prepareStatement("INSERT INTO books	VALUES(?,?,?,?,?,?,?,?)");
	pst.setInt(1, book.getBookNo()); //書籍番号
	pst.setString(2, book.getBookName()); //書籍名
	pst.setString(3, book.getWriter()); //著者
	pst.setInt(4, book.getPrice()); //売価
	pst.setInt(5, book.getCost()); //原価
	pst.setInt(6, book.getStock()); //在庫
	pst.setInt(7, book.getCateNo()); //分類番号
	pst.setInt(8, book.getCompNo()); //出版社番号
	num = pst.executeUpdate();
	pst.close();
}
if(num == 1) {
	return true;
} else {
	return false;
}
}
/*
* 書籍の情報を更新する
* 引数：
* book:新しい書籍情報を保持したインスタンス
* 戻り値：
* 更新成功時はtrue、失敗した場合はfalse
*
*/
public boolean update(Books book) throws SQLException {
//追加
	PreparedStatement pst = null;
	int num = 0;
	if(book == null) {
		return false;
	}
	if(book.getStock()<0) {
		return false;
	}
	try {
		pst = con.prepareStatement("UPDATE books SET STOCK=? WHERE BOOK_NO=?");
		pst.setInt(2, book.getBookNo());
		pst.setInt(1, book.getStock());
		num = pst.executeUpdate();
	}
	catch(Exception e){}
	finally{
		if(pst != null) {
			try {
				pst.close();
			}
			catch(SQLException e) {}
		}
	}
	if(num == 1) {
		return true;
	}
	else {
		return false;
	}
}
/*
* 新しい書籍番号を取得する
* 引数：なし
* 戻り値：
* 新しい書籍番号
* １件も書籍が登録されていない場合は1を返す。それ以外の場
合は、最も大きな書籍番号+1の値を返す
*/
public int getNewBookNo() throws SQLException {
int no = 0; //仕様書参照
PreparedStatement pst = null;
pst = con.prepareStatement("SELECT MAX(book_No) FROM books");
pst.executeQuery();
ResultSet rs = pst.executeQuery();
while (rs.next()) { //rs.nextで回し、最大値が出たところでそれ
	をif文に使用
	no = rs.getInt("MAX(book_no)");
}
//追加
no++;
return no;
}
}