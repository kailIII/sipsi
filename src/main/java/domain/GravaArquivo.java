package domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class GravaArquivo {

    // Funcao que devolve o valor que ha no arquivo inscricao
    public static int devolveInscricao() {
        File arq = new File("C:/Relatorios/inscricao.txt");

        int inscricao = 0;

        try {
            //Indicamos o arquivo que ser� lido
            FileReader fileReader = new FileReader(arq);

            //Criamos o objeto bufferReader que nos
            // oferece o m�todo de leitura readLine()
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            inscricao = Integer.parseInt(bufferedReader.readLine());

            //liberamos o fluxo dos objetos ou fechamos o arquivo
            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (inscricao);
    }

    // Funcao que devolve o valor que ha no arquivo do ano
    public static int devolveAno() {
        File arq = new File("C:/Relatorios/ano.txt");

        int ano = 0;

        try {
            //Indicamos o arquivo que ser� lido
            FileReader fileReader = new FileReader(arq);

            //Criamos o objeto bufferReader que nos
            // oferece o m�todo de leitura readLine()
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            ano = Integer.parseInt(bufferedReader.readLine());

            //liberamos o fluxo dos objetos ou fechamos o arquivo
            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (ano);
    }

    // Fun��o que devolve o n�mero da inscri��o com base no valor dos arquivos de inscri��o e ano
    public static String realizaInscricao() {
        int ano = devolveAno();
        int inscricao = devolveInscricao();
        Calendar c = new GregorianCalendar();

        c.setTimeInMillis(System.currentTimeMillis());

        int anoAtual = c.get(Calendar.YEAR);
        String retorno = "-1";

        File arqInscricao = new File("C:/Relatorios/inscricao.txt");
        File arqAno = new File("C:/Relatorios/ano.txt");

        try {
            FileWriter writerInscricao = new FileWriter(arqInscricao);
            FileWriter writerAno = new FileWriter(arqAno);

            PrintWriter saidaInscricao = new PrintWriter(writerInscricao);
            PrintWriter saidaAno = new PrintWriter(writerAno);

            if (ano != anoAtual) {
                ano = anoAtual;
                inscricao = 0;
            }

            inscricao++;

            saidaAno.println(ano);
            saidaInscricao.println(inscricao);

            retorno = inscricao + "/" + ano;

            saidaInscricao.close();
            saidaAno.close();
            writerInscricao.close();
            writerAno.close();
        } catch (IOException e) {
            System.out.println("Erro ao acessar arquivo");
        }

        return (retorno);
    }
}
