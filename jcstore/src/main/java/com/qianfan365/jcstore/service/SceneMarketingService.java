package com.qianfan365.jcstore.service;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.SceneMarketing;
import com.qianfan365.jcstore.common.pojo.SceneMarketingExample;
import com.qianfan365.jcstore.common.pojo.SceneMarketingExample.Criteria;
import com.qianfan365.jcstore.dao.inter.ClientMapper;
import com.qianfan365.jcstore.dao.inter.SceneMarketingMapper;

@Service("sceneMarketingService")
public class SceneMarketingService {
  @Autowired
  private SceneMarketingMapper sceneMarketingMapper;
  @Autowired
  private UserService userService;
  @Autowired
  private ClientMapper clientMapper;
  
  /**
   * 查询所有场景营销账户数据，场景是0
   * @param sceneMarketing
   * @return
   */
  public Page<SceneMarketing> findAllScene(Integer currentPage,Integer limit,Long querytime,int type,int belongs){
    SceneMarketingExample sceneEx = new SceneMarketingExample();
    Criteria criteria = sceneEx.createCriteria();
    if(querytime!=null){
      criteria.andCreateTimeLessThanOrEqualTo(new Date(querytime));
    }
    if(belongs>0){
      criteria.andBelongEqualTo(belongs);
    }
    criteria.andTypeEqualTo(type);
    sceneEx.setOrderByClause("create_time desc");
    PageHelper.startPage(currentPage,limit);
    return (Page<SceneMarketing>) this.sceneMarketingMapper.selectByExample(sceneEx);
  }
  
  
  /**
   * 删除所有场景营销数据信息
   * @param id
   * @return
   */
  public ResultData delectScene(Integer id){
    sceneMarketingMapper.deleteByPrimaryKey(id);
    return ResultData.build().success();
  }
  
  /**
   * 场景修改新增列表
   * @param scene
   * @param id
   * @param belong
   * @return
   */
  public int add(SceneMarketing scene){
    Date date = new Date();
    //校验存储内容是否符合要求
    if(scene.getTitle() != null && scene.getTitle().length() <= 30 && scene.getUrl().length() <=100){
      if(scene.getPicture().isEmpty()){
        return 0;
      }
    }
    //ID为空时新增
    scene.setUpdateTime(date);
    if(scene.getId() == null){
      scene.setCreateTime(date);
      return sceneMarketingMapper.insert(scene);
    }else{
      //有ID时修改
      return sceneMarketingMapper.updateByPrimaryKeySelective(scene);
      }
  }
}


