package jogoshannon.local;

import java.util.Arrays;

public class CalculaEntropiaComputador {
    
    public static void main(String[] args) {
        
        double dados[][] = {
                {
                    0.1786745199949133500000000000000000000000,
                    0.1200382967984781100000000000000000000000,
                    0.1046275563521344200000000000000000000000,
                    0.0879583004569121090000000000000000000000,
                    0.0632539346814442460000000000000000000000,
                    0.0544995214816681310000000000000000000000,
                    0.0513406135029885890000000000000000000000,
                    0.0421242964745616320000000000000000000000,
                    0.0405677519244444920000000000000000000000,
                    0.0392881845584423790000000000000000000000,
                    0.0375008355163886360000000000000000000000,
                    0.0345893317906268290000000000000000000000,
                    0.0272088657701753940000000000000000000000,
                    0.0234860966387371030000000000000000000000,
                    0.0207379369197878620000000000000000000000,
                    0.0143422819411318220000000000000000000000,
                    0.0108473688538537290000000000000000000000,
                    0.0099446720826310737000000000000000000000,
                    0.0098144391443474813000000000000000000000,
                    0.0085869708505543572000000000000000000000,
                    0.0083598814028160214000000000000000000000,
                    0.0036832009622790041000000000000000000000,
                    0.0036227577828628018000000000000000000000,
                    0.0026964734294289468000000000000000000000,
                    0.0019568553047189818000000000000000000000,
                    0.0001043455668067995100000000000000000000,
                    0.0000860651905931188940000000000000000000,
                    0.0000586446262725979730000000000000000000,  
                },
                
                
                
                
                {
                    0.2885634339816448000000000000000000000000,
                    0.1559576144120835300000000000000000000000,
                    0.1277425318692560500000000000000000000000,
                    0.0888101659884696220000000000000000000000,
                    0.0673593533653037420000000000000000000000,
                    0.0532300083225835520000000000000000000000,
                    0.0393950657903368860000000000000000000000,
                    0.0335617387715958640000000000000000000000,
                    0.0264727857827971110000000000000000000000,
                    0.0215674237327225060000000000000000000000,
                    0.0178684485748397540000000000000000000000,
                    0.0136953335300341110000000000000000000000,
                    0.0121709565451574970000000000000000000000,
                    0.0100087713372900150000000000000000000000,
                    0.0091614758997859147000000000000000000000,
                    0.0087643494687825868000000000000000000000,
                    0.0069262281560192370000000000000000000000,
                    0.0058317643414195191000000000000000000000,
                    0.0043147879604616690000000000000000000000,
                    0.0038151440002729088000000000000000000000,
                    0.0028219888512702991000000000000000000000,
                    0.0009145200468060189600000000000000000000,
                    0.0005257672074876658400000000000000000000,
                    0.0002977342564608820100000000000000000000,
                    0.0001165816250788599300000000000000000000,
                    0.0000545757683411658360000000000000000000,
                    0.0000347327148059931630000000000000000000,
                    0.0000134154373826204520000000000000000000,  
                },
                
                
                
                
                {
                    0.3747384828753146400000000000000000000000,
                    0.1677730999188908900000000000000000000000,
                    0.1111143904510782800000000000000000000000,
                    0.0797821644207920700000000000000000000000,
                    0.0579746355404296140000000000000000000000,
                    0.0418826620643996050000000000000000000000,
                    0.0324671168116064740000000000000000000000,
                    0.0254482547328123870000000000000000000000,
                    0.0203808587435578840000000000000000000000,
                    0.0167165157464290070000000000000000000000,
                    0.0137051676346993390000000000000000000000,
                    0.0113301556234205410000000000000000000000,
                    0.0094448234358970977000000000000000000000,
                    0.0079817693333823544000000000000000000000,
                    0.0067473681109990495000000000000000000000,
                    0.0057551237041566106000000000000000000000,
                    0.0050421571929434349000000000000000000000,
                    0.0039418518182503604000000000000000000000,
                    0.0028671095701947994000000000000000000000,
                    0.0023890466690405705000000000000000000000,
                    0.0018475329622070543000000000000000000000,
                    0.0003322616771374747100000000000000000000,
                    0.0001801212694678172500000000000000000000,
                    0.0000721782399176979170000000000000000000,
                    0.0000420450041350642340000000000000000000,
                    0.0000225556999742806120000000000000000000,
                    0.0000130616667824919110000000000000000000,
                    0.0000041868096684285557000000000000000000,
                },
                
                
                
                
                {
                    0.4484834219627851200000000000000000000000,
                    0.1776785579235415600000000000000000000000,
                    0.1028040896538065900000000000000000000000,
                    0.0699064188928101850000000000000000000000,
                    0.0460600413487146850000000000000000000000,
                    0.0319965307154953670000000000000000000000,
                    0.0243018272801448530000000000000000000000,
                    0.0188372855030617780000000000000000000000,
                    0.0148837742220639340000000000000000000000,
                    0.0121765800474803490000000000000000000000,
                    0.0099341527654544237000000000000000000000,
                    0.0082101839646016271000000000000000000000,
                    0.0071218162487494147000000000000000000000,
                    0.0058978190394147114000000000000000000000,
                    0.0050057013036972154000000000000000000000,
                    0.0043269641428347154000000000000000000000,
                    0.0036387918867734326000000000000000000000,
                    0.0029786595542560267000000000000000000000,
                    0.0022689634872725154000000000000000000000,
                    0.0017778078128308297000000000000000000000,
                    0.0013877845650511074000000000000000000000,
                    0.0001746966845399798000000000000000000000,
                    0.0000727681717206194460000000000000000000,
                    0.0000332881952482088060000000000000000000,
                    0.0000217891729746911950000000000000000000,
                    0.0000103786047186621290000000000000000000,
                    0.0000056020877742778514000000000000000000,
                    0.0000010024788648707729000000000000000000,
                },
                
                
                
                
                {
                    0.5175804267124203300000000000000000000000,
                    0.1743366037348813600000000000000000000000,
                    0.0886576955322958690000000000000000000000,
                    0.0561931948266555790000000000000000000000,
                    0.0360566302180827040000000000000000000000,
                    0.0254088542469038290000000000000000000000,
                    0.0190106886713465490000000000000000000000,
                    0.0151089101058019920000000000000000000000,
                    0.0120181985896551670000000000000000000000,
                    0.0101835677544532490000000000000000000000,
                    0.0084921444464772164000000000000000000000,
                    0.0070739565008240959000000000000000000000,
                    0.0060498914739332546000000000000000000000,
                    0.0050967078337936884000000000000000000000,
                    0.0043211699320802885000000000000000000000,
                    0.0037219509541492174000000000000000000000,
                    0.0031677552197993488000000000000000000000,
                    0.0025912395146629763000000000000000000000,
                    0.0019978585211648434000000000000000000000,
                    0.0015586533892816804000000000000000000000,
                    0.0011284116274369433000000000000000000000,
                    0.0001418512278121578900000000000000000000,
                    0.0000550480653347119030000000000000000000,
                    0.0000254748411618590830000000000000000000,
                    0.0000137693875261437700000000000000000000,
                    0.0000046291088685322753000000000000000000,
                    0.0000012383603342570425000000000000000000,
                    0.0000001769086191795774200000000000000000,
                },
                
                
                
                
                {
                    0.5645301823756014900000000000000000000000,
                    0.1639999619055802800000000000000000000000,
                    0.0783960751159828020000000000000000000000,
                    0.0485776160971807470000000000000000000000,
                    0.0314731497678332190000000000000000000000,
                    0.0220457172891335500000000000000000000000,
                    0.0166389887303863210000000000000000000000,
                    0.0132314815942358370000000000000000000000,
                    0.0107462021353362840000000000000000000000,
                    0.0091526040312361352000000000000000000000,
                    0.0076814565807463040000000000000000000000,
                    0.0064888231861209315000000000000000000000,
                    0.0055660647812606013000000000000000000000,
                    0.0046688697562894989000000000000000000000,
                    0.0039730268896386641000000000000000000000,
                    0.0033868087593525499000000000000000000000,
                    0.0028413387155796343000000000000000000000,
                    0.0023275354192127781000000000000000000000,
                    0.0017886994705668150000000000000000000000,
                    0.0013656505955368700000000000000000000000,
                    0.0009274170138829606900000000000000000000,
                    0.0001210648648503610400000000000000000000,
                    0.0000406301470442760840000000000000000000,
                    0.0000171601927284243030000000000000000000,
                    0.0000079019444178998609000000000000000000,
                    0.0000019460012372439995000000000000000000,
                    0.0000003243335395406664400000000000000000,
                    0.0000000000000000000000000000000000000000,
                },
                
                
                
                
                {
                    0.5958691092814152300000000000000000000000,
                    0.1569325741663836500000000000000000000000,
                    0.0725044124987082590000000000000000000000,
                    0.0441614890332020710000000000000000000000,
                    0.0287125469817126440000000000000000000000,
                    0.0200258931062173260000000000000000000000,
                    0.0151416792353775870000000000000000000000,
                    0.0120395661098797270000000000000000000000,
                    0.0097745900992865873000000000000000000000,
                    0.0082702966903922571000000000000000000000,
                    0.0069682701386505454000000000000000000000,
                    0.0059014156645535651000000000000000000000,
                    0.0050062521393184544000000000000000000000,
                    0.0041879559167937157000000000000000000000,
                    0.0035407904288033494000000000000000000000,
                    0.0029992695689722267000000000000000000000,
                    0.0024765896015200798000000000000000000000,
                    0.0020039161340283875000000000000000000000,
                    0.0015239009103513697000000000000000000000,
                    0.0011069835109168745000000000000000000000,
                    0.0007015357627115931900000000000000000000,
                    0.0001039050152480006300000000000000000000,
                    0.0000294554796347205390000000000000000000,
                    0.0000103492225743611320000000000000000000,
                    0.0000032138611413258271000000000000000000,
                    0.0000006781541857843489700000000000000000,
                    0.0000000589699291986390580000000000000000,
                    0.0000000000000000000000000000000000000000,
                },
                
                
                
                
                {
                    0.6270828137088170200000000000000000000000,
                    0.1506330781869758600000000000000000000000,
                    0.0673204703963595500000000000000000000000,
                    0.0399963686198071750000000000000000000000,
                    0.0257767846947030250000000000000000000000,
                    0.0179414832973585630000000000000000000000,
                    0.0135571430378518250000000000000000000000,
                    0.0107577133143299130000000000000000000000,
                    0.0087236568293822068000000000000000000000,
                    0.0073129145544453631000000000000000000000,
                    0.0061467213834739817000000000000000000000,
                    0.0051523376685920974000000000000000000000,
                    0.0043297044396757205000000000000000000000,
                    0.0036032219978381188000000000000000000000,
                    0.0029953284752372352000000000000000000000,
                    0.0024812269347575743000000000000000000000,
                    0.0020104094652474221000000000000000000000,
                    0.0015825222459632218000000000000000000000,
                    0.0011812010675124815000000000000000000000,
                    0.0008231639600312871300000000000000000000,
                    0.0004857958809899331500000000000000000000,
                    0.0000771918922330416680000000000000000000,
                    0.0000188999247216884090000000000000000000,
                    0.0000054252514021694914000000000000000000,
                    0.0000010024921069226242000000000000000000,
                    0.0000001179402478732498500000000000000000,
                    0.0000000000000000000000000000000000000000,
                    0.0000000000000000000000000000000000000000,
                }
                
                
        };
        
        entropiaMinima = new double[dados.length];
        entropiaMaxima = new double[dados.length];
        
        calculaEntropia(dados);
        
        System.out.println("Minima: " + Arrays.toString(entropiaMinima));
        System.out.println("Máxima: " + Arrays.toString(entropiaMaxima));
        
    }
    
