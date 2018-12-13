package com.example.dell.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChartActivity extends AppCompatActivity {
    private LineChartView chartView;
    private LineChartData chartData;
    private int numberOfLines = 1;
    private int numberOfPoints = 12;

    float[][] randomNumbersTab = new float[numberOfLines][numberOfPoints];
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        chartView = (LineChartView)findViewById(R.id.chart);
        chartView.setOnValueTouchListener(new ValueTouchListener());
        generateValues();
        generateData();
        chartView.setViewportCalculationEnabled(false);
    }

    // 生成二维数据点
    private void generateValues(){
        for (int i = 0; i < numberOfLines; i++) {
            for (int j = 0; j < numberOfPoints; j++) {
                randomNumbersTab[i][j] = (float)Math.random()*10f;
            }
        }
    }

    private void generateData(){
        List<Line> lineList = new ArrayList<>();
        for (int i = 0; i < numberOfLines; i++) {
            List<PointValue> values = new ArrayList<>();
            for (int j = 0; j < numberOfPoints; j++) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }
            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            if (pointsHaveDifferentColor){
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lineList.add(line);
        }
        chartData = new LineChartData(lineList);
        if (hasAxes) {
            Axis axisX = new Axis();
            // 是否显示水平线
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName(getString(R.string.time));
                axisY.setName(getString(R.string.value));
            }
            // 设置X轴坐标轴名称
            chartData.setAxisXBottom(axisX);
            // 设置Y轴坐标轴名称
            chartData.setAxisYLeft(axisY);
        } else {
            chartData.setAxisXBottom(null);
            chartData.setAxisYLeft(null);
        }
        chartData.setBaseValue(Float.NEGATIVE_INFINITY);
        chartView.setLineChartData(chartData);
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener{
        @Override
        public void onValueSelected(int i, int i1, PointValue pointValue) {
            Toast.makeText(LineChartActivity.this, "Selected: " + pointValue, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }
    }
}
