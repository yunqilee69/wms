package com.yunqi.backend.service;

import com.yunqi.backend.common.constant.MenuConstants;
import com.yunqi.backend.common.constant.SystemConstants;
import com.yunqi.backend.mapper.MenuMapper;
import com.yunqi.backend.model.entity.Menu;
import java.time.LocalDateTime;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;














/**
 * 测试menu服务
 * @author liyunqi
 */
@SpringBootTest
public class MenuServiceTest {

    @Resource
    MenuMapper menuMapper;

    /**
     * 生成用户管理菜单
     */
    @Test
    void createUserMenus() {
        // 用户管理,目录
        Menu userManage = new Menu();
        userManage.setName("用户管理");
        userManage.setParentId(MenuConstants.ROOT_ID);
        userManage.setOrderNum(1);
        userManage.setRouterPath("user");
        userManage.setPathCode("");
        userManage.setComponent("");
        userManage.setQueryParams("");
        userManage.setMenuType(MenuConstants.TYPE_DIR);
        userManage.setStatus(SystemConstants.STATUS_VALID);
        userManage.setPerms("");
        userManage.setIcon("User");
        menuMapper.insert(userManage);
        userManage.setPathCode("0-" + userManage.getId());
        menuMapper.updateById(userManage);

        // 员工管理，菜单
        Menu empManage = new Menu();
        empManage.setName("员工管理");
        empManage.setParentId(userManage.getId());
        empManage.setOrderNum(1);
        empManage.setRouterPath("emp");
        empManage.setComponent("user/emp/index");
        empManage.setMenuType(MenuConstants.TYPE_MENU);
        empManage.setStatus(SystemConstants.STATUS_VALID);
        empManage.setPerms("user:emp:list");
        empManage.setIcon("emp");
        menuMapper.insert(empManage);
        empManage.setPathCode(userManage.getPathCode() + "-" + empManage.getId());
        menuMapper.updateById(empManage);

        // 客户管理，菜单
        Menu customManage = new Menu();
        customManage.setName("客户管理");
        customManage.setParentId(userManage.getId());
        customManage.setOrderNum(2);
        customManage.setRouterPath("custom");
        customManage.setComponent("user/custom/index");
        customManage.setMenuType(MenuConstants.TYPE_MENU);
        customManage.setStatus(SystemConstants.STATUS_VALID);
        customManage.setPerms("user:custom:list");
        customManage.setIcon("custom");
        menuMapper.insert(customManage);
        customManage.setPathCode(userManage.getPathCode() + "-" + customManage.getId());
        menuMapper.updateById(customManage);

        // 供应商管理，菜单
        Menu supplierManage = new Menu();
        supplierManage.setName("供应商管理");
        supplierManage.setParentId(userManage.getId());
        supplierManage.setOrderNum(3);
        supplierManage.setRouterPath("supplier");
        supplierManage.setComponent("user/supplier/index");
        supplierManage.setMenuType(MenuConstants.TYPE_MENU);
        supplierManage.setStatus(SystemConstants.STATUS_VALID);
        supplierManage.setPerms("user:supplier:list");
        supplierManage.setIcon("supplier");
        menuMapper.insert(supplierManage);
        supplierManage.setPathCode(userManage.getPathCode() + "-" + supplierManage.getId());
        menuMapper.updateById(supplierManage);

        // 派送员管理，菜单
        Menu courierManage = new Menu();
        courierManage.setName("派送员管理");
        courierManage.setParentId(userManage.getId());
        courierManage.setOrderNum(4);
        courierManage.setRouterPath("courier");
        courierManage.setComponent("user/courier/index");
        courierManage.setMenuType(MenuConstants.TYPE_MENU);
        courierManage.setStatus(SystemConstants.STATUS_VALID);
        courierManage.setPerms("user:courier:list");
        courierManage.setIcon("courier");
        menuMapper.insert(courierManage);
        courierManage.setPathCode(userManage.getPathCode() + "-" + courierManage.getId());
        menuMapper.updateById(courierManage);


    }

