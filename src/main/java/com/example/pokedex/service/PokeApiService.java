package com.example.pokedex.service;

import com.example.pokedex.model.PokemonModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PokeApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://pokeapi.co/api/v2";

    // ENDPOINT 1: Obtener lista de Pokémon (?limit=9 para mostrar una cuadrícula de 3x3 inicial)
    public List<PokemonModel> obtenerListaPokemon(int limite, int offset) {
        String url = BASE_URL + "/pokemon?limit=" + limite + "&offset=" + offset;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<PokemonModel> lista = new ArrayList<>();

        if (response != null && response.containsKey("results")) {
            List<Map<String, String>> results = (List<Map<String, String>>) response.get("results");
            for (Map<String, String> item : results) {
                String name = item.get("name");
                PokemonModel poke = obtenerPokemonPorNombreOId(name);
                if (poke != null) {
                    lista.add(poke);
                }
            }
        }
        return lista;
    }

    // ENDPOINT 2: Obtener datos principales, imagen, tipos y stats (/pokemon/{nameOrId})
    public PokemonModel obtenerPokemonPorNombreOId(String nombreOId) {
        try {
            String url = BASE_URL + "/pokemon/" + nombreOId.toLowerCase().trim();
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null) return null;

            PokemonModel model = new PokemonModel();
            Integer id = (Integer) response.get("id");
            model.setId(id);
            model.setNombre((String) response.get("name"));

            // Altura en metros y peso en kg
            model.setAltura(((Number) response.get("height")).doubleValue() / 10.0);
            model.setPeso(((Number) response.get("weight")).doubleValue() / 10.0);

            // Obtener imagen oficial de alta calidad (official-artwork)
            Map<String, Object> sprites = (Map<String, Object>) response.get("sprites");
            model.setImagenPixel((String) sprites.get("front_default"));
            Map<String, Object> other = (Map<String, Object>) sprites.get("other");
            Map<String, Object> artwork = (Map<String, Object>) other.get("official-artwork");
            model.setImagen((String) artwork.get("front_default"));

            // Obtener Tipos
            List<Map<String, Object>> typesList = (List<Map<String, Object>>) response.get("types");
            List<String> tipos = new ArrayList<>();
            for (Map<String, Object> typeEntry : typesList) {
                Map<String, String> type = (Map<String, String>) typeEntry.get("type");
                tipos.add(type.get("name"));
            }
            model.setTipos(tipos);

            // Obtener Estadísticas Base
            List<Map<String, Object>> statsList = (List<Map<String, Object>>) response.get("stats");
            Map<String, Integer> stats = new LinkedHashMap<>();
            for (Map<String, Object> statEntry : statsList) {
                Integer baseStat = (Integer) statEntry.get("base_stat");
                Map<String, String> stat = (Map<String, String>) statEntry.get("stat");
                stats.put(stat.get("name"), baseStat);
            }
            model.setEstadisticas(stats);

            // Llamamos al ENDPOINT 3 para anexar la descripción
            model.setDescripcion(obtenerDescripcionEspecie(id));

            return model;
        } catch (Exception e) {
            // Si el usuario busca un Pokémon que no existe
            return null;
        }
    }

    // ENDPOINT 3: Obtener la descripción en español (/pokemon-species/{id})
    public String obtenerDescripcionEspecie(int id) {
        try {
            String url = BASE_URL + "/pokemon-species/" + id;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("flavor_text_entries")) {
                List<Map<String, Object>> entries = (List<Map<String, Object>>) response.get("flavor_text_entries");
                for (Map<String, Object> entry : entries) {
                    Map<String, String> language = (Map<String, String>) entry.get("language");
                    if ("es".equals(language.get("name"))) {
                        String texto = (String) entry.get("flavor_text");
                        // Limpiar saltos de línea raros que trae la API
                        return texto.replace("\n", " ").replace("\f", " ");
                    }
                }
            }
        } catch (Exception e) {
            return "Descripción no disponible.";
        }
        return "Sin descripción.";
    }
}