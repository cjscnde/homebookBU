package homebook2;



import java.awt.HeadlessException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import common.MyUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AccountController implements Initializable {

    @FXML
    private TextField txtTitleid;
    
    @FXML
    private TextField txtTitle;
    
    @FXML
    private TextField txtFind;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsert;
    
    @FXML
    private Button btnFind;

    @FXML
    private TableColumn<AccountTitle, String> colTitleid;

    @FXML
    private TableColumn<AccountTitle, String> colTitle;

    @FXML
    private TableView<AccountTitle> table;

    

    private Connection conn = MyUtils.getConnection();
    //private Connection conn = null;

	private ActionEvent event;
    
    
    @FXML
    void  ActInsert () {
    	try {
			AccountTitle vo = new AccountTitle();
			AccountTitleDAO dao = new AccountTitleDAO();
			
			vo.setTitle_id(txtTitleid.getText());
			vo.setTitle(txtTitle.getText());
			if(dao.insert(vo,conn)) {
				JOptionPane.showMessageDialog(null, vo.getTitle() + "등록완료");
			}
			table.getItems().addAll(vo);
			dao.close();
			
			
			
//			HomeBookController homecontroller  = new HomeBookController();
//			homecontroller.actrefresh(event);
			
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }


    @FXML
    void  ActDelete () {
    	try {
			AccountTitle vo = new AccountTitle();
			AccountTitleDAO dao = new AccountTitleDAO();
			AccountTitle selVo = table.getSelectionModel().getSelectedItem();
			int index = table.getSelectionModel().getSelectedIndex();
			String key = selVo.getTitle_id();
			if(dao.delete(key,conn)) {
				JOptionPane.showMessageDialog(null, selVo.getTitle() + "자료삭제");
				//initialize(null, null);
				
			}
			table.getItems().clear();
			List<AccountTitle> data = dao.selectAll(conn);
			table.getItems().addAll(data);
			
			dao.close();
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    @FXML
    void  ActFind () { 	
    	try {
    		ObservableList<AccountTitle> data = table.getItems();
			int num= 0;
			for (int i=0; i <data.size(); i++) {
				if (data.get(i).getTitle().equals(txtFind.getText())) {
					num = i;
					break;
				}
			}
			table.scrollTo(num);
			table.getSelectionModel().select(num);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			colTitleid.setCellValueFactory(new PropertyValueFactory<AccountTitle, String>("title_id"));
			colTitle.setCellValueFactory(new PropertyValueFactory<AccountTitle, String>("title"));
			AccountTitleDAO dao = new AccountTitleDAO();
			List<AccountTitle> data = dao.selectAll(conn);
			table.getItems().addAll(data);
			//dao.close(conn);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
    
    
    
    
    
}
