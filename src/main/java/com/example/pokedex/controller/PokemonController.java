package com.example.pokedex.controller;

import com.example.pokedex.model.PokemonModel;
import com.example.pokedex.service.PokeApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PokemonController {

    private final PokeApiService pokeApiService;

    public PokemonController(PokeApiService pokeApiService) {
        this.pokeApiService = pokeApiService;
    }

    // Ruta principal y buscador
    @GetMapping("/")
    public String index(
            @RequestParam(name = "buscar", required = false) String buscar,
            @RequestParam(name = "pagina", defaultValue = "0") int pagina,
            Model model) {

        List<PokemonModel> pokemones = new ArrayList<>();
        int limite = 12; // 12 Pokémon (4 filas de 3 columnas) es el punto dulce de rendimiento

        if (buscar != null && !buscar.trim().isEmpty()) {
            PokemonModel encontrado = pokeApiService.obtenerPokemonPorNombreOId(buscar);
            if (encontrado != null) {
                pokemones.add(encontrado);
            } else {
                model.addAttribute("error", "No Pokémon found with name or ID: " + buscar);
            }
        } else {
            int offset = pagina * limite;
            pokemones = pokeApiService.obtenerListaPokemon(limite, offset);
        }

        model.addAttribute("pokemones", pokemones);
        model.addAttribute("buscar", buscar);
        model.addAttribute("paginaActual", pagina);
        return "index";
    }

    // Ruta para cuando se selecciona una tarjeta
    @GetMapping("/pokemon/{nombre}")
    public String detalle(@PathVariable String nombre, Model model) {
        PokemonModel pokemon = pokeApiService.obtenerPokemonPorNombreOId(nombre);
        if (pokemon == null) {
            return "redirect:/";
        }
        model.addAttribute("pokemon", pokemon);
        return "detalle";
    }
}