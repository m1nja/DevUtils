package afkt.project.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * detail: Base Button
 * @author Ttt
 * <pre>
 *     便于全局控制, 替换字体、样式等
 * </pre>
 */
public class BaseButton extends AppCompatButton {

    public BaseButton(Context context) {
        super(context);
    }

    public BaseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
