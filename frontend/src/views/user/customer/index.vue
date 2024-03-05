<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--客户数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
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

        <el-table v-loading="loading" :data="customerList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="头像" align="center" key="avatar" prop="avatar" >
            <template #default="scope">
              <el-avatar :size="60" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
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
          <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template #default="scope">
              <span>{{ scope.row.createTime }}</span>
            </template>
          </el-table-column>
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

    <!-- 添加或修改客户配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="customerRef" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="客户昵称" prop="nickname">
              <el-input v-model="form.nickname" placeholder="请输入客户昵称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户性别">
              <el-select v-model="form.gender" placeholder="请选择">
                <el-option
                    v-for="dict in sys_gender"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="地址">
              <el-input v-model="form.address" type="textarea" placeholder="请输入内容"></el-input>
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
import { getCustomerList, getCustomerById, addCustomer, delCustomer, updateCustomer } from "@/api/user/customer"
import UserAvatar from "@/views/user/profile/userAvatar.vue";

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_gender } = proxy.useDict( "sys_gender");

const customerList = ref([]);
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
    nickname: undefined,
    phone: undefined
  },
  rules: {
    nickname: [{ required: true, message: "客户昵称不能为空", trigger: "blur" }],
    password: [{ required: true, message: "客户密码不能为空", trigger: "blur" }, { min: 5, max: 20, message: "客户密码长度必须介于 5 和 20 之间", trigger: "blur" }],
    email: [{ required: true, message: "邮箱不能为空", trigger: "blur" }, { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
    phone: [{ required: true, message: "手机号不能为空", trigger: "blur" }, { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询客户列表 */
function getList() {
  loading.value = true;
  getCustomerList(queryParams.value).then(res => {
    loading.value = false;
    customerList.value = res.rows;
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
  const customerIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除').then(function () {
    return delCustomer(customerIds);
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
    nickname: undefined,
    phone: undefined,
    email: undefined,
    address: undefined
  };
  proxy.resetForm("customerRef");
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
  title.value = "添加客户";
};
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id || ids.value;
  getCustomerById(id).then(response => {
    form.value = response;
    open.value = true;
    title.value = "修改客户";
  });
};
/** 提交按钮 */
function submitForm() {
  proxy.$refs["customerRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateCustomer(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addCustomer(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
};

function handleAvatar(row) {

}

getList();
</script>
