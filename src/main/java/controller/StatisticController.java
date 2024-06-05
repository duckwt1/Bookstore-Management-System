package controller;

import bus.OrderBus;
import bus.UserBus;
import dto.Customer;
import dto.Order;
import dto.OrderDetail;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.hibernate.Hibernate;

import java.text.DateFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticController {

    private UserBus userBus = new UserBus();
    private OrderBus orderBus = new OrderBus();

    private List<Customer> customers;
    private List<OrderDetail> orderDetails;
    private List<Order> orders;
    private List<Order> orders2;

    @FXML
    private TableView<OrderDetail> detailedStatsTable;

    @FXML
    private Label newCustomersLabel;

    @FXML
    private BarChart<String, Number> salesChart;

    @FXML
    private Label title;

    @FXML
    private Label totalCustomersLabel;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Label totalSalesLabel;

    @FXML
    private void initialize() {
        customers = userBus.getAllCustomers();
        orderDetails = orderBus.getAllOrderDetails();
        orders = orderBus.getAllOrders();
        orders2 = orderBus.getAllOrderWithDetails();

        // Ensure Hibernate proxies are initialized
        orders2.forEach(order -> Hibernate.initialize(order.getOrderDetails()));

        loadStatisticsData();
    }

    private void loadStatisticsData() {
        int totalCustomers = getTotalCustomers();
        totalCustomersLabel.setText(String.valueOf(totalCustomers));

        double totalSales = getTotalSales();
        totalSalesLabel.setText("$" + totalSales);

        int totalOrders = getTotalOrders();
        totalOrdersLabel.setText(String.valueOf(totalOrders));

        String newCustomers = getNewCustomers();
        newCustomersLabel.setText(newCustomers);

        loadSalesChart(2024);

        loadDetailedStatsTable();
    }

    private int getTotalCustomers() {
        return (int) customers.stream()
                .filter(customer -> !customer.getName().equals("admin") && !customer.getName().equals("test"))
                .count();
    }

    private double getTotalSales() {
        return orderDetails.stream()
                .mapToDouble(orderDetail -> orderDetail.getQuantity() * orderDetail.getBookID().getPrice().doubleValue())
                .sum();
    }

    private int getTotalOrders() {
        return orders.size();
    }

    private String getNewCustomers() {
        return customers.isEmpty() ? "No new customers" : customers.get(customers.size() - 1).getName();
    }

    private void loadSalesChart(int year) {
        // Tạo một danh sách để lưu trữ thông tin doanh số cho từng tháng
        List<Double> monthlySales = new ArrayList<>(Collections.nCopies(12, 0.0));

        // Duyệt qua danh sách đơn hàng và tính toán tổng doanh số cho từng tháng
        for (Order order : orders2) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(order.getOrderDate());
            int orderYear = cal.get(Calendar.YEAR);
            if (orderYear == year) {
                int month = cal.get(Calendar.MONTH);

                // Tạo một danh sách tạm thời để lưu trữ tất cả các chi tiết đơn hàng trong mỗi đơn hàng
                List<OrderDetail> orderDetailList = new ArrayList<>(order.getOrderDetails());

                // Duyệt qua danh sách tạm thời và tính toán tổng doanh số cho mỗi tháng
                for (OrderDetail orderDetail : orderDetailList) {
                    double sales = orderDetail.getQuantity() * orderDetail.getBookID().getPrice().doubleValue();
                    monthlySales.set(month, monthlySales.get(month) + sales);
                }
            }
        }

        // Tạo series và thêm dữ liệu vào series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales " + year);

        // Thêm dữ liệu từ danh sách doanh số hàng tháng vào series
        for (int i = 0; i < monthlySales.size(); i++) {
            String monthName = new DateFormatSymbols(Locale.getDefault()).getMonths()[i];
            series.getData().add(new XYChart.Data<>(monthName, monthlySales.get(i)));
        }

        // Cập nhật biểu đồ
        salesChart.getData().clear();
        salesChart.getData().add(series);
    }



    private void loadDetailedStatsTable() {
        TableColumn<OrderDetail, String> bookColumn = new TableColumn<>("Book");
        bookColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBookID().getTitle()));

        TableColumn<OrderDetail, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> {
            OrderDetail orderDetail = cellData.getValue();
            Order order = orderDetail.getOrderID();
            int totalQuantity = getTotalQuantity(order);
            return new SimpleIntegerProperty(totalQuantity).asObject();
        });

        TableColumn<OrderDetail, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getBookID().getPrice().doubleValue()).asObject());

        detailedStatsTable.getColumns().setAll(bookColumn, quantityColumn, priceColumn);

        bookColumn.prefWidthProperty().bind(detailedStatsTable.widthProperty().multiply(0.5));
        quantityColumn.prefWidthProperty().bind(detailedStatsTable.widthProperty().multiply(0.2));
        priceColumn.prefWidthProperty().bind(detailedStatsTable.widthProperty().multiply(0.3));

        ObservableList<OrderDetail> orderDetailObservableList = FXCollections.observableArrayList(orderDetails);
        detailedStatsTable.setItems(orderDetailObservableList);
    }


    @FXML
    private void refresh() {
        loadStatisticsData();
    }

    private int getTotalQuantity(Order order) {
        return order.getOrderDetails().stream()
                .mapToInt(OrderDetail::getQuantity)
                .sum();
    }

}
