<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--采购单数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="单据号" prop="name">
            <el-input
                v-model="queryParams.documentCode"
                placeholder="请输入单据号"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="供应商名称" label-width="100" prop="brand">
            <el-input
                v-model="queryParams.supplierName"
                placeholder="请输入供应商名称"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="供应商电话" label-width="100" prop="category">
            <el-input
                v-model="queryParams.supplierPhone"
                placeholder="请输入供应商电话"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select
                v-model="queryParams.status"
                placeholder="状态"
                clearable
                style="width: 240px"
            >
              <el-option
                  v-for="item in purchase_order_status"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"

              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
                type="primary"
                plain
                icon="Plus"
                @click="handleAdd"
                v-hasPermi="['purchase:order:add']"
            >新建订单</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
                type="info"
                plain
                icon="SoldOut"
                :disabled="single"
                @click="handleTakeDelivery"
                v-hasPermi="['purchase:order:edit']"
            >收货</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
                type="success"
                plain
                icon="WalletFilled"
                :disabled="single"
                @click="handleSettlement"
                v-hasPermi="['purchase:order:delete']"
            >结算</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['purchase:order:delete']"
            >删除</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="单据号" align="center" key="documentCode" prop="documentCode">
            <template #default="scope">
              <router-link :to="'/order/purchase-detail/' + scope.row.id" class="link-type" >
                {{ scope.row.documentCode }}
              </router-link>
            </template>
          </el-table-column>
          <el-table-column label="供应商名称" align="center" key="supplierName" prop="supplierName" :show-overflow-tooltip="true" />
          <el-table-column label="供应商电话" align="center" key="supplierPhone" prop="supplierPhone" :show-overflow-tooltip="true" />
          <el-table-column label="订货数量" align="center" key="originNumber" prop="originNumber" />
          <el-table-column label="订货金额" align="center" key="originAmount" prop="originAmount">
            <template #default="scope">
              <span>{{ "￥" + scope.row.originAmount + "元" }}</span>
            </template>
          </el-table-column>
          <el-table-column label="退货金额" align="center" key="originAmount" prop="originAmount">
            <template #default="scope">
              <span>{{ "￥" + scope.row.returnAmount + "元" }}</span>
            </template>
          </el-table-column>
          <el-table-column label="实际付款金额" align="center" key="originAmount" prop="originAmount" >
            <template #default="scope">
              <span>{{ "￥" + scope.row.actualAmount + "元" }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" key="status" prop="status">
            <template #default="scope">
              <dict-tag :options="purchase_order_status" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" key="remark" prop="remark" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间" align="center" key="createTime" width="160" prop="createTime">
            <template #default="scope">
              {{ scope.row.createTime }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="查看支付记录" placement="top" v-if="scope.row.status == '3'">
                <el-button link type="primary" icon="Edit" @click="showPictures(scope.row)" v-hasPermi="['inventory:check:edit']"></el-button>
              </el-tooltip>
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
      </el-col>
    </el-row>

    <select-supplier ref="selectRef" @ok="handleQuery"/>

    <!-- 订单结算对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="checkRef" label-width="80px">
        <el-row v-if="!showPicture">
          <el-col :span="24">
            <el-form-item label="实际支付" prop="actualAmount">
                <el-input-number
                    v-model="form.actualAmount"
                    placeholder="实际支付额，单位（元）"
                    clearable
                    :precision="2"
                    style="width: 240px"
                    />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="支付截图" prop="pictureList">
              <image-upload :limit="10" v-model:model-value="form.pictureList"/>
            </el-form-item>

          </el-col>
        </el-row>
        <el-row v-if="!showPicture">
          <el-col>
            <el-form-item label="备注" prop="remark">
              <el-input
                  type="textarea"
                  clearable
                  v-model="form.remark"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer" v-if="!showPicture">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
        <div class="dialog-footer" v-else>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {delOrder, getOrderList, settlement, takeDelivery, getPictures} from "@/api/order/purchase.js"
import DictTag from "@/components/DictTag/index.vue";
import SelectSupplier from "@/views/order/purchase/selectSupplier.vue";
import ImageUpload from "@/components/ImageUpload/index.vue";

const router = useRouter();
const { proxy } = getCurrentInstance();
const {
  purchase_order_status } =
    proxy.useDict( "purchase_order_status");

const orderList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const showPicture = ref(false);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    documentCode: undefined,
    supplierName: undefined,
    supplierPhone: undefined
  },
  rules: {
    actualAmount: [{ required: true, message: "实际支付额不能为空", trigger: "blur" }],
    pictureList: [{ required: true, message: "支付截图不能为空", trigger: "blur" }],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询采购单列表 */
function getList() {
  loading.value = true;
  getOrderList(queryParams.value).then(res => {
    loading.value = false;
    orderList.value = res.rows;
    total.value = res.total;
  });
};

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
};
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
};
/** 删除按钮操作 */
function handleDelete(row) {
  const orderIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除').then(function () {
    return delOrder(orderIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
};

/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
};

/** 新增按钮操作 */
function handleAdd() {
  proxy.$refs["selectRef"].show();
}

// 收货
function handleTakeDelivery() {
  const id = ids.value.join(",");
  proxy.$modal.confirm('是否确认收货,收货后不可修改').then(function () {
    return takeDelivery(id);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("收货成功");
  }).catch(() => {});
}

// 结算
function handleSettlement() {
  title.value = "订单结算";
  showPicture.value = false;
  open.value = true;
}

// 重置结算表单
function reset() {
  form.value = {
    orderId: undefined,
    actualAmount: undefined,
    pictureList: undefined
  };
  title.value = "订单结算";
  showPicture.value = false;
  proxy.resetForm("checkRef");
}

/** 结算的确认按钮 */
function submitForm() {
  proxy.$refs["checkRef"].validate(valid => {
    if (valid) {
      form.value.orderId = ids.value.join(",");
      settlement(form.value).then(res => {
        proxy.$modal.msgSuccess("结算成功");
        open.value = false;
        getList();
      });
    }
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

// 查看已付款订单的支付截图
function showPictures(row) {
  getPictures(row.id).then(res => {
    form.value.pictureList = res;
    title.value = "查看支付截图"
    showPicture.value = true;
    open.value = true;
  })
}

getList();
</script>
