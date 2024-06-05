package controller;

import bus.BookBus;
import bus.OrderBus;
import bus.UserBus;
import dto.Book;
import dto.Customer;
import dto.OrderDetail;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static controller.DashBoardController.selectedTable;

public class ManagementController {

    public static Customer selectedCustomer = null;
    public static Book selectedBook = null;
    public static boolean check = false;

    private List<Customer> customers = new UserBus().getAllCustomers();
    private List<Book> books = new BookBus().getAllBooks();
    private List<OrderDetail> orderDetails = new OrderBus().getAllOrderDetails();
//    private List<Order> orders = new OrderBus().getAllOrders();

    @FXML
    private TableView<Object> tableview;

    @FXML
    private TextField searchField;

    @FXML
    private Label title;

    @FXML
    private Label title2;

    @FXML
    void initialize() {
        if (selectedTable.equals("customer")) {
            loadCustomerTable();
        } else if (selectedTable.equals("book")) {
            title.setText("Book Management");
            title.setStyle("-fx-font-size: 24px; -fx-font-family: 'Berlin Sans FB'; -fx-font-weight: bold;");

            title2.setText("LIST OF BOOKS");
            title2.setStyle("-fx-font-size: 18px; -fx-font-family: 'Berlin Sans FB';");

            loadBookTable();
        } else if (selectedTable.equals("order")) {
            title.setText("Order Management");
            title.setStyle("-fx-font-size: 24px; -fx-font-family: 'Berlin Sans FB'; -fx-font-weight: bold;");

            title2.setText("LIST OF ORDERS");
            title2.setStyle("-fx-font-size: 18px; -fx-font-family: 'Berlin Sans FB';");

            loadOrderDetailTable();
        }

        setupSearchFunctionality();
        System.out.println("Table loaded: " + selectedTable);
    }

