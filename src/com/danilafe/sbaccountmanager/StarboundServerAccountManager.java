/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.danilafe.sbaccountmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class StarboundServerAccountManager extends Application {
    
    private final ArrayList<String> users = new ArrayList<String>();
    private final HashMap<String, String> userpasswords = new HashMap<String, String>();
    private final ArrayList<String> banned_usernames = new ArrayList<String>();
    private final ArrayList<String> banned_ips = new ArrayList<String>();
    private final ArrayList<String> banned_playernames = new ArrayList<String>();
    private File config;
    
    @Override
    public void start(Stage primaryStage) {
        parseJSON();
        openAccountManager();
    }
    
    private void openAccountManager(){
        //Window Crap
        Stage accountManagerStage = new Stage();
        accountManagerStage.setTitle("Starbound Server Account Manager");
        
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(10);
        gp.setHgap(10);
        gp.setPadding(new Insets(25, 25, 25, 25));
        
        Text title = new Text("Starbound Account Manager");
        title.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 20));
        gp.add(title, 0, 0, 2, 1);
        
        ListView<String> user_list = new ListView<String>();
        ObservableList<String> user_list_o = FXCollections.observableArrayList(users);
        user_list.setItems(user_list_o);
        user_list.setPrefHeight(50);
        
        ListView<String> banned_users_list = new ListView<String>();
        ObservableList<String> banned_users_list_o = FXCollections.observableArrayList(banned_usernames);
        banned_users_list.setItems(banned_users_list_o);
        banned_users_list.setPrefHeight(50);
                
        ListView<String> banned_ips_list = new ListView<String>();
        ObservableList<String> banned_ips_list_o = FXCollections.observableArrayList(banned_ips);
        banned_ips_list.setItems(banned_ips_list_o);
        banned_ips_list.setPrefHeight(50);
                
        ListView<String> banned_playernames_list = new ListView<String>();
        ObservableList<String> banned_playernames_list_o = FXCollections.observableArrayList(banned_playernames);
        banned_playernames_list.setItems(banned_playernames_list_o);
        banned_playernames_list.setPrefHeight(50);
        
        Label user_list_l = new Label("Current Users");
        Label banned_users_list_l = new Label("Banned Users");
        Label banned_ips_list_l = new Label("Banned IP's");
        Label banned_playernames_list_l = new Label("Banned Player Names");
        
        Button add_user = new Button("Add User");
        Button add_banned_user = new Button("Add Banned User");
        Button add_banned_ip = new Button("Add Banned IP");
        Button add_banned_playername = new Button("Add Banner Playername");
        
        Button remove_user = new Button("Remove User");
        Button remove_banned_user = new Button("Remove Banned User");
        Button remove_banned_ip = new Button("Remove Banned IP");
        Button remove_banned_playername = new Button("Remove Banner Playername");
        
        HBox userbox = new HBox();
        userbox.setAlignment(Pos.BOTTOM_LEFT);
        userbox.getChildren().addAll(add_user, remove_user);
        userbox.setSpacing(5);
        
        HBox b_userbox = new HBox();
        b_userbox.setAlignment(Pos.BOTTOM_LEFT);
        b_userbox.getChildren().addAll(add_banned_user, remove_banned_user);
        b_userbox.setSpacing(5);
        
        HBox b_ipbox = new HBox();
        b_ipbox.setAlignment(Pos.BOTTOM_LEFT);
        b_ipbox.getChildren().addAll(add_banned_ip, remove_banned_ip);
        b_ipbox.setSpacing(5);       
        
        HBox b_playerbox = new HBox();
        b_playerbox.setAlignment(Pos.BOTTOM_LEFT);
        b_playerbox.getChildren().addAll(add_banned_playername, remove_banned_playername);
        b_playerbox.setSpacing(5);
        
        Button save = new Button("Save Config");
        save.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                save();
            }
                    
        });
        
        add_user.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                createAccount(user_list);
            }
            
        });
        add_banned_user.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                createBannedUser(banned_users_list);
            }
            
        });
        add_banned_ip.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                createBannedIP(banned_ips_list);
            }
            
        });
        add_banned_playername.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                createBannedPlayername(banned_playernames_list);
            }
            
        });
        
        remove_user.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                users.remove(user_list.getSelectionModel().getSelectedItem());
                user_list.setItems(FXCollections.observableArrayList(users));
            }
            
        });
        remove_banned_user.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                banned_usernames.remove(banned_users_list.getSelectionModel().getSelectedItem());
                banned_users_list.setItems(FXCollections.observableArrayList(banned_usernames));
            }
            
        });
        remove_banned_ip.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                banned_ips.remove(banned_ips_list.getSelectionModel().getSelectedItem());
                banned_ips_list.setItems(FXCollections.observableArrayList(banned_ips));
            }
            
        });
        remove_banned_playername.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                banned_playernames.remove(banned_playernames_list.getSelectionModel().getSelectedItem());
                banned_playernames_list.setItems(FXCollections.observableArrayList(banned_playernames));
            }
            
        });
        
        gp.add(user_list_l, 0, 1);
        gp.add(user_list, 0, 2);
        gp.add(userbox,0,3);
        gp.add(banned_users_list_l, 0, 4);
        gp.add(banned_users_list, 0, 5);
        gp.add(b_userbox,0,6);
        gp.add(banned_ips_list_l, 0, 7);
        gp.add(banned_ips_list, 0, 8);
        gp.add(b_ipbox,0,9);
        gp.add(banned_playernames_list_l, 0, 10);
        gp.add(banned_playernames_list, 0, 11);
        gp.add(b_playerbox,0,12);
        gp.add(save, 0, 13);
        
        Scene sc = new Scene(gp, 800/2,600);
        accountManagerStage.setScene(sc);
        accountManagerStage.show();

    }
    
    private void createAccount(ListView<String> to_update){
        Stage createAccountStage = new Stage();
        createAccountStage.initModality(Modality.APPLICATION_MODAL);
        
        //Set the stage info
        createAccountStage.setTitle("Add Server Account");
        
        //Create a layout
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(10);
        gp.setHgap(10);
        gp.setPadding(new Insets(25, 25, 25, 25));
        
        //Ads the important things
        Text welcome = new Text("Create Server Account");
        welcome.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 20));
        gp.add(welcome, 0, 0, 2, 1);
        
        Label username = new Label("Username");
        Label password = new Label("Password");
        Label r_password = new Label("Repeat Password");
        
        TextField usernamefield = new TextField();
        PasswordField passwordfield = new PasswordField();
        PasswordField r_passwordfield = new PasswordField();
        
        gp.add(username, 0, 1);
        gp.add(password, 0, 2);
        gp.add(r_password, 0, 3);
        
        gp.add(usernamefield, 1, 1);
        gp.add(passwordfield, 1, 2);
        gp.add(r_passwordfield, 1, 3);
        
        Text error = new Text("");
        HBox error_box = new HBox(10);
        error_box.setAlignment(Pos.CENTER);
        error_box.getChildren().add(error);
        gp.add(error_box, 0, 4,2,1);
        
        Button finish = new Button("Finish");
        finish.setDisable(true);
        HBox center_button = new HBox(10);
        center_button.setAlignment(Pos.CENTER);
        center_button.getChildren().add(finish);
        gp.add(center_button, 0, 5,2,1);
                
        ChangeListener name = new ChangeListener<String>(){

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                finish.setDisable(true);
                if(usernamefield.getText().equals("")){
                    error.setFill(Color.RED);
                    error.setText("Username can not be blank!");
                }
                if(!passwordfield.getText().equals(r_passwordfield.getText())){
                    error.setFill(Color.RED);
                    error.setText("Passwords do not match!");
                }
                if(passwordfield.getText().equals("") && r_passwordfield.getText().equals("")){
                    error.setFill(Color.RED);
                    error.setText("Passwords can not be blank!");
                }
                if(passwordfield.getText().equals(r_passwordfield.getText()) && !usernamefield.getText().equals("") && !passwordfield.getText().equals("")){
                    error.setFill(Color.GREEN);
                    error.setText("No issues.");
                    finish.setDisable(false);
                }
            }
            
        };
        
        usernamefield.textProperty().addListener(name);
        passwordfield.textProperty().addListener(name);
        r_passwordfield.textProperty().addListener(name);
        

        finish.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent e) {

               users.remove(usernamefield.getText());
               users.add(usernamefield.getText());
               userpasswords.put(usernamefield.getText(), passwordfield.getText());
               to_update.setItems(FXCollections.observableArrayList(users));
               
               createAccountStage.close();
            }
            
        });
        
        //Creates the scene
        Scene scene = new Scene(gp, 300, 275);  
        scene.getStylesheets().add(this.getClass().getResource("login_css.css").toExternalForm());
        
        createAccountStage.setScene(scene);
        createAccountStage.show();

    }

    private void createBannedUser(ListView<String> to_update){
        Stage createBannedUser = new Stage();
        createBannedUser.setTitle("Add Banned User");
        createBannedUser.initModality(Modality.APPLICATION_MODAL);
        
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(25, 25, 25, 25));
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(10);
        gp.setHgap(10);
        
        Text title = new Text("Add Banned Username");
        title.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 20));
        gp.add(title, 0, 0, 2, 1);
        
        Label newusername = new Label("Ban Username");
        TextField username = new TextField();
        gp.add(newusername,0,1);
        gp.add(username,1,1);
        
        Button finish = new Button("Finish");
        HBox finish_box = new HBox(10);
        finish_box.setAlignment(Pos.CENTER);
        finish_box.getChildren().add(finish);
        
        finish.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                banned_usernames.remove(username.getText());
                banned_usernames.add(username.getText());
                to_update.setItems(FXCollections.observableArrayList(banned_usernames));
                createBannedUser.close();
            }
            
        });
        
        gp.add(finish_box, 0, 2,2,1);
        
        Scene sc = new Scene(gp, 300, 175);
        createBannedUser.setScene(sc);
        createBannedUser.show();
    }
    
    private void createBannedIP(ListView<String> to_update){
        Stage createBannedIP = new Stage();
        createBannedIP.setTitle("Add Banned IP");
        createBannedIP.initModality(Modality.APPLICATION_MODAL);
        
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(25, 25, 25, 25));
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(10);
        gp.setHgap(10);
        
        Text title = new Text("Add Banned IP");
        title.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 20));
        gp.add(title, 0, 0, 2, 1);
        
        Label newusername = new Label("Ban IP");
        TextField username = new TextField();
        gp.add(newusername,0,1);
        gp.add(username,1,1);
        
        Button finish = new Button("Finish");
        HBox finish_box = new HBox(10);
        finish_box.setAlignment(Pos.CENTER);
        finish_box.getChildren().add(finish);
        
        finish.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                banned_ips.remove(username.getText());
                banned_ips.add(username.getText());
                to_update.setItems(FXCollections.observableArrayList(banned_ips));
                createBannedIP.close();
            }
            
        });
        
        gp.add(finish_box, 0, 2,2,1);
        
        Scene sc = new Scene(gp, 300, 175);
        createBannedIP.setScene(sc);
        createBannedIP.show();
    }
    
    private void createBannedPlayername(ListView<String> to_update){
        Stage createBannedPlayername = new Stage();
        createBannedPlayername.setTitle("Add Banned Playername");
        createBannedPlayername.initModality(Modality.APPLICATION_MODAL);
        
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(25, 25, 25, 25));
        gp.setAlignment(Pos.CENTER);
        gp.setVgap(10);
        gp.setHgap(10);
        
        Text title = new Text("Add Banned Playername");
        title.setFont(Font.font("Century Gothic", FontWeight.NORMAL, 20));
        gp.add(title, 0, 0, 2, 1);
        
        Label newusername = new Label("Ban Playername");
        TextField username = new TextField();
        gp.add(newusername,0,1);
        gp.add(username,1,1);
        
        Button finish = new Button("Finish");
        HBox finish_box = new HBox(10);
        finish_box.setAlignment(Pos.CENTER);
        finish_box.getChildren().add(finish);
        
        finish.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                banned_playernames.remove(username.getText());
                banned_playernames.add(username.getText());
                to_update.setItems(FXCollections.observableArrayList(banned_playernames));
                createBannedPlayername.close();
            }
            
        });
        
        gp.add(finish_box, 0, 2,2,1);
        
        Scene sc = new Scene(gp, 300, 175);
        createBannedPlayername.setScene(sc);
        createBannedPlayername.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void save() {
        try {
            PrintWriter ps = new PrintWriter(config, "UTF-8");
            ps.println("{");
            ps.println(" \"accounts\" : {");
            if(users.size() > 0){
               for(int i = 0; i < users.size() - 1; i ++){
                    ps.println("\t\"%username%\" : { \"password\" : \"%password%\" },".replace("%username%", users.get(i)).replace("%password%", userpasswords.get(users.get(i))));
               }
            ps.println("\t\"%username%\" : { \"password\" : \"%password%\" }".replace("%username%", users.get(users.size()-1)).replace("%password%", userpasswords.get(users.get(users.size()-1))));
             
            }
            ps.println(" },");
            ps.print(" \"bannedAccountNames\" : [");
            if(banned_usernames.size() > 0){
                for(int i = 0; i < banned_usernames.size() - 1; i ++){
                    ps.print("\"" + banned_usernames.get(i) + "\", ");
                }
                ps.print("\"" + banned_usernames.get(banned_usernames.size()-1) + "\" ");
            }
        
            ps.println("],");
            ps.print(" \"bannedAddresses\" : [");
            if(banned_ips.size() > 0){
                for(int i = 0; i < banned_ips.size() - 1; i ++){
                    ps.print("\"" + banned_ips.get(i) + "\", ");
                }
                ps.print("\"" + banned_ips.get(banned_ips.size()-1) + "\" ");
            }
            ps.println("],");
            ps.print(" \"bannedPlayerNames\" : [");
            if(banned_playernames.size() > 0){
                for(int i = 0; i < banned_playernames.size() - 1; i ++){
                    ps.print("\"" + banned_playernames.get(i) + "\", ");
                }
                ps.print("\"" + banned_playernames.get(banned_playernames.size()-1) + "\" ");  
            }
            
            ps.println("]");
            ps.println("}");
            ps.close();
        } catch (IOException ex) {
           //This shouldn't happen - We would have crashed earlier when parsing.
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void parseJSON() {
        //Show the File Chooser
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Starbound AccessControl.config File");
        File starbound = fc.showOpenDialog(null);
        config = starbound;
        if(starbound != null){
            try {
                FileInputStream fis = new FileInputStream(starbound); 
                JSONTokener jst = new JSONTokener(fis);
                JSONObject jsob = new JSONObject(jst);
                
                JSONObject accounts = jsob.getJSONObject("accounts");
                JSONArray banned_accounts = jsob.getJSONArray("bannedAccountNames");
                JSONArray banned_addresses = jsob.getJSONArray("bannedAddresses");
                JSONArray banned_players = jsob.getJSONArray("bannedPlayerNames");
                
                Iterator account_keys = accounts.keys();
                while(account_keys.hasNext()){
                    String key = (String) account_keys.next();
                    users.remove(key);
                    users.add(key);
                    userpasswords.put(key, accounts.getJSONObject(key).getString("password"));
                }
                
                for(int i = 0; i < banned_accounts.length(); i ++){
                    banned_usernames.add(banned_accounts.getString(i));
                }
                for(int i = 0; i < banned_addresses.length(); i ++){
                    banned_ips.add(banned_addresses.getString(i));
                }
                for(int i = 0; i < banned_players.length(); i ++){
                    banned_playernames.add(banned_players.getString(i));
                }
                
            } catch (FileNotFoundException ex) {
                //In theory this shouldn't happen.
                System.exit(1);
            } catch (JSONException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        
    }
    
}
