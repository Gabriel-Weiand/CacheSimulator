import java.io.FileInputStream;
import java.io.IOException;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

//input: cache_simulator <nsets> <bsize> <assoc> <substituição> <flag_saida> arquivo_de_entrada
public class cache_simulator {
    public static final int ADDRESS_SIZE = 32;//bits
    public static void main(String[] args) {
        args= new String[]{"1024", "4", "4", "r", "0", "vortex.in.sem.persons.bin"};//exemplo de input para nao ter q digitar no console toda vez
        /*
        offset=2bits
        sets=log2(1024/4)=8bits
        tag=32-10=22bits
        tag do 0 ao 21
        set do 22 ao 29
        off do 29 ao 31
         */
        LinkedList<String> inputs = new LinkedList<>();
        LinkedList<Address> addresses = new LinkedList<>();
        int[] dividers = addressDividers(args);//divisores do endereço
        try (FileInputStream fis = new FileInputStream(args[5])) {
            int byteRead;
            int byteCount= 0;
            String nova="";
            while ((byteRead = fis.read()) != -1) {//le todos arquivos
                nova=nova+ to8bitString(Integer.toBinaryString(byteRead));
                byteCount++;
                if(byteCount==4){//junta 4 bytes e separa do resto do input
                    System.out.println(nova);
                    byteCount=0;
                    inputs.add(nova);
                    addresses.add(calcAddress(nova,dividers));//adiciona o endereço já separado em uma linked list de addresses
                    calcAddress(nova,dividers).PrintAddress();
                    nova="";
                }
            }
            System.out.println("Número de sets: "+args[0]);//args são os comandos digitados depois do java Main
            System.out.println("Tamanho do bloco : "+args[1]);
            System.out.println("Associatividade: "+args[2]);
            System.out.println("Substituição: "+args[3]);
            System.out.println("Flag: "+args[4]);
            System.out.println("Arquivo: "+args[5]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("tag até: "+dividers[0]+"\n"+"indice até: "+dividers[1]);
    }
    public static int[] addressDividers(String[] args){
        int offset = log2(Integer.parseInt(args[1]));
        int index = log2(Integer.parseInt(args[0])/Integer.parseInt(args[2]));
        int tag= ADDRESS_SIZE-offset-index;
        return new int[]{tag,tag+index};
    }//retorna dois "divisores", que são os indices para separar posteriormente o endereço em tag, indice e offset

    public static Address calcAddress (String bitsaddress,int[] dividers){
        String tag=bitsaddress.substring(0,dividers[0]);
        String index=bitsaddress.substring(dividers[0],dividers[1]);
        String offset=bitsaddress.substring(dividers[1]);
        return new Address(tag,index,offset);
    }//função que separa o endereço em partes de acordo com <nsets> <bsize> <assoc>

    public static String to8bitString(String bits){
        while(bits.length()<8){
            bits="0"+bits;
        }
        return bits;
    }//funcao que transforma os n bits do byte em 8

    public static int log2(int N){
        //log2 N = loge N / loge 2
        int result = (int)(Math.log(N) / Math.log(2));
        return result;
    }//log base 2
} 