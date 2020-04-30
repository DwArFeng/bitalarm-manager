<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理服务</el-breadcrumb-item>
      <el-breadcrumb-item>数据支持</el-breadcrumb-item>
      <el-breadcrumb-item>报警类型指示器</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片工作区 -->
    <el-card class="box-card">
      <!-- 搜索与添加区域 -->
      <div style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="3" :offset="21">
            <el-button class="top-el-button" type="primary" @click="handleShowCreateDialog">添加指示器
            </el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 表格 -->
      <el-table
        :data="alarmTypeIndicator.data"
        stripe
        style="width: 100%">
        <el-table-column
          prop="key.byte_id"
          label="ID"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="label"
          label="标签">
        </el-table-column>
        <el-table-column label="操作" :width="150">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                @click="handleShowUpdateDialog(scope.row)">编辑
              </el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.row.key.byte_id)">删除
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页信息区域 -->
      <el-row type="flex" justify="end">
        <el-col>
          <div class="pagination-container">
            <el-pagination
              background
              layout="prev, pager, next"
              :page-size="pageSize"
              :total="parseInt(alarmTypeIndicator.count)"
              :hide-on-single-page="true"
              :current-page.sync="currentPage"
              @current-change="onPageChanged">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 添加报警类型指示器对话框 -->
    <el-dialog
      title="添加报警类型指示器"
      :visible.sync="createAlarmTypeIndicatorVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="createAlarmTypeIndicatorRules"
        status-icon
        :model="anchorAlarmTypeIndicator"
        ref="createAlarmTypeIndicatorForm">
        <el-form-item label="ID" prop="key">
          <el-input
            v-model="anchorAlarmTypeIndicator.key"
            placeholder="请输入报警类型指示器的主键"
            oninput="this.value=this.value.replace(/[^\d.]/g,'');"
          ></el-input>
        </el-form-item>
        <el-form-item label="标签" prop="label">
          <el-input
            v-model="anchorAlarmTypeIndicator.label"
            placeholder="请输入报警类型指示器标签"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createAlarmTypeIndicatorVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </div>
    </el-dialog>
    <!-- 更新报警类型指示器对话框 -->
    <el-dialog
      title="更新报警类型指示器"
      :visible.sync="updateAlarmTypeIndicatorVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="updateAlarmTypeIndicatorRules"
        status-icon
        :model="anchorAlarmTypeIndicator"
        ref="updateAlarmTypeIndicatorForm">
        <el-form-item label="ID" prop="key">
          <el-input
            v-model="anchorAlarmTypeIndicator.key"
            :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="标签" prop="label">
          <el-input
            v-model="anchorAlarmTypeIndicator.label"
            placeholder="请输入报警类型指示器标签"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateAlarmTypeIndicatorVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  exists, insert, update, remove, all,
} from '../../api/alarmTypeIndicator';

