package com.qianfan365.jcstore.controller.mobile;

import com.qianfan365.jcstore.common.bean.ProductBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.permission.PermissionPassport;
import com.qianfan365.jcstore.common.check.ProductCheck;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.constant.ProductStatusConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.pojo.Product;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.ProductGroupService;
import com.qianfan365.jcstore.service.ProductService;
import com.qianfan365.jcstore.service.ShopService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

import java.util.*;

/**
 * 商品相关
 *
 * @author guanliyu
 */
@Controller
@RequestMapping("/mobile/product")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private PermissionInfoService permissionInfoService;
    @Autowired
    private ProductGroupService productGroupService;

    /**
     * 添加商品修改商品(1.0期)
     *
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @PermissionPassport(permissionids = {PermissionType.PRODUCE_EDIT})
    @ResponseBody
    @Deprecated
    public ResultData addProduct(HttpSession session, Product product) {
        User user = getLoginUser(session);
        ResultData resultData = ResultData.build();
        if (ProductCheck.saveProductCheck(product)) {
            return resultData.parameterError();
        }
        Integer userId;
        if (product.getGroupId() != null) {
            if (user.getBelongs() == 0) {
                userId = user.getId();
            } else {
                userId = user.getBelongs();
            }
            if (productGroupService.findByIdAndUid(product.getGroupId(), userId)) {// 判断分组是否分组
                return resultData.failure();
            }
        }

        int status =
            productService.addProduct(product, user.getId(), user.getBelongs(), shopService.findShop(user).getId());
        if (status == -1) { // 不是本店的商品
            return resultData.parameterError();
        } else {
            return resultData.setStatus(status);
        }
    }

    /**
     * 添加商品修改商品(1.1期,新增条形码功能)
     *
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "saveProduct", method = RequestMethod.POST)
    @PermissionPassport(permissionids = {PermissionType.PRODUCE_EDIT})
    @ResponseBody
    @ModulePassport(moduleids = {ModuleType.PRODUCT}, setuser = true)
    public ResultData add(HttpSession session, Product product) {
        User user = getLoginUser(session);
        ResultData resultData = ResultData.build();
        // 校验试用账号
        if (user.getIsTrialAccount() && productService.checkTrialProductNum(user.getId())) {
            return resultData.setStatus(Status.TRIAL_ACCOUNT_PRODUCT_NUM_BEYOND);
        }
        // 参数校验
        if (ProductCheck.saveProductCheck(product)) {
            return resultData.parameterError();
        }
        Integer userId;
        // 商品分组部分
        if (product.getGroupId() != null) {
            if (!SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.PRODUCT_GROUP)) {
                return ResultData.build().setStatus(Status.MODULE_CHANGED);
            }
            // 获取店主ID
            if (user.getBelongs() == 0) {
                userId = user.getId();
            } else {
                userId = user.getBelongs();
            }
            // 校验所使用的商品分组ID是否属于当前用户
            if (productGroupService.findByIdAndUid(product.getGroupId(), userId)) {
                return resultData.failure();
            }
        }
        int status;
        // 更新或新增逻辑
        synchronized (ProductController.class) {
            // 校验条形码是否重复
            Integer shopId = shopService.findShop(user).getId();
            String barCode = product.getBarCode();
            String regex = "^[A-Za-z0-9]{1,20}$";
            if (StringUtils.isNotEmpty(barCode)) {
                if (barCode.matches(regex)) {
                    boolean checkBarCode =
                        this.productService.checkBarCode(product.getBarCode(), product.getId(), shopId);
                    // 条形码重复时返回状态
                    if (!checkBarCode) {
                        return resultData.setStatus(Status.PRODUCT_BARCODE_EXIST);
                    }
                } else {
                    return resultData.parameterError();
                }
            }
            // 添加商品
            status = productService.add(product, user.getId(), user.getBelongs(), shopId);
        }
        if (status == -1) { // 不是本店的商品
            return resultData.parameterError();
        } else {
            return resultData.setStatus(status).put("product", new ProductBean(product));
        }
    }

    /**
     * 删除商品
     *
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "del", method = RequestMethod.POST)
    @PermissionPassport(permissionids = {PermissionType.PRODUCE_DEL})
    @ResponseBody
    @ModulePassport(moduleids = {ModuleType.PRODUCT})
    public ResultData delProduct(HttpSession session, Integer id) {
        User user = getLoginUser(session);
        ResultData resultData = ResultData.build();
        if (user.getBelongs() == 0) {// 店长身份
            List<Integer> users = listUserId(user);
            if (productService.delProduct(id, users) == 1) {
                return resultData.success();
            } else {
                return resultData.failure();
            }
        }
        if (permissionInfoService.findByUidPid(PermissionType.PRODUCE_DEL, user.getId())
            != null) {// 如果有商品删除权限则查询到是否有符合的商品然后在删除
            List<Integer> users = listPermissionUserId(user);
            if (productService.delProduct(id, users) == 1) {
                return resultData.success();
            } else {
                return resultData.failure();
            }
        } else {
            if (productService.delProduct(id, Arrays.asList(user.getId())) == 1) {
                return resultData.success();
            } else {
                return resultData.failure();
            }
        }
    }

    /**
     * 修改商品
     *
     * @param session
     * @param product
     * @return
     */
    // @RequestMapping(value = "update", method = RequestMethod.POST)
    // @ResponseBody
    // public ResultData updateProduct(HttpSession session, Product product) {
    // User user = getLoginUser(session);
    // ResultData resultData = ResultData.build();
    // if (productService.updateProduct(product, user.getId()) == 1) {
    // return resultData.success();
    // } else {
    // return resultData.failure();
    // }
    // }

    /**
     * 分组查询所有商品列表
     *
     * @param session
     * @param currentPage
     * @param limit
     * @param product
     * @return
     */
    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
    @ModulePassport(moduleids = {ModuleType.PRODUCT})
    public ResultData findAllProduct(HttpSession session, @RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "20") Integer limit, Integer groupId) {
        User user = getLoginUser(session);
        if (user.getBelongs() == 0) {// 店长身份
            List<Integer> users = listUserId(user);
            Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
            map.put(PermissionType.PRODUCE_EDIT, true);
            map.put(PermissionType.PRODUCE_DEL, true);
            return ResultData.build().parsePageBean(productService.findByUidProduct(currentPage, limit, users, groupId)).put("permissionInfo", map);
        } else {
            List<Integer> users = listPermissionUserId(user);
            return ResultData.build().parsePageBean(productService.findByUidProduct(currentPage, limit, users, groupId)).put("permissionInfo", permissionInfoService.findMapByUidPids(Arrays.asList(PermissionType.PRODUCE_EDIT, PermissionType.PRODUCE_DEL), user.getId()));
        }

    }

    /**
     * 根据ID查询单个商品明细
     *
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @ResponseBody
    @ModulePassport(moduleids = {ModuleType.PRODUCT}, setuser = true)
    public ResultData findDetailProduct(HttpSession session, Integer id) {
        // 获取当前登陆的用户
        User user = getLoginUser(session);
        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
        List<Integer> usersId = new ArrayList<Integer>();
        // 当前登录为管理员时
        if (user.getBelongs() == 0) {
            // 获取旗下所有员工的ID
            usersId = listUserId(user);
            // 封装权限信息
            map.put(PermissionType.PRODUCE_EDIT, true);
            map.put(PermissionType.PRODUCE_DEL, true);
            map.put(PermissionType.VIEW_CORPUS, true);
        } else {
            // 登录状态为普通用户时
            usersId = listPermissionUserId(user);
            // 封装权限信息
            List<Integer> plist = Arrays.asList(PermissionType.PRODUCE_EDIT,
            		PermissionType.PRODUCE_DEL, PermissionType.VIEW_CORPUS);
            map = permissionInfoService.findMapByUidPids(plist, user.getId());
        }
        // 根据商品ID及用户ID查询商品
        Product product = productService.findByPidProduct(id, usersId);
        if (product == null) {
            return ResultData.build().setStatus(Status.PRODUCE_DEL);
        }

        if (!map.get(PermissionType.VIEW_CORPUS)) { // 没有权限成本价置空
            product.setCostPrice(null);
        }
        ProductBean bean = new ProductBean(product);
        boolean groupModule =
            SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.PRODUCT_GROUP);
        bean.setGroupFlag(groupModule);
        if (groupModule && product.getGroupId() != null) {
            bean.setGroupName(productGroupService.findById(product.getGroupId()).getName());
        }
        return ResultData.build().put("product", bean).put("permissionInfo", map);
    }

    /**
     * 根据商品名称查询商品列表(1.1期) 条件查询商品(全部整合至这一个接口)
     *
     * @param session
     * @param currentPage
     * @param limit
     * @param name
     * @param groupId
     * @param orderType
     * @return
     */
    @RequestMapping(value = "find", method = RequestMethod.GET)
    @ResponseBody
    @ModulePassport(moduleids = {ModuleType.PRODUCT}, setuser = true)
    public ResultData findProduct(HttpSession session, 
         @RequestParam(defaultValue = "1") Integer currentPage,
         @RequestParam(defaultValue = "20") Integer limit, 
         @RequestParam(defaultValue = "0") Integer orderType, 
         String name, Integer groupId, Long querytime) {
      if (1 == currentPage) {
        querytime = new Date().getTime();
      }
      // 获取当前登录用户信息
      User user = getLoginUser(session);
      boolean groupModule =
          SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.PRODUCT_GROUP);
      //如果不勾选商品分组模块，则商品管理里不显示按分组筛选
      if (groupId != null && !groupModule) {
        return ResultData.build().setStatus(Status.MODULE_CHANGED);
      }
      
      Integer shopId = shopService.findShop(user).getId();
      Map<Integer, Boolean> map;
      // 登录用户为管理员时
      if (user.getBelongs() == 0) {
        // 保存权限信息
        map = new HashMap<Integer, Boolean>();
        map.put(PermissionType.PRODUCE_EDIT, true);
        map.put(PermissionType.PRODUCE_DEL, true);
        // 查询并返回权限信息
      } else {
        map = permissionInfoService.findMapByUidPids(Arrays.asList(PermissionType.PRODUCE_EDIT, PermissionType.PRODUCE_DEL), user.getId());
      }
      return ResultData.build().put("group", groupModule).put("permissionInfo", map)
          .put("querytime", querytime).parseList(productService
            .find(currentPage, limit, name, shopId, groupId, orderType, querytime));

    }

    /**
     * 根据名字以及UID查询明细(1.0期)
     *
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "findname", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    public ResultData findNameProduct(HttpSession session, @RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "20") Integer limit, String name) {
        User user = getLoginUser(session);
        if (user.getBelongs() == 0) {// 店长身份
            List<Integer> users = listUserId(user);
            Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
            map.put(PermissionType.PRODUCE_EDIT, true);
            map.put(PermissionType.PRODUCE_DEL, true);
            return ResultData.build().parseList(productService.findByName(currentPage, limit, name, users)).put("permissionInfo", map);
        } else {
            List<Integer> users = listPermissionUserId(user);
            return ResultData.build().parseList(productService.findByName(currentPage, limit, name, users)).put("permissionInfo", permissionInfoService.findMapByUidPids(Arrays.asList(PermissionType.PRODUCE_EDIT, PermissionType.PRODUCE_DEL), user.getId()));
        }

    }

    /**
     * 根据分组查询商品列表
     *
     * @param session
     * @param currentPage
     * @param limit
     * @param groupId
     * @return
     */
    @RequestMapping(value = "group", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    public ResultData groupFind(HttpSession session, @RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "20") Integer limit, Integer groupId) {
        return ResultData.build().parseList(productService.findGroupProduct(currentPage, limit, groupId));

    }


    /**
     * 查询所有商品
     *
     * @param session
     * @param currentPage
     * @param limit
     * @param name
     * @return
     */
    // @RequestMapping(value = "all", method = RequestMethod.GET)
    // @ResponseBody
    // public ResultData findallProduct(HttpSession session,
    // @RequestParam(defaultValue = "1") Integer currentPage,
    // @RequestParam(defaultValue = "20") Integer limit, String name) {
    // return ResultData.build().parseList(productService.findAllProduct(currentPage, limit));
    // }

    /**
     * 根据条形码获取对应的商品
     *
     * @param barCode
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/findByBarCode")
    public ResultData findByBarCode(String barCode, HttpSession session) {
        String regex = "^[A-Za-z0-9]{1,20}$";
        // 参数校验
        if (StringUtils.isEmpty(barCode) || !barCode.matches(regex)) {
            return ResultData.build().parameterError();
        }
        User user = this.getLoginUser(session);
//        List<Integer> userIds = this.listUserId(user);
        // 查询
        Product product = this.productService.findByBarCode(barCode, shopService.findShop(user).getId());
        if (product != null) {
            ProductBean bean = new ProductBean(product);
            if (product.getGroupId() != null) {
                bean.setGroupName(productGroupService.findById(product.getGroupId()).getName());
            }
            return ResultData.build().put("product", bean);
        } else {
            return ResultData.build().data404();
        }
    }

    /**
     * 刷新库存
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ResultData refresh(Integer[] ids, HttpSession session) {
      User user = getLoginUser(session);
      Integer shopId = shopService.findShop(user).getId();
      // 参数校验
      if (ids == null || ids.length == 0) {
          return ResultData.build().parameterError();
      }
      // 查询列表
      List<Product> list = this.productService.findByIds(Arrays.asList(ids), shopId);
      return ResultData.build().put("aaData", list);
    }

    /**
     * 体验账号添加商品校验
     *
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "trialNum", method = RequestMethod.GET)
    public ResultData checkTrialProductNum(HttpSession session) {
        User user = this.getLoginUser(session);
        boolean check = this.productService.checkTrialProductNum(user.getId());
        if (user.getIsTrialAccount() && check) {
            return ResultData.build().setStatus(Status.TRIAL_ACCOUNT_PRODUCT_NUM_BEYOND);
        }
        return ResultData.build().success();
    }

    /**
     * 添加客户案例
     *
     * @param productId
     * @param images
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "case", method = RequestMethod.POST)
    public ResultData saveCustomerCase(Integer productId, String[] images, HttpSession session) {
        if (productId != null) {
            return productService.saveCustomerCase(productId, images, getLoginUser(session));
        }
        return ResultData.build().parameterError();
    }

    /**
     * 获取客户案例列表
     *
     * @param productId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listCase", method = RequestMethod.GET)
    public ResultData listCase(Integer productId) {
        if (productId != null) {
            Product product = productService.findById(productId);
            if (product.getStatus().equals(ProductStatusConstant.Yes)
                && StringUtils.isNotEmpty(product.getCustomerCase())) {
                return ResultData.build().parseList(Arrays.asList(product.getCustomerCase().split(",")));
            }
            return ResultData.build().parseList(null);
        }
        return ResultData.build().parameterError();
    }

}
