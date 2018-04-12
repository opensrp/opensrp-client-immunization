package org.smartregister.immunization.sample.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.smartregister.immunization.sample.R;

/**
 * Created by vkaruri on 4/9/2018.
 */
public class RoundedImageView extends  ImageView {
    public RoundedImageView(Context context) {
        super(context);
    }
    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ViewGroup parentLayout =   ((ViewGroup) this.getParent());
        int imageViewBackgroundColor = getImageViewBackgroundColor(parentLayout);
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.profile_image_sample_59);
        Bitmap foreground = createForegroundBitmap(background, imageViewBackgroundColor);
        foreground = punchAHoleInABitmap(foreground, background);
        this.setImageBitmap(combineTwoBitmaps(background,foreground));
    }

    private int getImageViewBackgroundColor(ViewGroup viewGroup) {
        int color = Color.TRANSPARENT;
        Drawable background = viewGroup.getBackground();
        if (background instanceof ColorDrawable) {
            color = ((ColorDrawable) background).getColor();
        }
        return color;
    }

    private Bitmap createForegroundBitmap(Bitmap background, int color) {
        Bitmap bitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(),
                background.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect((float) 0, (float) 0, (float) background.getWidth(),
                (float) background.getHeight(), paint);
        return bitmap;
    }

    private Bitmap punchAHoleInABitmap(Bitmap foreground, Bitmap background) {
        Bitmap bitmap = Bitmap.createBitmap(foreground.getWidth(), foreground.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(foreground, 0, 0, paint);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        float radius = (float)(background.getWidth()  * 0.49);
        float x = (float) (background.getWidth() * 0.5);
        float y = (float)  (background.getWidth() * 0.5);
        canvas.drawCircle(x, y, radius, paint);
        return bitmap;
    }

    private Bitmap combineTwoBitmaps(Bitmap background, Bitmap foreground) {
        Bitmap combinedBitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(), background.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(foreground, 0, 0, paint);
        return combinedBitmap;
    }
}