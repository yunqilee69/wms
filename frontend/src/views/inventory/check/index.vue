<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--盘点数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="单据号" prop="documentCode">
            <el-input
                v-model="queryParams.documentCode"
                placeholder="请输入单据号"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="名称" prop="name">
            <el-input
                v-model="queryParams.name"
                placeholder="请输入名称"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="类型" prop="type">
            <el-select
                v-model="queryParams.type"
                filterable
                clearable
                placeholder="请选择类型"
                @keyup.enter="handleQuery"
            >
              <el-option
                  v-for="item in sys_inventory_check_type"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
                v-model="queryParams.status"
                filterable
                clearable
                placeholder="请选择状态"
                @keyup.enter="handleQuery"
            >
              <el-option
                  v-for="item in sys_inventory_check_status"
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
                v-hasPermi="['inventory:check:add']"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
                type="success"
                plain
                icon="Edit"
                :disabled="single"
                @click="handleUpdate"
                v-hasPermi="['inventory:check:edit']"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['inventory:check:delete']"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
                type="info"
                plain
                icon="shop"
                :disabled="single"
                @click="handleCheckDetail"
                v-hasPermi="['inventory:check:pandian']"
            >盘点</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="checkList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="单据号" align="center" key="documentCode" prop="documentCode" :show-overflow-tooltip="true" />
          <el-table-column label="名称" align="center" key="name" prop="name" :show-overflow-tooltip="true" >
            <template #default="scope">
              <router-link :to="'/inventory/check-detail/' + scope.row.id" class="link-type">
                <span>{{ scope.row.name }}</span>
              </router-link>
            </template>
          </el-table-column>
          <el-table-column label="盘点单类型" align="center" prop="type" >
            <template #default="scope">
              <dict-tag :options="sys_inventory_check_type" :value="scope.row.type" />
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" key="checkBrand" prop="status">
            <template #default="scope">
              <dict-tag :options="sys_inventory_check_status" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" key="remark" prop="remark" :show-overflow-tooltip="true" />
          <el-table-column label="创建日期" align="center" prop="createTime" >
            <template #default="scope">
              <span>{{ scope.row.createTime }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="修改" placement="top">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['inventory:check:edit']"></el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['inventory:check:delete']"></el-button>
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

    <!-- 添加或修改货物配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="checkRef" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="盘点单名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入盘点单名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单据号">
              <template #label>
                  <span>
                     <el-tooltip content="单据号由系统自动生成，不可修改" placement="top">
                        <el-icon><question-filled /></el-icon>
                     </el-tooltip>
                     单据号
                  </span>
              </template>
              <el-input v-model="form.documentCode" disabled placeholder="自动生成" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="类型" prop="type">
              <el-select
                  v-model="form.type"
                  filterable
                  :disabled="update"
                  placeholder="请选择类型"
              >
                <el-option
                    v-for="item in sys_inventory_check_type"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <template #label>
                  <span>
                     <el-tooltip content="状态由系统自动生成，不可修改" placement="top">
                        <el-icon><question-filled /></el-icon>
                     </el-tooltip>
                     状态
                  </span>
              </template>
              <el-select
                  v-model="form.status"
                  filterable
                  disabled
                  placeholder="请选择状态"
              >
                <el-option
                    v-for="item in sys_inventory_check_status"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
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
import { getCheckList, addCheck, delCheck, getCheckById, updateCheck} from "@/api/inventory/check"

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict( "sys_normal_disable");

const  router = useRouter();
const checkList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const update = ref(false)

const data = reactive({
  detailForm: {},
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    documentCode: undefined,
    name: undefined,
    type: undefined,
    status: undefined
  }
});

const { queryParams, form, rules } = toRefs(data);
const {
  sys_inventory_check_type, sys_inventory_check_status } =
    proxy.useDict( "sys_inventory_check_type", "sys_inventory_check_status");

/** 查询盘点列表 */
function getList() {
  loading.value = true;
  getCheckList(queryParams.value).then(res => {
    loading.value = false;
    checkList.value = res.rows;
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
  const checkIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除').then(function () {
    return delCheck(checkIds);
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
    documentCode: undefined,
    name: undefined,
    type: undefined,
    status: undefined,
    remark: undefined

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
  reset();
  // 对应状态为已新建
  form.value.status = "1";
  open.value = true;
  title.value = "添加盘点单";
};
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id || ids.value;
  getCheckById(id).then(response => {
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
        updateCheck(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addCheck(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
};

function handleCheckDetail() {
  const id = ids.value[0];
  router.push("/inventory/check-detail/" + id);
}

getList();
</script>
