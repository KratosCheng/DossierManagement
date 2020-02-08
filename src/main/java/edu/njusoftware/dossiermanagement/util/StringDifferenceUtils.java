package edu.njusoftware.dossiermanagement.util;

public class StringDifferenceUtils {

    /**
     * 得到两个字符串最小编辑距离的动态规划二维数组
     * @param source
     * @param target
     * @return
     */
    private static int[][] getLevenshteinDistance(String source, String target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        int[][] distance = new int[source.length()+1][target.length()+1];
        for(int i=0; i<source.length()+1; ++i){
            distance[i][0] = i;
        }

        for(int j=1; j<target.length()+1; ++j){
            distance[0][j] = j;
        }

        int cost = 0;
        for(int i=1; i<source.length()+1; ++i){
            for(int j=1; j<target.length()+1; ++j){
                int tempCost = Math.min(distance[i-1][j]+1, distance[i][j-1]+1);
                if(source.charAt(i-1) == target.charAt(j-1)){
                    cost = 0;
                } else {
                    cost = 1;
                }
                distance[i][j] = Math.min(distance[i-1][j-1]+cost, tempCost);
            }
        }
        return distance;
    }

    /**
     * 将两个字符串不同部分设为空格
     * @param source
     * @param target
     * @return
     */
    public static String[] getTempString(String source, String target){
        int[][] distance = getLevenshteinDistance(source, target);
        int i = distance.length-1, j = distance[0].length-1;
        int minDistance = distance[i][j];
        String sTemp = source;
        String tTemp = target;
        while(i > 0 && j > 0) {
            if(distance[i][j-1] + 1 == minDistance){
                // 此时为s字符串i位置增加字符，t字符串j位置删除字符
                tTemp = tTemp.substring(0, j-1) + " " + tTemp.substring(j);
                minDistance = distance[i][j-1];
                j -= 1;
            } else if (distance[i-1][j] + 1 == minDistance) {
                // 此时为t字符串i位置增加字符，s字符串j位置删除字符
                sTemp=sTemp.substring(0, i-1) + " " + sTemp.substring(i);
                minDistance = distance[i-1][j];
                i -= 1;
            } else if (distance[i-1][j-1] + 1 == minDistance) {
                // 此时为s字符串和t字符串在i，j位置替换字符
                sTemp=sTemp.substring(0, i-1) + " " + sTemp.substring(i);
                tTemp=tTemp.substring(0, j-1) + " " + tTemp.substring(j);
                minDistance = distance[i-1][j-1];
                i -= 1;
                j -= 1;
            } else {
                //此时s字符串t字符串在i，j位置字符相等
                i -= 1;
                j -= 1;
            }
        }

        while(i > 0){
            sTemp=sTemp.substring(0, i-1) + " " + sTemp.substring(i);
            minDistance = distance[i-1][j];
            i -= 1;
        }

        while(j > 0){
            tTemp=tTemp.substring(0, j-1) + " " + tTemp.substring(j);
            minDistance = distance[i][j-1];
            j -= 1;
        }
        return new String[]{sTemp, tTemp};
    }

    /*
    * 比较源字符串和目标字符串，目标字符串字符为空格时，源字符串对应字符高亮显示
    */
    private static String getHighLight(String source,String temp){
        StringBuffer sb=new StringBuffer();
        char[] sourceChars = source.toCharArray();
        char[] tempChars = temp.toCharArray();
        boolean flag = false;
        for(int i=0; i<sourceChars.length; i++){
            if(tempChars[i] == ' '){
                if(i==0) sb.append("<span style='color:blue'>").append(sourceChars[i]);
                else if(flag) {
                    sb.append(sourceChars[i]);
                }
                else {
                    sb.append("<span style='color:blue'>").append(sourceChars[i]);
                }
                flag = true;
                if(i==sourceChars.length-1) {
                    sb.append("</span>");
                }
            }
            else if(flag){
                sb.append("</span>").append(sourceChars[i]);
                flag = false;
            }else sb.append(sourceChars[i]);
        }
        return sb.toString();
    }

    /*
     * 得到高亮显示的字符串
     */
    public static String[] getHighLightDifferent(String a,String b){
        String[] temp = getTempString(a, b);
        String[] result = {getHighLight(a, temp[0]), getHighLight(b, temp[1])};
        return result;
    }
}
