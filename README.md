<p align="center">
<a href="./README.zh-CN.md">简体中文</a>｜<a href="./README.md">English</a>
</p>

# OpenDataWare

The DataWare open-source project is a data security computation framework. Its core technology employs a set of compliant and secure standardized production processes to ensure the compliance, irreversibility, and ease of use in the circulation of data elements.

## Features

* Supports the definition of three DataWare types: Modal DataWare, Composed DataWare, Combinatorial DataWare;
* Supports the definition of DataWare metadata;
* Standard audit operators for DataWare metadata and data;
* Flink offline production suite for DataWare manufacturing;
* Netty delivery service suite for DataWare delivery;
* Core DataWare classes, used to define standard interfaces for DataWare models, production, delivery, and audit, facilitating modular extensibility;
* A series of operators for DataWare production and audit:
  + Data Irreversibility Audit Operators: Similarity Audit, Relevance Audit, Outlier Detection Audit, Identifier Recognition Operator;
  + General Operators: Quality Check Operator, Information Item Recognition Operator;
  + Compliance Operators: Illicit/Violence/Terrorism Dictionary Recognition Operator.

## Introduction to the Three Types of DataWares

* Modal DataWare: Typically contains only two fields. The subject field is generally the unique identifier of a business entity, which could be an device, person, company, etc. The feature field is usually a generalized tag. During DataWare delivery, the corresponding feature value is generally retrieved by directly querying the subject field.

| **Structure** | **Main Column**    | **Feature** | 
|:--------------|:-------------------|:------------|
| *Column*      | *ID*               | *Tags*      |
| content       | 11010*200001010000 | Medium      |
| content       | 11010*200001010001 | High        |
| content       | 11010*200001010002 | Low         |


* Composed DataWare: There are no specific requirements on the number of fields. It stores detailed data but must not contain any subject identifier; the subject identifier needs to be removed or anonymized. During DataWare delivery, detailed data can typically be retrieved in batches using an index and batch size.

| **Structure** | **Index** | **detail1**                        | **detail2**                      |
|:--------------|:----------|:-----------------------------------|:---------------------------------|
| *Column*      | *Index*   | *Total Investment(in 10,000 Yuan)* | *Total Income(in 10,000 Yuan)*   |
| content       | 0         | 1200                               | 3200                             |
| content       | 1         | 2200                               | -300                             |
| content       | 2         | 900                                | 200                              |     


* Combinatorial DataWare：Contains a subject field, multiple feature fields, multiple detailed fields, and multiple query fields. During DataWare delivery, querying by the subject field can only return the multiple feature fields, not the detailed fields. The detailed fields can be retrieved through batch queries using the query fields.

| **Structure** | **Index**          | **detail1**    | **detail2**        |
|:--------------|:-------------------|:---------------|:-------------------|
| *Column*      | *ID*               | *Tags1*        | *Tags2*            |
| content       | 11010*200001010000 | Medium         | food               |
| content       | 11010*200001010001 | High           | cars               |
| content       | 11010*200001010002 | Low            | mother and baby    |


| **Structure** | **Index** | **detail1**                           | **detail2**                     | **detail3**  |
|:--------------|:----------|:--------------------------------------|:--------------------------------|:-------------|
| *Column*      | *Index*   | *Total Investment(in 10,000 Yuan)*    | *Total Income(in 10,000 Yuan)*  | *Province*   |
| content       | 0         | 1200                                  | 3200                            | Beijing      |
| content       | 1         | 2200                                  | -300                            | Shanghai     |
| content       | 2         | 900                                   | 200                             | Shenzhen     |

## Two Architectures for DataWare Production

### Call As Production

![call-as-production.png](readme-resource/call-as-production.png)

Real-time Production & Delivery: As shown in the diagram, the red arrows represent the call chain, and the blue arrows represent the asynchronous data flow. When an application calls the DataWare delivery API, the delivery API directly initiates a production request to the Flink job. This enables the job to immediately fetch data, produce the DataWare, and return it to the delivery service, which finally delivers it to the application. The produced DataWare is asynchronously written to a cache or database, allowing it to be matched (cache hit) for subsequent deliveries of the same DataWare.

### Pre-Production

![pre-production.png](readme-resource/pre-production.png)