    static double entropiaMaxima[];
    static double entropiaMinima[];
    
    public static void calculaEntropia(double tabelaDistribuida[][]) {
        
        double entropiaMax;
        double entropiaMin;

        for (int n = 0; n < tabelaDistribuida.length; n++) {
            entropiaMax = entropiaMin = 0;

            double atualInt;
            double atual;
            double proximo;
            int i;
            for (i = 0; i < tabelaDistribuida[n].length - 1; i++) {
                atualInt = tabelaDistribuida[n][i];
                atual = tabelaDistribuida[n][i];
                proximo = tabelaDistribuida[n][i + 1];

                entropiaMin += (i + 1) * (atual - proximo) * log2(i + 1);
                if (atualInt != 0.0d) {
                    entropiaMax += atual * log2(atual);
                }
            }

            atualInt = tabelaDistribuida[n][i];
            atual = tabelaDistribuida[n][i];

            entropiaMin += (i + 1) * (atual) * log2(i + 1);
            if (atualInt != 0.0d) {
                entropiaMax += atual * log2(atual);
            }

            entropiaMaxima[n] = -entropiaMax;
            entropiaMinima[n] = entropiaMin;
        }
    }

    private static final double LOG_2 = Math.log(2);

    private static double log2(double x) {
        return Math.log(x) / LOG_2;
    }
    
}
