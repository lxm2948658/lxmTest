package com.qianfan365.jcstore.controller.mobile;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.Client;
import com.qianfan365.jcstore.common.pojo.Feedback;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.ClientService;
import com.qianfan365.jcstore.service.FeedbackService;

/**
 * 意见反馈部分移动端接口
 * 
 * @author szz
 *
 */
@Controller
@RequestMapping("/mobile/feedback")
public class FeedbackMobileController extends BaseController {

  @Autowired
  private FeedbackService feedbackService;

  @Autowired
  private ClientService clientService;

  /**
   * 新增意见反馈
   * @param feedback
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(method = RequestMethod.POST, value = "/save")
  public ResultData save(Feedback feedback, HttpSession session) {
    // 手机号码11位数字
    String mobileRegex = "^[0-9]{11}$";
    // 参数校验
    if (feedback != null
        && StringUtils.isNotEmpty(feedback.getFeedbackContent())
        && (feedback != null && (feedback.getNetworkEnvironment() == 1 || feedback
            .getNetworkEnvironment() == 2))
        && feedback.getFeedbackContent().length() <= 200
        && (StringUtils.isEmpty(feedback.getPhoneNumber()) || (StringUtils.isNotEmpty(feedback
            .getPhoneNumber()) && feedback.getPhoneNumber().matches(mobileRegex)))) {
      // 获取当前登录的用户
      User user = this.getLoginUser(session);
      if (user != null) {
        feedback.setUsername(user.getUsername());
        // 获取对应后台账户信息
        Client client =
            clientService.getByUID(user.getBelongs() == 0 ? user.getId() : user.getBelongs());
        feedback.setCompanyName(client.getCompanyName());
      }
      this.feedbackService.insert(feedback);
      return ResultData.build().put("feedback", feedback);
    }
    return ResultData.build().parameterError();
  }

}
