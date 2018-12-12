package com.videogo.remoteplayback.list.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    public static <T> List<T> filter(List<T> list, ListUtilsHook<T> hook) {
        List<T> rtnList = new ArrayList<>();
        for (T t : list) {
            if (hook.test(t)) {
                rtnList.add(t);
            }
        }
        return rtnList;
    }
}