package biz.borealis.numberpicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class NumberPicker extends LinearLayout {

    private float mItemSmallHeight;
    private int mAllVerticalScroll;
    private int mMin;
    private int mMax;
    private RecyclerView mRecyclerView;
    private NumberPickerAdapter mNumberPickerAdapter;
    private float mTextSize;
    private float mTextSizeSelected;
    private int mTextColor;
    private int mTextColorSelected;
    private boolean mAnimateTextSize, mTextFadeColor;

    public NumberPicker(Context context) {
        this(context, null);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.np_NumberPicker, defStyleAttr, 0);

        Resources resources = context.getResources();
        mMin = a.getInt(R.styleable.np_NumberPicker_np_min_number, resources.getInteger(R.integer.np_def_min));
        mMax = a.getInt(R.styleable.np_NumberPicker_np_min_number, resources.getInteger(R.integer.np_def_max));
        mTextColor = a.getColor(R.styleable.np_NumberPicker_np_text_color, ContextCompat.getColor(context, R.color.np_text_color));
        mTextSize = a.getDimension(R.styleable.np_NumberPicker_np_text_size, resources.getDimension(R.dimen.np_text_size));
        mTextColorSelected = a.getColor(R.styleable.np_NumberPicker_np_text_color, ContextCompat.getColor(context, R.color.np_text_color_selected));
        mTextSizeSelected = a.getDimension(R.styleable.np_NumberPicker_np_text_size, resources.getDimension(R.dimen.np_text_size_selected));
        mTextFadeColor = a.getBoolean(R.styleable.np_NumberPicker_np_fade_text_color, resources.getBoolean(R.bool.np_def_fade_color));
        mAnimateTextSize = a.getBoolean(R.styleable.np_NumberPicker_np_animate_text_size, resources.getBoolean(R.bool.np_def_animate_text_size));

        a.recycle();

        setMinimumWidth(context.getResources().getDimensionPixelSize(R.dimen.np_min_width));

        mItemSmallHeight = NumberPickerAdapter.getTextViewHeight(context, false, mTextSize, mTextSizeSelected);
        float itemBigHeight = NumberPickerAdapter.getTextViewHeight(context, true, mTextSize, mTextSizeSelected);

        mRecyclerView = new RecyclerView(context);
        int listHeight = (int) (mItemSmallHeight * 2 + itemBigHeight);
        mRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, listHeight));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        mAllVerticalScroll = 0;

        final LinearLayoutManager dateLayoutManager = new LinearLayoutManager(context);
        dateLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(dateLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                synchronized (this) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        calculatePositionAndScroll();
                    } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        if(mNumberPickerAdapter != null){
                            mNumberPickerAdapter.setSelectedItem(-1);
                        }

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mAllVerticalScroll += dy;
            }
        });
        mNumberPickerAdapter = new NumberPickerAdapter(context, mMax, mMin, itemBigHeight, mItemSmallHeight);
        mNumberPickerAdapter.setTextSize(mTextSize, mTextSizeSelected);
        mNumberPickerAdapter.setAnimateTextSize(mAnimateTextSize);
        mNumberPickerAdapter.setTextFadeColor(mTextFadeColor);
        mRecyclerView.setAdapter(mNumberPickerAdapter);
        mNumberPickerAdapter.setSelectedItem(1);
        addView(mRecyclerView);
    }

    public int getMin() {
        return mMin;
    }

    public void setMin(int min) {
        this.mMin = min;
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        this.mMax = max;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public float getTextSizeSelected() {
        return mTextSizeSelected;
    }

    public void setTextSizeSelected(float mTextSizeSelected) {
        this.mTextSizeSelected = mTextSizeSelected;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getTextColorSelected() {
        return mTextColorSelected;
    }

    public void setTextColorSelected(int mTextColorSelected) {
        this.mTextColorSelected = mTextColorSelected;
    }

    private void calculatePositionAndScroll() {
        int expectedPosition = Math.round(mAllVerticalScroll / mItemSmallHeight);
        if (expectedPosition == -1) {
            expectedPosition = 0;
        } else if (expectedPosition >= mRecyclerView.getAdapter().getItemCount() - 2) {
            expectedPosition = mRecyclerView.getAdapter().getItemCount() - 2;
            mAllVerticalScroll = Math.round(expectedPosition * mItemSmallHeight);
        }
        scrollListToPosition(expectedPosition);
    }

    private void scrollListToPosition(int expectedPosition) {
        float targetScrollPosDate = expectedPosition * mItemSmallHeight;
        final float missingPxDate = targetScrollPosDate - mAllVerticalScroll;
        if (missingPxDate != 0) {
            mRecyclerView.smoothScrollBy(0, (int) missingPxDate);
        }
        mNumberPickerAdapter.setSelectedItem(Math.round(mAllVerticalScroll / mItemSmallHeight) + 1);
    }
}
