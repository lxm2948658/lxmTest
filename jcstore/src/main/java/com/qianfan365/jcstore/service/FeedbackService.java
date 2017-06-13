package com.qianfan365.jcstore.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qianfan365.jcstore.common.bean.FeedbackBeen;
import com.qianfan365.jcstore.common.constant.FeedbackConstant.NetworkEnvironment;
import com.qianfan365.jcstore.common.pojo.Feedback;
import com.qianfan365.jcstore.common.pojo.FeedbackExample;
import com.qianfan365.jcstore.dao.inter.FeedbackMapper;

@Service
public class FeedbackService {

  @Autowired
  private FeedbackMapper feedbackMapper;

  /**
   * 列表查询意见反馈信息
   * 
   * @param currentPage
   * @param limit
   * @return
   */
  public Page<FeedbackBeen> list(Integer currentPage, Integer limit) {
    PageHelper.startPage(currentPage, limit);
    FeedbackExample example = new FeedbackExample();
    example.setOrderByClause(" createtime desc,id asc");
    Page<Feedback> list = (Page<Feedback>) this.feedbackMapper.selectByExample(example);
    return convertToBean(list);
  }

  /**
   * 转换为bean,用于给运营后台返回数据列表
   * 
   * @param list
   * @return
   */
  private Page<FeedbackBeen> convertToBean(Page<Feedback> list) {
    Page<FeedbackBeen> page = new Page<FeedbackBeen>();
    page.setPageNum(list.getPageNum());
    page.setPageSize(list.getPageSize());
    page.setTotal(list.getTotal());
    int i = 1;
    for (Feedback feedback : list) {
      Integer serialNumber = list.getPageSize() * (list.getPageNum() - 1);
      try {
        FeedbackBeen bean = new FeedbackBeen();
        BeanUtils.copyProperties(bean, feedback);
        bean.setSerialNumber(serialNumber + i++);
        if (feedback.getNetworkEnvironment().equals(NetworkEnvironment.TRAFFIC)) {
          bean.setNetworkEnvironment("数据流量");
        } else if (feedback.getNetworkEnvironment().equals(NetworkEnvironment.WIFI)) {
          bean.setNetworkEnvironment("wifi");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        bean.setCreatetime(format.format(feedback.getCreatetime()));
        page.add(bean);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return page;
  }

  /**
   * 新增
   * 
   * @param feedback
   */
  public void insert(Feedback feedback) {
    // 补充参数
    if (StringUtils.isEmpty(feedback.getPhoneModel())) {
      feedback.setPhoneModel("未知");
    }
    if (StringUtils.isEmpty(feedback.getSystemVersion())) {
      feedback.setSystemVersion("未知");
    }
    // 转换内容,替换全部换行符为空格
    feedback.setFeedbackContent(feedback.getFeedbackContent().replaceAll("(\r\n|\r|\n|\n\r)", " "));
    feedback.setCreatetime(new Date());
    this.feedbackMapper.insert(feedback);
  }

}
