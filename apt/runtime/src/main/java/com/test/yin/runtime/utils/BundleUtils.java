package com.test.yin.runtime.utils;

import android.os.Bundle;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/7/7.
 */
public class BundleUtils {

    public static <T> T get(Bundle bundle, String key) {
        return (T)bundle.get(key);
    }

    public static <T> T get(Bundle bundle, String key, Object defaultValue) {
        Object object = bundle.get(key);
        if (object == null) {
            object = defaultValue;
        }
        return (T) object;
    }

}
