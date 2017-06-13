package com.qianfan365.jcstore.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qianfan365.jcstore.common.pojo.Content;
import com.qianfan365.jcstore.common.pojo.ContentExample;
import com.qianfan365.jcstore.dao.inter.ContentMapper;

/**
 * 内容设置相关操作
 * @author liuhaoran
 *
 */
@Service("contentService")
public class ContentService {
  @Autowired
  private ContentMapper contentMapper;
  
  /**
   * 根据id查询设置的对应内容
   * 
   * @param id
   * @return
   */
  public Content find(int id) {
    Content content = contentMapper.selectByPrimaryKey(id);
    if(content == null){
      content = new Content();
      content.setId(id);
      content.setContent("");
    }
    return content;
  }
  
  /**
   * 保存内容设置
   * 
   * @param content
   * @return
   */
  public int saveContent(Content content) {
    Date date = new Date();
    
    ContentExample example = new ContentExample();
    example.createCriteria();
    int count = 3 - contentMapper.countByExample(example);
    if(count > 0){
      for(int i=0;i<count;i++){
        Content record = new Content();
        record.setContent("");
        record.setCreatetime(date);
        record.setUpdatetime(date);
        contentMapper.insertSelective(record);
      }
    }
    
    content.setUpdatetime(date);
    return contentMapper.updateByPrimaryKeySelective(content);
  }
}
