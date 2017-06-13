package com.qianfan365.jcstore.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qianfan365.jcstore.common.bean.OrderDateBean;
import com.qianfan365.jcstore.common.bean.OrderListBean;
import com.qianfan365.jcstore.common.bean.OrderRefundBean;
import com.qianfan365.jcstore.common.bean.RefundPrintBean;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageInform;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageType;
import com.qianfan365.jcstore.common.constant.MessageConstant.PushInform;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.OrderConstant.OrderType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.constant.RefundConstant.RefundStatus;
import com.qianfan365.jcstore.common.pojo.OrderInfo;
import com.qianfan365.jcstore.common.pojo.Refund;
import com.qianfan365.jcstore.common.pojo.RefundDetail;
import com.qianfan365.jcstore.common.pojo.RefundDetailExample;
import com.qianfan365.jcstore.common.pojo.RefundExample;
import com.qianfan365.jcstore.common.pojo.RefundExample.Criteria;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserPermission;
import com.qianfan365.jcstore.common.util.PageForm;
import com.qianfan365.jcstore.common.util.TimeUtils;
import com.qianfan365.jcstore.dao.inter.RefundDetailMapper;
import com.qianfan365.jcstore.dao.inter.RefundMapper;

@Service("refundService")
public class RefundService {
  @Autowired
  private RefundMapper refundMapper;
  @Autowired
  private RefundDetailMapper refundDetailMapper;
  @Autowired
  private OrderInfoService orderService;
  @Autowired
  private UserService userService;
  @Autowired
  private MessageService messageService;
  @Autowired
  private PermissionInfoService permissionInfoService;
  @Autowired
  private ProductService productService;
  
