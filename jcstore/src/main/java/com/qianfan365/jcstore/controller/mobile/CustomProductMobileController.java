package com.qianfan365.jcstore.controller.mobile;

import com.google.common.collect.Lists;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.permission.PermissionPassport;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.CustomProduct;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.CustomProductService;
import com.qianfan365.jcstore.service.PermissionInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * Created by SZZ on 2017/3/7.
 * 定制商品接口
 */
@Controller
@RequestMapping("/mobile/customProduct")
public class CustomProductMobileController extends BaseController {

    @Autowired
    private CustomProductService customProductService;
    @Autowired
    private PermissionInfoService permissionInfoService;

    /**
     * 添加图片
     *
     * @param categoryId
     * @param images
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addPic", method = RequestMethod.POST)
    @PermissionPassport(permissionids = {PermissionType.BESPOKE_PRODUCE_EDIT})
    @ModulePassport(moduleids = {ModuleType.BESPOKE_ORDER})
    public ResultData addPic(Integer categoryId, String[] images) {
        if (categoryId != null && images != null && images.length <= 10) {
            return customProductService.addPic(categoryId, Arrays.asList(images));
        }
        return ResultData.build().parameterError();
    }

    /**
     * 列表查询
     *
     * @param categoryId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ModulePassport(moduleids = {ModuleType.BESPOKE_ORDER})
    public ResultData list(HttpSession session, Integer categoryId) {
        if (categoryId != null) {
        		User loginUser = getLoginUser(session);
            List<CustomProduct> customProducts =
                customProductService.listAllByCategoryId(categoryId);
            return ResultData.build().parseList(customProducts)
            		.put("permissionInfo", permissionInfoService.findMapByUidPids(
            		Lists.newArrayList(PermissionType.BESPOKE_PLACE_ORDER,PermissionType.BESPOKE_PRODUCE_EDIT),
            		loginUser.getId(), loginUser.getBelongs()));
        }
        return ResultData.build().parameterError();
    }

    /**
     * 为商品添加描述信息
     * @param id
     * @param description
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/description",method = RequestMethod.POST)
    @PermissionPassport(permissionids = {PermissionType.BESPOKE_PRODUCE_EDIT})
    @ModulePassport(moduleids = {ModuleType.BESPOKE_ORDER})
    public ResultData description(Integer id, String description) {
        if (id != null && description.length() <= 30) {
            customProductService.description(id,description);
            return ResultData.build().success();
        }
        return ResultData.build().parameterError();
    }

    /**
     * 批量移除
     * @param ids
     * @param categoryId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @PermissionPassport(permissionids = {PermissionType.BESPOKE_PRODUCE_EDIT})
    @ModulePassport(moduleids = {ModuleType.BESPOKE_ORDER})
    public ResultData delete(Integer[] ids,Integer categoryId){
        if(ids != null && ids.length != 0){
            // 批量删除
            customProductService.delete(ids,categoryId);
            return ResultData.build().success();
        }
        return ResultData.build().parameterError();
    }

}
