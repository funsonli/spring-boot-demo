# Spring Boot入门样例-800-activiti整合activiti工作流业务流程管理

> 日常员工申请请假，有些公司需要经理和老板审批，有些公司需要经理/总监/HR/总经理审批，Activiti将这样业务流程变成可设计，从而无需开发千变万化的流程。本demo演示整合activiti实现工作流业务流程管理。

### 前言

本Spring Boot入门样例准备工作参考：

- [Spring Boot入门样例-001-Java和Maven安装配置](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-001-java.md)
- [Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)
- [Spring Boot入门样例-005-如何运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

> idea中需要安装actibpm插件，bpmn才能正确显示 插件参考[Spring Boot入门样例-003-idea 安装配置和插件](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-003-idea.md)

### pox.xml
必要的依赖如下，具体参见该项目的pox.xml
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-spring-boot-starter-basic</artifactId>
            <version>6.0.0</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
```

### 配置文件

resources/application.yml配置内容
```

spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:mysql://localhost:3306/springbootdemo?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true
    username: root
    password: root
    hikari:
      minimum-idle: 5
      maximum-pool-size: 15
      auto-commit: true
      idle-timeout: 30000
      pool-name: DatebookHikariCP
      max-lifetime: 1800000
      connection-timeout: 30000
      connection-test-query: SELECT 1

  activiti:
    check-process-definitions: true # activti是否自动部署
    db-identity-used: true #是否使用activti自带的用户体系
    database-schema-update: true #是否每次都更新数据库

```

### 代码解析

ActivitiService.java 如下

```
public interface ActivitiService {
    /**
     * 启动流程
     *
     * @param processDefinitionKey 流程定义id
     * @param paramsMap            参数
     * @return
     */
    ProcessInstance startProcess(String processDefinitionKey, Map<String, Object>
            paramsMap) throws Exception;

    /**
     * 完成任务
     *
     * @param taskId    任务id
     * @param paramsMap 任务携带变量
     */
    void complete(String taskId, Map<String, Object> paramsMap);

    /**
     * 完成任务
     *
     * @param taskId
     */
    public void complete(String taskId);

    /**
     * 通过流程id 查询任务
     *
     * @param processInstanceId
     * @return
     */
    Task queryTaskByProcessId(String processInstanceId);

    /**
     * 通过任务id，查询任务信息
     *
     * @param taskId
     * @return
     */
    Task queryTaskById(String taskId);

    /**
     * 设置任务认领组
     *
     * @param taskId
     * @param groupId
     */
    void addCandidateGroup(String taskId, String groupId);


    /**
     * 认领任务
     *
     * @param taskId 任务id
     * @param userId 认领人id
     */
    void claim(String taskId, String userId);

    /**
     * 设置认证用户，用于定义流程启动人
     *
     * @param userId
     */
    void setAuthUser(String userId);

    /**
     * 查看定义的流程图
     *
     * @param processDefinitionId
     * @return
     */
    byte[] definitionImage(String processDefinitionId) throws IOException;

    /**
     * 查看流程进度图
     *
     * @param pProcessInstanceId
     * @return
     * @throws Exception
     */
    byte[] getProcessImage(String pProcessInstanceId) throws Exception;


    /**
     * 通过任务和变量名称获取变量
     *
     * @param taskId
     * @param varName
     * @return
     */
    public Object queryVariables(String taskId, String varName);


    /**
     * 通过流程id 查询流程
     *
     * @param processId
     * @return
     */
    HistoricProcessInstanceEntity queryProcessInstance(String processId);


//    **************** 流程管理 interface **************

    /**
     * 删除流程
     *
     * @param processInstanceId
     * @param deleteReason
     * @return
     * @throws Exception
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason) throws Exception;

    /**
     * 流程部署
     * 此方法为手动部署，传入/resource/processes目录下的流程问卷名称即可
     *
     * @param fileName 文件名
     * @param category 流程分类
     */
    Deployment deploy(String fileName, String category);

    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentId 流程部署ID
     */
    void deleteDeployment(String deploymentId);

    /**
     * 流程部署列表
     *
     * @return
     */
    List<Deployment> deployList();

    /**
     * 获取部署流程列表
     *
     * @return
     */
    List<ProcessDefinition> getProcessList() throws Exception;

    /**
     * 挂起流程
     *
     * @param processDefinitionId
     * @throws Exception
     */
    void suspendProcess(String processDefinitionId) throws Exception;

    /**
     * 激活流程
     *
     * @param processDefinitionId
     * @throws Exception
     */
    void activateProcess(String processDefinitionId) throws Exception;

    /**
     * 任务回退
     *
     * @param taskId  当前任务id
     * @param userId  用户id
     * @param reason  理由
     * @param groupId 分组id
     * @param backNum 退回数
     * @throws Exception
     */
    public void rollBackTask(String taskId, String userId, String reason, String groupId, int backNum) throws
            Exception;

}
```

ActivitiServiceImpl.java 如下 具体流程实现

```
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ActivitiServiceImpl implements ActivitiService {

    /**
     * 工作流运行服务
     */
    @Autowired
    private RuntimeService runtimeService;
    /**
     * 工作流任务服务
     */
    @Autowired
    private TaskService taskService;


    /**
     * 工作流历史数据服务
     */
    @Autowired
    private HistoryService historyService;

    /**
     * 用户服务
     */
    @Autowired
    private IdentityService identityService;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 启动流程
     *
     * @param processDefinitionKey 流程定义id
     * @param paramsMap            参数
     * @return 流程实例
     * @throws Exception
     */
    @Override
    public ProcessInstance startProcess(String processDefinitionKey, Map<String, Object> paramsMap) throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, paramsMap);
        return processInstance;
    }


    @Override
    public Object queryVariables(String taskId, String varName) {
        HistoricVariableInstance variableInstance =
                historyService.createHistoricVariableInstanceQuery().taskId(taskId).variableName(varName).singleResult();
        return variableInstance.getValue();
    }

    @Override
    public HistoricProcessInstanceEntity queryProcessInstance(String processId) {
        return null;
    }

    /**
     * 查找流程申请人
     *
     * @param processId
     * @return
     */
    private String getProcessStartUserId(String processId) {
        HistoricProcessInstance historicProcessInstance = queryHistoricProcessInstance(processId);
        return historicProcessInstance.getStartUserId();
    }


    /**
     * 通过流程id查询流程
     *
     * @param processInstanceId
     * @return
     */
    private HistoricProcessInstance queryHistoricProcessInstance(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }


    @Override
    public void complete(String taskId, Map<String, Object> paramsMap) {
        Map<String, Object> filterMap = paramsMap.entrySet().stream().filter(map -> map.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // 存储下个节点权限组，以及当前审核说明
        //设置任务完成时间
        taskService.setVariableLocal(taskId, "reason", paramsMap.get("reason"));
        taskService.setVariableLocal(taskId, "auditUserId", paramsMap.get("userId"));
        taskService.setVariableLocal(taskId, "createDate", new Date());
        taskService.complete(taskId, filterMap);
    }

    @Override
    public void complete(String taskId) {
        //设置任务完成时间
        taskService.setVariableLocal(taskId, "createDate", new Date());
        taskService.complete(taskId);
    }

    @Override
    public Task queryTaskByProcessId(String processInstanceId) {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        return task;
    }

    @Override
    public Task queryTaskById(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return task;
    }

    @Override
    public void addCandidateGroup(String taskId, String groupId) {
        taskService.addCandidateGroup(taskId, groupId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void claim(String taskId, String userId) {
        taskService.claim(taskId, userId);
    }

    @Override
    public void setAuthUser(String userId) {
        identityService.setAuthenticatedUserId(userId);
    }

    /**
     * 获取流程图像，已执行节点和流程线高亮显示
     */
    @Override
    public byte[] getProcessImage(String processInstanceId) throws Exception {
        //  获取历史流程实例
        HistoricProcessInstance historicProcessInstance = queryHistoricProcessInstance(processInstanceId);
        if (historicProcessInstance == null) {
            throw new Exception();
        } else {
            // 获取流程定义
            ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
                    .getProcessDefinition(historicProcessInstance.getProcessDefinitionId());

            // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
            List<HistoricActivityInstance> historicActivityInstanceList = historyService
                    .createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                    .orderByHistoricActivityInstanceId().asc().list();

            // 已执行的节点ID集合
            List<String> executedActivityIdList = new ArrayList<String>();
            @SuppressWarnings("unused") int index = 1;
            log.info("获取已经执行的节点ID");
            for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
                executedActivityIdList.add(activityInstance.getActivityId());
                log.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " + activityInstance
                        .getActivityName());
                index++;
            }
            // 获取流程图图像字符流
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
            DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
            InputStream imageStream = generator.generateDiagram(bpmnModel, "png", executedActivityIdList);
            byte[] buffer = new byte[imageStream.available()];
            imageStream.read(buffer);
            imageStream.close();
            return buffer;
        }
    }

    /**
     * 查看 定义的流程图
     *
     * @param processDefinitionId 流程id
     * @return
     */
    @Override
    public byte[] definitionImage(String processDefinitionId) throws IOException {
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        if (model != null && model.getLocationMap().size() > 0) {
            ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
            InputStream imageStream = generator.generateDiagram(model, "png", new ArrayList<>());
            byte[] buffer = new byte[imageStream.available()];
            imageStream.read(buffer);
            imageStream.close();
            return buffer;
        }
        return null;
    }

    @Override
    public void deleteProcessInstance(String processInstanceId, String deleteReason) throws Exception {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    /**
     * 此方法为手动部署，传入/resource/processes目录下的流程名称即可
     *
     * @param fileName
     * @return
     */
    @Override
    public Deployment deploy(String fileName, String category) {
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/" + fileName)
                .category(category).deploy();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId())
                .list();

        // 设置流程分类
        for (ProcessDefinition processDefinition : list) {
            repositoryService.setProcessDefinitionCategory(processDefinition.getId(), category);
            log.info("部署成功，流程ID=" + processDefinition.getId());
        }
        long count = repositoryService.createProcessDefinitionQuery().count();
        log.info("deploy name:{},id:{},definition count:{} ", deploy.getName(), deploy.getId(), count);
        return deploy;
    }

    @Override
    public void deleteDeployment(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    @Override
    public List<Deployment> deployList() {
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        return list;
    }

    @Override
    public List<ProcessDefinition> getProcessList() throws Exception {
        return repositoryService.createProcessDefinitionQuery().list();
    }

    @Override
    public void suspendProcess(String processDefinitionId) throws Exception {
        repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
    }

    @Override
    public void activateProcess(String processDefinitionId) throws Exception {
        repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rollBackTask(String taskId, String uid, String reason, String groupId, int backNum) throws
            Exception {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new Exception("流程未启动或已执行完成，无法撤回");
        }
        String applyUserId = getCurrentApplyUserId(task.getProcessInstanceId());
        // 上一个task
        HistoricTaskInstance preTask = getApplyUserTask(task.getProcessInstanceId(), backNum);
        if (preTask == null || preTask.getId() == null) {
            return;
        }
        String processDefinitionId = preTask.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        //变量
        HistoricActivityInstance myActivity = getCurrentApplyActivity(task.getExecutionId(), preTask.getId());
        String myActivityId = myActivity.getActivityId();

        //得到流程节点
        FlowNode targetNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);
        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        log.warn("------->> activityId:" + activityId);
        FlowNode sourceNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);
        // 记录原来方向
        List<SequenceFlow> oriSequenceFlowList = new ArrayList<>();
        oriSequenceFlowList.addAll(sourceNode.getOutgoingFlows());
        //清理活动方向
        sourceNode.getOutgoingFlows().clear();
        //建立新方向
        taskService.setVariableLocal(task.getId(), "reason", reason);
        taskService.setVariableLocal(task.getId(), "auditGroupId", groupId);
        buildNewFlowNode(task, applyUserId, targetNode, sourceNode, uid, reason);
        //记录原活动方向，恢复原方向
        sourceNode.setOutgoingFlows(oriSequenceFlowList);
        // 设置任务候选人
        setAssigneeUser(task, preTask.getAssignee());
    }

    /**
     * 新任务设置候选人
     *
     * @param task
     * @param applyUserId
     */
    private void setAssigneeUser(Task task, String applyUserId) {
        Task newTask = queryTaskByProcessId(task.getProcessInstanceId());
        taskService.setAssignee(newTask.getId(), applyUserId);
    }

    /**
     * 建立新方向
     *
     * @param task
     * @param applyUserId
     * @param targetNode
     * @param sourceNode
     */
    private void buildNewFlowNode(Task task, String applyUserId, FlowNode targetNode, FlowNode sourceNode, String
            uid, String reason) {
        List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(sourceNode);
        newSequenceFlow.setTargetFlowElement(targetNode);
        newSequenceFlowList.add(0, newSequenceFlow);
        sourceNode.setOutgoingFlows(newSequenceFlowList);
        identityService.setAuthenticatedUserId(applyUserId);
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "驳回");
        //添加代理
        taskService.setAssignee(task.getId(), uid);
        //完成任务
        task.setCategory("refuse");
        taskService.setVariableLocal(task.getId(), "reson", reason);
        complete(task.getId());
        Task t =
                taskService.createTaskQuery().active().processInstanceId(task.getProcessInstanceId()).singleResult();
        taskService.setVariableLocal(t.getId(), "reason", reason);
    }

    /**
     * 获取当前流程的申请人
     *
     * @param processId
     * @return
     */
    private String getCurrentApplyUserId(String processId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId)
                .singleResult();
        String applyUserId = processInstance.getStartUserId();
        return applyUserId;
    }

    /**
     * 获取当前任务的历史活动实例
     *
     * @param excutionId 任务执行id
     * @param taskId     任务id
     * @return
     */
    private HistoricActivityInstance getCurrentApplyActivity(String excutionId, String taskId) {
        HistoricActivityInstance myActivity = null;
        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery().executionId
                (excutionId).finished().list();
        for (HistoricActivityInstance hai : haiList) {
            if (taskId.equals(hai.getTaskId())) {
                myActivity = hai;
                break;
            }
        }
        return myActivity;
    }

    /**
     * 得到申请流程用户的任务
     *
     * @param processInstanceId 流程实例id
     * @param backNum           回退节点数
     * @return
     */
    private HistoricTaskInstance getApplyUserTask(String processInstanceId, int backNum) {
        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).finished().orderByTaskCreateTime().asc().list();
        // 如果返回数有误，直接返回空
        if (backNum <= 0 || taskInstanceList.size() < backNum) {
            return null;
        }
        return taskInstanceList.get(taskInstanceList.size() - backNum);
    }
}

