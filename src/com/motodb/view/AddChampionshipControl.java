/**
 * 'addChampionship.fxml' Control Class
 */

package com.motodb.view;

import org.controlsfx.control.CheckComboBox;

import com.motodb.controller.ChampionshipManager;
import com.motodb.controller.ChampionshipManagerImpl;
import com.motodb.controller.ClassesManager;
import com.motodb.controller.ClassesManagerImpl;
import com.motodb.controller.SponsorManager;
import com.motodb.controller.SponsorManagerImpl;
import com.motodb.model.Championship;
import com.motodb.view.alert.AlertTypes;
import com.motodb.view.alert.AlertTypesImpl;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AddChampionshipControl extends ScreenControl {
	
	
	// Alert panel to manage exceptions
    private final AlertTypes alert = new AlertTypesImpl();
    
    ChampionshipManager manager = new ChampionshipManagerImpl();
    ClassesManager classesManager = new ClassesManagerImpl();
    SponsorManager sponsorManager = new SponsorManagerImpl();
	
    @FXML
	private TableView<Championship> championshipTable;
	@FXML
	private TableColumn<Championship, String> yearColumn, editionColumn, sponsorsColumn, classesColumn;
	@FXML
	private TextField yearField, editionField, searchField;
	@FXML
    private CheckComboBox<String> classesField, sponsorsField;
	@FXML
	private Button addSponsor;
    @FXML
    private VBox vBoxFields;
    
	public AddChampionshipControl(){
		
	}
	    
    /**
     * Called after the fxml file has been loaded; this method initializes 
     * the fxml control class. 
     */
    public void initialize() {
    	classesField=new CheckComboBox<String>(classesManager.getClassesNames());
    	vBoxFields.getChildren().add(vBoxFields.getChildren().size()-3, classesField);
    	
    	sponsorsField=new CheckComboBox<String>(sponsorManager.getSponsor());
    	vBoxFields.getChildren().add(vBoxFields.getChildren().size()-2, sponsorsField);
        
    	// Initialize the table
    	yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asString());
    	editionColumn.setCellValueFactory(cellData -> cellData.getValue().editionProperty().asString());
    	classesColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
    			
    		manager.getClassesStrings(cellData.getValue().getYear()).toString()
    	));
    	
    	sponsorsColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
    		manager.getSponsorStrings(cellData.getValue().getYear()).toString()  
        ));
    	
        // Add observable list data to the table
        championshipTable.setItems(manager.showChampionship());
        
        // Make the table columns editable by double clicking
        this.edit();
        // Use a 'searchField' to search for books in the tableView
        this.search();
        // Listen for selection changes and enable delete button
        this.update();
    }
    
    /**
     * Called when the user press the 'add' button; this method adds
     * a new depot to the controller ObservableList of depots
     */
	@FXML
    private void add() {
        try {
        	manager.insertChampionship(Integer.parseInt(yearField.getText()),Integer.parseInt(editionField.getText()),
        			classesField.getCheckModel().getCheckedItems(),sponsorsField.getCheckModel().getCheckedItems());
        	championshipTable.setItems(manager.showChampionship());
        	this.clear();
        } catch (Exception e) {
            alert.showWarning(e);
        }
    }
	
	/**
     * Called when the user edit a depot name directly from the tableColumn;
     * This method edits the selected field in the observableList of depots and 
     * makes fields editable directly from the table
     */
	private void edit() {
		
	}
	
	/**
     * Called on delete button press, opens a confirmation dialog asking if you 
     * really want to delete the element; this method is called 
     * to delete the selected element from the observableList
     */
    @FXML
    private void delete() {
        
    }
    
    /**
     * Called when the user enter something in the search field;
     * It search name of the depot
     */
    private void search(){
    	
    }
    
    /**
	 * It listen for selection changes to disable/enable the delete button 
	 * when the user selects something in the table
	 */
	private void update(){
        
	}

}
