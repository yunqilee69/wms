<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--货物数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="货物名称" prop="name">
            <el-input
                v-model="queryParams.name"
                placeholder="请输入货物名称"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="品牌" prop="brand">
            <el-select
                v-model="queryParams.brand"
                filterable
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
          <el-form-item label="类别" prop="category">
            <el-select
                v-model="queryParams.category"
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
                v-hasPermi="['inventory:waresInfo:add']"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
                type="success"
                plain
                icon="Edit"
                :disabled="single"
                @click="handleUpdate"
                v-hasPermi="['inventory:waresInfo:edit']"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['inventory:waresInfo:delete']"
            >删除</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="wareList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="图片" align="center" key="picture" prop="picture" >
            <template #default="scope">
              <image-preview :src="scope.row.picture" :height="50" :width="50" />
            </template>
          </el-table-column>
          <el-table-column label="品牌" align="center" key="brand" prop="brand" :show-overflow-tooltip="true" >
            <template #default="scope">
              <dict-tag :options="sys_inventory_brand" :value="scope.row.brand" />
            </template>
          </el-table-column>
          <el-table-column label="货物名称" align="center" key="name" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="类别" align="center" key="category" prop="category" :show-overflow-tooltip="true">
            <template #default="scope">
              <dict-tag :options="sys_inventory_category" :value="scope.row.category" />
            </template>
          </el-table-column>
          <el-table-column label="条形码" align="center" key="barCode" prop="barCode" :show-overflow-tooltip="true" />
          <el-table-column label="保质期" align="center" key="qualityMonth" prop="qualityMonth" :show-overflow-tooltip="true" />
          <el-table-column label="规格" align="center" key="spec" prop="spec" :show-overflow-tooltip="true">
            <template #default="scope">
              <dict-tag :options="sys_inventory_spec" :value="scope.row.spec" />
            </template>
          </el-table-column>
          <el-table-column label="单位" align="center" key="unit" prop="unit" :show-overflow-tooltip="true">
            <template #default="scope">
              <dict-tag :options="sys_inventory_unit" :value="scope.row.unit" />
            </template>
          </el-table-column>
          <el-table-column label="进价" align="center" key="purchasePrice" prop="purchasePrice" :show-overflow-tooltip="true">
            <template #default="scope">
              {{ "￥" + scope.row.purchasePrice + "元" }}
            </template>
          </el-table-column>
          <el-table-column label="售价" align="center" key="salePrice" prop="salePrice" :show-overflow-tooltip="true">
            <template #default="scope">
              {{ "￥" + scope.row.salePrice + "元" }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="修改" placement="top">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['inventory:waresInfo:edit']"></el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['inventory:waresInfo:delete']"></el-button>
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
      <el-form :model="form" :rules="rules" ref="wareRef" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="货物名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入货物名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="品牌" prop="brand">
              <el-select
                  v-model="form.brand"
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
                  v-model="form.category"
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
              <el-input v-model="form.barCode" placeholder="请输入条形码"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="规格" prop="spec">
              <el-select
                  v-model="form.spec"
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
                  v-model="form.unit"
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
              <el-input v-model="form.purchasePrice" placeholder="请输入进价"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="售价" prop="salePrice">
              <el-input v-model="form.salePrice" placeholder="请输入售价"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="保质期" prop="qualityMonth">
              <el-input v-model="form.qualityMonth" placeholder="请输入保质期(月)"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="货物图片" prop="picture">
              <image-upload :limit="1" v-model:model-value="form.picture" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="描述">
              <el-input v-model="form.description" type="textarea" placeholder="请输入内容"></el-input>
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
import { getWareList, getWareById, addWare, delWare, updateWare } from "@/api/inventory/ware"
import DictTag from "@/components/DictTag/index.vue";
import FileUpload from "@/components/FileUpload/index.vue";
import ImagePreview from "@/components/ImagePreview/index.vue";

const router = useRouter();
const { proxy } = getCurrentInstance();
const {
  sys_inventory_brand, 
  sys_inventory_spec, 
  sys_inventory_unit, 
  sys_inventory_category } = 
    proxy.useDict( "sys_inventory_brand",
        "sys_inventory_spec",
        "sys_inventory_unit",
        "sys_inventory_category");

const wareList = ref([]);
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
    brand: undefined,
    category: undefined,
    barCode: undefined
  },
  rules: {
    name: [{ required: true, message: "货物名称不能为空", trigger: "blur" }],
    barCode: [{ required: true, message: "条形码不能为空", trigger: "blur" }],
    purchasePrice: [{ required: true, message: "进价不能为空", trigger: "blur" },
      { pattern: /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/, message: "请输入正确额格式,可保留两位小数", trigger: "blur" }],
    salePrice: [{ required: true, message: "售价不能为空", trigger: "blur" },
      { pattern: /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/, message: "请输入正确额格式,可保留两位小数", trigger: "blur" }],
    qualityMonth: [{ required: true, message: "保质期不能为空", trigger: "blur" }, { pattern: /^\d+$/, message: "保质期必须为数字" }],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询货物列表 */
function getList() {
  loading.value = true;
  getWareList(queryParams.value).then(res => {
    loading.value = false;
    wareList.value = res.rows;
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
  const wareIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除').then(function () {
    return delWare(wareIds);
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
    picture: undefined,
    barCode: undefined,
    spec: undefined,
    unit: undefined,
    purchasePrice: undefined,
    salePrice: undefined,
    description: undefined,
    quantityMonth: undefined
  };
  proxy.resetForm("wareRef");
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
  title.value = "添加货物";
};
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id || ids.value;
  getWareById(id).then(response => {
    form.value = response;
    open.value = true;
    title.value = "修改货物";
  });
};
/** 提交按钮 */
function submitForm() {
  proxy.$refs["wareRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updateWare(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addWare(form.value).then(response => {
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
