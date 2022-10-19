package com.yangmy.cobweb.application.admin.utils;

import com.yangmy.cobweb.application.admin.domain.dto.Node;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
@Slf4j
public class ClusterContext {

    private final Map<String, Node> nodeMap =new ConcurrentHashMap<>();

    public List<Node> list(){
        List<Node> list=new ArrayList<>();
        for (String ip : nodeMap.keySet()) {
            list.add(nodeMap.get(ip));
        }
        return list;
    }

    public void put(String ip,Node node){
        nodeMap.put(ip,node);
    }

    public void remove(String ip){
        nodeMap.remove(ip);
    }

    public void clear(){
        nodeMap.clear();
    }

    //根据ip获取letBaseUrl
    public String getNodeBaseURL(String ip){
        Node node= nodeMap.get(ip);
        if(node==null||node.getBaseURL().equals("")
        ||node.getBaseURL()==null){
            throw new RuntimeException("集群故障:无法操作，该节点已失联");
        }
        return node.getBaseURL();
    }
}
