package com.penn.chartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: DaDing
 * @description: 折线图
 * @create: 2018-11-13 11:08
 **/
public class LineChartView extends View {


    private List<Point> points = new ArrayList<>();
    private Paint paint = new Paint();
    private Paint pointPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private int width = 0;//控件的宽
    private int height = 0;//控件的高
    private float emptyTextSize = 52;            //没有数据时显示
    private int emptyTextColor = 0xff999999;
    private String emptyTextStr = "没有数据";

    private int xAxisColor = 0xff999999;//x轴颜色
    private int yAxisColor = 0xff999999;//y轴颜色
    private int lineColor = 0xff9646fd;//折线颜色
    private int xAxisTextColor = 0xff999999;//x轴文字颜色
    private int yAxisTextColor = 0xff999999;//y轴文字颜色
    private float xAxisWidth = 2f;//x轴宽度
    private float yAxisWidth = 2f;//y轴宽度
    private float lineWidth = 2f;//折线宽度
    private float chartPadding = 20f;//绘制图标的区域距离控件的边的距离，类似padding,默认10
    private String xAxisUnit = "";//x轴的数值的单位
    private String yAxisUnit = "";//y轴的数值的单位
    private float xAxisSetMaxValue = 1f;//x轴的最大值
    private float xAxisSetMinValue = 0f;//x轴的最小值
    private boolean xAxisSetMax = false;//是否设置了x轴的最大值
    private boolean xAxisSetMin = false;//是否设置了x轴的最小值
    private float xAxisTextSpace = 20f;//x轴与文字的间距
    private float yAxisTextSpace = 20f;//y轴与文字的间距
    private float xAxisTextSize = 20f;
    private float yAxisTextSize = 30f;
    private float pointTextSize = 14f;
    private int xValueVisibleCount = 0;     //x轴坐标点的数量，默认0，则全部显示
    private int yValueCount = 5;     //y轴坐标点的数量，默认5个
    private int xValueSpace = 2;      //x轴坐标点间隔多少个显示，默认为0，就是没有间隔，都显示
    private boolean isYValueShowInteger = false;//y轴文字是否是显示整数


    private float singlePointSize = 30f;//只有一个点的时候，点的大小
    private float normalPointSize = 2f;//正常状态点的大小

    private XValueFormatter xValueFormatter;//x轴文字的显示形式
    private YValueFormatter yValueFormatter;//y轴文字的显示形式
    private PointValueFormatter pointValueFormatter;//点的文字显示形式

