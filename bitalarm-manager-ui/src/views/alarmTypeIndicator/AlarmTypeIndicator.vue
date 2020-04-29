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
          <el-col :span="3" offset="21">
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
          prop="key.string_id"
          label="ID"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="label"
          label="标签"
          width="400px"
        >
        </el-table-column>
        <el-table-column
          prop="description"
          label="描述"
        >
        </el-table-column>
        <el-table-column
          prop="example_content"
          label="配置示例"
          :formatter="exampleContentFormatter"
          width="100px">
        </el-table-column>
        <el-table-column label="操作" :width="150">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                type="success"
                @click="handleShowDetailDialog(scope.row)">详细信息
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
        ref="createPermissionForm">
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
      :visible.sync="updatePermissionVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="updateAlarmTypeIndicatorRules"
        status-icon
        :model="anchorAlarmTypeIndicator"
        ref="updatePermissionForm">
        <el-form-item label="ID" prop="key">
          <el-input
            v-model="anchorAlarmTypeIndicator.key"
            :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input
            v-model="anchorAlarmTypeIndicator.name"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="anchorAlarmTypeIndicator.remark"
            placeholder="请输入报警类型指示器备注"></el-input>
        </el-form-item>
        <el-form-item label="实时记录" prop="function">
          <el-switch
            active-text="启用"
            inactive-text="不启用"
            v-model="anchorAlarmTypeIndicator.realtime_enabled"
          ></el-switch>
        </el-form-item>
        <el-form-item label="持久记录" prop="function">
          <el-switch
            active-text="启用"
            inactive-text="不启用"
            v-model="anchorAlarmTypeIndicator.persistence_enabled"
          ></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updatePermissionVisible = false">取消</el-button>
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
      this.anchorAlarmTypeIndicator.key = row.key.long_id;
      this.anchorAlarmTypeIndicator.name = row.name;
      this.anchorAlarmTypeIndicator.realtime_enabled = row.realtime_enabled;
      this.anchorAlarmTypeIndicator.persistence_enabled = row.persistence_enabled;
      this.anchorAlarmTypeIndicator.remark = row.remark;
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
          this.anchorAlarmTypeIndicator.name,
          this.anchorAlarmTypeIndicator.remark,
          this.anchorAlarmTypeIndicator.realtime_enabled,
          this.anchorAlarmTypeIndicator.persistence_enabled,
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
              message: `报警类型指示器 ${res.data.long_id} 创建成功`,
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
          this.anchorAlarmTypeIndicator.name,
          this.anchorAlarmTypeIndicator.remark,
          this.anchorAlarmTypeIndicator.realtime_enabled,
          this.anchorAlarmTypeIndicator.persistence_enabled,
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
        + '删除报警类型指示器时会同时删除报警类型指示器的过滤器设置和触发器设置，但不会一并移除与此报警类型指示器相关数据。<br>'
        + '<b>我们强联建议您只是将报警类型指示器的实时数据记录和持久数据记录禁用，同时禁用所有的过滤器以及触发器，'
        + '而不是删除报警类型指示器。</b><br>'
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
    exampleContentFormatter() {
      return '见详细页面';
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
