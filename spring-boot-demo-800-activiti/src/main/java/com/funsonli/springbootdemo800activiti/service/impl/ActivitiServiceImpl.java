package com.funsonli.springbootdemo800activiti.service.impl;

import com.funsonli.springbootdemo800activiti.service.ActivitiService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 流程实现类
 *
 * @author Funsonli
 * @date 2019/11/12
 */
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
