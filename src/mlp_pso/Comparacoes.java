package mlp_pso;

import java.io.BufferedReader;
import java.util.Scanner;

public class Comparacoes {

    public double[][] calculaEsperadoIris(java.util.ArrayList<Integer> numeros, int[][] valEsperado, int nAtrib, int nDados, double[] maior, BufferedReader bf) {
        Scanner sc = new Scanner(bf);
        double[][] valores = new double[nDados][nAtrib];
        String[] valor;
        for (Integer j : numeros) {
            String linha = sc.nextLine();
            valor = (linha.split(","));
            for (int i = 0; i < nAtrib; i++) {
                valores[j][i] = Double.parseDouble(valor[i]) / maior[i];
            }

            if (valor[nAtrib].equals("Iris-setosa")) {
                valEsperado[j][0] = 1;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 0;

            }
            if (valor[nAtrib].equals("Iris-versicolor")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 1;
                valEsperado[j][2] = 0;
            }
            if (valor[nAtrib].equals("Iris-virginica")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 1;
            }
        }
        return valores;
    }

    public double[][] calculaEsperadoPoker(java.util.ArrayList<Integer> numeros, int[][] valEsperado, int nAtrib, int nDados, double[] maior, BufferedReader bf) {
        Scanner sc = new Scanner(bf);
        double[][] valores = new double[nDados][nAtrib];
        String[] valor;
        for (Integer j : numeros) {
            String linha = sc.nextLine();
            valor = (linha.split(","));
            for (int i = 0; i < nAtrib; i++) {
                valores[j][i] = Double.parseDouble(valor[i]) / maior[i];
            }
            if (valor[nAtrib].equals("0")) {
                valEsperado[j][0] = 1;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 0;
                valEsperado[j][3] = 0;
                valEsperado[j][4] = 0;
                valEsperado[j][5] = 0;
                valEsperado[j][6] = 0;
                valEsperado[j][7] = 0;
                valEsperado[j][8] = 0;
                valEsperado[j][9] = 0;
            }
            if (valor[nAtrib].equals("1")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 1;
                valEsperado[j][2] = 0;
                valEsperado[j][3] = 0;
                valEsperado[j][4] = 0;
                valEsperado[j][5] = 0;
                valEsperado[j][6] = 0;
                valEsperado[j][7] = 0;
                valEsperado[j][8] = 0;
                valEsperado[j][9] = 0;
            }
            if (valor[nAtrib].equals("2")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 1;
                valEsperado[j][3] = 0;
                valEsperado[j][4] = 0;
                valEsperado[j][5] = 0;
                valEsperado[j][6] = 0;
                valEsperado[j][7] = 0;
                valEsperado[j][8] = 0;
                valEsperado[j][9] = 0;
            }
            if (valor[nAtrib].equals("3")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 0;
                valEsperado[j][3] = 1;
                valEsperado[j][4] = 0;
                valEsperado[j][5] = 0;
                valEsperado[j][6] = 0;
                valEsperado[j][7] = 0;
                valEsperado[j][8] = 0;
                valEsperado[j][9] = 0;
            }
            if (valor[nAtrib].equals("4")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 0;
                valEsperado[j][3] = 0;
                valEsperado[j][4] = 1;
                valEsperado[j][5] = 0;
                valEsperado[j][6] = 0;
                valEsperado[j][7] = 0;
                valEsperado[j][8] = 0;
                valEsperado[j][9] = 0;
            }
            if (valor[nAtrib].equals("5")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 0;
                valEsperado[j][3] = 0;
                valEsperado[j][4] = 0;
                valEsperado[j][5] = 1;
                valEsperado[j][6] = 0;
                valEsperado[j][7] = 0;
                valEsperado[j][8] = 0;
                valEsperado[j][9] = 0;
            }
            if (valor[nAtrib].equals("6")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 0;
                valEsperado[j][3] = 0;
                valEsperado[j][4] = 0;
                valEsperado[j][5] = 0;
                valEsperado[j][6] = 1;
                valEsperado[j][7] = 0;
                valEsperado[j][8] = 0;
                valEsperado[j][9] = 0;
            }
            if (valor[nAtrib].equals("7")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 0;
                valEsperado[j][3] = 0;
                valEsperado[j][4] = 0;
                valEsperado[j][5] = 0;
                valEsperado[j][6] = 0;
                valEsperado[j][7] = 1;
                valEsperado[j][8] = 0;
                valEsperado[j][9] = 0;

            }
            if (valor[nAtrib].equals("8")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 0;
                valEsperado[j][3] = 0;
                valEsperado[j][4] = 0;
                valEsperado[j][5] = 0;
                valEsperado[j][6] = 0;
                valEsperado[j][7] = 0;
                valEsperado[j][8] = 1;
                valEsperado[j][9] = 0;
            }
            if (valor[nAtrib].equals("9")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 0;
                valEsperado[j][3] = 0;
                valEsperado[j][4] = 0;
                valEsperado[j][5] = 0;
                valEsperado[j][6] = 0;
                valEsperado[j][7] = 0;
                valEsperado[j][8] = 0;
                valEsperado[j][9] = 1;
            }
        }
        return valores;
    }

    public double[][] calculaEsperadoVinho(java.util.ArrayList<Integer> numeros, int[][] valEsperado, int nAtrib, int nDados, double[] maior, BufferedReader bf) {
        Scanner sc = new Scanner(bf);
        double[][] valores = new double[nDados][nAtrib];
        String[] valor;
        for (Integer j : numeros) {
            String linha = sc.nextLine();
            valor = (linha.split(","));
            if (valor[0].equals("1")) {
                valEsperado[j][0] = 1;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 0;
            }
            if (valor[0].equals("2")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 1;
                valEsperado[j][2] = 0;
            }
            if (valor[0].equals("3")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 0;
                valEsperado[j][2] = 1;
            }
            for (int i = 0; i < nAtrib; i++) {
                valores[j][i] = Double.parseDouble(valor[i+1]) / maior[i];
            }
        }
        return valores;
    }
    
    public double[][] calculaEsperadoBC(java.util.ArrayList<Integer> numeros, int[][] valEsperado, int nAtrib, int nDados, double[] maior, BufferedReader bf){
        Scanner sc = new Scanner(bf);
        double[][] valores = new double[nDados][nAtrib];
        String[] valor;
        int posClassifica=1;
        for (Integer j : numeros) {
            String linha = sc.nextLine();
            valor = (linha.split(","));
            if (valor[posClassifica].equals("M")) {
                valEsperado[j][0] = 1;
                valEsperado[j][1] = 0;
            }
            if (valor[posClassifica].equals("B")) {
                valEsperado[j][0] = 0;
                valEsperado[j][1] = 1;
            }
            int k=0;
            for (int i = 0; i <= nAtrib; i++) {
                if(i != posClassifica){
                    valores[j][k] = Double.parseDouble(valor[i]) / maior[k];
                    k++;
                }
            }
        }
        return valores;
    }
}
