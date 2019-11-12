package com.funsonli.springbootdemo800activiti.service;

/**
 * Class for
 *
 * @author Funsonli
 * @date 2019/11/12
 */
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * activiti service 接口类
 *
 * @author nickbi
 * @date 2019/1/17
 */
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
