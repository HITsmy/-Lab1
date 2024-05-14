package org.example.Tool;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
    /**
     * 查找节点
     * @param  util , word
     * @return boolean
     */
    public static boolean searchWord(Util util, String word){
        if(util.nodes.contains(word)){
            //System.out.println("存在——————————————————————————");
            return true;


        }
        //System.out.println("不不不存在——————————————————————————");
        return false;

    }
    /**
     *  查询桥接词
     * @param util, word1, word2
     * @return String
     */
    public static String queryBridgeWords(Util util, String word1, String word2){
        if(!searchWord(util, word1) || !searchWord(util, word2)){
            System.out.println("No "+word1+" or "+word2+" in the graph!");
            return null;

        }
        List<String> bw = new ArrayList<>();
        boolean flag = false;
        for(edge i : util.links.get(word1)){

            List<edge> nextedge = util.links.get(i.dst);
            for(edge j : nextedge){
                if(j.dst.equals(word2)){

                    flag = true;
                    bw.add(i.dst);
                }
            }
        }
        if(flag){
            System.out.println("The bridge words from "+word1+" to "+word2+" are: "+bw);
            Random rand = new Random();
            return bw.get(rand.nextInt(bw.size()));
        }
        System.out.println("No bridge words from "+word1+" to "+word2+"!");
        return null;
    }
    /**
     *  生成新文本
     * @param inputText, gs
     * @return String
     */
    public static String generateNewText(String inputText,Util util){
        String [] splitParts = inputText.split(" ");
        List<String> sp = Arrays.asList(splitParts);
        List<String> backet = new ArrayList<>();
        backet.add(sp.get(0));
        for(int i=1; i<splitParts.length;i++){
            String bw =queryBridgeWords(util,splitParts[i-1],splitParts[i]);
            if(bw!=null) {
                backet.add(bw);
                backet.add(splitParts[i]);

            }else{
                backet.add(splitParts[i]);
            }


        }
        return backet.toString();
    }





    /**
     * 最短路径（基于迪杰斯特拉算法）
     * @param  util, word1, word2
     * @return String
     */
    public static String calcShortestPath(Util util, String word1, String word2){
        //初始化S,U
        HashMap<String, Integer> S = new HashMap();
        HashMap<String, Integer> U = new HashMap();
        S.put(word1,0);
        for(String i : util.nodes){
            if(!i.equals(word1)){
                U.put(i, 10000);
            }
        }
        for(edge i : util.links.get(word1)){
           U.put(i.dst, util.weights.get(i));
        }
        System.out.println("初始化： "+U);

        //计算距离
        while(true){
            //选择最短距离
            String chosenNode = null;
            Integer cur = 100000;
            for( String key : U.keySet()){
                if(cur>U.get(key)){
                    cur = U.get(key);
                }
            }
            for( String key : U.keySet()){
                if(U.get(key).equals(cur)){
                    chosenNode = key;
                    break;

                }
            }
            S.put(chosenNode,U.get(chosenNode));
            U.remove(chosenNode);
            System.out.println(chosenNode+"-----------------------------------------------------------");

            //重新计算期望距离
            if(util.links.get(chosenNode)!=null){

                for(edge i : util.links.get(chosenNode)){
                    if(U.get(i.dst)!=null && U.get(i.dst)>util.weights.get(i)+S.get(chosenNode)){
                        U.put(i.dst,util.weights.get(i)+S.get(chosenNode));
                    }
                }


            }
            if(U.size()==1){
                System.out.println(S +"   "+ U);
                System.out.println(U.keySet().toArray()[0]+"-----------------------------------------------------------");

                break;
            }
            System.out.println(S +"   "+ U);

        }

        return "Undefined";


    }
    /**
     * 随机漫步
     * @param util
     * @return String
     */
    public static String randomWalk(Util util,String filename){
        //随机选取起始点
        Random rand = new Random();
        String chosenNode = util.nodes.get(rand.nextInt(util.nodes.size()));
        String tempNode = chosenNode;
        edge tempEdge = null;
        //初始化节点记录
        List<String> nodePath = new ArrayList<>();
        nodePath.add(chosenNode);
        //初始化路径记录
        List<edge> edgePath = new ArrayList<>();
        try {
            File file=new File("src/main/java/org/example/routine/"+filename);
            file.createNewFile();
            FileWriter write=new FileWriter("src/main/java/org/example/routine/"+filename,true);
            BufferedWriter bw=new BufferedWriter(write);
            bw.write(tempNode+"  ");
            for( ;true; ){

                //寻找出边
                List<edge> optionalEdge = util.links.get(tempNode);
                if(optionalEdge==null){
                    System.out.println("因该节点"+tempNode+" 无出边结束遍历");
                    break;

                }
                Random rand2 = new Random();
                tempEdge = optionalEdge.get(rand2.nextInt(optionalEdge.size()));
                tempNode = tempEdge.dst;
                if(edgePath.contains(tempEdge)){
                    System.out.println("因重复路径"+tempEdge.src+"————>"+tempEdge.dst+" 结束遍历");
                    break;

                }
                nodePath.add(tempNode);
                bw.write(tempNode+"  ");
                edgePath.add(tempEdge);
            }
            bw.close();
            write.close();
            return nodePath.toString();
        }catch(IOException e){
            e.printStackTrace();
            return nodePath.toString();
        }


    }


}


}
