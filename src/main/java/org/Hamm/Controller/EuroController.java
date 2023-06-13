package org.Hamm.Controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import org.Hamm.Model.DAO.ReservationDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EuroController {
    @FXML
    private Label totalLabel;

    @FXML
    private LineChart<String, Integer> lineChart;

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void initialize() {
        try {
            ReservationDAO reservationDAO = new ReservationDAO();

            List<Integer> counts = reservationDAO.countAccumulatedReservationsByYear();

            int total = counts.get(counts.size() - 1);
            double totalEuros = total * 3;
            totalLabel.setText("Total: " + totalEuros + " euros");

            XYChart.Series<String, Integer> series = new XYChart.Series<>();

            for (int i = 0; i < counts.size(); i++) {
                String year = String.valueOf(LocalDate.now().getYear() + i); // Obtener el año actual y sumar el índice para obtener el año correspondiente
                series.getData().add(new XYChart.Data<>(year, counts.get(i)));
            }

            lineChart.setAnimated(false); // Desactivar la animación para evitar que los valores aparezcan temporalmente
            lineChart.setCreateSymbols(true); // Mostrar los símbolos de los datos
            lineChart.getData().add(series);

            // Ocultar los valores en el eje Y
            lineChart.getYAxis().setTickLabelsVisible(false);
            lineChart.getYAxis().setOpacity(0);

            // Ajustar el tamaño del gráfico para evitar espacios vacíos debajo de la gráfica
            lineChart.setMaxHeight(Region.USE_PREF_SIZE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
