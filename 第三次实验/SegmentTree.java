package SegmentTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

/**
 * 区间树(红黑树的变种)
 */
enum Color {RED, BLACK};

class Segment {
    private int low;       //左端点
    private int high;      //右端点

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }
}

class Node {
    private int data;
    private Color color;
    private Node left;
    private Node right;
    private Node parent;
    //区间树的新增部分
    private int max;            //存储左右区间中最大的端点 max = max(left,right,max);
    private Segment segment;    //区间信息

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }
}

public class SegmentTree {
    private Node root;
    private Node nil;

    public void Segment() {
        nil = new Node();
        nil.setLeft(null);
        nil.setRight(null);
        nil.setColor(Color.BLACK);
        nil.setMax(0);
        Segment seg = new Segment();
        seg.setLow(0);
        seg.setHigh(0);
        nil.setSegment(seg);
        root.setParent(nil);
        root.setLeft(nil);
        root.setRight(nil);
    }

    //左旋
    public void leftRotate(Node x) {
        Node y = x.getRight();
        x.setRight(y.getLeft());
        if (y.getLeft() != nil) y.getLeft().setParent(x);
        y.setParent(x.getParent());
        if (x.getParent() == nil) {
            root = y;
        } else {
            if (x.getParent().getLeft() == x) {
                x.getParent().setLeft(y);
            } else {
                x.getParent().setRight(y);
            }
        }
        y.setLeft(x);
        x.setParent(y);
    }

    //右旋
    public void rightRotate(Node x) {
        Node y = x.getLeft();
        x.setLeft(y.getRight());
        if (y.getRight() != nil) y.getRight().setParent(x);
        y.setParent(x.getParent());
        if (x.getParent() == nil) {
            root = y;
        } else {
            if (x.getParent().getLeft() == x) {
                x.getParent().setLeft(y);
            } else {
                x.getParent().setRight(y);
            }
        }
        y.setRight(x);
        x.setParent(y);
    }

    //调整Max
    public void maxFixup(Node node) {
        int nodeMax = Math.max(node.getLeft().getMax(), node.getSegment().getHigh());
        nodeMax = Math.max(node.getRight().getMax(), nodeMax);
        node.setMax(nodeMax);
    }

    //插入节点
    public void insert(Node node) {
        Node cur = root;
        Node p = root;
        while (cur != nil) {
            p = cur;
            //设置该区间中最大端点
            int m = Math.max(node.getMax(), cur.getMax());
            cur.setMax(m);
            if (node.getData() < cur.getData()) {
                cur = cur.getLeft();
            } else {
                cur = cur.getRight();
            }
        }
        node.setParent(p);
        if (p != nil) {
            if (node.getData() < p.getData()) {
                p.setLeft(node);
            } else {
                p.setRight(node);
            }
        } else {
            root = node;
        }
        node.setColor(Color.RED);
        //插入调整
        insertFixup(node);
    }

    public void insertFixup(Node node) {
        Node parent, gparent;
        while ((parent = node.getParent()) != nil && parent.getColor() == Color.RED) {
            gparent = parent.getParent();
            if (parent == gparent.getLeft()) {       //父节点是祖父节点的左孩子
                Node uncle = gparent.getRight();
                //情况1：叔叔节点都为红色
                if (uncle != null && uncle.getColor() == Color.RED) {
                    //上层染黑,矛盾上移
                    parent.setColor(Color.BLACK);
                    uncle.setColor(Color.BLACK);
                    gparent.setColor(Color.RED);
                    node = gparent;
                    continue;
                }
                //情况2: 叔叔节点为黑色，且自己为父节点的右孩子
                if (parent.getRight() == node) {
                    //进行左旋,转换为情况3
                    leftRotate(parent);
                    Node tmp = node;
                    node = parent;
                    parent = tmp;
                    //调整max
                    maxFixup(node);
                    maxFixup(parent);
                }
                //情况3：叔叔节点为黑色，且自己为父节点的左孩子
                parent.setColor(Color.BLACK);
                gparent.setColor(Color.RED);
                rightRotate(gparent);
                //调整max
                maxFixup(gparent);
                maxFixup(parent);
            } else {                                 //父节点是祖父节点的右孩子
                Node uncle = gparent.getLeft();
                //情况1：叔叔节点都为红色
                if (uncle != nil && uncle.getColor() == Color.RED) {
                    //上层染黑,矛盾上移
                    parent.setColor(Color.BLACK);
                    uncle.setColor(Color.BLACK);
                    gparent.setColor(Color.RED);
                    node = gparent;
                    continue;
                }
                //情况2: 叔叔节点为黑色，且自己为父节点的左孩子
                if (parent.getLeft() == node) {
                    //进行右旋,转换为情况3
                    rightRotate(parent);
                    Node tmp = node;
                    node = parent;
                    parent = tmp;
                    //调整max
                    maxFixup(node);
                    maxFixup(parent);
                }
                //情况3：叔叔节点为黑色，且自己为父节点的左孩子
                parent.setColor(Color.BLACK);
                gparent.setColor(Color.RED);
                leftRotate(gparent);
                //调整max
                maxFixup(gparent);
                maxFixup(parent);
            }
        }
        //将根节点设为黑色
        root.setColor(Color.BLACK);
    }

