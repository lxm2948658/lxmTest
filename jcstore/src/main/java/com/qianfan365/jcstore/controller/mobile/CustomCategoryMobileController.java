package com.qianfan365.jcstore.controller.mobile;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.permission.PermissionPassport;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.CustomCategory;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.CustomCategoryService;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by SZZ on 2017/3/7.
 * 定制商品类别相关接口
 */
@Controller
@RequestMapping("/mobile/customCategory")
public class CustomCategoryMobileController extends BaseController {

    @Autowired
    private CustomCategoryService customCategoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionInfoService permissionInfoService;

    /**
     * 新增
     *
     * @param customCategory
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @PermissionPassport(permissionids = {PermissionType.BESPOKE_PRODUCE_EDIT})
    @ModulePassport(moduleids = {ModuleType.BESPOKE_ORDER})
    public ResultData save(CustomCategory customCategory, HttpSession session) {
        // 参数校验 一级分类8个字符,二级分类10个字符,均不限制字符类型
        // 新增时 一级分类传递 name 更新时传递 name,id
        // 新增时 二级分类传递 name,pid 更新时传递 name,pid,id
        if (customCategory != null && StringUtils.isNotEmpty(customCategory.getName()) && (
            ((customCategory.getPid() == null || customCategory.getPid().equals(0))
                && customCategory.getName().length() <= 8) || (
                (customCategory.getPid() != null && !customCategory.getPid().equals(0))
                    && customCategory.getName().length() <= 10))) {
            // 执行业务逻辑
            customCategory.setBelongs(userService.getAdminID(getLoginUser(session)));
            customCategoryService.save(customCategory);
            return ResultData.build().success();
        }
        return ResultData.build().parameterError();
    }

    /**
     * 列表查询分类
     *
     * @param session
     * @param pid     为0时查询1级分类,其余查询对应二级分类
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ModulePassport(moduleids = {ModuleType.BESPOKE_ORDER})
    public ResultData list(HttpSession session, @RequestParam(defaultValue = "0") Integer pid) {
        User loginUser = getLoginUser(session);
        return ResultData.build().parseList(customCategoryService.listAll(userService.getAdminID(loginUser), pid))
        		.put("permissionInfo", permissionInfoService.findMapByUidPid(PermissionType.BESPOKE_PRODUCE_EDIT,
            		loginUser.getId(), loginUser.getBelongs()));
    }

    /**
     * 删除
     *
     * @param session
     * @param id      类别ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @PermissionPassport(permissionids = {PermissionType.BESPOKE_PRODUCE_EDIT})
    @ModulePassport(moduleids = {ModuleType.BESPOKE_ORDER})
    public ResultData delete(HttpSession session, Integer id) {
        if (id != null) {
            Integer belongs = userService.getAdminID(getLoginUser(session));
            customCategoryService.deleteByIds(belongs, id);
            return ResultData.build().success();
        }
        return ResultData.build().parameterError();
    }

}
