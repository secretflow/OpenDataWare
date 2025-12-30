package com.cec.examine.template;

import java.util.List;

public abstract class AbstractConfigLoader {

    protected String resourcePath;

    protected String remoteURL;
    /**
     * 从本地ResourceFile加载配置
     * @return
     */
    public abstract List loadFromResourceFile();
    /**
     * 从本地远端加载配置
     * @return
     */
    public abstract List loadFromRemote();

}
