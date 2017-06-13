package com.qianfan365.jcstore.controller.mobile;

import com.github.pagehelper.Page;
import com.qianfan365.jcstore.common.bean.CustomerBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.permission.PermissionPassport;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.pojo.Customer;
import com.qianfan365.jcstore.common.pojo.CustomerGroup;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.CustomerGroupService;
import com.qianfan365.jcstore.service.CustomerService;
import com.qianfan365.jcstore.service.PermissionInfoService;
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
 * 客户操作接口
 *
 * @author szz
 */
@Controller
@RequestMapping("/mobile/customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private PermissionInfoService permissionInfoService;
    @Autowired
    private CustomerGroupService customerGroupService;

    /**
     * 保存客户信息,用户更新或新增(1期接口,不再更新或维护)
     *
     * @param session
     * @param customer
     * @return
     */
    @RequestMapping(path = "/save", method = RequestMethod.POST)
    @ResponseBody
    @PermissionPassport(permissionids = PermissionType.CUSTOMER_EDIT)
    @Deprecated
    public ResultData save(HttpSession session, Customer customer) {
        String nameRegex = "^[\u4E00-\u9FA5A-Za-z]{1,10}$";
        String phoneRegex = "^[\\d-\\*#\\+]{1,16}$";
        // 校验数据
        if (customer != null && StringUtils.isNotEmpty(customer.getName())
            && customer.getName().matches(nameRegex) && StringUtils.isNotEmpty(customer.getPhone())
            && customer.getPhone().matches(phoneRegex)
            && StringUtils.isNotEmpty(customer.getAddress()) && customer.getAddress().length() <= 50
            && (StringUtils.isEmpty(customer.getRemark()) || (
            StringUtils.isNotEmpty(customer.getRemark()) && customer.getRemark().length() <= 100))
            && (
            (StringUtils.isNotEmpty(customer.getCompany()) && customer.getCompany().length() <= 20)
                || StringUtils.isEmpty(customer.getCompany()))) {
            // 保存数据
            User user = this.getLoginUser(session);
            // 新增逻辑
            if (customer.getId() == null) {
                customer = this.customerService.insert(customer, user);
            } else {
                // 更新逻辑
                int i = this.customerService.update(customer, user, false);
                // 判断更新是否成功
                if (i == -1) {
                    return ResultData.build().setStatus(Status.NO_PERMISSION);
                } else if (i == 0) {
                    return ResultData.build().setStatus(Status.DATA404);
                }
            }
            // 1.1期,返回结果封装(增添类型名称及折扣信息)
            CustomerBean bean = customerService.convertToBean(customer, true);
            return ResultData.build().success().put("customer", bean);
        }
        return ResultData.build().parameterError();
    }

    /**
     * 保存客户信息,用户更新或新增(1.1期后使用)
     *
     * @param session
     * @param customer
     * @return
     */
    @RequestMapping(path = "/saveCustomer", method = RequestMethod.POST)
    @ResponseBody
    @PermissionPassport(permissionids = PermissionType.CUSTOMER_EDIT)
    @ModulePassport(moduleids = {ModuleType.CUSTOMER}, setuser = true)
    public ResultData saveCustomer(HttpSession session, Customer customer) {
        User user = this.getLoginUser(session);
        boolean groupModule =
            SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.CUSTOMER_GROUP);
        if (customer.getGroupId() != null && !groupModule) {
            return ResultData.build().setStatus(Status.MODULE_CHANGED);
        }
        // 校验试用账号
        if (user.getIsTrialAccount() && customerService.checkTrialProductNum(user.getId())) {
            return ResultData.build().setStatus(Status.TRIAL_ACCOUNT_CUSTOMER_NUM_BEYOND);
        }
        // 校验数据
        if (customerService.check(customer)) {
            // 保存数据
            // 校验类型
            boolean flag = false; // 是否需要置空客户分组的标识
            if (customer.getGroupId() != null) {
                CustomerGroup group = this.customerGroupService.getById(customer.getGroupId());
                // 获取的分组为null或者分组不属于当前用户所在店铺,则返回参数错误
                if (group == null || !group.getBelongs().equals((user.getBelongs() == 0 ?
                    user.getId() :
                    user.getBelongs()))) {
                    return ResultData.build().parameterError();
                }
            } else {
                // 分组为空,此时更新则置空分组信息
                if (groupModule) {  //有分组模块才置空
                    flag = true;
                }
            }
            if (customer.getId() == null) {
                customer = this.customerService.insert(customer, user);
            } else {
                int i = this.customerService.update(customer, user, flag);
                if (i == -1) {
                    return ResultData.build().setStatus(Status.NO_PERMISSION);
                } else if (i == 0) {
                    return ResultData.build().setStatus(Status.DATA404);
                }
            }
            // 1.1期,结果封装至bean,增加分组及折扣信息
            CustomerBean bean = customerService.convertToBean(customer, groupModule);
            return ResultData.build().success().put("customer", bean);
        }
        return ResultData.build().parameterError();
    }

    /**
     * 获取客户信息
     *
     * @param session
     * @param cid
     * @return
     */
    @RequestMapping(path = "/get", method = RequestMethod.GET)
    @ResponseBody
    @ModulePassport(moduleids = {ModuleType.CUSTOMER}, setuser = true)
    public ResultData get(HttpSession session, Integer cid) {
        // 校验数据
        if (cid != null) {
            User user = this.getLoginUser(session);
            boolean groupModule =
                SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.CUSTOMER_GROUP);
            // 判断权限
            int[] permissionids = {2};
            // 用户具有查看全部内容的权限或者为前台管理账户
            Customer customer = null;
            if (user.getBelongs() == 0
                || permissionInfoService.checkPermission(user.getId(), permissionids)) {
                customer = this.customerService.getByBelongs(cid,
                    user.getBelongs() == 0 ? user.getId() : user.getBelongs());
            } else {
                customer = this.customerService.getByUid(cid, user.getId());
            }
            CustomerBean bean = customerService.convertToBean(customer, groupModule);
            return ResultData.build().success().put("customer", bean).put("permissionInfo", permissionInfoService.findMapByUidPid(PermissionType.CUSTOMER_EDIT, user.getId(), user.getBelongs()));
        }
        return ResultData.build().parameterError();
    }

    /**
     * 列表查询
     *
     * @param session
     * @param keyword
     * @param currentPage
     * @return
     */
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    @ModulePassport(moduleids = {ModuleType.CUSTOMER}, setuser = true)
    public ResultData list(HttpSession session, @RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "1") Integer currentPage, Integer groupId) {
        // 校验请求数据
        if (keyword != null) {
            User user = this.getLoginUser(session);
            boolean groupModule =
                SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.CUSTOMER_GROUP);
            if (groupId != null && !groupModule) {
                return ResultData.build().setStatus(Status.MODULE_CHANGED);
            }

            // 判断权限
            int[] permissionids = {2};
            // 用户具有查看全部内容的权限或者为前台管理账户
            Page<Customer> page = null;
            Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
            if (user.getBelongs() == 0) {
                page = this.customerService.searchByBelongs(currentPage, keyword,
                    user.getBelongs() == 0 ? user.getId() : user.getBelongs(), groupId);
                map.put(PermissionType.CUSTOMER_EDIT, true);
                map.put(PermissionType.CUSTOMER_DEL, true);
            } else if (permissionInfoService.checkPermission(user.getId(), permissionids)) {
                page = this.customerService.searchByBelongs(currentPage, keyword,
                    user.getBelongs() == 0 ? user.getId() : user.getBelongs(), groupId);
                map =
                    permissionInfoService.findMapByUidPids(Arrays.asList(PermissionType.CUSTOMER_EDIT, PermissionType.CUSTOMER_DEL), user.getId());
            } else {
                page = this.customerService.searchById(currentPage, keyword, user.getId(), groupId);
                map =
                    permissionInfoService.findMapByUidPids(Arrays.asList(PermissionType.CUSTOMER_EDIT, PermissionType.CUSTOMER_DEL), user.getId());
            }
            // 1.1期,结果转换为bean返回
            Page<CustomerBean> list = customerService.convertToBean(page, groupModule);
            return ResultData.build().parsePageBean(list).put("permissionInfo", map).put("group", groupModule);
        }
        return ResultData.build().parameterError();
    }

    /**
     * 删除客户信息
     *
     * @param session
     * @param cid
     * @return
     */
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PermissionPassport(permissionids = PermissionType.CUSTOMER_DEL)
    @ModulePassport(moduleids = {ModuleType.CUSTOMER})
    public ResultData delete(HttpSession session, Integer cid) {
        // 校验参数
        if (cid != null) {
            Customer customer = new Customer();
            customer.setId(cid);
            customer.setStatus(false);
            User user = this.getLoginUser(session);
            int i = this.customerService.update(customer, user, false);
            if (i == -1) {
                return ResultData.build().setStatus(Status.NO_PERMISSION);
            } else if (i == 0) {
                return ResultData.build().setStatus(Status.DATA404);
            }
            return ResultData.build().success();
        }
        return ResultData.build().parameterError();
    }

    /**
     * 查询用户,开单时查询使用,每页返回10条数据
     *
     * @param session
     * @param keyword
     * @param currentPage
     * @return
     */
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    @ResponseBody
    @ModulePassport(moduleids = {ModuleType.CUSTOMER}, setuser = true)
    public ResultData search(HttpSession session, @RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "1") Integer currentPage, Long querytime, Integer groupId, @RequestParam(defaultValue = "10") Integer limit) {
        if (1 == currentPage || null == querytime || 0 == querytime) {
            querytime = new Date().getTime();
        }
        // 校验参数
        if (keyword != null) {
            // 校验权限
            User user = this.getLoginUser(session);
            boolean groupModule =
                SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.CUSTOMER_GROUP);
            if (groupId != null && !groupModule) {
                return ResultData.build().setStatus(Status.MODULE_CHANGED);
            }

            // 判断权限
            int[] permissionids = {2};
            Page<Customer> page = null;
            Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
            if (user.getBelongs() == 0) {
                page = this.customerService.findByBelongs(currentPage, limit, keyword,
                    user.getBelongs() == 0 ? user.getId() : user.getBelongs(), querytime, groupId);
                map.put(PermissionType.CUSTOMER_EDIT, true);
                map.put(PermissionType.CUSTOMER_DEL, true);
            } else if (permissionInfoService.checkPermission(user.getId(), permissionids)) {
                page = this.customerService.findByBelongs(currentPage, limit, keyword,
                    user.getBelongs() == 0 ? user.getId() : user.getBelongs(), querytime, groupId);
                map =
                    permissionInfoService.findMapByUidPids(Arrays.asList(PermissionType.CUSTOMER_EDIT, PermissionType.CUSTOMER_DEL), user.getId());
            } else {
                page =
                    this.customerService.findById(currentPage, limit, keyword, user.getId(), querytime, groupId);
                map =
                    permissionInfoService.findMapByUidPids(Arrays.asList(PermissionType.CUSTOMER_EDIT, PermissionType.CUSTOMER_DEL), user.getId());
            }
            // 1.1期,结果转换为bean返回
            Page<CustomerBean> list = customerService.convertToBean(page, groupModule);
            return ResultData.build().parsePageBean(list).put("querytime", querytime).put("permissionInfo", map).put("group", groupModule);
        }
        return ResultData.build().parameterError();
    }

    @ResponseBody
    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ModulePassport(moduleids = {ModuleType.CUSTOMER}, setuser = true)
    public ResultData all(String keyword, Integer groupId, HttpSession session) {
        // 校验是否具有分组权限
        User user = this.getLoginUser(session);
        boolean groupModule =
            SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.CUSTOMER_GROUP);
        if (groupId != null && !groupModule) {
            return ResultData.build().setStatus(Status.MODULE_CHANGED);
        }
        // 判断是个人权限还是全员权限
        int[] permissionids = {2};
        boolean permission_flag = false;
        if (user.getBelongs().equals(0)
            || permissionInfoService.checkPermission(user.getId(), permissionids)) {
            permission_flag = true;
        }
        TreeMap<String, ArrayList<CustomerBean>> all =
            customerService.all(keyword, groupId, user, permission_flag, groupModule);
        // 返回权限
        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
        if (user.getBelongs() == 0) {
            map.put(PermissionType.CUSTOMER_EDIT, true);
            map.put(PermissionType.CUSTOMER_DEL, true);
        }else{
            map =
                permissionInfoService.findMapByUidPids(Arrays.asList(PermissionType.CUSTOMER_EDIT, PermissionType.CUSTOMER_DEL), user.getId());
        }
        return ResultData.build().put("aaData", all).
            put("permissionInfo",map).put("group", groupModule);
    }

    /**
     * 体验账号添加客户校验
     *
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "trialNum", method = RequestMethod.GET)
    public ResultData checkTrialProductNum(HttpSession session) {
        User user = this.getLoginUser(session);
        boolean check = this.customerService.checkTrialProductNum(user.getId());
        if (user.getIsTrialAccount() && check) {
            return ResultData.build().setStatus(Status.TRIAL_ACCOUNT_CUSTOMER_NUM_BEYOND);
        }
        return ResultData.build().success();
    }

}
