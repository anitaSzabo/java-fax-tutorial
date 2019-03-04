package application;

import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.Group;
 
public class PieChartSample extends Application {
 
	 private double dragStartX, dragStartY;
	 private GridPane grid = new GridPane();
	 private PieChart pieChart;
	 private LineChart lineChart;
	 private LineChart lineChart2;
	 private AreaChart areaChart;
	
    @Override public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        scene.getStylesheets().add("style/stylesheet.css");
        stage.setTitle("Charts");
        stage.setWidth(1500);
        stage.setHeight(1000);
        
        pieChart = createPieChart();
        setDrillDown(pieChart);
        
        lineChart = createLineChart();
        lineChart2 = createLineChart();
        
        areaChart = createAreaChart();
        
        lineChart.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start a drag-and-drop gesture*/
                /* allow any transfer mode */
            	dragStartX = event.getSceneX();
                dragStartY = event.getSceneY();
                Dragboard db = lineChart.startDragAndDrop(TransferMode.ANY);
                
                /* Put a string on a dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putString(lineChart.getTitle());
                db.setContent(content);
                
                event.consume();
            }
        });
        
        pieChart.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                /* accept it only if it is not dragged from the same node 
                 * and if it has a string data */
                if (event.getGestureSource() != pieChart &&
                        event.getDragboard().hasString()) {
                    /* allow for moving */
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                
                event.consume();
            }
        });
        
        pieChart.setOnDragEntered(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
            /* the drag-and-drop gesture entered the target */
            /* show to the user that it is an actual gesture target */
                 if (event.getGestureSource() != pieChart &&
                         event.getDragboard().hasString()) {
                     //pieChart.setFill(Color.GREEN);
                 }
                        
                 event.consume();
            }
        });
        
        pieChart.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                //pieChart.setFill(Color.BLACK);

                event.consume();
            }
        });
        
        pieChart.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                   //pieChart.setText(db.getString());
                   success = true;
                   pieChart.setTitle("new title");
                   grid.getChildren().remove(0);
                   grid.add(lineChart, 0, 0);
                   //grid.add(pieChart, 0, 1);
                }
                /* let the source know whether the string was successfully 
                 * transferred and used */
                //MouseUtil.slideToDest(Arrays.asList(lineChart), pieChart);
                event.setDropCompleted(success);
                System.out.println("drag dropped");
                
                event.consume();
             }
        });
        
        lineChart.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag and drop gesture ended */
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    //lineChart.setText("");
                }
                System.out.println("drag done");
                event.consume();
            }
        });
        
        
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(100);
        grid.setHgap(100);
        
        //grid.setGridLinesVisible(true);
        grid.add(pieChart, 0, 0);
        grid.add(lineChart, 0, 1);
        grid.add(areaChart, 1, 0);
        grid.add(lineChart2, 0, 2);
        
        ((Group) scene.getRoot()).getChildren().add(grid);

        stage.setScene(scene);
        
        ScrollBar s1 = new ScrollBar();
        s1.setMax(500);
        s1.setMin(0);
        s1.setValue(100);
        s1.setUnitIncrement(30);
        s1.setBlockIncrement(35);
        
        s1.setOrientation(Orientation.VERTICAL);
        
        ((Group) scene.getRoot()).getChildren().add(s1);
        stage.show();
        
        
    }
    
    private LineChart createLineChart() {
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));
        
        lineChart.getData().add(series);
        
        return lineChart;
	}

	private void setDrillDown(PieChart chart) {
    	for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                    	System.out.println(String.valueOf(data.getPieValue()) + "%");
                    	Alert alert = new Alert(AlertType.INFORMATION);
                    	alert.setTitle("Details");
                    	alert.setContentText(String.valueOf(data.getPieValue()) + "% " + data.getName());
                    	alert.setHeaderText("");
                    	alert.showAndWait();
                     }
                });
        }
    }
    
    private PieChart createPieChart() {
    	ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Grapefruit", 13),
                new PieChart.Data("Oranges", 25),
                new PieChart.Data("Plums", 10),
                new PieChart.Data("Pears", 22),
                new PieChart.Data("Apples", 30));
        final PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Imported Fruits");
        pieChart.setLegendSide(Side.LEFT);
        pieChart.setLabelLineLength(3);
        
        return pieChart;
    }
    
    private AreaChart createAreaChart() {
    	final NumberAxis xAxis = new NumberAxis(1, 31, 1);
        final NumberAxis yAxis = new NumberAxis();
        final AreaChart<Number,Number> ac = 
            new AreaChart<Number,Number>(xAxis,yAxis);
        ac.setTitle("Temperature Monitoring (in Degrees C)");
 
        XYChart.Series seriesApril= new XYChart.Series();
        seriesApril.setName("April");
        seriesApril.getData().add(new XYChart.Data(1, 4));
        seriesApril.getData().add(new XYChart.Data(3, 10));
        seriesApril.getData().add(new XYChart.Data(6, 15));
        seriesApril.getData().add(new XYChart.Data(9, 8));
        seriesApril.getData().add(new XYChart.Data(12, 5));
        seriesApril.getData().add(new XYChart.Data(15, 18));
        seriesApril.getData().add(new XYChart.Data(18, 15));
        seriesApril.getData().add(new XYChart.Data(21, 13));
        seriesApril.getData().add(new XYChart.Data(24, 19));
        seriesApril.getData().add(new XYChart.Data(27, 21));
        seriesApril.getData().add(new XYChart.Data(30, 21));
        
        XYChart.Series seriesMay = new XYChart.Series();
        seriesMay.setName("May");
        seriesMay.getData().add(new XYChart.Data(1, 20));
        seriesMay.getData().add(new XYChart.Data(3, 15));
        seriesMay.getData().add(new XYChart.Data(6, 13));
        seriesMay.getData().add(new XYChart.Data(9, 12));
        seriesMay.getData().add(new XYChart.Data(12, 14));
        seriesMay.getData().add(new XYChart.Data(15, 18));
        seriesMay.getData().add(new XYChart.Data(18, 25));
        seriesMay.getData().add(new XYChart.Data(21, 25));
        seriesMay.getData().add(new XYChart.Data(24, 23));
        seriesMay.getData().add(new XYChart.Data(27, 26));
        seriesMay.getData().add(new XYChart.Data(31, 26));
        
        ac.getData().addAll(seriesApril, seriesMay);
        
        return ac;
    }
    
 
    public static void main(String[] args) {
        launch(args);
    }
}