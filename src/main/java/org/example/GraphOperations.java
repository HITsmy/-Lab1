package org.example;
import org.example.Tool.edge;
import org.example.Util;
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
                    edge.setAttribute("ui.style", "size: 3px; text-size: 30px;");
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
     * @param  word
     * @return boolean
     */
    public static boolean searchWord(String word){
        if(Util.nodes.contains(word)){
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
    public static String queryBridgeWords(String word1, String word2){
        if(!searchWord(word1) || !searchWord(word2)){
            System.out.println("No "+word1+" or "+word2+" in the graph!");
            return null;

        }
        List<String> bw = new ArrayList<>();
        boolean flag = false;
        for(edge i : Util.links.get(word1)){

            List<edge> nextedge = Util.links.get(i.dst);
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
    public static String generateNewText(String inputText){
        String [] splitParts = inputText.split(" ");
        List<String> sp = Arrays.asList(splitParts);
        List<String> backet = new ArrayList<>();
        backet.add(sp.get(0));
        for(int i=1; i<splitParts.length;i++){
            String bw =queryBridgeWords(splitParts[i-1],splitParts[i]);
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
     * @param word1, word2
     * @return String
     */
    public static void calcShortestPath(String word1, String word2){
        //初始化S,U,R
        HashMap<String, Integer> S = new HashMap();
        HashMap<String, Integer> U = new HashMap();
        HashMap<String,List<String>> R = new HashMap<>();
        S.put(word1,0);
        for(String i : Util.nodes){
            List<String> a = new ArrayList<>();
            a.add(word1);
            R.put(i, a);
            if(!i.equals(word1)){
                U.put(i, 10000);
            }
        }
        for(edge i : Util.links.get(word1)){
           U.put(i.dst, Util.weights.get(i));
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
            R.get(chosenNode).add(chosenNode);
            U.remove(chosenNode);
            System.out.println(chosenNode+"-----------------------------------------------------------");
            //System.out.println(R.get(chosenNode));
            System.out.println(R.get(chosenNode));
            //重新计算期望距离
            if(Util.links.get(chosenNode)!=null){

                for(edge i : Util.links.get(chosenNode)){
                    if(U.get(i.dst)!=null && U.get(i.dst)>Util.weights.get(i)+S.get(chosenNode)){

                        U.put(i.dst,Util.weights.get(i)+S.get(chosenNode));
                        R.remove(i.dst);

                        R.put(i.dst,new ArrayList<>(R.get(chosenNode)));
                    }
                }


            }
            if(U.size()==1){
                System.out.println(S +"   "+ U);

                chosenNode = U.keySet().toArray()[0].toString();
                S.put(chosenNode,U.get(chosenNode));
                R.get(chosenNode).add(chosenNode);
                U.remove(chosenNode);
                System.out.println(chosenNode+"-----------------------------------------------------------");
                //System.out.println(R.get(chosenNode));
                System.out.println(R.get(chosenNode));
                break;
            }
            System.out.println(S +"   "+ U);

        }

        /**
         *  选做，将word1到所有节点的路径都打印出来
         */
        for (String key : R.keySet()) {
            List<String> path = R.get(key);
            StringBuilder pathStr = new StringBuilder("");
            for (String node : path) {
                pathStr.append(node+" -> ");
            }
            if (pathStr.length() >= 4) {
                pathStr.delete(pathStr.length() - 4, pathStr.length());
            }
            String pathStr_ = pathStr.toString();
            System.out.println(key+":  "+pathStr_);
        }



        /**
         *  实现可视化, word1到word2
         */

        // 获取到word1到word2的节点路径
        List<String> path = R.get(word2);
        int front = -1, rear = 0;
        while (rear < path.size()) {
            Node node = nodeHashMap.get(path.get(rear));
            node.setAttribute("ui.style", "size: 30px; text-size: 30px; fill-color: pink;");
            if (front >= 0) {
                String edgeString = path.get(front) + "->" +path.get(rear);
                System.out.println(front);
                System.out.println(rear);
                System.out.println(edgeString);
                Edge edge = edgeHashMap.get(edgeString);
                edge.setAttribute("ui.style", "size: 3px; text-size: 30px; fill-color: pink;");
            }
            rear ++; front ++;
        }
    }
    /**
     * 随机漫步
     * @param util
     * @return String
     */
    public static String randomWalk(String filename){
        //随机选取起始点
        Random rand = new Random();
        String chosenNode = Util.nodes.get(rand.nextInt(Util.nodes.size()));
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
            FileWriter write=new FileWriter("src/main/java/org/example/routine/"+filename);
            BufferedWriter bw=new BufferedWriter(write);
            bw.write(tempNode+"  ");
            for( ;true; ){

                //寻找出边
                List<edge> optionalEdge = Util.links.get(tempNode);
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

