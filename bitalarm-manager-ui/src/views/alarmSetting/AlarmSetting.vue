<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理服务</el-breadcrumb-item>
      <el-breadcrumb-item>实体管理</el-breadcrumb-item>
      <el-breadcrumb-item>报警设置管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片工作区 -->
    <el-card class="box-card">
      <!-- 搜索与添加区域 -->
      <div style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input
              placeholder="请输入报警设置ID，仅支持精确查询"
              class="input-with-select"
              v-model="id2Search"
              clearable
              oninput="this.value=this.value.replace(/[^\d.]/g,'');"
            >
              <el-button slot="append" icon="el-icon-search" @click="handleIdSearch"></el-button>
            </el-input>
          </el-col>
          <el-col :span="6">
            <el-input
              placeholder="请输入报警设置数据点ID，仅支持精确查询"
              class="input-with-select"
              v-model="pointId2Search"
              clearable
              oninput="this.value=this.value.replace(/[^\d.]/g,'');"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="handlePointIdSearch"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="3">
            <el-select
              v-model="alarmType2Search"
              clearable placeholder="报警类型"
              @change="handleAlarmTypeSearch"
            >
              <el-option
                v-for="item in alarmTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-input
              placeholder="请输入报警文本，支持任意部位匹配"
              class="input-with-select"
              v-model="alarmMessage2Search"
              clearable
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="handleAlarmMessageSearch"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :span="3">
            <el-button class="top-el-button" type="primary" @click="handleShowCreateDialog">添加报警设置
            </el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 表格 -->
      <el-table
        :data="alarmSetting.data"
        stripe
        style="width: 100%">
        <el-table-column
          prop="key.long_id"
          label="ID"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="point_id"
          label="数据点ID"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="enabled"
          label="是否启用"
          :formatter="enabledFormatter"
          width="100px">
        </el-table-column>
        <el-table-column
          prop="index"
          label="报警索引"
          width="120px">
        </el-table-column>
        <el-table-column
          prop="alarm_message"
          label="报警信息">
        </el-table-column>
        <el-table-column
          prop="alarm_type"
          label="报警类型"
          :formatter="alarmTypeFormatter"
          width="120px">
        </el-table-column>
        <el-table-column
          prop="remark"
          label="备注">
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
                @click="handleDelete(scope.row.key.long_id)">删除
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
              :total="parseInt(alarmSetting.count)"
              :hide-on-single-page="true"
              :current-page.sync="currentPage"
              @current-change="onPageChanged">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 添加报警设置对话框 -->
    <el-dialog
      title="添加报警设置"
      :visible.sync="createAlarmSettingVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="createAlarmSettingRules"
        status-icon
        :model="anchorAlarmSetting"
        ref="createAlarmSettingForm">
        <el-form-item label="ID" prop="key">
          <el-input
            v-model="anchorAlarmSetting.key"
            placeholder="请输入报警设置的主键，如不输入则自动生成"
            oninput="this.value=this.value.replace(/[^\d.]/g,'');"
          ></el-input>
        </el-form-item>
        <el-form-item label="数据点" prop="point_id">
          <el-input
            v-model="anchorAlarmSetting.point_id"
            placeholder="请输入报警设置的数据点"
            oninput="this.value=this.value.replace(/[^\d.]/g,'');"
          ></el-input>
        </el-form-item>
        <el-form-item label="启用" prop="enabled">
          <el-switch
            active-text="启用"
            inactive-text="不启用"
            v-model="anchorAlarmSetting.enabled"
          ></el-switch>
        </el-form-item>
        <el-form-item label="索引" prop="index">
          <el-input-number
            v-model="anchorAlarmSetting.index"
            controls-position="right"
            :min="0"
            :max="2147483647"
            :step="1"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="报警信息" prop="alarm_message">
          <el-input
            v-model="anchorAlarmSetting.alarm_message"
            placeholder="请输入报警信息"></el-input>
        </el-form-item>
        <el-form-item label="报警类型" prop="alarm_type">
          <el-select v-model="anchorAlarmSetting.alarm_type" placeholder="请选择">
            <el-option
              v-for="item in alarmTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="anchorAlarmSetting.remark"
            placeholder="请输入报警设置备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createAlarmSettingVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </div>
    </el-dialog>
    <!-- 更新报警设置对话框 -->
    <el-dialog
      title="更新报警设置"
      :visible.sync="updateAlarmSettingVisible"
      center>
      <el-form
        label-width="80px"
        label-position="right"
        :rules="updateAlarmSettingRules"
        status-icon
        :model="anchorAlarmSetting"
        ref="updateAlarmSettingForm">
        <el-form-item label="ID" prop="key">
          <el-input
            v-model="anchorAlarmSetting.key"
            :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="数据点" prop="pointId">
          <el-input
            v-model="anchorAlarmSetting.point_id"
            placeholder="请输入报警设置的数据点"
            oninput="this.value=this.value.replace(/[^\d.]/g,'');"
          ></el-input>
        </el-form-item>
        <el-form-item label="启用" prop="enabled">
          <el-switch
            active-text="启用"
            inactive-text="不启用"
            v-model="anchorAlarmSetting.enabled"
          ></el-switch>
        </el-form-item>
        <el-form-item label="索引" prop="index">
          <el-input-number
            v-model="anchorAlarmSetting.index"
            controls-position="right"
            :min="0"
            :max="2147483647"
            :step="1"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="报警信息" prop="alarmMessage">
          <el-input
            v-model="anchorAlarmSetting.alarm_message"
            placeholder="请输入报警信息"></el-input>
        </el-form-item>
        <el-form-item label="报警类型" prop="alarm_type">
          <el-select v-model="anchorAlarmSetting.alarm_type" placeholder="请选择">
            <el-option
              v-for="item in alarmTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="anchorAlarmSetting.remark"
            placeholder="请输入报警设置备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateAlarmSettingVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  all, exists, insert, inspect, remove, update, childForPoint, alarmTypeEquals, alarmMessageLike,
} from '../../api/alarmSetting';

