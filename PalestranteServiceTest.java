


import services.PalestranteService;

import entities.Palestrante;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PalestranteServiceTest {
    private PalestranteService palestranteService;

    @BeforeEach
    void setUp() {
        palestranteService = new PalestranteService();
    }

    @Test
    void cadastrarPalestrante() {
        Palestrante palestrante = palestranteService.cadastrarPalestrante("João Silva", 
                "PhD em Ciência da Computação", "Tecnologia");
        
        assertNotNull(palestrante);
        assertEquals(1, palestrante.getId());
        assertEquals("João Silva", palestrante.getNome());
    }

    @Test
    void buscarPalestrantePorId() {
        Palestrante palestrante = palestranteService.cadastrarPalestrante("João Silva", 
                "PhD em Ciência da Computação", "Tecnologia");
        
        assertTrue(palestranteService.buscarPalestrantePorId(palestrante.getId()).isPresent());
        assertFalse(palestranteService.buscarPalestrantePorId(999).isPresent());
    }

    @Test
    void atualizarPalestrante() {
        Palestrante palestrante = palestranteService.cadastrarPalestrante("João Silva", 
                "PhD em Ciência da Computação", "Tecnologia");
        
        assertTrue(palestranteService.atualizarPalestrante(palestrante.getId(), 
                "João Silva Jr.", "PhD em Engenharia de Software", "Desenvolvimento"));
        
        Palestrante atualizado = palestranteService.buscarPalestrantePorId(palestrante.getId()).get();
        assertEquals("João Silva Jr.", atualizado.getNome());
        assertEquals("Desenvolvimento", atualizado.getAreaAtuacao());
    }

    @Test
    void removerPalestrante() {
        Palestrante palestrante = palestranteService.cadastrarPalestrante("João Silva", 
                "PhD em Ciência da Computação", "Tecnologia");
        
        assertTrue(palestranteService.removerPalestrante(palestrante.getId()));
        assertFalse(palestranteService.removerPalestrante(999));
    }
}