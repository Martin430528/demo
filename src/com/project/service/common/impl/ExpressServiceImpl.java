package com.project.service.common.impl;

import com.project.service.common.IExpressService;
import com.project.utils.HttpClientUtil;

import org.springframework.stereotype.Service;

@Service("expressService")
public class ExpressServiceImpl implements IExpressService {

    /**
     * 爱查快递参数
     */
    public static final String EXPRESS_ID = "104058";
    public static final String EXPRESS_KEY = "ae0e4263dbdf21313f1ee61ff639a736";

    public String autoQuery(String expressNumber) {
        try {
            String url = "http://api.ickd.cn/?id=" + EXPRESS_ID + "&secret="
                    + EXPRESS_KEY + "&com=auto&nu=" + expressNumber;
            String text = HttpClientUtil.createAndGetText(url);
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
