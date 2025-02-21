package com.example.vencimento_criptoativos.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.vencimento_criptoativos.Model.Cripto;
import com.example.vencimento_criptoativos.Repository.CriptoRepository;
import com.example.vencimento_criptoativos.Services.CriptoService;
import org.springframework.ui.Model;

@Controller
public class ControllerVencimentoCripto {

    @Autowired
    private CriptoService criptoService;

    @Autowired
    private CriptoRepository criptoRepository;

    @GetMapping("/cadastro")
    public String exibirFormulario(Model model) {
        model.addAttribute("cripto", new Cripto()); // Certifique-se de que a classe CriptoAtivo existe
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarCripto(
            @RequestParam("codigo") String codigo,
            @RequestParam("datainicio") String datainicio,
            @RequestParam("taxa") Double taxa,
            @RequestParam("valor") Double valor,
            @RequestParam("vencimento") List<String> vencimentos,
            @RequestParam("porcentagem") List<Double> porcentagens,
            RedirectAttributes redirectAttributes) throws IOException {

        // Itera sobre os vencimentos e cadastra cada um separadamente
        // Supondo que 'valor' seja o valor total que você deseja dividir
        double valorTotal = valor; // Valor total que será dividido
        double somaPorcentagens = 0; // Variável para verificar a soma das porcentagens

        // Calculando a soma total das porcentagens
        for (Double porcentagem : porcentagens) {
            somaPorcentagens += porcentagem;
        }

        // Agora, dividimos o valor total de acordo com as porcentagens
        for (int i = 0; i < vencimentos.size(); i++) {
            Cripto cripto = new Cripto();

            // Calculando o valor para o vencimento atual, com base na porcentagem
            double valorVencimento = (valorTotal * (porcentagens.get(i) / 100.0));

            // Agora você pode definir os campos corretamente
            cripto.setCodigo(codigo);

            // Convertendo as datas de String para LocalDate, se necessário
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataInicioLocal = LocalDate.parse(datainicio, formatter);
            LocalDate vencimentoLocal = LocalDate.parse(vencimentos.get(i), formatter);

            cripto.setDatainicio(dataInicioLocal); // Define a data de início como LocalDate
            cripto.setTaxa(taxa);
            cripto.setValor(valorVencimento); // Define o valor calculado para o vencimento
            cripto.setVencimento(vencimentoLocal); // Define a data de vencimento como LocalDate
            cripto.setPorcentagem(porcentagens.get(i));

            // Salva o objeto cripto
            criptoService.salvarCripto(cripto);
        }

        redirectAttributes.addFlashAttribute("mensagem", "Cripto cadastrada com sucesso!");
        return "redirect:/cadastro";
    }

    // @GetMapping("/listar")
    // public String listar() {
    // return "listar";
    // }

    @GetMapping("/listar")
    public String listar(Model model) {
        // Obtendo a lista de criptos
        List<Cripto> criptos = criptoService.getTodosCripto();

        // Calculando o valor futuro para cada cripto na lista
        for (Cripto cripto : criptos) {
            Double valorFuturo = criptoService.calcularValorFuturo(cripto);
            cripto.setValorFuturo(valorFuturo);
        }

        // Ordenando as criptos por data de vencimento (do mais próximo ao mais
        // distante)
        criptos.sort(Comparator.comparing(Cripto::getVencimento));

        // Passando a lista ordenada para o modelo
        model.addAttribute("criptos", criptos);

        return "listar"; // Nome do template
    }

    @GetMapping("/pesquisar-por-data")
    public String pesquisarPorData(
            @RequestParam(value = "inicio", required = false) String dataInicio,
            @RequestParam(value = "fim", required = false) String dataFim,
            Model model) {

        List<Cripto> criptos;

        if (dataInicio == null || dataInicio.isEmpty() || dataFim == null || dataFim.isEmpty()) {
            criptos = criptoService.getTodosCripto();
        } else {
            // Convertendo as datas para LocalDate
            LocalDate inicio = LocalDate.parse(dataInicio);
            LocalDate fim = LocalDate.parse(dataFim);

            // Buscar criptoativos dentro do período
            criptos = criptoService.obterCriptoPorData(inicio, fim);
        }

        // Calcular o valor futuro para cada cripto
        for (Cripto cripto : criptos) {
            Double valorFuturo = criptoService.calcularValorFuturo(cripto);
            cripto.setValorFuturo(valorFuturo); // Adicionando o valor futuro ao objeto Cripto
        }

        // Adiciona os resultados ao modelo
        model.addAttribute("criptos", criptos);

        // Passar o valor futuro global se necessário
        Double valorFuturo = criptos.stream().mapToDouble(Cripto::getValorFuturo).sum();
        model.addAttribute("valorFuturo", valorFuturo);

        return "listar"; // Renderiza o template listar.html
    }

    @PostMapping("/deletar/{id}")
    public String deletarCripto(@PathVariable Long id) {

        criptoService.deletarPorId(id);

        return "redirect:/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarCriptoAtivo(@PathVariable Long id, Model model) {
        Cripto cripto = criptoService.buscarPorId(id); // Buscar o criptoativo pelo ID
        model.addAttribute("cripto", cripto);
        return "editar";
    }

    @PostMapping("/salvar")
    public String salvarCripto(@ModelAttribute Cripto cripto) {
        criptoService.salvar(cripto); // Se tiver ID, ele edita; se não, cria
        return "redirect:/listar";
    }

}
