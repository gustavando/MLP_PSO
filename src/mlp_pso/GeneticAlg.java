package mlp_pso;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GeneticAlg {
    
    private String local;
    private double taxaMut;
    private int ent;
    private int n1;
    private int n2;
    private int nAmostras;
    private int nIndividuos;
    private int nGeracoes;
    
    GeneticAlg(int n1, int n2, int ent, int nAmostras, String local, double taxaMut, int nIndividuos, int nGeracoes){
        this.taxaMut=taxaMut;
        this.ent=ent;
        this.n1=n1;
        this.n2=n2;
        this.local=local;
        this.nAmostras=nAmostras;
        this.nIndividuos=nIndividuos;
        this.nGeracoes=nGeracoes;
    }
    
    public void validacaoCruzada() {
        int[][] valEsperado = new int[nAmostras][n2];
        try {
            double[][] dados = leDados(this.local, this.nAmostras, this.ent, valEsperado);
            double desempenhoTotal = 0;

            for (int i = 0; i < 10; i++) {
                long tempoInicial = System.currentTimeMillis();
                int limInf = this.nAmostras - (i + 1) * this.nAmostras / 10;
                int limSup = limInf + this.nAmostras / 10;
                double[] w = geraIndividuos(limInf, limSup, dados, valEsperado);
                desempenhoTotal += testa(w, limInf, limSup, dados, valEsperado, tempoInicial);
            }
            desempenhoTotal /= 10;
            System.out.println("Desempenho Final: " + desempenhoTotal);
            FileWriter fw = new FileWriter("Saida2.txt",true);
            PrintWriter pw = new PrintWriter(fw);        
            pw.println("Desempenho médio: " + desempenhoTotal + " %");
            pw.println();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public double[] validacao2(int foldAtual,double[][] dados, int[][] valEsperado){
        double[] w = null;
        long tempoInicial=System.currentTimeMillis();
        int limInf = this.nAmostras - (foldAtual + 1) * this.nAmostras / 10;
        int limSup = limInf + this.nAmostras / 10;
        //int nw = this.n1 * (this.ent + 1) + (this.n1 + 1) * this.n2;  
        w = geraIndividuos(limInf,limSup,dados,valEsperado);
        double desempenhoTotal = testa(w, limInf, limSup, dados, valEsperado,tempoInicial);
        try{
            System.out.println("Desempenho Final: " + desempenhoTotal);
            FileWriter fw = new FileWriter("Saida2.txt",true);
            PrintWriter pw = new PrintWriter(fw);        
            pw.println("Desempenho médio: " + desempenhoTotal + " %");
            pw.println();
            pw.close();
            fw.close();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return w;
    }
    
    public double[] geraIndividuos(int limInf, int limSup, double[][] dados, int[][] valEsperado){
        int nW = this.n1 * (this.ent + 1) + (this.n1 + 1) * this.n2;
        double[][] w = new double[this.nIndividuos][nW];
        
        //Inicializar indivíduos        
        double[] avaliacoes=new double[this.nIndividuos];
        
        for (int i = 0; i < this.nIndividuos; i++){
            for(int j=0;j<nW;j++){
                w[i][j] = 2*Math.random()-1;
            }          
        }
        
        /*for(int i=0; i<nW;i++){
            System.out.println(w[0][i]);
        }*/
        //System.out.println();
        
        //Avaliar indivíduos  
        for(int i = 0; i < this.nGeracoes; i++){
            avaliacoes[0] = 1 - calculaErroMedio(w[0],limInf,limSup,dados,valEsperado);           
            for (int j = 1; j < this.nIndividuos; j++){
                avaliacoes[j] = 1 - calculaErroMedio(w[j],limInf,limSup,dados,valEsperado);
                avaliacoes[j] += avaliacoes[j-1];
            }
            fazCruzamento(w[roleta(avaliacoes)], w[roleta(avaliacoes)]);
            fazMutacao(w);
        }
        int melhor = avaliaMelhor(avaliacoes);
        return w[melhor];        
    }
    
    public void fazMutacao(double[][] w){
        for (int i = 0; i < w.length; i++){
            for(int j = 0; j < w[0].length; j++){
                
                if (Math.random() < this.taxaMut) {
                    w[i][j] = 2*Math.random()-1;
                }
            }
        }
    }
    
    //Aritmético
    public void fazCruzamento(double[] w1, double[] w2){
        //int inf = (int)(Math.random()*(w1.length-1));
        //int sup = (int)(Math.random()*(w1.length-inf-1)) + inf + 1; 
        double prob=0.55;
        double w1Aux;
        for (int i=0; i< w1.length; i++){
            w1Aux = w1[i];
            w1[i] = prob * w1Aux + (1 - prob) * w2[i];
            w2[i] = prob * w2[i] + (1 - prob) * w1Aux;        
        }
    }
    
    public static int roleta(double[] avaliacoes){
        int ultimaPos = avaliacoes.length;
        double valor= Math.random() * avaliacoes[ultimaPos-1];
        int min = 0;
        int max = avaliacoes.length-1;
        int pos = (max+min)/2;
        while (min < max){
            if(valor>avaliacoes[pos]){
                if(min==max-1)
                    return max;
                min = pos;
                pos = (min+max)/2;
                
            }
            else{
                if(pos==0 || avaliacoes[pos-1]<valor){
                    return pos;
                }
                else{
                    max = pos;
                    pos = (min+max)/2;
                }
            }
        }
        return pos;
    }
    
    public int avaliaMelhor(double[] avaliacoes){
        int melhor = 0;
        double anterior = avaliacoes[0];
        double atual;
        for (int i=1; i < avaliacoes.length; i++){
            atual = avaliacoes[i] - avaliacoes[i-1];
            if(atual > anterior)
                melhor = i;
            anterior = atual;
        }
        return melhor;
    }
    
    public double testa(double[] w, int limInf, int limSup, double[][] dados, int[][] valEsperado,long tempoInicial) {
        double[] Y1 = new double[this.n1];
        double[] Y2 = new double[this.n2];
        double[] I1 = new double[this.n1];
        double[] I2 = new double[this.n2];
        int ini;
        int fim;
        int interv;
        int acertos = 0;
        for (int i = limInf; i < limSup; i++) {
            ini = 0;
            fim = this.ent + 1;
            interv = this.ent + 1;
            for (int j = 0; j < this.n1; j++) {
                I1[j] = calculaU(w, ini, dados[i], fim);
                ini = ini + interv;
                fim = ini + interv;
            }
            for (int j = 0; j < this.n1; j++) {
                Y1[j] = 1 / (Math.exp(-I1[j]) + 1);
            }
            fim = this.n1 + ini + 1;
            interv = this.n1 + 1;
            for (int j = 0; j < this.n2; j++) {
                I2[j] = calculaU(w, ini, Y1, fim);
                ini = ini + interv;
                fim = ini + interv;
            }
            for (int j = 0; j < this.n2; j++) {
                Y2[j] = 1 / (Math.exp(-I2[j]) + 1);
            }
            //System.out.println("Y2[0]: "+Y2[0]+ " Y2[1]: "+Y2[1]+" Y2[2]: "+Y2[2] );
            for (int j = 0; j < this.n2; j++) {
                if (Y2[j] > 0.5) {
                    Y2[j] = 1.0;
                } else {
                    Y2[j] = 0.0;
                }
            }
            int j=0;
            while(j<this.n2 && Y2[j]==valEsperado[i][j]){
                j++;
            }
            acertos+=j/this.n2;
        }
        /*int nw = this.n1 * (this.ent + 1) + (this.n1 + 1) * this.n2;  
        for(int i=0; i<nw;i++){
            System.out.println(w[i]);
        }*/
        double desempenho = (double) acertos / (this.nAmostras / 10) * 100;
        System.out.println("Desempenho: " + desempenho);
        long tempoFinal=System.currentTimeMillis();
        tempoFinal =(tempoFinal - tempoInicial);
        try{
            escreve(desempenho,tempoFinal, w);
        }catch(IOException e){
            System.out.println("Erro ao escrever arquivo");
        }
        return desempenho;
    }
    
    public double calculaErro(int[] d, double[] Y) {
        double err = 0;
        for (int i = 0; i < this.n2; i++) {
            err = err + Math.pow(((double) d[i] - Y[i]), 2);
        }
        err /= 2;
        return err;
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
        //System.out.println("Erro Médio: " + erroMedio);
        return erroMedio;
    }
    
    public double calculaU(double[] w, int ini, double[] dados, int fim) {
        double u = 0;
        for (int j = ini; j < fim - 1; j++) {
            u += +w[j] * dados[j - ini];
        }
        u += w[fim - 1];
        return u;
    }
    
    public double[][] leDados(String nome, int nDados, int nAtrib, int[][] valEsperado) throws IOException {
        FileReader fr = new FileReader(nome);
        BufferedReader bf = new BufferedReader(fr);
        Scanner sc = new Scanner(bf);
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
        sc.close();
        bf.close();
        fr.close();
        return valores;
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
    
    public void escreve(double acertos,long tempo, double[] w) throws IOException{
        FileWriter fw = new FileWriter("Saida2.txt",true);
        PrintWriter pw = new PrintWriter(fw);        
        pw.println("Algoritmo Genético");
        pw.println("Acertos: " + acertos + " %");
        pw.println("Tempo: " + tempo + " milisegundos");
        pw.println("Neurônios camada escondida: " + this.n1);
        for(int i=0;i<w.length;i++){
            pw.print(w[i]+",");
        }
        pw.println();
        pw.close();
        fw.close();
    }
    
}
