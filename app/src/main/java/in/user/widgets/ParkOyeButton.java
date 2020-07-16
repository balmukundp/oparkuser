package in.user.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import in.user.R;


@SuppressWarnings("ALL")
public class ParkOyeButton extends Button {

    private Context mContext;

    public ParkOyeButton(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ParkOyeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setAttributeSet(attrs);
        init();
    }

    public ParkOyeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setAttributeSet(attrs);
        init();
    }

    public ParkOyeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void setAttributeSet(AttributeSet attrs)
    {
       /* if(attrs!=null) {
            TypedArray attributeValue = mContext.obtainStyledAttributes(attrs, R.styleable.EyeFindTextStyle);
            try {
                boolean isFontStyleRegular = attributeValue.getBoolean(R.styleable.EyeFindTextStyle_fontStyleRegular, true);

            }finally {
                attributeValue.recycle();
            }
        }
*/
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

        ParkOyeFont mtribeFont = ParkOyeFont.getTribeFontInstance(mContext);
        setTypeface(mtribeFont.getTribeRegularTypeFace());


    }
}
