<template>
  <div class="app-container">
    <el-row>
      <el-col :span="8">
        <span>{{ checkId }}</span>
      </el-col>
      <el-col :span="8">
        <span>盘点名称</span>
      </el-col>
      <el-col :span="8">
        <span>盘点类型</span>
      </el-col>
    </el-row>
    <span>这是盘点过程界面</span>
    <span>id为 {{ checkId }}</span>
  </div>
</template>

<script setup name="User">
import { getCheckList, addCheck, delCheck, getCheckById, updateCheck} from "@/api/inventory/check"
import {useRoute} from "vue-router";

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict( "sys_normal_disable");

const  route = useRoute();
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
const checkId = route.params.checkId;


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

function handleCheck(row) {
  router.push({ path: redirect.value || "/", query: otherQueryParams });
}

getList();
</script>