    /**
     * 生成采购菜单
     */
    @Test
    void createProcureMenus() {
        // 采购管理 目录
        Menu purchaseManage = new Menu();
        purchaseManage.setName("采购管理");
        purchaseManage.setParentId(MenuConstants.ROOT_ID);
        purchaseManage.setOrderNum(2);
        purchaseManage.setRouterPath("purchase");
        purchaseManage.setComponent("");
        purchaseManage.setMenuType(MenuConstants.TYPE_DIR);
        purchaseManage.setStatus(SystemConstants.STATUS_VALID);
        purchaseManage.setPerms("");
        purchaseManage.setIcon("暂定");
        menuMapper.insert(purchaseManage);
        purchaseManage.setPathCode("0-" + purchaseManage.getId());
        menuMapper.updateById(purchaseManage);

        // 采购订单 菜单
        Menu purchaseOrder = new Menu();
        purchaseOrder.setName("采购订单");
        purchaseOrder.setParentId(purchaseManage.getId());
        purchaseOrder.setOrderNum(1);
        purchaseOrder.setRouterPath("order");
        purchaseOrder.setComponent("purchase/order/index");
        purchaseOrder.setMenuType(MenuConstants.TYPE_MENU);
        purchaseOrder.setStatus(SystemConstants.STATUS_VALID);
        purchaseOrder.setPerms("purchase:order:list");
        purchaseOrder.setIcon("暂定");
        menuMapper.insert(purchaseOrder);
        purchaseOrder.setPathCode(purchaseManage.getPathCode() + "-" + purchaseOrder.getId());
        menuMapper.updateById(purchaseOrder);

        // 退货管理 菜单
        Menu returnManage = new Menu();
        returnManage.setName("退货管理");
        returnManage.setParentId(purchaseManage.getId());
        returnManage.setOrderNum(2);
        returnManage.setRouterPath("return");
        returnManage.setComponent("purchase/return/index");
        returnManage.setMenuType(MenuConstants.TYPE_MENU);
        returnManage.setStatus(SystemConstants.STATUS_VALID);
        returnManage.setPerms("purchase:return:list");
        returnManage.setIcon("暂定");
        menuMapper.insert(returnManage);
        returnManage.setPathCode(purchaseManage.getPathCode() + "-" + returnManage.getId());
        menuMapper.updateById(returnManage);

        // 采购结算 菜单
        Menu purchaseSettlement = new Menu();
        purchaseSettlement.setName("采购结算");
        purchaseSettlement.setParentId(purchaseManage.getId());
        purchaseSettlement.setOrderNum(3);
        purchaseSettlement.setRouterPath("settlement");
        purchaseSettlement.setComponent("purchase/settlement/index");
        purchaseSettlement.setMenuType(MenuConstants.TYPE_MENU);
        purchaseSettlement.setStatus(SystemConstants.STATUS_VALID);
        purchaseSettlement.setPerms("purchase:settlement:list");
        purchaseSettlement.setIcon("暂定");
        menuMapper.insert(purchaseSettlement);
        purchaseSettlement.setPathCode(purchaseManage.getPathCode() + "-" + purchaseSettlement.getId());
        menuMapper.updateById(purchaseSettlement);


    }

    /**
     * 生成销售菜单
     */
    @Test
    void createSaleMenus() {
        Menu saleManage = new Menu();
        saleManage.setName("销售管理");
        saleManage.setParentId(MenuConstants.ROOT_ID);
        saleManage.setOrderNum(3);
        saleManage.setRouterPath("sale");
        saleManage.setMenuType(MenuConstants.TYPE_DIR);
        saleManage.setStatus(SystemConstants.STATUS_VALID);
        saleManage.setIcon("暂定");
        menuMapper.insert(saleManage);
        saleManage.setPathCode("0-" + saleManage.getId());
        menuMapper.updateById(saleManage);

        // 销售订单 菜单
        Menu saleOrder = new Menu();
        saleOrder.setName("销售订单");
        saleOrder.setParentId(saleManage.getId());
        saleOrder.setOrderNum(1);
        saleOrder.setRouterPath("order");
        saleOrder.setPathCode("");
        saleOrder.setComponent("sale/order/index");
        saleOrder.setQueryParams("");
        saleOrder.setMenuType(MenuConstants.TYPE_MENU);
        saleOrder.setStatus(0);
        saleOrder.setPerms("sale:order:list");
        saleOrder.setIcon("暂定");
        menuMapper.insert(saleOrder);
        saleOrder.setPathCode(saleManage.getPathCode() + "-" + saleOrder.getId());
        menuMapper.updateById(saleOrder);

        // 送货管理 菜单
        Menu sendManage = new Menu();
        sendManage.setName("送货管理");
        sendManage.setParentId(saleManage.getId());
        sendManage.setOrderNum(2);
        sendManage.setRouterPath("send");
        sendManage.setPathCode("");
        sendManage.setComponent("sale/send/index");
        sendManage.setQueryParams("");
        sendManage.setMenuType(MenuConstants.TYPE_MENU);
        sendManage.setStatus(0);
        sendManage.setPerms("sale:send:list");
        sendManage.setIcon("");
        menuMapper.insert(sendManage);
        sendManage.setPathCode(saleManage.getPathCode() + "-" + sendManage.getId());
        menuMapper.updateById(sendManage);

        // 销售结算 菜单
        Menu saleSettleManage = new Menu();
        saleSettleManage.setName("销售结算");
        saleSettleManage.setParentId(saleManage.getId());
        saleSettleManage.setOrderNum(3);
        saleSettleManage.setRouterPath("settlement");
        saleSettleManage.setPathCode("");
        saleSettleManage.setComponent("sale/settlement/index");
        saleSettleManage.setQueryParams("");
        saleSettleManage.setMenuType(MenuConstants.TYPE_MENU);
        saleSettleManage.setStatus(0);
        saleSettleManage.setPerms("sale:settlement:list");
        saleSettleManage.setIcon("");
        menuMapper.insert(saleSettleManage);
        saleSettleManage.setPathCode(saleManage.getPathCode() + "-" + saleSettleManage.getId());
        menuMapper.updateById(saleSettleManage);

    }

