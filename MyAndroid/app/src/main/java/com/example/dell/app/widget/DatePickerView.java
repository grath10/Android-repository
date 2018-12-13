package com.example.dell.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.dell.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DatePickerView extends View{
    private Context context;
    private boolean isLoop = true;
    public static final float MARGIN_ALPHA = 2.8f;
    public static final float SPEED = 10;
    private List<String> mDataList;
    private int mCurrentSelected;
    private Paint mPaint, nPaint;
    private float mMaxTextSize = 80;
    private float mMinTextSize = 40;
    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 120;
    private int mViewHeight;
    private int mViewWidth;
    private float mLastDownY;
    private float mMoveLen = 0;
    private boolean isInit = false;
    private boolean canScroll = true;
    private onSelectListener mSelectListener;
    private Timer timer;
    private UserTimerTask timerTask;

    private Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(Math.abs(mMoveLen) < SPEED){
                mMoveLen = 0;
                if(timerTask != null){
                    timerTask.cancel();
                    timerTask = null;
                    performSelect();
                }
            }else {
                mMoveLen -= mMoveLen/Math.abs(mMoveLen)*SPEED;
            }
            invalidate();
        }
    };

    public DatePickerView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.context = context;
        init();
    }

    public void setOnSelectListener(onSelectListener listener){
        mSelectListener = listener;
    }

    private void performSelect(){
        if(mSelectListener != null){
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
        }
    }

    public void setData(List<String> dataList){
        mDataList = dataList;
        mCurrentSelected = dataList.size() / 4;
        invalidate();
    }

    public void setSelected(int selected) {
        mCurrentSelected = selected;
        if (isLoop) {
            int distance = mDataList.size() / 2 - mCurrentSelected;
            if (distance < 0) {
                for (int i = 0; i < -distance; i++) {
                    moveHeadToTail();
                    mCurrentSelected--;
                }
            } else if (distance > 0) {
                for (int i = 0; i < distance; i++) {
                    moveTailToHead();
                    mCurrentSelected++;
                }
            }
        }
        invalidate();
    }

    public void setSelected(String mSelectItem) {
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).equals(mSelectItem)) {
                setSelected(i);
                break;
            }
        }
    }

    private void moveHeadToTail() {
        if (isLoop) {
            String head = mDataList.get(0);
            mDataList.remove(0);
            mDataList.add(head);
        }
    }

    private void moveTailToHead() {
        if (isLoop) {
            String tail = mDataList.get(mDataList.size() - 1);
            mDataList.remove(mDataList.size() - 1);
            mDataList.add(0, tail);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        // 按照View高度计算字体大小
        mMaxTextSize = mViewHeight / 7f;
        mMinTextSize = mMaxTextSize / 2.2f;
        isInit = true;
        invalidate();
    }

    private void init() {
        timer = new Timer();
        mDataList = new ArrayList<>();
        //第一个paint
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(ContextCompat.getColor(context, R.color.text1));
        //第二个paint
        nPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nPaint.setStyle(Paint.Style.FILL);
        nPaint.setTextAlign(Paint.Align.CENTER);
        nPaint.setColor(ContextCompat.getColor(context, R.color.text2));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据index绘制view
        if (isInit) {
            drawData(canvas);
        }
    }

    private void drawData(Canvas canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        float scale = parabola(mViewHeight / 4.0f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint);
        // 绘制上方data
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
        // 绘制下方data
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            drawOtherText(canvas, i, 1);
        }
    }

    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = MARGIN_ALPHA * mMinTextSize * position + type * mMoveLen;
        float scale = parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        nPaint.setTextSize(size);
        nPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        Paint.FontMetricsInt fmi = nPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected + type * position),
                (float) (mViewWidth / 2.0), baseline, nPaint);
    }

    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveLen += (event.getY() - mLastDownY);
                if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
                    if (!isLoop && mCurrentSelected == 0) {
                        mLastDownY = event.getY();
                        invalidate();
                        return true;
                    }
                    if (!isLoop) {
                        mCurrentSelected--;
                    }
                    // 往下滑超过离开距离
                    moveTailToHead();
                    mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
                } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
                    if (mCurrentSelected == mDataList.size() - 1) {
                        mLastDownY = event.getY();
                        invalidate();
                        return true;
                    }
                    if (!isLoop) {
                        mCurrentSelected++;
                    }
                    // 往上滑超过离开距离
                    moveHeadToTail();
                    mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
                }
                mLastDownY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                doUp();
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event){
        if(timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
        mLastDownY = event.getY();
    }

    private void doUp(){
        if(Math.abs(mMoveLen) < 0.0001){
            mMoveLen = 0;
            return;
        }
        if(timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
        timerTask = new UserTimerTask(updateHandler);
        timer.schedule(timerTask, 0 , 10);
    }
    public interface onSelectListener{
        void onSelect(String text);
    }

    public void setCanScroll(boolean canScroll){
        this.canScroll = canScroll;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return canScroll && super.dispatchTouchEvent(event);
    }

    public void setLoop(boolean loop) {
        this.isLoop = loop;
    }

    class UserTimerTask extends TimerTask{
        Handler handler;

        public UserTimerTask(Handler handler){
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }
}
