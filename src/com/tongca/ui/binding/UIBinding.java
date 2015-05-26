package com.tongca.ui.binding;

import android.app.Activity;
import android.view.View;

import com.tongca.ui.binding.provider.BaseResourceProvider;
import com.tongca.ui.binding.provider.BaseTextProvider;
import com.tongca.ui.binding.provider.IResourceProvider;
import com.tongca.ui.binding.provider.ITextProvider;

import java.lang.reflect.Field;

/**
 * @author Toan Nguyen Canh
 */
public class UIBinding {
    private static ITextProvider _textProvider;
    private static IResourceProvider _resProvider;
    
    /**
     * private constructor
     */
    private UIBinding() {
    }

    public static void setDebug(boolean isDebug) {
        UIConfig.DEBUG = isDebug;
    }

    public static boolean isDebug() {
        return UIConfig.DEBUG;
    }

    /**
     * set custom {@link ITextProvider}
     *
     * @param provider text provider to set
     */
    public static void setTextProvider(ITextProvider provider) {
        _textProvider = provider;
    }

    /**
     * set the {@link IResourceProvider}
     *
     * @param provider an instance of {@link IResourceProvider}
     */
    public static void setResourceProvider(IResourceProvider provider) {
        _resProvider = provider;
    }

    /**
     * bind views to the T object
     *
     * @param clazz    Class instances representing T object
     * @param object   the object will hold the content of root view
     * @param activity the current activity
     */
    public static <T> void bind(Class<T> clazz, T object, Activity activity) {
        View root = activity.getWindow().getDecorView()
                .findViewById(android.R.id.content);
        bind(clazz, object, root);
    }

    /**
     * bind views to the T object
     *
     * @param clazz  Class instances representing T object
     * @param object the object will hold the content of root view
     * @param root   the root view
     */
    public static <T> void bind(Class<T> clazz, T object, View root) {
        if (_textProvider == null) {
            _textProvider = new BaseTextProvider(root.getContext());
        }
        // update TextView (text style and text value)
        UITextViewUtils.updateTextViewFromXmlLayout(root, _textProvider);

        // initial view
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            bindView(object, field, root);
        }
    }

    /**
     * bind views to the T object
     *
     * @param object   the object will hold the content of root view
     * @param activity the current activity
     */
    public static <T> void bind(T object, Activity activity) {
        View root = activity.getWindow().getDecorView()
                .findViewById(android.R.id.content);
        bind(object, root);
    }

    /**
     * bind views to the T object
     *
     * @param object the object will hold the content of root view
     * @param root   the root view
     */
    public static <T> void bind(T object, View root) {
        if (_textProvider == null) {
            _textProvider = new BaseTextProvider(root.getContext());
        }

        // update TextView (text style and text value)
        UITextViewUtils.updateTextViewFromXmlLayout(root, _textProvider);

        // initial view
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            bindView(object, field, root);
        }
    }

    /**
     * bind views to the T object
     *
     * @param activity the current activity
     */
    public static void bind(Activity activity) {
        View root = activity.getWindow().getDecorView()
                .findViewById(android.R.id.content);
        bind(activity, root);
    }

    private static void bindView(Object object, Field field, View root) {
        try {
            if (_resProvider == null) {
                _resProvider = new BaseResourceProvider(root.getContext());
            }
            // find view from root view
            UIViewUtils.bindView(object, field, root, _resProvider);

            // update view from annotations
            UITextViewUtils.updateTextViewFromAnnotations(object, field, _textProvider);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
