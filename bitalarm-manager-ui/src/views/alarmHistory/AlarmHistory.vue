<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>管理服务</el-breadcrumb-item>
      <el-breadcrumb-item>数据查询</el-breadcrumb-item>
      <el-breadcrumb-item>报警历史查询</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片工作区 -->
    <el-card class="box-card">
      <!-- 搜索与添加区域 -->
      <div style="margin-top: 15px;">
        <el-divider content-position="left">综合信息查询</el-divider>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input
              placeholder="请输入报警设置ID，仅支持精确查询"
              class="input-with-select"
              v-model="alarmSettingId2Search"
              clearable
              oninput="this.value=this.value.replace(/[^\d.]/g,'');"
            >
            </el-input>
          </el-col>
          <el-col :span="8">
            <el-date-picker
              class="search-row"
              v-model="dateRange2Search"
              type="datetimerange"
              :picker-options="pickerOptions"
              range-separator="至"
              start-placeholder="报警开始最早日期"
              end-placeholder="报警开始最晚日期"
              value-format="timestamp"
              @change="handleDateChange"
            >
            </el-date-picker>
          </el-col>
          <el-col :span="8">
            <el-input
              placeholder="请输入持续时间，单位为毫秒"
              class="input-with-select"
              v-model="duration2Search"
              clearable
              @input="handleDurationInput"
            >
              <el-select
                class="top-input-select"
                slot="prepend"
                v-model="gtLt2Search"
                placeholder="请选择">
                <el-option
                  v-for="item in gtLtOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-input>
          </el-col>
          <el-col :span="2">
            <el-button-group class="search-row">
              <el-button
                class="search-row-button"
                icon="el-icon-search"
                @click="handleGeneralSearch"
              >
              </el-button>
              <el-button
                class="search-row-button"
                icon="el-icon-close"
                @click="handleGeneralSearchClear"
              >
              </el-button>
            </el-button-group>
          </el-col>
        </el-row>
        <el-divider content-position="left">报警类型、报警信息查询</el-divider>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-select
              class="search-row"
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
          <el-col :span="18">
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
        </el-row>
      </div>
      <!-- 表格 -->
      <el-table
        :data="alarmHistory.data"
        stripe
        style="width: 100%">
        <el-table-column
          prop="key.long_id"
          label="ID"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="alarm_setting_key.long_id"
          label="报警设置ID"
          width="180px">
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
          prop="start_date"
          label="起始日期"
          :formatter="timestampFormatter"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="end_date"
          label="结束日期"
          :formatter="timestampFormatter"
          width="180px">
        </el-table-column>
        <el-table-column
          prop="duration"
          label="持续时间"
          width="180px">
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
              :total="parseInt(alarmHistory.count)"
              :hide-on-single-page="true"
              :current-page.sync="currentPage"
              @current-change="onPageChanged">
            </el-pagination>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import {
  all, alarmTypeEquals, alarmMessageLike, childForAlarmSetting,
  childForAlarmSettingDurationLT, childForAlarmSettingDurationGT,
  childForAlarmSettingStartDateBetween,
  durationLt, durationGt, startDateBetween,
} from '../../api/alarmHistory';

import {
  all as allType,
} from '../../api/alarmTypeIndicator';