Cache-First & Pre-Production: As shown in the diagram, the red arrows represent the call chain, and the blue arrows represent the asynchronous data flow. When an application calls the DataWare delivery API, the request primarily attempts to retrieve data from the cache or database and does not trigger DataWare production. If a DataWare needs to be returned, pre-production must be initiated.

## Getting Started

```shell
git clone https://github.com/secretflow/OpenDataWare
cd OpenDataWare
mvn install 
```
Alternatively, you can use an IDE tool such as IntelliJ IDEA or Eclipse to import the Maven project, and then directly run the Main function in the [OpenDataWare-examples](opendataware-examples) module.

## Code Examples

Please refer to and test run the code in the [OpenDataWare-examples](opendataware-examples) module.
We have prepared a small set of sample data in the Resource directory of this module for use with the relevant example programs.
You can use the following code to directly inspect the data structures of DataWare metadata, Composed DataWare, Configurable DataWares, and Combinatorial DataWare.
```java
//...
public abstract class DCExample<T extends DataWare> {

    public static void main(String[] args) {
        ModalDataWareExample modalDataWareExample = new ModalDataWareExample("DataWareDemo/ModalDataWareDemo.csv");
        System.out.println("这是一个模态数据元件的数据部分：" + modalDataWareExample.getJSON());
        ComposedDataWareExample composedDataWareExample = new ComposedDataWareExample("DataWareDemo/ComposedDataWareDemo.csv");
        System.out.println("这是一个组态数据元件的数据部分：" + composedDataWareExample.getJSON());
        CombinatorialDataWareExample combinatorialDataWareExample = new CombinatorialDataWareExample("DataWareDemo/CombinatorialDataWareDemo.csv");
        System.out.println("这是一个组合态数据元件的数据部分：" + combinatorialDataWareExample.getJSON());
    }
}
```
## How to produce DataWares

The project currently supports two production modes: Call As Production and Pre-Production. It utilizes Flink SQL on Flink-2.1.0 for DataWare manufacturing.

### Call As Production

"Call As Production" means that DataWare production is triggered when the DataWare delivery interface is invoked. The data sources required for DataWare production are typically multiple HTTP RESTful APIs. The production logic involves integrating and processing the responses from these multiple APIs to immediately return the DataWare result.
The running example has been placed in the [OpenDataWare-examples](opendataware-examples) module and can be started first.

```Java
com.cec.example.Delivery.netty.DemoServer.main
```
After startup, the DemoServerwill load the SQL related to "Production-on-Call" in its configPluginmethod. The DataWare delivery interface is:

```Java
http://localhost:8088/cap
```
该接口对应的Controller为：
```Java
com.cec.example.Delivery.netty.controller.CallAsProductionController
```
Default parameters have been configured in the Controller code, so you can directly request the "call_modal" url path in your browser to get the result.

