<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--货位数据-->
      <el-col :span="24" :xs="24">
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
                placeholder="请选择货物名称"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>

          <el-form-item label="品牌" prop="wareBrand">
            <el-select
                v-model="queryParams.wareBrand"
                filterable
                clearable
                placeholder="请选择品牌"
                @keyup.enter="handleQuery"
            >
              <el-option
                  v-for="item in sys_inventory_brand"
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
                icon="edit"
                :disabled="multiple"
                @click="handleAlarmThreshold"
                v-hasPermi="['system:user:remove']"
            >批量设置阈值</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="货位名称" align="center" key="locationName" prop="locationName" :show-overflow-tooltip="true" />
          <el-table-column label="货物名称" align="center" prop="wareName" >
            <template #default="scope">
              <span @click="showWareDetail(scope.row.wareId)" class="link-type">
                {{ scope.row.wareName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="品牌" align="center" key="wareBrand" prop="wareBrand">
            <template #default="scope">
              <dict-tag :options="sys_inventory_brand" :value="scope.row.wareBrand" />
            </template>
          </el-table-column>
          <el-table-column label="在库数量" align="center" key="number" prop="number" />
          <el-table-column label="报警阈值" align="center" key="alarmThreshold" prop="alarmThreshold" />
          <el-table-column label="规格" align="center" key="wareSpec" prop="wareSpec" :show-overflow-tooltip="true">
            <template #default="scope">
              <dict-tag :options="sys_inventory_spec" :value="scope.row.wareSpec" />
            </template>
          </el-table-column>
          <el-table-column label="单位" align="center" key="wareUnit" prop="wareUnit" :show-overflow-tooltip="true">
            <template #default="scope">
              <dict-tag :options="sys_inventory_unit" :value="scope.row.wareUnit" />
            </template>
          </el-table-column>
          <el-table-column label="总金额" align="center" key="totalAmount" prop="totalAmount">
            <template #default="scope">
              {{ "￥" + scope.row.totalAmount + "元" }}
            </template>
          </el-table-column>
          <el-table-column label="生产日期" align="center" prop="productionDate" >
            <template #default="scope">
              <span>{{ scope.row.productionDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="保质期(月)" align="center" prop="qualityMonth" >
            <template #default="scope">
              <span>{{ scope.row.qualityMonth }}</span>
            </template>
          </el-table-column>
          <el-table-column label="有效期" align="center" prop="guaranteeDate" >
            <template #default="scope">
              <span>{{ scope.row.guaranteeDate }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-button link type="primary" icon="Edit" @click="handleAlarmThreshold(scope.row)" v-hasPermi="['system:user:edit']">设置阈值</el-button>
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

    <!-- 查看货物详情   -->
    <el-dialog :title="'货物详情'" v-model="detailShow" width="500px" append-to-body>
      <el-form :model="detailForm" :rules="rules" ref="wareRef" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="货物名称" prop="name">
              <el-input v-model="detailForm.name" placeholder="请输入货物名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="品牌" prop="brand">
              <el-select
                  v-model="detailForm.brand"
                  filterable
                  placeholder="请选择品牌"
              >
                <el-option
                    v-for="item in sys_inventory_brand"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="类别" prop="category">
              <el-select
                  v-model="detailForm.category"
                  filterable
                  placeholder="请选择类别"
              >
                <el-option
                    v-for="item in sys_inventory_category"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="条形码" prop="barCode">
              <el-input v-model="detailForm.barCode" placeholder="请输入条形码"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="规格" prop="spec">
              <el-select
                  v-model="detailForm.spec"
                  filterable
                  placeholder="请选择规格"
              >
                <el-option
                    v-for="item in sys_inventory_spec"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位" prop="unit">
              <el-select
                  v-model="detailForm.unit"
                  filterable
                  placeholder="请选择单位"
              >
                <el-option
                    v-for="item in sys_inventory_unit"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="进价" prop="purchasePrice">
              <el-input v-model="detailForm.purchasePrice" placeholder="请输入进价"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="售价" prop="salePrice">
              <el-input v-model="detailForm.salePrice" placeholder="请输入售价"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="描述">
              <el-input v-model="detailForm.description" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup name="User">
import { getRecordList, updateAlarmThreshold } from "@/api/inventory/record"
import { getWareById  } from "@/api/inventory/ware"

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict( "sys_normal_disable");

const recordList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const detailShow = ref(false);

const data = reactive({
  detailForm: {},
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    locationName: undefined,
    wareName: undefined,
    wareBrand: undefined
  }
});

const { queryParams, form, rules, detailForm } = toRefs(data);
const {
  sys_inventory_brand,
  sys_inventory_spec,
  sys_inventory_unit,
  sys_inventory_category } =
    proxy.useDict( "sys_inventory_brand",
        "sys_inventory_spec",
        "sys_inventory_unit",
        "sys_inventory_category");

/** 查询货位列表 */
function getList() {
  loading.value = true;
  getRecordList(queryParams.value).then(res => {
    loading.value = false;
    recordList.value = res.rows;
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

/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 显示货物的详细数据 */
function showWareDetail(wareId) {
  getWareById(wareId).then((res) => {
    detailForm.value = res;
    detailShow.value = true;
  });
  console.log(wareId);
}

/** 设置货物阈值 */
function handleAlarmThreshold(row) {
  const tempName = row.wareName || "选中货物";
  proxy.$prompt('请输入' + tempName + '的报警阈值', "设置阈值", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    closeOnClickModal: false,
    inputPattern: /(^[1-9]([0-9]+)?$)|(^[0-9]{1}$)/,
    inputErrorMessage: "报警阈值必须为正确数字",
  }).then(({ value }) => {
    const recordIds = row.id || ids.value.join(",");
    updateAlarmThreshold(recordIds, value).then(response => {
      proxy.$modal.msgSuccess("设置成功");
      getList();
    });
  }).catch(() => {});
}

getList();
</script>
