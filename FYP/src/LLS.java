import java.util.*;

public class LLS {
    List<List<Float>> list = new ArrayList<>();
    static List<List<Float>> listT = new ArrayList<>();
    private static int ideOfMatrixSize = 3;
    float[] resultM = new float[3];

    public LLS() {

    }

    public float[] LLSc(float[][] A, float[] b) throws Exception {
        int numbOfRow = A.length;
        int numbOfCol = 3;
        float[][] matrix = A;
        float[] matrix2 = b;
        float[][] matrixT = new float[numbOfCol][numbOfRow];

        
        float[][] mmMatrix = matrix;

        float[][] mmMatrixT = matrixT(mmMatrix);

        float[][] mtMatrix = MultiplyMatrix(mmMatrixT, mmMatrix);

        int[][] adj = new int[matrix.length][matrix.length];
        adjoint(mtMatrix, adj);

        float[][] matrixInverse = new float[mtMatrix.length][mtMatrix.length];
        inverse(mtMatrix, matrixInverse);

        float[][] cancel0M = mmMatrixT;

        float[][] finalMatrixAs = new float[3][4];
        finalMatrixAs = MultiplyMatrix(matrixInverse, cancel0M);

        float[] resultM = new float[3];
        float[][] exMatrix = new float[3][numbOfRow];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < numbOfRow - 1; j++) {
                exMatrix[i][j] = finalMatrixAs[i][j];
            }
        }

        resultM = MultiplyMatrix(finalMatrixAs, matrix2);
        float[] resultLLS = new float[3];
        resultLLS[0] = resultM[0];
        resultLLS[1] = resultM[1];
        resultLLS[2] = resultM[2];
        return resultLLS;
    }

    public float[] getLLSresult() {
        float[] resultLLS = new float[3];
        resultLLS[0] = resultM[0];
        resultLLS[1] = resultM[1];
        resultLLS[2] = resultM[2];
        return resultLLS;
    }

    public static void display(float[][] matrix) {
        for (float[] row : matrix) {
            for (float column : row) {
                System.out.print(column + " ");
            }
            System.out.println();
        }
    }

    public static void display(int[][] matrix) {
        for (int[] row : matrix) {
            for (int column : row) {
                System.out.print(column + " ");
            }
        }
    }

    public static float[][] matrixT(float[][] matrix) {
        float[][] matrixT = new float[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrixT[j][i] = matrix[i][j];
            }
        }
        return matrixT;
    }

    public static void display2(float[][] matrix) {
        System.out.println("display2+:");
        int row = matrix.length;
        int col = matrix[0].length;
        System.out.println("row :" + row + " col :" + col + "!!!");
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public static void display2(float[] matrix) {
        System.out.println("display2:::");
        int row = matrix.length;
        int col = 1;
        System.out.println("row :" + row + " col :" + col + "!!!");
        for (int i = 0; i < row; i++) {
            System.out.print(matrix[i] + " ");
            System.out.println("");
        }
    }

    private static float[][] multiply(float[][] first, float[][] second) {
        int row = first.length;
        int row2 = second.length;
        System.out.println("row:" + row + " row2:" + row2);
        int column = first[0].length;
        int column2 = second[0].length;
        System.out.println("col:" + column + " col2:" + column2);
        int j = 0;
        int i = 0;
        int k = 0;
        float[][] sum = new float[row][row];

        for (i = 0; i < row; i++) {
            float total = 0;
            for (j = 0; j < row; j++) {
                float fn = first[i][j];
                float sn = second[j][i];
                float product = fn * sn;
                total += product;
            }
            System.out.print(total + " ");
        }

        return sum;
    }

    public float[][] MultiplyMatrix(float[][] a, float[][] b) throws Exception {

        if (a[0].length != b.length)
            throw new Exception("Matrices incompatible for multiplication");
        float matrix[][] = new float[ideOfMatrixSize][b[0].length];

        for (int i = 0; i < ideOfMatrixSize; i++)
            for (int j = 0; j < ideOfMatrixSize; j++)
                matrix[i][j] = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = calculateRowColumnProduct(a, i, b, j);
            }
        }
        return matrix;
    }

    public float[] MultiplyMatrix(float[][] a, float[] b) throws Exception {
        if (a[0].length != b.length)
            throw new Exception("Matrices incompatible for multiplication");
        float matrix[] = new float[ideOfMatrixSize];

        for (int i = 0; i < ideOfMatrixSize; i++)
            for (int j = 0; j < 1; j++)
                matrix[i] = 0;

        for (int i = 0; i < matrix.length; i++) {
            {
                matrix[i] = calculateRowColumnProduct(a, i, b, 1);
            }
        }
        return matrix;
    }

    public float calculateRowColumnProduct(float[][] A, int row, float[][] B, int col) {
        float product = 0;
        for (int i = 0; i < A[row].length; i++)
            product += A[row][i] * B[i][col];
        return product;
    }

    public float calculateRowColumnProduct(float[][] A, int row, float[] B, int col) {
        float product = 0;
        for (int i = 0; i < A[row].length; i++)
            product += A[row][i] * B[i];
        return product;
    }

    private static float[][] checkM(float[][] matrix) {
        display2(matrix);

        int row = matrix.length;
        int col = matrix[0].length;
        System.out.println("row :" + row + " col :" + col + "%%%");

        if (row != col) {
            float[][] nMatrix = new float[row][row];

            if (row > 3) {

                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < 3; j++) {
                        nMatrix[i][j] = matrix[i][j];
                    }
                    for (int j = 0; j != row; j++) {
                        nMatrix[j][3] = 0;
                    }

                }
                display2(nMatrix);
            }
            return nMatrix;

        }
        return matrix;
    }

    static void getCofactor(float[][] matrix, float temp[][], int p, int q, int n) {
        int i = 0, j = 0;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row != p && col != q) {
                    temp[i][j++] = matrix[row][col];

                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    static double determinant(float A[][], int n) {
        double D = 0;

        if (n == 1)
            return A[0][0];

        float[][] temp = new float[A.length][A.length];

        int sign = 1;

        for (int f = 0; f < n; f++) {
            getCofactor(A, temp, 0, f, n);
            D += sign * A[0][f] * determinant(temp, n - 1);
            sign = -sign;
        }

        return D;
    }

    static void adjoint(float[][] matrix, int[][] adj) {
        if (matrix.length == 1) {
            adj[0][0] = 1;
            return;
        }

        int sign = 1;
        float[][] temp = new float[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                getCofactor(matrix, temp, i, j, matrix.length);
                sign = ((i + j) % 2 == 0) ? 1 : -1;
                adj[j][i] = (int) ((sign) * (determinant(temp, matrix.length - 1)));
            }
        }
    }

    static boolean inverse(float[][] A, float[][] inverse) {
        double det = determinant(A, A.length);
        if (det == 0) {
            System.out.print("Singular matrix, can't find its inverse");
            return false;
        }

        int[][] adj = new int[A.length][A.length];
        adjoint(A, adj);

        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A.length; j++)
                inverse[i][j] = adj[i][j] / (float) det;

        return true;
    }

    static float[][] cancel0(float[][] matrix, int numb) {
        float[][] nMatrix = new float[matrix.length - 1][numb];
        {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < numb; j++) {
                    nMatrix[i][j] = matrix[i][j];
                }

            }
        }

        return nMatrix;
    }

}
