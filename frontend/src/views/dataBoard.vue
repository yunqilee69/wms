<template>
  <div class="app-container home">
    <el-row>
      <el-col :span="4">
        <el-radio-group v-model="radio" label="size control">
          <el-radio-button label="近7天"/>
          <el-radio-button label="本周"/>
          <el-radio-button label="本月"/>
        </el-radio-group>
      </el-col>
    </el-row>

    <br>
    <!--  图表  -->
    <el-row :gutter="30">
      <el-col :span="12">
        <div ref="purchaseRef" class="chart"></div>
      </el-col>
      <el-col :span="12">
        <div ref="saleRef" class="chart"></div>
      </el-col>
    </el-row>
    <el-row :gutter="30">
      <el-col :span="12">
        <div ref="inventoryNumberRef" class="chart"></div>
      </el-col>
      <el-col :span="12">
        <div ref="inventoryAmountRef" class="chart"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import * as echarts from "echarts";
import {getInventoryAmount, getInventoryNumber, getPurchaseMoney, getSaleMoney} from "@/api/charts.js";

const radio = ref('近7天')
const begin = ref();
const end = ref();

const purchaseRef = ref();
const saleRef = ref();
const inventoryNumberRef = ref();
const inventoryAmountRef = ref();

// 监视选择的日期范围
watch(radio, (newValue)=> {
  console.log(newValue)
  const currentDate = new Date();
  if (newValue === '近7天') {
    // 查找近7天的数据
    end.value = formatDate(currentDate);
    // 获取近7天的第一天
    const firstDay = new Date(currentDate.setDate(currentDate.getDate() -6));
    begin.value = formatDate(firstDay);

  } else if (newValue === '本周') {
    // 查找本周的数据
    // 获取本周的第一天（周一为第一天）
    const firstDay = new Date(currentDate.setDate(currentDate.getDate() - currentDate.getDay() + (currentDate.getDay() === 0 ? -6 : 1)));
    // 获取本周的最后一天（周日为最后一天）
    const lastDay = new Date(currentDate.setDate(currentDate.getDate() + 6));
    begin.value = formatDate(firstDay);
    end.value = formatDate(lastDay);
  } else if (newValue === '本月') {
    // 查找本月的数据
    // 获取本月的第一天
    const firstDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
    // 获取下个月的第一天，然后减去一天得到本月的最后一天
    const nextMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1);
    const lastDay = new Date(nextMonth.setDate(nextMonth.getDate() - 1));
    begin.value = formatDate(firstDay);
    end.value = formatDate(lastDay);
  }

  // 获取数据
  init();
}, {immediate: true})

// 格式化日期为 yyyy-mm-dd
function formatDate(date) {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  return year + '-' + month + '-' + day;
}

// 处理chart
function initLineChart(id, data) {
  let chart = echarts.getInstanceByDom(id);
  if (chart == null) {
    chart = echarts.init(id);
  }

  const option = {
    title: {
      text: data.title,
    },
    xAxis: {
      name: data.xName,
      type: 'category',
      data: data.xAxis,
      axisTick: {
        alignWithLabel: true,
        length: 8, // 设置刻度的长度
      },
      axisLabel: {
        interval: 'auto', // 设置为 0 表示全部显示，也可以设置为其他值，如2，表示间隔显示
        //rotate: data.xAxis.length > 10 ? -25 : 0
      }
    },
    yAxis: {
      name: data.yName,
      type: 'value'
    },
    tooltip: {

    },
    series: [
      {
        name: data.yName,
        data: data.yAxis,
        type: 'line',
        smooth: true
      }
    ]
  };
  chart.setOption(option);
}

// 页面的初始化
function init() {
  getPurchaseMoney(begin.value, end.value).then(res=> {
    res.title = "采购金额";
    res.xName = "日期"
    res.yName = "单位（元）"
    initLineChart(purchaseRef.value, res);
  });
  getSaleMoney(begin.value, end.value).then(res=> {
    res.title = "销售金额";
    res.xName = "日期"
    res.yName = "单位（元）"
    initLineChart(saleRef.value, res);
  });
  getInventoryNumber(begin.value, end.value).then(res=> {
    res.title = "仓库货物总数量";
    res.xName = "日期"
    res.yName = "单位（件）"
    initLineChart(inventoryNumberRef.value, res);
  });
  getInventoryAmount(begin.value, end.value).then(res=> {
    res.title = "仓库货物总金额";
    res.xName = "日期"
    res.yName = "单位（元）"
    initLineChart(inventoryAmountRef.value, res);
  });
}


init();

</script>

<style>
.chart {
  width: 100%;
  height: 300px;
}
</style>