```

SpringBootDemo800ActivitiApplication.java 如下增加exclude = {org.activiti.spring.boot.SecurityAutoConfiguration.class}，不使用activiti中的安全验证
```
@SpringBootApplication(exclude = {org.activiti.spring.boot.SecurityAutoConfiguration.class})
public class SpringBootDemo800ActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo800ActivitiApplication.class, args);
    }

}

```

SpringBootDemo800ActivitiApplicationTests.java 如下
``` 
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootDemo800ActivitiApplicationTests {
    private static Logger logger = LoggerFactory.getLogger(SpringBootDemo800ActivitiApplicationTests.class);

    @Autowired
    private ActivitiService activitiService;

    /**
     * 流程定义key
     */
    private static final String DEFAULT_PROCESS_KEY = "test";
    /**
     * 流程文件名称
     */
    private static final String DEFAULT_PROCESS_FILE_NAME = "test.bpmn";

    @Test
    public void contextLoads() {
    }

    /**
     * 流程部署
     */
    @Test
    public void deployment() {
        Deployment test = activitiService.deploy(DEFAULT_PROCESS_FILE_NAME, DEFAULT_PROCESS_KEY);
        logger.info("process deploy succeed id: " + test.getId());
    }

    /**
     * 发起流程申请
     * 可以在act_hi_taskinst表中看到经理审批的task id
     */
    @Test
    public void startProcess() throws Exception {
        //启动流程
        HashMap<String, Object> params = new HashMap<>();
        params.put("createDate", new Date());
        ProcessInstance processInstance = activitiService.startProcess(DEFAULT_PROCESS_KEY, params);

        logger.info("process start succeed id: {}", processInstance.getId());
        //完成第一个审核节点
        String userId = "1001";
        //设置认证人信息，activiti将此信息设置为process start user
        activitiService.setAuthUser(userId);
        String processInstanceId = processInstance.getId();
        Task task = activitiService.queryTaskByProcessId(processInstanceId);
        //任务认领
        activitiService.claim(task.getId(), userId);
        activitiService.complete(task.getId(), params);
        logger.info("task: {}", task.getId());
    }

    /**
     * 完成审核节点任务
     * 可以在act_hi_taskinst表中看到经理审批的task id
     */
    @Test
    public void taskComplete() {
        //  可以在act_hi_taskinst表中看到经理审批的task id
        String taskId = "50012";
        HashMap<String, Object> params = new HashMap<>();
        params.put("createDate", new Date());
        //如果audit为true，流程结束；如果为false，流程回滚到第一个审核节点
        params.put("audit", true);
        activitiService.complete(taskId, params);
        logger.info("complete task succeed id: {}", taskId);
    }

    /**
     * 重新部署流程
     */

    @Test
    public void reDeployment() {
        //重新部署流程
        String processInstanceId = "55001";
        activitiService.deleteDeployment(processInstanceId);
        activitiService.deploy(DEFAULT_PROCESS_FILE_NAME, DEFAULT_PROCESS_KEY);
    }
}
```

### 运行

点击[运行](https://github.com/funsonli/spring-boot-demo/blob/master/doc/spring-boot-demo-005-run.md)

``` 
执行SpringBootDemo800ActivitiApplicationTests::deploy()
2019-11-12 15:54:16.590  INFO 151288 --- [           main] c.f.s.service.impl.ActivitiServiceImpl   : 部署成功，流程ID=test:9:55004
2019-11-12 15:54:16.597  INFO 151288 --- [           main] c.f.s.service.impl.ActivitiServiceImpl   : deploy name:null,id:55001,definition count:9 
2019-11-12 15:54:16.612  INFO 151288 --- [           main] pringBootDemo800ActivitiApplicationTests : process deploy succeed id: 55001


