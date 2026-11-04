package hn.optica.model;

public class Venta {

    private int id;
    private int clienteId;
    private String clienteNombre;
    private int medicionId;
    private int aroId;
    private String aroNombre;
    private int lunaId;
    private String lunaNombre;
    private int vendedorId;
    private String vendedorNombre;
    private int cantidad;
    private double precioAro;
    private double precioLuna;
    private double subtotal;
    private double impuesto;
    private double total;
    private String fecha;

    public Venta() {
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

    public int getMedicionId() {
        return medicionId;
    }

    public void setMedicionId(int medicionId) {
        this.medicionId = medicionId;
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

    public int getLunaId() {
        return lunaId;
    }

    public void setLunaId(int lunaId) {
        this.lunaId = lunaId;
    }

    public String getLunaNombre() {
        return lunaNombre;
    }

    public void setLunaNombre(String lunaNombre) {
        this.lunaNombre = lunaNombre;
    }

    public int getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(int vendedorId) {
        this.vendedorId = vendedorId;
    }

    public String getVendedorNombre() {
        return vendedorNombre;
    }

    public void setVendedorNombre(String vendedorNombre) {
        this.vendedorNombre = vendedorNombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioAro() {
        return precioAro;
    }

    public void setPrecioAro(double precioAro) {
        this.precioAro = precioAro;
    }

    public double getPrecioLuna() {
        return precioLuna;
    }

    public void setPrecioLuna(double precioLuna) {
        this.precioLuna = precioLuna;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
