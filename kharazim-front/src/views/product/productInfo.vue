<template>
  <div>
    <el-form :inline="true" :model="pageRequest" class="page-form-block">
      <el-form-item label="商品编码">
        <el-input v-model="pageRequest.code" clearable placeholder="商品编码" @keyup.enter="loadProducts" />
      </el-form-item>
      <el-form-item label="商品名称">
        <el-input v-model="pageRequest.name" clearable placeholder="商品名称" @keyup.enter="loadProducts" />
      </el-form-item>
      <el-form-item label="商品分类">
        <el-tree-select
          v-model="pageRequest.categoryCodes"
          :data="categoryData"
          :props="{ value: 'code', label: 'name' }"
          :render-after-expand="false"
          check-on-click-node
          check-strictly
          clearable
          multiple
          node-key="code"
          placeholder="选择商品分类"
          show-checkbox
          value-key="code"
        />
      </el-form-item>
      <el-form-item label="供应商">
        <el-select
          v-model="pageRequest.supplierCodes"
          clearable
          filterable
          multiple
          placeholder="选择供应商"
          reserve-keyword
        >
          <el-option v-for="item in supplierOptions" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </el-form-item>
      <el-form-item label="商品描述">
        <el-input v-model="pageRequest.description" clearable placeholder="商品描述" @keyup.enter="loadProducts" />
      </el-form-item>
      <el-form-item class="page-form-block-search-block">
        <el-button type="primary" @click="loadProducts">查询</el-button>
        <el-button type="primary" @click="clearPageRequestAndLoadProducts">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <el-button type="primary" @click="openAddProductDialog">新建商品</el-button>
  </div>
  <div>
    <el-table :data="productPageData.data" border style="width: 100%; margin-top: 10px">
      <el-table-column label="商品编码" prop="code" width="160" />
      <el-table-column label="商品名称" prop="name" width="220" />
      <el-table-column align="center" label="主图" width="120">
        <template v-slot="{ row }">
          <el-image
            v-if="row.defaultImageUrl"
            :preview-src-list="[row.defaultImageUrl, ...row.imageUrls]"
            :src="row.defaultImageUrl"
            preview-teleported
            style="width: 80px"
          />
        </template>
      </el-table-column>
      <el-table-column label="商品单位" prop="unitName" width="100" />
      <el-table-column label="商品属性" prop="attributesDesc" width="200" />
      <el-table-column label="商品分类" prop="categoryFullName" width="200" />
      <el-table-column label="供应商" prop="supplierName" width="130" />
      <el-table-column label="描述信息" prop="description" />
    </el-table>
  </div>
  <div class="pagination-block">
    <el-pagination
      v-model:current-page="pageRequest.pageIndex"
      v-model:page-size="pageRequest.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :total="productPageData.totalCount"
      background
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="loadProducts"
      @current-change="loadProducts"
    />
  </div>
  <el-dialog v-model="addProductVisible" :close-on-click-modal="false" draggable title="新建商品" width="1000">
    <el-form :model="addProductData" label-width="24%">
      <div class="add-product-form">
        <el-form-item class="add-product-form-item" label="商品名称">
          <el-input v-model="addProductData.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item class="add-product-form-item" label="商品分类">
          <el-tree-select
            v-model="addProductData.categoryCode"
            :check-on-click-node="false"
            :data="categoryData"
            :props="{ value: 'code', label: 'name' }"
            :render-after-expand="false"
            check-strictly
            clearable
            expand-on-click-node
            filterable
            node-key="code"
            placeholder="选择商品分类"
            show-checkbox
            value-key="code"
          />
        </el-form-item>
        <el-form-item class="add-product-form-item" label="供应商">
          <el-select v-model="addProductData.supplierCode" clearable filterable placeholder="选择供应商">
            <el-option v-for="item in supplierOptions" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </el-form-item>
        <el-form-item class="add-product-form-item" label="单位">
          <el-select v-model="addProductData.unitCode" clearable filterable placeholder="选择商品单位">
            <el-option v-for="item in productUnitOptions" :key="item.code" :label="item.name" :value="item.code">
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right">{{ item.englishName }}</span>
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <el-form-item label="商品描述" label-width="11%">
        <el-input
          v-model="addProductData.description"
          :rows="4"
          placeholder="商品描述"
          style="width: 89%"
          type="textarea"
        />
      </el-form-item>
      <el-form-item label="属性" label-width="11%">
        <div v-for="attribute in addProductData.attributes" style="display: inline-block">
          <div style="display: block">
            <el-input v-model="attribute.name" placeholder="属性名" style="width: 40%" />
            <span> : </span>
            <el-input v-model="attribute.value" placeholder="属性值" style="width: 40%" />
          </div>
        </div>
        <el-button icon="Plus" @click="addProductDataAddAttribute" />
      </el-form-item>
      <el-form-item label="商品主图" label-width="11%">
        <SingleImageUpload v-model="addProductData.defaultImage" />
      </el-form-item>
      <el-form-item label="商品图片" label-width="11%">
        <MultiImageUpload v-model="addProductData.images" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeAddProductDialog">取消</el-button>
        <el-button type="primary" @click="confirmAddProduct">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRaw } from "vue";
