/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todolistgradedexample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author shree
 */
public class ToDoListGradedExample extends Application implements Serializable {

    /**
     * @param args the command line arguments
     */
    private Item items;
    private SimpleDateFormat fr = new SimpleDateFormat("MM-dd-yyyy");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private ObservableList<Item> toDoList = FXCollections.observableArrayList();

    public static void main(String[] args) {
        // TODO code application logic here

        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ToggleGroup group = new ToggleGroup();
        RadioButton byLabel = new RadioButton("By Label");
        RadioButton byDeadLine = new RadioButton("By DeadLine");
        ListView<Item> list = new ListView<>();

        readFromFile();
        list.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {

            @Override
            public ListCell<Item> call(ListView<Item> param) {
                //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                return new MarkRed();
            }
        });

        list.setItems(toDoList);
        list.setPrefWidth(400);
        HBox mainPane = new HBox(5);

        VBox vb1 = new VBox(10);
        VBox vb2 = new VBox(10);

        HBox hb1 = new HBox(5);
        HBox hb2 = new HBox(5);
        Label task = new Label("Task");
        TextField tf1 = new TextField("");
        tf1.setPrefSize(195, 10);
        Label dl = new Label("DeadLine");
        DatePicker deadLine = new DatePicker();
        hb1.setSpacing(32.5);
        hb1.getChildren().addAll(task, tf1);

        hb2.getChildren().addAll(dl, deadLine);

        Button add = new Button("Add");
        //add.setMaxSize(100, 150);
        vb2.getChildren().addAll(hb1, hb2, add);

