package hn.optica.model;

public class Apartado {

    private int id;
    private int clienteId;
    private String clienteNombre;
    private int aroId;
    private String aroNombre;
    private String marca;
    private double precio;
    private String fecha;
    private String estado;

    public Apartado() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public int getAroId() {
        return aroId;
    }

    public void setAroId(int aroId) {
        this.aroId = aroId;
    }

    public String getAroNombre() {
        return aroNombre;
    }

    public void setAroNombre(String aroNombre) {
        this.aroNombre = aroNombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
