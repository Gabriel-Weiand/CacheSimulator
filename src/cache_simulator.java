import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

//input: cache_simulator <nsets> <bsize> <assoc> <substituição> <flag_saida> arquivo_de_entrada
public class cache_simulator {
    public static final int ADDRESS_SIZE = 32;//bits

    public static void main(String[] args) {
        //cache_simulator 1 4 32 R 1 vortex.in.sem.persons.bin
        //args = new String[]{"8192", "16", "32", "R", "1", "src/vortex.in.sem.persons.bin"};//exemplo de input para nao ter q digitar no console toda vez
        int accesses = 0;//acessos a memória
        int hits = 0;
        int misses = 0;
        int missesComp = 0;
        int missesConflict = 0;
        int missesCap = 0;
        LinkedList<LineIndex> cache = new LinkedList<>();//nossa cache
        for (int aux = 0; aux < Integer.parseInt(args[0]); aux++) {//cria todas linhas
            LineIndex novaLinha = new LineIndex();
            LinkedList<TagVal> novasInfos = new LinkedList<>();
            for (int aux2 = 0; aux2 < Integer.parseInt(args[2]); aux2++) {//inicializa os Tagvals
                novasInfos.add(new TagVal());
            }
            novaLinha.setTagVals(novasInfos);//seta
            cache.add(novaLinha);//adiciona a linha na cache
        }
        int[] dividers = addressDividers(args);//divisores do endereço
        try (FileInputStream fis = new FileInputStream(args[5])) {
            int byteRead;
            int byteCount = 0;
            String nova = "";
            while ((byteRead = fis.read()) != -1) {//le todos arquivos
                nova = nova + to8bitString(Integer.toBinaryString(byteRead));
                byteCount++;
                if (byteCount == 4) {//junta 4 bytes e separa do resto do input
                    accesses++;
                    byteCount = 0;
                    Address actualAddress = calcAddress(nova, dividers);
                    String tag = actualAddress.getTag();//guarda a tag do endereço
                    int index = 0;
                    if (!actualAddress.getIndex().equals("")) {
                        index = Integer.parseInt(actualAddress.getIndex(), 2);
                    }
                    boolean achoutag = false;
                    int vals = 0;//bits de validade positivos encontrados na linha
                    TagVal atual;
                    TagVal hit = new TagVal();
                    for (int percorrerLinha = 0; percorrerLinha < Integer.parseInt(args[2]); percorrerLinha++) {//percorre linha
                        atual = cache.get(index).getTagVal(percorrerLinha);//procura tag
                        if (atual.getValid()) {
                            vals++;
                        }
                        if (atual.getTag().equals(tag)) {
                            hit = atual;
                            achoutag = true;
                        }
                    }
                    if (achoutag) {//hit
                        hits++;
                        if (args[3].equals("L")) {
                            if (vals == Integer.parseInt(args[2])) {
                                cache.get(index).getTagValLL().remove(hit);
                                cache.get(index).getTagValLL().addLast(hit);
                            }
                        }
                    } else {//miss
                        if (vals != Integer.parseInt(args[2])) {//miss compulsório
                            missesComp++;
                            cache.get(index).getTagVal(vals).setValid(true);
                            cache.get(index).getTagVal(vals).setTag(tag);
                        } else {
                            if (missesComp == (Integer.parseInt(args[0]) * Integer.parseInt(args[2]))) {//miss de capacidade só ocorrem depous que os misses compulsorios se igualam a quantidade total de TagInfos
                                missesCap++;
                            } else {//miss de conflito
                                missesConflict++;
                            }//tratamento da falta
                            switch (args[3]) {
                                case "R":
                                    random(cache.get(index), tag);
                                    break;
                                case "L":
                                    fifo(cache.get(index), tag);
                                    break;
                                case "F":
                                    fifo(cache.get(index), tag);
                                    break;
                            }
                        }
                    }
                    nova = "";
                }
            }
        } catch (IOException e) {
            System.out.println("Arquivo não encontrado!");
        }
        misses = missesCap + missesConflict + missesComp;
        //formatação dos números
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
        nf.setGroupingUsed(false);//não usa ponto para separar os milhares
        DecimalFormat numFrmt = (DecimalFormat)nf;
        numFrmt.setMaximumFractionDigits(2); //seta o maximo de digitos após a virgula
        //calculo dos ratios
        double hitRate = (double) hits / accesses;
        double missRate = (double) misses / accesses;
        double compMissRate = (double) missesComp / misses;
        double capMissRate = (double) missesCap / misses;
        double confMissRate = (double) missesConflict / misses;
        if (args[4].equals("1")) {
            System.out.print(numFrmt.format(accesses) + " " + numFrmt.format(hitRate) + " " + numFrmt.format(missRate) + " " + numFrmt.format(compMissRate) + " " + numFrmt.format(capMissRate) + " " + numFrmt.format(confMissRate));
        } else {//formato livre
            System.out.println("Acessos a memória: " + accesses);
            switch (args[3]) {
                case "R":
                    System.out.println("Política de substituição: Random");
                    break;
                case "L":
                    System.out.println("Política de substituição: Last Recently Used");
                    break;
                case "F":
                    System.out.println("Política de substituição: First In First Out");
                    break;
            }
            System.out.println("Número de sets: " + args[0]);
            System.out.println("Tamanho do bloco(em bytes): " + args[1]);
            System.out.println("Associatividade: " + args[2] + " vias");
            System.out.println("Hits: " + hits);
            System.out.println("Taxa de hits: " + hitRate);
            System.out.println("Misses: " + misses);
            System.out.println("Taxa de misses: " + missRate);
            System.out.println("Misses compulsórios: " + missesComp);
            System.out.println("Taxa de misses compulsórios: " + compMissRate);
            System.out.println("Misses de capacidade: " + missesCap);
            System.out.println("Taxa de misses de capacidade: " + capMissRate);
            System.out.println("Misses de conflito: " + missesConflict);
            System.out.println("Taxa de misses de conflito: " + confMissRate);
            System.out.println("Arquivo de entrada: " + args[5]);
            System.out.println("          _.-\"\"\"-._        \n" +
                    "         /  _   _  \\       \n" +
                    "        /  (9) (9)  \\      \n" +
                    "       /_,         ,_\\     \n" +
                    "       | \\         / |     \n" +
                    "_      \\  \\._____./  /  __ \n" +
                    "\\`\\     \\   \\___/   / _|  \\\n" +
                    " \\ `\\   /\\         /\\ \\   /\n" +
                    "  |  `\\/ /`'-----'`\\ \\/  / \n" +
                    "  |_|\\/ /           \\   /  \n" +
                    "  /    /|           |\\_/   \n" +
                    "  \\___/ |           | \\    \n" +
                    "   \\ .  |           |  \\   \n" +
                    "    \\|  |           |  |   \n" +
                    "     |  `.         .'  |   \n" +
                    "     \\    `-.___.-'    /   \n" +
                    "     `\\       |       /'   \n" +
                    "       `\\     |     /'     \n" +
                    "    .-.-.`\\   |   /'.-.-.  \n" +
                    "   (,(,(,`^   |   ^`,),),) \n" +
                    "    '-'-'-----`-----'-'-'  ");
            System.out.println("Trabalho orquestrado por Gabriel e Thiago");
            System.out.println("Abraços...");
        }
    }