    /**
     * 生成库存菜单
     */
    @Test
    void createInventoryMenus() {
        Menu inventoryManage = new Menu();
        inventoryManage.setName("库存管理");
        inventoryManage.setParentId(0L);
        inventoryManage.setOrderNum(4);
        inventoryManage.setRouterPath("inventory");
        inventoryManage.setPathCode("");
        inventoryManage.setComponent("");
        inventoryManage.setQueryParams("");
        inventoryManage.setMenuType(MenuConstants.TYPE_DIR);
        inventoryManage.setStatus(0);
        inventoryManage.setPerms("");
        inventoryManage.setIcon("暂定");
        menuMapper.insert(inventoryManage);
        inventoryManage.setPathCode("0-" + inventoryManage.getId());
        menuMapper.updateById(inventoryManage);

        // 货物信息
        Menu waresInfo = new Menu();
        waresInfo.setName("货物信息");
        waresInfo.setParentId(inventoryManage.getId());
        waresInfo.setOrderNum(1);
        waresInfo.setRouterPath("waresInfo");
        waresInfo.setPathCode("");
        waresInfo.setComponent("inventory/waresInfo/index");
        waresInfo.setQueryParams("");
        waresInfo.setMenuType(MenuConstants.TYPE_MENU);
        waresInfo.setStatus(0);
        waresInfo.setPerms("inventory:waresInfo:list");
        menuMapper.insert(waresInfo);
        waresInfo.setPathCode(inventoryManage.getPathCode() + "-" + waresInfo.getId());
        menuMapper.updateById(waresInfo);

        // 库存记录
        Menu inventoryRecord = new Menu();
        inventoryRecord.setName("库存记录");
        inventoryRecord.setParentId(inventoryManage.getId());
        inventoryRecord.setOrderNum(2);
        inventoryRecord.setRouterPath("record");
        inventoryRecord.setPathCode("");
        inventoryRecord.setComponent("inventory/record/index");
        inventoryRecord.setQueryParams("");
        inventoryRecord.setMenuType(MenuConstants.TYPE_MENU);
        inventoryRecord.setStatus(0);
        inventoryRecord.setPerms("inventory:record:list");
        menuMapper.insert(inventoryRecord);
        inventoryRecord.setPathCode(inventoryManage.getPathCode() + "-" + inventoryRecord.getId());
        menuMapper.updateById(inventoryRecord);

        // 货位管理
        Menu waresPosition = new Menu();
        waresPosition.setName("货位管理");
        waresPosition.setParentId(inventoryManage.getId());
        waresPosition.setOrderNum(3);
        waresPosition.setRouterPath("waresPosition");
        waresPosition.setPathCode("");
        waresPosition.setComponent("inventory/waresPosition/index");
        waresPosition.setQueryParams("");
        waresPosition.setMenuType(MenuConstants.TYPE_MENU);
        waresPosition.setStatus(0);
        waresPosition.setPerms("inventory:waresPosition:list");
        menuMapper.insert(waresPosition);
        waresPosition.setPathCode(inventoryManage.getPathCode() + "-" + waresPosition.getId());
        menuMapper.updateById(waresPosition);

        // 库存盘点
        Menu inventoryCheck = new Menu();
        inventoryCheck.setName("库存盘点");
        inventoryCheck.setParentId(inventoryManage.getId());
        inventoryCheck.setOrderNum(4);
        inventoryCheck.setRouterPath("check");
        inventoryCheck.setPathCode("");
        inventoryCheck.setComponent("inventory/check/index");
        inventoryCheck.setQueryParams("");
        inventoryCheck.setMenuType(MenuConstants.TYPE_MENU);
        inventoryCheck.setStatus(0);
        inventoryCheck.setPerms("inventory:check:list");
        menuMapper.insert(inventoryCheck);
        inventoryCheck.setPathCode(inventoryManage.getPathCode() + "-" + inventoryCheck.getId());
        menuMapper.updateById(inventoryCheck);

    }

