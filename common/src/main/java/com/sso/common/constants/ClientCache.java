package com.sso.common.constants;

/**
 * sso客户端缓存。是在拦截器中进行注销的。
 *
 */
public interface ClientCache {

    /**
     * 判断是否存在失效的token
     */
    boolean containsInvalidKey(String key);

    /**
     * 添加失效的缓存
     */
    void addInvalidKey(String key);

    /**
     * 删除失效的缓存
     */
    void removeInvalidKey(String key);
}
