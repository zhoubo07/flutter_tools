package com.dengyun.baselibrary.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @Title List相关的工具方法
 * @Author: zhoubo
 * @CreateDate: 2019/1/24 4:14 PM
 */
public class ListUtils {
    public static boolean isEmpty(List list) {
        if (list != null && list.size() > 0) {
            return false;
        }
        return true;
    }

    public static boolean isAllEmpty(List... lists) {
        for (int i = 0; i < lists.length; i++) {
            if (!isEmpty(lists[i])) {
                return false;
            }
        }
        return true;
    }

    public static String listToString(List<String> list, String separator) {
        if (isEmpty(list)) return "";
        StringBuilder sbd = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sbd.append(list.get(i));
            if (i != list.size() - 1) {
                sbd.append(separator);
            }
        }
        return sbd.toString();
    }

    public static List intArrayToList(int[] protocols) {
        int size = protocols.length;
        Integer[] array = new Integer[size];
        for (int i = 0; i < protocols.length; i++) {
            Integer integer = protocols[i];
            array[i] = integer;
        }
        return Arrays.asList(array);
    }

    /**
     * List 交换位置
     */
    public static <T> void swap(List<T> list, int index1, int index2) {
        T temp = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, temp);
    }

    /**
     * List 移动条目
     * @param list  待移动的List
     * @param fromIndex 等待移动的条目下标
     * @param toIndex   将要移动到的位置（原始位置）
     */
    public static <T> void move(List<T> list, int fromIndex, int toIndex) {
        if (fromIndex < 0) fromIndex = 0;
        if (fromIndex > list.size()-1) fromIndex = list.size()-1;

        T temp = list.get(fromIndex);
        list.remove(fromIndex);
        if (fromIndex<toIndex){
            // 下移
            list.add(toIndex-1,temp);
        }else {
            // 上移
            list.add(toIndex,temp);
        }
    }

    /**
     * List中的某个条目置顶
     */
    public static <T> void moveToTop(List<T> list, int fromIndex) {
        move(list,fromIndex,0);
    }
}
