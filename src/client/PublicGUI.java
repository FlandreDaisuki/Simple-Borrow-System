package client;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 *
 * @author VampireNight
 */
public class PublicGUI extends Application{
    private TableView _itemTable = new TableView();
    private boolean _loginBool = false;
    private String _loginName = "Guest";
    
    public static void main(String[] args) {
            Application.launch(args);
    }
    
    @Override
    public void start(Stage mainStage) throws Exception {
        Administrator user = new Administrator("localhost",3000);
//        User user = new User("localhost",3000);
        
        final ObservableList<PublicGUI.TableItem> data = FXCollections.observableArrayList();
        for (String category : user.getAllItemList().keySet()) {
            for (String item : user.getAllItemList().get(category)) {
                data.add(new PublicGUI.TableItem(category,item));
            }
        }
        TableColumn categoryCol = new TableColumn("Category");
        TableColumn itemCol = new TableColumn("Item");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        itemCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        
        _itemTable.setItems(data);
        _itemTable.getColumns().addAll(categoryCol, itemCol);
        //Initialized

        //GridPane root  = new GridPane();
        //root.setId("root");
        
        
        VBox backFrame1v = new VBox();
        HBox titleAndLoginMsgFrame2h = new HBox();
        Label titleLabel = new Label("Borrow System");
        Label loginMsgLabel = new Label("Hello, "+_loginName+"!");
        titleAndLoginMsgFrame2h.getChildren().addAll(titleLabel,loginMsgLabel);
        HBox tabFrame2h = new HBox();
        
        ToggleGroup tgroup = new ToggleGroup();
        ToggleButton tb1 = new ToggleButton("Login/Logout");
        tb1.setToggleGroup(tgroup);
        tb1.setSelected(true);
        
        VBox loginFrame3v = new VBox();
        Label loginInfo=new Label("");
        HBox loginAccoutFrame4h = new HBox();
        Label loginAccoutLabel = new Label("Username :");
        TextField loginAccoutField = new TextField();
        loginAccoutFrame4h.getChildren().addAll(loginAccoutLabel,loginAccoutField);
        HBox loginPasswordFrame4h = new HBox();
        Label loginPasswordLabel = new Label("Password :");
        PasswordField loginPasswordField = new PasswordField();
        loginPasswordFrame4h.getChildren().addAll(loginPasswordLabel,loginPasswordField);
        HBox loginButtonFrame4h = new HBox();
        Button loginButton = new Button("Login");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                boolean loginAccept=user.login(loginAccoutField.getText(),loginPasswordField.getText());
                
                if(_loginBool){
                    loginInfo.setText(_loginName+" has logined !");
                }else{
                    if(loginAccept){
                        _loginBool=true;
                        _loginName=loginAccoutField.getText();
                        loginInfo.setText("Hello, "+_loginName+"!");
                        loginMsgLabel.setText("Hello, "+_loginName+"!");
                        //systemMsg.setText(admin.getMessages().toString());
                    }else{
                        loginInfo.setText("Password Error!");
                    }
                }
            }
        });
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(_loginBool){
                    loginInfo.setText("Bye, "+_loginName+"!");
                    user.logout();
                    //systemMsg.setText(admin.getMessages().toString());
                    _loginBool=false;
                    _loginName="Guest";
                    
                    loginMsgLabel.setText("Hello, "+_loginName+"!");
                }
                else{
                    loginInfo.setText("What are you doing!?");
                }
            }
        });
        loginButtonFrame4h.getChildren().addAll(loginButton,logoutButton);
        loginFrame3v.getChildren().addAll(loginInfo,loginAccoutFrame4h,loginPasswordFrame4h,loginButtonFrame4h);
        tb1.setUserData(loginFrame3v);
        
        ToggleButton tb2 = new ToggleButton("List All");
        tb2.setToggleGroup(tgroup);
        tb2.setUserData(_itemTable);
        
        ToggleButton tb3 = new ToggleButton("Query");
        tb3.setToggleGroup(tgroup);
        VBox queryFrame3v = new VBox();
        
        HBox quaryCategoryFrame4h = new HBox();
        Label quaryCategoryLabel = new Label("Category: ");
        ChoiceBox quaryCategoryCB = new ChoiceBox(FXCollections.observableArrayList("ABC","DEF"));
        quaryCategoryFrame4h.getChildren().addAll(quaryCategoryLabel,quaryCategoryCB);
        
        HBox quaryNameFrame4h = new HBox();
        Label quaryNameLabel = new Label("Name: ");
        ChoiceBox quaryNameCB = new ChoiceBox(FXCollections.observableArrayList("CBA","FED"));
        quaryNameFrame4h.getChildren().addAll(quaryNameLabel,quaryNameCB);
        
        HBox quaryDateFrame4h = new HBox();
        VBox queryDateFromFrame5v = new VBox();
        Label queryDateFromLabel = new Label("From :");
        DatePicker dateFromPicker = new DatePicker();
        dateFromPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                //LocalDate dateFrom = dateFromPicker.getValue();
                //System.err.println("Selected date: " + dateFrom);
            }
        });
        queryDateFromFrame5v.getChildren().addAll(queryDateFromLabel,dateFromPicker);
        
        VBox queryDateToFrame5v = new VBox();
        Label queryDateToLabel = new Label("To :");
        DatePicker dateToPicker = new DatePicker();
        dateToPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                //LocalDate dateFrom = dateFromPicker.getValue();
                //System.err.println("Selected date: " + dateFrom);
            }
        });
        queryDateToFrame5v.getChildren().addAll(queryDateToLabel,dateToPicker);
        quaryDateFrame4h.getChildren().addAll(queryDateFromFrame5v,queryDateToFrame5v);
        
        HBox quaryLowestFrame4h = new HBox();
        Label queryInfo = new Label("queryInfo");
        Button queryButton = new Button("Query!");
        quaryLowestFrame4h.getChildren().addAll(queryInfo,queryButton);
        
        queryFrame3v.getChildren().addAll(quaryCategoryFrame4h,quaryNameFrame4h,quaryDateFrame4h,quaryLowestFrame4h);
        tb3.setUserData(queryFrame3v);
        
        ToggleButton tb4 = new ToggleButton("Borrow");
        tb4.setToggleGroup(tgroup);
        tabFrame2h.getChildren().addAll(tb1,tb2,tb3,tb4);
        
        
        
        
        tb4.setUserData(new Label("ABC-B"));

        VBox displayFrame2v = new VBox();
        displayFrame2v.getChildren().addAll(loginFrame3v);
        
        tgroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle toggle, Toggle new_toggle) {
                    if (new_toggle == null)
                    {
                        displayFrame2v.getChildren().clear();
                    }
                    else
                    {
                        displayFrame2v.getChildren().clear();
                        displayFrame2v.getChildren().add((Node)tgroup.getSelectedToggle().getUserData());
                    }

                 }
        });
        
        
        
        
        backFrame1v.getChildren().addAll(titleAndLoginMsgFrame2h,tabFrame2h,displayFrame2v);
        Scene scene = new Scene(backFrame1v, 600, 600);
        scene.getStylesheets().add(this.getClass().getResource("PublicGUI.css").toExternalForm());
        mainStage.setScene(scene);
        mainStage.show();

        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                user.close();
            }
        });
    }
    
    public static class TableItem {
        private final SimpleStringProperty category;
        private final SimpleStringProperty itemName;

        private TableItem(String cName, String iName) {
            this.category = new SimpleStringProperty(cName);
            this.itemName = new SimpleStringProperty(iName);
        }

        public String getCategory() {
            return category.get();
        }
        public void setCategory(String cName) {
            category.set(cName);
        }

        public String getItemName() {
            return itemName.get();
        }

        public void setItemName(String iName) {
            itemName.set(iName);
        }
    }
}
