<template>
  <!-- 授权用户 -->
  <el-dialog title="选择客户" v-model="visible" width="800px" top="5vh" append-to-body>
    <el-row :gutter="20">
      <!--客户数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="82px">
          <el-form-item label="客户昵称" prop="nickname">
            <el-input
                v-model="queryParams.nickname"
                placeholder="请输入客户昵称"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phone">
            <el-input
                v-model="queryParams.phone"
                placeholder="请输入手机号码"
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

        <el-table
                  ref="refTable"
                  :data="dataList"
                  highlight-current-row
                  @current-change="handleCurrentChange"
                  height="260px">
          <el-table-column label="头像" align="center" key="avatar" prop="avatar" >
            <template #default="scope">
              <image-preview :src="scope.row.avatar" :height="50" :width="50" />
            </template>
          </el-table-column>
          <el-table-column label="客户昵称" align="center" key="nickname" prop="nickname" :show-overflow-tooltip="true" />
          <el-table-column label="手机号" align="center" key="phone" prop="phone" :show-overflow-tooltip="true" />
          <el-table-column label="性别" align="center" key="gender" prop="gender" :show-overflow-tooltip="true">
            <template #default="scope">
              <dict-tag :options="sys_gender" :value="scope.row.gender" />
            </template>
          </el-table-column>
          <el-table-column label="邮箱" align="center" key="email" prop="email" :show-overflow-tooltip="true"/>
          <el-table-column label="地址" align="center" key="address" prop="address" :show-overflow-tooltip="true"/>
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
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="handleSelectRecord">确 定</el-button>
        <el-button @click="visible = false">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { getCustomerList } from "@/api/user/customer.js"
import { addOrder } from "@/api/order/sale.js"

const { proxy } = getCurrentInstance();
const { sys_gender } = proxy.useDict( "sys_gender");

const dataList = ref([]);
const visible = ref(false);
const total = ref(0);
const customerId = ref();


const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  nickname: undefined,
  phone: undefined
});

// 显示弹框
function show() {
  getList();
  visible.value = true;
}

// 设置当前选择行
const setCurrent = (row) => {
  refTable.value.setCurrentRow(row)
}
const handleCurrentChange = (val) => {
  if (val === undefined) {
    return;
  }
  customerId.value = val.id
}

// 获取数据
function getList() {
  getCustomerList(queryParams).then(res => {
    dataList.value = res.rows;
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
  if (customerId.value == undefined) {
    proxy.$modal.msgWarning("请选择一条记录");
    return;
  }

  addOrder(customerId.value).then(res => {
    proxy.$modal.msgSuccess("添加成功");
      visible.value = false;
      emit("ok");
  });

}


defineExpose({
  show,
});
</script>
