package homebook2;

import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.security.auth.login.LoginContext;
import javax.swing.JOptionPane;

import org.omg.CORBA.Request;

import common.MyUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeBookController implements Initializable {
	private Connection conn = MyUtils.getConnection();

	@FXML
	private Label labUserid;

	@FXML
	private Button btnRefresh;

	@FXML
	private Button btnAccountReg;
	@FXML
	private Button btnLogout;

	@FXML
	private Button btnRevenue;

	@FXML
	private Button bntSum;

	@FXML
	private ComboBox<String> comTitle;

	@FXML
	private ComboBox<String> comSection;

	@FXML
	private TableColumn<HomeBook, String> colSection;

	@FXML
	private TableColumn<HomeBook, String> colTitle;

	@FXML
	private TableColumn<HomeBook, String> colDay;

	@FXML
	private TableColumn<HomeBook, Long> colRevenue;

	@FXML
	private TableColumn<HomeBook, Long> colExpense;

	@FXML
	private TableColumn<HomeBook, String> colRemark;

	@FXML
	private TableColumn<HomeBook, String> colUserid;

	@FXML
	private TableColumn<HomeBook, Long> colSerialno;

	@FXML
	private TextField txtSerial;

	@FXML
	private TextField txtRevenue;

	@FXML
	private TextField txtRemark;

	@FXML
	private TextField txtSum;

	@FXML
	private TextField txtExpense;

	@FXML
	private DatePicker txtDay;

	@FXML
	private TableView<HomeBook> table;

//	Date today = new Date();
//	SimpleDateFormat regdate = new SimpleDateFormat("yyyyMMdd");

	@FXML
	void actsum(ActionEvent event) {

		HomeBookDAO dao = new HomeBookDAO();

		try {
			txtSum.setText(dao.sum1(conn) + "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void actinsert(ActionEvent event) throws Exception {
		try {
			HomeBookDAO dao = new HomeBookDAO();

			HomeBook t = new HomeBook();
			// serialNo는 auto incremental을 사용하여 자동으로 추가되도록 설정
			// vo.setSerialNo(Long.parseLong(txtSerial.getText()));
			// 텍스트필드를 읽어서 vo에 넣는다.
			t.setSerialno(getNewSerialNumber());
			t.setUserid(labUserid.getText());
			t.setDay((txtDay.getValue() + ""));
			t.setSection(comSection.getValue());
			t.setTitleid(dao.gettitleid(comTitle.getValue()));
			t.setTitle(comTitle.getValue());
			t.setRemark(txtRemark.getText());
			t.setRevenue(Revenue());
			table.getItems().addAll(t);
			if (dao.insert(t, conn)) {
				JOptionPane.showMessageDialog(null, t.getRemark() + ": 등록완료");
				dao.close();
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 값을 넣으면 refresh메소드 호출해서 새로고침된다.
		actrefresh(event);
	}

	@FXML
	void selectRow(MouseEvent event) {
		int index = table.getSelectionModel().getFocusedIndex();
		HomeBook vo = table.getSelectionModel().getSelectedItem();
		// txtSerial.setText(vo.getSerialNo()+ "");
		// labUserid.setText(vo.getUserid());
		txtDay.setPromptText(vo.getDay());
		comSection.setValue(vo.getSection());
		comTitle.setValue(vo.getTitle());
		txtRemark.setText(vo.getRemark());
		txtRevenue.setText(vo.getRevenue() + "");

	}

	@FXML
	void actrefresh(ActionEvent event) throws Exception {
		// 테이블 한번 지우고 컬럼을 새롭게 불러온다.
		table.getItems().clear();
		// 데이터 랜더링
		colSerialno.setCellValueFactory(new PropertyValueFactory<HomeBook, Long>("serialno"));
		// 가져올때 DB의 컬럼명을 가져오는것이 아니라 HomeBook클래스의 필드명을 가져오므로 주의한다.
		colUserid.setCellValueFactory(new PropertyValueFactory<HomeBook, String>("userid"));
		colDay.setCellValueFactory(new PropertyValueFactory<HomeBook, String>("day"));
		colSection.setCellValueFactory(new PropertyValueFactory<HomeBook, String>("section"));
		colTitle.setCellValueFactory(new PropertyValueFactory<HomeBook, String>("title"));
		colRemark.setCellValueFactory(new PropertyValueFactory<HomeBook, String>("remark"));
		colRevenue.setCellValueFactory(new PropertyValueFactory<HomeBook, Long>("revenue"));
		HomeBookDAO dao = new HomeBookDAO();
		try {
			List<HomeBook> data = dao.selectAll(conn);
			table.getItems().addAll(data);

			comTitle.getItems().clear();
			comTitle.getItems().addAll(dao.gettitle());
			comTitle.setValue(dao.gettitle().get(0)); // 기본값

			dao.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void actdelete(ActionEvent event) {
		try {
			HomeBookDAO dao = new HomeBookDAO();
			//
			HomeBook selVo = table.getSelectionModel().getSelectedItem();
			int index = table.getSelectionModel().getSelectedIndex();
			Long key = selVo.getSerialno();
			if (!labUserid.getText().equals(selVo.getUserid()))  {
				JOptionPane.showMessageDialog(null, selVo.getRemark() + ": 삭제권한이 없습니다.");
			}
			else {
				int result = JOptionPane.showConfirmDialog(null, "데이터를 삭제하시겠습니까?");
				if(result ==JOptionPane.YES_OPTION) {
					JOptionPane.showMessageDialog(null, selVo.getRemark() + ": 자료삭제");
					dao.delete(key, conn);
					table.getItems().remove(index);
					dao.close();
			}
				else {}
			}
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		HomeBookDAO dao = new HomeBookDAO();

//		vo리스트로 형태로 가져오기-> string리스트 형태로 저장 -> comTitle에 삽입

		comTitle.getItems().addAll(dao.gettitle());
		comTitle.setValue(dao.gettitle().get(1)); // 기본값

		comSection.getItems().addAll("지출", "수입");
		comSection.setValue("지출"); // 기본값

		txtDay.setValue(LocalDate.now());

		// 데이터 랜더링
		colSerialno.setCellValueFactory(new PropertyValueFactory<HomeBook, Long>("serialno"));
		// 가져올때 DB의 컬럼명을 가져오는것이 아니라 HomeBook클래스의 필드명을 가져오므로 주의한다.
		colUserid.setCellValueFactory(new PropertyValueFactory<HomeBook, String>("userid"));
		colDay.setCellValueFactory(new PropertyValueFactory<HomeBook, String>("day"));
		colSection.setCellValueFactory(new PropertyValueFactory<HomeBook, String>("section"));
		colTitle.setCellValueFactory(new PropertyValueFactory<HomeBook, String>("title"));
		colRemark.setCellValueFactory(new PropertyValueFactory<HomeBook, String>("remark"));
		colRevenue.setCellValueFactory(new PropertyValueFactory<HomeBook, Long>("revenue"));

		HomeBook vo = new HomeBook();

		try {
			List<HomeBook> data = dao.selectAll(conn);
			table.getItems().addAll(data);
			dao.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setID(String aa) {
		labUserid.setText(aa);
	}

	private long getNewSerialNumber() {
		// 새로운 시리얼넘버를 생성하여 주는 함수
		long num = 0;
		try {
			HomeBookDAO dao = new HomeBookDAO();
			num = dao.getMaxSerialno(conn) + 1;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}

	@FXML
	void actAccountReg(ActionEvent event) {
		try {
			Stage newStage = new Stage();
			Stage stage = (Stage) btnAccountReg.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("account_title.fxml"));
			BorderPane second = loader.load();
			Scene sc = new Scene(second);
			newStage.setScene(sc);
			newStage.show();
			// stage.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void logout(ActionEvent event) {
		JOptionPane.showMessageDialog(null, "로그아웃 되었습니다.");
		try {

			Stage newStage = new Stage();
			Stage stage = (Stage) btnLogout.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
			BorderPane second = loader.load();
			Scene sc = new Scene(second);
			newStage.setScene(sc);
			newStage.show();
			stage.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	long Revenue() {
		long confirmMoney;
		if (comSection.getValue().equals("지출")) {
			confirmMoney = (Long.parseLong(txtRevenue.getText())) * -1;
			return confirmMoney;
		} else {
			confirmMoney = (Long.parseLong(txtRevenue.getText())) * 1;
			return confirmMoney;

		}

	}

}
