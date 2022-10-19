package com.yangmy.cobweb.common.core.utils.database;

import java.lang.annotation.*;

/**
 * @author YangMingYang
 * @since 2022/10/5
 */
@Inherited
//标明使用位置
@Target(ElementType.FIELD)
// 标明声明周期
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

}
