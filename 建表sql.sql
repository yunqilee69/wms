### 用户表
DROP TABLE IF exists tb_user;
create table `tb_user` (
    id BIGINT PRIMARY KEY COMMENT "主键",

    username VARCHAR(64) COMMENT "账号",
    password VARCHAR(64) COMMENT "密码",
    nickname VARCHAR(64) COMMENT "昵称",
    email VARCHAR(64) COMMENT "邮箱",
    phone VARCHAR(64) COMMENT "手机号",
    status VARCHAR(64) COMMENT "状态",
    gender VARCHAR(64) COMMENT "性别",
    avatar VARCHAR(256) COMMENT "头像",

    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 角色表
DROP TABLE IF exists tb_role;
create table `tb_role` (
    id BIGINT PRIMARY KEY COMMENT "主键",

    name VARCHAR(64) COMMENT "角色名称",
    role_key VARCHAR(64) COMMENT "角色权限字符串",
    order_num INT COMMENT "显示顺序",
    status VARCHAR(64) COMMENT "角色状态",

    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 菜单表
DROP TABLE IF exists tb_menu;
create table `tb_menu` (
    id BIGINT PRIMARY KEY COMMENT "主键",

    name VARCHAR(64) COMMENT "菜单名称",
    parent_id BIGINT COMMENT "父菜单ID",
    order_num INT COMMENT "显示顺序",
    router_path VARCHAR(256) COMMENT "路由路径",
    component VARCHAR(64) COMMENT "组件路径",
    query_params VARCHAR(256) COMMENT "路由参数",
    path_code VARCHAR(256) COMMENT "总路径",
    menu_type VARCHAR(64) COMMENT "菜单类型 （M目录 C菜单 F按钮）",
    status VARCHAR(64) COMMENT "状态",
    perms VARCHAR(64) COMMENT "权限标识",
    icon VARCHAR(64) COMMENT "图标",

    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 用户角色表
DROP TABLE IF exists tb_user_role;
create table `tb_user_role` (
    id BIGINT PRIMARY KEY COMMENT "主键",
    user_id BIGINT COMMENT "用户ID",
    role_id BIGINT COMMENT "角色ID"
);

### 角色菜单表
DROP TABLE IF exists tb_role_menu;
create table `tb_role_menu` (
    id BIGINT PRIMARY KEY COMMENT "主键",

    role_id BIGINT COMMENT "角色ID",
    menu_id BIGINT COMMENT "菜单ID"

);

### 字典表
drop table if exists tb_dict_type;
create table tb_dict_type(
    id BIGINT PRIMARY KEY COMMENT '主键',
    name varchar(64) COMMENT '字典名称',
    code varchar(64) COMMENT '字典编码',
    remark varchar(256) COMMENT '备注',
    order_num INT COMMENT '显示顺序',
    status VARCHAR(64) COMMENT '状态',
    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 字典项表
drop table if exists tb_dict_item;
create table tb_dict_item(
    id BIGINT PRIMARY KEY COMMENT '主键', 
    type_id BIGINT COMMENT '字典类型ID',
    type_code varchar(64) COMMENT '字典编码',
    label varchar(64) COMMENT '字典项名称',
    value varchar(64) COMMENT '字典项值',
    order_num INT COMMENT '显示顺序',
    status VARCHAR(64) COMMENT '状态',
    list_class varchar(64) COMMENT '表格回显样式',
    remark VARCHAR(255) COMMENT '备注',
    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 下面为业务表

### 客户表
DROP TABLE IF exists tb_user_customer;
create table tb_user_customer (
	id BIGINT primary key COMMENT "主键",

    nickname VARCHAR(64) COMMENT "客户名称",
    name VARCHAR(64) COMMENT "联系人名称",
    email VARCHAR(64) COMMENT "邮箱",
    phone VARCHAR(64) COMMENT "手机号",
    gender VARCHAR(64) COMMENT "性别",
    avatar VARCHAR(256) COMMENT "头像",
    address VARCHAR(255) COMMENT "地址",
    
    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 供应商表
DROP TABLE IF exists tb_user_supplier;
create table tb_user_supplier (
	id BIGINT primary key COMMENT "主键",

    nickname VARCHAR(64) COMMENT "供应商名称",
    name VARCHAR(64) COMMENT "联系人名称",
    email VARCHAR(64) COMMENT "邮箱",
    phone VARCHAR(64) COMMENT "手机号",
    gender VARCHAR(64) COMMENT "性别",
    avatar VARCHAR(256) COMMENT "头像",
    address VARCHAR(255) COMMENT "地址",
    
    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 货物信息表
DROP TABLE IF exists tb_inventory_ware;
create table tb_inventory_ware (
	id BIGINT primary key COMMENT "主键",

    name VARCHAR(64) COMMENT "商品名称",
    brand VARCHAR(64) COMMENT "品牌",
    spec VARCHAR(64) COMMENT "规格",
    unit VARCHAR(64) COMMENT "单位",
    bar_code VARCHAR(64) COMMENT "条形码",
    description VARCHAR(255) COMMENT "描述",
    purchase_price DECIMAL(10, 2) COMMENT "进价",
    sale_price DECIMAL(10, 2) COMMENT "售价",
    category VARCHAR(64) COMMENT "分类",
   	picture VARCHAR(255) COMMENT "图片",
    quality_month int COMMENT "保质期（月）",

    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 货物金额变化表
DROP TABLE IF exists tb_inventory_ware_money;
create table tb_inventory_ware_money (
    id BIGINT primary key COMMENT "主键",
    ware_id BIGINT COMMENT "货物id",
    sale_price DECIMAL(10, 2) COMMENT "售价",
    purchase_price DECIMAL(10, 2) COMMENT "进价",
    record_time DATETIME COMMENT "更新时间"
);

### 货位表
DROP TABLE IF exists tb_inventory_location;
create table tb_inventory_location (
	id BIGINT primary key COMMENT "主键",

    name VARCHAR(64) COMMENT "名称",
    status VARCHAR(64) COMMENT "状态",
    capacity VARCHAR(64) COMMENT "容量",
    remark VARCHAR(255) COMMENT "备注",
    
    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 库存记录表
DROP TABLE IF exists tb_inventory_record;
create table tb_inventory_record (
	id BIGINT primary key COMMENT "主键",
	location_id BIGINT COMMENT "货位id",
    ware_id BIGINT COMMENT "货物id",
    
    location_name VARCHAR(64) COMMENT "货位名称",
    
    number int COMMENT "数量",
    production_date DATE COMMENT "生产日期",
    quality_month int COMMENT "保质期，单位（月)",
    guarantee_date DATE COMMENT "有效期",
    alarm_threshold int COMMENT "报警阈值",
    total_value DECIMAL(10, 2) COMMENT "总价值",
    
    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 库存信息表
### 每天都会存储一条记录
DROP TABLE IF exists tb_inventory_info;
create table tb_inventory_info (
    id BIGINT primary key COMMENT "主键",
    total_amount DECIMAL(10, 2) COMMENT "仓库总价值",
	total_number int COMMENT "仓库总数量",
    record_time DATETIME COMMENT "记录时间"
);

### 库存盘点表
DROP TABLE IF exists tb_inventory_check;
create table tb_inventory_check (
	id BIGINT primary key COMMENT "主键",
    
    name VARCHAR(64) COMMENT "盘点表",
    type VARCHAR(64) COMMENT "类型",
    remark VARCHAR(255) COMMENT "备注",
    status VARCHAR(64) COMMENT "状态",
    
    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 库存盘点细节表
DROP TABLE IF exists tb_inventory_check_detail;
create table tb_inventory_check_detail (
	id BIGINT primary key COMMENT "主键",
    check_id BIGINT COMMENT "盘点表id",
    
    record_id BIGINT COMMENT "库存记录id",
    pre_number int COMMENT "盘点前数量",
    now_number int COMMENT "盘点后数量",
    pre_production_date DATE COMMENT "盘点前生产日期",
    now_production_date DATE COMMENT "盘点后生产日期"
    
);

### 采购订单表
DROP TABLE IF exists tb_order_purchase;
create table tb_order_purchase (
	id BIGINT primary key COMMENT "主键",
    
    document_code VARCHAR(255) COMMENT "订单单据号",
    status VARCHAR(64) COMMENT "订单状态",
    
    supplier_id BIGINT COMMENT "供应商id",
    supplier_name VARCHAR(64) COMMENT "供应商名称",
    supplier_phone VARCHAR(64) COMMENT "供应商电话",
    receiver_name VARCHAR(64) COMMENT "收件人",
    receiver_phone VARCHAR(64) COMMENT "收件人电话",
    receipt_time DATETIME COMMENT "收货时间",
    origin_number int DEFAULT 0 COMMENT "货物数量",
    origin_amount DECIMAL(10, 2) DEFAULT 0 COMMENT "原本货物金额",
    return_number int DEFAULT 0 COMMENT "退货数量",
    return_amount DECIMAL(10, 2) DEFAULT 0 COMMENT "退款金额",
    remark VARCHAR(1024) COMMENT "备注",
    actual_amount DECIMAL(10, 2) DEFAULT 0 COMMENT "实际付款金额",
    
    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 采购订单详情表
DROP TABLE IF exists tb_order_purchase_detail;
create table tb_order_purchase_detail (
	id BIGINT primary key COMMENT "主键",
    
    order_id BIGINT COMMENT "订单id",
    ware_id BIGINT COMMENT "货物id",
    record_id BIGINT COMMENT "库存id",
    
    picture VARCHAR(255) COMMENT "货物图片",
    location_name VARCHAR(64) COMMENT "货位名称",
    ware_name VARCHAR(64) COMMENT "货物名称",
    ware_brand VARCHAR(64) COMMENT "货物品牌",
    ware_spec VARCHAR(64) COMMENT "货物规格",
    ware_unit VARCHAR(64) COMMENT "货物单位",
    ware_bar_code VARCHAR(64) COMMENT "货物条形码",
    ware_purchase_price DECIMAL(10, 2) COMMENT "货物进价",
    ware_sale_price DECIMAL(10, 2) COMMENT "货物售价",
    ware_number int COMMENT "货物数量",
    
    type VARCHAR(64) COMMENT "类型（进货，退货）"
);

### 销售订单表
DROP TABLE IF exists tb_order_sale;
create table tb_order_sale (
	id BIGINT primary key COMMENT "主键",
    
    document_code VARCHAR(255) COMMENT "订单单据号",
    status VARCHAR(64) COMMENT "订单状态",
    
    customer_id BIGINT COMMENT "客户id",
    delivery_name VARCHAR(64) COMMENT "送货人名称",
    delivery_phone VARCHAR(64) COMMENT "送货人电话",
    customer_name VARCHAR(64) COMMENT "客户名称",
    customer_phone VARCHAR(64) COMMENT "客户电话",
    customer_address VARCHAR(255) COMMENT "客户地址",
    delivery_time DATETIME COMMENT "送货时间",
    origin_number int DEFAULT 0 COMMENT "货物数量",
    origin_amount DECIMAL(10, 2) DEFAULT 0 COMMENT "原本货物金额",
    return_number int DEFAULT 0 COMMENT "退货数量",
    return_amount DECIMAL(10, 2) DEFAULT 0 COMMENT "退款金额",
    remark VARCHAR(1024) COMMENT "备注",
    actual_amount DECIMAL(10, 2) DEFAULT 0 COMMENT "实际付款金额",
    
    creator VARCHAR(64) COMMENT "创建人",
    create_time DATETIME COMMENT "创建时间",
    updater VARCHAR(64) COMMENT "更新人",
    update_time DATETIME COMMENT "更新时间"
);

### 销售订单详情表
DROP TABLE IF exists tb_order_sale_detail;
create table tb_order_sale_detail (
	id BIGINT primary key COMMENT "主键",
    
    order_id BIGINT COMMENT "订单id",
    ware_id BIGINT COMMENT "货物id",
    record_id BIGINT COMMENT "库存id",
    
    picture VARCHAR(255) COMMENT "货物图片",
    location_name VARCHAR(64) COMMENT "货位名称",
    ware_name VARCHAR(64) COMMENT "货物名称",
    ware_brand VARCHAR(64) COMMENT "货物品牌",
    ware_spec VARCHAR(64) COMMENT "货物规格",
    ware_unit VARCHAR(64) COMMENT "货物单位",
    ware_bar_code VARCHAR(64) COMMENT "货物条形码",
    ware_purchase_price DECIMAL(10, 2) COMMENT "货物进价",
    ware_sale_price DECIMAL(10, 2) COMMENT "货物售价",
    ware_number int COMMENT "货物数量",
    
    type VARCHAR(64) COMMENT "类型（送货，退货）"
);

### 订单结算截图表
DROP TABLE IF exists tb_order_settlement;
create table tb_order_settlement (
    id BIGINT primary key COMMENT "主键",
    order_id BIGINT COMMENT "订单id",
    picture VARCHAR(255) COMMENT "图片"
);