

import entities.Participante;
import services.ParticipanteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParticipanteServiceTest {
    private ParticipanteService participanteService;

    @BeforeEach
    void setUp() {
        participanteService = new ParticipanteService();
    }

    @Test
    void cadastrarParticipante() {
        Participante participante = participanteService.cadastrarParticipante("Maria Souza", 
                "maria@email.com", "12345678900");
        
        assertNotNull(participante);
        assertEquals(1, participante.getId());
        assertEquals("Maria Souza", participante.getNome());
    }

    @Test
    void buscarParticipantePorCpf() {
        Participante participante = participanteService.cadastrarParticipante("Maria Souza", 
                "maria@email.com", "12345678900");
        
        assertTrue(participanteService.buscarParticipantePorCpf("12345678900").isPresent());
        assertFalse(participanteService.buscarParticipantePorCpf("00000000000").isPresent());
    }

    @Test
    void atualizarParticipante() {
        Participante participante = participanteService.cadastrarParticipante("Maria Souza", 
                "maria@email.com", "12345678900");
        
        assertTrue(participanteService.atualizarParticipante(participante.getId(), 
                "Maria Souza Silva", "maria.silva@email.com", "12345678900"));
        
        Participante atualizado = participanteService.buscarParticipantePorId(participante.getId()).get();
        assertEquals("Maria Souza Silva", atualizado.getNome());
        assertEquals("maria.silva@email.com", atualizado.getEmail());
    }

    @Test
    void removerParticipante() {
        Participante participante = participanteService.cadastrarParticipante("Maria Souza", 
                "maria@email.com", "12345678900");
        
        assertTrue(participanteService.removerParticipante(participante.getId()));
        assertFalse(participanteService.removerParticipante(999));
    }
}