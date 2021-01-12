from math import log, ceil
import pandas as pd
from TNode import TNode


# 加载数据
def load_data(filepath):
    f = open(filepath)
    line = f.readline()
    f.close()
    return line.replace("\n", "")        # 去掉换行符


# 按深度优先打印树
def print_tree(node, deep, path, encode):
    if node is None:
        return 0
    sum_len = 0
    # 叶子节点
    if node.get_left() is None and node.get_right() is None:
        print(node.key, "("+path+")", end=",")
        encode[node.key] = path                  # 保存Huffman编码
        sum_len = deep * node.get_count()
    else:
        sum_len += print_tree(node.left, deep+1, path + "0", encode)
        sum_len += print_tree(node.right, deep+1, path + "1", encode)
    return sum_len


# 构造Huffman树,并返回树的根节点
def build_tree(node_count):
    while len(node_count) > 1:
        node_count = pd.Series(node_count)  # 转为Series
        node_count = node_count.sort_values()  # 通过value进行排序
        node_count = node_count[-1::-1]  # 逆序
        node_count = dict(node_count)  # 转回dict
        # 取出两个最小节点，构造新的父节点,并插入dict中
        left = node_count.popitem()
        right = node_count.popitem()
        parent = TNode(left[1] + right[1])
        parent.set_left(left[0])                            # 设置左右孩子
        parent.set_right(right[0])
        node_count.update({parent: left[1] + right[1]})      # 存入已经组合的新节点
    return node_count.popitem()[0]       # 最后剩下来的为根节点


if __name__ == "__main__":
    data = load_data("data/data.txt")
    # 统计每个字符出现次数
    charCount = {}
    for i in data:
        if i not in charCount.keys():
            charCount[i] = 0
        charCount[i] += 1
    nodeCount = dict()
    pre_code_len = 0             # 编码前的字符长度
    # 将各字符转换为TNode对象
    for i in charCount:
        pre_code_len += ceil(log(len(charCount), 2))*charCount[i]       # 统计编码前的字符长度
        temp = charCount[i]  # 字母出现次数
        i = TNode(i)        # 把每个字母转为Node结点
        i.set_count(temp)       # 出现次数
        nodeCount[i] = temp
    print("编码前的字符长度为：", pre_code_len)

    # 根据node_count结点数，构造Huffman树
    root = build_tree(nodeCount)

    print("先序遍历Huffman树为", end=":")
    encode = dict()                     #保存Huffman各字符编码
    code_len = print_tree(root, 0, "", encode)                    # 打印树叶节点,并统计编码长度
    print("\n编码后的字符长度为：", code_len)
    print("压缩率为：", round(code_len/pre_code_len*100, 2), "%")

    # 将原始数据编码并输出到文件
    encode_str = ""
    for i in data:
        encode_str += encode[i]
    f = open("data/encode.txt", 'w')  # 若是'wb'就表示写二进制文件
    f.write(encode_str)
    f.close()
