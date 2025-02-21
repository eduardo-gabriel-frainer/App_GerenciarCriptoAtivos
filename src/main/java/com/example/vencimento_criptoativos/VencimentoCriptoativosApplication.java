package com.example.vencimento_criptoativos;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VencimentoCriptoativosApplication {

	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		// Fecha o processo que está usando a porta 8080 automaticamente
		closePortIfInUse(8080);

		// Inicia a aplicação Spring Boot
		SpringApplication.run(VencimentoCriptoativosApplication.class, args);

		// Aguarda um tempo para garantir que a aplicação tenha tempo de iniciar
		Thread.sleep(1000);

		// URL para abrir no navegador
		String url = "http://localhost:8080/cadastro";

		// Verifica o sistema operacional e usa o comando apropriado para abrir o
		// navegador
		openUrlInBrowser(url);
	}

	// Função para matar o processo que está utilizando a porta especificada
	private static void closePortIfInUse(int port) throws IOException, InterruptedException {
		String os = System.getProperty("os.name").toLowerCase();

		// Verifica se o sistema operacional é Windows
		if (os.contains("win")) {
			// Windows: Encontra o PID da porta e mata o processo
			String cmd = "netstat -ano | findstr :" + port;
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			String output = new String(process.getInputStream().readAllBytes());
			String pid = output.split(" ")[output.split(" ").length - 1];
			if (!pid.isEmpty()) {
				// Mata o processo que está usando a porta
				Runtime.getRuntime().exec("taskkill /PID " + pid + " /F");
			}
		}
	}

	// Função para abrir a URL no navegador de acordo com o sistema operacional
	private static void openUrlInBrowser(String url) throws IOException, URISyntaxException {
		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {
			// Windows
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
		} else if (os.contains("mac")) {
			// macOS
			Runtime.getRuntime().exec("open " + url);
		} else if (os.contains("nix") || os.contains("nux")) {
			// Linux
			Runtime.getRuntime().exec("xdg-open " + url);
		} else {
			// Caso o sistema operacional não seja reconhecido
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				URI uri = new URI(url);
				desktop.browse(uri);
			}
		}
	}
}