```json
{
  "requestId": "3b159d66-9cd0-4549-b623-b859e1065fd0",
  "enterprise_code": "900000000000000000",
  "processTime": "2025-11-25T09:50:06",
  "ratio": "70.0"
}
```
The configuration of sources and sinks in Flink SQL leverages specific connectors.<br>
source
```SQL
--资源表模型
CREATE TABLE enterprise_info_tbl (
    code STRING,
    msg STRING,
    data ROW<
        enterprise_code STRING,--企业统一信用代码
        revenue_of_recycle DOUBLE,--废弃物回收营收(亿)
        measurement STRING,--物流环节减废措施
        update_time TIMESTAMP(3),--更新日期
        scale INT,--公司规模(人)
        registered_assets DOUBLE,--公司注册资产(亿)
        revenue_of_total DOUBLE--公司营收(亿)
    >,
   enterprise_code STRING NULL --虚拟列，为了where语句可以直接获取到“enterprise_code”
) WITH (
  'connector' = 'easy-rest',
  'url' = 'http://localhost:8088/r',
  'method' = 'GET',
  'interval' = '0',--0代表只请求一次，大于0则按照毫秒进行等到
  'format' = 'json'
)

--资源表模型
CREATE TABLE enterprise_city_tbl (
    code STRING,
    msg STRING,
    data ROW<
        enterprise_code STRING,--企业统一信用代码
        city STRING --城市
    >,
    enterprise_code STRING NULL --虚拟列，为了where语句可以直接获取到“enterprise_code”
) WITH (
  'connector' = 'easy-rest',
  'url' = 'http://localhost:8088/rd',
  'method' = 'GET',
  'interval' = '0',--0代表只请求一次，大于0则按照毫秒进行等到
  'format' = 'json',
  'listen.open' = 'true'
)

--为每一次请求生成请求ID与时间戳
CREATE TABLE request_tbl (
    requestId STRING,
    processTime TIMESTAMP(3) --调用日期
) WITH (
    'connector' = 'easy-rest',
    'url' = '',
    'method' = '$request',
    'interval' = '0',--0代表只请求一次，大于0则按照毫秒进行等到
    'format' = 'json',
    'listen.open' = 'true'
)


```
sink
```SQL
--模态元件表模型
--主体字段为企业信用代码，特征字段为废弃物回收业务占比
CREATE TABLE dc001_tbl (
    requestId STRING,
    processTime TIMESTAMP(3), --调用日期
    enterprise_code STRING,--企业信用代码
    ratio DOUBLE--废弃物回收业务占比
) WITH (
    'connector' = 'blockingMap'
)
```
The SQL used in production includes placeholders like $numberwhich act as variables that can be passed during invocation, thereby flexibly supporting parameterized conditions in the WHERE clause.
```SQL
SELECT
    requestId,
    processTime,
    t2.data.enterprise_code,
    t1.data.revenue_of_recycle/t1.data.revenue_of_total*100 AS ratio
FROM request_tbl, enterprise_info_tbl t1 LEFT JOIN enterprise_city_tbl t2
ON t1.data.enterprise_code = t2.data.enterprise_code
WHERE t1.enterprise_code='$1' AND t2.enterprise_code='$1'
```

### Pre-Production
"Pre-production of DataWares" means that a delivery call does not directly trigger DataWare production. Typically, DataWares are produced in advance and stored in a storage medium accessible by the delivery call, such as a query engine like Redis or a database. The call directly reads the pre-stored DataWare data. To facilitate future expansion of delivery storage, we provide a Flink Sink that allows specifying the delivery interface implementation class name to support writing to various DataWare delivery storages.<br>
in the [OpenDataWare-examples](opendataware-examples) module, you can find example code in 
```java
com.cec.example.ProductionProcesses.flink.DataDataWareProductionBatchExamples;
```
The Sink ddl sql is:

```SQL
--模态元件表模型
--链接方式为Delivery输出
--主体字段为企业信用代码，特征字段为废弃物回收业务占比
CREATE TABLE modal_delivery_tbl (
    enterprise_code STRING,--企业信用代码
    ratio DOUBLE--废弃物回收业务占比
) WITH (
    'connector' = 'delivery',
    'db.name' = 'im_delivery',
    'dc.type' = 'modal',
    'dc.id' = 'DC002',
    'dc.main.key' = 'enterprise_code',
    'dc.delivery.class' = 'com.cec.deliver.InnerMemory.DataDataWareDeliveryInnerMemory'
)
```

## Compilation

Compilation environment preparation: 

* Unix-like environment (we use Linux, Mac OS X, Cygwin, WSL, Windows PowerShell)
* Git
* Maven (we require version 3.9.9)
* Java (version 20,21)

## Modules

* [OpenDataWare-core](opendataware-core) The core DataWare classes. All other modules depend on this core module. It contains the entity class definitions for DataWares and DataWare metadata, as well as interface definitions for extending delivery suites and production suites. The core module does not depend on any third-party code packages.
* [OpenDataWare-delivery-service](opendataware-delivery-service) The DataWare delivery service suite. It provides the framework code for writing DataWare data to databases and building high-performance services. The delivery module is extensible and can rely on service middleware such as Tomcat, TongWeb, or Netty.
* [OpenDataWare-production-flink](opendataware-production-flink) The DataWare production suite. It provides the framework code for DataWare production. The production module is extensible and can rely on mature computation frameworks like Flink or Spark.
* [OpenDataWare-examine-sdk](opendataware-examine-sdk) A collection of operators for standard DataWare auditing, DataWare production auditing, and data desensitization SDK operators.
* [OpenDataWare-examples](opendataware-examples) Demonstrates various sub-process examples within the DataWare production workflow. The example code is designed to depend on all modules to best illustrate how each module is called.

