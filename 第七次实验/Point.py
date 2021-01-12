# 定义点对象
class Point(object):
    def __init__(self, tag, x, y):
        self.x = x            # x坐标
        self.y = y            # y坐标
        self.tag = tag        # 标签字符

    def setX(self, x):
        self.x = x

    def getX(self):
        return self.x

    def setY(self, y):
        self.y = y

    def getY(self):
        return self.y

    def setTag(self, tag):
        self.tag = tag

    def getTag(self):
        return self.tag
