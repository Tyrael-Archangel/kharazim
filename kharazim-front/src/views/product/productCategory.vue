<template>
  <div>
    <el-button @click="showCreateCategory(null)">添加分类</el-button>
  </div>
  <div>
    <el-table
        :data="categoryData"
        :default-expand-all="true"
        :tree-props="{ children: 'children' }"
        border
        row-key="id"
        style="margin-top: 10px"
        :indent="20"
    >
      <el-table-column label="分类编码" prop="code" />
      <el-table-column label="分类名称" prop="name" />
      <el-table-column label="备注" prop="remark" />
      <el-table-column label="操作">
        <template v-slot="{ row }">
          <el-button size="small" @click="showCreateCategory(row)">添加子分类</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <div>
    <el-dialog
        v-model="createCategoryVisible"
        draggable
        title="添加分类"
        width="500"
    >
      <el-form :model="createNewCategory" label-width="20%">
        <el-form-item label="父级分类">
          <el-tree-select
              v-model="createNewCategory.parentId"
              :current-node-key="createNewCategory.parentId"
              :data="categoryData"
              :fit-input-width="true"
              :props="{ value: 'id', label: 'name' }"
              :render-after-expand="false"
              check-on-click-node
              check-strictly
              node-key="id"
              placeholder="请选择父级分类"
              show-checkbox
              value-key="id"
          />
        </el-form-item>
        <el-form-item label="分类名称">
          <el-input v-model="createNewCategory.name" placeholder="分类名称" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="createNewCategory.remark" placeholder="备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeCreateCategory">取消</el-button>
          <el-button type="primary" @click="confirmCreateCategory"
          >确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import axios from "@/utils/http.js";
import { AxiosResponse } from "axios";

interface ProductCategory {
  id: number;
  parentId: number | null;
  children: ProductCategory[] | null;
  code: string;
  name: string;
  remark: string | null;
  fullPathName: string;
}

const categoryData = ref<ProductCategory[]>([]);

function loadCategories() {
  axios
      .get("/kharazim-api/product/category/tree")
      .then((res: AxiosResponse) => {
        categoryData.value = res.data.data;
      });
}

const createCategoryVisible = ref(false);

interface CreateProductCategory {
  parentId: number | null;
  name: string;
  remark: string | null;
}

const createNewCategory = ref<CreateProductCategory>({
  parentId: null,
  name: "",
  remark: "",
});

function closeCreateCategory() {
  createCategoryVisible.value = false;
}

function showCreateCategory(row: ProductCategory) {
  if (row) {
    createNewCategory.value = {
      parentId: row.id,
      name: "",
      remark: "",
    };
  } else {
    createNewCategory.value = {
      parentId: null,
      name: "",
      remark: "",
    };
  }
  createCategoryVisible.value = true;
}

function confirmCreateCategory() {
  axios
      .post("/kharazim-api/product/category/add", {
        parentId: createNewCategory.value.parentId,
        name: createNewCategory.value.name,
        remark: createNewCategory.value.remark,
      })
      .then(() => {
        loadCategories();
        closeCreateCategory();
      });
}

onMounted(() => loadCategories());
</script>

<style scoped></style>