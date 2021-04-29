package es.sacyl.gsa.inform.ui.graficos;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.Div;
import java.time.LocalDate;
import java.util.stream.IntStream;

/**
 *
 * @author 06551256M
 *
 * https://apexcharts.com/docs/creating-first-javascript-chart/
 *
 * https://appreciated-collection.herokuapp.com/apexcharts/
 *
 * https://vaadin.com/directory/component/apexchartsjs/discussions
 */
public class Grafico1 extends Div {

    public Grafico1() {
        ApexCharts areaChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.area)
                        .withZoom(ZoomBuilder.get()
                                .withEnabled(false)
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false)
                        .build())
                .withStroke(StrokeBuilder.get().withCurve(Curve.straight).build())
                .withSeries(new Series<>("STOCK ABC", 10.0, 41.0, 35.0, 51.0, 49.0, 62.0, 69.0, 91.0, 148.0))
                .withTitle(TitleSubtitleBuilder.get()
                        .withText("Fundamental Analysis of Stocks")
                        .withAlign(Align.left).build())
                .withSubtitle(TitleSubtitleBuilder.get()
                        .withText("Price Movements")
                        .withAlign(Align.left).build())
                .withLabels(IntStream.range(1, 10).boxed().map(day -> LocalDate.of(2000, 1, day).toString()).toArray(String[]::new))
                .withXaxis(XAxisBuilder.get()
                        .withType(XAxisType.datetime).build())
                .withYaxis(YAxisBuilder.get()
                        .withOpposite(true).build())
                .withLegend(LegendBuilder.get().withHorizontalAlign(HorizontalAlign.left).build())
                .build();
        add(areaChart);
        setWidth("100%");
    }
}
