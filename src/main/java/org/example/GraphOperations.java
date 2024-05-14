package org.example;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.HashMap;

/**
 *  对图进行操作的类
 */
public class GraphOperations {
    // 两个哈希表，对在建图时需要
    public static HashMap<String , Node> nodeHashMap = new HashMap<>();
    public static HashMap<String, Edge> edgeHashMap = new HashMap<>();

    /**
     *  根据单词数组，生成有向图
     * @param words
     * @return 有向图
     */
    public static Graph generateGraph(String[] words) {
        Graph graph = new MultiGraph("DirectedGraph");

        //用两个指针遍历单词数组建立图
        int front = -1, rear = 0, len = words.length;
        while (rear < len) {
            String word = words[rear];
            if (front < 0 || !nodeHashMap.containsKey(word)) { // 没有这个word节点
                Node node = graph.addNode(words[rear]);
                node.setAttribute("ui.style", "size: 30px; text-size: 30px; fill-color: yellow;");
                node.setAttribute("ui.label", node.getId());
                nodeHashMap.put(word, node);
                if (front >= 0) {
                    Edge edge = graph.addEdge(words[front]+"->"+word, words[front], word, true);
                    edge.setAttribute("weight", 1);
                    edge.setAttribute("ui.style", "size: 3px; text-size: 30px; shape: cubic-curve;");
                    edge.setAttribute("ui.label", (Object) edge.getAttribute("weight"));
                    edgeHashMap.put(words[front]+"->"+word, edge);
                }
            } else { // 有这个word节点
                if (!edgeHashMap.containsKey(words[front]+"->"+word)) { // 没有这条边
                    Edge edge = graph.addEdge(words[front]+"->"+word, words[front], word, true);
                    edge.setAttribute("weight", 1);
                    edge.setAttribute("ui.style", "size: 3px; text-size: 30px;");
                    edge.setAttribute("ui.label", (Object) edge.getAttribute("weight"));
                    edgeHashMap.put(words[front]+"->"+word, edge);
                } else {
                    // 先获取到这条边的对象 然后修改其权值
                    Edge edge = edgeHashMap.get(words[front]+"->"+word);
                    int weight = (int)edge.getAttribute("weight");
                    weight++;
                    edge.setAttribute("weight", weight);
                    edge.setAttribute("ui.label", (Object) edge.getAttribute("weight"));
                }
            }
            front ++;  rear ++;
        }
        return graph;

    }


}
