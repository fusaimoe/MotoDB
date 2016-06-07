package com.motodb.view;

import com.motodb.controller.ChampionshipManager;
import com.motodb.controller.ChampionshipManagerImpl;
import com.motodb.model.Championship;
import com.motodb.view.alert.AlertTypes;
import com.motodb.view.alert.AlertTypesImpl;
import com.motodb.view.util.PersistentButtonToggleGroup;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class WorldStandingControl extends ScreenControl {

    // Alert panel to manage exceptions
    private final AlertTypes alert = new AlertTypesImpl();
    
    // Controller
    private final ChampionshipManager manager = new ChampionshipManagerImpl();

    // ToggleGroup to have just one toggleButton selected at a time
    private final ToggleGroup yearsButtons = new PersistentButtonToggleGroup();
    private final ToggleGroup classesButtons = new PersistentButtonToggleGroup();
    
    @FXML
    private HBox years,classes;
    @FXML
    private TextField searchField;

    public WorldStandingControl() {
        super();

        for (Championship c : manager.getChampionships()) {
        	ToggleButton button = new ToggleButton(Integer.toString(c.getYear()));
            button.setToggleGroup(yearsButtons);
            button.setUserData(c.getYear());
        }

        if (!yearsButtons.getToggles().isEmpty()) {
            yearsButtons.getToggles().get(0).setSelected(true);
            
            // Creating a button for each class of that year, and adding such button to the group
            for (String s : manager.getClassesStrings((Integer.parseInt(yearsButtons.getSelectedToggle().getUserData().toString())))) {
                ToggleButton button = new ToggleButton(s);
                button.setToggleGroup(classesButtons);
                button.setUserData(s);
            }
        }
    }

    /**
     * Called after the fxml file has been loaded; this method initializes the
     * fxml control class.
     */
    public void initialize() {

        // Adding the buttons created in the constructor to the hBox, before the button
        for (Toggle button : yearsButtons.getToggles()) {
            years.getChildren().add(yearsButtons.getToggles().indexOf(button), (ToggleButton)button);
        }
        for (Toggle button : classesButtons.getToggles()) {
            button.setToggleGroup(classesButtons);
            classes.getChildren().add(classesButtons.getToggles().indexOf(button), (ToggleButton)button);
        }

        // Method which handles the selection of a year
        this.filter();

        if (!classesButtons.getToggles().isEmpty()) {
        	classesButtons.getToggles().get(0).setSelected(true);
        }
    }

    /**
     * Called when the user selects a toggle-button to filter classes so
     * there are only buttons for each class of that year
     */
    private void filter() {
    	
        yearsButtons.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
            	
                classesButtons.getToggles().clear();
                classes.getChildren().clear();

                // THIS CODE IS COPIED FROM TOP, IT NEEDS TO BE REFACTORED
                for (String s : manager.getClassesStrings((Integer.parseInt(yearsButtons.getSelectedToggle().getUserData().toString())))) {
                    ToggleButton button = new ToggleButton(s);
                    button.setToggleGroup(classesButtons);
                    button.setUserData(s);
                }
                // THIS CODE IS COPIED FROM TOP, IT NEEDS TO BE REFACTORED
                for (Toggle button : classesButtons.getToggles()) {
                    button.setToggleGroup(classesButtons);
                    classes.getChildren().add(classesButtons.getToggles().indexOf(button), (ToggleButton)button);
                } 
            }
        });
    }
}
