/*
* OrderDetailsテーブル用DAOクラス
*/
package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import business.OrderDetails;
public class OrderDetailsDAO {
	private Connection con = null;
	public OrderDetailsDAO(Connection con){
//追加
		this.con = con;
	}
/*
* 注文明細情報の登録
* 引数：
* detail: 登録する注文明細情報を保持するインスタンス
* 戻り値：
* 登録に成功したらtrue、失敗したらfalse
*/
public boolean addOrderDetails(OrderDetails detail) throws SQLException
{
//追加
	if(detail == null) {
		return false;
	}
	PreparedStatement pst = con.prepareStatement("INSERT INTO order_details VALUES(?,?,?);");
	pst.setInt(1, detail.getOrderNo());
	pst.setInt(2, detail.getBookNo());
	pst.setInt(3, detail.getOrderQty());
	int result = pst.executeUpdate();
	pst.close();
	if(result != 1) {
		return false;
	}
	return true;
}
}