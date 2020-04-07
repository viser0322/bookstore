package business;
import java.sql.Connection;
import java.sql.SQLException;
import dao.ConnectionManager;
import dao.CustomersDAO;
public class ChangeController {
	public void execute(String password1,String password2, Customers
		customer){
		Connection con = ConnectionManager.getConnection();
		CustomersDAO dao = new CustomersDAO(con);
		int num = 0;
		try {
			num = dao.changePassword(password1.equals(password2),
				password1,customer.getLoginId());
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(num == 1) {
				System.out.println("パスワードを更新しました");
			}
			else {
				System.out.println("パスワードの更新に失敗しました");
			}
			if(con != null) {
				try {
					con.close();
				}
				catch(SQLException e) {}
			}
		}
	}
}