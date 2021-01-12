/**
 * C++ 语言: 二叉查找树
 *
 * @author pan
 * @date 2020/11/17
 */

#include <iostream>
#include <fstream>
#include "RBTree.h"
using namespace std;

void preOrder(ofstream &of,RBTNode<int>* tree){
    if(tree != NULL)
    {
        //cout<< tree->key << " " ;
        of<<(tree->key)<<(tree->color==0?",红":",黑")<<endl;
        preOrder(of,tree->left);
        preOrder(of,tree->right);
    }
}
void inOrder(ofstream &of,RBTNode<int>* tree){
    if(tree != NULL)
    {
        //cout<< tree->key << " " ;
        preOrder(of,tree->left);
        of<<(tree->key)<<(tree->color==0?",红":",黑")<<endl;
        preOrder(of,tree->right);
    }
}
int main()
{
    ifstream fin("D:\\CLionProjects\\insert.txt");
    ofstream fout1("D:\\CLionProjects\\LNR.txt");
    ofstream fout2("D:\\CLionProjects\\NLR.txt");
    //int a[]= {10, 40, 30, 60, 90, 70, 20, 50, 80};
    int i;
    //int ilen = (sizeof(a)) / (sizeof(a[0])) ;
    int ilen;
    fin>>ilen;
    int a[ilen];
    for(int i=0;i<ilen;++i) fin>>a[i];
    RBTree<int>* tree=new RBTree<int>();

    cout << "== 原始数据: ";
    for(i=0; i<ilen; i++)
        cout << a[i] <<" ";
    //cout << endl;
    //插入数据
    for(i=0; i<ilen; i++) tree->insert(a[i]);

    //cout << "== 前序遍历: ";
    RBTNode<int> *p = tree->mRoot;
    preOrder(fout1,p);
    //tree->preOrder();
    //cout << "\n== 中序遍历: ";
    p = tree->mRoot;
    inOrder(fout2,p);

    cout<<endl;
    cout << "== 树的详细信息: " << endl;
    tree->print();

    // 销毁红黑树
    tree->destroy();

    return 0;
}

