<template>
  <!-- 授权用户 -->
  <el-dialog title="选择库存" v-model="visible" width="800px" top="5vh" append-to-body>
    <el-form :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item label="货物名称" prop="wareName">
        <el-input
            v-model="queryParams.wareName"
            placeholder="请输入货物名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="货物品牌" prop="wareBrand">
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
      <el-form-item label="条形码" prop="barCode">
        <el-input
            v-model="queryParams.barCode"
            placeholder="请输入条形码"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row>
      <el-table @row-click="clickRow" ref="refTable" :data="recordList" @selection-change="handleSelectionChange" height="260px">
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
        <el-table-column label="条形码" align="center" key="barCode" prop="barCode" :show-overflow-tooltip="true" />
        <el-table-column label="在库数量" align="center" key="number" prop="number" />
        <el-table-column label="生产日期" align="center" prop="productionDate" >
          <template #default="scope">
            <span>{{ scope.row.productionDate }}</span>
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
    </el-row>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="handleSelectRecord">确 定</el-button>
        <el-button @click="visible = false">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="SelectUser">
import { unCheckRecordList, addCheckRecordAll } from "@/api/inventory/checkDetail"

const props = defineProps({
  checkId: {
    type: [Number, String]
  }
});

const { proxy } = getCurrentInstance();
const { sys_inventory_brand } = proxy.useDict("sys_inventory_brand");

const recordList = ref([]);
const visible = ref(false);
const total = ref(0);
const recordIds = ref([]);

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  checkId: undefined,
  wareName: undefined,
  wareBrand: undefined,
  barCode: undefined
});

// 显示弹框
function show() {
  queryParams.checkId = props.checkId;
  getList();
  visible.value = true;
}
/**选择行 */
function clickRow(row) {
  proxy.$refs["refTable"].toggleRowSelection(row);
}
// 多选框选中数据
function handleSelectionChange(selection) {
  recordIds.value = selection.map(item => item.id);
}
// 查询表数据
function getList() {
  unCheckRecordList(queryParams).then(res => {
    recordList.value = res.rows;
    total.value = res.total;
  });
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}
const emit = defineEmits(["ok"]);
/** 选择授权用户操作 */
function handleSelectRecord() {
  const checkId = queryParams.checkId;
  const uIds = recordIds.value.join(",");
  if (uIds == "") {
    proxy.$modal.msgError("请选择要盘点的货物");
    return;
  }
  addCheckRecordAll({ checkId: checkId, recordIds: uIds }).then(res => {
    proxy.$modal.msgSuccess("添加成功");
    visible.value = false;
    emit("ok");
  });
}

defineExpose({
  show,
});
</script>
