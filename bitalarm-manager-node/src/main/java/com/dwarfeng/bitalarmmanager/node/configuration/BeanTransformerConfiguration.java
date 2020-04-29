package com.dwarfeng.bitalarmmanager.node.configuration;

import com.dwarfeng.bitalarm.sdk.bean.entity.*;
import com.dwarfeng.bitalarm.stack.bean.entity.*;
import com.dwarfeng.subgrade.impl.bean.DozerBeanTransformer;
import com.dwarfeng.subgrade.stack.bean.BeanTransformer;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanTransformerConfiguration {

    @Autowired
    private Mapper mapper;

    @Bean
    public BeanTransformer<AlarmSetting, JSFixedFastJsonAlarmSetting> alarmSettingJSFixedFastJsonAlarmSettingBeanTransformer() {
        return new DozerBeanTransformer<>(AlarmSetting.class, JSFixedFastJsonAlarmSetting.class, mapper);
    }

    @Bean
    public BeanTransformer<CurrentAlarm, JSFixedFastJsonCurrentAlarm>
    currentAlarmJSFixedFastJsonCurrentAlarmBeanTransformer() {
        return new DozerBeanTransformer<>(CurrentAlarm.class, JSFixedFastJsonCurrentAlarm.class, mapper);
    }

    @Bean
    public BeanTransformer<AlarmInfo, JSFixedFastJsonAlarmInfo>
    alarmInfoJSFixedFastJsonAlarmInfoBeanTransformer() {
        return new DozerBeanTransformer<>(AlarmInfo.class, JSFixedFastJsonAlarmInfo.class, mapper);
    }

    @Bean
    public BeanTransformer<AlarmHistory, JSFixedFastJsonAlarmHistory>
    alarmHistoryJSFixedFastJsonAlarmHistoryBeanTransformer() {
        return new DozerBeanTransformer<>(AlarmHistory.class, JSFixedFastJsonAlarmHistory.class, mapper);
    }

    @Bean
    public BeanTransformer<AlarmTypeIndicator, FastJsonAlarmTypeIndicator>
    AlarmTypeIndicatorJSFixedFastJsonAlarmTypeIndicatorBeanTransformer() {
        return new DozerBeanTransformer<>(AlarmTypeIndicator.class, FastJsonAlarmTypeIndicator.class, mapper);
    }
}
