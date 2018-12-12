package com.videogo.remoteplayback.list.utils;

// 用来给ListUtils充当函数钩子的接口
public interface ListUtilsHook<T> {
    public boolean test(T t);
}
