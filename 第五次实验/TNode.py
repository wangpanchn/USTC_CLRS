class TNode(object):
    def __init__(self, key=None):     # 构造函数
        self.key = key                    # 关键字
        self.count = 0                    # 出现次数
        self.left = None                # 左孩子
        self.right = None              # 右孩子

    def set_key(self, key):
        self.key = key

    def get_key(self):
        return self.key

    def set_count(self,count):
        self.count = count

    def get_count(self):
        return self.count

    def set_left(self, left):
        self.left = left

    def get_left(self):
        return self.left

    def set_right(self, right):
        self.right = right

    def get_right(self):
        return self.right