import {
  all as allType,
} from '../../api/alarmTypeIndicator';

export default {
  name: 'AlarmSetting',
  data() {
    const validateAlarmSettingNotExists = (rule, value, callback) => {
      if (value === '') {
        callback();
      } else {
        exists(value)
          .then((res) => {
            if (res.meta.code !== 0) {
              callback(new Error('无法验证报警设置是否存在'));
              return null;
            }
            if (res.data) {
              callback(new Error('报警设置已经存在'));
              return null;
            }
            callback();
            return null;
          })
          .catch(() => {
            callback(new Error('无法验证报警设置是否存在'));
            return null;
          });
      }
    };

    return {
      alarmSetting: {},
      pageSize: 15,
      currentPage: 1,
      createAlarmSettingVisible: false,
      updateAlarmSettingVisible: false,
      anchorAlarmSetting: {
        key: '',
        point_id: '',
        enabled: false,
        index: '',
        alarm_message: '',
        alarm_type: '',
        remark: '',
      },
      createAlarmSettingRules: {
        key: [
          {
            validator: validateAlarmSettingNotExists,
            trigger: 'blur',
          },
        ],
        point_id: [
          {
            required: true,
            message: '报警设置的数据点不能为空',
            trigger: 'blur',
          },
        ],
        index: [
          {
            required: true,
            message: '报警设置的索引不能为空',
            trigger: 'blur',
          },
        ],
        alarm_message: [
          {
            required: true,
            message: '报警信息不能为空',
            trigger: 'blur',
          },
        ],
        alarm_type: [
          {
            required: true,
            message: '报警类型不能为空',
            trigger: 'blur',
          },
        ],
      },
      updateAlarmSettingRules: {
        point_id: [
          {
            required: true,
            message: '报警设置的数据点不能为空',
            trigger: 'blur',
          },
        ],
        index: [
          {
            required: true,
            message: '报警设置的索引不能为空',
            trigger: 'blur',
          },
        ],
        alarm_message: [
          {
            required: true,
            message: '报警信息不能为空',
            trigger: 'blur',
          },
        ],
        alarm_type: [
          {
            required: true,
            message: '报警类型不能为空',
            trigger: 'blur',
          },
        ],
      },
      id2Search: '',
      pointId2Search: '',
      alarmType2Search: null,
      alarmMessage2Search: '',
      alarmTypeMap: new Map(),
      alarmTypeOptions: [],
    };
  },
  created() {
    this.lookupAll();
    this.lookupAllType();
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
          this.alarmSetting = res.data;
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
          this.alarmSetting = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupAllType() {
      allType(0, 127)
        .then((res) => {
          if (res.meta.code !== 0) {
            this.$message({
              showClose: true,
              message: '请求异常，请稍后再试',
              center: true,
            });
            return null;
          }
          const alarmTypeMap = new Map();
          const alarmTypeOptions = [];
          res.data.data.forEach((indicator) => {
            alarmTypeMap.set(indicator.key.byte_id, indicator.label);
            alarmTypeOptions.push({ value: indicator.key.byte_id, label: indicator.label });
          });
          this.alarmTypeMap = alarmTypeMap;
          this.alarmTypeOptions = alarmTypeOptions;
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
    },
    handleShowCreateDialog() {
      this.createAlarmSettingVisible = true;
    },
    handleShowUpdateDialog(row) {
      if (this.$refs.updateAlarmSettingForm !== undefined) {
        this.$refs.updateAlarmSettingForm.resetFields();
      }
      this.anchorAlarmSetting.key = row.key.long_id;
      this.anchorAlarmSetting.point_id = row.point_id;
      this.anchorAlarmSetting.enabled = row.enabled;
      this.anchorAlarmSetting.index = row.index;
      this.anchorAlarmSetting.alarm_message = row.alarm_message;
      this.anchorAlarmSetting.alarm_type = row.alarm_type;
      this.anchorAlarmSetting.remark = row.remark;
      this.updateAlarmSettingVisible = true;
    },
    handleCreate() {
      if (this.$refs.createAlarmSettingForm === undefined) {
        return;
      }
      this.$refs.createAlarmSettingForm.validate((bool) => {
        if (!bool) {
          return;
        }
        insert(
          this.anchorAlarmSetting.key,
          this.anchorAlarmSetting.point_id,
          this.anchorAlarmSetting.enabled,
          this.anchorAlarmSetting.index,
          this.anchorAlarmSetting.alarm_message,
          this.anchorAlarmSetting.alarm_type,
          this.anchorAlarmSetting.remark,
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
              message: `报警设置 ${res.data.long_id} 创建成功`,
              type: 'success',
              center: true,
            });
            this.createAlarmSettingVisible = false;
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
      if (this.$refs.updateAlarmSettingForm === undefined) {
        return;
      }
      this.$refs.updateAlarmSettingForm.validate((bool) => {
        if (!bool) {
          return;
        }
        update(
          this.anchorAlarmSetting.key,
          this.anchorAlarmSetting.point_id,
          this.anchorAlarmSetting.enabled,
          this.anchorAlarmSetting.index,
          this.anchorAlarmSetting.alarm_message,
          this.anchorAlarmSetting.alarm_type,
          this.anchorAlarmSetting.remark,
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
              message: '报警设置更新成功',
              type: 'success',
              center: true,
            });
            this.updateAlarmSettingVisible = false;
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
      this.$confirm('此操作将永久删除此报警设置。<br>'
          + '删除报警设置时会同时删除报警信息和当前报警，但不会一并移除与此报警设置相关的历史报警。<br>'
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
    enabledFormatter(row) {
      return row.enabled ? '是' : '否';
    },
    alarmTypeFormatter(row) {
      if (this.alarmTypeMap.has(row.alarm_type)) {
        return this.alarmTypeMap.get(row.alarm_type);
      }
      return row.alarm_type;
    },
    handleIdSearch() {
      if (this.id2Search === '') {
        this.lookupAll();
      } else {
        exists(this.id2Search)
          .then((res) => {
            if (res.data) {
              return inspect(this.id2Search);
            }
            this.$message({
              showClose: true,
              message: `报警设置 ${this.id2Search} 不存在`,
              type: 'warning',
              center: true,
            });
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

            this.alarmSetting.count = 1;
            this.alarmSetting.current_page = 0;
            this.alarmSetting.data = [res.data];
            this.alarmSetting.rows = this.pageSize;
            this.alarmSetting.totle_pages = 1;
            this.currentPage = 1;
            return null;
          });
      }
    },
    handlePointIdSearch() {
      if (this.pointId2Search === '') {
        this.lookupAll();
      } else {
        childForPoint(this.pointId2Search, 0, this.pageSize)
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '请求异常，请稍后再试',
                center: true,
              });
              return null;
            }
            this.alarmSetting = res.data;
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
          });
      }
    },
    handleAlarmTypeSearch() {
      if (this.alarmType2Search === '') {
        this.lookupAll();
      } else {
        alarmTypeEquals(this.alarmType2Search, 0, this.pageSize)
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '请求异常，请稍后再试',
                center: true,
              });
              return null;
            }
            this.alarmSetting = res.data;
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
          });
      }
    },
    handleAlarmMessageSearch() {
      if (this.alarmMessage2Search === '') {
        this.lookupAll();
      } else {
        alarmMessageLike(this.alarmMessage2Search, 0, this.pageSize)
          .then((res) => {
            if (res.meta.code !== 0) {
              this.$message({
                showClose: true,
                message: '请求异常，请稍后再试',
                center: true,
              });
              return null;
            }
            this.alarmSetting = res.data;
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
          });
      }
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
