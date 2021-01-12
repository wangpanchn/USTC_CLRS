from copy import deepcopy
from time import time
import numpy as np
"""
使用回溯法实现最佳调度问题
"""


def load_data(filepath):
    """
    加载数据
    :param filepath: 文件路径
    :return: 任务数，机器数，任务列表
    """
    f = open(filepath)
    line = f.readline()
    line = line.replace("\n", "")       # 去掉换行符
    line = line.split(" ")
    tasks = f.readline().split(" ")
    tasks = [float(i) for i in tasks]
    f.close()
    return int(line[0]), int(line[1]), tasks


def backTacke(pathboard, taskList, i):
    """
    回溯法寻找最佳调度
    :param pathboard: 每个机器已经分配过的任务
    :param taskList: 可选择的任务列表
    :param i: 第i个任务
    :return:
    """
    global minFinishTime, count, bestPath
    count += 1
    if minFinishTime <= max(pathsSum):                           # 剪枝，剪枝效果明显
        return
    if len(taskList) == 0:
        finishTime = 0
        for i in range(0, len(pathboard)):                # 统计每个机器的任务时间，选择最大的时间作为完成时间
            finishTime = max(finishTime, sum(pathboard[i]))
        if finishTime < minFinishTime:
            minFinishTime = finishTime                  # 记录最优解值
            bestPath = list()
            bestPath.append(deepcopy(pathboard))        # 记录最优解的任务分配
        elif finishTime == minFinishTime:
            bestPath.append(deepcopy(pathboard))
        return
    for select in range(0, len(pathboard)):                # 每一个选择,每个机器
        task = taskList.pop()                           # 做出选择
        pathboard[select][i] = task
        pathsSum[select] += task                        # 统计该机器已分配的时间
        backTacke(pathboard, taskList, i+1)
        pathboard[select][i] = 0.0                      # 撤销选择
        taskList.append(task)
        pathsSum[select] -= task                        # 统计该机器已分配的时间


def printResult(bestPath):
    """
    打印最优解
    :param bestPath: 最优解结果数组
    :return: void
    """
    cnt = 1
    for pathBoard in bestPath:
        print("方案%d:"%cnt)
        cnt+=1
        for mechineId in range(0, len(pathBoard)):
            print("Mechine%d"%(mechineId+1),end=": ")
            for i in range(0, len(tasks)):
                if pathBoard[mechineId][i]>0:
                    print("<%d,%.1f>"%(len(tasks)-i,pathBoard[mechineId][i]), end=",")
                    #print("<%d>" % (len(tasks) - i), end=",")
            print("")
        break


if __name__ == "__main__":
    start = time()*1000
    n, m, tasks = load_data("data/data.txt")      # n:任务数，m:机器数, tasks:任务
    # pathboard = [[0.0 for _ in range(n)] for _ in range(m)]   # 每行代表一个机器所分配的任务
    pathsSum = [0.0]*m                          # 记录当前每个机器已经分配的总时间，用来剪枝
    pathboard = np.zeros((m, n), dtype=np.float).tolist()
    bestPath = list()                           # 记录最优路径
    count = 0                                   # 统计执行多少次回溯
    minFinishTime = float('inf')                # 浮点型的最大值
    backTacke(pathboard, tasks, 0)
    printResult(bestPath)                       # 打印最优解
    print("最短时间：", minFinishTime)
    end = time()*1000
    # print("运行时间:", end - start, "ms")
    # print(count)