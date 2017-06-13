package com.qianfan365.jcstore.controller.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.qianfan365.jcstore.common.bean.MarketingBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.MarketingService;

@Controller
@RequestMapping("manage/marketing")
public class MarketingManageController extends BaseController {

  @Autowired
  private MarketingService marketingService;

  @ResponseBody
  @RequestMapping(value = "list", method = RequestMethod.GET)
  public ResultData list(Integer type, Integer orderType,
      @RequestParam(defaultValue = "1") Integer currentPage,
      @RequestParam(defaultValue = "20") Integer limit) {
    Page<MarketingBean> list = marketingService.list(currentPage,limit,type,orderType);
    return ResultData.build().parsePageBean(list);
  }

}
