#include <iostream>
#include <fstream>
#include <ctime>
#include <algorithm>

using namespace std;

const int N = 100010;
//交换
void swap(int &a,int &b){
    int temp;
    temp = a;
    a = b;
    b = temp;
}
//三者中位数换入右边
int selectPivot(int q[],int l,int r){
    int mid = l+r>>1;
    if(q[r]>q[mid]) swap(q[mid],q[r]);
    if(q[l]>q[mid]) swap(q[l],q[mid]);
    if(q[r]<q[l]) swap(q[r],q[l]);
    return q[r];
}

//插入排序
void insert_sort(int q[],int l,int r){
    for(int i=l+1;i<=r;++i){
        int j=i;
        //向后移动
        while(j>l&&q[j]<q[j-1]) swap(q[j],q[j-1]),j--;
    }
}

//划分区间
int partition (int q[],int l,int r,int &countSame){
    int x = selectPivot(q,l,r);                 //选择三者中位数，并交换到位置r
    int i = l-1;                                //为了++i方面，减少边界判断
    int samePos = l;                          //记录当前相同元素位置
    for(int j=l;j<r;j++){
        if(q[j]<=x) {
            swap(q[++i],q[j]);  //把小的元素放到左边
            //只比之前多这一句,查看交换的数是否等于中枢值
            if(q[i-1] == x) swap(q[samePos++],q[i-1]);  //把相同的元素放到最左边
        }
    }
    swap(q[i+1],q[r]);                  //交换中枢值
    countSame = samePos-l;                   //与中枢值相同个数
    //把左端相同数据移到中枢旁边
    int j = i;
    while(samePos>l){
        swap(q[j--],q[--samePos]);
    }
    return i+1;                                 //返回中枢值的位置
}

//快排
void quick_sort(int q[],int l,int r){
    if(l>=r) return;
    //当元素少于17个时，使用插排
    if(r-l+1<17){
        insert_sort(q,l,r);
        return;
    }
    int countSame=0;                    //与中枢值相同的元素个数
    int pivotPos = partition(q,l,r,countSame);   //划分左右区间,且相同的元素已放在中枢值左边
    quick_sort(q,l,pivotPos-countSame-1);
    quick_sort(q,pivotPos+1,r);
}



int main(){
    int q[N];
    int q2[N];
    int n;
    ifstream in("D:\\CLionProjects\\data.txt");
    ofstream out("D:\\CLionProjects\\sorted.txt");
    in>>n;
    //n =30;
    for(int i=0;i<n;++i) {
        in>>q[i];
        q2[i] = q[i];
    }
    in.close();
    clock_t start = clock();
    quick_sort(q,0,n-1);
    clock_t end = clock();
    for(int i=1;i<=n;++i) out<<q[i]<<" ";
    cout<<"优化后的快排："<<(double)(end-start)<<"ms"<<endl;
    clock_t start2 = clock();
    sort(q2,q2+n);
    clock_t end2 = clock();
    cout<<"官方自带排序："<<(double)(end2-start2)<<"ms"<<endl;
    return 0;
}