    //两区间是否重叠
    public boolean isSubSegment(Segment seg, Segment subSeg) {
        return seg.getLow() <= subSeg.getHigh() && subSeg.getLow()<=seg.getHigh();
    }

    //查找重叠子区间
    public Node findSubSegment(Segment subSeg) {
        Node x = root;
        while (x != nil && !isSubSegment(x.getSegment(), subSeg)) {
            if (x.getLeft() != nil && subSeg.getLow() <= x.getLeft().getMax()) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }
        return x;
    }

    public void print() {
        Stack<Node> stack = new Stack<Node>();
        Node p = root;
        while (p != nil || !stack.isEmpty()) {
            if (p != nil) {
                stack.push(p);
                p = p.getLeft();
            } else {
                p = stack.pop();
                System.out.println(p.getData() + "," + p.getColor() + ",max:" + p.getMax() + ", segment:" + p.getSegment().getLow() + "-" + p.getSegment().getHigh());
                p = p.getRight();
            }
        }
    }

    public static void main(String[] args) {
        SegmentTree sgTree = new SegmentTree();
        sgTree.readFile();
        sgTree.print();
        System.out.println("root:" + sgTree.root.getData() + " max:" + sgTree.root.getMax());
        System.out.println("请输入子区间左右端点:");
        Scanner scanner = new Scanner(System.in);
        int low, high;
        if(scanner.hasNextInt()){
            low = scanner.nextInt();
        }else{
            System.out.println("请输入数据！");
            return;
        }
        if(scanner.hasNextInt()){
            high = scanner.nextInt();
        }else{
            System.out.println("请输入数据！");
            return;
        }
        scanner.close();
        //low = 32;
        //high = 34;
        Segment subSeg = new Segment();
        subSeg.setLow(low);
        subSeg.setHigh(high);
        Node node = sgTree.findSubSegment(subSeg);
        if (node != sgTree.nil) {
            System.out.println("找到Node = low:" + node.getSegment().getLow() + ",high:" + node.getSegment().getHigh());
        } else {
            System.out.println("未找到");
        }
    }

    //读取文件
    public void readFile() {
        try {
            File file = new File("C:\\Users\\pan\\Desktop\\研一（上）作业\\算法导论实验课\\第三次实验\\insert.txt");
            Scanner scanner = new Scanner(file);
            Scanner line;
            scanner.nextLine();
            while (scanner.hasNext()) {
                line = new Scanner(scanner.nextLine());//一行
                if (!line.hasNext()) break;
                Node node = new Node();
                //node.setData(scanner.nextInt());
                Segment seg = new Segment();        //区间
                seg.setLow(line.nextInt());
                seg.setHigh(line.nextInt());
                node.setMax(seg.getHigh());
                node.setData(seg.getLow());        //左端点作为data
                node.setSegment(seg);               //区间
                node.setLeft(nil);
                node.setRight(nil);
                node.setParent(nil);
                this.insert(node);
                line.close();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        }
    }
}