        vb2.setStyle("-fx-padding: 10;"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;"
                + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: black;");
        VBox vb3 = new VBox(10);

        HBox hb3 = new HBox(5);
        HBox hb4 = new HBox(5);
        Button sd = new Button("Started");
        sd.setPrefSize(85, 10);
        //sd.setMaxSize(100, 150);
        DatePicker started = new DatePicker();
        //hb3.setSpacing(27);
        hb3.getChildren().addAll(sd, started);
        Button con = new Button("Completed");
        con.setMaxSize(100, 150);
        DatePicker completed = new DatePicker();
        hb4.getChildren().addAll(con, completed);

        vb3.getChildren().addAll(hb3, hb4);

        Button remove = new Button("Remove Completed Items");

        Label sort = new Label("Sort");

        byLabel.setToggleGroup(group);
        byDeadLine.setToggleGroup(group);

        vb1.getChildren().addAll(vb2, vb3, remove, sort, byLabel, byDeadLine);

        add.setOnAction(e -> {
            addAction(tf1, deadLine, list);
            list.refresh();
            sortAccordingly(group, byDeadLine);
        });

        sd.setOnAction(e -> {
            try {
                startedAction(started, list);
                sortAccordingly(group, byDeadLine);
                list.refresh();
            } catch (ParseException ex) {
                Logger.getLogger(ToDoListGradedExample.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        con.setOnAction(e -> {
            try {
                completedAction(completed, list);
                sortAccordingly(group, byDeadLine);
                list.refresh();
            } catch (ParseException ex) {
                Logger.getLogger(ToDoListGradedExample.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        remove.setOnAction(e -> {
            removeAction(list);
            sortAccordingly(group, byDeadLine);
            list.refresh();
        });

        byLabel.setOnAction(e -> {
            sortByLabel();
        });

        byDeadLine.setOnAction(e -> {
            sortByDeadLine();
        });
        mainPane.getChildren().addAll(list, vb1);

        Scene scene = new Scene(mainPane, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("To Do List");

        primaryStage.setOnCloseRequest(e -> {
            writeToFile();
        });

        primaryStage.show();

    }

    public void addAction(TextField tf1, DatePicker deadLine, ListView<Item> list) {
        try {
            if (!(tf1.getText().isEmpty() || deadLine.getValue() == null)) {
                String date = dtf.format(deadLine.getValue());
                if (!(toDoList.contains(new Item(tf1.getText(), fr.parse(date))))) {
                    toDoList.add(new Item(tf1.getText(), fr.parse(date)));
                } else {
                    displayAlerts("Add Error Dialogue Box", "Task  with same deadline already Existing", "Please add a different task", list);
                    tf1.setText("");
                    deadLine.setValue(null);
                }
            } else if ((tf1.getText().isEmpty() && deadLine.getValue() == null)) {
                displayAlerts("Add Error Dialogue Box", "Invalid Input", "Please fill in task and deadline details", list);
            } else if (tf1.getText().isEmpty()) {
                displayAlerts("Add Error Dialogue Box", "Invalid Task", "Please fill in task", list);
                deadLine.setValue(null);
            } else {
                displayAlerts("Add Error Dialogue Box", "Invalid deadline", "Please fill in deadline details", list);
                tf1.setText("");
            }
        } catch (ParseException ex) {
            Logger.getLogger(ToDoListGradedExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception exc) {
            System.out.println("here");
        }

    }

    public void writeToFile() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("List.bin")));
            oos.writeObject(new ArrayList<>(toDoList));
            oos.close();
        } catch (IOException ex) {

        }
    }

    public void readFromFile() {
        File file = new File("List.bin");

        if (file.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                ArrayList<Item> list1 = ((ArrayList<Item>) ois.readObject());
                toDoList = FXCollections.observableArrayList(list1);
                ois.close();
            } catch (FileNotFoundException fe) {
                System.out.println("FE");
            } catch (IOException ie) {
                System.out.println("ie");
            } catch (ClassNotFoundException ce) {
                System.out.println("ce");
            }
        }

    }

    public void startedAction(DatePicker started, ListView<Item> list) throws ParseException {
        if (started.getValue() == null) {
            displayAlerts("Error Dialogue Box", "Invalid complete date", "Please fill in complete date details", list);
        } else {
            Item check = list.getSelectionModel().getSelectedItem();
            if (check == null) {
                displayAlerts("task Selection Error", "No task selected", "Please select a taskfrom the list", list);
                started.setValue(null);
            } else {
                for (Item tempItem : toDoList) {
                    if (tempItem.equals(check)) {
                        if (!(tempItem.isInProgress() || tempItem.isIsCompleted())) {
                            tempItem.setInProgress(true);
                            //tempItem.setIsCompleted(false);
                            tempItem.setStartDate(fr.format(fr.parse(dtf.format(started.getValue()))));
                            list.refresh();
                            if (check.startDateComparator(fr.format(fr.parse(dtf.format(started.getValue())))) < 0) {
                                displayAlerts("Date selection Error", "The start date is after the deadline", "Please make sure you start your work on time", list);
                            }

                        } else {
                            //Alert
                            displayAlerts("Task selection Error", "The task selected is already finished", "Please select an unfinished task", list);
                            started.setValue(null);
                        }
                    }
                }
            }
        }

    }

    public void completedAction(DatePicker completed, ListView<Item> list) throws ParseException {

        if (completed.getValue() == null) {
            displayAlerts("Error Dialogue Box", "Invalid complete date", "Please fill in complete date details", list);
        } else {
            Item check = list.getSelectionModel().getSelectedItem();
            if (check == null) {
                displayAlerts("Task Selection Error", "No task selected", "Please select a task from the list", list);
                completed.setValue(null);
            } else {

                for (Item tempItem : toDoList) {
                    if (tempItem.equals(check)) {
                        if ((tempItem.isInProgress() == true) && (tempItem.isIsCompleted() == false)) {

                            if (check.completeDateComparator(fr.format(fr.parse(dtf.format(completed.getValue())))) < 0) {
                                displayAlerts("Date selection Error", "The complete date is before the start date", "Please select a valid date", list);
                                tempItem.setIsCompleted(false);
                                tempItem.setInProgress(true);
                            } else {
                                if (check.startDateComparator(fr.format(fr.parse(dtf.format(completed.getValue())))) < 0) {
                                    System.out.println("Overdue");
                                    displayAlerts("Delayed", "The task is finished after the deadline", "Make sure you finish your work on time", list);

                                    tempItem.setCompleteDate(fr.format(fr.parse(dtf.format(completed.getValue()))));
                                    tempItem.setIsCompleted(true);
                                    tempItem.setInProgress(false);
                                    list.refresh();
                                } else {
                                    tempItem.setCompleteDate(fr.format(fr.parse(dtf.format(completed.getValue()))));
                                    list.refresh();
                                    tempItem.setIsCompleted(true);
                                    tempItem.setInProgress(false);
                                }
                            }
                        } else {
                            displayAlerts("Task selection Error", "The task selected is not yet started", "Please select an started task to finish it.", list);
                            completed.setValue(null);
                        }
                    }
                }

            }

        }
    }

    public void removeAction(ListView<Item> list) {
        ArrayList<Item> removeItems = new ArrayList<>();
        for (Item tempItem : toDoList) {
            if (tempItem.isIsCompleted()) {
                removeItems.add(tempItem);
            }
        }
        toDoList.removeAll(removeItems);
    }

    public void sortByLabel() {
        Collections.sort(toDoList);
    }

    public void sortByDeadLine() {
        Collections.sort(toDoList, new ItemComparator());
    }

    public void sortAccordingly(ToggleGroup group, RadioButton byDeadLine) {
        if (group.getSelectedToggle() == byDeadLine) {
            sortByLabel();
            sortByDeadLine();
        } else {
            sortByLabel();
        }
    }

    public void displayAlerts(String title, String header, String content, ListView<Item> list) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get()
                == ButtonType.OK) {
            list.refresh();
        } else {
            list.refresh();
        }

    }
}

class MarkRed extends ListCell<Item> {

    @Override
    protected void updateItem(Item item, boolean empty) throws NullPointerException {

        super.updateItem(item, empty);

        if (item == null) {
            setText("");
        } else {
            setText(item.toString());
            if ((item.isInProgress() == true) && (item.isIsCompleted() == false)) {
                if (item.startDateComparator(item.getStartDate()) < 0) {
                    setTextFill(Color.RED);
                } else {
                    setTextFill(Color.BLACK);
                }
            } else if ((item.isInProgress() == false) && (item.isIsCompleted() == true)) {
                if (item.startDateComparator(item.getCompleteDate()) < 0) {
                    setTextFill(Color.RED);
                } else {
                    setTextFill(Color.GREEN);
                }
            } else {
                setTextFill(Color.BLACK);
            }
        }

    }
}
