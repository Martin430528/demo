package com.project.service.common;

/**
 * 快递查询服务接口
 *
 * @author GuoZhiLong
 * @date 2015年11月1日 下午6:28:29
 */
public interface IExpressService {

    /**
     * 根据单号自动查询
     *
     * @param expressNumber 快递单号
     * @return String
     * @throws
     * @author GuoZhiLong
     */
    String autoQuery(String expressNumber);

}
