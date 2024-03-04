package com.sheldon.devlab.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName AlgorithmUtilsTest
 * @Author sheldon
 * @Date 2024/2/26 16:39
 * @Version 1.0
 * @Description TODO
 */
class AlgorithmUtilsTest {

    @Test
    void minDistance() {
        List<String> list01 = Arrays.asList("Java", "大二", "男");
        List<String> list02 = Arrays.asList("Java", "大三", "男");
        List<String> list03 = Arrays.asList("Python", "大二", "女");

        int i01 = AlgorithmUtils.minDistance(list01, list02);
        int i02 = AlgorithmUtils.minDistance(list01, list03);
        System.out.println(i01);
        System.out.println(i02);
    }
}