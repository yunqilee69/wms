<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>库存预警</span>
        </div>
      </template>
      <el-alert
          v-if="recordList == null || recordList.length === 0"
          type="success"
          show-icon
          title="没有货物处于报警状态~"
          :closable="false"
      />
      <el-alert
          v-else
          type="error"
          show-icon
          title="请检查下面的货物，库存数量低于报警阈值！"
          :closable="false"
      />
      <el-descriptions
          v-for="item in recordList"
          :key="item.id"
          class="margin-top"
          :column="4"
          border
          style="margin-top: 10px"
      >
        <el-descriptions-item
          label="货位名称"
        >
          {{ item.locationName }}
        </el-descriptions-item>
        <el-descriptions-item
          label="货物名称"
        >
          {{ item.wareName }}
        </el-descriptions-item>
        <el-descriptions-item
            label="库存数量"
        >
          {{ item.number }}
        </el-descriptions-item>
        <el-descriptions-item
          label="报警阈值"
        >
          {{ item.alarmThreshold }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>订单结算预警</span>
        </div>
      </template>
      <el-alert
          v-if="orderList == null || orderList.length === 0 "
          type="success"
          show-icon
          title="没有订单处于未结算状态~"
          :closable="false"
      />
      <el-alert
          v-else
          type="error"
          show-icon
          title="请检查下面的订单，订单状态处于已收货/已送货，但未付款/收款"
          :closable="false"
      />
      <el-descriptions
          v-for="item in orderList"
          :key="item.id"
          class="margin-top"
          :column="4"
          border
          style="margin-top: 10px"
      >
        <el-descriptions-item
            label="单据号"
        >
          {{ item.documentCode }}
        </el-descriptions-item>
        <el-descriptions-item
            label="订单类型"
        >
          {{ item.orderName }}
        </el-descriptions-item>
        <el-descriptions-item
            label="应支付/收款"
        >
          {{ item.amount }}
        </el-descriptions-item>
        <el-descriptions-item
            label="收货/送货时间"
        >
          {{ item.wareTime }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import {getAlarmRecord, getAlarmOrder} from "@/api/index.js";

const recordList = ref([])
const orderList = ref([])

getAlarmRecord().then(res => {
  recordList.value = res;
})

getAlarmOrder().then(res => {
  orderList.value = res;
})

</script>

<style>
.box-card {
  margin: 20px;
}
.cell-item {
  display: flex;
  align-items: center;
}
</style>