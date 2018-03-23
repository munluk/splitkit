package com.splitkit.splitkit;
import java.lang.invoke.MethodHandles;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.splitkit.exception.SplitException;
import com.splitkit.exception.UserInputException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;


/**
 *
 * @author Lukas Munteanu
 */
public class App extends Application {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getName());
    
    final private TableView<Person> table = new TableView<Person>();
    final private Logic logic = new Logic();
    final private ObservableList<Person> peopleList = FXCollections.observableArrayList();
    final private Image removeImage = new Image(getClass().getResourceAsStream("/images/removebutton.png"), 16, 16, true, false);

    @SuppressWarnings("unchecked")
	@Override
    public void start(Stage primaryStage) {
    	addTestPeople();
        Scene scene = new Scene(new Group());
        final Label label = new Label("People");
        label.setFont(new Font("Arial", 20));
        
        // ==================== Table View ====================
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY );
        TableColumn<Person, String> nameCol = new TableColumn<>("Name");
        nameCol.setStyle( "-fx-alignment: CENTER;");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Person, String>>() {
                @Override
                public void handle(CellEditEvent<Person, String> t) {
                    (t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setName(t.getNewValue());
                }
            }
        );
        
        TableColumn<Person, Float> expensesCol = new TableColumn<>("Expenses");
        expensesCol.setStyle( "-fx-alignment: CENTER;");
        expensesCol.setCellValueFactory(
                new PropertyValueFactory<Person, Float>("expenses"));
        expensesCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Float>() {
            @Override
            public Float fromString(String string) {
                return Float.parseFloat(string);
            }

            @Override
            public String toString(Float object) {
                return Float.toString(object);
            }
        }));
        expensesCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Person, Float>>() {
                @Override
                public void handle(CellEditEvent<Person, Float> t) {
                    ((Person) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setExpenses(t.getNewValue());
                }
            }
        );
        
        TableColumn<Person, Float> shareCol = new TableColumn<>("% of total");
        shareCol.setStyle( "-fx-alignment: CENTER;");
        shareCol.setCellValueFactory(
                new PropertyValueFactory<Person, Float>("sip"));
        shareCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Float>() {
            @Override
            public Float fromString(String string) {
                if(string.isEmpty()){
                    return 0.0f;
                }
                float num = Float.parseFloat(string);
                return Float.parseFloat(String.format(Locale.ROOT, "%.2f", num));
            }

            @Override
            public String toString(Float object) {
                if(object.floatValue() == 0.0f){
                    return "";
                }else{
                    return String.format(Locale.ROOT, "%.2f", object.floatValue());
                }
            }
        }));
        
        shareCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Person, Float>>() {
                @Override
                public void handle(CellEditEvent<Person, Float> t) {
                    ((Person) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setSip(t.getNewValue());
                }
            }
        );

        TableColumn<Person, String> removeCol = new TableColumn<>("");
        removeCol.setStyle( "-fx-alignment: CENTER;");
        removeCol.setPrefWidth(40);
        Callback<TableColumn<Person, String>, TableCell<Person, String>> removeColCellFactory
                = new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
            @Override
            public TableCell<Person, String> call(final TableColumn<Person, String> param) {
                final TableCell<Person, String> cell = new TableCell<Person, String>() {
                    
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                        	ImageButton btn = new ImageButton(removeImage);
                            btn.setOnAction(event -> {
                                Person person = getTableView().getItems().get(getIndex());
                                peopleList.remove(person);
                                logger.debug("Removing %s", person.getName());
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        };
        removeCol.setCellFactory(removeColCellFactory);
        table.setItems(this.peopleList);
       
        table.getColumns().addAll(nameCol, expensesCol, shareCol, removeCol);
        
        // ==================== Text Fields ====================
        TextField nameTextField = new TextField();
        TextField expenseTextField = new TextField();
        TextField shareTextField = new TextField();
        nameTextField.setMaxWidth(nameCol.getPrefWidth());
        expenseTextField.setMaxWidth(expensesCol.getPrefWidth());
        shareTextField.setMaxWidth(expensesCol.getPrefWidth());
        nameTextField.setPromptText("Name");
        expenseTextField.setPromptText("Expense");
        shareTextField.setPromptText("Share");
        
        // optional-text below the share textfield
        final Text optionalText = new Text();
        optionalText.setId("optional-text");
        optionalText.setText("  (Optional)");
        final VBox vbShareTextField = new VBox(10);
        vbShareTextField.setSpacing(3);
        vbShareTextField.setAlignment(Pos.TOP_LEFT);
        vbShareTextField.setPadding(new Insets(0, 0, 0, 0));
        vbShareTextField.getChildren().addAll(shareTextField, optionalText);
        
        // ==================== Buttons ====================
        // Define the add-button and split-button
        Button addBtn = new Button("Add");
        addBtn.setId("Add-button");
        Button splitEvenBtn = new Button(String.format("Split Even"));
        addBtn.setId("Split-button");
        Button splitBtn = new Button("Split");
        
        // Button Layout -- put add and split button into hbox
        final HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.TOP_LEFT);
        hbBtn.getChildren().add(nameTextField);
        hbBtn.getChildren().add(expenseTextField);
        hbBtn.getChildren().add(vbShareTextField);
        hbBtn.getChildren().add(addBtn);
        
        final HBox hbSplitBtn = new HBox(10);
        hbSplitBtn.setAlignment(Pos.TOP_RIGHT);
        hbSplitBtn.getChildren().add(splitEvenBtn);
        hbSplitBtn.getChildren().add(splitBtn);
        hbBtn.getChildren().add(hbSplitBtn);
        HBox.setHgrow(hbSplitBtn, Priority.ALWAYS);
        
        
        // ==================== Output ====================
        // scroll pane for the output text of the split calculation
        ScrollPane outputPane = new ScrollPane();
        outputPane.setPrefSize(0, 0);
        outputPane.setId("output-pane");
        outputPane.setPrefSize(table.getWidth(), 120);
        // output text
        final Text outputText = new Text();
        outputText.setId("output-text");
        outputPane.setContent(outputText);
        
        // ==================== User Message ====================
        // Text message for the user
        final HBox hbMessage = new HBox(10);
        hbMessage.setAlignment(Pos.BOTTOM_LEFT);
        final Text userMessage = new Text();
        userMessage.setId("message-text");
        hbMessage.getChildren().add(userMessage);
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table, hbBtn, hbMessage, outputPane);
        
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        
        // ==================== Actions ====================
        // action for add-button
        addBtn.setOnAction(event->{
            try{
                // Do some checks if person with selected features can be added
                String shareTextFieldString = shareTextField.getText();
                String name = nameTextField.getText();
                if(shareTextFieldString.isEmpty()){
                    shareTextFieldString = "0.0";
                }
                if (name.equals("")){
                    throw new UserInputException("Empty name field!");
                }
                if(Person.getPerson(name, peopleList) != null) {
                	throw new UserInputException("Person already in List!");
                }else {
                	Person person2add = new Person(
                            name,
                            Float.parseFloat(shareTextFieldString));
                    person2add.addExpense(Float.parseFloat(expenseTextField.getText()));
	                this.peopleList.add(person2add);
	                logger.debug("Number of people in list: %s", this.peopleList.size());
                }
            }
            catch(java.lang.NumberFormatException e){userMessage.setText("Empty amount or wrong numbers!");}
            catch(java.lang.Exception e){userMessage.setText(e.getMessage());
            }
            table.refresh();
            nameTextField.setText("");
            expenseTextField.setText("");
            shareTextField.setText("");
        });
        
        // split-even button
        splitEvenBtn.setOnAction(event->{
            userMessage.setText("");
            try{
            	SplitDTO splitDTO = logic.computeSplit(peopleList, 1);
                outputText.setText(splitDTO.asString());
            }catch(SplitException e){
                userMessage.setText(e.getMessage());
            }
        });
        
        // split button
        splitBtn.setOnAction(event->{
            userMessage.setText("");
            try{
            	SplitDTO splitDTO = logic.computeSplit(peopleList, 0);
                outputText.setText(splitDTO.asString());
            }catch(SplitException e){
                userMessage.setText(e.getMessage());
            }
        });
        
        // add style sheets
        scene.getStylesheets().add(App.class.getResource("/App.css").toExternalForm());
        
        // ==================== Stage ====================
        primaryStage.sizeToScene();
        primaryStage.setTitle("SplitKit");
        primaryStage.setResizable(false);
        primaryStage.setTitle("SplitKit");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void addTestPeople() {
    	Person personA = new Person("A", 20.0f);
	    personA.addExpense(10.0f);
	    peopleList.add(personA);
	    Person personB = new Person("B", 25.0f);
	    personB.addExpense(12.0f);
	    peopleList.add(personB);
	    Person personC = new Person("C", 25.0f);
	    personC.addExpense(15.0f);
	    peopleList.add(personC);
	    Person personD = new Person("D", 10.0f);
	    personD.addExpense(28.0f);
	    peopleList.add(personD);
	    Person personE = new Person("E", 20.0f);
	    personE.addExpense(35.0f);
	    peopleList.add(personE);
    }
}

