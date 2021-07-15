/** author: Qiwei Sun and Haoyu Wang
 * date: 2019.10.31
 * This is used to format the time stamp
 */

package com.example.project.utils;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class PercentFormatter extends ValueFormatter {

    private DecimalFormat mFormat;
    private PieChart pieChart;

    public PercentFormatter(String unit) {
        setValueFormatter(unit);
    }

    public PercentFormatter(PieChart pieChart, String unit) {
        this(unit);
        this.pieChart = pieChart;
    }

    /**
     * 设置单位
     *
     * @param unit
     */
    public void setValueFormatter(String unit) {
        mFormat = new DecimalFormat("###,###,###,##" + unit);
//        mFormat = new DecimalFormat("###,###,##0.0 " + unit);
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value);
    }

    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
            // Converted to percent
            return getFormattedValue(value);
        } else {
            // raw value, skip percent sign
            return mFormat.format(value);
        }
    }

}
