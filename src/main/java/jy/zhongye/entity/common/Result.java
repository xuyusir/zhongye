package jy.zhongye.entity.common;

import java.io.Serializable;

/**
 * @program: zhongye *
 * @description: 自定义返回数据结构体
 * @author: Mr.Xuyu
 * @create: 2019-01-23 11:39
 **/
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    // 响应业务状态
    private Integer status;
    //消息代码
    private Integer code;
    // 响应消息
    private String msg;
    // 响应中的数据
    private Object data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Result(Integer status,Integer code,String msg,Object data){
        this.status=status;
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    //Enum class
    public Result(ResultEnum resultEnum,Object data){
        this.status=resultEnum.getStatus();
        this.code=resultEnum.getCode();
        this.msg=resultEnum.getMsg();
        this.data=data;
    }


}
