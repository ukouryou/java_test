package algorithm;

public class EditDistance {
    public static int editDistance(String s, String t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int n = s.length(); // length of s
        int m = t.length(); // length of t

        int[][] d = new int[m+1][n+1];

        d[0][0] = 0;

        for (int i=1; i <=m; i++) {
            d[i][0] = i;
        }

        for (int j=1; j <=n; j++) {
            d[0][j] = j;
        }


        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                d[i][j] = min(d[i-1][j] + 1, d[i-1][j-1] + substitute(s.charAt(j-1), t.charAt(i-1)) , d[i][j-1] + 1);
            }
        }


        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                System.out.print(d[i][j] + "	");
            }
            System.out.println();
        }

        return d[m][n];
    }

    public static int substitute(int source, int target) {
        return source == target ? 0 : 2;
    }

    public static int min(int insertCost, int substituteCost, int deleteCost) {
        if (substituteCost < insertCost && substituteCost < deleteCost) {
            System.out.println("substitute");
        }

        return Math.min(Math.min(insertCost, substituteCost), deleteCost);
    }

    public static void main(String[] args) {
        System.out.println(EditDistance.editDistance("intention","execution"));

    }

}