    @FXML
    void add(ActionEvent event) {
        if (selectedTable.equals("customer")) {
            check = true;
            try {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/addCustomer.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.setTitle("Add Customer");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (selectedTable.equals("book")) {
            try {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/addBook.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.setTitle("Add Book");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadCustomerTable() {
        tableview.getColumns().clear();
        setupCustomerTable();

        tableview.getItems().addAll(customers);
    }

    private void loadBookTable() {
        tableview.getColumns().clear();
        setupBookTable();

        tableview.getItems().addAll(books);
    }

    private void loadOrderDetailTable() {
        tableview.getColumns().clear();
        setupOrderDetailTable();

        tableview.getItems().addAll(orderDetails);
    }

    private void setupCustomerTable() {
        TableColumn<Object, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Object, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Object, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Object, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(createButtonCellFactory());

        idColumn.prefWidthProperty().bind(tableview.widthProperty().multiply(0.2));
        nameColumn.prefWidthProperty().bind(tableview.widthProperty().multiply(0.3));
        emailColumn.prefWidthProperty().bind(tableview.widthProperty().multiply(0.3));
        actionColumn.prefWidthProperty().bind(tableview.widthProperty().multiply(0.2));

        tableview.getColumns().addAll(idColumn, nameColumn, emailColumn, actionColumn);
    }

    private void setupBookTable() {
        TableColumn<Object, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Object, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Object, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Object, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Object, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(createButtonCellFactory());

        idColumn.prefWidthProperty().bind(tableview.widthProperty().multiply(0.2));
        titleColumn.prefWidthProperty().bind(tableview.widthProperty().multiply(0.35));
        authorColumn.prefWidthProperty().bind(tableview.widthProperty().multiply(0.2));
        actionColumn.prefWidthProperty().bind(tableview.widthProperty().multiply(0.25));

        tableview.getColumns().addAll(idColumn, titleColumn, authorColumn, priceColumn, actionColumn);
    }

    private void setupOrderDetailTable() {

        TableColumn<Object, Integer> orderIdColumn = new TableColumn<>("Order ID");
        orderIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getClass().equals(OrderDetail.class) ? ((OrderDetail) cellData.getValue()).getOrderID().getId() : 0));

        TableColumn<Object, String> bookIdColumn = new TableColumn<>("Book ID");
        bookIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getClass().equals(OrderDetail.class) ? ((OrderDetail) cellData.getValue()).getBookID().getTitle() : ""));

        TableColumn<Object, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Object, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(createButtonCellFactory());

        orderIdColumn.prefWidthProperty().bind(tableview.widthProperty().multiply(0.2));
        bookIdColumn.prefWidthProperty().bind(tableview.widthProperty().multiply(0.4));
        quantityColumn.prefWidthProperty().bind(tableview.widthProperty().multiply(0.1));
        actionColumn.prefWidthProperty().bind(tableview.widthProperty().multiply(0.3));

        tableview.getColumns().addAll(orderIdColumn, bookIdColumn, quantityColumn, actionColumn);
    }

    private Callback<TableColumn<Object, Void>, TableCell<Object, Void>> createButtonCellFactory() {
        return new Callback<TableColumn<Object, Void>, TableCell<Object, Void>>() {
            @Override
            public TableCell<Object, Void> call(final TableColumn<Object, Void> param) {
                return new TableCell<Object, Void>() {

                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    private final Button printButton = new Button("Print");
                    private final HBox buttons = new HBox();

                    {
                        if (selectedTable.equals("customer") || selectedTable.equals("book")) {
                            editButton.setStyle("-fx-background-color: yellow;");
                            editButton.setOnAction((ActionEvent event) -> {
                                if (selectedTable.equals("customer")) {
                                    selectedCustomer = (Customer) getTableView().getItems().get(getIndex());
                                    editCustomer();
                                } else if (selectedTable.equals("book")) {
                                    System.out.println("Edit Book");
                                    selectedBook = (Book) getTableView().getItems().get(getIndex());
                                    editBook();
                                }
                            });

                            deleteButton.setStyle("-fx-background-color: red;");
                            deleteButton.setOnAction((ActionEvent event) -> {
                                if (selectedTable.equals("customer")) {
                                    Customer data = (Customer) getTableView().getItems().get(getIndex());
                                    new UserBus().deleteCustomer(data.getId());
                                    tableview.getItems().remove(data);
                                } else if (selectedTable.equals("book")) {
                                    Book data = (Book) getTableView().getItems().get(getIndex());
                                    // Assuming BookBus has a deleteBook method
//                                    new BookBus().deleteBook(data.getId());
                                    tableview.getItems().remove(data);
                                }
                            });

                            buttons.getChildren().addAll(editButton, deleteButton);
                        } else if (selectedTable.equals("order")) {
                            printButton.setStyle("-fx-background-color: green;");
                            printButton.setOnAction((ActionEvent event) -> {
                                OrderDetail orderDetail = (OrderDetail) getTableView().getItems().get(getIndex());
                                System.out.println("Print Order: " + orderDetail.getOrderID().getId());
                                printOrderDetailToFile(orderDetail.getOrderID().getId(), orderDetail.getBookID().getTitle(), orderDetail.getQuantity(), orderDetail.getBookID().getPrice().doubleValue(), "order" + orderDetail.getOrderID().getId() + ".txt");
                            });

                            buttons.getChildren().add(printButton);
                        }

                        buttons.setSpacing(10);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttons);
                        }
                    }
                };
            }
        };
    }

    public void printOrderDetailToFile(int orderId, String bookTitle, int quantity, double price, String filePath) {
        try (PrintStream out = new PrintStream(new FileOutputStream(filePath, true))) {

            double total = 0;

            out.println("Order Detail");
            out.println();
            out.println("============");
            out.println();

            for (OrderDetail orderDetail : orderDetails) {
                if (orderDetail.getOrderID().getId() == orderId) {
                    out.println("Order ID: " + orderId);
                    out.println("Book Title: " + bookTitle);
                    out.println("Price: " + price);
                    out.println("Quantity: " + quantity);
                    out.println();

                    total += price * quantity;
                }
            }

            out.println("Total: " + total);
            out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupSearchFunctionality() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTableItems(newValue);
        });
    }

    private void filterTableItems(String searchText) {
        if (selectedTable.equals("customer")) {
            filterCustomerTable(searchText);
        } else if (selectedTable.equals("book")) {
            filterBookTable(searchText);
        } else if (selectedTable.equals("order")) {
            filterOrderDetailTable(searchText);
        }
    }

    private void filterCustomerTable(String searchText) {
        List<Customer> filteredCustomers = customers.stream()
                .filter(customer -> customer.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                        customer.getEmail().toLowerCase().contains(searchText.toLowerCase()))
                .toList();
        tableview.getItems().setAll(filteredCustomers);
    }

    private void filterBookTable(String searchText) {
        List<Book> filteredBooks = books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(searchText.toLowerCase()))
                .toList();
        tableview.getItems().setAll(filteredBooks);
    }

    private void filterOrderDetailTable(String searchText) {
        List<OrderDetail> filteredOrderDetails = orderDetails.stream()
                .filter(orderDetail -> Integer.toString(orderDetail.getOrderID().getId()).contains(searchText) ||
                        orderDetail.getBookID().getTitle().toLowerCase().contains(searchText.toLowerCase()))
                .toList();
        tableview.getItems().setAll(filteredOrderDetails);
    }

    private void editCustomer() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/editCustomer.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setTitle("Edit Customer");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editBook() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/java2final/editBook.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setTitle("Edit Book");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
