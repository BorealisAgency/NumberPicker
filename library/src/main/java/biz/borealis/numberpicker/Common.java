package biz.borealis.numberpicker;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by kristijan.draca@borealis.biz
 */
public class Common {
    public static int dp2px(Context context, int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }
}
