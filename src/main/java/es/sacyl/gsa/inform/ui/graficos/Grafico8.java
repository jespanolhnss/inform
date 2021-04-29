package es.sacyl.gsa.inform.ui.graficos;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.Div;

/**
 *
 * @author 06551256M
 */
public class Grafico8 extends Div {

    public Grafico8() {
        ApexCharts radarChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.radar)
                        .build())
                .withSeries(new Series<>("Series 1", 80, 50, 30, 40, 100, 20))
                .withTitle(TitleSubtitleBuilder.get()
                        .withText("Basic Radar Chart")
                        .build())
                .withLabels("January", "February", "March", "April", "May", "June")
                .build();
        add(radarChart);
        setWidth("100%");
    }
}
