package com.funsonli.springbootdemo800activiti;
import com.funsonli.springbootdemo800activiti.service.ActivitiService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;

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