import { AxiosResponse } from "axios";
import axios from "@/utils/http.js";
import { fileUrl, fileUrls } from "@/utils/fileUrl.ts";
import MultiImageUpload from "@/components/upload/MultiImageUpload.vue";
import SingleImageUpload from "@/components/upload/SingleImageUpload.vue";
import { UploadFileObj } from "@/components/upload/UploadFileObj";
import { useRouter } from "vue-router";
import { loadSuppliers, SupplierVO } from "@/views/supplier/supplier.vue";

interface Attribute {
  name: string;
  value: string;
}

export interface ProductInfo {
  code: string;
  name: string;
  categoryCode: string;
  categoryName: string;
  categoryFullName: string;
  supplierCode: string;
  supplierName: string;
  unitCode: string;
  unitName: string;
  defaultImage: string;
  defaultImageUrl: string;
  images: string[] | null;
  imageUrls: string[] | null;
  description: string;
  attributes: Attribute[];
  attributesDesc: string;
}

const productPageData = ref({ totalCount: 0, data: [] as ProductInfo[] });

const initPageRequest = {
  code: "",
  name: "",
  categoryCodes: [],
  supplierCodes: [],
  description: "",
  pageIndex: 1,
  pageSize: 10,
};

const pageRequest = reactive({ ...initPageRequest });

function clearPageRequestAndLoadProducts() {
  Object.assign(pageRequest, initPageRequest);
  loadProducts();
}

function loadProducts() {
  axios.get("/kharazim-api/product/sku/page", { params: toRaw(pageRequest) }).then((response: AxiosResponse) => {
    productPageData.value = response.data;
    productPageData.value.data.forEach((item: ProductInfo) => {
      item.defaultImageUrl = fileUrl(item.defaultImage);
      item.imageUrls = fileUrls(item.images);
    });
  });
}

export interface ProductCategory {
  id: number;
  parentId: number | null;
  children: ProductCategory[] | null;
  code: string;
  name: string;
}

const categoryData = ref<ProductCategory[]>([]);

function loadCategories() {
  axios.get("/kharazim-api/product/category/tree").then((res: AxiosResponse) => {
    categoryData.value = res.data.data;
  });
}

const supplierOptions = ref<SupplierVO[]>([]);

interface ProductUnitOption {
  code: string;
  name: string;
  englishName: string;
}

const productUnitOptions = ref<ProductUnitOption[]>([]);

function loadProductUnitOptions() {
  axios.get("/kharazim-api/product/unit/list").then((res: AxiosResponse) => {
    productUnitOptions.value = res.data.data;
  });
}

interface AddProductRequest {
  name: string;
  categoryCode: string;
  supplierCode: string;
  unitCode: string;
  defaultImage: UploadFileObj;
  images: UploadFileObj[];
  description: string;
  attributes: Attribute[];
}

const addProductVisible = ref(false);
const addProductData = ref<AddProductRequest>({
  name: "",
  categoryCode: "",
  supplierCode: "",
  unitCode: "",
  defaultImage: {} as UploadFileObj,
  images: [] as UploadFileObj[],
  description: "",
  attributes: [
    {
      name: "",
      value: "",
    },
  ],
});

function openAddProductDialog() {
  addProductData.value = {
    name: "",
    categoryCode: "",
    supplierCode: "",
    unitCode: "",
    defaultImage: {} as UploadFileObj,
    images: [] as UploadFileObj[],
    description: "",
    attributes: [
      {
        name: "",
        value: "",
      },
    ],
  };
  addProductVisible.value = true;
}

function addProductDataAddAttribute() {
  addProductData.value?.attributes.push({
    name: "",
    value: "",
  });
}

function closeAddProductDialog() {
  addProductVisible.value = false;
}

function confirmAddProduct() {
  const addProductRequest = {
    name: addProductData.value?.name,
    categoryCode: addProductData.value?.categoryCode,
    supplierCode: addProductData.value?.supplierCode,
    unitCode: addProductData.value?.unitCode,
    defaultImage: addProductData.value?.defaultImage.fileId,
    images: addProductData.value?.images.map((x: any) => x.fileId),
    description: addProductData.value?.description,
    attributes: addProductData.value?.attributes.filter((x) => x.name && x.value),
  } as AddProductRequest;
  console.log(addProductRequest);
  axios.post("/kharazim-api/product/sku/create", addProductRequest).then(() => {
    loadProducts();
    closeAddProductDialog();
  });
}

const router = useRouter();

onMounted(() => {
  const query = router.currentRoute.value.query;
  const skuCode = query.skuCode as string;
  if (skuCode) {
    pageRequest.code = skuCode;
  }
  loadProducts();
  loadCategories();
  loadSuppliers().then((res: SupplierVO[]) => (supplierOptions.value = res));
  loadProductUnitOptions();
});
</script>

<style scoped>
.add-product-form {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  padding-bottom: 15px;
  width: 100%;
}

.add-product-form-item {
  width: 45%;
}
</style>