执行SpringBootDemo800ActivitiApplicationTests::startProcess()
2019-11-12 15:49:50.183  INFO 153448 --- [           main] pringBootDemo800ActivitiApplicationTests : process start succeed id: 50001
2019-11-12 15:49:50.590  INFO 153448 --- [           main] pringBootDemo800ActivitiApplicationTests : task: 50006
在act_hi_taskinst表中可以看到两条历史记录，一条为员工申请 一条为经理审批  
50006	test:8:47508	_3	50001	50003	员工申请				1001	2019-11-12 01:49:50.072	2019-11-12 01:49:50.243	2019-11-12 01:49:50.479	407		50				
50012	test:8:47508	_5	50001	50003	经理审批					2019-11-12 01:49:50.489					50
在act_ru_task表示当前需要处理的任务
52512	1	52503	52501	test:8:47508	经理审批			_5				50	2019-11-12 01:53:23.161			1			

将经理审批的task id 50012填入，执行SpringBootDemo800ActivitiApplicationTests::taskComplete()
2019-11-12 15:56:57.968  INFO 153380 --- [           main] pringBootDemo800ActivitiApplicationTests : complete task succeed id: 50012

将deploy打印的deploy succeed id: 55001填入 执行SpringBootDemo800ActivitiApplicationTests::reDeployment()
2019-11-12 15:58:53.932  INFO 153936 --- [           main] c.f.s.service.impl.ActivitiServiceImpl   : 部署成功，流程ID=test:9:60004
2019-11-12 15:58:53.940  INFO 153936 --- [           main] c.f.s.service.impl.ActivitiServiceImpl   : deploy name:null,id:60001,definition count:9 
				
```

### 参考
- Spring Boot入门样例源代码地址 [https://github.com/funsonli/spring-boot-demo](https://github.com/funsonli/spring-boot-demo)
- Bootan源代码地址 [https://github.com/funsonli/bootan](https://github.com/funsonli/bootan)
- Activiti官网 https://www.activiti.org/
- Activiti表结构 https://blog.csdn.net/hj7jay/article/details/51302829

### 附
如果您喜欢本Spring Boot入门样例和样例代码，请[点赞Star](https://github.com/funsonli/spring-boot-demo)