export default {
  name: 'AlarmHistory',
  data() {
    return {
      alarmHistory: {},
      pageSize: 10,
      currentPage: 1,
      alarmSettingId2Search: '',
      duration2Search: '',
      gtLt2Search: 'gt',
      alarmType2Search: null,
      alarmMessage2Search: '',
      dateRange2Search: null,
      alarmTypeMap: new Map(),
      alarmTypeOptions: [],
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一分钟',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 60 * 1000);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近一小时',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近一天',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            },
          },
        ],
      },
      gtLtOptions: [
        {
          value: 'gt',
          label: '大于',
        }, {
          value: 'lt',
          label: '小于',
        },
      ],
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
          this.alarmHistory = res.data;
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
          this.alarmHistory = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupChildForAlarmSetting() {
      childForAlarmSetting(this.alarmSettingId2Search, this.currentPage - 1, this.pageSize)
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
            return childForAlarmSetting(
              this.alarmSettingId2Search, res.data.total_pages - 1, this.pageSize,
            );
          }
          this.alarmHistory = res.data;
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
          this.alarmHistory = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupChildForAlarmSettingDurationLT() {
      childForAlarmSettingDurationLT(
        this.alarmSettingId2Search, this.duration2Search, this.currentPage - 1, this.pageSize,
      )
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
            return childForAlarmSettingDurationLT(
              this.alarmSettingId2Search,
              this.duration2Search,
              res.data.total_pages - 1,
              this.pageSize,
            );
          }
          this.alarmHistory = res.data;
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
          this.alarmHistory = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupChildForAlarmSettingDurationGT() {
      childForAlarmSettingDurationGT(
        this.alarmSettingId2Search, this.duration2Search, this.currentPage - 1, this.pageSize,
      )
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
            return childForAlarmSettingDurationGT(
              this.alarmSettingId2Search,
              this.duration2Search,
              res.data.total_pages - 1,
              this.pageSize,
            );
          }
          this.alarmHistory = res.data;
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
          this.alarmHistory = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupChildForAlarmSettingStartDateBetween() {
      childForAlarmSettingStartDateBetween(
        this.alarmSettingId2Search,
        this.dateRange2Search[0],
        this.dateRange2Search[1],
        this.currentPage - 1,
        this.pageSize,
      )
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
            return childForAlarmSettingStartDateBetween(
              this.alarmSettingId2Search,
              this.dateRange2Search[0],
              this.dateRange2Search[1],
              res.data.total_pages - 1,
              this.pageSize,
            );
          }
          this.alarmHistory = res.data;
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
          this.alarmHistory = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupDurationLt() {
      durationLt(this.duration2Search, this.currentPage - 1, this.pageSize)
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
            return durationLt(this.duration2Search, res.data.total_pages - 1, this.pageSize);
          }
          this.alarmHistory = res.data;
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
          this.alarmHistory = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupDurationGt() {
      durationGt(this.duration2Search, this.currentPage - 1, this.pageSize)
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
            return durationGt(this.duration2Search, res.data.total_pages - 1, this.pageSize);
          }
          this.alarmHistory = res.data;
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
          this.alarmHistory = res.data;
          this.currentPage = res.data.current_page + 1;
          return null;
        });
    },
    lookupStartDateBetween() {
      startDateBetween(
        this.dateRange2Search[0],
        this.dateRange2Search[1],
        this.currentPage - 1,
        this.pageSize,
      )
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
            return startDateBetween(
              this.dateRange2Search[0],
              this.dateRange2Search[1],
              res.data.total_pages - 1,
              this.pageSize,
            );
          }
          this.alarmHistory = res.data;
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
          this.alarmHistory = res.data;
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
    enabledFormatter(row) {
      return row.enabled ? '是' : '否';
    },
    alarmTypeFormatter(row) {
      if (this.alarmTypeMap.has(row.alarm_type)) {
        return this.alarmTypeMap.get(row.alarm_type);
      }
      return row.alarm_type;
    },
    timestampFormatter(row, column) {
      const timestamp = row[column.property];
      // 时间戳为10位需*1000，时间戳为13位的话不需乘1000 var date = new Date(timestamp*1000);
      const date = new Date(timestamp);
      const Y = `${date.getFullYear()}-`;
      const M = `${date.getMonth() + 1 < 10 ? `0${date.getMonth() + 1}` : date.getMonth() + 1}-`;
      const D = `${date.getDate()} `;
      const h = `${date.getHours()}:`;
      const m = `${date.getMinutes()}:`;
      const s = date.getSeconds();
      return Y + M + D + h + m + s;
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
            this.alarmHistory = res.data;
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
      this.alarmType2Search = '';
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
            this.alarmHistory = res.data;
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
    handleDateChange() {
      if (this.dateRange2Search != null) {
        this.duration2Search = '';
      }
    },
    handleDurationInput() {
      this.duration2Search = this.duration2Search.replace(/[^\d.]/g, '');
      if (this.duration2Search !== '') {
        this.dateRange2Search = null;
      }
    },
    handleGeneralSearch() {
      if (this.alarmSettingId2Search === '') {
        if (this.dateRange2Search == null && this.duration2Search === '') {
          this.lookupAll();
        } else if (this.dateRange2Search != null) {
          this.lookupStartDateBetween();
        } else if (this.gtLt2Search === 'gt') {
          this.lookupDurationGt();
        } else {
          this.lookupDurationLt();
        }
      } else if (this.dateRange2Search == null) {
        if (this.dateRange2Search == null && this.duration2Search === '') {
          this.lookupChildForAlarmSetting();
        } else if (this.dateRange2Search != null) {
          this.lookupChildForAlarmSettingStartDateBetween();
        } else if (this.gtLt2Search === 'gt') {
          this.lookupChildForAlarmSettingDurationGT();
        } else {
          this.lookupChildForAlarmSettingDurationLT();
        }
      }
    },
    handleGeneralSearchClear() {
      this.alarmSettingId2Search = '';
      this.dateRange2Search = null;
      this.duration2Search = '';
      this.lookupAll();
    },
  },
};
</script>

<!--suppress CssUnusedSymbol -->
<style scoped>
  .pagination-container {
    text-align: center;
  }

  .search-row {
    width: 100%;
  }

  .search-row-button {
    width: 50%;
    color: #909399;
    background-color: #F5F7FA;
  }

  .top-input-select {
    width: 80px;
  }

  .el-row {
    margin-bottom: 20px;
  }

  .el-row:last-child{
    margin-bottom: 0;
  }
</style>
