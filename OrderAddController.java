/*
* 書籍注文のためのビジネスロジッククラス
*/
package business;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Hashtable;
import dao.BooksDAO;
import dao.ConnectionManager;
import dao.OrderDetailsDAO;
import dao.OrdersDAO;
public class OrderAddController {
/*
* 書籍注文を実行するメソッド
* 引数：
* orderTable: 注文情報（書籍番号、注文冊数）を保持する
Hashtable
* cust:顧客情報を保持するCustomersインスタンス
* 戻り値：なし
*/
public void execute(Hashtable<String, String> orderTable, Customers
	cust)
{
	Connection con = null;
	try {
//データベースとの接続を確立
		con = ConnectionManager.getConnection();
//自動コミットの停止
		con.setAutoCommit(false);
//DAOの作成
		BooksDAO booksDAO = new BooksDAO(con);
		OrdersDAO ordersDAO = new OrdersDAO(con);
		OrderDetailsDAO orderDetailsDAO = new
		OrderDetailsDAO(con);
//新しい注文番号の取得
		int newOrderNo = ordersDAO.getNewOrderNo();
//Orderオブジェクトの設定と登録
		Orders order = new Orders(newOrderNo, new
			Date(System.currentTimeMillis()),
			cust);
		ordersDAO.addOrders(order);
//注文数だけループ
		for(String key : orderTable.keySet()) {
//注文番号をint型に変換
			int bookNo = Integer.parseInt(key);
//注文番号から書籍を検索
			Books book = booksDAO.findByBookNo(bookNo);
//該当書籍が存在しない場合の処理
			if(book == null) {
				System.out.println("該当する書籍が見つかりません(" + bookNo + ")");
				throw new SQLException();
			}
			int stock = book.getStock();
			int orderQty =
			Integer.parseInt(orderTable.get(key));
			if(orderQty < 1) {
				System.out.println("注文数には1以上の数を指定してください(書籍番号:" + bookNo + " 注文数:" + orderQty + ")");
				throw new SQLException();
			}
			if(stock < orderQty) {
				System.out.println("在庫が足りません(書籍番号:" + bookNo + " 在庫数:" + stock + " 注文数:" + orderQty + ")");
				throw new SQLException() ;
			} else {
				int stockAfterOrder = stock - orderQty;
				book.setStock(stockAfterOrder);
				booksDAO.update(book);
				OrderDetails orderDetail = new
				OrderDetails(order, book,
					orderQty);
				orderDetailsDAO.addOrderDetails(orderDetail);
			}
		}
		con.commit();
		System.out.println("注文登録を完了しました");
		System.out.println("在庫数を更新しました");
		con.setAutoCommit(true);
	} catch (SQLException e) {
		System.out.println("全ての注文がキャンセルされました");
//ロールバックの発行
		try {
			if(con != null) {
				con.rollback();
			}
//System.out.println("ロールバックしました。");
		} catch(SQLException e2) {
		}
	} finally {
		try {
			if(con != null) {
				con.close();
			}
		} catch (SQLException e) {
		}
	}
}
}