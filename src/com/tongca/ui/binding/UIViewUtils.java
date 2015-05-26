package com.tongca.ui.binding;

import android.text.TextUtils;
import android.view.View;

import com.tongca.ui.binding.annotations.UITextView;
import com.tongca.ui.binding.annotations.UIView;
import com.tongca.ui.binding.provider.IResourceProvider;

import java.lang.reflect.Field;

/**
 * @author Toan Nguyen Canh
 */
class UIViewUtils {
    /**
     * private constructor
     */
    private UIViewUtils() {

    }

    /**
     * find the view from root view and set it to the field of object
     *
     * @param object      the object holds view
     * @param field       the field of object will hold the view
     * @param root        the parent view
     * @param resProvider resources provider
     */
    public static <T> void bindView(T object, Field field, View root,
                                    IResourceProvider resProvider) {
        if (isExpose(field)) {
            View v = findView(field, root, resProvider);
            field.setAccessible(true);
            try {
                field.set(object, v);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isExpose(Field field) {
        return field.getAnnotation(UIView.class) != null
                || field.getAnnotation(UITextView.class) != null;
    }

    /**
     * find the view from root view
     *
     * @param field       the field describe view
     * @param root        the parent view
     * @param resProvider resources provider
     * @return the view if it is found, otherwise return null
     */
    private static View findView(Field field, View root,
                                 IResourceProvider resProvider) {
        View view = null;
        if (field.getAnnotation(UIView.class) != null) {
            UIView desc = field.getAnnotation(UIView.class);
            // find view by view id
            {
                int id = desc.value();
                if (id == 0)
                    id = desc.viewById();

                if (id != 0)
                    view = root.findViewById(id);
            }
            // find view by tag
            if (view == null) {
                String tag = desc.viewByTag();
                if (!TextUtils.isEmpty(tag))
                    view = root.findViewWithTag(tag);

            }
        }

        // find view by field name
        if (view == null) {
            String fieldName = field.getName();
            // find view with id or tag matches field name
            // id matches field name
            int autoId = resProvider.getIdentifierByName(fieldName);
            view = autoId == 0 ? null : root.findViewById(autoId);

            // tag matches field name
            if (view == null) {
                view = root.findViewWithTag(fieldName);
            }
        }
        return view;
    }
}
