package com.jacotest.jacocotest.common.response;


import com.jacotest.jacocotest.common.errorcode.BaseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: code-diff-parent
 * @Package: com.dr.common.response
 * @Description: java类作用描述
 * @Author: duanrui
 * @CreateDate: 2021/3/5 13:48
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniqueApoResponse<T> extends ApiResponse<T>{
    private String uniqueData;

    public UniqueApoResponse<T> success(T data,String uniqueData) {
        super.setCode(BaseCode.SUCCESS.getCode());
        super.setMsg(BaseCode.SUCCESS.getInfo());
        super.setData(data);
        this.uniqueData = uniqueData;
        return this;
    }
}
