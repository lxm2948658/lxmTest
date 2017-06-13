/**
 * 
 */
package com.qianfan365.jcstore.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;

/**
 * 抽奖工具
 * 
 * @author wpy
 * @package_name com.qianfan365.jcstore.common.util
 * @time 2017年6月7日 - 下午1:49:24
 */
public class LotteryUtils {
	/**
	 * 未中奖
	 */
	static final public Integer LOSING_LOTTERY = new Integer( 0 );
	/**
	 * 所有号码
	 */
	private List<Integer>			mainList;
	/**
	 * 中奖概率
	 */
	private Integer					probalility;
	/**
	 * 中奖号码
	 */
	private Map<String, Integer>	prizeMap;
	/**
	 * 奖品等级
	 */
	private Integer					prizeLevel;

	/**
	 * 已中奖的奖品等级
	 */
	private Integer					winPrizeLevel;

	/**
	 * 是否已经中奖
	 */
	private Boolean					isWinning;

	/**
	 * 带奖品的
	 * 
	 * @param probalility
	 * @param prizeLevel
	 */
	public LotteryUtils(Integer probalility, Integer prizeLevel) {
		this.probalility = probalility;
		this.prizeLevel = prizeLevel;
		mainList = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			mainList.add( i );
		}
	}

	/**
	 * 只有概率的
	 * 
	 * @param probalility
	 */
	public LotteryUtils(Integer probalility) {
		this( probalility, null );

	}

	/**
	 * 是否已经中奖
	 * 
	 * @return
	 */
	public boolean isWinning() {
		if (isWinning != null) {
			return isWinning;
		}
		prizeMap = new HashMap<>();
		// 打乱顺序
		Collections.shuffle( mainList );

		// 抽取中奖号码
		for (int i = 0; i < probalility; i++) {
			int winningNumber = RandomUtils.nextInt( mainList.size() );
			prizeMap.put( mainList.get( winningNumber ).toString(), mainList.get( winningNumber ) );
			mainList.remove( winningNumber );
		}
		// 用户抽取的号码是否在中奖号码里头
		int consumerNumber = RandomUtils.nextInt( 100 );
		if (prizeMap.get( "" + consumerNumber ) != null) {
			isWinning = true;
			return true;
		}
		isWinning = false;
		return false;
	}

	/**
	 * 抽中了几等奖，没抽中和没设奖品等级时返回0
	 * 
	 * @return
	 */
	public Integer winningPrizeLevel() {
		if (winPrizeLevel != null) {
			return winPrizeLevel;
		}
		if (!isWinning()) {
			return LOSING_LOTTERY;
		}
		if (prizeLevel == null) {
			return LOSING_LOTTERY;
		}
		return RandomUtils.nextInt( prizeLevel ) + 1;
	}

	public static void main(String[] args) {
		/*for (int i = 0; i < 10; i++) {
			LotteryUtils lotteryUtils20 = new LotteryUtils( 20 );
			System.out.println( "是否已中奖：" + (lotteryUtils20.isWinning() ? "是" : "否") );

		}*/
		for (int i = 0; i < 10; i++) {
			LotteryUtils lotteryUtils10 = new LotteryUtils(100,3 );
			if (lotteryUtils10.winningPrizeLevel().equals( 0 )) {
				System.out.println( "未中奖" );
			} else {
				System.out.println( "中了" + lotteryUtils10.winningPrizeLevel() + "等奖" );
			}
		}
	}

}
