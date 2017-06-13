package com.qianfan365.jcstore.common.util;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Created by qianfanyanfa on 16/8/12.
 */
public class CommonUtils {
    /**
     * 生成指定位数的随机数
     * @param length
     * @return
     */
    public static Optional<String> generateNum(int length) {
        return Stream.generate(() -> String.valueOf(new Random().nextInt(10))).limit(length).reduce((x, y) -> {
            return x + y;
        });
    }
}
