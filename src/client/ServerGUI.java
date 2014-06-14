package client;

import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerGUI extends Application{

    private TableView itemTable = new TableView();
    
    public static void main(String[] args) {
            Application.launch(args);
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        Administrator admin = new Administrator("localhost",3000);
        Map<String, List<String>> allItemList = admin.getAllItemList();
        
        final ObservableList<TableItem> data = FXCollections.observableArrayList();
        for (String category : allItemList.keySet()) {
            for (String item : allItemList.get(category)) {
                data.add(new TableItem(category,item));
            }
        }

        Group root  = new Group();
        Scene scene = new Scene(root, 600, 350, Color.AQUA);


        HBox mainFrame = new HBox(); // left for side bar ; right for table
        mainFrame.setPadding(new Insets(10,30,10,30));
        mainFrame.setSpacing(50);

        TableColumn categoryCol = new TableColumn("Category");
        TableColumn itemCol = new TableColumn("Item");
        categoryCol.setCellValueFactory(new PropertyValueFactory<TableItem,String>("category"));
        itemCol.setCellValueFactory(new PropertyValueFactory<TableItem,String>("itemName"));
        
        itemTable.setItems(data);
        itemTable.getColumns().addAll(categoryCol, itemCol);

        VBox leftsidebar=new VBox(); 
        leftsidebar.setPadding(new Insets(50,0,0,0));
        leftsidebar.setSpacing(20);
        leftsidebar.setMaxWidth(200);

        VBox buttonbox=new VBox(); 
        //buttonbox.setPadding(new Insets(30,10,30,10));
        Button btn1=new Button("Button1");
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

            }
        });
        Button btn2=new Button("Button2");
        buttonbox.getChildren().addAll(btn1,btn2);
        buttonbox.setSpacing(50);
        buttonbox.setAlignment(Pos.CENTER);

        final Separator sepHor = new Separator();
        sepHor.setValignment(VPos.CENTER);

        VBox loginbox=new VBox(); 
        //loginbox.setPadding(new Insets(10,10,10,10));
        loginbox.setSpacing(10);
        Label loginMsg = new Label("Hello, Guest!");
        TextField accountGrid = new TextField();
        PasswordField pwGrid = new PasswordField();

        HBox accountbox = new HBox();
        accountbox.getChildren().addAll(new Label("Account: "),accountGrid);

        HBox pwbox = new HBox();
        pwbox.getChildren().addAll(new Label("Password: "),pwGrid);
        
        
        
        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
//                System.out.println("[" + accountGrid.getText() + "]");
//                System.out.println("[" + pwGrid.getText() + "]");
                boolean loginAccept=admin.login(accountGrid.getText(),pwGrid.getText());
                if(loginAccept){
                    loginMsg.setText("Hello, "+accountGrid.getText()+"!");
                }
                else{
                    loginMsg.setText("Password Error!");
                }
            }
        });
        
        HBox loginbtnbox = new HBox();
        loginbtnbox.getChildren().addAll(loginBtn);
        loginbox.getChildren().addAll(loginMsg,accountbox,pwbox,loginbtnbox);

        leftsidebar.getChildren().addAll(buttonbox,sepHor,loginbox);


        mainFrame.getChildren().addAll(leftsidebar,itemTable);
        root.getChildren().add(mainFrame);

        mainStage.setScene(scene);
        mainStage.show();

        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                admin.close();
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
