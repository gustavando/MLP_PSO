/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mlp_pso;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class PSO {

    private int n1;
    private int n2;
    private int ent;
    private int nAmostras;
    private char tipo;
    private String local;
    private double[][] w;
    private int epoca;

    PSO(int n1, int n2, int ent, int nAmostras,char tipo,String local) {
        this.n1 = n1;
        this.n2 = n2;
        this.ent = ent;
        this.nAmostras = nAmostras;
        this.tipo=tipo;
        this.local=local;
        this.w = new double[100][n1 * (ent + 1) + (n1 + 1) * n2 ];
    }
    
    PSO(int n1, int n2, int ent, int nAmostras,char tipo,String local,double[] w) {
        this.n1 = n1;
        this.n2 = n2;
        this.ent = ent;
        this.nAmostras = nAmostras;
        this.tipo=tipo;
        this.local=local;
        this.w = new double[100][n1 * (ent + 1) + (n1 + 1) * n2 ];
        for(int i=0;i<100;i++){
            this.w[i] = w;
        }
    }

    public double[] validacaoCruzada() {
        int[][] valEsperado = new int[nAmostras][n2];
        int nw = this.n1 * (this.ent + 1) + (this.n1 + 1) * this.n2;
        try {
            double[][] dados = leDados(local, nAmostras, ent, valEsperado);
            //System.out.println(valEsperado[0][0]+" "+valEsperado[0][1]+" "+valEsperado[0][2]);
            double desempenhoTotal = 0;

            /*
             * for (int i = 0; i < 150; i++) { for (int j = 0; j < 3; j++) {
             * System.out.print(valEsperado[i][j] + " "); }
             * System.out.println(); }
             */
            double[] g=null;
            for (int i = 0; i < 10; i++) {
                long tempoInicial=System.currentTimeMillis();
                int limInf = this.nAmostras - (i + 1) * this.nAmostras / 10;
                int limSup = limInf + this.nAmostras / 10;
                double[] wIni= new double[nw];
                double[] vIni= new double[nw];
                if(tipo=='s'){
                    wIni=sorteiaPesos(nw);
                    vIni=sorteiaPesos(nw);
                }
                g = treina(limInf, limSup, dados, valEsperado,wIni,vIni);           
                desempenhoTotal += testa(g, limInf, limSup, dados, valEsperado,tempoInicial);
            }
            desempenhoTotal /= 10;
            System.out.println("Desempenho Final: " + desempenhoTotal);
            FileWriter fw = new FileWriter("Saida2.txt",true);
            PrintWriter pw = new PrintWriter(fw);        
            pw.println("Desempenho médio: " + desempenhoTotal + " %");
            pw.println("Tipo: "+this.tipo);
            pw.println();
            pw.close();
            fw.close();
            return g;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
    public double[] validacao2(int foldAtual,double[][] dados, int[][] valEsperado){
        double[] g=null;
        long tempoInicial=System.currentTimeMillis();
        int limInf = this.nAmostras - (foldAtual + 1) * this.nAmostras / 10;
        int limSup = limInf + this.nAmostras / 10;
        int nw = this.n1 * (this.ent + 1) + (this.n1 + 1) * this.n2;
        double[] wIni= new double[nw];
        double[] vIni= new double[nw];
        g = treina(limInf, limSup, dados, valEsperado,wIni,vIni);           
        double desempenhoTotal = testa(g, limInf, limSup, dados, valEsperado,tempoInicial);
        try{
            System.out.println("Desempenho Final: " + desempenhoTotal);
            FileWriter fw = new FileWriter("Saida2.txt",true);
            PrintWriter pw = new PrintWriter(fw);        
            pw.println("Desempenho médio: " + desempenhoTotal + " %");
            pw.println("Tipo: "+this.tipo);
            pw.println();
            pw.close();
            fw.close();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return g;
    }

    public double[] treina(int limInf, int limSup, double[][] dados, int[][] valEsperado,double[] wIni,double[] vIni) {
        int nw = this.n1 * (this.ent + 1) + (this.n1 + 1) * this.n2;
        double[][] v = new double[100][nw];
        for (int i = 0; i < 100; i++) {
            /*if(this.tipo=='r'){           
                this.w[i] = sorteiaPesos(nw);
                v[i] = sorteiaPesos(nw);
            }
            else{
                System.arraycopy(wIni, 0, this.w[i], 0, nw);
                System.arraycopy(vIni, 0, v[i], 0, nw);
            }*/
            switch(this.tipo){
                case 'r':
                    this.w[i] = sorteiaPesos(nw);
                    v[i] = sorteiaPesos(nw);
                    break;
                case 's':
                    //copia(w[i],wIni,nw);
                    System.arraycopy(wIni, 0, this.w[i], 0, nw);
                    System.arraycopy(vIni, 0, v[i], 0, nw);
                    break;
                default:
                    v[i] = sorteiaPesos(nw);
                    break;
            }
        }
        double[][] p = new double[100][nw];
        for(int i=0;i<100;i++)
            System.arraycopy(this.w[i], 0, p[i], 0, nw);
        double[] g = new double[nw];
        System.arraycopy(p[99], 0, g, 0, nw);
        double erroMedioW = 1;
        double erroMedioAnt=0;
        double[] gAnt = new double[nw];
        int k = 0,cont=0,maximo=0;
        if(this.local.equals("poker-hand-training-true.data"))
            maximo=150;    
        else
            maximo=500;
        while (cont<8 && k<maximo) {
            erroMedioAnt = erroMedioW;
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < nw; j++) {
                    double ag = Math.random();
                    double ap = Math.random();
                    v[i][j] = 0.1 * v[i][j] + 0.1 * ap * (p[i][j] - this.w[i][j]) + 3 * ag * (g[j] - this.w[i][j]);
                }
                for (int j = 0; j < nw; j++) {
                    this.w[i][j] += v[i][j];
                }
                erroMedioW = calculaErroMedio(this.w[i], limInf, limSup, dados, valEsperado);
                if (erroMedioW < calculaErroMedio(p[i], limInf, limSup, dados, valEsperado)) {
                    System.arraycopy(this.w[i], 0, p[i], 0, nw);
                    if (erroMedioW < calculaErroMedio(g, limInf, limSup, dados, valEsperado)) {
                        System.arraycopy(p[i], 0, g, 0, nw);
                    }
                }
            }
            if(gAnt[0]==g[0])
                cont++;
            else
                cont=0;
            k++;
            System.arraycopy(g, 0, gAnt, 0, nw);
        }
        this.epoca=k;
        System.out.println(k);
        return g;
    }

    public double testa(double[] g, int limInf, int limSup, double[][] dados, int[][] valEsperado,long tempoInicial) {
        double[] Y1 = new double[this.n1];
        double[] Y2 = new double[this.n2];
        double[] I1 = new double[this.n1];
        double[] I2 = new double[this.n2];
        int nTreina = this.nAmostras - this.nAmostras / 10;
        double[] erro = new double[nTreina];
        int ini;
        int fim;
        int interv;
        int acertos = 0;
        for (int i = limInf; i < limSup; i++) {
            ini = 0;
            fim = this.ent + 1;
            interv = this.ent + 1;
            for (int j = 0; j < this.n1; j++) {
                I1[j] = calculaU(g, ini, dados[i], fim);
                ini = ini + interv;
                fim = ini + interv;
            }
            for (int j = 0; j < this.n1; j++) {
                Y1[j] = 1 / (Math.exp(-I1[j]) + 1);
            }
            fim = this.n1 + ini + 1;
            interv = this.n1 + 1;
            for (int j = 0; j < this.n2; j++) {
                I2[j] = calculaU(g, ini, Y1, fim);
                ini = ini + interv;
                fim = ini + interv;
            }
            for (int j = 0; j < this.n2; j++) {
                Y2[j] = 1 / (Math.exp(-I2[j]) + 1);
            }
            for (int j = 0; j < this.n2; j++) {
                if (Y2[j] > 0.5) {
                    Y2[j] = 1.0;
                } else {
                    Y2[j] = 0.0;
                }
            }
            int j=0;
            /* Se um dos dígitos for diferente do valor esperado, o programa pára imediatamente. 
             * acertos só será incrementado se todas as posições de Y2 forem comparadas e se as mesmas estiverem corretas */
            while(j<this.n2 && Y2[j]==valEsperado[i][j]){
                j++;
            }
            acertos+=j/this.n2;
            //System.out.println(Y2[0] + " " + Y2[1] + " " + Y2[2]);
            /*if (Y2[0] == valEsperado[i][0] && Y2[1] == valEsperado[i][1] && Y2[2] == valEsperado[i][2]
                    && Y2[3] == valEsperado[i][3] && Y2[4] == valEsperado[i][4] && Y2[5] == valEsperado[i][5]
                    && Y2[6] == valEsperado[i][6] && Y2[7] == valEsperado[i][7] && Y2[8] == valEsperado[i][8] && Y2[9] == valEsperado[i][9]) {
                //System.out.println("certo");
                acertos++;
            } else {
                //System.out.println("errado");
            }*/
        }
        double desempenho = (double) acertos / (this.nAmostras / 10) * 100;
        System.out.println("Desempenho: " + desempenho);
        long tempoFinal=System.currentTimeMillis();
        tempoFinal =(tempoFinal - tempoInicial);
        try{
            escreve(desempenho,this.epoca,tempoFinal,g);
        }catch(IOException e){
            System.out.println("Erro ao escrever arquivo");
        }
        return desempenho;
    }

    public double calculaU(double[] w, int ini, double[] dados, int fim) {
        double u = 0;
        //System.out.println(fim);
        for (int j = ini; j < fim - 1; j++) {
            u += +w[j] * dados[j - ini];
        }
        u += w[fim - 1];
        return u;
    }

    public double calculaErroMedio(double[] wV, int limInf, int limSup, double[][] dados, int[][] valEsperado) {
        double[] Y1 = new double[this.n1];
        double[] Y2 = new double[this.n2];
        double[] I1 = new double[this.n1];
        double[] I2 = new double[this.n2];
        int nTreina = this.nAmostras - this.nAmostras / 10;
        double[] erro = new double[nTreina];
        double erroTotal = 0;
        int ini;
        int fim;
        int interv;
        for (int i = 0; i < limInf; i++) {
            ini = 0;
            fim = this.ent + 1;
            interv = this.ent + 1;
            for (int j = 0; j < this.n1; j++) {
                I1[j] = calculaU(wV, ini, dados[i], fim);
                ini = ini + interv;
                fim = ini + interv;
            }
            //System.out.println("hbkhbhbkh "+fim);
            for (int j = 0; j < this.n1; j++) {
                Y1[j] = 1 / (Math.exp(-I1[j]) + 1);
            }
            fim = this.n1 + ini + 1;
            interv = this.n1 + 1;
            for (int j = 0; j < this.n2; j++) {
                //System.out.println("Fim: "+fim);
                I2[j] = calculaU(wV, ini, Y1, fim);
                ini = ini + interv;
                fim = ini + interv;
            }
            //System.out.println("hbkhbhbkh "+fim);
            for (int j = 0; j < this.n2; j++) {
                Y2[j] = 1 / (Math.exp(-I2[j]) + 1);
            }
            erro[i] = calculaErro(valEsperado[i], Y2);
            erroTotal += erro[i];
        }
        for (int i = limSup; i < this.nAmostras; i++) {
            ini = 0;
            fim = this.ent + 1;
            interv = this.ent + 1;
            for (int j = 0; j < this.n1; j++) {
                I1[j] = calculaU(wV, ini, dados[i], fim);
                ini = ini + interv;
                fim = ini + interv;
            }
            for (int j = 0; j < this.n1; j++) {
                Y1[j] = 1 / (Math.exp(-I1[j]) + 1);
            }
            fim = this.n1 + ini + 1;
            interv = this.n1 + 1;
            for (int j = 0; j < this.n2; j++) {
                I2[j] = calculaU(wV, ini, Y1, fim);
                ini = ini + interv;
                fim = ini + interv;
            }
            for (int j = 0; j < this.n2; j++) {
                Y2[j] = 1 / (Math.exp(-I2[j]) + 1);
            }
            erro[i - (limSup - limInf)] = calculaErro(valEsperado[i], Y2);
            erroTotal += erro[i - (limSup - limInf)];
        }
        double erroMedio = erroTotal / nTreina;
        //System.out.println(erroMedio);
        //imprimePesos(w1, n1, ent);
        return erroMedio;
    }

    public double calculaErro(int[] d, double[] Y) {
        double err = 0;
        for (int i = 0; i < this.n2; i++) {
            err = err + Math.pow(((double) d[i] - Y[i]), 2);
        }
        err /= 2;
        return err;
    }

    public double[][] leDados(String nome, int nDados, int nAtrib, int[][] valEsperado) throws IOException {
        FileReader fr = new FileReader(nome);
        BufferedReader bf = new BufferedReader(fr);
        //Scanner sc = new Scanner(bf);
        //double[][] valores = new double[nDados][nAtrib];
        //String[] valor;
        ArrayList<Integer> numeros = new ArrayList<Integer>();
        for (int i = 0; i < nDados; i++) {
            numeros.add(i);
        }
        Collections.shuffle(numeros);
        double[][] valores;
        Comparacoes comparador=new Comparacoes();
        if(this.local.equals("iris.data")){
            double[] maior = normaliza(this.local,5);
            valores=comparador.calculaEsperadoIris(numeros, valEsperado, nAtrib, nDados, maior, bf);
        }
        else if(this.local.equals("poker-hand-training-true.data")){
            double[] maior = normaliza(this.local,1);
            valores=comparador.calculaEsperadoPoker(numeros, valEsperado, nAtrib, nDados, maior, bf);
        }
        else if(this.local.equals("wdbc.data")){
            double[] maior = normaliza(this.local,2);
            valores=comparador.calculaEsperadoBC(numeros, valEsperado, nAtrib, nDados, maior, bf);
        }
        else{    
            double[] maior = normaliza(this.local,1);
            valores=comparador.calculaEsperadoVinho(numeros, valEsperado, nAtrib, nDados, maior, bf);
        }
       
        //System.out.println(valEsperado[0][0] + " " + valEsperado[0][1] + " " + valEsperado[0][2]);
        bf.close();
        fr.close();
        return valores;
    }

    public double[] copia(double[] w, double[] wA, int n) {
        for (int i = 0; i < n; i++) {
            w[i] = wA[i];
        }
        return w;
    }

    public double[] sorteiaPesos(int n) {
        double[] sorteio = new double[n];
        for (int i = 0; i < n; i++) {
            sorteio[i] = Math.random();
        }
        return sorteio;
    }
    
    public double[] normaliza(String nome,int incremento) throws IOException{
        FileReader fr = new FileReader(nome);
        BufferedReader bf = new BufferedReader(fr);
        Scanner sc = new Scanner(bf);
        String[] valor;
        double[] maior = new double[this.ent];
        for (int j = 0; j < this.nAmostras; j++) {
            int k = 0;
            String linha = sc.nextLine();
            valor = (linha.split(","));
            for (int i = 0; i <= this.ent; i++) {
                if (i!=incremento-1) {
                    if(Double.parseDouble(valor[i])>maior[k]) maior[k] = Double.parseDouble(valor[i]);       
                    k++;
                }
            }
        }
        sc.close();
        bf.close();
        fr.close();
        return maior;
    }
    
    public void escreve(double acertos, int epocas,long tempo,double[] g) throws IOException{
        FileWriter fw = new FileWriter("Saida2.txt",true);
        PrintWriter pw = new PrintWriter(fw);       
        pw.println("PSO");
        pw.println("Acertos: " + acertos + " %");
        pw.println("Épocas: " + epocas);
        pw.println("Tempo: " + tempo + " milisegundos");
        pw.println("Tipo :" + this.tipo);
        pw.println("Neurônios camada escondida: " + this.n1);
        for(int i=0;i<g.length;i++){
            pw.print(g[i]+",");
        }
        pw.println();
        pw.close();
        fw.close();
    }
}