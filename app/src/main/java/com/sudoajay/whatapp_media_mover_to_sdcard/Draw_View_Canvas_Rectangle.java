package com.sudoajay.whatapp_media_mover_to_sdcard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class Draw_View_Canvas_Rectangle extends View  {
    private int background_Color;
    private int bg_Color;
    private int whatsApp_Color;
    private int dot_Color ;
    private Paint paint = new Paint();
    private double whatsApp_Percentage , other_Percentage, multi;

    public Draw_View_Canvas_Rectangle(Context context, AttributeSet attribs) {
        super(context, attribs);
        background_Color= Color.WHITE;
        bg_Color = ContextCompat.getColor(context,R.color.Draw_On_Background);
        whatsApp_Color = ContextCompat.getColor(context,R.color.colorPrimary);
        dot_Color = ContextCompat.getColor(context,R.color.dot_color_other);
    }
    public void get_Percentage(double whatsApp_Percentage , double other_Percentage ){

        this.whatsApp_Percentage = whatsApp_Percentage;


        this.other_Percentage = other_Percentage ;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        setWillNotDraw(false);
        if(multi == 1 ){
            multi = (((getWidth()-30 ) - 13)/100);
            whatsApp_Percentage *=multi;
            other_Percentage *=multi;
        }
        setBackgroundColor(background_Color);

            if (whatsApp_Percentage == 0) {
                paint.setColor(bg_Color);
                canvas.drawRoundRect(0, 0, getWidth() - 30, getHeight() - 30, 8, 8, paint);
                paint.setColor(dot_Color);
                canvas.drawRoundRect((float) whatsApp_Percentage - 10, 0, (float) (other_Percentage + whatsApp_Percentage), getHeight() - 30, 0, 8, paint);

            } else {
                paint.setColor(bg_Color);
                canvas.drawRoundRect((float) (other_Percentage + whatsApp_Percentage), 0, getWidth() - 30, getHeight() - 30, 8, 8, paint);
                paint.setColor(whatsApp_Color);
                canvas.drawRoundRect(0, 0, (float) whatsApp_Percentage, getHeight() - 30, 8, 8, paint);
                paint.setColor(dot_Color);
                canvas.drawRoundRect((float) whatsApp_Percentage - 10, 0, (float) (other_Percentage + whatsApp_Percentage), getHeight() - 30, 0, 8, paint);

            }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    public double getMulti() {
        return multi;
    }

    public void setMulti(double multi) {
        this.multi = multi;
    }
}
