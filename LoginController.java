/*
* ログイン処理を行うビジネスロジッククラス
*/
package business;
import java.sql.Connection;
import java.sql.SQLException;
import dao.ConnectionManager;
import dao.CustomersDAO;
public class LoginController {
/*
* ログイン処理を行うメソッド
* 引数：
* loginId: ログイン名
* password:　パスワード
* 戻り値：
* ログインした顧客情報を保持するインスタンス
* ログイン失敗時はnullを返す
*/
public Customers execute(String loginId, String password) {
	Customers cust = null;
//追加
	Connection con = null;
	try {
		con = ConnectionManager.getConnection();
		CustomersDAO cusDAO = new CustomersDAO(con);
		Customers customer = cusDAO.findByLoginId(loginId);
		if(customer == null) {
			System.out.println("ログインIDが間違っています");
			return null;
		}
		String truePassword = customer.getPassword();
		if(password.equals(truePassword)) {
			System.out.println("ログイン完了");
			cust = customer;
		} else {
			System.out.println("パスワードが間違っています");
		}
	} catch (SQLException e) {
	} finally {
		try {
			if(cust != null) {
				con.close();
			}
		} catch(SQLException e) {
		}
	}
	return cust;
}
}