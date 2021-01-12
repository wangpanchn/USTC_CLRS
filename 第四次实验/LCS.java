package LCS;

import java.util.Scanner;

/**
 * 最长公共子序列(LCS)
 * */
public class LCS {
   /**
    * c[i,j]存放最优解值，计算时行优先
    * b[i,j] 解矩阵，存放构造最优解信息\
    *           ↖ , 由c[i-1,j-1]确定
    * b[i,j] =  ↑ , 由c[i-1,j]确定
    *           ← , 由c[i,j-1]确定
    * 构造解时，从b[m,n]出发，到 i=0或j=0停止，遇到“↖”打印Xi(Yi)
    *
    * 递归式：
    *                0                          i=1 or j=0
    *       c[i,j] = c[i-1,j-1]+1               i,j>0 and Xi=Yi
    *                max{c[i,j-1],c[i-1,j]}     i,j>0 and Xi!=Yi
    * */
   static int len = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String X = "1010101010001";
        String Y = "101010101001";
        System.out.print("请输入字符串1：");
        X = scanner.nextLine();
        System.out.print("请输入字符串2：");
        Y = scanner.nextLine();
        int[][] c = new int[X.length()+1][Y.length()+1];            //记录最优解
        String[][] b = new String[X.length()+1][Y.length()+1];     //构造解
        LCS_Length(X,Y,c,b);
        System.out.print("最长公共子串：");
        print_LCS(b,X,X.length(),Y.length());
        System.out.println("\n长度："+len);
    }

    //构造LCS数组
    public static void LCS_Length(String X,String Y,int[][] c,String[][] b){
        int m = X.length();
        int n = Y.length();
        for(int i=1;i<=m;++i){
            for (int j = 1; j <= n; j++) {
                if(X.charAt(i-1) == Y.charAt(j-1)) {
                    c[i][j] = c[i-1][j-1] + 1;
                    b[i][j] = "↖";
                }/*else{
                    //谁大选谁
                    c[i][j] = Math.max(c[i-1][j],c[i][j-1]);
                    b[i][j] = c[i][j]==c[i-1][j]?"↑":"←";
                }*/
                else if(c[i-1][j] >= c[i][j-1]){
                    c[i][j] = c[i-1][j];
                    b[i][j] = "↑";
                }else{
                    c[i][j] = c[i][j-1];
                    b[i][j] = "←";
                }
            }
        }
    }
    //构造解
    public static void print_LCS(String[][] b,String X,int i,int j){
        if(i==0||j==0) return;
        if(b[i][j].equals("↖")){
            print_LCS(b,X,i-1,j-1);
            len ++;
            System.out.print(X.charAt(i-1)+" ");
        }else{
            if(b[i][j].equals("↑")) print_LCS(b,X,i-1,j);
            else print_LCS(b,X,i,j-1);
        }
    }
}
