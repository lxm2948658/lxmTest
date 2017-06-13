package com.qianfan365.jcstore.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * Created by qianfanyanfa on 16/3/22. 关于金钱转换相关的代码
 */
public class AmountUtil {

    /**
     * 金额为分的格式
     */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    /**
     * 元转换成分
     *
     * @param amountStr 元字符串
     * @return int类型的分
     */
    public static BigInteger yuan2Fen(String amountStr) {

        int index = amountStr.indexOf(".");
        int length = amountStr.length();
        BigInteger fen = new BigInteger("0");
        if (index == -1) {
            fen = new BigInteger(amountStr + "00");
        } else if (length - index >= 3) {
            fen = new BigInteger((amountStr.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            fen = new BigInteger((amountStr.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            fen = new BigInteger((amountStr.substring(0, index + 1)).replace(".", "") + "00");
        }
        return fen;
    }

    public static BigInteger yuan2Fen(Double amount) {
        DecimalFormat myformat = new DecimalFormat("0.00");
        return yuan2Fen(myformat.format(amount));
    }

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String fen2Yuan(String amount) {

        DecimalFormat myformat = new DecimalFormat("0.00");
        return myformat.format(BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)));
    }


}
