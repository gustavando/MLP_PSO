/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mlp_pso;

/**
 *
 * @author Asus
 */
public class MLP_PSO {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) { 
        GeneticAlg ag= new GeneticAlg(5, 3, 4, 150, "iris.data", 0.005, 100, 600);
        ag.validacaoCruzada();
        
        ValidacaoCruzada vc=new ValidacaoCruzada();
        vc.fazValidacao();
        
        PSO psoIris2 = new PSO(5, 3, 4, 150, 's', "iris.data");
        psoIris2.validacaoCruzada();
            
        BackPropagation iris2 = new BackPropagation(5, 3, 4, 0.7, 0, 150, 's', "iris.data",0.000001);
        iris2.validacaoCruzada();
        
        PSO psoBC2 = new PSO(6, 2, 31,569, 's', "wdbc.data");
        psoBC2.validacaoCruzada();
        
        BackPropagation bc2 = new BackPropagation(6, 2, 31, 0.5, 0, 569, 's', "wdbc.data",0.000001);
        bc2.validacaoCruzada();
        
        PSO psoWine2 = new PSO(5, 3, 13,78, 's', "wine.data");
        psoWine2.validacaoCruzada();
        
        BackPropagation wine2 = new BackPropagation(5, 3, 13, 0.4, 0, 178, 's', "wine.data",0.000001);
        wine2.validacaoCruzada();      
        
        PSO psoIris3 = new PSO(5, 3, 4, 150, 'r', "iris.data");
        psoIris3.validacaoCruzada();
        
        BackPropagation iris3 = new BackPropagation(5, 3, 4, 0.7, 0, 150, 'r', "iris.data",0.000001);
        iris3.validacaoCruzada();
        
        PSO psoBC3 = new PSO(6, 2, 31,569, 'r', "wdbc.data");
        psoBC3.validacaoCruzada();
        
        BackPropagation bc3 = new BackPropagation(6, 2, 31, 0.5, 0, 569, 'r', "wdbc.data",0.000001);
        bc3.validacaoCruzada();
        
        PSO psoWine3 = new PSO(5, 3, 13, 178, 'r', "wine.data");
        psoWine3.validacaoCruzada();
        
        BackPropagation wine3 = new BackPropagation(5, 3, 13, 0.4, 0.025, 178, 'r', "wine.data",0.000001);
        wine3.validacaoCruzada();
       
        PSO psoIris4 = new PSO(11, 3, 4, 150, 'r', "iris.data");
        psoIris4.validacaoCruzada();
        
        BackPropagation iris4 = new BackPropagation(11, 3, 4, 0.4, 0.03, 150, 'r', "iris.data",0.000001);
        iris4.validacaoCruzada();
        
        PSO psoBC4 = new PSO(7, 2, 31,569, 'r', "wdbc.data");
        psoBC4.validacaoCruzada();
        
        BackPropagation bc4 = new BackPropagation(7, 2, 31, 0.5, 0, 569, 'r', "wdbc.data",0.000001);
        bc4.validacaoCruzada();
        
        PSO psoWine4 = new PSO(8, 3, 13, 178, 'r', "wine.data");
        psoWine4.validacaoCruzada();
        
        BackPropagation wine4 = new BackPropagation(8, 3, 13, 0.2, 0, 178, 'r', "wine.data",0.000001);
        wine4.validacaoCruzada();
        
        
        
//-----------------------------------------------------------------------------------------------------
        
        /*
         * Parâmetros (n1, n2, nAtrib, tA, momentum, nExemplos, pesoFixo, nomeBanco, erro)
         * n1: Número de neurônios na camada intermediárias
         * n2: Número de neurônios na camada de saída
         * nAtrib: Número de atributos do problema
         * tA: taxa de aprendizado. Muda quanto os pesos variam a cada ajuste
         * Momentum: Considera o ajuste anterior dos pesos para realizar o ajuste atual
         * nExemplos: Número de exemplos no banco
         * pesoFixo: Determina se o peso é sorteado novamente ou mantido fixo após um cross-validation
         * erro: Diferença entre o erro anterior e o erro atual. Se for pequena, há convergência da rede
         */
       /* BackPropagation bc1 = new BackPropagation(16, 2, 31, 0.3, 0, 569, 's', "wdbc.data", 0.0000000001);
        bc1.validacaoCruzada();
        BackPropagation bc2 = new BackPropagation(7, 2, 31, 0.3, 0, 569, 's', "wdbc.data", 0.00001);
        bc2.validacaoCruzada();
        BackPropagation bc3 = new BackPropagation(7, 2, 31, 0.3, 0, 569, 's', "wdbc.data", 0.00001);
        bc3.validacaoCruzada();
        BackPropagation bc4 = new BackPropagation(12, 2, 31, 0.3, 0, 569, 's', "wdbc.data", 0.00001);
        bc4.validacaoCruzada();
        BackPropagation bc5 = new BackPropagation(12, 2, 31, 0.3, 0, 569, 's', "wdbc.data", 0.00001);
        bc5.validacaoCruzada();
        BackPropagation bc6 = new BackPropagation(12, 2, 31, 0.3, 0, 569, 's', "wdbc.data", 0.00001);
        bc6.validacaoCruzada();
        BackPropagation bc7 = new BackPropagation(7, 2, 31, 0.6, 0, 569, 's', "wdbc.data", 0.00001);
        bc7.validacaoCruzada();
        BackPropagation bc8 = new BackPropagation(7, 2, 31, 0.6, 0, 569, 's', "wdbc.data", 0.00001);
        bc8.validacaoCruzada();
        BackPropagation bc9 = new BackPropagation(7, 2, 31, 0.6, 0, 569, 's', "wdbc.data", 0.00001);
        bc9.validacaoCruzada();
        BackPropagation bc10 = new BackPropagation(7, 2, 31, 0.3, 0, 569, 's', "wdbc.data", 0.001);
        bc10.validacaoCruzada();
        BackPropagation bc11 = new BackPropagation(7, 2, 31, 0.3, 0, 569, 's', "wdbc.data", 0.001);
        bc11.validacaoCruzada();
        BackPropagation bc12 = new BackPropagation(7, 2, 31, 0.3, 0, 569, 's', "wdbc.data", 0.001);
        bc12.validacaoCruzada();*/
        
//-----------------------------------------------------------------------------------------------------
        /*PSO psoWine4 = new PSO(8, 3, 13, 178, 'r', "wine.data");
        psoWine4.validacaoCruzada();
        
        BackPropagation wine4 = new BackPropagation(8, 3, 13, 0.2, 0, 178, 'r', "wine.data");
        wine4.validacaoCruzada();  */
        
    }
}
