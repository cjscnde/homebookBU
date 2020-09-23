package homebook2;

import java.awt.HeadlessException;

import java.io.IOException;
import java.sql.Connection;

import javax.swing.JOptionPane;

import common.MyUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;



public class loginController {
	@FXML
	private TextField txtID;

	@FXML
	private TextField txtPassword;

	@FXML
	private Button btnLogin;
	

	@FXML
	void login(ActionEvent event) {
		MemberDAO dao = new MemberDAO();
		long logininfo = dao.Login(txtID.getText(), txtPassword.getText());

		if (logininfo != 1) {
			JOptionPane.showMessageDialog(null, "로그인실패");

		} else {
			login_success(event);
		}
	}

	@FXML
	void login_success(ActionEvent event) {

		try {
			Stage newStage = new Stage();

			Stage stage = (Stage) btnLogin.getScene().getWindow();
			// 새 레이아웃 추가
			FXMLLoader loader = new FXMLLoader(getClass().getResource("homebookFx.fxml"));
			// 씬에 레이아웃 추가

			BorderPane second = loader.load();
			HomeBookController controller = loader.getController();
			controller.setID(txtID.getText());

			Scene sc = new Scene(second);
			// 씬을 스테이지에서 상영
			
			
			newStage.setScene(sc);
			newStage.show();
//            // 기존 페이지 삭제
			stage.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
