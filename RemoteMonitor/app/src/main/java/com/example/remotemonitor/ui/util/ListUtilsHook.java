package com.example.remotemonitor.ui.util;

// 用来给ListUtils充当函数钩子的接口
public interface ListUtilsHook<T> {
    public boolean test(T t);
}
