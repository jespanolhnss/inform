/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.reports.indicadores;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.FillBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.TooltipBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.series.SeriesType;
import com.github.appreciated.apexcharts.config.tooltip.builder.YBuilder;
import com.github.appreciated.apexcharts.config.yaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.Div;
import es.sacyl.gsa.inform.bean.DWIndicadorValorAno;

/**
 *
 * @author 06551256M
 */
public class GraficoBarrasIngresosAA extends Div {

    public GraficoBarrasIngresosAA(DWIndicadorValorAno anoactual, DWIndicadorValorAno anoanterior,
            String ejexLabel, Integer xactual, Integer xanterior) {

        Series actual = new Series<>(xactual.toString(), SeriesType.column, anoactual.getMesesNoTotal());
        Series anterior = new Series<>(xanterior.toString(), SeriesType.column, anoanterior.getMesesNoTotal());
        ApexCharts barChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get()
                        .withType(Type.bar)
                        .build())
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get()
                                .withHorizontal(false)
                                .withColumnWidth("55%")
                                .build())
                        .build())
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false).build())
                .withStroke(StrokeBuilder.get()
                        .withShow(true)
                        .withWidth(2.0)
                        .withColors("transparent")
                        .build())
                .withSeries(actual, anterior)
                .withYaxis(YAxisBuilder.get()
                        .withTitle(TitleBuilder.get()
                                .withText(ejexLabel)
                                .build())
                        .build())
                .withXaxis(XAxisBuilder.get().withCategories("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic").build())
                .withFill(FillBuilder.get()
                        .withOpacity(1.0).build())
                .withTooltip(TooltipBuilder.get()
                        .withY(YBuilder.get()
                                .withFormatter("function (val) {\n"
                                        + // Formatter currently not yet working
                                        "return \" \" + val + \" \"\n"
                                        + "}").build())
                        .build())
                .build();

        barChart.setLabels("Gráfico comparado");
        //  barChart.setResponsive(responsive);
        add(barChart);
        setWidth("100%");
    }
}
