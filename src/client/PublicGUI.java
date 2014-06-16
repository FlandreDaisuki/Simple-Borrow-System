package client;


import database.Duration;
import database.ItemTag;
import java.time.LocalDate;
import java.util.Date;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private LocalDate _queryDateFrom;
    private LocalDate _queryDateTo;
    
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
        
        VBox backFrame1v = new VBox();
        HBox titleAndLoginMsgFrame2h = new HBox();
        Label titleLabel = new Label("Borrow System");
        Label loginMsgLabel = new Label("Hello, "+_loginName+"!");
        titleAndLoginMsgFrame2h.getChildren().addAll(titleLabel,loginMsgLabel);
        HBox tabFrame2h = new HBox();
        
        ToggleGroup tgroup = new ToggleGroup();
        
        // Toggle for Login
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
        
        
        // Toggle for List All
        ToggleButton tb2 = new ToggleButton("List All");
        tb2.setToggleGroup(tgroup);
        tb2.setUserData(_itemTable);
        
        // Toggle for Query
        ToggleButton tb3 = new ToggleButton("Query");
        tb3.setToggleGroup(tgroup);
        VBox queryFrame3v = new VBox();
        
        HBox quaryCategoryFrame4h = new HBox();
        Label quaryCategoryLabel = new Label("Category: ");
        ChoiceBox quaryCategoryCB = new ChoiceBox(FXCollections.observableArrayList(user.getAllItemList().keySet()));
        quaryCategoryFrame4h.getChildren().addAll(quaryCategoryLabel,quaryCategoryCB);
        
        HBox quaryNameFrame4h = new HBox();
        Label quaryNameLabel = new Label("Name: ");
        ChoiceBox quaryNameCB = new ChoiceBox(FXCollections.observableArrayList());
        quaryCategoryCB.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Object> () {
                @Override
                public void changed(ObservableValue<? extends Object> ov, Object t, Object new_t) {
                    quaryNameCB.setItems(FXCollections.observableArrayList(user.getAllItemList().get(new_t)));
                }
            }
        );
        quaryNameFrame4h.getChildren().addAll(quaryNameLabel,quaryNameCB);
        
        HBox quaryDateFrame4h = new HBox();
        quaryDateFrame4h.setSpacing(20);
        
        VBox queryDateFromFrame5v = new VBox();
        Label queryDateFromLabel = new Label("From :");
        DatePicker dateFromPicker = new DatePicker();
        dateFromPicker.setOnAction(new EventHandler() {
             @Override public void handle(Event t) {
                _queryDateFrom = dateFromPicker.getValue();
            }
        });
        queryDateFromFrame5v.getChildren().addAll(queryDateFromLabel,dateFromPicker);
        
        VBox queryDateToFrame5v = new VBox();
        Label queryDateToLabel = new Label("To :");
        DatePicker dateToPicker = new DatePicker();
        dateToPicker.setOnAction(new EventHandler() {
            @Override public void handle(Event t) {
                _queryDateTo = dateToPicker.getValue();
            }
        });
        queryDateToFrame5v.getChildren().addAll(queryDateToLabel,dateToPicker);
        quaryDateFrame4h.getChildren().addAll(queryDateFromFrame5v,queryDateToFrame5v);
        
        HBox quaryLowestFrame4h = new HBox();
        quaryLowestFrame4h.setSpacing(15);
        Label queryInfo = new Label("");
        Button queryButton = new Button("Query!");
        
        queryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                
                String selectedCategory=(String) quaryCategoryCB.getSelectionModel().getSelectedItem();
                String selectedName=(String) quaryNameCB.getSelectionModel().getSelectedItem();
                
                if(_queryDateFrom!=null && _queryDateTo!=null && !selectedCategory.isEmpty() && !selectedName.isEmpty())
                {
                    ItemTag itg = new ItemTag(selectedCategory,selectedName);
                    Duration dur=new Duration(new Date(_queryDateFrom.toEpochDay()),new Date(_queryDateTo.toEpochDay()));
                    boolean queryFetch = user.query(itg,dur);
                    
                    if(queryFetch){
                        queryInfo.setText("Can Borrow!");
                    }else{
                        queryInfo.setText("Have been borrowed!");
                    }
                    
                }else{
                    queryInfo.setText("Some slot is NULL");
                }
            }
        });
        quaryLowestFrame4h.getChildren().addAll(queryInfo,queryButton);
        
        queryFrame3v.getChildren().addAll(quaryCategoryFrame4h,quaryNameFrame4h,quaryDateFrame4h,quaryLowestFrame4h);
        tb3.setUserData(queryFrame3v);
        
        // Toggle for Borrow
        ToggleButton tb4 = new ToggleButton("Borrow");
        tb4.setToggleGroup(tgroup);
        VBox borrowFrame3v = new VBox();
        
        HBox borrowCategoryFrame4h = new HBox();
        Label borrowCategoryLabel = new Label("Category: ");
        ChoiceBox borrowCategoryCB = new ChoiceBox(FXCollections.observableArrayList(user.getAllItemList().keySet()));
        borrowCategoryFrame4h.getChildren().addAll(borrowCategoryLabel,borrowCategoryCB);
        
        HBox borrowNameFrame4h = new HBox();
        Label borrowNameLabel = new Label("Name: ");
        ChoiceBox borrowNameCB = new ChoiceBox(FXCollections.observableArrayList());
        borrowCategoryCB.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Object> () {
                @Override
                public void changed(ObservableValue<? extends Object> ov, Object t, Object new_t) {
                    borrowNameCB.setItems(FXCollections.observableArrayList(user.getAllItemList().get(new_t)));
                }
            }
        );
        borrowNameFrame4h.getChildren().addAll(borrowNameLabel,borrowNameCB);
        
        HBox borrowDateFrame4h = new HBox();
        borrowDateFrame4h.setSpacing(20);
        
        VBox borrowDateFromFrame5v = new VBox();
        Label borrowDateFromLabel = new Label("From :");
        DatePicker borrowDateFromPicker = new DatePicker();
        borrowDateFromPicker.setOnAction(new EventHandler() {
             @Override public void handle(Event t) {
                _queryDateFrom = borrowDateFromPicker.getValue();
            }
        });
        borrowDateFromFrame5v.getChildren().addAll(borrowDateFromLabel,borrowDateFromPicker);
        
        VBox borrowDateToFrame5v = new VBox();
        Label borrowDateToLabel = new Label("To :");
        DatePicker borrowDateToPicker = new DatePicker();
        borrowDateToPicker.setOnAction(new EventHandler() {
            @Override public void handle(Event t) {
                _queryDateTo = borrowDateToPicker.getValue();
            }
        });
        borrowDateToFrame5v.getChildren().addAll(borrowDateToLabel,borrowDateToPicker);
        borrowDateFrame4h.getChildren().addAll(borrowDateFromFrame5v,borrowDateToFrame5v);
        
        HBox borrowLowestFrame4h = new HBox();
        borrowLowestFrame4h.setSpacing(15);
        Label borrowInfo = new Label("BorrowInfo");
        Button borrowButton = new Button("Borrow!");
        
        borrowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                
                String selectedCategory=(String) borrowCategoryCB.getSelectionModel().getSelectedItem();
                String selectedName=(String) borrowNameCB.getSelectionModel().getSelectedItem();
                
                if(_queryDateFrom!=null && _queryDateTo!=null && !selectedCategory.isEmpty() && !selectedName.isEmpty())
                {
                    ItemTag itg = new ItemTag(selectedCategory,selectedName);
                    Duration dur=new Duration(new Date(_queryDateFrom.toEpochDay()),new Date(_queryDateTo.toEpochDay()));
                    boolean borrowFetch = user.borrow(itg,dur);
                    
                    if(borrowFetch){
                        borrowInfo.setText("Borrow Successful !");
                    }else{
                        if(_loginBool) {
                            borrowInfo.setText("Have been borrowed !");
                        } else {
                            borrowInfo.setText("Guest can't borrow !");
                        }
                    }
                    
                }else{
                    borrowInfo.setText("Some slot is NULL");
                }
            }
        });
        
        borrowLowestFrame4h.getChildren().addAll(borrowInfo,borrowButton);
        borrowFrame3v.getChildren().addAll(borrowCategoryFrame4h,borrowNameFrame4h,borrowDateFrame4h,borrowLowestFrame4h);
        tb4.setUserData(borrowFrame3v);
        tabFrame2h.getChildren().addAll(tb1,tb2,tb3,tb4);
        
        ImageView introFrame3 = new ImageView();
        Image introImg = new Image("/client/img/intro.png", true);
        introFrame3.setImage(introImg);

        VBox displayFrame2v = new VBox();
        displayFrame2v.getChildren().addAll(loginFrame3v);
        
        tgroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle toggle, Toggle new_toggle) {
                        
                    if (new_toggle == null) {
                        displayFrame2v.getChildren().clear();
                        displayFrame2v.getChildren().add(introFrame3);
                    } else {
                        displayFrame2v.getChildren().clear();
                        displayFrame2v.getChildren().add((Node)tgroup.getSelectedToggle().getUserData());
                    }
                 }
        });
        
        
        /*CSS setting*/
        titleLabel.setId("titlelabel");
        loginMsgLabel.setId("loginmsg");
        titleAndLoginMsgFrame2h.setId("title");
        loginFrame3v.setId("loginframe3v");
        tabFrame2h.setId("tabframe");
        _itemTable.setId("itemtable");
        tb1.setId("tb1");
        tb2.setId("tb2");
        tb3.setId("tb3");
        tb4.setId("tb4");
        queryFrame3v.setId("queryframe3v");
        /*CSS setting*/
        
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
