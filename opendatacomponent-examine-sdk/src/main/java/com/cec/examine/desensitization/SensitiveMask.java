package com.cec.examine.desensitization;

import com.cec.examine.template.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * 对遮蔽的抽象
 */
public interface SensitiveMask {

    List<Config> configs = new ArrayList<>(10);

    void setConfigs(List<Config> configs);

}
