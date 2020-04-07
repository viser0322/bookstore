/*
* Customresテーブル用DAOクラス
*/
package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.regex.Pattern;
import business.Customers;
public class CustomersDAO {
	private Connection con = null;
	public CustomersDAO(Connection con){
		this.con = con;
	}
/*
* ログイン名による検索（UNIQUE制約がかかっているので、結果は一意)
* 戻り値：指定されたログイン名を持つ顧客情報を保持したイン
スタンス
* 検索失敗時はnullを返す
*/
public Customers findByLoginId(String loginId) throws SQLException{
//戻り値を宣言
	Customers cust = null;
	PreparedStatement pst = con.prepareStatement("SELECT * FROM customers WHERE login_id = ?;");
	pst.setString(1, loginId);
	ResultSet rs = pst.executeQuery();
	if(rs.next()) {
		int cust_no = rs.getInt("CUST_NO");
		String id = rs.getString("LOGIN_ID");
		String pass = rs.getString("PASSWORD");
		String name = rs.getString("CUST_NAME");
		String addr = rs.getString("CUST_ADDR");
		Date date = rs.getDate("ENTRY_DATE");
		String email = rs.getString("EMAIL");
		cust = new Customers(cust_no, id, pass, name, addr,
			date,
			email);
	}
	return cust;
}
public int changePassword(boolean b,String password,String id) throws
SQLException{
	PreparedStatement pst =null;
	int num = 0;
	if(isHalf(password)) {
		if(b) {
			try {
				pst = con.prepareStatement("UPDATE customers SET PASSWORD=? WHERE LOGIN_ID=?");
				pst.setString(1, password);
				pst.setString(2, id);
				num = pst.executeUpdate();
			}
			catch(SQLException e) {}
			finally {
				try {
					if(pst!=null) {
//PreparedStatementオブ
						ジェクトのクローズ
						pst.close();
					}
				}
				catch(SQLException e) {}
			}
		}
		else {
			System.out.println("パスワードが一致しません");
		}
	}
	else {
		System.out.println("半角英数字で入力してください");
	}
	return num;
}
public static boolean isHalf(String str) {
return Pattern.matches("^[0-9a-zA-Z]+$", str); //^前方
一致（この文字で始まる） $後方一致（この文字で終わる）
}
}