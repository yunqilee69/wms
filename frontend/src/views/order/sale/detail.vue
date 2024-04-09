<template>
  <div class="app-container">
    <!-- 订单信息  -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单详细信息</span>
        </div>
      </template>
      <el-descriptions
          class="margin-top"
          :column="4"
          border
          style="margin-top: 10px"
      >
        <el-descriptions-item
            label="单据号"
        > {{ orderInfo.documentCode }}
        </el-descriptions-item>
        <el-descriptions-item
            label="状态"
        >
          <dict-tag :value="orderInfo.status" :options="sale_order_status"/>
        </el-descriptions-item>
        <el-descriptions-item
            label="客户名称"
        >
          {{ orderInfo.customerName }}
        </el-descriptions-item>
        <el-descriptions-item
            label="客户电话"
        >
          {{ orderInfo.customerPhone }}
        </el-descriptions-item>
        <el-descriptions-item
            label="订货数量"
        >
          {{ orderInfo.originNumber }}
        </el-descriptions-item>
        <el-descriptions-item
            label="订货金额"
        >
          {{ orderInfo.originAmount }}
        </el-descriptions-item>
        <el-descriptions-item
            label="退货数量"
        >
          {{ orderInfo.returnNumber }}
        </el-descriptions-item>
        <el-descriptions-item
            label="退货金额"
        >
          {{ orderInfo.returnAmount }}
        </el-descriptions-item>
        <el-descriptions-item
            v-if="orderInfo.status != '1'"
            label="送件人名称"
        >
          {{ orderInfo.deliveryName }}
        </el-descriptions-item>
        <el-descriptions-item
            v-if="orderInfo.status != '1'"
            label="送件人电话"
        >
          {{ orderInfo.deliveryPhone }}
        </el-descriptions-item>
        <el-descriptions-item
            v-if="orderInfo.status != '1'"
            label="送件时间"
        >
          {{ orderInfo.deliveryTime }}
        </el-descriptions-item>
        <el-descriptions-item
            v-if="orderInfo.status != '1'"
            label="实际收款金额"
        >
          {{ orderInfo.actualAmount }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
    <br>

    <!-- 查询条件   -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="货位名称" prop="locationName">
        <el-input
            v-model="queryParams.locationName"
            placeholder="请输入货位名称"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="货物名称" prop="wareName">
        <el-input
            v-model="queryParams.wareName"
            placeholder="请输入货物名称"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="条形码" prop="wareBarCode">
        <el-input
            v-model="queryParams.wareBarCode"
            placeholder="请输入条形码"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 按钮   -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['order:sale:edit']"
        >新增货物</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="info"
            plain
            icon="Minus"
            @click="handleReturn"
            v-hasPermi="['order:sale:edit']"
        >退货</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="multiple"
            @click="handleSetNumber"
            v-hasPermi="['order:sale:edit']"
        >设置数量</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="multiple"
            @click="handleSetAmount"
            v-hasPermi="['order:sale:edit']"
        >设置价格</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="CircleClose"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['order:sale:edit']"
        >删除货物</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Close"
            @click="handleClose"
        >关闭</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格   -->
    <el-table v-loading="loading" :data="tableDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="图片" align="center" key="picture" prop="picture">
        <template #default="scope">
          <image-preview :src="scope.row.picture" :height="50" :width="50" />
        </template>
      </el-table-column>
      <el-table-column label="货位名称" align="center" key="locationName" prop="locationName" :show-overflow-tooltip="true" />
      <el-table-column label="货物名称" align="center" prop="wareName"/>
      <el-table-column label="品牌" align="center" key="wareBrand" prop="wareBrand"/>
      <el-table-column label="条形码" align="center" key="wareBarCode" prop="wareBarCode" :show-overflow-tooltip="true" />
      <el-table-column label="规格" align="center" key="wareSpec" prop="wareSpec" />
      <el-table-column label="单位" align="center" key="wareUnit" prop="wareUnit" />
      <el-table-column label="数量" align="center" key="wareNumber" prop="wareNumber" />
      <el-table-column label="进价" align="center" key="salePrice" prop="salePrice">
        <template #default="scope">
          {{ "￥" + scope.row.wareSalePrice + "元" }}
        </template>
      </el-table-column>
      <el-table-column label="总金额" align="center" key="totalAmount" prop="totalAmount">
        <template #default="scope">
          {{ "￥" + (scope.row.wareSalePrice * scope.row.wareNumber) + "元" }}
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" key="type" prop="type">
          <template #default="scope">
            <dict-tag :options="order_detail_type" :value="scope.row.type"/>
          </template>
      </el-table-column>
    </el-table>
    <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

<!--    新增货物-->
    <select-record ref="recordRef" @ok="handleQuery" :order-id="orderId" :detail-type="detailType" />
  </div>
</template>

<script setup>
import { getOrderById, getOrderDetailList, delDetail, setDetailNumber, setDetailAmount } from "@/api/order/sale.js"
import DictTag from "@/components/DictTag/index.vue";
import SelectRecord from "@/views/order/sale/selectRecord.vue";

const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();
const {
  sale_order_status, order_detail_type } =
    proxy.useDict( "sale_order_status", "order_detail_type");

const orderId = route.params.orderId;
const orderInfo = ref({});
const tableDataList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const detailType = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderId: orderId,
    locationName: undefined,
    wareName: undefined,
    barCode: undefined
  }
});

const { queryParams, form } = toRefs(data);

/**
 * 获取订单信息
 */
function getOrderInfo() {
  getOrderById(orderId).then(res => {
    orderInfo.value = res;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}


/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/**
 * 获取表格数据
 */
function getList() {
  loading.value = true;
  getOrderDetailList(queryParams.value).then(res => {
    loading.value = false;
    tableDataList.value = res.rows;
    total.value = res.total;
  });
  getOrderInfo();
}

// 新增货物
function handleAdd() {
  // 送货
  detailType.value = "送货";
  nextTick(() => {
    proxy.$refs['recordRef'].show();
  });
}

// 退货
function handleReturn() {
  // 退货
  detailType.value = "退货";
  nextTick(() => {
    proxy.$refs['recordRef'].show();
  });
}

// 设置数量
function handleSetNumber() {
  const detailIds = ids.value.join(",");
  proxy.$prompt('请输入的新数量', "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    closeOnClickModal: false,
    inputPattern: /^\d*$/,
    inputErrorMessage: "必须为数字",
  }).then(({ value }) => {
    setDetailNumber({detailIds:detailIds, number:value}).then(response => {
      getList();
      proxy.$modal.msgSuccess("修改成功");
    });
  }).catch(() => {});
}

// 设置价格
function handleSetAmount() {
  const detailIds = ids.value.join(",");
  proxy.$prompt('请输入的新价格', "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    closeOnClickModal: false,
    inputPattern: /^\d*$/,
    inputErrorMessage: "必须为数字",
  }).then(({ value }) => {
    setDetailAmount({detailIds:detailIds, amount:value}).then(response => {
      getList();
      proxy.$modal.msgSuccess("修改成功");
    });
  }).catch(() => {});
}

function handleDelete() {
  const detailIds = ids.value;
  proxy.$modal.confirm('是否确认删除').then(function () {
    return delDetail(detailIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

// 关闭界面
function handleClose() {
  router.push("/order/sale")
}

getList();
</script>

<style>

</style>