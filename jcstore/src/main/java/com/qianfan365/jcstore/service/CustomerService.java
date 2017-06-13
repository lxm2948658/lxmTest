package com.qianfan365.jcstore.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qianfan365.jcstore.common.bean.CustomerBean;
import com.qianfan365.jcstore.common.pojo.Customer;
import com.qianfan365.jcstore.common.pojo.CustomerExample;
import com.qianfan365.jcstore.common.pojo.CustomerExample.Criteria;
import com.qianfan365.jcstore.common.pojo.CustomerGroup;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.util.PinYinUtil;
import com.qianfan365.jcstore.dao.inter.CustomerMapper;
import com.qianfan365.jcstore.dao.inter.OrderInfoMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 客户操作相关
 *
 * @author szz
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private PermissionInfoService permissionInfoService;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private CustomerGroupService customerGroupService;
    @Autowired
    private UserService userService;

    /**
     * 新增客户
     *
     * @param customer
     * @param user
     * @return
     */
    public Customer insert(Customer customer, User user) {
        Date nowTime = new Date();
        customer.setCreatetime(nowTime);
        customer.setUpdatetime(nowTime);
        customer.setStatus(true);
        customer.setBelongs(userService.getAdminID(user));
        customer.setUid(user.getId());
        customer.setStaffname(user.getStaffname());
        customer.setInitial(PinYinUtil.getInitial(customer.getName()));
        this.customerMapper.insert(customer);
        return customer;
    }

    /**
     * 根据客户ID及所属信息belongs获取单个客户信息 用于具有查看全员权限的用户获取信息
     *
     * @param cid     客户ID
     * @param belongs 所属信息(管理员ID)
     * @return
     */
    public Customer getByBelongs(Integer cid, Integer belongs) {
        return customerMapper.getByBelongs(cid, belongs);
    }

    /**
     * 根据客户ID及用户ID查询单个客户信息 用于没有查看全员权限的用户
     *
     * @param cid 客户ID
     * @param uid 用户ID
     * @return
     */
    public Customer getByUid(Integer cid, Integer uid) {
        return customerMapper.getByUid(cid, uid);
    }

    /**
     * 根据条件查询客户
     *
     * @param currentPage
     * @param keyword
     * @param belongsId
     * @param groupId
     * @return
     */
    public Page<Customer> searchByBelongs(Integer currentPage, String keyword, Integer belongsId, Integer groupId) {
        CustomerExample example = new CustomerExample();
        example.setOrderByClause(" updatetime desc");
        getListQueryCriteria(keyword, groupId, example).andBelongsEqualTo(belongsId);
        PageHelper.startPage(currentPage, 20);
        return (Page<Customer>) customerMapper.selectByExample(example);
    }

    /**
     * 根据用户id获取客户信息
     *
     * @param currentPage
     * @param keyword
     * @param id
     * @param groupId
     * @return
     */
    public Page<Customer> searchById(Integer currentPage, String keyword, Integer id, Integer groupId) {
        CustomerExample example = new CustomerExample();
        example.setOrderByClause(" updatetime desc");
        getListQueryCriteria(keyword, groupId, example).andUidEqualTo(id);
        PageHelper.startPage(currentPage, 20);
        return (Page<Customer>) customerMapper.selectByExample(example);
    }

    /**
     * 获取列表查询条件 (客户姓名 模糊搜索,客户类型)
     * <p>
     * 1.3期之后废弃
     *
     * @param keyword
     * @param belongsId
     * @param groupId
     * @param example
     */
    private Criteria getListQueryCriteria(String keyword, Integer groupId, CustomerExample example) {
        Criteria criteria = getBasicCriteria(example);
        // 判断是否需要模糊查询
        if (keyword != null && keyword.length() > 0)
            criteria.andNameLike("%" + keyword + "%");
        // 判断是否需要根据客户类型分组查询
        if (groupId != null)
            criteria.andGroupIdEqualTo(groupId);
        return criteria;
    }

    /**
     * 获取基本的查询条件(客户未被删除)
     *
     * @param belongsId
     * @param example
     * @return
     */
    private Criteria getBasicCriteria(CustomerExample example) {
        return example.createCriteria().andStatusEqualTo(true);
    }

    /**
     * 更新用户信息
     *
     * @param customer
     * @param user
     * @param flag
     * @return
     */
    public int update(Customer customer, User user, Boolean flag) {
        // 初始化更新标识
        if (flag == null)
            flag = false;
        int i = 0;
        customer.setUpdatetime(new Date());
        customer.setInitial(PinYinUtil.getInitial(customer.getName()));
        // 判断权限
        int[] permissionids = {2};
        boolean permission =
            this.permissionInfoService.checkPermission(user.getId(), permissionids);
        // 管理员用户或具有权限用户则可更新
        if (user.getBelongs() == 0 || permission) {
            CustomerExample example = new CustomerExample();
            getBasicCriteria(example).andBelongsEqualTo(userService.getAdminID(user)).andIdEqualTo(customer.getId());
            i = this.customerMapper.updateByExampleSelective(customer, example);
            // 置空分组信息 1.1期使用
            if (flag)
                this.customerMapper.updateGroupIdToNull(customer.getId());
        } else {
            // 只可查看自己信息的用户需要校验此客户是否为自己添加
            Customer key = this.customerMapper.selectByPrimaryKey(customer.getId());
            if (key != null && key.getStatus()) {
                if (key.getUid().equals(user.getId())) {
                    i = this.customerMapper.updateByPrimaryKeySelective(customer);
                    // 置空分组信息 1.1期使用
                    if (flag)
                        this.customerMapper.updateGroupIdToNull(customer.getId());
                } else {
                    i = -1;
                }
            }
        }
        return i;
    }


    /**
     * 用户开单时查询用户,同时模糊搜索手机号及客户姓名,用于具有查看全部信息权限的用户
     *
     * @param currentPage
     * @param limit
     * @param keyword
     * @param belongsId
     * @param querytime
     * @param groupId
     * @return
     */
    public Page<Customer> findByBelongs(Integer currentPage, Integer limit, String keyword, Integer belongsId, Long querytime, Integer groupId) {
        CustomerExample example = new CustomerExample();
        example.setOrderByClause(" updatetime desc");
        // 两个查询条件是or连接
        Criteria criteria =
            getNamelikeCriteria(keyword, querytime, example).andBelongsEqualTo(belongsId);
        Criteria criteria2 =
            getPhonelikeCriteria(keyword, querytime, example).andBelongsEqualTo(belongsId);
        if (groupId != null) {
            criteria.andGroupIdEqualTo(groupId);
            criteria2.andGroupIdEqualTo(groupId);
        }
        // 此时每页显示10条数据
        PageHelper.startPage(currentPage, limit);
        return (Page<Customer>) customerMapper.selectByExample(example);
    }

    /**
     * 获取 客户姓名 模糊查询 条件对象 1.3期起,开单搜索及客户管理均使用此逻辑
     *
     * @param keyword
     * @param querytime
     * @param example
     * @return
     */
    private Criteria getPhonelikeCriteria(String keyword, Long querytime, CustomerExample example) {
        return example.or().andStatusEqualTo(true).andPhoneLike(
            "%" + keyword + "%").andCreatetimeLessThanOrEqualTo(new Date(querytime));
    }

    /**
     * 获取 客户联系方式 模糊查询 条件对象 1.3期起,开单搜索及客户管理均使用此逻辑
     *
     * @param keyword
     * @param querytime
     * @param example
     * @return
     */
    private Criteria getNamelikeCriteria(String keyword, Long querytime, CustomerExample example) {
        return getBasicCriteria(example).andNameLike(
            "%" + keyword + "%").andCreatetimeLessThanOrEqualTo(new Date(querytime));
    }

    /**
     * 用户开单时查询用户,同时模糊搜索手机号及客户姓名,用于具有查看个人信息权限的用户
     *
     * @param currentPage
     * @param limit
     * @param keyword
     * @param id
     * @param groupId
     * @return
     */
    public Page<Customer> findById(Integer currentPage, Integer limit, String keyword, Integer id, Long querytime, Integer groupId) {
        CustomerExample example = new CustomerExample();
        example.setOrderByClause(" updatetime desc");
        // 两个查询条件是or连接
        Criteria criteria = getNamelikeCriteria(keyword, querytime, example).andUidEqualTo(id);
        Criteria criteria2 = getPhonelikeCriteria(keyword, querytime, example).andUidEqualTo(id);
        if (groupId != null) {
            criteria.andGroupIdEqualTo(groupId);
            criteria2.andGroupIdEqualTo(groupId);
        }
        PageHelper.startPage(currentPage, limit);
        Page<Customer> page = (Page<Customer>) customerMapper.selectByExample(example);
        return page;
    }

    /**
     * 更新最近交易信息
     *
     * @param receiceAmoun
     * @param customerId
     */
    public void updateLatelyAccount(Double receiceAmoun, Integer customerId) {
        if (customerId != null && receiceAmoun != null) {
            Customer customer = new Customer();
            customer.setLatelyAccount(receiceAmoun);
            customer.setId(customerId);
            this.customerMapper.updateByPrimaryKeySelective(customer);
        }
    }

    /**
     * 根据ID查询客户
     *
     * @param cId
     * @return
     */
    public Customer findById(Integer cId) {
        return customerMapper.selectByPrimaryKey(cId);

    }

    /**
     * bean封装,1.1期新增功能
     *
     * @param customer
     * @return
     */
    public CustomerBean convertToBean(Customer customer, boolean groupModule) {
        CustomerBean bean = null;
        try {
            bean = new CustomerBean();
            BeanUtils.copyProperties(bean, customer);
            bean.setGroupFlag(groupModule);
            if (!groupModule) {
                customer.setGroupId(null);
                bean.setGroupId(null);
            }
            Integer groupId = customer.getGroupId();
            if (groupId != null) {
                CustomerGroup group = this.customerGroupService.getById(customer.getGroupId());
                if (group != null) {
                    bean.setGroupName(group.getGroupName());
                    bean.setDiscount(group.getDiscount());
                }
            }
        } catch (Exception e) {}
        return bean;
    }

    /**
     * bean封装,1.1期新增功能
     *
     * @param page
     * @return
     */
    public Page<CustomerBean> convertToBean(Page<Customer> page, Boolean groupModule) {
        Page<CustomerBean> list = new Page<CustomerBean>();
        list.setPageNum(page.getPageNum());
        list.setPageSize(page.getPageSize());
        list.setTotal(page.getTotal());
        page.forEach(c -> list.add(this.convertToBean(c, groupModule)));
        return list;
    }

    /**
     * 清空客户类型信息
     *
     * @param groupId
     * @param user
     */
    public void deleteCustomerGroup(Integer groupId, User user) {
        CustomerExample example = new CustomerExample();
        example.createCriteria().andBelongsEqualTo(userService.getAdminID(user)).andGroupIdEqualTo(groupId);
        List<Customer> list = this.customerMapper.selectByExample(example);
        for (Customer customer : list) {
            customer.setGroupId(null);
            this.customerMapper.updateByPrimaryKey(customer);
        }
    }

    /**
     * 校验新增客户信息
     *
     * @param customer
     * @return
     */
    public boolean check(Customer customer) {
        String phoneRegex = "^[\\d-\\*#\\+]{1,16}$";
        return customer != null && StringUtils.isNotEmpty(customer.getName())
            && customer.getName().length() <= 10 && StringUtils.isNotEmpty(customer.getPhone())
            && customer.getPhone().matches(phoneRegex) && (
            StringUtils.isEmpty(customer.getAddress()) || customer.getAddress().length() <= 50) && (
            StringUtils.isEmpty(customer.getRemark()) || customer.getRemark().length() <= 100) && (
            StringUtils.isEmpty(customer.getCompany()) || customer.getCompany().length() <= 20);
    }

    /**
     * 体验账号增加客户数量校验
     *
     * @param id
     * @return
     */
    public boolean checkTrialProductNum(Integer id) {
        CustomerExample example = new CustomerExample();
        example.createCriteria().andBelongsEqualTo(id).andStatusEqualTo(true);
        return customerMapper.countByExample(example) >= 10;
    }

    /**
     * 查询
     * @param keyword 关键字
     * @param groupId 分组ID
     * @param user  用户
     * @param permission_flag 权限表示(true 全员权限,false 个人权限)
     * @param groupModel 分组权限
     */
    public TreeMap<String, ArrayList<CustomerBean>> all(String keyword, Integer groupId, User user, boolean permission_flag, boolean groupModel) {
        CustomerExample example = new CustomerExample();
        example.setOrderByClause(" FIELD(initial,'#'),initial asc,updatetime desc");
        Criteria criteria = example.createCriteria().andStatusEqualTo(true);
        boolean notEmpty = StringUtils.isNotEmpty(keyword);
        Criteria or = null;
        if (notEmpty) {
            criteria.andNameLike("%" + keyword + "%");
            or = example.or().andPhoneLike("%" + keyword + "%").andStatusEqualTo(true);;
        }
        if (groupId != null) {
            criteria.andGroupIdEqualTo(groupId);
            if (notEmpty)
                or.andGroupIdEqualTo(groupId);
        }
        if (permission_flag) {
            criteria.andBelongsEqualTo(userService.getAdminID(user));
            if (notEmpty)
                or.andBelongsEqualTo(userService.getAdminID(user));
        } else {
            criteria.andUidEqualTo(user.getId());
            if (notEmpty)
                or.andUidEqualTo(user.getId());
        }
        List<Customer> customers = customerMapper.selectByExample(example);
        // 生成数据
        TreeMap<String, ArrayList<CustomerBean>> map =
            new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int i = o1.compareTo(o2);
                    if(i != 0 && o1.equals("#")){
                        return 1;
                    }
                    return i;
                }
            });
        for (Customer customer : customers) {
            ArrayList<CustomerBean> customerBeen = map.get(customer.getInitial());
            if(customerBeen == null){
                customerBeen = new ArrayList<>();
                if(StringUtils.isEmpty(customer.getInitial())){
                    customer.setInitial(PinYinUtil.getInitial(customer.getName()));
                }
                map.put(customer.getInitial(),customerBeen);
            }
            customerBeen.add(convertToBean(customer,groupModel));
        }
        return map;
    }

    /**
     * 刷库用的
     */
    public void initCustomerInfo(){
        CustomerExample example = new CustomerExample();
        // 字段为字符串或者为null
        example.createCriteria().andInitialIsNull();
        example.or().andInitialEqualTo("");
        List<Customer> customers = customerMapper.selectByExample(example);
        for (Customer customer : customers) {
            customer.setInitial(PinYinUtil.getInitial(customer.getName()));
            customerMapper.updateByPrimaryKey(customer);
        }
    }

}
