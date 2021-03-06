package cafe.adriel.nmsalphabet.ui.util;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

public class TextViewClickMovement extends LinkMovementMethod {
    private final OnTextViewClickMovementListener mListener;
    private final GestureDetector mGestureDetector;
    private TextView mWidget;
    private Spannable mBuffer;

    public TextViewClickMovement(final Context context, final OnTextViewClickMovementListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new SimpleOnGestureListener());
    }

    @Override
    public boolean onTouchEvent(final TextView widget, final Spannable buffer, final MotionEvent event) {
        mWidget = widget;
        mBuffer = buffer;
        mGestureDetector.onTouchEvent(event);
        return false;
    }

    public enum LinkType {
        PHONE,
        WEB_URL,
        EMAIL_ADDRESS,
        NONE
    }

    public interface OnTextViewClickMovementListener {
        void onLinkClicked(final String linkText, final LinkType linkType);

        void onLongClick(final String text);
    }

    class SimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            final String linkText = getLinkText(mWidget, mBuffer, e);
            if (mListener != null) {
                mListener.onLongClick(linkText);
            }
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            final String linkText = getLinkText(mWidget, mBuffer, event);
            LinkType linkType = LinkType.NONE;

            if (Patterns.PHONE.matcher(linkText).matches()) {
                linkType = LinkType.PHONE;
            } else if (Patterns.WEB_URL.matcher(linkText).matches()) {
                linkType = LinkType.WEB_URL;
            } else if (Patterns.EMAIL_ADDRESS.matcher(linkText).matches()) {
                linkType = LinkType.EMAIL_ADDRESS;
            }

            if (mListener != null) {
                mListener.onLinkClicked(linkText, linkType);
            }

            return false;
        }

        private String getLinkText(final TextView widget, final Spannable buffer, final MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

            if (link.length != 0) {
                return buffer.subSequence(buffer.getSpanStart(link[0]),
                        buffer.getSpanEnd(link[0])).toString();
            }

            return "";
        }
    }
}