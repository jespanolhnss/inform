/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.reports.indicadores;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.TitleSubtitle;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.FillBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.TooltipBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.github.appreciated.apexcharts.config.series.SeriesType;
import com.github.appreciated.apexcharts.config.tooltip.builder.YBuilder;
import com.github.appreciated.apexcharts.config.yaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.html.Div;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.dao.DWQuerysDao;
import es.sacyl.gsa.inform.dao.HpHisClinicaDao;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author 06551256M
 */
public class GraficosUrgencias extends Div {

    public GraficosUrgencias() {

    }

    public Div ultimos30dias() {
        this.removeAll();
        ArrayList<DatoGenericoBean> covidLista = new DWQuerysDao().getUgenciasUltimos30dias("COVID");
        ArrayList<DatoGenericoBean> nocovidLista = new DWQuerysDao().getUgenciasUltimos30dias("NOCOVID");
        String[] label = new String[30];
        Double[] valuesCovid = new Double[30];
        Double[] valuesNoCovid = new Double[30];
        int i = 0;
        for (DatoGenericoBean dato : covidLista) {
            label[i] = dato.getTipoDato();
            valuesCovid[i] = Double.valueOf(dato.getValorInt());
            i++;
        }
        i = 0;
        for (DatoGenericoBean dato : nocovidLista) {
            valuesNoCovid[i] = Double.valueOf(dato.getValorInt());
            i++;
        }
        Series covidSerie = new Series<>("Covid", SeriesType.column, valuesCovid);
        Series nocovodSerid = new Series<>("No Covid", SeriesType.column, valuesNoCovid);
        TitleSubtitle titulo = new TitleSubtitle();
        titulo.setText("Últimos 30 dias");
        ApexCharts barChart = ApexChartsBuilder.get()
                .withTitle(titulo)
                .withChart(ChartBuilder.get()
                        .withType(Type.line)
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
                .withSeries(covidSerie, nocovodSerid)
                .withYaxis(YAxisBuilder.get()
                        .withTitle(TitleBuilder.get()
                                .withText("Pacientes")
                                .build())
                        .build())
                .withXaxis(XAxisBuilder.get().withCategories(
                        label).build())
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
        return this;
    }

    public Div urgActuales() {
        this.removeAll();
        ArrayList<DatoGenericoBean> lista = new HpHisClinicaDao().getUrgenciasFecha(LocalDate.now());

        String[] label = new String[lista.size()];
        Double[] values = new Double[lista.size()];
        int i = 0;
        int total = 0;
        for (DatoGenericoBean dato : lista) {
            label[i] = dato.getTipoDato() + "(" + dato.getValorInt() + ")";
            values[i] = Double.valueOf(dato.getValorInt());
            total += dato.getValorInt();
            i++;
        }
        TitleSubtitle titulo = new TitleSubtitle();
        titulo.setText("Urgencias Hoy :" + total);
        ApexCharts pieChart = ApexChartsBuilder.get()
                .withTitle(titulo)
                .withChart(ChartBuilder.get().withType(Type.pie).build())
                .withLabels(label)
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.right)
                        .build())
                .withSeries(values)
                .withResponsive(ResponsiveBuilder.get()
                        .withBreakpoint(480.0)
                        .withOptions(OptionsBuilder.get()
                                .withLegend(LegendBuilder.get()
                                        .withPosition(Position.bottom)
                                        .build())
                                .build())
                        .build())
                .build();
        add(pieChart);
        setWidth("100%");
        return this;
    }

}