    public LineChartView(Context context) {
        this(context,null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.LineChartView,defStyleAttr,0);
        xAxisColor = array.getColor(R.styleable.LineChartView_xAxisColor,xAxisColor);
        yAxisColor = array.getColor(R.styleable.LineChartView_xAxisColor,yAxisColor);
        xAxisWidth = array.getDimension(R.styleable.LineChartView_xAxisWidth,xAxisWidth);
        yAxisWidth = array.getDimension(R.styleable.LineChartView_yAxisWidth,yAxisWidth);
        chartPadding = array.getDimension(R.styleable.LineChartView_chartPadding, chartPadding);
        lineColor = array.getColor(R.styleable.LineChartView_lineColor,lineColor);
        lineWidth = array.getDimension(R.styleable.LineChartView_lineWidth,lineWidth);
        xAxisTextSize = array.getDimension(R.styleable.LineChartView_xAxisTextSize,xAxisTextSize);
        yAxisTextSize = array.getDimension(R.styleable.LineChartView_yAxisTextSize,yAxisTextSize);
        xAxisTextColor = array.getColor(R.styleable.LineChartView_xAxisTextColor,xAxisTextColor);
        yAxisTextColor = array.getColor(R.styleable.LineChartView_yAxisTextColor,yAxisTextColor);
        emptyTextStr = array.getString(R.styleable.LineChartView_empty_text);
        emptyTextSize = array.getDimension(R.styleable.LineChartView_empty_text_size,emptyTextSize);
        emptyTextColor = array.getColor(R.styleable.LineChartView_empty_text_color,emptyTextColor);
        array.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode==MeasureSpec.EXACTLY){
            width = widthSize;
        }
        if (heightMode==MeasureSpec.EXACTLY){
            height = heightSize;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        textPaint.setAntiAlias(true);
        paint.setAntiAlias(true);
        pointPaint.setAntiAlias(true);
        //如果没有数据，就提示没有数据的提示语
        if (points.isEmpty()){
            textPaint.setTextSize(emptyTextSize);
            textPaint.setColor(emptyTextColor);
            float textWidth = textPaint.measureText(emptyTextStr);
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            float textHeight = fm.descent - fm.ascent;
            float x = width/2 - textWidth/2;
            float y = height/2 + textHeight/2;
            canvas.drawText(emptyTextStr,x,y,textPaint);
            return;
        }
        float yMax = getYMax();
        float yMin = getYMin();
        float xMax = getXMax();
        float xMin = getXMin();

        if (yMax - yMin==0){
            yMax += 1;
        }

        //获得y轴左边的文字需要的宽度和高度
        textPaint.setTextSize(yAxisTextSize);
        String yStr = "";
        if (yValueFormatter!=null){
            yStr = yValueFormatter.getFormatterValue(yMax);
        }else {
            yStr = yMax+yAxisUnit;
        }
        float yTextWidth = textPaint.measureText(yStr);//y轴文字的宽度
        Paint.FontMetrics yFm = textPaint.getFontMetrics();
        float yValueHeight = yFm.descent - yFm.ascent;//y轴文字的高度


        //获得x轴下边的文字需要的高度
        textPaint.setTextSize(xAxisTextSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float xTextHeight = fm.descent - fm.ascent;

        //画y轴
        paint.setStrokeWidth(yAxisWidth);
        paint.setColor(yAxisColor);
        float yStartX = getPaddingStart()+chartPadding+yTextWidth+yAxisTextSpace;
        float yStartY = getPaddingTop()+chartPadding+30f;
        float yStopX = getPaddingStart()+chartPadding+yTextWidth+yAxisTextSpace;
        float yStopY = this.height - (getPaddingBottom()+chartPadding+xTextHeight+xAxisTextSpace);
        canvas.drawLine(yStartX,yStartY,yStopX,yStopY,paint);

        //画x轴
        paint.setStrokeWidth(xAxisWidth);
        paint.setColor(xAxisColor);
        float xStartX = getPaddingStart()+chartPadding+yTextWidth+yAxisTextSpace;
        float xStartY = this.height - (getPaddingBottom()+chartPadding+xTextHeight+xAxisTextSpace);
        float xStopX = this.width - (getPaddingEnd()+chartPadding+30f);
        float xStopY = this.height - (getPaddingBottom()+chartPadding+xTextHeight+xAxisTextSpace);
        canvas.drawLine(xStartX,xStartY,xStopX,xStopY,paint);


        float xAxisLength = xStopX - xStartX;//x轴的长度
        float yAxisLength = yStopY - yStartY;//y轴的长度

        float xValueY = this.height -(getPaddingBottom()+chartPadding);//x轴的y坐标
        float yValueX = getPaddingStart()+chartPadding;//y轴的x坐标

        //求x的每段的距离

        float segmentLength = points.size()==1?xAxisLength : (xAxisLength / (points.size()-1));

        //画折线
        float oldX = 0f;
        float oldY = 0f;
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);

        pointPaint.setColor(lineColor);
        float radius = points.size()==1?singlePointSize:normalPointSize;
        textPaint.setTextSize(14f);
        for (int i = 0; i < points.size(); i++) {
            //如果点小于最小值，大于最大值，就不画
            if (points.get(i).getX()<xMin||points.get(i).getX()>xMax){
                continue;
            }
            float pointX = xStartX + i * segmentLength;
            float pointY = yStopY - ((points.get(i).getY() - yMin) / (yMax - yMin)) * yAxisLength;

            canvas.drawCircle(pointX,pointY,radius,pointPaint);
            String pointStr = "";
            if (pointValueFormatter!=null){
                pointStr = pointValueFormatter.getFormatterValue(points.get(i).getY());
            }else {
                //如果有小数，那么就格式化
                pointStr = points.get(i).getY()+yAxisUnit;
            }
            canvas.drawText(pointStr,pointX,pointY-10,textPaint);

            if (oldX == 0f && oldY == 0f) {
                oldX = pointX;
                oldY = pointY;
            } else {
                //画前一个点到这个点之间的线
                canvas.drawLine(oldX, oldY, pointX, pointY, paint);
                oldX = pointX;
                oldY = pointY;
            }
        }

        //画x轴线上的点的文字
        textPaint.setTextSize(xAxisTextSize);
        textPaint.setColor(xAxisTextColor);

        int count = points.size();

        for (int i=0;i<count;i++){
            float x = xStartX + i*segmentLength;
            String xValueStr = "";
            if (xValueFormatter!=null){
                xValueStr = xValueFormatter.getFormattedValue(points.get(i).getX());
            }else {
                xValueStr = String.valueOf((int)points.get(i).getX())+xAxisUnit;
            }
            float xValueWidth = textPaint.measureText(xValueStr);
            if (count<=5){
                canvas.drawText(xValueStr,x - xValueWidth/2,xValueY,textPaint);
            }else {
                if (i%(xValueSpace+1)==0){
                    canvas.drawText(xValueStr,x - xValueWidth/2,xValueY,textPaint);
                }
            }

        }


        //画y轴线上的点的文字
        textPaint.setTextSize(yAxisTextSize);
        textPaint.setColor(yAxisTextColor);
        for (int i=0;i<=yValueCount;i++){
            float y = yStopY - ((float) i/yValueCount)*yAxisLength;
            float yValue;
            if ((yMax-yMin)/yValueCount<1){
                yValue = yMin+i;
            }else {
                yValue = ((float) i/yValueCount)*(yMax-yMin)+yMin;
            }
            String yValueStr = "";
            if (yValueFormatter!=null){
                yValueStr = yValueFormatter.getFormatterValue(yValue);
            }else {
                yValueStr = yValue+yAxisUnit;
            }
            canvas.drawText(yValueStr,yValueX,y+yValueHeight/2,textPaint);
        }
    }