export default {
  data() {
    const validateAlarmTypeIndicatorNotExists = (rule, value, callback) => {
      if (value === '') {
        callback();
      } else {
        exists(value)
          .then((res) => {
            if (res.meta.code !== 0) {
              callback(new Error('无法验证报警指示器是否存在'));
              return null;
            }
            if (res.data) {
              callback(new Error('报警指示器已经存在'));
              return null;
            }
            callback();
            return null;
          })
          .catch(() => {
            callback(new Error('无法验证报警指示器是否存在'));
            return null;
          });
      }
    };

    return {
      alarmTypeIndicator: {},
      pageSize: 15,
      currentPage: 1,
      anchorAlarmTypeIndicator: {
        key: '',
        label: '',
      },
      createAlarmTypeIndicatorVisible: false,
      updateAlarmTypeIndicatorVisible: false,
      createAlarmTypeIndicatorRules: {
        key: [
          {
            required: true,
            message: 'ID不能为空',
            trigger: 'blur',
          },
          {
            validator: validateAlarmTypeIndicatorNotExists,
            trigger: 'blur',
          },
        ],
        label: [
          {
            required: true,
            message: '标签不能为空',
            trigger: 'blur',
          },
        ],
      },
      updateAlarmTypeIndicatorRules: {
        label: [
          {
            required: true,
            message: '标签不能为空',
            trigger: 'blur',
          },
        ],
      },
      id2Search: '',
      label2Search: '',
    };
  },
  created() {
    this.lookupAll();
  },
  methods: {
    onPageChanged() {
      this.lookupAll();
    },
    lookupAll() {
      all(this.currentPage - 1, this.pageSize)
        .then((res) => {
          if (res.meta.code !== 0) {
            this.$message({
              showClose: true,
              message: '请求异常，请稍后再试',
              center: true,
            });
            return null;
          }
          // 当查询的页数大于总页数，自动查询最后一页。
          if (res.data.current_page >= res.data.total_pages && res.data.total_pages > 0) {
            return all(res.data.total_pages - 1, this.pageSize);
          }
          this.alarmTypeIndicator = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        })
        .catch((err) => {
          this.$message({
            showClose: true,
            message: `通信错误，异常信息: ${err.message}`,
            type: 'error',
            center: true,
          });
          return null;
        })
        .then((res) => {
          if (res == null) {
            return null;
          }
          if (res.meta.code !== 0) {
            this.$message({
              showClose: true,
              message: '请求异常，请稍后再试',
              center: true,
            });
            return null;
          }
          this.alarmTypeIndicator = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    handleShowCreateDialog() {
      this.createAlarmTypeIndicatorVisible = true;
    },
    handleShowUpdateDialog(row) {
      if (this.$refs.updateAlarmTypeIndicatorForm !== undefined) {
        this.$refs.updateAlarmTypeIndicatorForm.resetFields();
      }
      this.anchorAlarmTypeIndicator.key = row.key.byte_id;
      this.anchorAlarmTypeIndicator.label = row.label;
      this.updateAlarmTypeIndicatorVisible = true;
    },
    handleCreate() {
      if (this.$refs.createAlarmTypeIndicatorForm === undefined) {
        return;
      }
      this.$refs.createAlarmTypeIndicatorForm.validate((bool) => {
        if (!bool) {
          return;
        }
        insert(
          this.anchorAlarmTypeIndicator.key,
          this.anchorAlarmTypeIndicator.label,
        )
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '创建失败',
                type: 'error',
                center: true,
              });
              return null;
            }
            this.$message({
              showClose: true,
              message: `报警类型指示器 ${res.data.byte_id} 创建成功`,
              type: 'success',
              center: true,
            });
            this.createAlarmTypeIndicatorVisible = false;
            this.lookupAll();
            return null;
          })
          .catch((err) => {
            this.$message({
              showClose: true,
              message: `通信错误，异常信息: ${err.message}`,
              type: 'error',
              center: true,
            });
            return null;
          });
      });
    },
    handleUpdate() {
      if (this.$refs.updateAlarmTypeIndicatorForm === undefined) {
        return;
      }
      this.$refs.updateAlarmTypeIndicatorForm.validate((bool) => {
        if (!bool) {
          return;
        }
        update(
          this.anchorAlarmTypeIndicator.key,
          this.anchorAlarmTypeIndicator.label,
        )
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '更新失败',
                type: 'error',
                center: true,
              });
              return null;
            }
            this.$message({
              showClose: true,
              message: '报警类型指示器更新成功',
              type: 'success',
              center: true,
            });
            this.updateAlarmTypeIndicatorVisible = false;
            this.lookupAll();
            return null;
          })
          .catch((err) => {
            this.$message({
              showClose: true,
              message: `通信错误，异常信息: ${err.message}`,
              type: 'error',
              center: true,
            });
            return null;
          });
      });
    },
    handleDelete(key) {
      this.$confirm('此操作将永久删除此报警类型指示器。<br>'
        + '删除报警类型指示器后，已经使用此报警类型的实体的报警类型将会直接显示为byte数字。<br>'
        + '是否继续?',
      '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        dangerouslyUseHTMLString: true,
        type: 'warning',
      })
        .then(() => remove(key))
        .catch(() => Promise.reject(new Error('canceled')))
        .then((res) => {
          if (res.meta.code !== 0) {
            this.$message({
              showClose: true,
              message: '删除失败',
              type: 'error',
              center: true,
            });
            return null;
          }
          this.$message({
            type: 'success',
            message: '删除成功!',
          });
          this.lookupAll();
          return null;
        })
        .catch((err) => {
          if (err.message === 'canceled') {
            return null;
          }
          this.$message({
            showClose: true,
            message: `通信错误，异常信息: ${err.message}`,
            type: 'error',
            center: true,
          });
          return null;
        });
    },
  },
};
</script>

<style scoped>
  .pagination-container {
    text-align: center;
  }

  .top-el-button {
    width: 100%;
  }
</style>
