package zkhaider.com.cooleaf.ui.roundedpicture;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

import zkhaider.com.cooleaf.R;

/**
 * Created by Haider on 2/15/2015.
 */
public class LetterAvatar extends ColorDrawable {

    Paint paint = new Paint();
    Rect bounds = new Rect();

    String pLetters;

    private float ONE_DP = 0.0f;
    private Resources pResources;
    private int pPadding;
    int pSize = 0;
    float pMeasuredTextWidth;
    int pBoundsTextWidth;
    int pBoundsTextHeight;

    public LetterAvatar(Context context, int color, String letter, int paddingInDp) {
        super(color);
        this.pLetters = letter;
        this.pResources = context.getResources();
        ONE_DP = 1 * pResources.getDisplayMetrics().density;
        this.pPadding = Math.round(paddingInDp * ONE_DP);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        paint.setAntiAlias(true);

        do {
            paint.setTextSize(++pSize);
            paint.getTextBounds(pLetters, 0, pLetters.length(), bounds);
        } while ((bounds.height() < (canvas.getHeight() - pPadding))
                && (paint.measureText(pLetters) < (canvas.getWidth() - pPadding)));

        paint.setTextSize(pSize);
        pMeasuredTextWidth = paint.measureText(pLetters);
        pBoundsTextHeight = bounds.height();

        float xOffset = ((canvas.getWidth() - pMeasuredTextWidth) / 2);
        float yOffset = (pBoundsTextHeight + (canvas.getHeight() - pBoundsTextHeight) / 2);

        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        paint.setColor(android.R.color.white);
        canvas.drawText(pLetters, xOffset, yOffset, paint);
    }
}

