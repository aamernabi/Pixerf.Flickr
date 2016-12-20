package com.pixerf.flickr.utils.imageutils;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * created by Aamer on 12/16/2016.
 */

class MemoryCache {

    private static final String TAG = MemoryCache.class.getSimpleName();
    private Map<String, Bitmap> cache = Collections.synchronizedMap(
            new LinkedHashMap<String, Bitmap>(10, 1.5f, true));//Last argument true for LRU ordering
    private long size = 0; //current allocated size
    private long limit = 1000000; //max memory in bytes

    MemoryCache() {
        //use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    private void setLimit(long new_limit) {
        limit = new_limit;
        Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
    }

    Bitmap get(String id) {
        try {
            if (!cache.containsKey(id))
                return null;
            //NullPointerException sometimes happen here..
            return cache.get(id);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

    void put(String id, Bitmap bitmap) {
        try {
            if (cache.containsKey(id))
                size -= getSizeInBytes(cache.get(id));
            cache.put(id, bitmap);
            size += getSizeInBytes(bitmap);
            checkSize();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void checkSize() {
        Log.i(TAG, "cache size = " + size + ", length = " + cache.size());
        if (size > limit) {
            Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();//least recently accessed item will be the first one iterated
            while (iter.hasNext()) {
                Entry<String, Bitmap> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if (size <= limit)
                    break;
            }
            Log.i(TAG, "Clean cache. New size " + cache.size());
        }
    }

    void clear() {
        try {
            //NullPointerException sometimes happen here..
            cache.clear();
            size = 0;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage());
        }
    }

    private long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
