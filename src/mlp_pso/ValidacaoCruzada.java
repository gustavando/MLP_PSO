package mlp_pso;


public class ValidacaoCruzada {

    public void fazValidacao() {
        
        double[] w;
        double[][] dados=null;
        int[][] valEsperado = new int[150][3];
        PSO psoIris = new PSO(5, 3, 4, 150, 'r', "iris.data");
        try{
            dados = psoIris.leDados("iris.data", 150, 4, valEsperado);
        } catch(Exception e){
            System.out.println("falha");
        }
        BackPropagation iris = new BackPropagation(5, 3, 4, 0.3, 0, 150, 'd', "iris.data", 0.000001);
        for(int i=0;i<10;i++){
            System.out.println("------------------------- PSO -------------------------");
            w = psoIris.validacao2(i, dados, valEsperado);
            System.out.println("------------------- BackPropagation -------------------");
            iris.validacao2(i, dados, valEsperado, w);
        }

        
        valEsperado=new int[569][2];
        PSO psoBC = new PSO(10, 2, 31, 569, 'r', "wdbc.data");
        try{
            dados = psoBC.leDados("wdbc.data", 569, 31, valEsperado);
        } catch(Exception e){
            System.out.println("falha");
        }
        BackPropagation bc = new BackPropagation(10, 2, 31, 0.5, 0, 569, 'd', "wdbc.data", 0.000001);
        for(int i=0;i<10;i++){
            System.out.println("------------------------- PSO -------------------------");
            w = psoBC.validacao2(i, dados, valEsperado);
            System.out.println("------------------- BackPropagation -------------------");
            bc.validacao2(i, dados, valEsperado, w);
        }
        
        
        valEsperado=new int[178][3];
        PSO psoWine = new PSO(5, 3, 13, 178, 'r', "wine.data");
        try{
            dados = psoWine.leDados("wine.data", 178, 13, valEsperado);
        } catch(Exception e){
            System.out.println("falha");
        }
        BackPropagation wine = new BackPropagation(5, 3, 13, 0.4, 0, 178, 'd', "wine.data", 0.000001);
        for(int i=0;i<10;i++){
            System.out.println("------------------------- PSO -------------------------");
            w = psoWine.validacao2(i, dados, valEsperado);
            System.out.println("------------------- BackPropagation -------------------");
            wine.validacao2(i, dados, valEsperado, w);
        }
        /*iris.validacaoCruzada();

        PSO psoBC = new PSO(10, 2, 31, 569, 'r', "wdbc.data");
        w = psoBC.validacaoCruzada();

        BackPropagation bc = new BackPropagation(10, 2, 31, 0.5, 0, 569, 'd', "wdbc.data", 0.000001);
        bc.validacaoCruzada();

        PSO psoWine = new PSO(5, 3, 13, 178, 'r', "wine.data", w);
        w = psoWine.validacaoCruzada();

        BackPropagation wine = new BackPropagation(5, 3, 13, 0.4, 0, 178, 'd', "wine.data", w, 0.000001);
        wine.validacaoCruzada();*/
    }
}
