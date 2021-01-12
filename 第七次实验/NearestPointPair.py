import sys
import re

from numpy.ma import sqrt

from Point import Point

"""
求平面上n个顶点的最近点对问题（分治法来实现）
"""


def loadData(path):
    f = open(path)
    line = f.readline()
    S = list()
    while len(line) > 0:
        values = re.findall(r"((\d+\.\d+)|(\d+))", line, re.S)  # 使用正则表达式提取其中的数值
        # print(values)
        p = Point(line[0], float(values[0][0]), float(values[1][0]))  # 创建一个点对象
        S.append(p)
        line = f.readline()
    return S  # 原始点集合


def distance(a, b):
    """
    两点间的距离
    :param a:
    :param b:
    :return:
    """
    # return sqrt(pow(a.getX()-b.getX(), 2)-pow(a.getY()-b.getY(), 2))
    return round(sqrt(pow((a.getX() - b.getX()), 2) + pow(a.getY() - b.getY(), 2)), 6)


def divide(points):
    n = len(points)
    # 特判只有一个点或者是两个点的情况
    if n < 2:  # 一个点的情况
        return sys.maxsize, None, None
    elif n == 2:  # 两个点的情况直接求距离
        return distance(points[0], points[1]), points[0], points[1]

    # 对所有点按照横坐标进行排序
    points = sorted(points, key=lambda p: p.getX())
    # points = sorted(points, cmp=cmpX())
    half = n // 2  # 将数据集划分为两半

    # 递归划分，要先排序在递归
    delta1, a1, b1 = divide(points[:half])
    delta2, a2, b2 = divide(points[half:])
    # 治的过程
    # delta 为最短距离
    delta, a, b = (delta1, a1, b1) if delta1 < delta2 else (delta2, a2, b2)  # 双目运算

    midValue = points[half].getX()  # 中值

    # 根据中间的位置将点集分成两个部分
    left, right = [], []
    for p in points:
        if midValue - delta < p.getX() < midValue:  # 在左边δ区域内点
            left.append(p)
        elif midValue <= p.getX() < midValue + delta:  # 在右边δ的区域内点
            right.append(p)

    # 右侧点集按照纵坐标排序
    # right = sorted(right, key=lambda p: p.getX())
    right = sorted(right, key=lambda p: (p.getY(), p.getX()))
    # print([p.getY() for p in right])
    res = delta  # 当前最短距离
    for p in left:  # 遍历左部分点集，与每个右边的距离，这样宽为δ，高为2δ（对左边每个点求在右边的最短距离）
        # 左开右闭的二分（已经按Y轴排序）
        l, r = -1, len(right) - 1
        while r - l > 1:  # 二分查找，找到以当前p点为最左点的y轴最低点在数组中的index(y轴距离也在δ内)
            m = (l + r) >> 1
            if right[m].getY() <= p.getY() - delta:  # 往上找（在δ距离外）
                l = m
            else:  # 往下找（在δ距离内）
                r = m

        index = r  # 能找到的最下方的点的index
        # 在范围内最多只有7个点能够构成最近点对（从下往上最多找7个点）
        for j in range(7):
            if j + index >= len(right):  # 不够7个点的情况下
                break
            if distance(p, right[index + j]) < res:  # 找到更小距离的两个点，修改最小点对与距离
                res = distance(p, right[index + j])
                a, b = p, right[index + j]

    return res, a, b


if __name__ == "__main__":
    P = loadData("data/data.txt")
    P = sorted(P, key=lambda p: p.getX())  # 按X排序，临时排序
    # print([p.getX() for p in P])
    rd, a, b = divide(P)
    print("最近距离：", rd, "\n点对为：",
          a.getTag() + "(" + str(a.getX()) + "," + str(a.getY()) + ")",
          b.getTag() + "(" + str(b.getX()) + "," + str(b.getY()) + ")")
