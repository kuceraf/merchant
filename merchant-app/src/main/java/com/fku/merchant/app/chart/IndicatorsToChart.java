//package com.fku.merchant.app.chart;
//
//import com.fku.analyst.CsvTradesLoader;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.axis.DateAxis;
//import org.jfree.chart.plot.XYPlot;
//import org.jfree.chart.ui.ApplicationFrame;
//import org.jfree.data.time.Day;
//import org.jfree.data.time.Minute;
//import org.jfree.data.time.TimeSeriesCollection;
//import org.ta4j.core.Bar;
//import org.ta4j.core.Decimal;
//import org.ta4j.core.Indicator;
//import org.ta4j.core.TimeSeries;
//import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class IndicatorsToChart {
//    public static void main(String[] args) {
//        TimeSeries series = CsvTradesLoader.loadBitstampSeries();
//        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
//        TimeSeriesCollection dataset = new TimeSeriesCollection();
//        dataset.addSeries(buildChartTimeSeries(series, closePrice, "Apple Inc. (AAPL) - NASDAQ GS"));
//
//        JFreeChart chart = createChart(dataset);
//        displayChart(chart);
//    }
//
//    private static JFreeChart createChart(TimeSeriesCollection dataset) {
//        JFreeChart chart = ChartFactory.createTimeSeriesChart(
//                "Apple Inc. 2013 Close Prices", // title
//                "Date", // x-axis label
//                "Price Per Unit", // y-axis label
//                dataset, // data
//                true, // create legend?
//                true, // generate tooltips?
//                false // generate URLs?
//        );
//        XYPlot plot = (XYPlot) chart.getPlot();
//        DateAxis axis = (DateAxis) plot.getDomainAxis();
//        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
//        return chart;
//    }
//
//    /**
//     * Displays a chart in a frame.
//     * @param chart the chart to be displayed
//     */
//    private static void displayChart(JFreeChart chart) {
//        // Chart panel
//        ChartPanel panel = new ChartPanel(chart);
//        panel.setFillZoomRectangle(true);
//        panel.setMouseWheelEnabled(true);
//        panel.setPreferredSize(new java.awt.Dimension(500, 270));
//        // Application frame
//        ApplicationFrame frame = new ApplicationFrame("Ta4j example - Indicators to chart");
//        frame.setContentPane(panel);
//        frame.pack();
////        RefineryUtilities.centerFrameOnScreen(frame);
//        frame.setVisible(true);
//    }
//
//    /**
//     * Builds a JFreeChart time series from a Ta4j time series and an indicator.
//     * @param barseries the ta4j time series
//     * @param indicator the indicator
//     * @param name the name of the chart time series
//     * @return the JFreeChart time series
//     */
//    private static org.jfree.data.time.TimeSeries buildChartTimeSeries(TimeSeries barseries, Indicator<Decimal> indicator, String name) {
//        org.jfree.data.time.TimeSeries chartTimeSeries = new org.jfree.data.time.TimeSeries(name);
//        for (int i = 0; i < barseries.getBarCount(); i++) {
//            Bar bar = barseries.getBar(i);
//            chartTimeSeries.add(new Minute(Date.from(bar.getEndTime().toInstant())), indicator.getValue(i).doubleValue());
//        }
//        return chartTimeSeries;
//    }
//}