  /**
   * 增加退款单
   * @param orderId
   * @param user
   * @param refundFee
   * @param total
   * @param description
   * @param details
   * @return
   */
  public int add(OrderInfo order, User user, Double refundFee, Double total, String description, Date expectedDate, List<RefundDetail> details){
    Refund refund = new Refund();
    BeanUtils.copyProperties(order, refund);
    refund.setId(null);
    refund.setOrderInfoId(order.getId());
    Date date = new Date();
    refund.setCreatetime(date);
    refund.setUpdatetime(date);
    refund.setRefundSalesman(user.getStaffname());
    refund.setRefundUid(user.getId());
    refund.setRefundDescription(description);
    refund.setRefundFee(refundFee);
    refund.setRefundTotal(total);
    refund.setExpectedDate(expectedDate);
    RefundExample example = new RefundExample();
    example.createCriteria().andOrderInfoIdEqualTo(order.getId());
    int count = refundMapper.countByExample(example)+1;
    refund.setRefundNum(new StringBuilder().append("TH").append(order.getNumber())
      .append(count<10?"0":"").append(count).toString());
    int num = refundMapper.insertSelective(refund);
    if(refund.getMinimalFlag()==OrderType.ROUTINE){
      //增加退款单详情
      addDetail(refund.getId(), order.getId(), details);
    }
    //更改退款金额累计
    orderService.updateRefundFee(refundFee, 0.0, order.getId());
    if(!user.getIsTrialAccount() && SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.MESSAGE)){
      //发送消息
      Integer belongs = (user.getBelongs()==0)?user.getId():user.getBelongs();
      List<UserPermission> uplist = permissionInfoService.findMessageNeed(belongs, PermissionType.REFUND_REMID);
      String refundNum = refund.getRefundNum();
      refundNum = "TH"+refundNum.substring(refundNum.length()-8, refundNum.length());
      messageService.insertBatch(String.format(MessageInform.REFUND_REMID, refundNum),PushInform.REFUND_REMID,
        uplist, PermissionType.REFUND_REMID, refund.getId(), MessageType.REFUND_REMID, order.getUserId(), false);
    }
    return num>0?refund.getId():0;
  }
  
  /**
   * 增加退款单详情
   * @param refundId
   * @param orderId
   * @param details
   */
  public void addDetail(Integer refundId, Integer orderId, List<RefundDetail> details){
    for (RefundDetail detail : details) {
      if(detail.getNumber()>0){
        detail.setOrderDetailId(detail.getId());
        detail.setId(null);
        detail.setRefundId(refundId);
        detail.setOrderInfoId(orderId);
        Date date = new Date();
        detail.setCreatetime(date);
        detail.setUpdatetime(date);
        refundDetailMapper.insertSelective(detail);
        //更改退款的商品数量累计
        orderService.updateOrderRefundNum(detail.getNumber(), detail.getOrderDetailId());
      }
    }
  }
  
  /**
   * 完成退货单
   * @param refund
   * @return
   */
  public int finish(Refund refund){
    refund.setStatus(RefundStatus.FINISH);
    refund.setUpdatetime(new Date());
    int num = refundMapper.updateByPrimaryKey(refund);
    orderService.updateRefundFee(-refund.getRefundFee(), refund.getRefundFee(), refund.getOrderInfoId());
    if(refund.getMinimalFlag()==OrderType.ROUTINE && num>0){
      RefundDetailExample example = new RefundDetailExample();
      example.createCriteria().andRefundIdEqualTo(refund.getId());
      List<RefundDetail> details = refundDetailMapper.selectByExample(example);
      for (RefundDetail detail : details) {
        productService.updateInventory(detail.getProductId(), -detail.getNumber());
      }
    }
    return num;
  }
  
  /**
   * 作废退货单
   * @param refund
   * @return
   */
  public int cancel(Refund refund){
    refund.setStatus(RefundStatus.CANCEL);
    refund.setUpdatetime(new Date());
    int num = refundMapper.updateByPrimaryKey(refund);
    orderService.updateRefundFee(-refund.getRefundFee(), 0.0, refund.getOrderInfoId());
    if(refund.getMinimalFlag()==OrderType.ROUTINE){
      RefundDetailExample example = new RefundDetailExample();
      example.createCriteria().andRefundIdEqualTo(refund.getId());
      List<RefundDetail> details = refundDetailMapper.selectByExample(example);
      //更改退款的商品数量累计
      for (RefundDetail detail : details) {
        orderService.updateOrderRefundNum(-detail.getNumber(), detail.getOrderDetailId());
      }
    }
    return num;
  }
  
  /**
   * 作废订单时作废退货单
   * @param oid
   * @return
   */
  public int cancelByOid(Integer oid){
    RefundExample refundExample = new RefundExample();
    refundExample.createCriteria().andOrderInfoIdEqualTo(oid).andStatusEqualTo(RefundStatus.PENDING);
    Refund refund = new Refund();
    refund.setStatus(RefundStatus.CANCEL);
    refund.setUpdatetime(new Date());
    return refundMapper.updateByExampleSelective(refund, refundExample);
  }
  
  /**
   * 退货单列表
   * @param currentPage
   * @param limit
   * @param uid
   * @param status
   * @param querytime
   * @return
   */
  public PageForm findList(Integer currentPage, Integer limit, RefundExample example, User user) {
    List<OrderDateBean> dateBeans = new ArrayList<OrderDateBean>();
    PageHelper.startPage(currentPage, limit);
    Page<Refund> page = (Page<Refund>) refundMapper.selectByExample(example);
    PageForm pageForm = PageForm.getPageForm(page);
    String time = "";
    List<OrderListBean> listBeans = new ArrayList<OrderListBean>();
    if (!page.isEmpty()) {
      Map<Integer, Boolean> map = permissionInfoService.findMapByUidPid(PermissionType.FINISH_ORDER, user.getId(), user.getBelongs());
      List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
      boolean generalModule = moduleids.contains(ModuleType.GENERAL_ORDER);
      boolean minimalModule = moduleids.contains(ModuleType.MINIMAL_ORDER);
      List<Integer> oids = page.stream().map(p -> p.getOrderInfoId()).collect(Collectors.toList());
      Map<Integer, OrderInfo> orderMap = orderService.findByIds(oids).stream().collect(Collectors.toMap(o -> o.getId(), o -> o));
      for (Refund refund : page) {
        OrderListBean orderListBean = new OrderListBean(refund);
        orderListBean.setFinish(refund.getStatus()==1 && map.get(PermissionType.FINISH_ORDER)
            && (refund.getMinimalFlag()==0?generalModule:minimalModule));
        orderListBean.setOrderStatus(orderMap.get(refund.getOrderInfoId()).getOrderStatus());
        findOrderProduct(orderListBean);
        if(time.equals(orderListBean.getTime())){
          listBeans.add(orderListBean);
        }else{
          time = orderListBean.getTime();
          OrderDateBean dateBean = new OrderDateBean();
          dateBean.setTime(time);
          listBeans = new ArrayList<OrderListBean>();
          listBeans.add(orderListBean);
          dateBean.setList(listBeans);
          dateBeans.add(dateBean);
        }
        
      }
    }
    pageForm.setData(dateBeans);
    return pageForm;
  }

  /**
   * 退货单列表 || 退货单搜索 时的条件判断
   * @param uid
   * @param status
   * @param querytime
   * @return
   */
  public RefundExample getRefundExample(Integer customerId, String customer, String phone,
              Date begin, Date end, List<Integer> uid, Integer status, Long querytime) {
    RefundExample example = new RefundExample();
    Criteria criteria = example.createCriteria();
    criteria.andUserIdIn(uid).andCreatetimeLessThanOrEqualTo(new Date(querytime));
    if (status != null) {
      criteria.andStatusEqualTo(status);
    }else{ //退货单搜索时没有退货单状态的选项
      criteria.andStatusNotEqualTo(RefundStatus.CANCEL);//退货搜不出来作废的
      if (customerId != null) {
        criteria.andCustomerIdEqualTo(customerId);
      }
      if (customer != null) {
        criteria.andCustomerEqualTo(customer);
      }
      if (phone != null) {
        criteria.andPhoneEqualTo(phone);
      }
      if (begin != null && end != null) {
        criteria.andUpdatetimeBetween(begin, TimeUtils.formatDayEnd1(end));
      }
    }
    example.setOrderByClause(" updatetime desc,id desc");
    return example;
  }
  
  /**
   * 查询退货单下的所有明细
   * @param orderInfoId
   * @return
   */
  public void findOrderProduct(OrderListBean orderListBean) {
    RefundDetailExample example = new RefundDetailExample();
    example.createCriteria().andRefundIdEqualTo(orderListBean.getId());
    example.setOrderByClause("id desc");
    List<RefundDetail> details = refundDetailMapper.selectByExample(example);
    orderListBean.setProductNum(details.size());
    orderListBean.setProductName(details.stream().map(RefundDetail::getName).collect(Collectors.joining("、")));
  }
  
  /**
   * 根据订单id查询未作废的退货单id
   * @param oid
   * @return
   */
  public List<Integer> findRidByOid(Integer oid){
    return refundMapper.selectRidByOid(oid);
  }
  
  /**
   * 根据订单id查询退货单bean
   * @param oid
   * @return
   */
  public List<OrderRefundBean> findByOid(Integer oid){
    return refundMapper.selectByOid(oid);
  }
  
  /**
   * 根据订单id查询退货单bean(打印凭证版)
   * @param oid
   * @return
   */
  public List<RefundPrintBean> findPrintByOid(Integer oid){
    return refundMapper.selectPrintByOid(oid);
  }
  
  /**
   * 根据订单商品退款id查询退货详情集合
   * @param oid
   * @param pids
   * @param rids
   * @return
   */
  public List<RefundDetail> findRdByOPRid(Integer oid,List<Integer> pids,List<Integer> rids){
    RefundDetailExample example = new RefundDetailExample();
    example.createCriteria().andOrderInfoIdEqualTo(oid).andProductIdIn(pids).andRefundIdIn(rids);
    return refundDetailMapper.selectByExample(example);
  }
  
  /**
   * 根据id查询退货单
   * @param id
   * @return
   */
  public Refund findById(Integer id){
    return refundMapper.selectByPrimaryKey(id);
  }
  
  /**
   * 根据退款id查询退货详情
   * @param oid
   * @param pids
   * @param rids
   * @return
   */
  public List<RefundDetail> findDetailsById(Integer rid){
    RefundDetailExample example = new RefundDetailExample();
    example.createCriteria().andRefundIdEqualTo(rid);
    return refundDetailMapper.selectByExample(example);
  }
}
