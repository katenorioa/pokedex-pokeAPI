package com.example.pokedex.model;

import java.util.List;
import java.util.Map;

public class PokemonModel {
    private Integer id;
    private String nombre;
    private String imagen;      // Arte HD
    private String imagenPixel; // Sprite Pixel Art
    private String descripcion;
    private Double altura;
    private Double peso;
    private List<String> tipos;
    private Map<String, Integer> estadisticas;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getImagenPixel() { return imagenPixel; }
    public void setImagenPixel(String imagenPixel) { this.imagenPixel = imagenPixel; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public List<String> getTipos() { return tipos; }
    public void setTipos(List<String> tipos) { this.tipos = tipos; }

    public Map<String, Integer> getEstadisticas() { return estadisticas; }
    public void setEstadisticas(Map<String, Integer> estadisticas) { this.estadisticas = estadisticas; }
}