    /**
     * 设置数据
     */
    public void setPoints(List<Point> points){
        this.points.clear();
        this.points.addAll(points);
        invalidate();
    }

    /**
     * 设置x轴最大值
     */
    public void setXAxisSetMaxValue(float xMax){
        xAxisSetMaxValue = xMax;
        xAxisSetMax = true;
    }


    /**
     * 设置x轴最小值
     */
    public void setXAxisSetMinValue(float xMin){
        xAxisSetMinValue = xMin;
        xAxisSetMin = true;
    }



    /**
     * 设置x轴的数值的单位
     */
    public void setXAxisUnit(String unit){
        xAxisUnit = unit;
    }

    /**
     * 设置y轴的数值的单位
     */
    public void setYAxisUnit(String unit){
        yAxisUnit = unit;
    }

    /**
     * 设置x轴坐标点的间隔
     * @param xValueSpace
     */
    public void setXValueSpace(int xValueSpace) {
        this.xValueSpace = xValueSpace;
    }

    /**
     * 设置y轴坐标的数量
     * @param yValueCount
     */
    public void setYValueCount(int yValueCount) {
        this.yValueCount = yValueCount;
    }

    /**
     * 设置x轴与文字的距离
     * @param xAxisTextSpace
     */
    public void setxAxisTextSpace(float xAxisTextSpace) {
        this.xAxisTextSpace = xAxisTextSpace;
    }


    /**
     * 设置y轴与文字的距离
     * @param yAxisTextSpace
     */
    public void setyAxisTextSpace(float yAxisTextSpace) {
        this.yAxisTextSpace = yAxisTextSpace;
    }

    private float getYMax(){
        float yMax = points.get(0).getY();
        for (Point point : points) {
            yMax = point.getY()>yMax?point.getY():yMax;
        }
        if (yValueFormatter!=null){
            String formatterValue = yValueFormatter.getFormatterValue(yMax);
            yMax = Float.valueOf(formatterValue);
        }
        return yMax;
    }

    private float getYMin(){
        float yMin = points.get(0).getY();
        for (Point point : points) {
            yMin = point.getY()<yMin?point.getY():yMin;
        }
        if (yValueFormatter!=null){
            String formatterValue = yValueFormatter.getFormatterValue(yMin);
            yMin = Float.valueOf(formatterValue);
        }
        return yMin;
    }


    private float getXMax(){
        float xMax = points.get(0).getX();
        for (Point point : points) {
            xMax = point.getX()>xMax?point.getX():xMax;
        }
        return xMax;
    }

    private float getXMin(){
        float xMin = points.get(0).getX();
        for (Point point : points) {
            xMin = point.getX()<xMin?point.getX():xMin;
        }
        return xMin;
    }

    public interface XValueFormatter{
        String getFormattedValue(float value);
    }

    /**
     * 设置x轴显示文字的形式
     * @param xValueFormatter
     */
    public void setXValueFormatter(XValueFormatter xValueFormatter) {
        this.xValueFormatter = xValueFormatter;
    }

    public interface YValueFormatter{
        String getFormatterValue(float value);
    }

    /**
     * 设置y轴数值显示形式
     * @param yValueFormatter
     */
    public void setYValueFormatter(YValueFormatter yValueFormatter) {
        this.yValueFormatter = yValueFormatter;
    }

    public interface PointValueFormatter{
        String getFormatterValue(float value);
    }

    /**
     * 设置点的数值显示形式
     * @param pointValueFormatter
     */
    public void setPointValueFormatter(PointValueFormatter pointValueFormatter) {
        this.pointValueFormatter = pointValueFormatter;
    }

    /**
     * 设置y轴数值为整数
     * @param YValueShowInteger
     */
    public void setYValueShowInteger(boolean YValueShowInteger) {
        isYValueShowInteger = YValueShowInteger;
    }

    public void setSinglePointSize(float singlePointSize) {
        this.singlePointSize = singlePointSize;
    }

    public void setNormalPointSize(float normalPointSize) {
        this.normalPointSize = normalPointSize;
    }
}
