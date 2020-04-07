/*
* 書籍管理システムの実行クラス
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import business.BookAddController;
import business.BookFindByCodeController;
import business.BookFindByWordController;
import business.BookFindController;
import business.BookUpdateController;
import business.ChangeController;
import business.Customers;
import business.LoginController;
import business.OrderAddController;
public class OrderMain {
	public static void main(String args[]) {
		Customers customer = null;
		while (true) {
			System.out.println("\n*** 操作メニュー ***");
			System.out.println("Login(ログイン）\t....登録済み顧客としてログインします。注文前にはログインが必要です");
			System.out.println("FindAll(書籍一覧表示)\t....登録済み書籍を一覧表示します");
			System.out.println("FindByCode（書籍番号検索)\t....登録済み書籍を書籍番号で検索・表示します");
			System.out.println("FindByWord（書籍名検索)\t....登録済み書籍を書籍名で検索・表示します");
			System.out.println("Update（在庫更新）\t....登録済み書籍の在庫数を更新します");
			System.out.println("Add(書籍登録）\t\t....書籍を新規登録します");
			System.out.println("Order(注文）\t\t....書籍を注文します。事前にログインが必要です");
			System.out.println("Change(パスワード変更）\t\t....パスワードを変更します。事前にログインが必要です");
System.out.println("Logout(ログアウト）\t\t....ログアウト処理を行います");//追加分
System.out.print("コマンドを入力してください(exitで終了）>>");
String command = keyIn();
System.out.println("\n");
if (command.equalsIgnoreCase("Login")) {
	customer = login();
} else if (command.equalsIgnoreCase("FindAll")) {
	BookFindController bc = new
	BookFindController();
	bc.execute();
} else if (command.equalsIgnoreCase("FindByCode")) {
	findByCode();
} else if (command.equalsIgnoreCase("FindByWord")) {
	findByWord();
} else if (command.equalsIgnoreCase("Update")) {
	update();
} else if (command.equalsIgnoreCase("Add")) {
	add();
} else if (command.equalsIgnoreCase("Order")) {
	order(customer);
} else if (command.equalsIgnoreCase("Change")) {
	change(customer);
}else if(command.equalsIgnoreCase("Logout")) {//追加分
	if(customer == null) {
		System.out.println("ログインしていません");
	} else {
		customer = null;
		System.out.println("ログアウトしました");
	}
}
else if ( command.equalsIgnoreCase("exit")) {
	System.out.println("ご利用いただきありがとうございます");
	break;
}
else {
	System.out.println("指定した文字列を入力してください");
}
}
}
/*
* ログイン部分のユーザ入力受付メソッド
* 引数：なし
* 戻り値：
* ログインした顧客情報を保持するインスタンス
* ログイン失敗時はnullを返す
*/
private static Customers login() {
	Customers cust = null;
	System.out.println("*** ログイン ***");
	System.out.println("登録されているログインIDとパスワードを入力して、ログインします");
	System.out.print("ログインID >> ");
	String loginId = keyIn();
	if (loginId == null || loginId.length() < 1) {
		System.out.println("ログインIDが入力されていません");
		return cust;
	}
	System.out.print("パスワード >> ");
	String password = keyIn();
	if (password == null || password.length() < 1) {
		System.out.println("パスワードが入力されていません");
		return cust;
	}
	LoginController lc = new LoginController();
	cust = lc.execute(loginId, password);
	return cust;
}
/*
* 書籍番号を元に書籍情報を検索する機能のための入力受付メソッド
* 引数：なし
* 戻り値：なし
*/
private static void findByCode(){
	System.out.println("*** 書籍番号検索 ***");
	System.out.println("書籍番号を入力することで、登録済み書籍から該当する書籍情報を表示します");
	try {
		int bookNo = keyInVal("書籍番号", true);
		BookFindByCodeController bc = new
		BookFindByCodeController();
		bc.execute(bookNo);
	}catch(Exception e){
	}
}
/*
* 書籍名を元に書籍情報を検索する機能のための入力受付メソッド
* 引数：なし
* 戻り値：なし
*/
private static void findByWord(){
	System.out.println("*** 書籍名検索 ***");
	System.out.println("書籍名を入力することで、登録済み書籍から該当する書籍情報を表示します");
	try {
		System.out.print("書籍名 >>");
		String bookName = keyIn();
		if (bookName == null || bookName.length() < 1) {
			System.out.println("書籍名が入力されていません");
			return;
		}
		BookFindByWordController bc = new
		BookFindByWordController();
		bc.execute(bookName);
	} catch (Exception e) {
	}
}
/*
* 指定書籍の在庫情報を更新する機能のための入力受付メソッド
* 引数：なし
* 戻り値：なし
*/
private static void update() {
	int bookNo = 0;
	int stock = 0;
	System.out.println("*** 在庫更新 ***");
	System.out.println("登録済みの書籍番号を入力し、続いて設定したい在庫数を入力してください。");
	System.out.println("該当書籍の在庫数を入力された在庫数に更新します");
	System.out.println("");
	try {
		bookNo = keyInVal("書籍番号", true);
		stock = keyInVal("在庫", true);
		BookUpdateController bu = new BookUpdateController();
		bu.execute(bookNo, stock);
	} catch (Exception e) {
	}
}
/*
* 書籍登録機能のための入力受付メソッド
* 引数：なし
* 戻り値：なし
*/
private static void add() {
	System.out.println("*** 書籍登録 ***");
	System.out.println("書籍名・著者名・売価・原価・在庫・分類番号・出版社番号を入力してください。");
	System.out.println("新しい書籍情報を登録します。");
	System.out.println("書籍名・分類番号・出版社番号は必須入力項目です。");
	System.out.println("");
	try {
		System.out.print("書籍名 >>");
		String bookName = keyIn();
		if (bookName == null || bookName.length() < 1) {
			System.out.println("書籍名が入力されていません");
			return;
		}
		System.out.print("著者名 >>");
		String writer = keyIn();
		int price = keyInVal("売価", false);
		int cost = keyInVal("原価", false);
		int stock = keyInVal("在庫", false);
		int cateNo = keyInVal("分類番号", true);
		int compNo = keyInVal("出版社番号", true);
		BookAddController bc = new BookAddController();
		bc.execute(bookName, writer, price, cost, stock, cateNo,
			compNo);
	} catch (Exception e) {
	}
}
/*
* 書籍注文のための入力受付メソッド
* 引数：
* customer: ログインしているユーザ
* 戻り値：なし
*/
private static void order(Customers customer){
// ログインチェック
	if ( customer == null ) {
		System.out.println("最初にログインしてください");
		return;
	}
	System.out.println("*** 注文 ***");
	System.out.println("書籍番号と注文冊数を指定することで、書籍の注文を行います。");
	System.out.println("複数の書籍を同時に注文することができます。");
	System.out.println("在庫が不足している場合は、注文できません。注文数を修正して、最初からやり直してください。");
// 注文情報（書籍番号、注文冊数）を保持する
	Hashtable<String, String> order = new Hashtable<String,
	String>();
	try {
// 注文情報の入力を受け付ける(nが入力されるまで）
		while (true) {
			int bookNo = keyInVal("書籍番号", true);
			int qty = keyInVal("注文冊数", true);
// すでに注文されている書籍かどうか確認
			if (!order.containsKey(String.valueOf(bookNo)))
			{
// 新しい注文の場合は、そのまま登録
				order.put(String.valueOf(bookNo),
					String.valueOf(qty));
			}
			else {
// 同じ書籍番号の書籍が注文済みの場合は、注文冊数を増やす
				int qtyVal =
				Integer.parseInt(order.get(String.valueOf(bookNo)));
				qtyVal += qty;
				order.put(String.valueOf(bookNo),
					String.valueOf(qtyVal));
			}
			System.out.print("続けますか(y/n)？ >>");
			String answer = keyIn();
			while(!answer.equalsIgnoreCase("n") &&
				!answer.equalsIgnoreCase("ｎ")
				&&
				!answer.equalsIgnoreCase("y")
				&& !answer.equalsIgnoreCase("ｙ"))
			{
				System.out.print("yかnを入力してください(y/n)？>>");
				answer = keyIn();
			}
			if (answer.equalsIgnoreCase("n") ||
				answer.equalsIgnoreCase("ｎ")){
				break;
		}
/*
if (answer.equalsIgnoreCase("n"))
break;
*/
}
OrderAddController oc = new OrderAddController();
oc.execute(order, customer);
}catch(Exception e) {
}
}
/*
* パスワード変更のための入力受付メソッド
* 引数：
* customer: ログインしているユーザ
* 戻り値：なし
*/
private static void change(Customers customer){
// ログインチェック
	if ( customer == null ) {
		System.out.println("最初にログインしてください");
		return;
	}
	System.out.println("*** 変更 ***");
	System.out.println("パスワードを2回入力することで、パスワードの変更を行います");
	try {
		System.out.print("新しいパスワード >>");
		String password1 = keyIn();
		System.out.print("パスワードの確認 >>");
		String password2 = keyIn();
		ChangeController cc = new ChangeController();
		cc.execute(password1, password2, customer);
	}catch(Exception e) {}
}
/*
* キーボードから入力された値をStringで返す
* 引数：なし
* 戻り値：入力された文字列
*/
private static String keyIn() {
	String line = null;
	try {
		BufferedReader key = new BufferedReader(new
			InputStreamReader(
				System.in));
		line = key.readLine();
	} catch (IOException e) {
	}
	return line;
}
/*
* キーボードから入力された値をintに変換して返す
* 引数：
* item: 入力項目
* chk: truの場合は未入力時に例外発生、falseの場合は0を返す
* 例外：
* NumberFormatException:入力された値が数値以外の場合
* NoInputException: chkにtrueが指定されているとき、値が未
入力の場合
*/
private static int keyInVal(String item, boolean chk)
throws NoInputException {
	int val = 0;
	try {
		System.out.print(item + " >> ");
		String line = keyIn();
		if (line == null || line.length() < 1) {
			if (chk) {
				System.out.println(item + "が入力されていません");
				throw new NoInputException();
			} else
			line = "0";
		}
		val = Integer.parseInt(line);
	} catch (NumberFormatException e) {
		System.out.println("数字で入力してください");
		throw e;
	}
	return val;
}
}
class NoInputException extends Exception {
}