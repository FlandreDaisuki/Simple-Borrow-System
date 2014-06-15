package client;

import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

        GridPane root  = new GridPane();
        //root.setId("root");
        Scene scene = new Scene(root, 600, 600,Color.BLUEVIOLET);
        scene.getStylesheets().add(PublicGUI.class.getResource("PublicGUI.css").toExternalForm()); //fail to set on
        
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

        ToggleButton tb2 = new ToggleButton("List All");
        tb2.setToggleGroup(tgroup);

        ToggleButton tb3 = new ToggleButton("Query");
        tb3.setToggleGroup(tgroup);
        
        ToggleButton tb4 = new ToggleButton("Borrow");
        tb4.setToggleGroup(tgroup);
        tabFrame2h.getChildren().addAll(tb1,tb2,tb3,tb4);
        
        tb1.setUserData(new Label("ABC-A"));
        tb2.setUserData(_itemTable);
        tb3.setUserData(new Label("ABC-C"));
        tb4.setUserData(new Label("ABC-B"));

        VBox displayFrame2v = new VBox();
        
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
        root.getChildren().addAll(backFrame1v);
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
