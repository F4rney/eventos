import entities.Evento;
import entities.Palestrante;
import entities.Participante;
import services.EventoService;
import services.PalestranteService;
import services.ParticipanteService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EventoService eventoService = new EventoService();
    private static final PalestranteService palestranteService = new PalestranteService();
    private static final ParticipanteService participanteService = new ParticipanteService();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    menuEventos();
                    break;
                case 2:
                    menuPalestrantes();
                    break;
                case 3:
                    menuParticipantes();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n===== SISTEMA DE GESTÃO DE EVENTOS =====");
        System.out.println("1. Gerenciar Eventos");
        System.out.println("2. Gerenciar Palestrantes");
        System.out.println("3. Gerenciar Participantes");
        System.out.println("0. Sair");
    }

    private static void menuEventos() {
        int opcao;
        do {
            System.out.println("\n===== MENU DE EVENTOS =====");
            System.out.println("1. Cadastrar novo evento");
            System.out.println("2. Listar todos os eventos");
            System.out.println("3. Buscar evento por ID");
            System.out.println("4. Atualizar evento");
            System.out.println("5. Cancelar evento");
            System.out.println("6. Adicionar palestrante ao evento");
            System.out.println("7. Remover palestrante do evento");
            System.out.println("8. Listar eventos futuros");
            System.out.println("0. Voltar ao menu principal");
            
            opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    cadastrarEvento();
                    break;
                case 2:
                    listarEventos();
                    break;
                case 3:
                    buscarEventoPorId();
                    break;
                case 4:
                    atualizarEvento();
                    break;
                case 5:
                    cancelarEvento();
                    break;
                case 6:
                    adicionarPalestranteAoEvento();
                    break;
                case 7:
                    removerPalestranteDoEvento();
                    break;
                case 8:
                    listarEventosFuturos();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarEvento() {
        System.out.println("\n--- Cadastro de Evento ---");
        String nome = lerString("Nome do evento: ");
        String descricao = lerString("Descrição: ");
        LocalDate data = lerData("Data do evento (dd/mm/aaaa): ");
        String local = lerString("Local: ");
        int capacidade = lerInteiro("Capacidade máxima: ");

        Evento evento = eventoService.criarEvento(nome, descricao, data, local, capacidade);
        System.out.println("Evento cadastrado com sucesso! ID: " + evento.getId());
    }

    private static void listarEventos() {
        List<Evento> eventos = eventoService.listarEventos();
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
        } else {
            System.out.println("\n--- Lista de Eventos ---");
            eventos.forEach(System.out::println);
        }
    }

    private static void buscarEventoPorId() {
        int id = lerInteiro("Digite o ID do evento: ");
        eventoService.buscarEventoPorId(id).ifPresentOrElse(
            evento -> {
                System.out.println("\n--- Detalhes do Evento ---");
                System.out.println("ID: " + evento.getId());
                System.out.println("Nome: " + evento.getNome());
                System.out.println("Descrição: " + evento.getDescricao());
                System.out.println("Data: " + evento.getData().format(dateFormatter));
                System.out.println("Local: " + evento.getLocal());
                System.out.println("Capacidade: " + evento.getCapacidade());
                System.out.println("Vagas disponíveis: " + (evento.getCapacidade() - evento.getParticipantes().size()));
                
                System.out.println("\nPalestrantes:");
                if (evento.getPalestrantes().isEmpty()) {
                    System.out.println("Nenhum palestrante associado.");
                } else {
                    evento.getPalestrantes().forEach(p -> System.out.println("- " + p.getNome()));
                }
            },
            () -> System.out.println("Evento não encontrado.")
        );
    }

    private static void atualizarEvento() {
        int id = lerInteiro("Digite o ID do evento a ser atualizado: ");
        if (eventoService.buscarEventoPorId(id).isPresent()) {
            String nome = lerString("Novo nome do evento: ");
            String descricao = lerString("Nova descrição: ");
            LocalDate data = lerData("Nova data do evento (dd/mm/aaaa): ");
            String local = lerString("Novo local: ");
            int capacidade = lerInteiro("Nova capacidade máxima: ");

            if (eventoService.atualizarEvento(id, nome, descricao, data, local, capacidade)) {
                System.out.println("Evento atualizado com sucesso!");
            } else {
                System.out.println("Falha ao atualizar evento.");
            }
        } else {
            System.out.println("Evento não encontrado.");
        }
    }

    private static void cancelarEvento() {
        int id = lerInteiro("Digite o ID do evento a ser cancelado: ");
        if (eventoService.cancelarEvento(id)) {
            System.out.println("Evento cancelado com sucesso!");
        } else {
            System.out.println("Falha ao cancelar evento ou evento não encontrado.");
        }
    }

    private static void adicionarPalestranteAoEvento() {
        int eventoId = lerInteiro("Digite o ID do evento: ");
        int palestranteId = lerInteiro("Digite o ID do palestrante: ");
        
        var eventoOpt = eventoService.buscarEventoPorId(eventoId);
        var palestranteOpt = palestranteService.buscarPalestrantePorId(palestranteId);
        
        if (eventoOpt.isPresent() && palestranteOpt.isPresent()) {
            if (eventoService.adicionarPalestranteAoEvento(eventoId, palestranteOpt.get())) {
                System.out.println("Palestrante adicionado ao evento com sucesso!");
            } else {
                System.out.println("Palestrante já está associado a este evento.");
            }
        } else {
            System.out.println("Evento ou palestrante não encontrado.");
        }
    }

    private static void removerPalestranteDoEvento() {
        int eventoId = lerInteiro("Digite o ID do evento: ");
        int palestranteId = lerInteiro("Digite o ID do palestrante: ");
        
        var eventoOpt = eventoService.buscarEventoPorId(eventoId);
        var palestranteOpt = palestranteService.buscarPalestrantePorId(palestranteId);
        
        if (eventoOpt.isPresent() && palestranteOpt.isPresent()) {
            if (eventoService.removerPalestranteDoEvento(eventoId, palestranteOpt.get())) {
                System.out.println("Palestrante removido do evento com sucesso!");
            } else {
                System.out.println("Palestrante não estava associado a este evento.");
            }
        } else {
            System.out.println("Evento ou palestrante não encontrado.");
        }
    }

    private static void listarEventosFuturos() {
        List<Evento> eventos = eventoService.listarEventosFuturos();
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento futuro cadastrado.");
        } else {
            System.out.println("\n--- Eventos Futuros ---");
            eventos.forEach(System.out::println);
        }
    }

    private static void menuPalestrantes() {
        int opcao;
        do {
            System.out.println("\n===== MENU DE PALESTRANTES =====");
            System.out.println("1. Cadastrar novo palestrante");
            System.out.println("2. Listar todos os palestrantes");
            System.out.println("3. Buscar palestrante por ID");
            System.out.println("4. Atualizar palestrante");
            System.out.println("5. Remover palestrante");
            System.out.println("0. Voltar ao menu principal");
            
            opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    cadastrarPalestrante();
                    break;
                case 2:
                    listarPalestrantes();
                    break;
                case 3:
                    buscarPalestrantePorId();
                    break;
                case 4:
                    atualizarPalestrante();
                    break;
                case 5:
                    removerPalestrante();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarPalestrante() {
        System.out.println("\n--- Cadastro de Palestrante ---");
        String nome = lerString("Nome: ");
        String curriculo = lerString("Currículo: ");
        String areaAtuacao = lerString("Área de atuação: ");

        Palestrante palestrante = palestranteService.cadastrarPalestrante(nome, curriculo, areaAtuacao);
        System.out.println("Palestrante cadastrado com sucesso! ID: " + palestrante.getId());
    }

    private static void listarPalestrantes() {
        List<Palestrante> palestrantes = palestranteService.listarPalestrantes();
        if (palestrantes.isEmpty()) {
            System.out.println("Nenhum palestrante cadastrado.");
        } else {
            System.out.println("\n--- Lista de Palestrantes ---");
            palestrantes.forEach(System.out::println);
        }
    }

    private static void buscarPalestrantePorId() {
        int id = lerInteiro("Digite o ID do palestrante: ");
        palestranteService.buscarPalestrantePorId(id).ifPresentOrElse(
            palestrante -> {
                System.out.println("\n--- Detalhes do Palestrante ---");
                System.out.println("ID: " + palestrante.getId());
                System.out.println("Nome: " + palestrante.getNome());
                System.out.println("Área de atuação: " + palestrante.getAreaAtuacao());
                System.out.println("Currículo: " + palestrante.getCurriculo());
                
                System.out.println("\nEventos associados:");
                if (palestrante.getEventos().isEmpty()) {
                    System.out.println("Nenhum evento associado.");
                } else {
                    palestrante.getEventos().forEach(e -> System.out.println("- " + e.getNome()));
                }
            },
            () -> System.out.println("Palestrante não encontrado.")
        );
    }

    private static void atualizarPalestrante() {
        int id = lerInteiro("Digite o ID do palestrante a ser atualizado: ");
        if (palestranteService.buscarPalestrantePorId(id).isPresent()) {
            String nome = lerString("Novo nome: ");
            String curriculo = lerString("Novo currículo: ");
            String areaAtuacao = lerString("Nova área de atuação: ");

            if (palestranteService.atualizarPalestrante(id, nome, curriculo, areaAtuacao)) {
                System.out.println("Palestrante atualizado com sucesso!");
            } else {
                System.out.println("Falha ao atualizar palestrante.");
            }
        } else {
            System.out.println("Palestrante não encontrado.");
        }
    }

    private static void removerPalestrante() {
        int id = lerInteiro("Digite o ID do palestrante a ser removido: ");
        if (palestranteService.removerPalestrante(id)) {
            System.out.println("Palestrante removido com sucesso!");
        } else {
            System.out.println("Falha ao remover palestrante ou palestrante não encontrado.");
        }
    }

    private static void menuParticipantes() {
        int opcao;
        do {
            System.out.println("\n===== MENU DE PARTICIPANTES =====");
            System.out.println("1. Cadastrar novo participante");
            System.out.println("2. Listar todos os participantes");
            System.out.println("3. Buscar participante por CPF");
            System.out.println("4. Atualizar participante");
            System.out.println("5. Remover participante");
            System.out.println("6. Inscrever participante em evento");
            System.out.println("7. Cancelar inscrição em evento");
            System.out.println("8. Emitir certificado");
            System.out.println("0. Voltar ao menu principal");
            
            opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    cadastrarParticipante();
                    break;
                case 2:
                    listarParticipantes();
                    break;
                case 3:
                    buscarParticipantePorCpf();
                    break;
                case 4:
                    atualizarParticipante();
                    break;
                case 5:
                    removerParticipante();
                    break;
                case 6:
                    inscreverParticipanteEmEvento();
                    break;
                case 7:
                    cancelarInscricaoEmEvento();
                    break;
                case 8:
                    emitirCertificado();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarParticipante() {
        System.out.println("\n--- Cadastro de Participante ---");
        String nome = lerString("Nome: ");
        String email = lerString("Email: ");
        String cpf = lerString("CPF: ");

        Participante participante = participanteService.cadastrarParticipante(nome, email, cpf);
        System.out.println("Participante cadastrado com sucesso! ID: " + participante.getId());
    }

    private static void listarParticipantes() {
        List<Participante> participantes = participanteService.listarParticipantes();
        if (participantes.isEmpty()) {
            System.out.println("Nenhum participante cadastrado.");
        } else {
            System.out.println("\n--- Lista de Participantes ---");
            participantes.forEach(System.out::println);
        }
    }

    private static void buscarParticipantePorCpf() {
        String cpf = lerString("Digite o CPF do participante: ");
        participanteService.buscarParticipantePorCpf(cpf).ifPresentOrElse(
            participante -> {
                System.out.println("\n--- Detalhes do Participante ---");
                System.out.println("ID: " + participante.getId());
                System.out.println("Nome: " + participante.getNome());
                System.out.println("Email: " + participante.getEmail());
                System.out.println("CPF: " + participante.getCpf());
                
                System.out.println("\nEventos inscritos:");
                if (participante.getEventosInscritos().isEmpty()) {
                    System.out.println("Nenhum evento inscrito.");
                } else {
                    participante.getEventosInscritos().forEach(e -> System.out.println("- " + e.getNome()));
                }
            },
            () -> System.out.println("Participante não encontrado.")
        );
    }

    private static void atualizarParticipante() {
        int id = lerInteiro("Digite o ID do participante a ser atualizado: ");
        if (participanteService.buscarParticipantePorId(id).isPresent()) {
            String nome = lerString("Novo nome: ");
            String email = lerString("Novo email: ");
            String cpf = lerString("Novo CPF: ");

            if (participanteService.atualizarParticipante(id, nome, email, cpf)) {
                System.out.println("Participante atualizado com sucesso!");
            } else {
                System.out.println("Falha ao atualizar participante.");
            }
        } else {
            System.out.println("Participante não encontrado.");
        }
    }

    private static void removerParticipante() {
        int id = lerInteiro("Digite o ID do participante a ser removido: ");
        if (participanteService.removerParticipante(id)) {
            System.out.println("Participante removido com sucesso!");
        } else {
            System.out.println("Falha ao remover participante ou participante não encontrado.");
        }
    }

    private static void inscreverParticipanteEmEvento() {
        int participanteId = lerInteiro("Digite o ID do participante: ");
        int eventoId = lerInteiro("Digite o ID do evento: ");
        
        var participanteOpt = participanteService.buscarParticipantePorId(participanteId);
        var eventoOpt = eventoService.buscarEventoPorId(eventoId);
        
        if (participanteOpt.isPresent() && eventoOpt.isPresent()) {
            if (eventoService.inscreverParticipante(eventoId, participanteOpt.get())) {
                System.out.println("Participante inscrito no evento com sucesso!");
            } else {
                System.out.println("Participante já está inscrito neste evento ou evento está lotado.");
            }
        } else {
            System.out.println("Participante ou evento não encontrado.");
        }
    }

    private static void cancelarInscricaoEmEvento() {
        int participanteId = lerInteiro("Digite o ID do participante: ");
        int eventoId = lerInteiro("Digite o ID do evento: ");
        
        var participanteOpt = participanteService.buscarParticipantePorId(participanteId);
        var eventoOpt = eventoService.buscarEventoPorId(eventoId);
        
        if (participanteOpt.isPresent() && eventoOpt.isPresent()) {
            if (eventoService.cancelarInscricao(eventoId, participanteOpt.get())) {
                System.out.println("Inscrição cancelada com sucesso!");
            } else {
                System.out.println("Participante não estava inscrito neste evento.");
            }
        } else {
            System.out.println("Participante ou evento não encontrado.");
        }
    }

    private static void emitirCertificado() {
        int participanteId = lerInteiro("Digite o ID do participante: ");
        int eventoId = lerInteiro("Digite o ID do evento: ");
        
        String certificado = participanteService.emitirCertificado(participanteId, eventoId, eventoService);
        if (certificado != null) {
            System.out.println("\n--- CERTIFICADO ---");
            System.out.println(certificado);
            System.out.println("------------------");
        } else {
            System.out.println("Participante não está inscrito neste evento ou dados inválidos.");
        }
    }

    // Métodos auxiliares para entrada de dados
    private static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Por favor, digite um número inteiro válido.");
            } finally {
                scanner.nextLine(); // Limpar buffer
            }
        }
    }

    private static LocalDate lerData(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String dataStr = scanner.nextLine();
                return LocalDate.parse(dataStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Use dd/mm/aaaa.");
            }
        }
    }
}