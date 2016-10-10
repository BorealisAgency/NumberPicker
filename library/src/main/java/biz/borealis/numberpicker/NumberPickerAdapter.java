package biz.borealis.numberpicker;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NumberPickerAdapter extends RecyclerView.Adapter<NumberPickerAdapter.Holder> {

    private static final int VIEW_TYPE_PADDING = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private Context mContext;
    private int mMax, mMin;
    private float mItemBigHeight, mItemSmallHeight;
    private int selectedItem = -1;
    private float mTextSize, mTextSizeSelected;
    private boolean mAnimateTextSize, mTextFadeColor;

    public NumberPickerAdapter(Context context, int mMax, int mMin, float itemBigHeight, float itemSmallHeight) {
        this.mContext = context;
        this.mMax = mMax;
        this.mMin = mMin;
        this.mItemBigHeight = itemBigHeight;
        this.mItemSmallHeight = itemSmallHeight;
        this.mAnimateTextSize = context.getResources().getBoolean(R.bool.np_def_animate_text_size);
        this.mTextFadeColor = context.getResources().getBoolean(R.bool.np_def_fade_color);
    }

    @NonNull
    private static TextView getTextView(Context context, boolean isBig, float textSize, float textSizeSelected) {
        TextView number = new TextView(context);
        number.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        number.setGravity(Gravity.CENTER_HORIZONTAL);
        if (isBig) {
            number.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeSelected);
        } else {
            number.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        return number;
    }

    @NonNull
    private static TextView getTextView(Context context, float textSize, float textSizeSelected) {
        return getTextView(context, false, textSize, textSizeSelected);
    }

    public static int getTextViewHeight(Context context, boolean isBig, float textSize, float textSizeSelected) {
        TextView textView = getTextView(context, isBig, textSize, textSizeSelected);
        textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return textView.getMeasuredHeight();
    }

    @Override
    public NumberPickerAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            TextView number = getTextView(mContext, mTextSize, mTextSizeSelected);
            return new ItemHolder(number);
        } else {
            View paddingView = new View(mContext);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(Common.dp2px(mContext, 1), (int) mItemSmallHeight);
            paddingView.setLayoutParams(layoutParams);
            return new PaddingHolder(paddingView);
        }
    }

    @Override
    public void onBindViewHolder(NumberPickerAdapter.Holder holder, int position) {

        if (holder instanceof PaddingHolder) {
            PaddingHolder paddingHolder = (PaddingHolder) holder;
            ViewGroup.LayoutParams params = paddingHolder.itemView.getLayoutParams();
            if (position != 0) {
                params.height = (int) (mItemSmallHeight + mItemBigHeight - mItemSmallHeight);
            } else {
                params.height = (int) mItemSmallHeight;
            }
        }
        if (holder instanceof ItemHolder) {
            final ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.number.setText(String.valueOf(mMax - (mMax - mMin) + position - 1)); //minus padding view
            if (position == selectedItem) {
                if(mTextFadeColor){
                    final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), itemHolder.number.getCurrentTextColor(), ContextCompat.getColor(mContext, R.color.np_text_color_selected));
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            itemHolder.number.setTextColor((Integer) animator.getAnimatedValue());
                        }

                    });
                    colorAnimation.start();
                }
                if(mAnimateTextSize){
                    ValueAnimator textSizeAnimation = ValueAnimator.ofObject(new FloatEvaluator(), mTextSize, mTextSizeSelected);
                    textSizeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            itemHolder.number.setTextSize(TypedValue.COMPLEX_UNIT_PX, (Float) animator.getAnimatedValue());
                        }
                    });
                    textSizeAnimation.start();
                }

            } else {
                itemHolder.number.setTextColor(ContextCompat.getColor(mContext, R.color.np_text_color));
                itemHolder.number.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == getItemCount() - 1) {
            return VIEW_TYPE_PADDING;
        }
        return VIEW_TYPE_ITEM;
    }

    public void setSelectedItem(int selectedItem) {
        if (selectedItem != this.selectedItem) {
            this.selectedItem = selectedItem;
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return mMax - mMin + 3; // calculate number of items plus 2 padding
    }

    public void setTextSize(float textSize, float textSizeSelected) {
        mTextSize = textSize;
        mTextSizeSelected = textSizeSelected;
        notifyDataSetChanged();
    }

    public boolean isAnimateTextSize() {
        return mAnimateTextSize;
    }

    public void setAnimateTextSize(boolean mAnimateTextSize) {
        this.mAnimateTextSize = mAnimateTextSize;
    }

    public boolean isTextFadeColor() {
        return mTextFadeColor;
    }

    public void setTextFadeColor(boolean mTextFadeColor) {
        this.mTextFadeColor = mTextFadeColor;
    }

    private class PaddingHolder extends Holder {

        private PaddingHolder(View itemView) {
            super(itemView);

        }
    }

    private class ItemHolder extends Holder {
        private TextView number;

        private ItemHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView;
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        private Holder(View itemView) {
            super(itemView);
        }
    }
}
