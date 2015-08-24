package demoiselle.xDemo.util;

import java.math.BigDecimal;
import java.net.URL;

public class Util {

	private static final String NOME_ARQUIVO_CONFIGURACAO = "demoiselle.properties";
	
	private static String dirCorrente;
	
	static {


		URL url = Util.class.getClassLoader().getResource(
				NOME_ARQUIVO_CONFIGURACAO);
		if (url != null) {
			dirCorrente = url.toString();
			if (dirCorrente.startsWith("file:\\")) { 
				dirCorrente = dirCorrente.substring("file:\\".length(), dirCorrente.length());
			}
			if (dirCorrente.startsWith("vfs:/")){
				dirCorrente = dirCorrente.substring("vfs:/".length(), dirCorrente.length());
			}
			try {
				if(dirCorrente.indexOf("WEB-INF") != -1) { 
					dirCorrente = dirCorrente.substring(0, dirCorrente.indexOf("WEB-INF"));
				}
				if(dirCorrente.endsWith("/")) { 
					dirCorrente = dirCorrente.substring(0, dirCorrente.length() - 1);
				}
				int local = dirCorrente.indexOf(":");
				if (local == -1) {
					// SE NAO TEM : É PQ EH LINUX
					dirCorrente = "/" + dirCorrente;
				}
			} catch (Exception e) {
			}
		} else {
			throw new RuntimeException("O caminho do arquivo de configuração principal do sistema não foi encontrado: " + NOME_ARQUIVO_CONFIGURACAO);
		}
	}

	public static String dirCorrente() {
		if (dirCorrente.indexOf(NOME_ARQUIVO_CONFIGURACAO) != -1) {
			return dirCorrente.substring(0,dirCorrente.indexOf(NOME_ARQUIVO_CONFIGURACAO));
		} else {
			return dirCorrente;	
		}
	}
	  public static String formataValorParaString(Double bd, int quantidadeCasasDecimais){
		  return formataValorParaString(new BigDecimal(bd), quantidadeCasasDecimais);
	  }

	  public static String formataValorParaString(BigDecimal bd, int quantidadeCasasDecimais){
		  bd = bd.setScale(quantidadeCasasDecimais,BigDecimal.ROUND_UP);
		  if (bd == null) {
			  return "";
		  }
		  if (quantidadeCasasDecimais == 0) {
			  java.text.DecimalFormat df = new java.text.DecimalFormat( "#,##0" );  
			  return df.format( bd );  
		  } else {
			  java.text.DecimalFormat df = new java.text.DecimalFormat( "#,##0.00" );  
			  return df.format( bd );  
		  }
	  }

}
