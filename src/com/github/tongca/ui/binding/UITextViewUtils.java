package com.github.tongca.ui.binding;

import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.tongca.ui.binding.annotations.UITextView;
import com.github.tongca.ui.binding.provider.ITextProvider;

import java.lang.reflect.Field;
import java.util.Map;

public class UITextViewUtils {
    public static final String ATTR_TEXT_KEY = "textKey";
    public static final String ATTR_TEXT_HINT = "textHint";
    public static final String ATTR_TEXT_STYLE = "textStyle";

    /**
     * private constructor, prevent any instance is created outside this class
     */
    private UITextViewUtils() {
    }

    /**
     * @param object
     * @param field
     * @param provider
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static void updateTextViewFromAnnotations(Object object, Field field,
                                                     ITextProvider provider)
            throws IllegalAccessException,
            IllegalArgumentException {

        UITextView desc = field.getAnnotation(UITextView.class);
        if (desc != null) {
            // update view
            field.setAccessible(true);
            Object v = field.get(object);
            if (v instanceof TextView) {
                ((TextView) v).setText(findText(desc, provider));
            } else if (UIConfig.DEBUG) {
                throw new RuntimeException(field.getName()
                        + " isn't an instance of "
                        + TextView.class.getSimpleName());
            }
        }
    }

    private static CharSequence findText(UITextView desc, ITextProvider provider) {
        CharSequence text = null;
        String textKey = desc.textId();

        if (!TextUtils.isEmpty(textKey))
            text = provider.getText(textKey);
        return text;
    }

    public static void updateTextViewFromXmlLayout(View view,
                                                   final ITextProvider provider) {
        if (view == null) {
            return;
        }
        if (view instanceof TextView) {
            updateTextViewFromXmlResources((TextView) view, provider);
        } else if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            int child = parent.getChildCount();
            for (int i = 0; i < child; i++) {
                updateTextViewFromXmlLayout(parent.getChildAt(i), provider);
            }
        }
    }

    /**
     * This method sets attributes which will be provided by
     * {@link com.github.tongca.ui.binding.provider.ITextProvider} to textView
     *
     * @param textView TextView object
     * @param provider resources provider
     */
    private static void updateTextViewFromXmlResources(TextView textView,
                                                       ITextProvider provider) {
        Map<String, String> attrs = provider.getTextDescription(textView);
        // update text key
        if (!TextUtils.isEmpty(attrs.get(ATTR_TEXT_KEY))) {
            CharSequence text = provider.getText(attrs.get(ATTR_TEXT_KEY));
            if (TextUtils.isEmpty(text)) {
                textView.setText("");
            } else {
                textView.setText(text);
            }
        }

        // update text hint
        if (!TextUtils.isEmpty(attrs.get(ATTR_TEXT_HINT))) {
            CharSequence text = provider.getText(attrs.get(ATTR_TEXT_KEY));
            if (!TextUtils.isEmpty(text)) {
                textView.setHint(text);
            }
        }

        // update type face
        Typeface tf = null;
        if (!TextUtils.isEmpty(attrs.get(ATTR_TEXT_STYLE))) {
            tf = provider.getTypeface(attrs.get(ATTR_TEXT_STYLE));
        }
        // override Typeface of text view if the new Typeface is available
        if (tf != null) {
            textView.setTypeface(tf);
        }
    }
}
