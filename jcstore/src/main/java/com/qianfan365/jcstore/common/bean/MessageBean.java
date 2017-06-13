package com.qianfan365.jcstore.common.bean;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.qianfan365.jcstore.common.constant.MessageConstant;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageType;
import com.qianfan365.jcstore.common.pojo.Message;

public class MessageBean extends Message{
  
  private static final long serialVersionUID = 1L;
  
  private String detailName;       //详情名称
  private Boolean detailFalg;      //详情标记
  private Boolean print;           //打印判断标记
  
  public MessageBean(Message message){
    BeanUtils.copyProperties(message, this);
    String detailName = MessageConstant.messageDetail().get(this.getMessageType());
    if(MessageType.SYSTEM_MESSAGE.equals(this.getMessageType())){
      boolean detailFalg = StringUtils.isNotBlank(this.getLink());
      this.setDetailFalg(detailFalg);
      this.setDetailName(detailFalg?detailName:null);
    }else if(MessageType.MONTHLY_STATISTICS.equals(this.getMessageType())){
    	this.setDetailFalg(true);
    }else{
      this.setDetailFalg(detailName!=null);
      this.setDetailName(detailName);
    }
    
    //1.7期取消>>改为图标,估注掉
//    if(this.getDetailFalg()){
//      this.setContent(this.getContent()+">>");
//    }
  }
  
  public String getDetailName() {
    return detailName;
  }
  public void setDetailName(String detailName) {
    this.detailName = detailName;
  }
  public Boolean getDetailFalg() {
    return detailFalg;
  }
  public void setDetailFalg(Boolean detailFalg) {
    this.detailFalg = detailFalg;
  }
	public Boolean getPrint() {
		return print;
	}
	public void setPrint(Boolean print) {
		this.print = print;
	}
  
}
