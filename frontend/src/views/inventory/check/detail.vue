<template>
  <div class="app-container">
    <!--货物数据-->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="货物名称" prop="wareName">
        <el-input
            v-model="queryParams.wareName"
            placeholder="请输入货物名称"
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
      <el-form-item label="条形码" prop="barCode">
        <el-input
            v-model="queryParams.barCode"
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['inventory:check:edit']"
        >新增货物</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Plus"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['inventory:check:edit']"
        >更新货物</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="CircleClose"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['inventory:check:edit']"
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

    <el-table v-loading="loading" :data="checkDetailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="货物图片" prop="picture" >
        <template #default="scope">
          <image-preview :src="scope.row.picture" :height="50" :width="50" />
        </template>
      </el-table-column>
      <el-table-column label="货位名称" prop="locationName" />
      <el-table-column label="货物名称" prop="wareName" :show-overflow-tooltip="true" />
      <el-table-column label="品牌" prop="wareBrand" :show-overflow-tooltip="true" >
        <template #default="scope">
          <dict-tag :options="sys_inventory_brand" :value="scope.row.wareBrand" />
        </template>
      </el-table-column>
      <el-table-column label="条形码" prop="barCode" :show-overflow-tooltip="true" />
      <el-table-column label="前数量" prop="preNumber" :show-overflow-tooltip="true" />
      <el-table-column label="后数量" prop="nowNumber" :show-overflow-tooltip="true" />
      <el-table-column label="前生产日期" align="center" prop="status">
        <template #default="scope">
          <span>{{ scope.row.preProductionDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="后生产日期" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ scope.row.nowProductionDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-tooltip content="修改" placement="top">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['inventory:check:edit']"></el-button>
          </el-tooltip>
          <el-tooltip content="删除" placement="top">
            <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['inventory:check:edit']"></el-button>
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

    <!-- 添加或修改货物配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="checkRef" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="盘点后数量" prop="nowNumber">
              <el-input v-model="form.nowNumber" placeholder="请输入盘点后数量" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="盘点后生产日期" prop="name">
              <el-date-picker
                  v-model="form.nowProductionDate"
                  type="date"
                  placeholder="选择日期时间"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <select-record ref="selectRef" @ok="handleQuery" :checkId="queryParams.checkId"/>
  </div>
</template>

<script setup>
import { getCheckDetailList, addCheckDetail, delCheckDetail, getCheckDetailById, updateCheckDetail} from "@/api/inventory/checkDetail"
import SelectRecord from "@/views/inventory/check/selectRecord.vue";
import ImagePreview from "@/components/ImagePreview/index.vue";

const route = useRoute();
const { proxy } = getCurrentInstance();
const { sys_inventory_brand } = proxy.useDict("sys_inventory_brand");

const checkDetailList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const multiple = ref(true);
const total = ref(0);
const open = ref(false);
const ids = ref([]);
const single = ref(true);
const title = ref("");
const update = ref(false)

const data = reactive({
  detailForm: {},
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    checkId: route.params.checkId,
    wareName: undefined,
    wareBrand: undefined,
    barCode: undefined
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询盘点细节数据 */
function getList() {
  loading.value = true;
  getCheckDetailList(queryParams.value).then(response => {
    checkDetailList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
// 返回按钮
function handleClose() {
  const obj = { path: "/inventory/check" };
  proxy.$tab.closeOpenPage(obj);
}
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
  const checkDetailIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除').then(function () {
    return delCheckDetail(checkDetailIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
};

/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    nowNumber: undefined,
    nowProductionDate: undefined

  };
  update.value = false;
  proxy.resetForm("checkRef");
};

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
};

/** 新增按钮操作 */
function handleAdd() {
  proxy.$refs["selectRef"].show();
};
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id || ids.value;
  getCheckDetailById(id).then(response => {
    form.value = response;
    open.value = true;
    title.value = "修改盘点单";
    update.value = true;
  });
};
/** 提交按钮 */
function submitForm() {
  proxy.$refs["checkRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateCheckDetail(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addCheckDetail(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
};


getList();
</script>
