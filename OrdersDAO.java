/*
* Ordersテーブル用DAOクラス
*/
package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import business.Orders;
public class OrdersDAO{
	private Connection con = null;
	public OrdersDAO(Connection con){
//追加
		this.con = con;
	}
/*
* 注文登録メソッド
* 引数：
* order: 登録する注文情報を保持するインスタンス
* 戻り値：
* 登録に成功したらtrue、失敗したらfalseを返す
*/
public boolean addOrders(Orders order) throws SQLException {
//追加
	if(order == null) {
		return false;
	}
	PreparedStatement pst = con.prepareStatement("INSERT INTO orders VALUES(?, ?, ?);");
	pst.setInt(1, order.getOrderNo());
	pst.setDate(2, order.getOrderDate());
	pst.setInt(3, order.getCustNo());
	int result = pst.executeUpdate();
	if(result != 1) {
		return false;
	}
	return true;
}
/*
* 新しい注文番号を取得する
* 引数：なし
* 戻り値：
* 新しい注文番号
* １件も注文が登録されていない場合は1を返す。それ以外の場
合は、最も大きな注文番号+1の値を返す
*/
public int getNewOrderNo() throws SQLException {
	int no = 1;
//追加
	PreparedStatement pst = con.prepareStatement("SELECT MAX(order_no) FROM orders;");
	ResultSet rs = pst.executeQuery();
	while(rs.next()) {
		no = rs.getInt("MAX(order_no)");
		no++;
	}
	return no;
}
}