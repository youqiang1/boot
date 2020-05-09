package com.yq.activiti.config;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * <p> activiti process config</p>
 * @author youq  2020/5/6 19:31
 */
@Slf4j
@Configuration
public class ActivitiProcessConfig {

    @Value("${spring.activiti.db-identity-used}")
    private boolean dbIdentityUsed;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    /**
     * <p> activiti流程引擎创建</p>
     * @param transactionManager 事物管理
     * @param dataSource         数据源
     * @return org.activiti.engine.ProcessEngine
     * @author youq  2020/5/7 15:28
     */
    @Bean
    public ProcessEngine customProcessEngine(DataSourceTransactionManager transactionManager,
                                             DataSource dataSource)
            throws IOException {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        //自动部署已有的流程文件
        //作用相当于(据bpmn文件部署流程repositoryService.createDeployment().addClasspathResource("singleAssignee.bpmn").deploy();)
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources(ResourceLoader.CLASSPATH_URL_PREFIX + "processes/*.bpmn");
        configuration.setTransactionManager(transactionManager);
        //设置数据源
        configuration.setDataSource(dataSource);
        //是否每次都更新数据库
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
        configuration.setDeploymentResources(resources);
        //设置是否使用activiti自带的用户体系
        configuration.setDbIdentityUsed(dbIdentityUsed);
        return configuration.buildProcessEngine();
    }

    /**
     * <p> 工作流仓储服务</p>
     * @param customProcessEngine activiti流程引擎
     * @return org.activiti.engine.RuntimeService
     * @author youq  2020/5/7 15:24
     */
    @Bean
    public RepositoryService getRepositoryService(ProcessEngine customProcessEngine) {
        return customProcessEngine.getRepositoryService();
    }

    /**
     * <p> 工作流运行服务</p>
     * @param customProcessEngine activiti流程引擎
     * @return org.activiti.engine.RuntimeService
     * @author youq  2020/5/7 15:24
     */
    @Bean
    public RuntimeService runtimeService(ProcessEngine customProcessEngine) {
        return customProcessEngine.getRuntimeService();
    }

    /**
     * <p> 工作流任务服务</p>
     * @param customProcessEngine activiti流程引擎
     * @return org.activiti.engine.RuntimeService
     * @author youq  2020/5/7 15:24
     */
    @Bean
    public TaskService taskService(ProcessEngine customProcessEngine) {
        return customProcessEngine.getTaskService();
    }

    /**
     * <p> 工作流历史数据服务</p>
     * @param customProcessEngine activiti流程引擎
     * @return org.activiti.engine.RuntimeService
     * @author youq  2020/5/7 15:24
     */
    @Bean
    public HistoryService historyService(ProcessEngine customProcessEngine) {
        return customProcessEngine.getHistoryService();
    }

    /**
     * <p> 工作流管理服务</p>
     * @param customProcessEngine activiti流程引擎
     * @return org.activiti.engine.RuntimeService
     * @author youq  2020/5/7 15:24
     */
    @Bean
    public ManagementService managementService(ProcessEngine customProcessEngine) {
        return customProcessEngine.getManagementService();
    }

    /**
     * <p> 工作流用户服务</p>
     * @param customProcessEngine activiti流程引擎
     * @return org.activiti.engine.RuntimeService
     * @author youq  2020/5/7 15:24
     */
    @Bean
    public IdentityService identityService(ProcessEngine customProcessEngine) {
        return customProcessEngine.getIdentityService();
    }
    //
    // @Autowired
    // private RepositoryService repositoryService;
    //
    // @Autowired
    // private RuntimeService runtimeService;
    //
    // @PostConstruct
    // public void init() {
    //     log.info("测试流程初始化。。。");
    //     Deployment deployment = repositoryService.createDeployment()
    //             .name("测试流程")
    //             .addClasspathResource("processes/TestProcess.bpmn")
    //             .deploy();
    //     log.info("流程部署ID：{}", deployment.getId());
    //     log.info("流程部署时间：{}", deployment.getDeploymentTime());
    //     log.info("流程部署名称：{}", deployment.getName());
    // }
    //
    // @PostConstruct
    // public void start() {
    //     log.info("流程启动。。。");
    //     runtimeService.startProcessInstanceByKey("testProcess");
    //     log.info("流程启动完成");
    // }

}
