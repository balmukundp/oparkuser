package in.user.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import in.user.R;


@SuppressWarnings("ALL")
public class ParkOyeTextView extends TextView {

    private final Context mContext;

    public ParkOyeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setAttributeSet(attrs);
        init();
    }

    public ParkOyeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setAttributeSet(attrs);
        init();
    }

    public ParkOyeTextView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void setAttributeSet(AttributeSet attrs)
    {
        if(attrs!=null) {
            TypedArray attributeValue = mContext.obtainStyledAttributes(attrs, R.styleable.ParkOyeTextStyle);
            try {
                boolean isFontStyleRegular = attributeValue.getBoolean(R.styleable.ParkOyeTextStyle_customFont, true);
            }finally {
                attributeValue.recycle();
            }
        }
    }

    private void init() {
        ParkOyeFont parkOyeFont = ParkOyeFont.getTribeFontInstance(mContext);
        setTypeface(parkOyeFont.getTribeRegularTypeFace());


    }

}