    public static int[] addressDividers(String[] args) {
        int offset = log2(Integer.parseInt(args[1]));
        int index = log2(Integer.parseInt(args[0]));
        int tag = ADDRESS_SIZE - offset - index;
        return new int[]{tag, tag + index};
    }//retorna dois "divisores", que são os indices para separar posteriormente o endereço em tag, indice e offset

    public static Address calcAddress(String bitsaddress, int[] dividers) {
        String tag = bitsaddress.substring(0, dividers[0]);
        String index = bitsaddress.substring(dividers[0], dividers[1]);
        String offset = bitsaddress.substring(dividers[1]);
        return new Address(tag, index, offset);
    }//função que separa o endereço em partes de acordo com <nsets> <bsize> <assoc>

    public static String to8bitString(String bits) {
        while (bits.length() < 8) {
            bits = "0" + bits;
        }
        return bits;
    }//funcao que transforma os n bits do byte em 8

    public static int log2(int N) {
        //log2 N = loge N / loge 2
        int result = (int) (Math.log(N) / Math.log(2));
        return result;
    }//log base 2

    public static void random(LineIndex linha, String tag) {//substituição random
        Random r = new Random();
        int numero = r.nextInt(linha.linkedListSize());
        linha.getTagVal(numero).setTag(tag);
    }//substituição random

    public static void fifo(LineIndex linha, String tag) {
        TagVal att = linha.getTagValLL().pollFirst();
        att.setTag(tag);
        linha.getTagValLL().addLast(att);
    }//substituição fifo
} 