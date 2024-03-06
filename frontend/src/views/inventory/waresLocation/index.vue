<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--货位数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="货位名称" prop="name">
            <el-input
                v-model="queryParams.name"
                placeholder="请输入货位名称"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
                v-model="queryParams.status"
                filterable
                placeholder="请选择状态"
                @keyup.enter="handleQuery"
            >
              <el-option
                  v-for="item in sys_normal_disable"
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
                v-hasPermi="['system:user:add']"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
                type="success"
                plain
                icon="Edit"
                :disabled="single"
                @click="handleUpdate"
                v-hasPermi="['system:user:edit']"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['system:user:remove']"
            >删除</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="locationList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="货位名称" align="center" key="name" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="状态" align="center" key="status" prop="status" :show-overflow-tooltip="true">
            <template #default="scope">
              <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="容量" align="center" key="capacity" prop="capacity" />
          <el-table-column label="备注" align="center" key="remark" prop="remark" :show-overflow-tooltip="true"/>r
          <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="修改" placement="top">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:user:edit']"></el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:user:remove']"></el-button>
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

    <!-- 添加或修改货位配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="locationRef" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="货位名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入货位名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select
                  v-model="form.status"
                  filterable
                  placeholder="请选择状态"
              >
                <el-option
                    v-for="item in sys_normal_disable"
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
            <el-form-item prop="capacity">
              <template #label>
                  <span>
                     <el-tooltip content="自定义的容量，用于员工自行分辨" placement="top">
                        <el-icon><question-filled /></el-icon>
                     </el-tooltip>
                     容量
                  </span>
              </template>
              <el-input v-model="form.capacity" placeholder="请输入容量"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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

  </div>
</template>

<script setup name="User">
import { getLocationList, getLocationById, addLocation, delLocation, updateLocation } from "@/api/inventory/location"
import DictTag from "@/components/DictTag/index.vue";

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict( "sys_normal_disable");

const locationList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    status: undefined
  },
  rules: {
    name: [{ required: true, message: "货位名称不能为空", trigger: "blur" }],
    status: [{ required: true, message: "货位状态不能为空", trigger: "blur" }],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询货位列表 */
function getList() {
  loading.value = true;
  getLocationList(queryParams.value).then(res => {
    loading.value = false;
    locationList.value = res.rows;
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
  const locationIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除').then(function () {
    return delLocation(locationIds);
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
    name: undefined,
    brand: undefined,
    category: undefined,
    barCode: undefined,
    spec: undefined,
    unit: undefined,
    purchasePrice: undefined,
    salePrice: undefined,
    description: undefined,
  };
  proxy.resetForm("locationRef");
};
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
};
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加货位";
};
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id || ids.value;
  getLocationById(id).then(response => {
    form.value = response;
    open.value = true;
    title.value = "修改货位";
  });
};
/** 提交按钮 */
function submitForm() {
  proxy.$refs["locationRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateLocation(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addLocation(form.value).then(response => {
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
