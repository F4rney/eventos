package services;

import entities.Evento;
import entities.Palestrante;
import entities.Participante;
import services.EventoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class EventoServiceTest {
    private EventoService eventoService;
    private Palestrante palestrante;
    private Participante participante;

    @BeforeEach
    void setUp() {
        eventoService = new EventoService();
        palestrante = new Palestrante("João Silva", "PhD em Ciência da Computação", "Tecnologia");
        participante = new Participante("Maria Souza", "maria@email.com", "12345678900");
    }

    @Test
    void criarEvento() {
        Evento evento = eventoService.criarEvento("Workshop Java", "Workshop sobre Java moderno", 
                LocalDate.now().plusDays(10), "Sala 101", 50);
        
        assertNotNull(evento);
        assertEquals(1, evento.getId());
        assertEquals("Workshop Java", evento.getNome());
        assertEquals(50, evento.getCapacidade());
    }

    @Test
    void buscarEventoPorId() {
        Evento evento = eventoService.criarEvento("Workshop Java", "Workshop sobre Java moderno", 
                LocalDate.now().plusDays(10), "Sala 101", 50);
        
        assertTrue(eventoService.buscarEventoPorId(evento.getId()).isPresent());
        assertFalse(eventoService.buscarEventoPorId(999).isPresent());
    }

    @Test
    void adicionarPalestranteAoEvento() {
        Evento evento = eventoService.criarEvento("Workshop Java", "Workshop sobre Java moderno", 
                LocalDate.now().plusDays(10), "Sala 101", 50);
        
        assertTrue(eventoService.adicionarPalestranteAoEvento(evento.getId(), palestrante));
        assertFalse(eventoService.adicionarPalestranteAoEvento(999, palestrante));
    }

    @Test
    void inscreverParticipante() {
        Evento evento = eventoService.criarEvento("Workshop Java", "Workshop sobre Java moderno", 
                LocalDate.now().plusDays(10), "Sala 101", 1);
        
        assertTrue(eventoService.inscreverParticipante(evento.getId(), participante));
        assertFalse(eventoService.inscreverParticipante(evento.getId(), participante)); // Duplicado
    }

    @Test
    void cancelarEvento() {
        Evento evento = eventoService.criarEvento("Workshop Java", "Workshop sobre Java moderno", 
                LocalDate.now().plusDays(10), "Sala 101", 50);
        
        assertTrue(eventoService.cancelarEvento(evento.getId()));
        assertFalse(eventoService.cancelarEvento(999));
    }
}