    /**
     * 生成数据看板
     */
    @Test
    void createDataBoardMenus() {
        Menu dataBoard = new Menu();
        dataBoard.setName("数据看板");
        dataBoard.setParentId(0L);
        dataBoard.setOrderNum(5);
        dataBoard.setRouterPath("dataBoard");
        dataBoard.setComponent("");
        dataBoard.setQueryParams("");
        dataBoard.setMenuType(MenuConstants.TYPE_DIR);
        dataBoard.setStatus(0);
        menuMapper.insert(dataBoard);
        dataBoard.setPathCode("0-" + dataBoard.getId());
        menuMapper.updateById(dataBoard);

        // 库存看板
        Menu inventory = new Menu();
        inventory.setName("库存看板");
        inventory.setParentId(dataBoard.getId());
        inventory.setOrderNum(1);
        inventory.setRouterPath("inventory");
        inventory.setPathCode("");
        inventory.setComponent("dataBoard/inventory/index");
        inventory.setQueryParams("");
        inventory.setMenuType(MenuConstants.TYPE_MENU);
        inventory.setStatus(0);
        inventory.setPerms("dataBoard:inventory:list");
        menuMapper.insert(inventory);
        inventory.setPathCode(dataBoard.getPathCode() + "-" + inventory.getId());
        menuMapper.updateById(inventory);

    }

    /**
     * 生成系统管理
     */
    @Test
    void createSystemManageMenus() {
        Menu system = new Menu();
        system.setName("系统管理");
        system.setParentId(0L);
        system.setOrderNum(6);
        system.setRouterPath("system");
        system.setComponent("");
        system.setQueryParams("");
        system.setMenuType(MenuConstants.TYPE_DIR);
        system.setStatus(0);
        system.setIcon("system");
        menuMapper.insert(system);
        system.setPathCode("0-" + system.getId());
        menuMapper.updateById(system);

        // 角色管理
        Menu role = new Menu();
        role.setName("角色管理");
        role.setParentId(system.getId());
        role.setOrderNum(1);
        role.setRouterPath("role");
        role.setPathCode("");
        role.setComponent("system/role/index");
        role.setQueryParams("");
        role.setMenuType(MenuConstants.TYPE_MENU);
        role.setStatus(0);
        role.setPerms("system:role:list");
        role.setIcon("peoples");
        menuMapper.insert(role);
        role.setPathCode(system.getPathCode() + "-" + role.getId());
        menuMapper.updateById(role);

        // 菜单管理
        Menu menu = new Menu();
        menu.setName("菜单管理");
        menu.setParentId(system.getId());
        menu.setOrderNum(2);
        menu.setRouterPath("menu");
        menu.setPathCode("");
        menu.setComponent("system/menu/index");
        menu.setQueryParams("");
        menu.setMenuType(MenuConstants.TYPE_MENU);
        menu.setStatus(0);
        menu.setPerms("system:menu:list");
        menu.setIcon("tree-table");
        menuMapper.insert(menu);
        menu.setPathCode(system.getPathCode() + "-" + menu.getId());
        menuMapper.updateById(menu);

        // 字典管理
        Menu dict = new Menu();
        dict.setName("字典管理");
        dict.setParentId(system.getId());
        dict.setOrderNum(3);
        dict.setRouterPath("dict");
        dict.setPathCode("");
        dict.setComponent("system/dict/index");
        dict.setQueryParams("");
        dict.setMenuType(MenuConstants.TYPE_MENU);
        dict.setStatus(0);
        dict.setPerms("system:dict:list");
        dict.setIcon("dict");
        menuMapper.insert(dict);
        dict.setPathCode(system.getPathCode() + "-" + dict.getId());
        menuMapper.updateById(dict);
    }
}
