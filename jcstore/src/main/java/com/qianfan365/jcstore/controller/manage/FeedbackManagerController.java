package com.qianfan365.jcstore.controller.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.qianfan365.jcstore.common.bean.FeedbackBeen;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.FeedbackService;
/**
 * 意见反馈部分后台接口
 * @author szz
 *
 */
@Controller
@RequestMapping("/manage/feedback")
public class FeedbackManagerController extends BaseController {

  @Autowired
  private FeedbackService feedbackService;

  /**
   * 列表查询
   * @param currentPage
   * @param limit
   * @return
   */
  @ResponseBody
  @RequestMapping(method = RequestMethod.GET,value = "/list")
  public ResultData list(@RequestParam(defaultValue = "1") Integer currentPage,
      @RequestParam(defaultValue = "20") Integer limit) {
    Page<FeedbackBeen> page = this.feedbackService.list(currentPage,limit);
    return ResultData.build().parsePageBean(page);
  }

}
