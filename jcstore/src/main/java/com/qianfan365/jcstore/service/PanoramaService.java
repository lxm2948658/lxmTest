package com.qianfan365.jcstore.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.Panorama;
import com.qianfan365.jcstore.common.pojo.PanoramaExample;
import com.qianfan365.jcstore.common.pojo.PanoramaExample.Criteria;
import com.qianfan365.jcstore.dao.inter.PanoramaMapper;

@Service("panoramaService")
public class PanoramaService {
  @Autowired
  private PanoramaMapper panoramaMapper;
  /**
   * 全景查询
   * @param sceneMarketing
   * @return
   */
  public Page<Panorama> findPanorama(Integer currentPage,Integer limit,Integer belongs){
    PanoramaExample panoramaExampleEx = new PanoramaExample();
    Criteria criteria = panoramaExampleEx.createCriteria();
    if(belongs>0){
      criteria.andBelongsEqualTo(belongs);
    }
    panoramaExampleEx.setOrderByClause("create_time desc");
    PageHelper.startPage(currentPage,limit);
    return (Page<Panorama>) this.panoramaMapper.selectByExample(panoramaExampleEx);
  }
  
  /**
   * 移动端全景查询
   * @param sceneMarketing
   * @return
   */
  public List<Panorama> find(Integer belongs,Long querytime){
    PanoramaExample panoramaExample = new PanoramaExample();
    Criteria criteria = panoramaExample.createCriteria();
    if(querytime != null) criteria.andCreateTimeLessThanOrEqualTo(new Date(querytime));
    if(belongs>0){
      criteria.andBelongsEqualTo(belongs);
    }
    panoramaExample.setOrderByClause("create_time desc");
    return this.panoramaMapper.selectByExample(panoramaExample);
  }
  
  /**
   * 新增/修改全景
   * @param scene
   * @param id
   * @param belong
   * @return
   */
  public void add(Panorama panorama){
    Date date = new Date();
    //ID为空时新增
    panorama.setUpdateTime(date);
    if(panorama.getId() == null){
      panorama.setCreateTime(date);
      panoramaMapper.insert(panorama);
    }else{
      //有ID时修改
      panoramaMapper.updateByPrimaryKeySelective(panorama);
      }
  }
  
  /**
   * 删除全景
   * @param id
   * @return
   */
  public ResultData deletePanorama(Integer id){
    panoramaMapper.deleteByPrimaryKey(id);
    return ResultData.build().success();
  }
}
