package mlp_pso;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class BackPropagation {
    private int n1;
    private int n2;
    private int ent;
    private double tA;
    private double mom;
    private int nAmostras;
    private char tipo; 
    private String local;
    private double[][] w1, w2;
    private double[] w;
    private double erroConv;
    
    BackPropagation(int n1, int n2, int ent, double tA, double mom, int nAmostras, char tipo, String local, double erroConv) {
        this.n1 = n1;
        this.n2 = n2;
        //this.n3 = n3;
        this.ent = ent;
        this.tA = tA;
        this.mom = mom;
        this.nAmostras = nAmostras;
        this.tipo = tipo;
        this.local = local;
        this.erroConv=erroConv;
    }
    BackPropagation(int n1, int n2, int ent, double tA, double mom, int nAmostras, char tipo, String local,double[] w, double erroConv) {
        this.n1 = n1;
        this.n2 = n2;
        //this.n3 = n3;
        this.ent = ent;
        this.tA = tA;
        this.mom = mom;
        this.nAmostras = nAmostras;
        this.tipo = tipo;
        this.local = local;
        this.w = w ;
        this.erroConv=erroConv;
    }
    
    public void validacaoCruzada() {
        int[][] valEsperado = new int[nAmostras][n2];
        try {
            long tempoInicial=System.currentTimeMillis();
            double[][] dados = leDados(local, nAmostras, ent, valEsperado);
            double desempenhoTotal = 0;
            
             /* for (int i = 0; i < 150; i++) { for (int j = 0; j < 2; j++) {
              System.out.print(valEsperado[i][j] + " "); }
              System.out.println(); }*/
                       
            int limInf, limSup;
            double[][] w1Ini = sorteiaPesos(this.n1, this.ent + 1);
            double[][] w2Ini = sorteiaPesos(this.n2, this.n1 + 1);
            if(this.tipo=='d'){
                for(int i=0;i<this.n1;i++){
                    for(int j=0;j<=this.ent;j++){
                        w1Ini[i][j]=this.w[j+i*(this.ent+1)];
                    }   
                }           
                for(int i=0;i<this.n2;i++){
                    for(int j=0;j<=this.n1;j++){
                        w2Ini[i][j]=w[j+i*(this.n1+1)];
                    }
                }
            }
            else{
                w1Ini = sorteiaPesos(this.n1, this.ent + 1);
                w2Ini = sorteiaPesos(this.n2, this.n1 + 1);
            }
            //double[][] w3Ini = sorteiaPesos(n3, n2 + 1);
            this.w1 = sorteiaPesos(this.n1, this.ent + 1);
            this.w2 = sorteiaPesos(this.n2, this.n1 + 1);
            //w3 = sorteiaPesos(n3, n2 + 1);
            for (int i = 0; i < 10; i++) {
                if (this.tipo == 'r') {
                    this.w1 = sorteiaPesos(this.n1, this.ent + 1);                   
                    this.w2 = sorteiaPesos(this.n2, this.n1 + 1);
                   // w3 = sorteiaPesos(n3, n2 + 1);
                } else {
                    this.w1 = copia(this.w1, w1Ini);
                    this.w2 = copia(this.w2, w2Ini);
                }
                //imprimePesos(this.w1,this.w1.length,this.w1[0].length);
                //imprimePesos(w1Ini,this.w1.length,this.w1[0].length);
                limInf = this.nAmostras - (i + 1) * this.nAmostras / 10;
                limSup = limInf + this.nAmostras / 10;
                int epocas=treina(limInf, limSup, dados, valEsperado);
                desempenhoTotal += testa(limInf, limSup, dados, valEsperado, tempoInicial,epocas);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void validacao2(int foldAtual,double[][] dados, int[][] valEsperado,double[] w){
        long tempoInicial=System.currentTimeMillis();
        int limInf = this.nAmostras - (foldAtual + 1) * this.nAmostras / 10;
        int limSup = limInf + this.nAmostras / 10;
        this.w1 = new double[this.n1][this.ent + 1];
        this.w2 = new double[this.n2][this.n1 + 1];
        for(int i=0;i<this.n1;i++){
            for(int j=0;j<=this.ent;j++){
                this.w1[i][j]=w[j+i*(this.ent+1)];
            }   
        }          
        int espaco = this.ent+(this.n1-1)*(this.ent+1)+1;
        for(int i=0;i<this.n2;i++){
            for(int j=0;j<=this.n1;j++){                
                this.w2[i][j] = w[j+i*(this.n1+1)+espaco];
            }
        }
        imprimePesos(this.w1);
        int epocas = treina(limInf, limSup, dados, valEsperado);
        double desempenhoTotal = testa(limInf, limSup, dados, valEsperado, tempoInicial,epocas);
        System.out.println("Desempenho Final: " + desempenhoTotal);
        try{
            FileWriter fw = new FileWriter("Saida2.txt",true);
            PrintWriter pw = new PrintWriter(fw);        
            pw.println("Desempenho médio: " + desempenhoTotal + " %");
            pw.println("Tipo: "+this.tipo);
            pw.println();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int treina(int limInf, int limSup, double[][] dados, int[][] valEsperado) {
        double errAtual = 0;
        double errAnt = 1;
        int epoca = 0;
        while (epoca < 5000 && Math.abs(errAtual - errAnt) > erroConv) {
            errAnt = errAtual;
            errAtual = calculaErroMedio(limInf, limSup, dados, valEsperado);
            epoca++;
            //System.out.println(limInf);
            System.out.println(errAtual);
            if(errAtual>errAnt) System.out.println("Não");
        }
        System.out.println("Épocas: "+epoca);
        return epoca;
    }
    
    public double testa(int limInf, int limSup, double[][] dados, int[][] valEsperado, long tempoInicial,int epocas) {
        double[] Y1 = new double[this.n1];
        double[] Y2 = new double[this.n2];
        //double[] Y3 = new double[n3];
        double[] I1 = new double[this.n1];
        double[] I2 = new double[this.n2];
       // double[] I3 = new double[n3];
        int acertos = 0;
        for (int i = limInf; i < limSup; i++) {
            for (int j = 0; j < this.n1; j++) {
                I1[j] = calculaU(this.w1, dados[i], j);                
            }
            for (int j = 0; j < this.n1; j++) {
                Y1[j] = 1 / (Math.exp(-I1[j]) + 1);
            }
            for (int j = 0; j < this.n2; j++) {
                I2[j] = calculaU(this.w2, Y1, j);
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
            /*if (Y2[0] == valEsperado[i][0] && Y2[1] == valEsperado[i][1] && Y2[2] == valEsperado[i][2]){
                   // && Y2[3] == valEsperado[i][3] && Y2[4] == valEsperado[i][4] && Y2[5] == valEsperado[i][5]
                   // && Y2[6] == valEsperado[i][6] && Y2[7] == valEsperado[i][7] && Y2[8] == valEsperado[i][8] && Y2[9] == valEsperado[i][9]) {
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
            escreve(desempenho,epocas,tempoFinal);
        }catch(IOException e){
            System.out.println("Erro ao escrever arquivo");
        }
        return desempenho;
        //imprimePesos(w1,n1,ent);
    }
    
    public double[][] ajustaPeso(double[][] w, double[] del, double[][] wAnt, double[] d) {
        int nL = w.length;
        int nC = w[0].length-1;
        for (int i = 0; i < nL; i++) {
            for (int j = 0; j < nC; j++) {
                w[i][j] = w[i][j] + this.tA * del[i] * d[j] + this.mom * (w[i][j] - wAnt[i][j]);
            }
            w[i][nC] += this.tA * del[i] + mom * (w[i][nC] - wAnt[i][nC]);
        }
        return w;
    }
    
    public double calculaU(double[][] w, double[] dados, int k) {
        double u = 0;
        int nC = w[0].length-1;
        for (int j = 0; j < nC; j++) {
            u = u + w[k][j] * dados[j];
        }
        u = u + w[k][nC];
        return u;
    }
    
    public double[] calculaDelta(double[] del, double[] Y, double[][] w) {
        int nL = w.length;
        int nC = w[0].length-1;
        double[] delta = new double[nC];
        double valor;
        for (int i = 0; i < nC; i++) {
            valor = 0;
            for (int j = 0; j< nL; j++) {
                valor += del[j] * w[j][i];
            }
            delta[i] = valor * Y[i] * (1 - Y[i]);   
        }
        //for(int i=0;i<nL;i++)
        //    delta[i] = valor * Math.exp(I[i]) / Math.pow((Math.exp(I[i]) + 1), 2);        
        return delta;
    }
    
    public double calculaErro(int[] d, double[] Y) {
        double err = 0;
        for (int i = 0; i < this.n2; i++) {
            ///System.out.println("Y2["+i+"]: "+Y[i]+"    "+"d["+i+"]: "+d[i]);
            err = err + Math.pow(((double) d[i] - Y[i]), 2);
            //System.out.println("Erro: "+err);
        }
        return err;
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
    
    public double calculaErroMedio(int limInf, int limSup, double[][] dados, int[][] valEsperado) {
        double[] Y1 = new double[this.n1];
        double[] Y2 = new double[this.n2];
        //double[] Y3 = new double[n3];
        double[] I1 = new double[this.n1];
        double[] I2 = new double[this.n2];
        //double[] I3 = new double[n3];
        int nTreina = this.nAmostras - this.nAmostras / 10;
        double[] erro = new double[nTreina];
        double[] del1;
        double[] del2 = new double[this.n2];
        //double[] del3 = new double[n3];
        double[][] w1Aux = new double[this.n1][this.ent + 1];
        double[][] w2Aux = new double[this.n2][this.n1 + 1];
        //double[][] w3Aux = new double[n3][n2 + 1];
        double[][] w1Ant = new double[this.n1][this.ent + 1];
        double[][] w2Ant = new double[this.n2][this.n1 + 1];
        //double[][] w3Ant = new double[n3][n2 + 1];
        double erroTotal = 0;
        //imprimePesos(this.w1,w1.length,w1[0].length);
        for (int i = 0; i < limInf; i++) {
            w1Ant = copia(w1Ant, w1Aux);
            w2Ant = copia(w2Ant, w2Aux);
            //w3Ant=copia(w3Ant, w3Aux, n3, n2 + 1);
            w1Aux = copia(w1Aux, this.w1);
            w2Aux = copia(w2Aux, this.w2);
            //w3Aux = copia(w3Aux, w3, n3, n2 + 1);
            //imprimePesos(this.w1,this.w1.length,this.w1[0].length);
            //System.out.println("w1:");
            //imprimePesos(this.w1,this.w1.length,this.w1[0].length);
            for (int j = 0; j < this.n1; j++) {
                I1[j] = calculaU(this.w1,dados[i], j);                
            }
            //System.out.println("I1: "+I1[0]);
            for (int j = 0; j < this.n1; j++) {
                Y1[j] = 1 / (Math.exp(-I1[j]) + 1);
            }
            //System.out.println("Y1: "+Y1[0]);
            //System.out.println("w2:");
            //imprimePesos(this.w2,this.w2.length,this.w2[0].length);
            for (int j = 0; j < this.n2; j++) {
                I2[j] = calculaU(this.w2, Y1, j);
            }
            //System.out.println("I2: "+I2[0]);
            for (int j = 0; j < this.n2; j++) {
                //System.out.println("M: "+Math.exp(-I2[j]));
                Y2[j] = 1 / (Math.exp(-I2[j]) + 1);
                //System.out.println("Y2["+j+"]: " + Y2[j]);
            }           
            //System.out.println("Y2: "+Y2[0]);
            for (int j = 0; j < this.n2; j++) { 
                del2[j] = ((double) valEsperado[i][j] - Y2[j]) * Y2[j] * (1 - Y2[j]);
            }
            this.w2 = ajustaPeso(this.w2, del2, w2Ant, Y1);
            del1 = calculaDelta(del2, Y1, this.w2);
            //System.out.println("del1: "+del1[0]);
            this.w1 = ajustaPeso(this.w1, del1, w1Ant, dados[i]);
            erro[i] = calculaErro(valEsperado[i], Y2);
            erroTotal += erro[i];
        }
        for (int i = limSup; i < this.nAmostras; i++) {
            //imprimePesos(w1Aux,n1,ent);
            w1Ant = copia(w1Ant, w1Aux);
            w2Ant = copia(w2Ant, w2Aux);
            //w3Ant = copia(w3Ant, w3Aux, n3, n2 + 1);
            w1Aux = copia(w1Aux, this.w1);
            w2Aux = copia(w2Aux, this.w2);
            //w3Aux = copia(w3Aux, w3, n3, n2 + 1);
            for (int j = 0; j < this.n1; j++) {
                I1[j] = calculaU(this.w1, dados[i], j);                
            }
            for (int j = 0; j < this.n1; j++) {
                Y1[j] = 1 / (Math.exp(-I1[j]) + 1);
            }
            for (int j = 0; j < this.n2; j++) {
                I2[j] = calculaU(this.w2, Y1, j);
            }
            for (int j = 0; j < this.n2; j++) {
                Y2[j] = 1 / (Math.exp(-I2[j]) + 1);
                //System.out.println("Y2["+j+"]: " + Y2[j]);
            }
            for (int j = 0; j < this.n2; j++) {
                del2[j]=((double) valEsperado[i][j] - Y2[j]) * Y2[j] * (1 - Y2[j]);
                //del2[j] = ((double) valEsperado[i][j] - Y2[j]) * Math.exp(-I2[j]) / Math.pow((Math.exp(-I2[j]) + 1), 2);
            }
            //System.out.println(del2.length);
            this.w2 = ajustaPeso(this.w2, del2, w2Ant, Y1);
            //del1 = calculaDelta(del2, Y1, this.w2);
            //this.w1 = ajustaPeso(this.w1, del1, w1Ant, dados[i]);
            erro[i - (limSup - limInf)] = calculaErro(valEsperado[i], Y2);
            erroTotal += erro[i - (limSup - limInf)];
            //System.out.println(erro[i - (limSup - limInf)]);
        }
        double erroMedio = erroTotal / (2*nTreina);
        
        //imprimePesos(w1, n1, ent);
        return erroMedio;
    }
    
    public double[][] sorteiaPesos(int n, int m) {
        double[][] weight = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                weight[i][j] = Math.random();
            }
        }
        return weight;
    }
    
    public double[][] copia(double[][] w, double[][] wA) {
        int m = w[0].length;
        for (int i = 0; i < w.length; i++) {            
            System.arraycopy(wA[i], 0, w[i], 0, m);
        }
        return w;
    }
    
    public void imprimePesos(double[][] w) {
        int n=this.w1.length;
        int m=this.w1[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(w[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }    
    
    public double[] normaliza(String nome,int incremento) throws IOException {
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

    public void escreve(double acertos, int epocas,long tempo) throws IOException{
        FileWriter fw = new FileWriter("Saida2.txt",true);
        PrintWriter pw = new PrintWriter(fw);    
        pw.println("Rede Neural");
        pw.println("Acertos: " + acertos + " %");
        pw.println("Épocas: " + epocas);
        pw.println("Tempo: " + tempo + " milisegundos");
        pw.println("Tipo :" + this.tipo);
        pw.println("Neurônios camada escondida: " + this.n1);
        pw.println("Taxa de aprendizado: " + this.tA);
        pw.println("Momentum: " + this.mom);
        pw.close();
        fw.close();
    }
    
    public double[] juntaPesos(){
        int nPosicoes = this.w1.length * this.w1[0].length + this.w2.length * this.w2[0].length;
        double[] weight = new double[nPosicoes];
        int k = 0;
        for(int i=0;i<this.w1.length;i++){
            for(int j=0;j<this.w1[0].length;j++){
                weight[k]=this.w1[i][j];
                k++;
            }
        }
        for(int i=0;i<this.w2.length;i++){
            for(int j=0;j<this.w2[0].length;j++){
                weight[k]=this.w2[i][j];
                k++;
            }
        }
        return weight;
    }
}