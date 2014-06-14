package client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ServerGUI extends Application{

	public static void main(String[] args) {
		Application.launch(args);
	}

        @Override
	public void start(Stage mainStage) throws Exception {
//            User user = new User("localhost", 3000);
//            user.login("Scott", "s123456");
//            user.logout();
//            user.close();
            
            Group root  = new Group();
            Scene scene = new Scene(root, 500, 300, Color.AQUA);
            HBox mainFrame = new HBox(); // left for side bar ; right for table
            
            VBox leftsidebar=new VBox(); 
            leftsidebar.setPadding(new Insets(10,10,10,10));
            int abc=10;
            
            Button btn1=new Button("Button1");
            btn1.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    
                }
            });
            Button btn2=new Button("Button2");
            leftsidebar.getChildren().addAll(btn1,btn2);
            
            
            GridPane loginPane=new GridPane();
            loginPane.setPadding(new Insets(10,10,10,10));
            loginPane.setVgap(5);
            loginPane.setHgap(5);
            
            TextField accountPane = new TextField();
            PasswordField pwPane = new PasswordField();
            //loginPane.getChildren().addAll(accountPane,pwPane);
            leftsidebar.getChildren().addAll(accountPane,pwPane);
            
            root.getChildren().add(leftsidebar);
            mainStage.setScene(scene);
            mainStage.show();

	}

}
