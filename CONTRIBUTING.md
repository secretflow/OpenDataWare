# 贡献与协作

## 参与方向

* [opendatacomponent-core](opendatacomponent-core) 主要定义了元件相关的实体类，抽象类或接口。
* 交付接口扩展，可以实现交付接口com.cec.deliver.DataComponentDelivery来进行扩展。交付模块主要目的在于实时、持久的为应用提供丰富的元件查询服务。
* 元件生产接口扩展，可以按照元件的数据结构扩展计算框架、丰富Flink元件生产相关的能力；
* 元件可逆性审核能力扩展，可以在[opendatacomponent-examine-sdk](opendatacomponent-examine-sdk)模块中package com.cec.examine.reversibility;包中扩展可逆审核算子。
* 可以不断完善各模块的单例测试；

## 设计讨论

如果您希望有更深入的讨论和对未来版本规划的相关想法，可以联系我们。