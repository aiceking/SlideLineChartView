package com.example.vmmet.myview_textview;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
/**
 * Created by Vmmet on 2016/7/22.
 */
public class SlideLineChartView extends View{
    private Paint chart_paint,Text_paint,LineAndPoint_paint,X_Y_lines,Rect_paint;
    //原点的坐标
    private float origin_x,origin_y;
    //X轴的单位间隔
    private float averagr_x;
    //点的X,Y坐标
    private List<String> list_x,list_y;
    private int lastX,offX,X1,X2;
    public SlideLineChartView(Context context) {
        super(context);
    }
    public SlideLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        chart_paint=new Paint();
        chart_paint.setAntiAlias(true);
        chart_paint.setStrokeWidth(3);
        chart_paint.setColor(Color.BLUE);
        Text_paint=new Paint();
        Text_paint.setTextAlign(Paint.Align.CENTER);
        Text_paint.setAntiAlias(true);
        Text_paint.setTextSize(30);
        Text_paint.setColor(Color.RED);
        LineAndPoint_paint=new Paint();
        LineAndPoint_paint.setAntiAlias(true);
        LineAndPoint_paint.setStrokeWidth(5);
        LineAndPoint_paint.setColor(Color.parseColor("#9400D3"));
        X_Y_lines=new Paint();
        X_Y_lines.setAntiAlias(true);
        X_Y_lines.setColor(Color.parseColor("#C6E2FF"));
        Path path = new Path();
        PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
        X_Y_lines.setPathEffect(effects);
        Rect_paint=new Paint();
        Rect_paint.setAntiAlias(true);
        Rect_paint.setColor(Color.parseColor("#60436EEE"));
    }
    public SlideLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setPoints(HashMap<String ,String> map){
        if (map!=null)
            list_x=new ArrayList<>();
            list_y=new ArrayList<>();
        ///把map中的坐标系拿出来
        Iterator iterator=map.keySet().iterator();
        while (iterator.hasNext()){
            list_x.add(iterator.next().toString());
        }
        //对list进行排序，升序排列
        getOrderList(list_x);
        for(int i=0;i<list_x.size();i++){
            list_y.add(map.get(list_x.get(i)));
        }
        X1=0;X2=0;
        invalidate();
    }
    private void getOrderList(List<String> list) {
        float []s=new float[list.size()];
        for (int i=0;i<list.size();i++){
            s[i]=Float.parseFloat(list.get(i));
        }
        Arrays.sort(s);
        list_x.clear();
        for (int i=0;i<s.length;i++){
            list_x.add(s[i]+"");
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        origin_x=60;
        origin_y=getHeight()-60;
        //X轴的单位间隔
        averagr_x=(getWidth()-90) / 6;
        //画基本图
        drawOthers(canvas);
        if (list_x!=null)
            if (list_x.size()!=0){
                //画X轴的箭头，刻度线以及对应的数字
                drawXtext(canvas);
                //画点和连线成折线图
                drawPointAndLine(canvas);
                //画柱状图和对应的高度text
                drawRectAndText(canvas);
            }
        super.onDraw(canvas);
    }
    private void drawPointAndLine(Canvas canvas) {
        for(int i=0;i<5;i++){
            //画点
            canvas.drawCircle(origin_x + averagr_x * Float.parseFloat(list_x.get(i)),
                    origin_y - averagr_x * Float.parseFloat(list_y.get(i + X1)) / 10,
                    10, LineAndPoint_paint);
            if (i<5){
                //连成折线图
                if (i+1+X1<list_x.size())
                canvas.drawLine(origin_x + averagr_x * Float.parseFloat(list_x.get(i)), origin_y - averagr_x * Float.parseFloat(list_y.get(i+X1)) / 10,
                        origin_x + averagr_x * Float.parseFloat(list_x.get(i+1)), origin_y - averagr_x * Float.parseFloat(list_y.get(i+1+X1)) / 10,
                        LineAndPoint_paint);
            }
        }
    }
    private void drawRectAndText(Canvas canvas) {
        for(int i=0;i<5;i++) {
            //画柱状图
            canvas.drawText(String.format("%.2f", Float.parseFloat(list_y.get(i+X1))).toString(),
                    origin_x + averagr_x * Float.parseFloat(list_x.get(i)),
                    origin_y - averagr_x * Float.parseFloat(list_y.get(i+X1)) / 10-20,
                    Text_paint);
            canvas.drawRect(origin_x + averagr_x * Float.parseFloat(list_x.get(i)) - averagr_x / 3,
                    origin_y - averagr_x * Float.parseFloat(list_y.get(i+X1)) / 10,
                    origin_x + averagr_x * Float.parseFloat(list_x.get(i)) + averagr_x / 3,
                    origin_y, Rect_paint);
        }
    }
    private void drawOthers(Canvas canvas) {
        //画Title
        canvas.drawText("这是我的折线图",getWidth()/2,50,Text_paint);
        ///画Y轴
        canvas.drawLine(origin_x, origin_y, origin_x, 0, chart_paint);
        ///画X轴
        canvas.drawLine(origin_x,origin_y,getWidth()-30,origin_y,chart_paint);
        ///画Y轴的箭头
        canvas.drawLine(origin_x, 0, origin_x-10, 15, chart_paint);
        canvas.drawLine(origin_x, 0, origin_x+10, 15, chart_paint);
        //画出Y轴的刻度线和对应数字
        for(int i=0;i<=7;i++){
            if (i>0)
                canvas.drawText(i *10+ "", origin_x - 30,origin_y -i * averagr_x+10,  Text_paint);
            canvas.drawLine(origin_x,origin_y-i*averagr_x,origin_x+10, origin_y-i*averagr_x,  chart_paint);
            //画出刻度虚线
            canvas.drawLine(origin_x,origin_y-i*averagr_x,getWidth()-30, origin_y-i*averagr_x,  X_Y_lines);
        }

    }
    private void drawXtext(Canvas canvas) {
        ///画X轴的箭头
        if (X1 + 5 == list_x.size()) {
            canvas.drawLine(getWidth() - 30, origin_y, getWidth() - 45, origin_y - 10, chart_paint);
            canvas.drawLine(getWidth() - 30, origin_y, getWidth() - 45, origin_y + 10, chart_paint);
        }
        //画出X轴的刻度线和对应数字
        for(int i=0;i<=5;i++){
            canvas.drawText(i + X1 + "", origin_x + i * averagr_x, origin_y + 40, Text_paint);
            if (i>0)
                canvas.drawLine(origin_x+i*averagr_x, origin_y, origin_x+i*averagr_x, origin_y-10, chart_paint);
            //画出刻度虚线
            canvas.drawLine(origin_x + i * averagr_x, origin_y, origin_x + i * averagr_x, 0, X_Y_lines);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //手指接触的X坐标
        int x = (int) event.getX();
        if (list_x!=null)
            if (list_x.size()>0)
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算移动的距离比例
                 offX = lastX-x ;
                X1=X2+offX/((int)averagr_x/2);
                if (list_x!=null)
                    if (list_x.size()!=0)
                if(X1>=0&&X1+5<=list_x.size()){
                invalidate();
                }else if (X1<0){
                    X2=0;
                }else {
                    X2=list_x.size()-5;
                }
                break;
            case MotionEvent.ACTION_UP:
                //记录已经滑动的距离比例
                offX = lastX-x ;
                X2=X2+offX/((int)averagr_x/2);
        }
        return true;
    }
}
